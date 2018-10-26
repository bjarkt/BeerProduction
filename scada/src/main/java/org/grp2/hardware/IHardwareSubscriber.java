package org.grp2.hardware;

public interface IHardwareSubscriber {
	/**
	 * Subscribe to a value in the cube.
	 * When the value is changes the callback will be executed.
	 * The condition check happens every interval.
	 * @param node any node
	 * @param callback any callback
	 * @param interval interval in milliseconds
	 */
	void subscribe(CubeNodeId node, SubscribeCallback callback, int interval);
}