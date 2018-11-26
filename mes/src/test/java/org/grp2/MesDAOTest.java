package org.grp2;

import org.grp2.data.MesDAO;
import org.grp2.database.DatabaseLogin;
import org.grp2.domain.OEE;
import org.grp2.enums.OrderItemStatus;
import org.grp2.shared.*;
import org.grp2.utility.AbstractDAOTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
public class MesDAOTest extends AbstractDAOTest {
    private MesDAO mesDAO = new MesDAO(DatabaseLogin.TEST);

    @Before
    public void setUp() {
        cleanUpTables(mesDAO);
    }

    @After
    public void cleanUp() {
        cleanUpTables(mesDAO);
    }

    @Test
    public void testGetLockedOrders() {
        int orderNumber = getOrderNumber("locked");

        List<Order> orderItems =  mesDAO.getLockedOrders();
        assertEquals(1, orderItems.size());
    }

    @Test
    public void testGetOrderItems() {
        int orderNumber = getOrderNumber();
        addOrder(orderNumber, 100, "pilsner", "open");

        Map<OrderItem, Recipe> orderItems = mesDAO.getOrderItems(orderNumber);
        assertEquals(1, orderItems.size());
    }

    @Test
    public void testViewAllBatches() {
        int orderNumber = getOrderNumber();
        addBatch("pilsner", orderNumber, 1, 300);

        List<Batch> batches = mesDAO.viewAllBatches();
        assertEquals(1, batches.size());
    }

    @Test
    public void testAddToQueueItems() {
        int orderNumber = getOrderNumber("locked");
        List<ProductionInformation> productionInformations = Arrays.asList(
                        new ProductionInformation("pilsner", orderNumber, 300, 100),
                        new ProductionInformation("ale", orderNumber, 300, 100),
                        new ProductionInformation("wheat", orderNumber, 300, 100)
                );

        int[] results = mesDAO.addToQueueItems(productionInformations);

        assertEquals(productionInformations.size(), Arrays.stream(results).sum());
    }

    @Test
    public void testSetOrderItemStatus() {
        List<Integer> updateResults = new ArrayList<>();
        int orderNumber = getOrderNumber();

        addOrder(orderNumber, 100, "pilsner", OrderItemStatus.NONPROCESSED.getStatus());
        addOrder(orderNumber, 100, "ale", OrderItemStatus.NONPROCESSED.getStatus());

        for (OrderItemStatus status : OrderItemStatus.values()) {
            updateResults.add(mesDAO.setOrderItemStatus(status, orderNumber));
            mesDAO.getOrderItems(orderNumber).keySet()
                    .forEach(orderItem -> assertEquals(status.getStatus(), orderItem.getStatus()));
        }

        assertTrue(!updateResults.contains(0));
    }

    @Test
    public void testGetBatch() {
        int orderNumber = getOrderNumber();
        addBatch("pilsner", orderNumber, 1, 300);

        Batch batch = mesDAO.getBatch(1);

        assertEquals("pilsner", batch.getBeerName());
        assertEquals(1, batch.getOrderNumber());
        assertEquals(300, batch.getMachineSpeed());
    }

    @Test
    public void testGetBeerData() {
        Beer beer = mesDAO.getBeerData("pilsner");

        assertTrue(beer.getMinSpeed() >= 0);
        assertTrue(beer.getMaxSpeed() >= 0);
        assertTrue(beer.getId() != -1);
    }

    @Test
    public void testGetOEE() {
        int orderNumber = getOrderNumber();
        addBatch("pilsner", orderNumber, 1, 300);

        OEE oee = mesDAO.getOEE(1);
        assertNotNull(oee);
    }

    private void addOrder(int orderNumber, int quantity, String beerName, String status) {
        mesDAO.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Order_items (quantity, order_number, status, beer_name) VALUES (?, ?, DEFAULT, ?);");
            ps.setInt(1, quantity);
            ps.setInt(2, orderNumber);
            ps.setString(3, beerName);
            ps.execute();
        });
    }

    private int getOrderNumber(String status) {
        AtomicReference<BigDecimal> orderNumber = new AtomicReference<>();

        mesDAO.executeQuery(conn -> {
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

    private int getOrderNumber() {
        return getOrderNumber("open");
    }

    private void addBatch(String recipeName, int orderNumber, int batchId, int machineSpeed) {
        mesDAO.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO batches VALUES (?, ?, ?, now(), null, 0, 0, ?)");
            ps.setString(1,recipeName);
            ps.setInt(2, orderNumber);
            ps.setInt(3, batchId);
            ps.setInt(4, machineSpeed);

            ps.execute();
        });

    }

}
