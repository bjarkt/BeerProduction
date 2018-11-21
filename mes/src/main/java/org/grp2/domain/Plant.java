package org.grp2.domain;

import org.grp2.dao.MesDAO;
import org.grp2.printmanager.IPrintManager;
import org.grp2.printmanager.SimplePdfPrinter1;
import org.grp2.printmanager.SimplePdfPrinter2;

public class Plant {

    private IPrintManager printManager;
    private MesDAO mesDAO;
    private UnirestWrapper unirestWrapper;

    public Plant(MesDAO mesDAO, UnirestWrapper unirestWrapper) {
        printManager = new SimplePdfPrinter1();
        this.unirestWrapper = unirestWrapper;
        this.mesDAO = mesDAO;
        this.printManager.setPath("report.pdf");
    }

    public IPrintManager getPrintManager() {
        return printManager;
    }

    public MesDAO getMesDAO() {
        return mesDAO;
    }

    public UnirestWrapper getUnirestWrapper() {
        return unirestWrapper;
    }
}
