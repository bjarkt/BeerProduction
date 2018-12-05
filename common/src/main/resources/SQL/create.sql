-- noinspection SqlNoDataSourceInspectionForFile

/* BEWARE: Resets auto-increment */
/*TRUNCATE TABLE Orders, order_items, batches RESTART IDENTITY;*/

DROP TABLE IF EXISTS Order_items, Orders, Recipes, Queue_items, Batches, Measurement_logs, State_time_logs;

SET TIME ZONE 'Europe/Copenhagen';

CREATE TABLE Orders (
	date_created TIMESTAMP default CURRENT_TIMESTAMP,
	status VARCHAR(255) default ('open'),
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
INSERT INTO Recipes VALUES (0, 'pilsner', 0, 600);
INSERT INTO Recipes VALUES (1, 'wheat', 0, 300);
INSERT INTO Recipes VALUES (2, 'ipa', 0, 150);
INSERT INTO Recipes VALUES (3, 'stout', 0, 200);
INSERT INTO Recipes VALUES (4, 'ale', 0, 100);
INSERT INTO Recipes VALUES (5, 'alcohol free', 0, 125);

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
	accepted INT default 0,
	defect INT default 0,
	machine_speed INT,
	PRIMARY KEY(beer_name, order_number)
);

CREATE TABLE Measurement_logs (
	batch_id BIGINT,
	measurement_time TIMESTAMP default CURRENT_TIMESTAMP,
	temperature DECIMAL,
	humidity DECIMAL,
	vibration DECIMAL,
	PRIMARY KEY (batch_id, measurement_time)
);

CREATE TABLE State_time_logs (
	batch_id BIGINT,
	phase INT,
	time_elapsed INT,
	PRIMARY KEY(batch_id, phase)
);