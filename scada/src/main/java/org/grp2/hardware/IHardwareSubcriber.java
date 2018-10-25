package org.grp2.hardware;

public interface IHardwareSubcriber {
	void subcribe(CubeNodeId node, SubcribeCallback callback, int interval);
}