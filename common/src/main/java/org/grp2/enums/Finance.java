package org.grp2.enums;

public enum Finance {


    PILSNER("pilsner", 4, 6),
    WHEAT("wheat", 5, 7),
    IPA("ipa", 4, 8),
    STOUT("stout", 6, 8),
    ALE("ale", 6, 7),
    ALCOHOLFREE("alcohol free", 6, 11);

    private final String name;
    private final int profit;
    private final int cost;

    Finance(String name, int profit, int cost){
        this.name = name;
        this.profit = profit;
        this.cost = cost;
    }

    public int getProfit() {
        return profit;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }
}
