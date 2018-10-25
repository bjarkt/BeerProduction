package org.grp2.hardware;

public interface IHardwareSubcriber {
	/**
	 * Subscribe to a value in the cube.
	 * When the value is changes the callback will be executed.
	 * The condition check happens every interval.
	 * @param node any node
	 * @param callback any callback
	 * @param interval interval in milliseconds
	 */
	void subcribe(CubeNodeId node, SubcribeCallback callback, int interval);
}