package org.grp2.domain;

import org.grp2.printmanager.IPrintManager;
import org.grp2.printmanager.SimplePdfPrinter1;

public class Plant {

    IPrintManager printManager;

    public Plant() {
        printManager = new SimplePdfPrinter1();
    }

    public IPrintManager getPrintManager() {
        return printManager;
    }
}
