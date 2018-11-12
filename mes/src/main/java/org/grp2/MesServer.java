package org.grp2;

import org.grp2.api.API;

public class MesServer {
	public static void main(String[] args) {
		API api = new API(7001);
		api.start();
	}
}
