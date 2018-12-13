package org.grp2.unitTest;

import org.grp2.domain.optimizer.IOptimizer;
import org.grp2.domain.optimizer.Optimizer;
import org.grp2.enums.Finance;
import org.grp2.shared.Beer;
import org.grp2.shared.Recipe;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OptimizerTest {

    private IOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new Optimizer();
    }

    @Test
    public void testGetOptimalMachSpeed() {
        Beer[] testBeer = getBeers();

        for (Beer beer : testBeer) {
            int speed = optimizer.getOptimalMachSpeed(beer);
            assertTrue(speed > 1);
        }
    }

    @Test
    public void testGetFastestMachSpeed() {
        Beer[] testBeer = getBeers();

        for (Beer beer : testBeer) {
            int speed = optimizer.getFastestMachSpeed(beer, 100);
            assertTrue(speed > 1);
        }
    }

    @Test
    public void testGetMostSavingMachSpeed() {
        Beer[] testBeer = getBeers();

        for (Beer beer : testBeer) {
            int speed = optimizer.getMostSavingMachSpeed(beer);
            assertTrue(speed > 1);
        }
    }

    private Beer[] getBeers() {
        Recipe pilsner = new Recipe(-1, Finance.PILSNER.getName(), 0, 600);
        Recipe wheat = new Recipe(-1, Finance.WHEAT.getName(), 0, 300);
        Recipe ipa = new Recipe(-1, Finance.IPA.getName(), 0, 150);
        Recipe stout = new Recipe(-1, Finance.STOUT.getName(), 0, 200);
        Recipe ale = new Recipe(-1, Finance.ALE.getName(), 0, 100);
        Recipe alcoholFree = new Recipe(-1, Finance.ALCOHOLFREE.getName(), 0, 125);

        return new Beer[]{
                new Beer(pilsner, Finance.PILSNER.getProfit(), Finance.PILSNER.getCost()),
                new Beer(wheat, Finance.WHEAT.getProfit(), Finance.WHEAT.getCost()),
                new Beer(ipa, Finance.IPA.getProfit(), Finance.IPA.getCost()),
                new Beer(stout, Finance.STOUT.getProfit(), Finance.STOUT.getCost()),
                new Beer(ale, Finance.ALE.getProfit(), Finance.ALE.getCost()),
                new Beer(alcoholFree, Finance.ALCOHOLFREE.getProfit(), Finance.ALCOHOLFREE.getCost())
        };
    }

}
