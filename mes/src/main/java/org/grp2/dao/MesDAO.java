package org.grp2.dao;

import org.grp2.database.DatabaseConnection;
import org.grp2.domain.Plant;
import org.grp2.enums.OrderItemStatus;
import org.grp2.enums.OrderStatus;
import org.grp2.shared.Batch;
import org.grp2.shared.Order;
import org.grp2.shared.OrderItem;
import org.grp2.shared.ProductionInformation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MesDAO extends DatabaseConnection {

    /**
     *
     * @return
     */
    public List<Order> viewOrders() {
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

    
    public List<OrderItem> viewOrderItems(int orderNumber) {
        List<OrderItem> orderItems = new ArrayList<>();
        this.executeQuery(conn -> {
            try {
                String getOrderQuery = "SELECT quantity, beer_name, status FROM Order_items WHERE order_number = ? ";
                PreparedStatement ps = conn.prepareStatement(getOrderQuery);
                ps.setInt(1, orderNumber);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int quantity = rs.getInt(1);
                    String beerName = rs.getString(2);
                    String status = rs.getString(3);
                    orderItems.add(new OrderItem(orderNumber, beerName, quantity, status));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return orderItems;

    }

    public List<Batch> viewAllBatches() {
        List<Batch> batches = new ArrayList<>();
        this.executeQuery(conn -> {
            try {
                String getOrderQuery = "SELECT * FROM Batches";
                PreparedStatement ps = conn.prepareStatement(getOrderQuery);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String beerName = rs.getString(1);
                    int orderNumber = rs.getInt(2);
                    int batchId = rs.getInt(3);
                    LocalDateTime started = rs.getTimestamp(4).toLocalDateTime();
                    LocalDateTime finished = rs.getTimestamp(5).toLocalDateTime();
                    int accepted = rs.getInt(6);
                    int defect = rs.getInt(7);
                    batches.add(new Batch(beerName, orderNumber, batchId, started, finished, accepted, defect));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return batches;
    }

    public String viewCurrentBatchStatus(int orderNumber) {
        AtomicReference<String> status = new AtomicReference<>();
        this.executeQuery(conn -> {
            try {
                String getOrderQuery = "SELECT Orders.status FROM Orders, Batches " +
                        "WHERE Orders.order_number = ? AND Orders.order_number = Batches.order_number;";
                PreparedStatement ps = conn.prepareStatement(getOrderQuery);
                ps.setInt(1, orderNumber);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    status.set(rs.getString(1));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return status.toString();
    }

    public Plant viewPlantStatistics() {
        Plant plant = new Plant();

        return plant;

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

}
