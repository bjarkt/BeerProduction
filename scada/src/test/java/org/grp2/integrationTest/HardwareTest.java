package org.grp2.integrationTest;

import org.grp2.hardware.Hardware;
import org.grp2.hardware.IHardware;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HardwareTest {
    IHardware hardware = new Hardware(IHardware.SIMULATION_URL);

    @Test
    public void testAbort() {
        // If fails, add Thread Sleep after abort is called.
        hardware.getProvider().abort();

        int state = hardware.getProvider().getState();
        assert (state == 8 || state == 9);
    }

    @Test
    public void testClear() {
        hardware.getProvider().abort();
        hardware.getProvider().clear();

        int state = hardware.getProvider().getState();
        assert (state == 1 || state == 2);
    }

    @Test
    public void testStop() {
        hardware.getProvider().clear();
        hardware.getProvider().stop();

        int state = hardware.getProvider().getState();
        assert (state == 2 || state == 7);
    }

    @Test
    public void testReset() {
        setupBatch();

        int state = hardware.getProvider().getState();
        assert (state == 4 || state == 15);
    }

    @Test
    public void testStart() {
        setupBatch();
        hardware.getProvider().setAmountToProduce(20);
        hardware.getProvider().setMachSpeed(100);
        hardware.getProvider().start();

        int state = hardware.getProvider().getState();
        assert (state == 3 || state == 6);
    }

    @Test
    public void testBatchId() {
        setupBatch();
        hardware.getProvider().setBatchId(1000);
        hardware.getProvider().setAmountToProduce(20);
        hardware.getProvider().setMachSpeed(100);
        hardware.getProvider().start();

        assertEquals(1000, hardware.getProvider().getBatchId(), 0);
    }

    @Test
    public void testProduct() {
        setupBatch();
        hardware.getProvider().setAmountToProduce(20);
        hardware.getProvider().setMachSpeed(100);
        hardware.getProvider().setProduct(3);
        hardware.getProvider().start();

        assertEquals(3, hardware.getProvider().getProduct(), 0);
    }

    @Test
    public void testMachineSpeed() {
        setupBatch();
        hardware.getProvider().setAmountToProduce(20);
        hardware.getProvider().setMachSpeed(100);
        hardware.getProvider().start();

        assertEquals(100, hardware.getProvider().getMachSpeed(), 0);
    }

    @Test
    public void testAmountToProduce() {
        setupBatch();
        hardware.getProvider().setProduct(0);
        hardware.getProvider().setAmountToProduce(20);
        hardware.getProvider().setMachSpeed(100);
        hardware.getProvider().start();

        assertEquals(20, hardware.getProvider().getAmountToProduce(), 0);
    }

    private void setupBatch() {
        hardware.getProvider().clear();
        hardware.getProvider().stop();
        hardware.getProvider().reset();
    }
}