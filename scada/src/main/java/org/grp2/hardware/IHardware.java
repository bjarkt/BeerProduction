package org.grp2.hardware;

public interface IHardware {
	String SIMULATION_URL = "opc.tcp://127.0.0.1:4840";
	String DOCKER_SIMULATION_URL = "opc.tcp://host.docker.internal:4840";
	String CUBE_URL = "opc.tcp://10.112.254.165:4840";

	/**
	 * Gets the provider which allows getting and setting values on the cube/simulation.
	 * @return a provider
	 */
	IHardwareProvider getProvider();

	/**
	 * Gets the subscriber which offers subscribing to cube values.
	 * @return a subscriber
	 */
	IHardwareSubscriber getSubscriber();
}