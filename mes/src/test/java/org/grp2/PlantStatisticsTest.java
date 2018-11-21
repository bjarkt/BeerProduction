package org.grp2;

import org.grp2.domain.MeasurementsStatistics;
import org.grp2.shared.Batch;
import org.grp2.shared.MeasurementLog;
import org.grp2.shared.Measurements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlantStatisticsTest {
    @Test
    public void measurementStatisticsTest() {
        Object[] data = setupMeasurementsData();
        MeasurementsStatistics measurementsStatistics = new MeasurementsStatistics((List<MeasurementLog>) data[0]);
        MeasurementsStatistics expected = (MeasurementsStatistics) data[1];

        assertEquals(expected.getAvgTemp(), measurementsStatistics.getAvgTemp(), 0);
        assertEquals(expected.getLowestTemp(), measurementsStatistics.getLowestTemp(), 0);
        assertEquals(expected.getHighestTemp(), measurementsStatistics.getHighestTemp(), 0);
    }

    @Test
    public void batchStatisticsTest() {
        List<Batch> batches = setupBatchData();
    }

    private Object[] setupMeasurementsData() {
        List<MeasurementLog> measurements = new ArrayList<>();

        Collections.addAll(measurements,
                new MeasurementLog(-1, null, new Measurements(24, 52, 0.2)),
                new MeasurementLog(-1, null, new Measurements(22, 42, -0.1)),
                new MeasurementLog(-1, null, new Measurements(25, 57, 0.7)),
                new MeasurementLog(-1, null, new Measurements(19, 32, -0.5)));

        MeasurementsStatistics expected = new MeasurementsStatistics(25.0, 19.0, 22.5);

        return new Object[]{measurements, expected};
    }

    private List<Batch> setupBatchData() {
        List<Batch> batches = new ArrayList<>();

        return batches;
    }
}
