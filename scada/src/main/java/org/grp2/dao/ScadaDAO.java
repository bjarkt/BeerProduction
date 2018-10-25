package org.grp2.dao;

import org.grp2.database.DatabaseConnection;
import org.grp2.database.IDatabaseCallback;
import org.grp2.domain.*;
import org.grp2.enums.State;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ScadaDAO extends DatabaseConnection {
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

    public LocalDateTime getBatchStartTime(int batchId){
        AtomicReference<LocalDateTime> timestamp = new AtomicReference<>();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT started FROM Batches WHERE batch_id = ?");
            ps.setInt(1, batchId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                LocalDateTime temp = rs.getTimestamp("started").toLocalDateTime();
                timestamp.set(temp);
            }
        });
        return timestamp.get();
    }

    public int createBatch(ProductionInformation productInfo){
        AtomicReference<BigDecimal> batchId = new AtomicReference<>();
        this.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO batches VALUES" +
                                                             "(?, ?, default, now(), null, null, null), " +
                                                            "RETURNING batch_id", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, productInfo.getRecipeName());
            ps.setString(2, String.valueOf(productInfo.getOrderNumber()));
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();

            while(rs.next()){
                batchId.set(rs.getBigDecimal(1));
            }

        });
        return batchId.get().intValue();
    }

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

}
