-- Create database
CREATE DATABASE `coffee_shop_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the created database
USE `coffee_shop_db`;

-- 1. Create `users` table
CREATE TABLE `users` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) UNIQUE NOT NULL,
    `password_hash` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `role` ENUM("ADMIN", "STAFF") NOT NULL,
    `email` VARCHAR(100) UNIQUE,
    `phone_number` VARCHAR(20) UNIQUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Create `categories` table
CREATE TABLE `categories` (
    `category_id` INT AUTO_INCREMENT PRIMARY KEY,
    `category_name` VARCHAR(100) UNIQUE NOT NULL,
    `type` ENUM("PRODUCT", "INGREDIENT") NOT NULL
);

-- 3. Create `products` table
CREATE TABLE `products` (
    `product_id` INT AUTO_INCREMENT PRIMARY KEY,
    `product_name` VARCHAR(100) UNIQUE NOT NULL,
    `description` TEXT,
    `price` DECIMAL(10, 2) NOT NULL,
    `category_id` INT NOT NULL,
    `image_url` VARCHAR(255),
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`category_id`) REFERENCES `categories`(`category_id`)
);

-- 4. Create `ingredients` table
CREATE TABLE `ingredients` (
    `ingredient_id` INT AUTO_INCREMENT PRIMARY KEY,
    `ingredient_name` VARCHAR(100) UNIQUE NOT NULL,
    `unit` VARCHAR(20) NOT NULL,
    `stock_quantity` DECIMAL(10, 2) DEFAULT 0.0,
    `min_stock_level` DECIMAL(10, 2) DEFAULT 0.0,
    `category_id` INT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`category_id`) REFERENCES `categories`(`category_id`)
);

-- 5. Create `product_ingredients` table (Junction table for N-N relationship)
CREATE TABLE `product_ingredients` (
    `product_ingredient_id` INT AUTO_INCREMENT PRIMARY KEY,
    `product_id` INT NOT NULL,
    `ingredient_id` INT NOT NULL,
    `quantity_needed` DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`product_id`),
    FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients`(`ingredient_id`),
    UNIQUE (`product_id`, `ingredient_id`)
);

-- 6. Create `tables` table
CREATE TABLE `tables` (
    `table_id` INT AUTO_INCREMENT PRIMARY KEY,
    `table_number` VARCHAR(10) UNIQUE NOT NULL,
    `capacity` INT,
    `status` ENUM("AVAILABLE", "OCCUPIED", "RESERVED", "CLEANING") DEFAULT "AVAILABLE"
);

-- 7. Create `customers` table
CREATE TABLE `customers` (
    `customer_id` INT AUTO_INCREMENT PRIMARY KEY,
    `full_name` VARCHAR(100) NOT NULL,
    `phone_number` VARCHAR(20) UNIQUE NOT NULL,
    `email` VARCHAR(100) UNIQUE,
    `loyalty_points` INT DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 8. Create `orders` table
CREATE TABLE `orders` (
    `order_id` INT AUTO_INCREMENT PRIMARY KEY,
    `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `total_amount` DECIMAL(10, 2) NOT NULL,
    `status` ENUM("PENDING", "PREPARING", "COMPLETED", "PAID", "CANCELLED") NOT NULL,
    `order_type` ENUM("DINE_IN", "TAKE_AWAY", "DELIVERY", "ONLINE") NOT NULL,
    `table_id` INT,
    `customer_id` INT,
    `user_id` INT NOT NULL,
    `discount_amount` DECIMAL(10, 2) DEFAULT 0.0,
    `final_amount` DECIMAL(10, 2) NOT NULL,
    `payment_method` VARCHAR(50),
    `notes` TEXT,
    FOREIGN KEY (`table_id`) REFERENCES `tables`(`table_id`),
    FOREIGN KEY (`customer_id`) REFERENCES `customers`(`customer_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`)
);

-- 9. Create `order_details` table
CREATE TABLE `order_details` (
    `order_detail_id` INT AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT NOT NULL,
    `product_id` INT NOT NULL,
    `quantity` INT NOT NULL,
    `price_at_order` DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`order_id`),
    FOREIGN KEY (`product_id`) REFERENCES `products`(`product_id`),
    UNIQUE (`order_id`, `product_id`)
);

-- 10. Create `loyalty_transactions` table
CREATE TABLE `loyalty_transactions` (
    `transaction_id` INT AUTO_INCREMENT PRIMARY KEY,
    `customer_id` INT NOT NULL,
    `order_id` INT,
    `points_change` INT NOT NULL,
    `transaction_type` ENUM("EARN", "REDEEM") NOT NULL,
    `transaction_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `description` VARCHAR(255),
    FOREIGN KEY (`customer_id`) REFERENCES `customers`(`customer_id`),
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`order_id`)
);

