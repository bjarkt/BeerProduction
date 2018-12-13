package org.grp2.hardware;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class Hardware implements IHardware {
    private OpcUaClient client;
    private IHardwareProvider provider;
    private IHardwareSubscriber subscriber;

    public Hardware(String endpointUrl) {
        try {
            client = buildClient(endpointUrl);
            client.connect().get();

            provider = new HardwareProvider(client);
            subscriber = new HardwareSubscriber(client);
        } catch (ExecutionException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IHardwareProvider getProvider() {
        return provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IHardwareSubscriber getSubscriber() {
        return subscriber;
    }

    /**
     * Creates client and connect it to given URL. <p></p>
     * {@link IHardware#CUBE_URL} or {@link IHardware#SIMULATION_URL}
     *
     * @param endpointUrl any URL
     * @return the created OPC-UA client
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private OpcUaClient buildClient(String endpointUrl) throws ExecutionException, InterruptedException, URISyntaxException {
        EndpointDescription[] endpoints = UaTcpStackClient.getEndpoints(endpointUrl).get();

        EndpointDescription endpoint = updateEndpointUrl(endpoints[0], endpointUrl);

        OpcUaClientConfig config = OpcUaClientConfig.builder()
                .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
                .setApplicationUri("urn:eclipse:milo:test:client")
                .setEndpoint(endpoint)
                .setIdentityProvider(new UsernameProvider("sdu", "1234"))
                .setSessionName(() -> "api-test")
                .build();

        return new OpcUaClient(config);
    }

    private EndpointDescription updateEndpointUrl(
            EndpointDescription original, String url) throws URISyntaxException {

        URI uri = new URI(original.getEndpointUrl()).parseServerAuthority();

        String hostname = url.split(":")[1].substring(2); // split on :, remove //

        String endpointUrl = String.format(
                "%s://%s:%s%s",
                uri.getScheme(),
                hostname,
                uri.getPort(),
                uri.getPath()
        );

        return new EndpointDescription(
                endpointUrl,
                original.getServer(),
                original.getServerCertificate(),
                original.getSecurityMode(),
                original.getSecurityPolicyUri(),
                original.getUserIdentityTokens(),
                original.getTransportProfileUri(),
                original.getSecurityLevel()
        );
    }
}