package org.grp2;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.javalin.Context;
import org.grp2.api.APIHandler;
import org.grp2.api.APIRoutes;
import org.grp2.dao.MesDAO;
import org.grp2.domain.OEE;
import org.grp2.domain.UnirestWrapper;
import org.grp2.javalin.Message;
import org.grp2.shared.Batch;
import org.grp2.utility.JavalinTestUtility;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.DataInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
    public void testCreateBatchesSuccess() throws UnirestException {
        String bodyJson = "{\"orderItems\":[{\"recipeName\":\"pilsner\",\"orderNumber\":1,\"machineSpeed\":600,\"quantity\":100,\"batchId\":1}]}";

        Map<String, String> pathParams = Collections.singletonMap("batch-id", "1");
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.CREATE_BATCHES,null,
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
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.CREATE_BATCHES,null,
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
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.CREATE_BATCHES,null,
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
