package org.grp2.dao;

import org.grp2.database.DatabaseConnection;
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
     * Get state time logs.
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
     * @param batchId batch id
     * @return a batch
     */
    public Batch getBatch(int batchId) {
        AtomicReference<Batch> batch = new AtomicReference<>();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT beer_name, order_number, batch_id, started, " +
                                                        "finished, accepted, defect FROM batches WHERE batch_id = ?");
            ps.setInt(1, batchId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Batch tmp = batchFromResultSet(rs, batchId);
                batch.set(tmp);
            }
        });

        return batch.get();
    }

    /**
     * Get items in queue_items.
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
     * @return
     */
    public ProductionInformation startNextBatch(){
        List<ProductionInformation> queueItems = getQueueItems();
        Optional<ProductionInformation> productionInformation = queueItems.stream().findFirst();
        boolean batchRunning = true;

        // is the machine ready? (no currently running batch)
        if (this.getCurrentBatch() == null) {
            productionInformation.ifPresent(this::addBatchAndRemoveFromQueue);
            batchRunning = false;
        }

        if (!batchRunning && productionInformation.isPresent()) { // no batch is running, and we got an item from the queue
            return productionInformation.get(); // return the item, signalling that it is ready to start
        } else {
            return null; // no item, cannot start
        }
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
     * Add a {@link ProductionInformation} from the queue to the batches table, and remove it from the queue.
     * @param productionInformation the item to add and remove
     */
    private void addBatchAndRemoveFromQueue(ProductionInformation productionInformation) {
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO batches VALUES (?, ?, ?, now(), null, null, null)");
            ps.setString(1, productionInformation.getRecipeName());
            ps.setInt(2, productionInformation.getOrderNumber());
            ps.setInt(3, productionInformation.getBatchId());

            ps.executeUpdate();
        });
        this.deleteQueueItem(productionInformation);
    }

    /**
     * Delete an item from the queue.
     * @param productionInformation item to remove
     */
    private void deleteQueueItem(ProductionInformation productionInformation) {
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM queue_items WHERE recipe_name = ? AND order_number = ?");
            ps.setString(1, productionInformation.getRecipeName());
            ps.setInt(2, productionInformation.getOrderNumber());

            ps.executeUpdate();
        });
    }

    /**
     * Get a recipe by beer name.
     * @param name name of beer
     * @return
     */
    public Recipe getRecipe(String name){
        AtomicReference<Recipe> recipe = new AtomicReference<>();

        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT id, name, min_speed, max_speed " +
                                                            "FROM recipes WHERE name = ?");
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Recipe temp = new Recipe(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("min_speed"), rs.getInt("max_speed"));

                recipe.set(temp);
            }
        });

        return recipe.get();
    }

    /**
     * Get the currently executing batch.
     * @return the executing batch
     */
    public Batch getCurrentBatch() {
        AtomicReference<Batch> batch = new AtomicReference<>();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT beer_name, order_number, batch_id, started, " +
                    "finished, accepted, defect FROM batches WHERE finished IS NULL ORDER BY batch_id DESC LIMIT 1");
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
    public void updateCurrentBatchFinished() {
        Batch currentBatch = this.getCurrentBatch();
        if (currentBatch != null) {
            this.executeQuery(conn -> {
                PreparedStatement ps = conn.prepareStatement("UPDATE batches SET finished = now() WHERE batch_id = ?");
                ps.setInt(1, currentBatch.getBatchId());

                ps.executeUpdate();
            });
        }
    }

    /**
     * Create a batch from a result set.
     * @param rs
     * @return
     * @throws SQLException
     */
    private Batch batchFromResultSet(ResultSet rs) throws SQLException {
        return batchFromResultSet(rs, rs.getInt("batch_id"));
    }

    /**
     * Create a batch from a result set.
     * @param rs
     * @return
     * @throws SQLException
     */
    private Batch batchFromResultSet(ResultSet rs, int batchId) throws SQLException {
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
        return new Batch(beerName, orderNumber, batchId, started, finished, accepted, defect);
    }

    /**
     * Adds a new row to the measurement logs, for the current batch.
     * @param temperature temperature
     * @param humidity humidity
     */
    public void updateMeasurementLogs(double temperature, double humidity) {
        Batch currentBatch = this.getCurrentBatch();

        if (currentBatch != null) {
            this.executeQuery(conn -> {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO measurement_logs VALUES (?, now(), ?, ?)");
                ps.setInt(1, currentBatch.getBatchId());
                ps.setDouble(2, temperature);
                ps.setDouble(3, humidity);

                ps.executeUpdate();
            });
        }
    }

    /**
     * Update the state time logs, for the current batch.
     * @param state the state
     * @param timeElapsed how much time spent in that state
     */
    public void updateStateTimeLogs(State state, int timeElapsed) {
        Batch currentBatch = this.getCurrentBatch();

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

                ps.executeUpdate();
            });
        }
    }

    /**
     * Update the produced column for the current batch.
     * @param accepted how many accepted
     */
    public void updateCurrentBatchProduced(int accepted) {
        Batch currentBatch = getCurrentBatch();

        if (currentBatch != null) {
            this.executeQuery(conn -> {
                PreparedStatement ps = conn.prepareStatement("UPDATE batches SET accepted = ? WHERE batch_id = ?");
                ps.setInt(1, accepted);
                ps.setInt(2, currentBatch.getBatchId());

                ps.executeUpdate();
            });
        }
    }

    /**
     * Update the defects column for the current batch.
     * @param defects how many defects
     */
    public void updateCurrentBatchDefects(int defects) {
        Batch currentBatch = getCurrentBatch();

        if (currentBatch != null) {
            this.executeQuery(conn -> {
                PreparedStatement ps = conn.prepareStatement("UPDATE batches SET defect = ? WHERE batch_id = ?");
                ps.setInt(1, defects);
                ps.setInt(2, currentBatch.getBatchId());

                ps.executeUpdate();
            });
        }
    }
}
