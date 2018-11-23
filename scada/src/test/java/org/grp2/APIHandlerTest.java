package org.grp2;

import io.javalin.Context;
import org.grp2.api.APIHandler;
import org.grp2.api.APIRoutes;
import org.grp2.data.ScadaDAO;
import org.grp2.domain.Machinery;
import org.grp2.hardware.Hardware;
import org.grp2.hardware.HardwareProvider;
import org.grp2.javalin.Message;
import org.grp2.shared.Batch;
import org.grp2.utility.JavalinTestUtility;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class APIHandlerTest {

    @Mock
    private Machinery machinery;

    private APIHandler apiHandler;

    private String basePath = "/api";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        apiHandler = new APIHandler(machinery);
    }

    @Test
    public void testStartNewProductionEmptyQueue() throws InterruptedException {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.START_NEW_PRODUCTION);

        when(machinery.startBatch()).thenReturn(new Message(200, "Batch started"));

        apiHandler.startNewProduction(context);

        Message response = JavalinTestUtility.getResponse(context, Message.class);

        assertEquals("Batch started", response.getMessage());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testStartNewProductionNonEmptyQueue() throws InterruptedException {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.START_NEW_PRODUCTION);

        when(machinery.startBatch()).thenReturn(new Message(200, "No batch started. Queue is empty, or another batch is currently executing."));

        apiHandler.startNewProduction(context);

        Message response = JavalinTestUtility.getResponse(context, Message.class);

        assertEquals("No batch started. Queue is empty, or another batch is currently executing.", response.getMessage());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testManageProductionGoodChoice() {
        List<String> choices = Arrays.asList("start", "stop", "clear", "abort", "reset");

        when(machinery.getHardware()).thenReturn(mock(Hardware.class));
        when(machinery.getHardware().getProvider()).thenReturn(mock(HardwareProvider.class));

        for (String choice : choices) {
            Context context = JavalinTestUtility.getContext(basePath + APIRoutes.MANAGE_PRODUCTION,
                    "choice", choice);

            apiHandler.manageProduction(context);

            Message response = JavalinTestUtility.getResponse(context, Message.class);

            assertEquals("", response.getMessage());
            assertEquals(200, response.getStatus());
        }

    }

    @Test
    public void testManageProductionBadChoice() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.MANAGE_PRODUCTION,
                "choice", "asdf");

        apiHandler.manageProduction(context);

        Message response = JavalinTestUtility.getResponse(context, Message.class);

        assertEquals("Choice not supported", response.getMessage());
        assertEquals(422, response.getStatus());
    }

    @Test
    public void testViewScreen() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_SCREEN);

        when(machinery.getHardware()).thenReturn(mock(Hardware.class));
        when(machinery.getHardware().getProvider()).thenReturn(mock(HardwareProvider.class));
        when(machinery.getScadaDAO()).thenReturn(mock(ScadaDAO.class));

        apiHandler.viewScreen(context);

        Map<String, Object> screen = JavalinTestUtility.getResponse(context);

        assertNotNull(screen.get("Measurements"));
        assertNotNull(screen.get("BatchOrder"));
        assertNotNull(screen.get("BatchData"));
        assertNotNull(screen.get("State"));
        assertNotNull(screen.get("AmountInQueue"));
    }

    @Test
    public void testViewLogSuccess() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_LOG,
                Collections.singletonMap("batch-id", "1"));

        when(machinery.getScadaDAO()).thenReturn(mock(ScadaDAO.class));
        when(machinery.getScadaDAO().getCurrentBatch()).thenReturn(
                new Batch("beerName", 1, 1, LocalDateTime.now(), LocalDateTime.now(),
                        0, 0, 0));

        apiHandler.viewLog(context);

        Map<String, Object> response = JavalinTestUtility.getResponse(context);

        assertNotNull(response.get("MeasurementLogs"));
        assertNotNull(response.get("StateTimeLogs"));
    }

    @Test
    public void testViewLogBadInput() {
        String badInput = "asdf";
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_LOG,
                Collections.singletonMap("batch-id", badInput));

        apiHandler.viewLog(context);

        Message response = JavalinTestUtility.getResponse(context, Message.class);

        assertEquals("Bad value for query-param: " + badInput, response.getMessage());
        assertEquals(422, response.getStatus());
    }

    @Test
    public void testViewLogNoBatch() {
        Context context = JavalinTestUtility.getContext(basePath + APIRoutes.VIEW_LOG);

        when(machinery.getScadaDAO()).thenReturn(mock(ScadaDAO.class));

        apiHandler.viewLog(context);

        Message response = JavalinTestUtility.getResponse(context, Message.class);

        assertEquals("There need to be batch running if no batch id is sent", response.getMessage());
        assertEquals(422, response.getStatus());
    }
}
