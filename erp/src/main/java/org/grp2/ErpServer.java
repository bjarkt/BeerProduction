package org.grp2;

import org.grp2.api.API;

public class ErpServer {
    public static void main(String[] args) {
        API api = new API(7002);
        api.start();
    }
}
