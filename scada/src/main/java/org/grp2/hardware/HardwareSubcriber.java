package org.grp2.hardware;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class HardwareSubcriber implements IHardwareSubcriber {
	private OpcUaClient client;

	public HardwareSubcriber(OpcUaClient client) {
		this.client = client;
		client.getSubscriptionManager().clearSubscriptions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subcribe(CubeNodeId node, SubcribeCallback callback, int interval) {
		ReadValueId readValueId =
				new ReadValueId(HardwareUtil.getNodeId(node), AttributeId.Value.uid(),
						null, null);

		int clientHandler = 123456789;

		MonitoringParameters parameters =
				new MonitoringParameters(uint(clientHandler), (double) interval, null, uint(clientHandler), true);

		MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(readValueId, MonitoringMode.Reporting, parameters);

		BiConsumer<UaMonitoredItem, DataValue> consumer = (item, value) -> {
			callback.action(value.getValue().getValue());
		};

		BiConsumer<UaMonitoredItem, Integer> onItemCreated = (monitoredItem, integer) -> {
			monitoredItem.setValueConsumer(consumer);
		};

		try {
			UaSubscription sub = client.getSubscriptionManager().createSubscription(1000).get();

			List<UaMonitoredItem> items = sub.createMonitoredItems(TimestampsToReturn.Both,
					Arrays.asList(request),
					onItemCreated).get();

		} catch(InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}