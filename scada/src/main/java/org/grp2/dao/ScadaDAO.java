package org.grp2.dao;

import org.grp2.database.DatabaseConnection;
import org.grp2.database.IDatabaseCallback;
import org.grp2.domain.MeasurementLog;
import org.grp2.domain.Measurements;
import org.grp2.domain.StateTimeLog;
import org.grp2.enums.State;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

}
