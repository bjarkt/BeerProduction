package org.grp2;

import org.grp2.api.API;

public class ScadaServer {
    public static void main(String[] args) {
        API api = new API(7000);
        api.start();
    }
}
