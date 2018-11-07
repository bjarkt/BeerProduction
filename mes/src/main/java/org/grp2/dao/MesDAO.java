package org.grp2.dao;

import org.grp2.database.DatabaseConnection;
import org.grp2.domain.Plant;
import org.grp2.enums.OrderItemStatus;
import org.grp2.enums.OrderStatus;
import org.grp2.shared.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MesDAO extends DatabaseConnection {


    /**
     * Get a list of {@link Order} ready for production.
     * @return list of orders.
     */
    public List<Order> getLockedOrders() {
        List<Order> orders = new ArrayList<>();

        this.executeQuery(conn -> {
            try {
                String getOrderQuery = "SELECT date_created, status, order_number FROM Orders WHERE status = ?";
                PreparedStatement ps = conn.prepareStatement(getOrderQuery);
                ps.setString(1, OrderStatus.LOCKED.getStatus());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Timestamp dateCreated = rs.getTimestamp(1);
                    String status = rs.getString(2);
                    int orderNumber = rs.getBigDecimal(3).intValue();
                    orders.add(new Order(orderNumber, dateCreated, status));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return orders;
    }


    /**
     * Get map of {@link OrderItem} and its {@link Recipe} for an order.
     * @param orderNumber
     * @return  map of OrderItem and Recipe.
     */
    public Map<OrderItem, Recipe> getOrderItems(int orderNumber) {

        Map<OrderItem, Recipe> orderItems = new HashMap<>();

        this.executeQuery(conn -> {
            try {
                String getOrderQuery = "SELECT Order_items.quantity, Order_items.beer_name, Order_items.status, Recipes.max_speed, Recipes.min_speed, Recipes.ID, Recipes.name FROM Order_items, Recipes WHERE order_number = ? AND Order_items.beer_name = Recipes.name";
                PreparedStatement ps = conn.prepareStatement(getOrderQuery);
                ps.setInt(1, orderNumber);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int quantity = rs.getInt(1);
                    String beerName = rs.getString(2);
                    String status = rs.getString(3);
                    OrderItem orderItem = new OrderItem(orderNumber, beerName, quantity, status);

                    int maxSpeed = rs.getInt(4);
                    int minSpeed = rs.getInt(5);
                    int id = rs.getInt(6);
                    String name = rs.getString(7);
                    Recipe recipe = new Recipe(id, name, minSpeed, maxSpeed);

                    orderItems.put(orderItem, recipe);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return orderItems;

    }


    /**
     * Get list of all {@link Batch}.
     * @return list of batches.
     */
    public List<Batch> viewAllBatches() {
        List<Batch> batches = new ArrayList<>();
        this.executeQuery(conn -> {
            try {
                String getOrderQuery = "SELECT * FROM Batches";
                PreparedStatement ps = conn.prepareStatement(getOrderQuery);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    batches.add(batchFromResultSet(rs));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return batches;
    }


    /**
     *
     * @return
     */
    public void viewPlantStatistics() {

    }


    /**
     * Add {@link ProductionInformation} list to queue_items.
     * @param productionInformations list of production information
     */
    public void addToQueueItems(List<ProductionInformation> productionInformations) {
        String sql = "INSERT INTO queue_items VALUES (DEFAULT, ?, ?, ?, ?)";

        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql);
            for (ProductionInformation productInfo : productionInformations) {
                ps.setInt(1, productInfo.getQuantity());
                ps.setInt(2, productInfo.getMachineSpeed());
                ps.setString(3, productInfo.getRecipeName());
                ps.setInt(4, productInfo.getOrderNumber());
                ps.addBatch();
            }
            ps.executeBatch();
        });
    }

    /**
     * Update status on {@link OrderItem} for every OrderItem connected to the orderNumber.
     * @param status    which status to set
     * @param orderNumber   orderNumber
     */
    public void setOrderItemStatus(OrderItemStatus status, int orderNumber){
        String sql = "UPDATE Order_items SET status = ? WHERE order_number = ?";

        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status.getStatus());
            ps.setInt(2, orderNumber);
            ps.execute();
        });
    }

    /**
     * Get measurements logs for a batch.
     * @param batchId batch id
     * @return list of measurementlog
     */
    public List<MeasurementLog> getMeasurementLogs(int batchId) {
        List<MeasurementLog> measurementLogs = new ArrayList<>();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT batch_id, measurement_time, temperature, humidity FROM Measurement_logs WHERE batch_id = ?");
            ps.setInt(1, batchId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDateTime timestamp = rs.getTimestamp("measurement_time").toLocalDateTime();
                MeasurementLog measurementLog = new MeasurementLog(rs.getBigDecimal("batch_id").intValue(),
                        timestamp, rs.getDouble("temperature"),
                        rs.getDouble("humidity"), 0);
                measurementLogs.add(measurementLog);
            }
        });

        return measurementLogs;
    }

    /**
     * Get batch by id.
     * @param batchId batch id
     * @return a batch
     */
    public Batch getBatch(int batchId) {
        AtomicReference<Batch> batch = new AtomicReference<>();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT beer_name, order_number, batch_id, started, " +
                    "finished, accepted, defect, machine_speed FROM batches WHERE batch_id = ?");
            ps.setInt(1, batchId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Batch tmp = batchFromResultSet(rs);
                batch.set(tmp);
            }
        });

        return batch.get();
    }

    /**
     * Create a batch from a result set.
     * @param rs
     * @return
     * @throws SQLException
     */
    private Batch batchFromResultSet(ResultSet rs) throws SQLException {
        int batchId = rs.getInt("batch_id");
        String beerName = rs.getString("beer_name");
        int orderNumber = rs.getInt("order_number");
        LocalDateTime started = rs.getTimestamp("started").toLocalDateTime();
        Timestamp timeStampFinished = rs.getTimestamp("finished");
        LocalDateTime finished = null;
        if (timeStampFinished != null) {
            finished = timeStampFinished.toLocalDateTime();
        }
        int accepted = rs.getInt("accepted");
        int defect = rs.getInt("defect");
        int machineSpeed = rs.getInt("machine_speed");
        return new Batch(beerName, orderNumber, batchId, started, finished, accepted, defect, machineSpeed);
    }

}