-- 11. Create `shifts` table
CREATE TABLE `shifts` (
    `shift_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME,
    `notes` TEXT,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`)
);

-- 12. Create `activity_logs` table
CREATE TABLE `activity_logs` (
    `log_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT,
    `action` VARCHAR(255) NOT NULL,
    `entity_type` VARCHAR(50),
    `entity_id` INT,
    `timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `details` TEXT,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`)
);


-- Insert sample users
INSERT INTO `users` (`username`, `password_hash`, `full_name`, `role`, `email`, `phone_number`)
VALUES
    ("admin", "$2a$10$EXAMPLEHASHFORADMIN", "Nguyễn Văn An", "ADMIN", "admin@coffeeshop.com", "0901112233"),
    ("staff01", "$2a$10$EXAMPLEHASHFORSTAFF01", "Trần Thị Bình", "STAFF", "binh.tran@coffeeshop.com", "0902223344"),
    ("staff02", "$2a$10$EXAMPLEHASHFORSTAFF02", "Lê Văn Cường", "STAFF", "cuong.le@coffeeshop.com", "0903334455"),
    ("staff03", "$2a$10$EXAMPLEHASHFORSTAFF03", "Phạm Thị Dung", "STAFF", "dung.pham@coffeeshop.com", "0904445566");

-- Insert sample categories (Product)
INSERT INTO `categories` (`category_name`, `type`)
VALUES
    ("Coffee", "PRODUCT"),
    ("Tea", "PRODUCT"),
    ("Smoothie", "PRODUCT"),
    ("Juice", "PRODUCT"),
    ("Pastry", "PRODUCT"),
    ("Snack", "PRODUCT");

-- Insert sample categories (Ingredient)
INSERT INTO `categories` (`category_name`, `type`)
VALUES
    ("Coffee Beans", "INGREDIENT"),
    ("Milk & Cream", "INGREDIENT"),
    ("Sweeteners", "INGREDIENT"),
    ("Fruits", "INGREDIENT"),
    ("Syrups", "INGREDIENT"),
    ("Toppings", "INGREDIENT"),
    ("Other Ingredients", "INGREDIENT");

-- Insert sample products
INSERT INTO `products` (`product_name`, `description`, `price`, `category_id`, `image_url`)
VALUES
    ("Espresso", "Cà phê đậm đặc", 35000.00, (SELECT category_id FROM `categories` WHERE category_name = "Coffee" AND type = "PRODUCT"), "/images/espresso.jpg"),
    ("Americano", "Cà phê đen pha loãng", 40000.00, (SELECT category_id FROM `categories` WHERE category_name = "Coffee" AND type = "PRODUCT"), "/images/americano.jpg"),
    ("Latte", "Cà phê sữa béo ngậy", 50000.00, (SELECT category_id FROM `categories` WHERE category_name = "Coffee" AND type = "PRODUCT"), "/images/latte.jpg"),
    ("Cappuccino", "Cà phê sữa với lớp bọt sữa dày", 50000.00, (SELECT category_id FROM `categories` WHERE category_name = "Coffee" AND type = "PRODUCT"), "/images/cappuccino.jpg"),
    ("Matcha Latte", "Trà xanh matcha với sữa", 55000.00, (SELECT category_id FROM `categories` WHERE category_name = "Tea" AND type = "PRODUCT"), "/images/matcha_latte.jpg"),
    ("Peach Tea", "Trà đào thơm mát", 45000.00, (SELECT category_id FROM `categories` WHERE category_name = "Tea" AND type = "PRODUCT"), "/images/peach_tea.jpg"),
    ("Strawberry Smoothie", "Sinh tố dâu tây tươi", 60000.00, (SELECT category_id FROM `categories` WHERE category_name = "Smoothie" AND type = "PRODUCT"), "/images/strawberry_smoothie.jpg"),
    ("Orange Juice", "Nước cam vắt nguyên chất", 50000.00, (SELECT category_id FROM `categories` WHERE category_name = "Juice" AND type = "PRODUCT"), "/images/orange_juice.jpg"),
    ("Croissant", "Bánh sừng bò giòn rụm", 25000.00, (SELECT category_id FROM `categories` WHERE category_name = "Pastry" AND type = "PRODUCT"), "/images/croissant.jpg"),
    ("Tiramisu", "Bánh Tiramisu Ý", 45000.00, (SELECT category_id FROM `categories` WHERE category_name = "Pastry" AND type = "PRODUCT"), "/images/tiramisu.jpg"),
    ("Chocolate Chip Cookie", "Bánh quy chocolate chip", 20000.00, (SELECT category_id FROM `categories` WHERE category_name = "Snack" AND type = "PRODUCT"), "/images/cookie.jpg");

-- Insert sample ingredients
INSERT INTO `ingredients` (`ingredient_name`, `unit`, `stock_quantity`, `min_stock_level`, `category_id`)
VALUES
    ("Arabica Beans", "gram", 5000.00, 1000.00, (SELECT category_id FROM `categories` WHERE category_name = "Coffee Beans" AND type = "INGREDIENT")),
    ("Robusta Beans", "gram", 3000.00, 500.00, (SELECT category_id FROM `categories` WHERE category_name = "Coffee Beans" AND type = "INGREDIENT")),
    ("Fresh Milk", "ml", 10000.00, 2000.00, (SELECT category_id FROM `categories` WHERE category_name = "Milk & Cream" AND type = "INGREDIENT")),
    ("Condensed Milk", "ml", 2000.00, 500.00, (SELECT category_id FROM `categories` WHERE category_name = "Milk & Cream" AND type = "INGREDIENT")),
    ("White Sugar", "gram", 5000.00, 1000.00, (SELECT category_id FROM `categories` WHERE category_name = "Sweeteners" AND type = "INGREDIENT")),
    ("Brown Sugar", "gram", 1500.00, 300.00, (SELECT category_id FROM `categories` WHERE category_name = "Sweeteners" AND type = "INGREDIENT")),
    ("Strawberry", "gram", 1000.00, 200.00, (SELECT category_id FROM `categories` WHERE category_name = "Fruits" AND type = "INGREDIENT")),
    ("Orange", "gram", 1500.00, 300.00, (SELECT category_id FROM `categories` WHERE category_name = "Fruits" AND type = "INGREDIENT")),
    ("Peach Syrup", "ml", 1000.00, 200.00, (SELECT category_id FROM `categories` WHERE category_name = "Syrups" AND type = "INGREDIENT")),
    ("Matcha Powder", "gram", 500.00, 100.00, (SELECT category_id FROM `categories` WHERE category_name = "Other Ingredients" AND type = "INGREDIENT")),
    ("Whipped Cream", "gram", 500.00, 100.00, (SELECT category_id FROM `categories` WHERE category_name = "Toppings" AND type = "INGREDIENT"));

-- Insert sample product_ingredients (recipes)
INSERT INTO `product_ingredients` (`product_id`, `ingredient_id`, `quantity_needed`)
VALUES
    ((SELECT product_id FROM `products` WHERE product_name = "Espresso"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Arabica Beans"), 10.00),
    ((SELECT product_id FROM `products` WHERE product_name = "Americano"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Arabica Beans"), 10.00),
    ((SELECT product_id FROM `products` WHERE product_name = "Latte"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Arabica Beans"), 15.00),
    ((SELECT product_id FROM `products` WHERE product_name = "Latte"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Fresh Milk"), 150.00),
    ((SELECT product_id FROM `products` WHERE product_name = "Cappuccino"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Arabica Beans"), 15.00),
    ((SELECT product_id FROM `products` WHERE product_name = "Cappuccino"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Fresh Milk"), 120.00),
    ((SELECT product_id FROM `products` WHERE product_name = "Matcha Latte"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Matcha Powder"), 10.00),
    ((SELECT product_id FROM `products` WHERE product_name = "Matcha Latte"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Fresh Milk"), 180.00),
    ((SELECT product_id FROM `products` WHERE product_name = "Peach Tea"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Peach Syrup"), 30.00),
    ((SELECT product_id FROM `products` WHERE product_name = "Strawberry Smoothie"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Strawberry"), 100.00),
    ((SELECT product_id FROM `products` WHERE product_name = "Strawberry Smoothie"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Fresh Milk"), 50.00),
    ((SELECT product_id FROM `products` WHERE product_name = "Orange Juice"), (SELECT ingredient_id FROM `ingredients` WHERE ingredient_name = "Orange"), 200.00);

-- Insert sample tables
INSERT INTO `tables` (`table_number`, `capacity`, `status`)
VALUES
    ("T1", 4, "AVAILABLE"),
    ("T2", 2, "OCCUPIED"),
    ("T3", 4, "AVAILABLE"),
    ("T4", 6, "AVAILABLE"),
    ("Bar1", 3, "AVAILABLE"),
    ("Bar2", 2, "CLEANING");

-- Insert sample customers
INSERT INTO `customers` (`full_name`, `phone_number`, `email`, `loyalty_points`)
VALUES
    ("Lê Thị Mai", "0912345678", "mai.le@example.com", 250),
    ("Nguyễn Văn Nam", "0987654321", "nam.nguyen@example.com", 120),
    ("Trần Minh Đức", "0901231234", "duc.tran@example.com", 50),
    ("Phạm Thu Hà", "0978123456", "ha.pham@example.com", 300),
    ("Hoàng Anh Tuấn", "0965432109", "tuan.hoang@example.com", 80),
    ("Đặng Thị Lan", "0943210987", "lan.dang@example.com", 180);

-- Insert sample orders and order_details using variables for order_id
-- Order 1
INSERT INTO `orders` (`order_date`, `total_amount`, `status`, `order_type`, `table_id`, `customer_id`, `user_id`, `discount_amount`, `final_amount`, `payment_method`)
VALUES (
    NOW() - INTERVAL 2 DAY, 100000.00, "PAID", "DINE_IN",
    (SELECT table_id FROM `tables` WHERE table_number = "T2"),
    (SELECT customer_id FROM `customers` WHERE phone_number = "0912345678"),
    (SELECT user_id FROM `users` WHERE username = "staff01"),
    0.00, 100000.00, "Cash"
);
SET @order1 := LAST_INSERT_ID();
INSERT INTO `order_details` (`order_id`, `product_id`, `quantity`, `price_at_order`)
VALUES
    (@order1, (SELECT product_id FROM `products` WHERE product_name = "Latte"), 2, 50000.00);

-- Order 2
INSERT INTO `orders` (`order_date`, `total_amount`, `status`, `order_type`, `user_id`, `discount_amount`, `final_amount`, `payment_method`)
VALUES (
    NOW() - INTERVAL 1 DAY, 75000.00, "PAID", "TAKE_AWAY",
    (SELECT user_id FROM `users` WHERE username = "staff02"),
    0.00, 75000.00, "Card"
);
SET @order2 := LAST_INSERT_ID();
INSERT INTO `order_details` (`order_id`, `product_id`, `quantity`, `price_at_order`)
VALUES
    (@order2, (SELECT product_id FROM `products` WHERE product_name = "Espresso"), 1, 35000.00),
    (@order2, (SELECT product_id FROM `products` WHERE product_name = "Croissant"), 1, 25000.00);

-- Order 3
INSERT INTO `orders` (`order_date`, `total_amount`, `status`, `order_type`, `customer_id`, `user_id`, `discount_amount`, `final_amount`, `payment_method`)
VALUES (
    NOW() - INTERVAL 1 DAY + INTERVAL 2 HOUR, 110000.00, "COMPLETED", "ONLINE",
    (SELECT customer_id FROM `customers` WHERE phone_number = "0987654321"),
    (SELECT user_id FROM `users` WHERE username = "staff03"),
    10000.00, 100000.00, "E-wallet"
);
SET @order3 := LAST_INSERT_ID();
INSERT INTO `order_details` (`order_id`, `product_id`, `quantity`, `price_at_order`)
VALUES
    (@order3, (SELECT product_id FROM `products` WHERE product_name = "Matcha Latte"), 1, 55000.00),
    (@order3, (SELECT product_id FROM `products` WHERE product_name = "Tiramisu"), 1, 45000.00);

-- Order 4
INSERT INTO `orders` (`order_date`, `total_amount`, `status`, `order_type`, `table_id`, `user_id`, `discount_amount`, `final_amount`)
VALUES (
    NOW(), 95000.00, "PENDING", "DINE_IN",
    (SELECT table_id FROM `tables` WHERE table_number = "T1"),
    (SELECT user_id FROM `users` WHERE username = "staff01"),
    0.00, 95000.00
);
SET @order4 := LAST_INSERT_ID();
INSERT INTO `order_details` (`order_id`, `product_id`, `quantity`, `price_at_order`)
VALUES
    (@order4, (SELECT product_id FROM `products` WHERE product_name = "Americano"), 1, 40000.00),
    (@order4, (SELECT product_id FROM `products` WHERE product_name = "Chocolate Chip Cookie"), 2, 20000.00);

-- Order 5
INSERT INTO `orders` (`order_date`, `total_amount`, `status`, `order_type`, `user_id`, `discount_amount`, `final_amount`)
VALUES (
    NOW() - INTERVAL 30 MINUTE, 120000.00, "PREPARING", "DELIVERY",
    (SELECT user_id FROM `users` WHERE username = "staff02"),
    0.00, 120000.00
);
SET @order5 := LAST_INSERT_ID();
INSERT INTO `order_details` (`order_id`, `product_id`, `quantity`, `price_at_order`)
VALUES
    (@order5, (SELECT product_id FROM `products` WHERE product_name = "Strawberry Smoothie"), 2, 60000.00);

-- Insert sample loyalty_transactions
INSERT INTO `loyalty_transactions` (`customer_id`, `order_id`, `points_change`, `transaction_type`, `description`)
VALUES
    ((SELECT customer_id FROM `customers` WHERE phone_number = "0912345678"), @order1, 100, "EARN", "Tích điểm từ đơn hàng #1"),
    ((SELECT customer_id FROM `customers` WHERE phone_number = "0987654321"), @order3, 100, "EARN", "Tích điểm từ đơn hàng #3"),
    ((SELECT customer_id FROM `customers` WHERE phone_number = "0987654321"), NULL, -50, "REDEEM", "Đổi điểm giảm giá");

-- Insert sample shifts
INSERT INTO `shifts` (`user_id`, `start_time`, `end_time`, `notes`)
VALUES
    ((SELECT user_id FROM `users` WHERE username = "staff01"), NOW() - INTERVAL 3 DAY - INTERVAL 8 HOUR, NOW() - INTERVAL 3 DAY, "Ca sáng"),
    ((SELECT user_id FROM `users` WHERE username = "staff02"), NOW() - INTERVAL 2 DAY - INTERVAL 8 HOUR, NOW() - INTERVAL 2 DAY, "Ca sáng"),
    ((SELECT user_id FROM `users` WHERE username = "staff01"), NOW() - INTERVAL 1 DAY - INTERVAL 8 HOUR, NOW() - INTERVAL 1 DAY, "Ca sáng"),
    ((SELECT user_id FROM `users` WHERE username = "staff03"), NOW() - INTERVAL 1 DAY - INTERVAL 8 HOUR, NOW() - INTERVAL 1 DAY, "Ca sáng"),
    ((SELECT user_id FROM `users` WHERE username = "staff02"), NOW() - INTERVAL 8 HOUR, NULL, "Ca sáng hôm nay");

-- Insert sample activity_logs
INSERT INTO `activity_logs` (`user_id`, `action`, `entity_type`, `entity_id`, `timestamp`, `details`)
VALUES
    ((SELECT user_id FROM `users` WHERE username = "admin"), "LOGIN", "USER", (SELECT user_id FROM `users` WHERE username = "admin"), NOW() - INTERVAL 5 DAY, "Admin logged in"),
    ((SELECT user_id FROM `users` WHERE username = "staff01"), "CREATED_ORDER", "ORDER", @order1, NOW() - INTERVAL 2 DAY, "Order #1 created"),
    ((SELECT user_id FROM `users` WHERE username = "admin"), "UPDATED_PRODUCT_PRICE", "PRODUCT", (SELECT product_id FROM `products` WHERE product_name = "Espresso"), NOW() - INTERVAL 3 DAY, "Espresso price changed from 30000 to 35000"),
    ((SELECT user_id FROM `users` WHERE username = "staff03"), "COMPLETED_ORDER", "ORDER", @order3, NOW() - INTERVAL 1 DAY + INTERVAL 3 HOUR, "Order #3 completed");