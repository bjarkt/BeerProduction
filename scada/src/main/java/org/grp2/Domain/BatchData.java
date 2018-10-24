package org.grp2.Domain;

public class BatchData {
    private int produced;
    private int acceptable;
    private int defect;

    public BatchData(int produced, int acceptable, int defect) {
        this.produced = produced;
        this.acceptable = acceptable;
        this.defect = defect;
    }

    public int getProduced() {
        return produced;
    }

    public void setProduced(int produced) {
        this.produced = produced;
    }

    public int getAcceptable() {
        return acceptable;
    }

    public void setAcceptable(int acceptable) {
        this.acceptable = acceptable;
    }

    public int getDefect() {
        return defect;
    }

    public void setDefect(int defect) {
        this.defect = defect;
    }
}
