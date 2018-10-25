package org.grp2.hardware;

public interface IHardwareProvider {
	void start();
	void stop();
	void reset();
	void clear();
	void abort();

	float getBatchId();
	void setBatchId(float id);

	float getProduct();
	void setProduct(float productId);

	float getAmountToProduce();
	void setAmountToProduce(float amount);

	float getMachSpeed();
	float getMachSpeedPercentage();
	void setMachSpeed(float speed);

	int getCurrentBeersProduced();
	int getAcceptedBeersProduced();
	int getDefectiveBeersProduced();

	float getHumidity();
	float getTemperature();
	float getVibration();

	int getState();

	@Deprecated
	int getStopReason();
}