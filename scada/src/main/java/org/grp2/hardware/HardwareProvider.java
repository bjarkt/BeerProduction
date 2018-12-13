package org.grp2.hardware;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

import java.util.concurrent.ExecutionException;

public class HardwareProvider implements IHardwareProvider {
    private OpcUaClient client;

    public HardwareProvider(OpcUaClient client) {
        this.client = client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        writeMachineCommand(2);
        executeCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        writeMachineCommand(3);
        executeCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        writeMachineCommand(1);
        executeCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        writeMachineCommand(5);
        executeCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void abort() {
        writeMachineCommand(4);
        executeCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getBatchId() {
        return (float) readValue(CubeNodeId.READ_BATCH_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBatchId(float id) {
        writeValue(CubeNodeId.WRITE_BATCH_ID, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getProduct() {
        return (float) readValue(CubeNodeId.READ_PRODUCT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProduct(float productId) {
        writeValue(CubeNodeId.WRITE_PRODUCT, productId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getAmountToProduce() {
        return (float) readValue(CubeNodeId.READ_AMOUNT_TO_PRODUCE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAmountToProduce(float amount) {
        writeValue(CubeNodeId.WRITE_AMOUNT_TO_PRODUCE, amount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getMachSpeed() {
        return (float) readValue(CubeNodeId.READ_MACH_SPEED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getMachSpeedPercentage() {
        return (float) readValue(CubeNodeId.READ_MACH_SPEED_PCT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMachSpeed(float speed) {
        writeValue(CubeNodeId.WRITE_MACH_SPEED, speed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentBeersProduced() {
        return (int) readValue(CubeNodeId.READ_CURRENT_PRODUCED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAcceptedBeersProduced() {
        return getCurrentBeersProduced() - getDefectiveBeersProduced();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDefectiveBeersProduced() {
        return (int) readValue(CubeNodeId.READ_CURRENT_DEFECTIVE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHumidity() {
        return (float) readValue(CubeNodeId.READ_HUMIDITY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getTemperature() {
        return (float) readValue(CubeNodeId.READ_TEMPERAURE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getVibration() {
        return (float) readValue(CubeNodeId.READ_VIBRATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getState() {
        return (int) readValue(CubeNodeId.READ_STATE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStopReason() {
        return (int) readValue(CubeNodeId.READ_STOP_REASON);
    }

    /**
     * Wraps the input object in a DataValue.
     *
     * @param o any object
     * @return the datavalue
     */
    private DataValue getDataValue(Object o) {
        return DataValue.valueOnly(new Variant(o));
    }

    /**
     * Reads a value on the located node from the cube.
     *
     * @param node any node to read
     * @return the object read
     */
    private Object readValue(CubeNodeId node) {
        Object output = null;

        NodeId nodeId = HardwareUtil.getNodeId(node);

        try {
            output = client.readValue(0, TimestampsToReturn.Both, nodeId).get().getValue().getValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return output;
    }

    /**
     * Writes the input value to the located node on the cube.
     *
     * @param node  any node to write on
     * @param input the value to be written
     */
    private void writeValue(CubeNodeId node, Object input) {
        NodeId nodeId = HardwareUtil.getNodeId(node);
        DataValue dv = getDataValue(input);

        try {
            client.writeValue(nodeId, dv).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(75);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes the already set machine command on the cube.
     */
    private void executeCommand() {
        writeValue(CubeNodeId.EXECUTE_MACH_CMD, true);
    }

    /**
     * Writes a machine command to be executed on the cube.
     * <p></p>
     * 1 = Reset <p>
     * 2 = Start <p>
     * 3 = Stop <p>
     * 4 = Abort <p>
     * 5 = Clear <p>
     *
     * @param commandId command id from 1 to 5
     */
    private void writeMachineCommand(int commandId) {
        writeValue(CubeNodeId.WRITE_MACH_CMD, commandId);
    }
}