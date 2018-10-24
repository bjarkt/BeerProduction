package org.grp2.hardware;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

import java.util.concurrent.ExecutionException;

public class Hardware implements IHardware {
	private OpcUaClient client;
	private IHardwareProvider provider;
	private IHardwareSubcriber subcriber;

	public Hardware(String endpointUrl) {
		try {
			client = buildClient(endpointUrl);
			client.connect().get();

			provider = new HardwareProvider(client);
			subcriber = new HardwareSubcriber(client);
		} catch(ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IHardwareProvider getProvider() {
		return provider;
	}

	@Override
	public IHardwareSubcriber getSubcriber() {
		return subcriber;
	}

	private OpcUaClient buildClient(String endpointUrl) throws ExecutionException, InterruptedException {
		EndpointDescription[] endpoints = UaTcpStackClient.getEndpoints(endpointUrl).get();

		OpcUaClientConfig config = OpcUaClientConfig.builder()
				.setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
				.setApplicationUri("urn:eclipse:milo:test:client")
				.setEndpoint(endpoints[0])
				.setIdentityProvider(new UsernameProvider("sdu", "1234"))
				.setSessionName(() -> "api-test")
				.build();

		return new OpcUaClient(config);
	}
}