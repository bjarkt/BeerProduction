package org.grp2.hardware;

public interface IHardwareProvider {
    /**
     * Execute start command on cube.
     */
    void start();

    /**
     * Execute stop command on cube.
     */
    void stop();

    /**
     * Execute reset command on cube.
     */
    void reset();

    /**
     * Execute clear command on cube.
     */
    void clear();

    /**
     * Execute abort command on cube.
     */
    void abort();

    /**
     * Gets the batchId of the current executing batch.
     *
     * @return batchId
     */
    float getBatchId();

    /**
     * Sets the batchId of the next batch.
     *
     * @param id any id
     */
    void setBatchId(float id);

    /**
     * Gets the product id of the current executing batch.
     * <p></p>
     * 0 = Pilsner <p>
     * 1 = Wheat <p>
     * 2 = IPA <p>
     * 3 = Stout <p>
     * 4 = Ale <p>
     * 5 = Alcohol Free
     *
     * @return product id
     */
    float getProduct();

    /**
     * Sets the product id of the next batch.
     * <p></p>
     * 0 = Pilsner <p>
     * 1 = Wheat <p>
     * 2 = IPA <p>
     * 3 = Stout <p>
     * 4 = Ale <p>
     * 5 = Alcohol Free
     *
     * @param productId id from 0 to 5
     */
    void setProduct(float productId);

    /**
     * Gets the amount of beers to produce in the current executing batch.
     *
     * @return amount of beers to produce
     */
    float getAmountToProduce();

    /**
     * Sets the amount of beers to produce of the next batch.
     *
     * @param amount amount from 0 to 65535
     */
    void setAmountToProduce(float amount);

    /**
     * Gets the machine speed of the current executing batch.
     *
     * @return the machine speed
     */
    float getMachSpeed();

    /**
     * Gets the normalized machine speed of the current executing batch.
     *
     * @return the machine speed from 0 to 100 %
     */
    float getMachSpeedPercentage();

    /**
     * Sets the machine speed of the next batch.
     * <p></p>
     * (0) Pilsner: 0 \u2264 value \u2264 600 <p>
     * (1) Wheat: 0 \u2264 value \u2264 300 <p>
     * (2) IPA: 0 \u2264 value \u2264 150 <p>
     * (3) Stout: 0 \u2264 value \u2264 200 <p>
     * (4) Ale: 0 \u2264 value \u2264 100 <p>
     * (5) Alcohol Free: 0 \u2264 value \u2264 125 <p>
     *
     * @param speed machine speed from 0 to the products maximum machine speed
     */
    void setMachSpeed(float speed);

    /**
     * Gets the amount of beers produced in the current executing batch.
     *
     * @return beers produced
     */
    int getCurrentBeersProduced();

    /**
     * Gets the amount of beers produced in the current executing batch which are are accepted.
     *
     * @return accepted beers produced
     */
    int getAcceptedBeersProduced();

    /**
     * Gets the amount of beers produced in the current executing batch which are are defective.
     *
     * @return defective beers produced
     */
    int getDefectiveBeersProduced();

    /**
     * @return the humidity
     * @// TODO: 25-10-2018 Update javadoc
     * Gets the humidity of the current executing batch.
     */
    float getHumidity();

    /**
     * @return the temperature
     * @// TODO: 25-10-2018 Update javadoc
     * Gets the temperature of the current executing batch.
     */
    float getTemperature();

    /**
     * @return the vibration
     * @// TODO: 25-10-2018 Update javadoc
     * Gets the vibration of the current executing batch.
     */
    float getVibration();

    /**
     * Gets the current {@link org.grp2.enums.State} as int of the machine.
     *
     * @return a state from 0 to 19
     */
    int getState();

    /**
     * @// TODO: 25-10-2018 Update javadoc
     * WIP-Doens't work...
     */
    @Deprecated
    int getStopReason();
}