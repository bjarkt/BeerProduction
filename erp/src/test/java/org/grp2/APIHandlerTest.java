package org.grp2;

import com.fasterxml.jackson.core.type.TypeReference;
import io.javalin.Context;
import org.grp2.api.APIHandler;
import org.grp2.api.APIRoutes;
import org.grp2.dao.ErpDAO;
import org.grp2.enums.OrderStatus;
import org.grp2.javalin.Message;
import org.grp2.shared.Order;
import org.grp2.shared.OrderItem;
import org.grp2.utility.JavalinTestUtility;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class APIHandlerTest {

    @Mock
    private ErpDAO erpDAO;

    private APIHandler apiHandler;

    private String basePath = "/api";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        apiHandler = new APIHandler(erpDAO);
    }

    @Test
    public void testCreateOrder() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.CREATE_ORDER);

        when(erpDAO.getNewOrderNumber()).thenReturn(1);

        apiHandler.createOrder(context);

        Message response = JavalinTestUtility.getResponse(context, Message.class);

        assertEquals(200, response.getStatus());
        assertEquals("1", response.getMessage());
    }

    @Test
    public void testAddOrderItem() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.ADD_ORDER_ITEM,
                "order-id", "1",
                                         "beer-name", "pilsner",
                                         "quantity", "100");

        when(erpDAO.addOrderItem(any(Integer.class), any(String.class), any(Integer.class))).thenReturn(true).thenReturn(false);

        apiHandler.addOrderItem(context);
        Message success = JavalinTestUtility.getResponse(context, Message.class);
        assertEquals(200, success.getStatus());

        apiHandler.addOrderItem(context);
        Message failure = JavalinTestUtility.getResponse(context, Message.class);
        assertEquals(403, failure.getStatus());
    }

    @Test
    public void testDeleteOrder() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.DELETE_ORDER,
                "order-id", "1");

        when(erpDAO.deleteOrder(any(Integer.class))).thenReturn(true).thenReturn(false);

        apiHandler.deleteOrder(context);
        Message success = JavalinTestUtility.getResponse(context, Message.class);
        assertEquals(200, success.getStatus());

        apiHandler.deleteOrder(context);
        Message failure = JavalinTestUtility.getResponse(context, Message.class);
        assertEquals(403, failure.getStatus());
    }

    @Test
    public void testEditOrderItem() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("quantity", "100");
        queryParams.put("new-beer-name", "ale");
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.EDIT_ORDER_ITEM, queryParams,
                                            "order-id", "1",
                                            "beer-name", "pilsner");
        when(erpDAO.editOrderItem(anyInt(), anyString(), anyInt(), anyString())).thenReturn(true).thenReturn(false);
        when(erpDAO.editOrderItem(anyInt(), anyString(), anyString())).thenReturn(true).thenReturn(false);
        when(erpDAO.editOrderItem(anyInt(), anyString(), anyInt())).thenReturn(true).thenReturn(false);

        apiHandler.editOrderItem(context);
        Message success = JavalinTestUtility.getResponse(context, Message.class);
        assertEquals(200, success.getStatus());

        apiHandler.editOrderItem(context);
        Message failure = JavalinTestUtility.getResponse(context, Message.class);
        assertEquals(403, failure.getStatus());
    }

    @Test
    public void testDeleteOrderItem() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.DELETE_ORDER_ITEM,
                "order-id", "1",
                                        "beer-name", "pilsner");
        when(erpDAO.deleteOrderItem(anyInt(), anyString())).thenReturn(true).thenReturn(false);

        apiHandler.deleteOrderItem(context);
        Message success = JavalinTestUtility.getResponse(context, Message.class);
        assertEquals(200, success.getStatus());

        apiHandler.deleteOrderItem(context);
        Message failure = JavalinTestUtility.getResponse(context, Message.class);
        assertEquals(403, failure.getStatus());
    }

    @Test
    public void testLockOrder() {
        String orderNumber = "1";
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.LOCK_ORDER,
                "order-id", orderNumber);

        apiHandler.lockOrder(context);
        Message response = JavalinTestUtility.getResponse(context, Message.class);
        assertEquals(200, response.getStatus());
        assertTrue(response.getMessage().contains(orderNumber));
    }

    @Test
    public void testViewOrderItems() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_ORDER_ITEMS,
                "order-id", "1");

        apiHandler.viewOrderItems(context);
        List response = JavalinTestUtility.getResponse(context, List.class);
        assertNotNull(response);
    }

    @Test
    public void testViewOrderDetails() {
        int orderNumber = 1;
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_ORDER_DETAILS,
                "order-id", String.valueOf(orderNumber));

        when(erpDAO.viewOrderDetails(anyInt())).thenReturn(new Order(orderNumber, Timestamp.valueOf(LocalDateTime.now()), "status"));

        apiHandler.viewOrderDetails(context);
        Map<String, Object> response = JavalinTestUtility.getResponse(context);
        assertEquals(response.get("orderNumber"), orderNumber);
    }

    @Test
    public void testViewOrdersOpen() {
        String openStatus = OrderStatus.OPEN.getStatus();
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_ORDERS,
                    "status", openStatus);
        when(erpDAO.getOrders(eq(openStatus))).thenReturn(Arrays.asList(
                new Order(1, Timestamp.valueOf(LocalDateTime.now()), openStatus),
                new Order(2, Timestamp.valueOf(LocalDateTime.now()), openStatus)
        ));

        apiHandler.viewOrders(context);
        List<Map<String, Object>> response = JavalinTestUtility.getResponse(context, new TypeReference<List<Map<String, Object>>>(){});

        for (Map<String, Object> order : response) {
            assertEquals(openStatus, order.get("status"));
        }
    }

    @Test
    public void testViewOrdersLocked() {
        String lockedStatus = OrderStatus.LOCKED.getStatus();
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_ORDERS,
                "status", lockedStatus);
        when(erpDAO.getOrders(eq(lockedStatus))).thenReturn(Arrays.asList(
                new Order(1, Timestamp.valueOf(LocalDateTime.now()), lockedStatus),
                new Order(2, Timestamp.valueOf(LocalDateTime.now()), lockedStatus)
        ));

        apiHandler.viewOrders(context);
        List<Map<String, Object>> response = JavalinTestUtility.getResponse(context, new TypeReference<List<Map<String, Object>>>(){});

        for (Map<String, Object> order : response) {
            assertEquals(lockedStatus, order.get("status"));
        }
    }

    @Test
    public void testViewOrdersDone() {
        String doneStatus = OrderStatus.DONE.getStatus();
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_ORDERS,
                "status", doneStatus);
        when(erpDAO.getOrders(eq(doneStatus))).thenReturn(Arrays.asList(
                new Order(1, Timestamp.valueOf(LocalDateTime.now()), doneStatus),
                new Order(2, Timestamp.valueOf(LocalDateTime.now()), doneStatus)
        ));

        apiHandler.viewOrders(context);
        List<Map<String, Object>> response = JavalinTestUtility.getResponse(context, new TypeReference<List<Map<String, Object>>>(){});

        for (Map<String, Object> order : response) {
            assertEquals(doneStatus, order.get("status"));
        }
    }
}
