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

	@Override
	public void start() {
		writeMachineCommand(2);
		executeCommand();
	}

	@Override
	public void stop() {
		writeMachineCommand(3);
		executeCommand();
	}

	@Override
	public void reset() {
		writeMachineCommand(1);
		executeCommand();
	}

	@Override
	public void clear() {
		writeMachineCommand(5);
		executeCommand();
	}

	@Override
	public void abort() {
		writeMachineCommand(4);
		executeCommand();
	}

	@Override
	public float getBatchId() {
		return (float) readValue(CubeNodeId.READ_BATCH_ID);
	}

	@Override
	public void setBatchId(float id) {
		writeValue(CubeNodeId.WRITE_BATCH_ID, id);
	}

	@Override
	public float getProduct() {
		return (float) readValue(CubeNodeId.READ_PRODUCT);
	}

	@Override
	public void setProduct(float productId) {
		writeValue(CubeNodeId.WRITE_PRODUCT, productId);
	}

	@Override
	public float getAmountToProduce() {
		return (float) readValue(CubeNodeId.READ_AMOUNT_TO_PRODUCE);
	}

	@Override
	public void setAmountToProduce(float amount) {
		writeValue(CubeNodeId.WRITE_AMOUNT_TO_PRODUCE, amount);
	}

	@Override
	public float getMachSpeed() {
		return (float) readValue(CubeNodeId.READ_MACH_SPEED);
	}

	@Override
	public float getMachSpeedPercentage() {
		return (float) readValue(CubeNodeId.READ_MACH_SPEED_PCT);
	}

	@Override
	public void setMachSpeed(float speed) {
		writeValue(CubeNodeId.WRITE_MACH_SPEED, speed);
	}

	@Override
	public int getCurrentBeersProduced() {
		return (int) readValue(CubeNodeId.READ_CURRENT_PRODUCED);
	}

	@Override
	public int getAcceptedBeersProduced() {
		return getCurrentBeersProduced() - getDefectiveBeersProduced();
	}

	@Override
	public int getDefectiveBeersProduced() {
		return (int) readValue(CubeNodeId.READ_CURRENT_DEFECTIVE);
	}

	@Override
	public float getHumidity() {
		return (float) readValue(CubeNodeId.READ_HUMIDITY);
	}

	@Override
	public float getTemperature() {
		return (float) readValue(CubeNodeId.READ_TEMPERAURE);
	}

	@Override
	public float getVibration() {
		return (float) readValue(CubeNodeId.READ_VIBRATION);
	}

	@Override
	public int getState() {
		return (int) readValue(CubeNodeId.READ_STATE);
	}

	@Override
	public int getStopReason() {
		return (int) readValue(CubeNodeId.READ_STOP_REASON);
	}

	private DataValue getDataValue(Object o) {
		return DataValue.valueOnly(new Variant(o));
	}

	private Object readValue(CubeNodeId node) {
		Object output = null;

		NodeId nodeId = HardwareUtil.getNodeId(node);

		try {
			output = client.readValue(0, TimestampsToReturn.Both, nodeId).get().getValue().getValue();
		} catch(InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return output;
	}

	private void writeValue(CubeNodeId node, Object input) {
		NodeId nodeId = HardwareUtil.getNodeId(node);
		DataValue dv = getDataValue(input);

		try {
			client.writeValue(nodeId, dv).get();
		} catch(InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(75);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void executeCommand() {
		writeValue(CubeNodeId.EXECUTE_MACH_CMD, true);
	}

	private void writeMachineCommand(int commandId) {
		writeValue(CubeNodeId.WRITE_MACH_CMD, commandId);
	}
}