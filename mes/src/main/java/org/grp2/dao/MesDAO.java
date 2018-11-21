package org.grp2.dao;

import org.grp2.database.DatabaseConnection;
import org.grp2.domain.BatchStatistics;
import org.grp2.domain.MeasurementsStatistics;
import org.grp2.domain.OEE;
import org.grp2.domain.Plant;
import org.grp2.domain.PlantStatistics;
import org.grp2.enums.Finance;
import org.grp2.enums.OrderItemStatus;
import org.grp2.enums.OrderStatus;
import org.grp2.shared.*;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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
    public PlantStatistics viewPlantStatistics(LocalDateTime from, LocalDateTime to) {
        BatchStatistics bs = getBatchStatistics(from, to);
        MeasurementsStatistics ms = getMeasurementStatistics(bs.getBatchList());

        PlantStatistics plantStatistics = new PlantStatistics(ms, bs);

        return  plantStatistics;
    }



    private BatchStatistics getBatchStatistics(LocalDateTime from, LocalDateTime to) {
        BatchStatistics batchStatistics;
        String sql = "SELECT * FROM batches WHERE started  >= ? AND finished <= ? ";
        List<Batch> batches = new ArrayList<>();
        double avgAccepted = 0;
        double avgDefects = 0;
        double avgProductionSeconds = 0;

        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setTimestamp(1, Timestamp.valueOf(from));
            ps.setTimestamp(2, Timestamp.valueOf(to));

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Batch temp = batchFromResultSet(rs);
                batches.add(temp);
            }

        });

        double accepted = 0;
        double defects = 0;
        double seconds = 0;
        for(Batch batch : batches) {
            accepted += batch.getAccepted();
            defects += batch.getDefect();
            seconds += ChronoUnit.SECONDS.between(batch.getStarted(), batch.getFinished());
        }
        avgAccepted = accepted / batches.size();
        avgDefects = defects / batches.size();
        avgProductionSeconds = seconds / batches.size();

        batchStatistics = new BatchStatistics(avgAccepted, avgDefects, avgProductionSeconds, batches);

        return batchStatistics;
    }

    private MeasurementsStatistics getMeasurementStatistics(List<Batch> batches){
        List<MeasurementLog> measurements = new ArrayList<>();
        for(Batch batch : batches) {
            measurements.addAll(getMeasurementLogs(batch.getBatchId()));
        }

        Double highestTemp = null;
        Double lowestTemp = null;
        for(MeasurementLog measurement : measurements) {
            if (highestTemp == null && lowestTemp == null) {
                highestTemp = measurement.getMeasurements().getTemperature();
                lowestTemp = measurement.getMeasurements().getTemperature();
            }
            if(measurement.getMeasurements().getTemperature() > highestTemp) {
                highestTemp = measurement.getMeasurements().getTemperature();
            }
            if(measurement.getMeasurements().getTemperature() < lowestTemp) {
                lowestTemp = measurement.getMeasurements().getTemperature();
            }
        }

        Double tempSum = 0.0;
        for(MeasurementLog measurement : measurements) {
            tempSum += measurement.getMeasurements().getTemperature();
        }

        Double avgTemp = tempSum / measurements.size();

        MeasurementsStatistics measurementStatistics = new MeasurementsStatistics(highestTemp, lowestTemp, avgTemp);

        return measurementStatistics;
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

    public Beer getBeerData(String beerName){
        Recipe recipe = new Recipe(-1, beerName, -1, -1);
        int profit = Finance.valueOf(beerName.toUpperCase()).getProfit();
        int cost = Finance.valueOf(beerName.toUpperCase()).getCost();

        this.executeQuery(conn -> {
            String getRecipeQuery = "SELECT max_speed, min_speed, ID FROM Recipes WHERE Recipes.name = ?";
            PreparedStatement ps = conn.prepareStatement(getRecipeQuery);
            ps.setString(1, Finance.valueOf(beerName.toUpperCase()).getName());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                recipe.setId(rs.getBigDecimal("ID").intValue());
                recipe.setMaxSpeed(rs.getInt("max_speed"));
                recipe.setMinSpeed(rs.getInt("min_speed"));
            }
        });

        return new Beer(recipe, profit,cost);

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

    public OEE getOEE(int batchID) {
        Batch batch = getBatch(batchID);
        OEE oee = new OEE(batch, getOEEStopTime(batch.getBatchId()));

        return oee;
    }


    /**
     * Method to get the stop time needed to calculate availability.
     * @param batchID
     * @return
     */
    private int getOEEStopTime(int batchID) {
        AtomicReference<Integer> stopTime = new AtomicReference<>();
        this.executeQuery(conn -> {
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT sum(time_elapsed) FROM State_time_logs WHERE batch_id = ? AND phase <> 6");
                ps.setInt(1, batchID);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    stopTime.set(rs.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return stopTime.get();
    }



}
