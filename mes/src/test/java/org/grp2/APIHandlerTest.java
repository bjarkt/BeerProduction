package org.grp2;

import com.mashape.unirest.http.exceptions.UnirestException;
import io.javalin.Context;
import org.grp2.api.APIHandler;
import org.grp2.api.APIRoutes;
import org.grp2.dao.MesDAO;
import org.grp2.domain.*;
import org.grp2.javalin.Message;
import org.grp2.shared.Batch;
import org.grp2.shared.Order;
import org.grp2.utility.JavalinTestUtility;
import org.grp2.utility.UnirestWrapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class APIHandlerTest {

    @Mock
    private MesDAO mockMesDAO;

    @Mock
    private UnirestWrapper unirestWrapper;

    private APIHandler apiHandler;

    private String basePath = "/api";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        apiHandler = new APIHandler(mockMesDAO, unirestWrapper);
    }

    @Test
    public void testViewOrders() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_ORDERS);

        when(mockMesDAO.getLockedOrders()).thenReturn(Collections.singletonList(
                new Order(1, Timestamp.valueOf(LocalDateTime.now()), "locked")));

        apiHandler.viewOrders(context);
        List<Map<String, Object>> result = JavalinTestUtility.getResponse(context, List.class);

        assertEquals(1, result.get(0).get("orderNumber"));
    }

    @Test
    public void testViewOrdersEmpty() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_ORDERS);

        apiHandler.viewOrders(context);
        List result = JavalinTestUtility.getResponse(context, List.class);

        assertNotNull(result);
    }

    @Test
    public void testViewOrderItems() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_ORDER_ITEMS,
                "order-number", "1");

        apiHandler.viewOrderItems(context);

        Map<String, Object> result = JavalinTestUtility.getResponse(context);

        assertNotNull(result.get("Recipe"));
        assertNotNull(result.get("OrderItems"));
    }

    @Test
    public void testViewAllBatches() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_ALL_BATCHES);

        when(mockMesDAO.viewAllBatches()).thenReturn(Collections.singletonList(new Batch("pilsner", 1, 1, LocalDateTime.now(), LocalDateTime.now(), 0, 0, 0)));

        apiHandler.viewAllBatches(context);

        List<Batch> result = (List<Batch>) JavalinTestUtility.getResponse(context, List.class);

        assertTrue(result.size() >= 1);
    }

    @Test
    public void testViewAllBatchesEmpty() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_ALL_BATCHES);

        apiHandler.viewAllBatches(context);

        List result = JavalinTestUtility.getResponse(context, List.class);

        assertNotNull(result);
    }

    @Test
    public void testViewPlantStatisticsSuccess() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_PLANT_STATISTICS,
                Collections.singletonMap("days", "10"));

        when(mockMesDAO.viewPlantStatistics(any(), any())).thenReturn(new PlantStatistics(new MeasurementsStatistics(-1.0, -1.0, -1.0), new BatchStatistics(-1, -1, -1, null)));

        apiHandler.viewPlantStatistics(context);

        PlantStatistics plantStatistics = JavalinTestUtility.getResponse(context, PlantStatistics.class);

        assertEquals(-1, plantStatistics.getMeasurementsStatistics().getAvgTemp(), 0);
    }

    @Test
    public void testViewPlantStatisticsFailure() {
        String badDaysValue = "asdf";
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_PLANT_STATISTICS,
                Collections.singletonMap("days", badDaysValue));

        when(mockMesDAO.viewPlantStatistics(any(), any())).thenReturn(new PlantStatistics(new MeasurementsStatistics(-1.0, -1.0, -1.0), new BatchStatistics(-1, -1, -1, null)));

        apiHandler.viewPlantStatistics(context);

        Message response = JavalinTestUtility.getResponse(context, Message.class);

        assertEquals("Bad value for query-param: " + badDaysValue, response.getMessage());
        assertEquals(422, response.getStatus());
    }

    @Test
    public void testCreateBatchesSuccess() throws UnirestException {
        String bodyJson = "{\"orderItems\":[{\"recipeName\":\"pilsner\",\"orderNumber\":1,\"machineSpeed\":600,\"quantity\":100,\"batchId\":1}]}";

        Map<String, String> pathParams = Collections.singletonMap("batch-id", "1");
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.CREATE_BATCHES, null,
                bodyJson, pathParams);


        when(unirestWrapper.post(anyString(), any())).thenReturn(new Message(200, "Success"));

        apiHandler.createBatches(context);
        Message response = JavalinTestUtility.getResponse(context, Message.class);

        assertEquals("Success", response.getMessage());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testCreateBatchesScadaFailure() throws UnirestException {
        String bodyJson = "{\"orderItems\":[{\"recipeName\":\"pilsner\",\"orderNumber\":1,\"machineSpeed\":600,\"quantity\":100,\"batchId\":1}]}";

        Map<String, String> pathParams = Collections.singletonMap("batch-id", "1");
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.CREATE_BATCHES, null,
                bodyJson, pathParams);

        when(unirestWrapper.post(anyString(), any())).thenReturn(
                new Message(422, "Error from SCADA : <error message>"));

        apiHandler.createBatches(context);
        Message response = JavalinTestUtility.getResponse(context, Message.class);

        assertTrue(response.getMessage().startsWith("Error from SCADA"));
        assertEquals(422, response.getStatus());
    }

    @Test
    public void testCreateBatchesJSONFailure() {
        String bodyJson = "bla bla bla, bad json";

        Map<String, String> pathParams = Collections.singletonMap("batch-id", "1");
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.CREATE_BATCHES, null,
                bodyJson, pathParams);

        apiHandler.createBatches(context);
        Message response = JavalinTestUtility.getResponse(context, Message.class);

        assertEquals(422, response.getStatus());
        assertTrue(response.getMessage().startsWith("JSON error"));
    }

    @Test
    public void testGetReportSuccess() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.GET_REPORT,
                "batch-id", "1");

        when(mockMesDAO.getMeasurementLogs(anyInt())).thenReturn(new ArrayList<>());
        when(mockMesDAO.getBatch(anyInt())).thenReturn(new Batch("", -1, -1,
                LocalDateTime.now(), LocalDateTime.now(), -1, -1, -1));

        apiHandler.getReport(context);
        byte[] pdfData = new byte[context.resultString().length()];
        DataInputStream dis = new DataInputStream(context.resultStream());
        try {
            dis.readFully(pdfData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(pdfData.length > 500); // 500 is larger than 404 error message
    }

    @Test
    public void testGetReportFailure() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.GET_REPORT,
                "batch-id", "1");

        when(mockMesDAO.getMeasurementLogs(anyInt())).thenReturn(null);
        when(mockMesDAO.getBatch(anyInt())).thenReturn(null);

        apiHandler.getReport(context);
        Message response = JavalinTestUtility.getResponse(context, Message.class);

        Assert.assertEquals(404, response.getStatus());
    }

    @Test
    public void testGetOEE() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.GET_OEE,
                "batch-id", "1");

        when(mockMesDAO.getOEE(anyInt())).thenReturn(
                new OEE(new Batch("testBeerName", -1, -1, null,
                        null, 100, 0, 300), 117.0));

        apiHandler.getOEE(context);
        OEE oeeResponse = JavalinTestUtility.getResponse(context, OEE.class);

        assertEquals(-4.85, oeeResponse.getOEE(), 0);
    }
}
