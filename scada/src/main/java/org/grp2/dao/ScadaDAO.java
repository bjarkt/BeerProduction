package org.grp2.dao;

import org.grp2.domain.MeasurementLog;
import org.grp2.domain.StateTimeLog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScadaDAO /* extends DatabaseConnection */ {
    public List<MeasurementLog> getMeasurementLogs(int batchId) {
        List<MeasurementLog> measurementLogs = new ArrayList<>();
        // this.executeQuery("select * from measurementLogs")
        MeasurementLog measurementLog = new MeasurementLog(batchId, LocalDateTime.now(), 0, 0, 0);
        measurementLogs.add(measurementLog);

        return measurementLogs;
    }

    public List<StateTimeLog> getStateTimeLogs(int batchId) {
        List<StateTimeLog> stateTimeLogs = new ArrayList<>(); // TODO db.getStateTimeLogs(batchId)
        StateTimeLog stateTimeLog = new StateTimeLog(batchId, "fixme", 0);
        stateTimeLogs.add(stateTimeLog);

        return stateTimeLogs;
    }

}
