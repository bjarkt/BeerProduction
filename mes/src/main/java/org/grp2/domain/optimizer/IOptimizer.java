package org.grp2.domain.optimizer;

import org.grp2.shared.Beer;

public interface IOptimizer {

    /**
     * Calculates the most profitable machine speed.
     *
     * @param beer {@link Beer} to produce.
     * @return most profitable machine speed.
     */
    int getOptimalMachSpeed(Beer beer);


    /**
     * Calculates the fastest, albeit more expensive, machine speed.
     *
     * @param beer     {@link Beer} to produce.
     * @param quantity number of beers to produce.
     * @return fastest machine speed.
     */
    int getFastestMachSpeed(Beer beer, int quantity);

    /**
     * Calculates the most resource saving machine speed.
     *
     * @param beer {@link Beer} to produce.
     * @return most resource saving machine speed.
     */
    int getMostSavingMachSpeed(Beer beer);

}
