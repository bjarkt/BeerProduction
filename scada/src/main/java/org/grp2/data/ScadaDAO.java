package org.grp2.data;

import org.grp2.database.DatabaseConnection;
import org.grp2.database.DatabaseLogin;
import org.grp2.enums.OrderItemStatus;
import org.grp2.enums.OrderStatus;
import org.grp2.enums.State;
import org.grp2.shared.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ScadaDAO extends DatabaseConnection {
    public ScadaDAO(DatabaseLogin loginInformation) {
        super(loginInformation);
    }

    public ScadaDAO() {
    }

    /**
     * Get measurements logs for a batch.
     *
     * @param batchId batch id
     * @return list of measurementlog
     */
    public List<MeasurementLog> getMeasurementLogs(int batchId) {
        List<MeasurementLog> measurementLogs = new ArrayList<>();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT batch_id, measurement_time, temperature, humidity, vibration FROM Measurement_logs WHERE batch_id = ?");
            ps.setInt(1, batchId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDateTime timestamp = rs.getTimestamp("measurement_time").toLocalDateTime();
                MeasurementLog measurementLog = new MeasurementLog(rs.getBigDecimal("batch_id").intValue(),
                        timestamp, rs.getDouble("temperature"),
                        rs.getDouble("humidity"), rs.getDouble("vibration"));
                measurementLogs.add(measurementLog);
            }
        });

        return measurementLogs;
    }

    /**
     * Get state time logs.
     *
     * @param batchId batch id
     * @return list of state time logs
     */
    public List<StateTimeLog> getStateTimeLogs(int batchId) {
        List<StateTimeLog> stateTimeLogs = new ArrayList<>();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT batch_id, phase, time_elapsed FROM state_time_logs WHERE batch_id = ?");
            ps.setInt(1, batchId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                State state = State.fromCode(rs.getInt("phase"));
                StateTimeLog stateTimeLog = new StateTimeLog(rs.getBigDecimal("batch_id").intValue(),
                        state, rs.getInt("time_elapsed"));
                stateTimeLogs.add(stateTimeLog);
            }
        });

        return stateTimeLogs;
    }

    /**
     * Get batch by id.
     *
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
     * Get items in queue_items.
     *
     * @return list of {@link ProductionInformation} objects
     */
    public List<ProductionInformation> getQueueItems() {
        List<ProductionInformation> items = new ArrayList<>();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT batches_id, quantity, machine_speed, recipe_name, " +
                    "order_number FROM queue_items ORDER BY order_number");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductionInformation temp = new ProductionInformation(rs.getString("recipe_name"), rs.getInt("order_number"),
                        rs.getInt("machine_speed"), rs.getInt("quantity"));
                temp.setBatchId(rs.getInt("batches_id"));

                items.add(temp);
            }
        });

        return items;
    }

    /**
     * Get items from the queue, and add the first as a batch, if the machine is ready.
     *
     * @return
     */
    public ProductionInformation startNextBatch() {
        List<ProductionInformation> queueItems = getQueueItems();
        Optional<ProductionInformation> productionInformation = queueItems.stream().findFirst();
        boolean batchRunning = true;

        // is the machine ready? (no currently running batch)
        if (this.getCurrentBatch() == null) {
            productionInformation.ifPresent(this::addBatch);
            batchRunning = false;
        }

        if (!batchRunning && productionInformation.isPresent()) { // no batch is running, and we got an item from the queue
            return productionInformation.get(); // return the item, signalling that it is ready to start
        } else {
            return null; // no item, cannot start
        }
    }

    /**
     * Add a {@link ProductionInformation} from the queue to the batches table, and remove it from the queue.
     *
     * @param productionInformation the item to add and remove
     * @return how many rows was changed in the database
     */
    private int addBatch(ProductionInformation productionInformation) {
        AtomicInteger insertResult = new AtomicInteger();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO batches VALUES (?, ?, ?, now(), null, 0, 0, ?)");
            ps.setString(1, productionInformation.getRecipeName());
            ps.setInt(2, productionInformation.getOrderNumber());
            ps.setInt(3, productionInformation.getBatchId());
            ps.setInt(4, productionInformation.getMachineSpeed());

            insertResult.set(ps.executeUpdate());
        });

        return insertResult.get();
    }

    /**
     * Delete an item from the queue.
     *
     * @param productionInformation item to remove
     * @return how many rows deleted in db
     */
    private int deleteQueueItem(ProductionInformation productionInformation) {
        return this.deleteQueueItem(productionInformation.getRecipeName(), productionInformation.getOrderNumber());
    }

    public int deleteQueueItem(Batch batch) {
        return this.deleteQueueItem(batch.getBeerName(), batch.getOrderNumber());
    }

    private int deleteQueueItem(String recipeName, int orderNumber) {
        AtomicInteger deleteResult = new AtomicInteger();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM queue_items WHERE recipe_name = ? AND order_number = ?");
            ps.setString(1, recipeName);
            ps.setInt(2, orderNumber);

            deleteResult.set(ps.executeUpdate());
        });

        return deleteResult.get();
    }

    /**
     * Get a recipe by beer name.
     *
     * @param name name of beer
     * @return
     */
    public Recipe getRecipe(String name) {
        AtomicReference<Recipe> recipe = new AtomicReference<>();

        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT id, name, min_speed, max_speed " +
                    "FROM recipes WHERE name = ?");
            ps.setString(1, name.toLowerCase());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Recipe temp = new Recipe(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("min_speed"), rs.getInt("max_speed"));

                recipe.set(temp);
            }
        });

        return recipe.get();
    }

    /**
     * Get the currently executing batch.
     *
     * @return the executing batch
     */
    public Batch getCurrentBatch() {
        AtomicReference<Batch> batch = new AtomicReference<>();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT beer_name, order_number, batch_id, started, " +
                    "finished, accepted, defect, machine_speed FROM batches WHERE finished IS NULL ORDER BY batch_id DESC LIMIT 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Batch tmp = batchFromResultSet(rs);
                batch.set(tmp);
            }
        });

        return batch.get();
    }

    /**
     * Set the finished time of the running batch.
     */
    public Batch updateCurrentBatchFinished() {
        Batch currentBatch = this.getCurrentBatch();
        if (currentBatch != null) {
            this.executeQuery(conn -> {
                PreparedStatement ps = conn.prepareStatement("UPDATE batches SET finished = now() WHERE batch_id = ?");
                ps.setInt(1, currentBatch.getBatchId());

                ps.executeUpdate();
            });
        }

        return currentBatch;
    }


    /**
     * Create a batch from a result set.
     *
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

    /**
     * Adds a new row to the measurement logs, for the current batch.
     *
     * @param temperature temperature
     * @param humidity    humidity
     */
    public int updateMeasurementLogs(double temperature, double humidity, double vibration) {
        Batch currentBatch = this.getCurrentBatch();
        AtomicInteger insertResult = new AtomicInteger();

        if (currentBatch != null) {
            this.executeQuery(conn -> {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO measurement_logs VALUES (?, now(), ?, ?, ?)");
                ps.setInt(1, currentBatch.getBatchId());
                ps.setDouble(2, temperature);
                ps.setDouble(3, humidity);
                ps.setDouble(4, vibration);

                insertResult.set(ps.executeUpdate());
            });
        }

        return insertResult.get();
    }

    /**
     * Update the state time logs, for the current batch.
     *
     * @param state       the state
     * @param timeElapsed how much time spent in that state
     * @return how many rows changed in db
     */
    public int updateStateTimeLogs(State state, int timeElapsed) {
        Batch currentBatch = this.getCurrentBatch();
        AtomicInteger result = new AtomicInteger();

        if (currentBatch != null && state != null) {
            AtomicInteger currentSeconds = new AtomicInteger(-1);
            this.executeQuery(conn -> {
                PreparedStatement ps = conn.prepareStatement("SELECT time_elapsed FROM state_time_logs WHERE batch_id = ? and phase = ?");
                ps.setInt(1, currentBatch.getBatchId());
                ps.setInt(2, state.getValue());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    currentSeconds.set(rs.getInt("time_elapsed"));
                }
            });


            this.executeQuery(conn -> {
                PreparedStatement ps;
                if (currentSeconds.get() == -1) { // phase does not exist for that batch id
                    ps = conn.prepareStatement("INSERT INTO state_time_logs VALUES (?, ?, ?)");
                    ps.setInt(1, currentBatch.getBatchId());
                    ps.setInt(2, state.getValue());
                    ps.setInt(3, timeElapsed);
                } else { // phase exists for that batch id
                    ps = conn.prepareStatement("UPDATE state_time_logs SET time_elapsed = ? WHERE batch_id = ? AND phase = ?");
                    ps.setInt(1, timeElapsed + currentSeconds.get());
                    ps.setInt(2, currentBatch.getBatchId());
                    ps.setInt(3, state.getValue());
                }

                result.set(ps.executeUpdate());
            });
        }

        return result.get();
    }

    /**
     * Update the produced column for the current batch.
     *
     * @param accepted how many accepted
     * @return how many rows changed in db
     */
    public int updateCurrentBatchProduced(int accepted) {
        Batch currentBatch = getCurrentBatch();
        AtomicInteger result = new AtomicInteger();

        if (currentBatch != null) {
            this.executeQuery(conn -> {
                PreparedStatement ps = conn.prepareStatement("UPDATE batches SET accepted = accepted + ? WHERE batch_id = ?");
                ps.setInt(1, accepted);
                ps.setInt(2, currentBatch.getBatchId());

                result.set(ps.executeUpdate());
            });
        }

        return result.get();
    }

    /**
     * Update the defects column for the current batch.
     *
     * @param defects how many defects
     * @return how many rows changed in db
     */
    public int updateCurrentBatchDefects(int defects) {
        Batch currentBatch = getCurrentBatch();
        AtomicInteger result = new AtomicInteger();

        if (currentBatch != null) {
            this.executeQuery(conn -> {
                PreparedStatement ps = conn.prepareStatement("UPDATE batches SET defect = defect + ? WHERE batch_id = ?");
                ps.setInt(1, defects);
                ps.setInt(2, currentBatch.getBatchId());

                result.set(ps.executeUpdate());
            });
        }

        return result.get();
    }

    /**
     * Updates the status of an order item
     *
     * @param finishedBatch which batch
     * @param status        new status
     * @return how many rows changed in db
     */
    public int updateOrderItemStatus(Batch finishedBatch, OrderItemStatus status) {
        AtomicInteger result = new AtomicInteger();
        if (finishedBatch != null) {
            this.executeQuery(conn -> {
                PreparedStatement ps = conn.prepareStatement("UPDATE order_items SET status = ? WHERE order_number = ? AND beer_name = ?");
                ps.setString(1, status.getStatus());
                ps.setInt(2, finishedBatch.getOrderNumber());
                ps.setString(3, finishedBatch.getBeerName());

                result.set(ps.executeUpdate());
            });
        }

        return result.get();
    }

    public List<OrderItem> getOrderItems(int orderNumber) {
        List<OrderItem> orderItems = new ArrayList<>();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM order_items WHERE order_number = ?");
            ps.setInt(1, orderNumber);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orderItems.add(new OrderItem(
                        rs.getInt("order_number"),
                        rs.getString("beer_name"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                ));
            }
        });

        return orderItems;
    }

    public int updateOrderStatus(int orderNumber, OrderStatus done) {
        AtomicInteger result = new AtomicInteger();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("UPDATE orders SET status = ? WHERE order_number = ?");
            ps.setString(1, done.getStatus());
            ps.setInt(2, orderNumber);

            result.set(ps.executeUpdate());
        });

        return result.get();
    }
}
