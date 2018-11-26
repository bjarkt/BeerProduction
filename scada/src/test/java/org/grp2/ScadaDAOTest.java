package org.grp2;

import org.grp2.data.ScadaDAO;
import org.grp2.database.DatabaseLogin;
import org.grp2.enums.OrderItemStatus;
import org.grp2.enums.State;
import org.grp2.shared.*;
import org.grp2.utility.AbstractDAOTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class ScadaDAOTest extends AbstractDAOTest {
    private ScadaDAO scadaDAO = new ScadaDAO(DatabaseLogin.TEST);

    @Before
    public void setUp() {
        cleanUpTables(scadaDAO);
    }

    @After
    public void cleanUp() {
        cleanUpTables(scadaDAO);
    }

    @Test
    public void testGetMeasurementLogs() {
        addBatch("pilsner", -1, 1, 300);
        scadaDAO.updateMeasurementLogs(1, 1, 1);

        List<MeasurementLog> measurementLog = scadaDAO.getMeasurementLogs(1);


        assertEquals(1.0, measurementLog.stream().findFirst().get().getMeasurements().getTemperature(), 0);
        assertEquals(1.0, measurementLog.stream().findFirst().get().getMeasurements().getHumidity(), 0);
        assertEquals(1.0, measurementLog.stream().findFirst().get().getMeasurements().getVibration(), 0);
    }

    @Test
    public void testGetStateTimeLogs() {
        addBatch("pilsner", -1, 1, 300);

        // First run
        for (State value : State.values()) {
            scadaDAO.updateStateTimeLogs(value, 10);
        }

        for (StateTimeLog log : scadaDAO.getStateTimeLogs(1)) {
            assertEquals(10, log.getTimeElapsed());
            assertTrue(Arrays.asList(State.values()).contains(log.getPhase()));
        }

        // Second run
        for (State value : State.values()) {
            scadaDAO.updateStateTimeLogs(value, 10);
        }


        for (StateTimeLog log :  scadaDAO.getStateTimeLogs(1)) {
            assertEquals(20, log.getTimeElapsed());
            assertTrue(Arrays.asList(State.values()).contains(log.getPhase()));
        }
    }

    @Test
    public void testGetBatch() {
        addBatch("pilsner", -1, -1, -1);
        Batch b = scadaDAO.getBatch(-1);
        assertEquals(-1, b.getOrderNumber());
        assertEquals(-1, b.getBatchId());
        assertEquals("pilsner", b.getBeerName());
        assertEquals(-1, b.getMachineSpeed());
    }

    @Test
    public void testGetQueueItems() {
        int orderNumber = getOrderNumber("locked");
        addQueueItem(100, 300, "pilsner", orderNumber);

        List<ProductionInformation> items = scadaDAO.getQueueItems();
        assertEquals(1, items.size());
    }

    @Test
    public void testStartNextBatch() {
        int orderNumber = getOrderNumber("locked");
        addQueueItem(100, 300, "pilsner", orderNumber);
        ProductionInformation startedBatch = scadaDAO.startNextBatch();

        assertNotNull(startedBatch);
        assertNotNull(scadaDAO.getCurrentBatch());
    }

    @Test
    public void deleteQueueItem() {
        int orderNumber = getOrderNumber("locked");
        addQueueItem(100, 300, "pilsner", orderNumber);
        assertEquals(1, scadaDAO.getQueueItems().size());

        scadaDAO.deleteQueueItem(new Batch("pilsner", orderNumber, 1, null, null, 0, 0, 300));
        assertEquals(0, scadaDAO.getQueueItems().size());
    }

    @Test
    public void testGetRecipe() {
        Recipe recipe = scadaDAO.getRecipe("pilsner");
        assertNotNull(recipe);
    }

    @Test
    public void testGetCurrentBatch() {
        addBatch("pilsner", 1, 1, 300);
        assertNotNull(scadaDAO.getCurrentBatch());

        scadaDAO.updateCurrentBatchFinished();
        assertNull(scadaDAO.getCurrentBatch());
    }

    @Test
    public void testUpdateCurrentBatchProduced() {
        addBatch("pilsner", -1, 1, 300);

        scadaDAO.updateCurrentBatchProduced(10);

        assertEquals(10, scadaDAO.getCurrentBatch().getAccepted());

        scadaDAO.updateCurrentBatchProduced(10);

        assertEquals(20, scadaDAO.getCurrentBatch().getAccepted());
    }

    @Test
    public void testUpdateCurrentBatchDefects() {
        addBatch("pilsner", -1, 1, 300);

        scadaDAO.updateCurrentBatchDefects(10);

        assertEquals(10, scadaDAO.getCurrentBatch().getDefect());

        scadaDAO.updateCurrentBatchDefects(10);

        assertEquals(20, scadaDAO.getCurrentBatch().getDefect());
    }

    @Test
    public void testUpdateOrderItemStatus() {
        int orderNumber = getOrderNumber("locked");
        addOrderItem(100, orderNumber, "Pilsner");
        Batch finishedBatch = new Batch("pilsner", orderNumber, 1, null, null, 0, 0, 0);
        for (OrderItemStatus value : OrderItemStatus.values()) {
            int rowsChanged = scadaDAO.updateOrderItemStatus(finishedBatch, value);
            assertEquals(1, rowsChanged);
        }
    }


    private void addBatch(String recipeName, int orderNumber, int batchId, int machineSpeed) {
        scadaDAO.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO batches VALUES (?, ?, ?, now(), null, 0, 0, ?)");
            ps.setString(1,recipeName);
            ps.setInt(2, orderNumber);
            ps.setInt(3, batchId);
            ps.setInt(4, machineSpeed);

            ps.execute();
        });
    }

    private void addQueueItem(int quantity, int machineSpeed, String recipeName, int orderNumber) {
        scadaDAO.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO queue_items VALUES (DEFAULT, ?, ?, ?, ?)");
            ps.setInt(1, quantity);
            ps.setInt(2, machineSpeed);
            ps.setString(3, recipeName);
            ps.setInt(4, orderNumber);

            ps.executeUpdate();
        });
    }



    private int getOrderNumber(String status) {
        AtomicReference<BigDecimal> orderNumber = new AtomicReference<>();

        scadaDAO.executeQuery(conn -> {
            String insertOrderQuery = "INSERT INTO Orders VALUES(DEFAULT, ?, DEFAULT) RETURNING order_number";
            PreparedStatement ps = conn.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, status);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                orderNumber.set(rs.getBigDecimal(1));
            }
            ps.close();
        });

        return orderNumber.get().intValue();
    }

    private void addOrderItem(int quantity, int orderNumber, String beerName) {
        scadaDAO.executeQuery(conn -> {
            String insertOrderItemQuery = "INSERT INTO Order_items (quantity, order_number, status, beer_name) VALUES (?, ?, default, ?);";
            PreparedStatement ps = conn.prepareStatement(insertOrderItemQuery);
            ps.setInt(1, quantity);
            ps.setInt(2, orderNumber);
            ps.setString(3, beerName);
            ps.execute();
        });
    }
}
