package org.grp2.integrationTest;

import org.grp2.data.ErpDAO;
import org.grp2.database.DatabaseLogin;
import org.grp2.shared.Order;
import org.grp2.shared.OrderItem;
import org.grp2.tests.AbstractDAOTest;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.*;

public class ErpDAOTest extends AbstractDAOTest {

    private ErpDAO erpDAO = new ErpDAO(DatabaseLogin.TEST);

    @BeforeClass
    public static void checkDatabaseConnection() {
        org.junit.Assume.assumeTrue(isDatabaseUp(DatabaseLogin.TEST));
    }

    @Before
    public void setUp() {
        cleanUpTables(erpDAO);
    }

    @After
    public void cleanUp() {
        cleanUpTables(erpDAO);
    }

    @Test
    public void TestOrderNumber() {
        int orderNumber = erpDAO.getNewOrderNumber();

        assertEquals(1, orderNumber);
    }

    @Test
    public void testAddOrderItem() {
        int orderNumber = erpDAO.getNewOrderNumber();
        boolean success = erpDAO.addOrderItem(orderNumber, "pilsner", 100);

        assertTrue(success);
    }

    @Test
    public void testGetOrderItems() {
        int orderNumber = erpDAO.getNewOrderNumber();
        erpDAO.addOrderItem(orderNumber, "pilsner", 100);

        List<OrderItem> orders = erpDAO.viewOrderItems(orderNumber);

        assertEquals(1, orders.size());
    }

    @Test
    public void testViewOrderDetails() {
        int orderNumber = erpDAO.getNewOrderNumber();

        Order order = erpDAO.viewOrderDetails(orderNumber);

        assertEquals(1, order.getOrderNumber());
        assertEquals("open", order.getStatus());
    }

    @Test
    public void testDeleteOrderItem() {
        int orderNumber = erpDAO.getNewOrderNumber();
        erpDAO.addOrderItem(orderNumber, "pilsner", 100);
        erpDAO.addOrderItem(orderNumber, "ale", 20);
        assertEquals(2, erpDAO.viewOrderItems(orderNumber).size());

        erpDAO.deleteOrderItem(orderNumber, "pilsner");
        assertEquals(1, erpDAO.viewOrderItems(orderNumber).size());
    }

    @Test
    public void testDeleteOrder() {
        int orderNumber = erpDAO.getNewOrderNumber();
        erpDAO.deleteOrder(orderNumber);

        assertEquals(0, erpDAO.getOrders("open").size());
    }

    @Test
    public void testEditOrderItemQuantity() {
        int orderNumber = erpDAO.getNewOrderNumber();
        erpDAO.addOrderItem(orderNumber, "pilsner", 50);
        erpDAO.editOrderItem(orderNumber, "pilsner", 100);

        OrderItem item = erpDAO.viewOrderItems(orderNumber).get(0);
        assertEquals(100, item.getQuantity());
    }

    @Test
    public void testLockOrder() {
        int orderNumber = erpDAO.getNewOrderNumber();
        assertEquals("open", erpDAO.viewOrderDetails(orderNumber).getStatus());

        erpDAO.lockOrder(orderNumber);

        assertEquals("locked", erpDAO.viewOrderDetails(orderNumber).getStatus());
    }

}
