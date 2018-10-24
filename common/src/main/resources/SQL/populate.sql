-- Orders entity

INSERT INTO Orders
VALUES(default, default, default)
RETURNING order_number

UPDATE Orders
SET status = ?
WHERE order_number = ?

SELECT order_number
FROM Orders
WHERE order_number = ?

SELECT *
FROM Orders

-- OrderItems entity

INSERT INTO Order_items
VALUES(?, ?, ?, ?)

UPDATE Order_items
SET status = ?
WHERE order_number = ? AND beer_name = ?

SELECT *
FROM Order_items
WHERE order_number = ?

-- Recipes entity

INSERT INTO Recipes
VALUES(?, ?, ?, ?)

SELECT id
FROM Recipes
WHERE name = ?

-- Batches entity

