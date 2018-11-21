package org.grp2.optimizer;

import org.grp2.shared.Recipe;

public class Beer extends Recipe {

    private int profit;
    private int cost;

    public Beer(Recipe recipe, int profit, int cost) {
        super(recipe.getId(), recipe.getName(), recipe.getMinSpeed(), recipe.getMaxSpeed());
        this.profit = profit;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public int getProfit() {
        return profit;
    }
}
