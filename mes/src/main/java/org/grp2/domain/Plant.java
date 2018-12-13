package org.grp2.domain;

import org.grp2.data.MesDAO;
import org.grp2.domain.optimizer.IOptimizer;
import org.grp2.domain.optimizer.Optimizer;
import org.grp2.domain.printmanager.IPrintManager;
import org.grp2.domain.printmanager.SimplePdfPrinter1;
import org.grp2.utility.UnirestWrapper;

public class Plant {

    private IPrintManager printManager;
    private MesDAO mesDAO;
    private IOptimizer optimizer;
    private UnirestWrapper unirestWrapper;

    public Plant(MesDAO mesDAO, UnirestWrapper unirestWrapper) {
        printManager = new SimplePdfPrinter1();
        this.optimizer = new Optimizer();
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

    public IOptimizer getOptimizer() {
        return optimizer;
    }

    public UnirestWrapper getUnirestWrapper() {
        return unirestWrapper;
    }
}
