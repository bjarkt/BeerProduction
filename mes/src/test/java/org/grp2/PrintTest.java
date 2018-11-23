package org.grp2;

import org.grp2.domain.printmanager.IPrintManager;
import org.grp2.domain.printmanager.SimplePdfPrinter1;
import org.grp2.domain.printmanager.SimplePdfPrinter2;
import org.grp2.shared.Batch;
import org.grp2.shared.MeasurementLog;
import org.grp2.shared.Measurements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class PrintTest {


    @Test
    public void testPrintManager1() throws IOException {
        IPrintManager printManager = new SimplePdfPrinter1();
        printManager.setPath(File.createTempFile("report", "pdf").getAbsolutePath());

        runTest(printManager);
    }

    @Test
    public void testPrintManager2() throws IOException {
        IPrintManager printManager = new SimplePdfPrinter2();
        printManager.setPath(File.createTempFile("report", "pdf").getAbsolutePath());

        runTest(printManager);
    }


    private void runTest(IPrintManager printManager) {
        List<MeasurementLog> measurementLogs = setupMeasurementsData();
        Batch batch = new Batch("pilsner", 1, 1, LocalDateTime.now(), LocalDateTime.now().plusSeconds(25), 250, 58, 540);

        printManager.writeDocument(batch, measurementLogs.toArray(new MeasurementLog[]{}));
        byte[] pdfData = printManager.getDocument();

        assertTrue(pdfData.length > 0);
    }


    private List<MeasurementLog> setupMeasurementsData() {
        List<MeasurementLog> measurements = new ArrayList<>();

        Collections.addAll(measurements,
                new MeasurementLog(1, LocalDateTime.now(), new Measurements(24, 52, 0.2)),
                new MeasurementLog(1, LocalDateTime.now(), new Measurements(22, 42, -0.1)),
                new MeasurementLog(1, LocalDateTime.now(), new Measurements(25, 57, 0.7)),
                new MeasurementLog(1, LocalDateTime.now(), new Measurements(19, 32, -0.5)));

        return measurements;
    }
}
