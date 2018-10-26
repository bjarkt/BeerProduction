package org.grp2.hardware;

public interface SubscribeCallback {
	/**
	 * The callback action. Whatever in scope is executed when invoked.
	 * @param value accessible value
	 */
	void action(Object value);
}