package org.grp2.optimizer;

import org.grp2.shared.Recipe;

public class Optimizer implements IOptimizer {

    public static void main(String[] args) {
        IOptimizer optimizer = new Optimizer();
        Recipe recipe = new Recipe(0, "stout", 0, 200);
        System.out.println("Most Profitable: " + optimizer.getOptimalMachSpeed(new Beer(recipe, 4, 6)));
        System.out.println("Fastest: " + optimizer.getFastestMachSpeed(new Beer(recipe, 4, 6), 50));
        System.out.println("Most Saving: " + optimizer.getMostSavingMachSpeed(new Beer(recipe, 4, 6)));
    }

    public Optimizer(){

    }

    @Override
    public int getOptimalMachSpeed(Beer beer) {
        // Initialize variables
        int optimalMachSpeed = -1;
        double maxProfitPerMinute = -1;

        // Iterate through every possible machine speed:
        for(int i = beer.getMinSpeed(); i <= beer.getMaxSpeed(); i++){
            // Ignore machine 0.
            if(i == 0) continue;
            // Calculate defect and accepted beers.
            int defectBeers = this.calculateDefectPct(i, beer.getName());
            int acceptedBeers = 100 - defectBeers;
            // Ignore machine speed if 0 accepted (failure).
            if(defectBeers >= 100) continue;
            // Calculate profit per minute.
            double temp = ((100*beer.getProfit()) - ((defectBeers * ((double) 100/acceptedBeers))*beer.getCost())) * ((double) i/100);
            // Set optimalMachSpeed if profit is higher.
            if(temp > maxProfitPerMinute){
                maxProfitPerMinute = temp;
                optimalMachSpeed = i;
            }
        }

        return optimalMachSpeed;
    }

    @Override
    public int getFastestMachSpeed(Beer beer, int quantity) {
        // Initialize variables
        int optimalMachSpeed = -1;
        double productionTime = Integer.MAX_VALUE;

        // Iterate through every possible machine speed:
        for(int i = beer.getMinSpeed(); i <= beer.getMaxSpeed(); i++){
            // Ignore machine 0.
            if(i == 0) continue;
            // Ignore if defect rate equals or is above 100%.
            if(this.calculateDefectPct(i, beer.getName()) >= 100) continue;
            int defectBeers = (int)(((double) quantity/100) * this.calculateDefectPct(i, beer.getName()));
            int acceptedBeers = quantity - defectBeers;
            double temp = (quantity + (defectBeers * ((double) quantity/acceptedBeers))) / i;
            if(temp < productionTime){
                productionTime = temp;
                optimalMachSpeed = i;
            }
        }

        return optimalMachSpeed;
    }

    @Override
    public int getMostSavingMachSpeed(Beer beer) {
        // Initialize variables
        int optimalMachSpeed = -1;
        double minDefectPercentage = Integer.MAX_VALUE;

        // Iterate through every possible machine speed:
        for(int i = beer.getMinSpeed(); i <= beer.getMaxSpeed(); i++){
            // Ignore machine speed 0.
            if(i == 0) continue;
            // Ignore if defect rate equals or is above 100%.
            if(this.calculateDefectPct(i, beer.getName()) >= 100) continue;
            int defectBeersPercentage = this.calculateDefectPct(i, beer.getName());
            if(defectBeersPercentage <= minDefectPercentage){
                minDefectPercentage = defectBeersPercentage;
                optimalMachSpeed = i;
            }
        }

        return optimalMachSpeed;
    }

    /**
     * Uses functions derived from regression to estimate percentage defect beers.
     * @param machSpeed machine speed
     * @param beerType  type of beer
     * @return  percentage of defect beers at a given machine speed.
     */
    private int calculateDefectPct(int machSpeed, String beerType){
        switch (beerType){
            // Exponential
            case "pilsner": return (int) Math.ceil(0.0887*Math.pow(Math.E,0.0109*machSpeed));
            // Linear
            case "wheat": return (int) Math.ceil(0.34*machSpeed -1.17);
            // Exponential
            case "ipa": return (int) Math.ceil(0.455*Math.pow(Math.E,0.0372*machSpeed));
            // Polynomial
            case "stout": return (int) Math.ceil((49.2) + (-0.115*machSpeed) + (1.65*Math.pow(10,-3))*(Math.pow(machSpeed,2)) + (-1.05*Math.pow(10,-5))*(Math.pow(machSpeed,3)));
            // Exponential
            case "ale": return (int) Math.ceil(1.11*Math.pow(Math.E,0.0361*machSpeed));
            // Polynomial
            case "alcohol free": return (int) Math.ceil((24.1) + (-0.203*machSpeed) + (4.96*Math.pow(10,-3))*(Math.pow(machSpeed,2)));

        }
        return 0;
    }

}
