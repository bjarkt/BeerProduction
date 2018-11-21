package org.grp2.domain;

import org.grp2.dao.MesDAO;
import org.grp2.printmanager.IPrintManager;
import org.grp2.printmanager.SimplePdfPrinter1;
import org.grp2.printmanager.SimplePdfPrinter2;

public class Plant {

    private static Plant instance;
    private IPrintManager printManager;
    private MesDAO mesDAO;

    private Plant() {
        printManager = new SimplePdfPrinter1();
        mesDAO = new MesDAO();
        this.printManager.setPath("report.pdf");
    }

    /**
     * Get singleton instance of {@link Plant}
     * @return instance of Plant.
     */
    public static Plant getInstance(){
        if(instance == null){
            instance = new Plant();
        }
        return instance;
    }

    public IPrintManager getPrintManager() {
        return printManager;
    }

    public MesDAO getMesDAO() {
        return mesDAO;
    }
}
