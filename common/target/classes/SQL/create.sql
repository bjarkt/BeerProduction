DROP TABLE Order_items, Orders, Recipes, Queue_items, Batches, Measurement_logs, State_time_logs;

CREATE TABLE Orders (
	date_created TIMESTAMP default CURRENT_TIMESTAMP,
	status VARCHAR(255) default ('nonProcessed'),
	order_number BIGSERIAL PRIMARY KEY
);

CREATE TABLE Order_items (
	quantity INT,
	order_number BIGINT,
	status VARCHAR(255) default ('nonProcessed'),
	beer_name VARCHAR(255),
	PRIMARY KEY(beer_name, order_number),
	FOREIGN KEY (order_number) REFERENCES Orders(order_number)
);

CREATE TABLE Recipes (
	id INT PRIMARY KEY,
	name VARCHAR(255) UNIQUE,
	min_speed INT,
	max_speed INT
);

CREATE TABLE Queue_items (
	batches_id BIGSERIAL PRIMARY KEY,
	quantity INT,
	machine_speed INT,
	recipe_name VARCHAR(255),
	order_number BIGINT
);

CREATE TABLE Batches (
	beer_name VARCHAR(255),
	order_number BIGINT,
	batch_id BIGINT UNIQUE,
	started TIMESTAMP default CURRENT_TIMESTAMP,
	finished TIMESTAMP,
	accepted INT,
	defect INT,
	PRIMARY KEY(beer_name, order_number)
);

CREATE TABLE Measurement_logs (
	batch_id BIGINT PRIMARY KEY,
	measurement_time TIMESTAMP default CURRENT_TIMESTAMP,
	temperature DECIMAL,
	humidity DECIMAL
);

CREATE TABLE State_time_logs (
	batch_id BIGINT,
	phase INT,
	time_elapsed INT,
	PRIMARY KEY(batch_id, phase)
);