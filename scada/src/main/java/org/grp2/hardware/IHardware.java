package org.grp2.hardware;

public interface IHardware {
	String SIMULATION_URL = "opc.tcp://127.0.0.1:4840";
	String CUBE_URL = "opc.tcp://192.168.1.2:4840";

	IHardwareProvider getProvider();
	IHardwareSubcriber getSubcriber();
}