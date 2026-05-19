-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: wms
-- ------------------------------------------------------
-- Server version	9.5.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '88cf49b1-ae6d-11f0-97cd-d493901cbdbe:1-1228';

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `action` varchar(255) DEFAULT NULL,
  `table_name` varchar(255) DEFAULT NULL,
  `record_id` int DEFAULT NULL,
  `time_stamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `audit_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;

--
-- Table structure for table `batch`
--

DROP TABLE IF EXISTS `batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `batch` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `batch_code` varchar(255) DEFAULT NULL,
  `manufacturing_date` date DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity_on_hand` int DEFAULT NULL,
  `quantity_available` int DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `ware_house_id` int DEFAULT NULL,
  `location_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `batch_code` (`batch_code`),
  KEY `product_id` (`product_id`),
  KEY `batch_ibfk_2` (`ware_house_id`),
  KEY `fk_batch_location` (`location_id`),
  CONSTRAINT `batch_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
  CONSTRAINT `batch_ibfk_2` FOREIGN KEY (`ware_house_id`) REFERENCES `ware_house` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_batch_location` FOREIGN KEY (`location_id`) REFERENCES `warehouse_location` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch`
--

/*!40000 ALTER TABLE `batch` DISABLE KEYS */;
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (1,'Lô MB Pro T5/2026','BAT-MBP-0526-01','2026-04-15',NULL,1,15,15,'Hàng nhập khẩu chính ngạch',1,1);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (2,'Lô Dell XPS T5/2026','BAT-XPS-0526-01','2026-04-20',NULL,2,10,10,'Nguyên seal',1,2);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (3,'Lô CPU Core i9','BAT-I9-0526-01','2026-03-10',NULL,3,30,30,'',1,3);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (4,'Lô VGA RTX 4090','BAT-4090-0526-01','2026-03-25',NULL,4,5,5,'Hàng giá trị cao',1,4);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (5,'Lô iPhone 15 PM','BAT-IP15-0526-01','2026-05-01',NULL,5,50,50,'',1,5);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (6,'Lô Galaxy S24U','BAT-S24U-0526-01','2026-05-05',NULL,6,45,45,'',2,6);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (7,'Lô iPad M4','BAT-IPAD-0526-01','2026-05-10',NULL,7,20,20,'',2,7);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (8,'Lô Chuột MX 3S','BAT-MX3S-0526-01','2026-02-15',NULL,8,100,100,'',1,1);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (9,'Lô Phím Keychron','BAT-Q1P-0526-01','2026-03-01',NULL,9,8,8,'Hàng tồn sắp hết',1,2);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (10,'Lô Màn LG','BAT-LG27-0526-01','2026-01-20',NULL,10,12,12,'',1,3);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (11,'Lô Tai nghe XM5','BAT-XM5-0526-01','2026-04-05',NULL,11,40,40,'',1,4);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (12,'Lô AirPods Pro 2','BAT-AIRP-0526-01','2026-04-25',NULL,12,80,80,'',1,5);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (13,'Lô PS5 Standard','BAT-PS5-0526-01','2026-01-10',NULL,13,0,0,'Đã xuất hết',2,6);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (14,'Lô Sony A7 IV','BAT-A74-0526-01','2026-03-22',NULL,14,8,8,'Kiểm tra kỹ chống ẩm',2,7);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (15,'Lô Router Deco X20','BAT-X20-0526-01','2026-02-28',NULL,15,60,60,'',1,1);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (16,NULL,'LAB-0132','2026-05-14','2027-05-30',16,100,100,NULL,1,2);
INSERT INTO `batch` (`id`, `name`, `batch_code`, `manufacturing_date`, `expiry_date`, `product_id`, `quantity_on_hand`, `quantity_available`, `note`, `ware_house_id`, `location_id`) VALUES (18,'Lô Xiaomi 13T','X13T-1',NULL,NULL,18,99,99,'Lô 13T đầu tiên',2,6);
/*!40000 ALTER TABLE `batch` ENABLE KEYS */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (1,'Computers & Laptops',NULL);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (2,'Phones & Tablets',NULL);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (3,'Accessories & Monitors',NULL);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (4,'Audio & Gaming',NULL);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (5,'Cameras & Network',NULL);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (6,'Laptops',1);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (7,'PC Components',1);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (8,'Smartphones',2);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (9,'Tablets',2);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (10,'Mice & Keyboards',3);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (11,'Monitors',3);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (12,'Audio Devices',4);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (13,'Gaming Consoles',4);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (14,'Cameras',5);
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES (15,'Network Devices',5);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `customer_receivable` decimal(10,2) DEFAULT NULL,
  `type` enum('BUSINESS','INDIVIDUAL') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;

--
-- Table structure for table `delivery_order`
--

DROP TABLE IF EXISTS `delivery_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_order` (
  `id` int NOT NULL,
  `pickup_time` timestamp NULL DEFAULT NULL,
  `completed_at` timestamp NULL DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `cancelled_at` timestamp NULL DEFAULT NULL,
  `cod` decimal(10,2) DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `customer_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employee_id` (`employee_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `delivery_order_ibfk_1` FOREIGN KEY (`id`) REFERENCES `order_header` (`id`) ON DELETE CASCADE,
  CONSTRAINT `delivery_order_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE SET NULL,
  CONSTRAINT `delivery_order_ibfk_3` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_order`
--

/*!40000 ALTER TABLE `delivery_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `delivery_order` ENABLE KEYS */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `ware_house_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `ware_house_id` (`ware_house_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`ware_house_id`) REFERENCES `ware_house` (`id`) ON DELETE SET NULL,
  CONSTRAINT `employee_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `order_header_id` int DEFAULT NULL,
  `ware_house_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `batch_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_header_id` (`order_header_id`),
  KEY `ware_house_id` (`ware_house_id`),
  KEY `product_id` (`product_id`),
  KEY `batch_id` (`batch_id`),
  CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_header_id`) REFERENCES `order_header` (`id`) ON DELETE CASCADE,
  CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`ware_house_id`) REFERENCES `ware_house` (`id`) ON DELETE SET NULL,
  CONSTRAINT `order_details_ibfk_3` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `order_details_ibfk_4` FOREIGN KEY (`batch_id`) REFERENCES `batch` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;

--
-- Table structure for table `order_header`
--

DROP TABLE IF EXISTS `order_header`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_header` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `delivery_status` enum('PENDING','IN_TRANSIT','DELIVERED','FAILED') DEFAULT 'PENDING',
  `payment_status` enum('UNPAID','PAID') DEFAULT 'UNPAID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `confirmed` tinyint(1) DEFAULT '0',
  `expected_arrival` date DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `order_header_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_header`
--

/*!40000 ALTER TABLE `order_header` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_header` ENABLE KEYS */;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `barcode` varchar(255) DEFAULT NULL,
  `sku_code` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `base_price` decimal(38,2) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','OUT_OF_STOCK','LOW_STOCK') NOT NULL,
  `category_id` int DEFAULT NULL,
  `unit` varchar(50) NOT NULL DEFAULT 'Cái' COMMENT 'Đơn vị tính (Cái, Hộp, Bộ...)',
  `brand` varchar(100) DEFAULT NULL COMMENT 'Thương hiệu sản phẩm',
  `warranty_months` int DEFAULT '0' COMMENT 'Thời gian bảo hành tính bằng tháng',
  `min_stock_level` int DEFAULT '0' COMMENT 'Định mức tồn kho tối thiểu để cảnh báo',
  `tax_rate` decimal(5,2) DEFAULT '10.00' COMMENT 'Thuế suất VAT (%)',
  `technical_specs` varchar(1000) DEFAULT NULL COMMENT 'Thông số kỹ thuật chi tiết của sản phẩm',
  PRIMARY KEY (`id`),
  UNIQUE KEY `barcode` (`barcode`),
  UNIQUE KEY `sku_code` (`sku_code`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (1,'8935001110001','LAP-MAC-MBP14M3','MacBook Pro 14 inch M3 2023',39990000.00,'','Laptop Apple MacBook Pro 14 inch chip M3, 8GB RAM, 512GB SSD','ACTIVE',6,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (2,'8935001110002','LAP-DEL-XPS15','Dell XPS 15 9530',45500000.00,'','Laptop Dell XPS 15 OLED Core i7 13700H','ACTIVE',6,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (3,'8935001110003','PCC-INT-I914900K','CPU Intel Core i9 14900K',14500000.00,'','Bộ vi xử lý Intel Core i9 thế hệ 14','ACTIVE',7,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (4,'8935001110004','PCC-ASU-RTX4090','VGA Asus ROG Strix RTX 4090',55000000.00,'','Card màn hình Asus ROG Strix GeForce RTX 4090 24GB','ACTIVE',7,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (5,'8935001110005','PHO-APP-IP15PM','iPhone 15 Pro Max 256GB',29500000.00,'','Điện thoại di động Apple iPhone 15 Pro Max Titan tự nhiên','ACTIVE',8,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (6,'8935001110006','PHO-SAM-S24U','Samsung Galaxy S24 Ultra',27000000.00,'','Điện thoại di động Samsung Galaxy S24 Ultra 5G AI','ACTIVE',8,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (7,'8935001110007','TAB-APP-IPADM4','iPad Pro 13 inch M4',35000000.00,'','Máy tính bảng Apple iPad Pro 13 M4 2024','ACTIVE',9,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (8,'2017885980547','ACC-LOG-MX3S','Chuột Logitech MX Master 3S',2500000.00,'/asset/uploads/yGC4bDa4f2cIaMpw2ygRzWqKUdKWCbvj0XC7MJ30.jpg','Chuột không dây Logitech MX Master 3S','ACTIVE',10,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (9,'8935001110009','ACC-KEY-Q1PRO','Bàn phím cơ Keychron Q1 Pro',4200000.00,'','Bàn phím cơ không dây Keychron Q1 Pro Custom','LOW_STOCK',10,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (10,'8935001110010','MON-LG-27GN950','Màn hình LG UltraGear 27 4K',16000000.00,'','Màn hình LG 27GN950-B 27 inch 4K Nano IPS 144Hz','ACTIVE',11,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (11,'8935001110011','AUD-SON-XM5','Tai nghe Sony WH-1000XM5',7500000.00,'','Tai nghe chống ồn không dây Sony WH-1000XM5','ACTIVE',12,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (12,'8935001110012','AUD-APP-AIR2','Tai nghe AirPods Pro 2',5500000.00,'','Tai nghe Apple AirPods Pro Gen 2 Type-C','ACTIVE',12,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (13,'8935001110013','GAM-SON-PS5','Máy chơi game PlayStation 5',13500000.00,'','Máy chơi game Sony PlayStation 5 Standard Edition','OUT_OF_STOCK',13,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (14,'2023567402221','CAM-SON-A7M4','Máy ảnh Sony Alpha A7 IV',55000000.00,'','Máy ảnh Mirrorless Sony Alpha A7 IV Body','ACTIVE',14,'Cái','',0,10,10.00,'');
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (15,'8935001110015','NET-TPL-X20','Router Mesh TP-Link Deco X20',2200000.00,'','Hệ thống Wi-Fi 6 Mesh TP-Link Deco X20 AX1800','ACTIVE',15,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (16,'2096980492711','PHO-APP-IP15P','Iphone 15 Pro',30000000.00,NULL,'Màn hình chống lóa ','ACTIVE',8,'Cái',NULL,0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (18,'2033029743397','PHO-XIA-13TP','Xiaomi 13T Pro',20000000.00,NULL,'Pin 5000MA','INACTIVE',8,'Cái','Xiaomi',0,0,10.00,NULL);
INSERT INTO `product` (`id`, `barcode`, `sku_code`, `name`, `base_price`, `img_url`, `description`, `status`, `category_id`, `unit`, `brand`, `warranty_months`, `min_stock_level`, `tax_rate`, `technical_specs`) VALUES (19,'2042989304241','AUD-APP-AP3','Airpod Pro 3',5650000.00,'/asset/uploads/Screenshot 2026-05-14 180132.png','Tai nghe cao cấp, chip H2/U2, chống ồn chủ chộng, pin 30 giờ, chuẩn kháng nước IP57 cùng 5 kích cỡ đệm tai bọt biển êm ái.','ACTIVE',12,'Cái','Apple',12,10,10.00,'Chip xử lý: Apple H2 (tai nghe), chip U2 (hộp sạc).\r\nChống ồn & Âm thanh: Khử tiếng ồn chủ động (ANC) cao cấp, Xuyên âm (Transparency), Âm thanh không gian (Spatial Audio).\r\nThời lượng pin: Lên đến 8 giờ nghe liên tục (tai nghe), kèm hộp sạc dùng lên tới 30+ giờ.\r\nKết nối: Bluetooth 5.3.Cổng sạc: USB-C và MagSafe.Kháng nước/bụi: Chuẩn IP57 (cả tai nghe và hộp sạc).\r\nĐệm tai (Eartips): Bọt biển Foam-infused với 5 kích cỡ (bao gồm cả XS và XXS).');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

--
-- Table structure for table `purchase_order`
--

DROP TABLE IF EXISTS `purchase_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order` (
  `id` int NOT NULL,
  `actual_arrival` date DEFAULT NULL,
  `total_purchase_amount` decimal(10,2) DEFAULT NULL,
  `delivery_result` enum('EARLY','LATE') DEFAULT NULL,
  `supplier_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `supplier_id` (`supplier_id`),
  CONSTRAINT `purchase_order_ibfk_1` FOREIGN KEY (`id`) REFERENCES `order_header` (`id`) ON DELETE CASCADE,
  CONSTRAINT `purchase_order_ibfk_2` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order`
--

/*!40000 ALTER TABLE `purchase_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_order` ENABLE KEYS */;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `tax_code` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `supplier_status` enum('ACTIVE','INACTIVE','SUSPENDED','TERMINATED') NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `supplier_payable` decimal(19,2) DEFAULT NULL,
  `reliability_score` int DEFAULT NULL,
  `failed_delivery_count` int DEFAULT NULL,
  `late_delivery_count` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tax_code` (`tax_code`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` (`id`, `name`, `tax_code`, `email`, `supplier_status`, `phone`, `address`, `supplier_payable`, `reliability_score`, `failed_delivery_count`, `late_delivery_count`) VALUES (1,'Công ty TNHH Phân phối FPT (Synnex FPT)','0102773539','contact@synnexfpt.com','ACTIVE','02473006666','Tòa nhà FPT, Phạm Văn Bạch, Cầu Giấy, Hà Nội',1250000000.00,98,0,1);
INSERT INTO `supplier` (`id`, `name`, `tax_code`, `email`, `supplier_status`, `phone`, `address`, `supplier_payable`, `reliability_score`, `failed_delivery_count`, `late_delivery_count`) VALUES (2,'Công ty Cổ phần Thế Giới Số (Digiworld)','0303102324','info@digiworld.com.vn','ACTIVE','02839290059','Tòa nhà Etown, Cộng Hòa, Tân Bình, TP. HCM',850000000.00,95,0,2);
INSERT INTO `supplier` (`id`, `name`, `tax_code`, `email`, `supplier_status`, `phone`, `address`, `supplier_payable`, `reliability_score`, `failed_delivery_count`, `late_delivery_count`) VALUES (3,'Công ty TNHH Apple Việt Nam','0107122144','sales.vn@apple.com','ACTIVE','02838233333','Phòng 201, Tòa nhà Mê Linh Point, Quận 1, TP. HCM',2500000000.00,100,0,0);
INSERT INTO `supplier` (`id`, `name`, `tax_code`, `email`, `supplier_status`, `phone`, `address`, `supplier_payable`, `reliability_score`, `failed_delivery_count`, `late_delivery_count`) VALUES (4,'Công ty TNHH Điện tử Samsung Vina','0300133261','partner.vn@samsung.com','ACTIVE','02838211111','Tòa nhà Bitexco, Bến Nghé, Quận 1, TP. HCM',1800000000.00,96,1,3);
INSERT INTO `supplier` (`id`, `name`, `tax_code`, `email`, `supplier_status`, `phone`, `address`, `supplier_payable`, `reliability_score`, `failed_delivery_count`, `late_delivery_count`) VALUES (5,'Công ty Cổ phần Máy tính Hà Nội (HACOM)','0101161194','cskh@hacom.vn','INACTIVE','19001903','131 Lê Thanh Nghị, Hai Bà Trưng, Hà Nội',0.00,85,0,5);
INSERT INTO `supplier` (`id`, `name`, `tax_code`, `email`, `supplier_status`, `phone`, `address`, `supplier_payable`, `reliability_score`, `failed_delivery_count`, `late_delivery_count`) VALUES (6,'Nhà phân phối Linh Kiện Z','0315487920','linhkienz@gmail.com','SUSPENDED','0988776655','Bình Tân, TP. HCM',15000000.00,45,5,12);
INSERT INTO `supplier` (`id`, `name`, `tax_code`, `email`, `supplier_status`, `phone`, `address`, `supplier_payable`, `reliability_score`, `failed_delivery_count`, `late_delivery_count`) VALUES (7,'Công ty TNHH MTV Phân phối ABC','0108994521','abc.distrib@yahoo.com','TERMINATED','0912345678','Thanh Xuân, Hà Nội',0.00,20,10,25);
INSERT INTO `supplier` (`id`, `name`, `tax_code`, `email`, `supplier_status`, `phone`, `address`, `supplier_payable`, `reliability_score`, `failed_delivery_count`, `late_delivery_count`) VALUES (8,'Công ty TNHH ASUS Technology VN','0305412498','dealer_vn@asus.com','ACTIVE','18006588','285 Cách Mạng Tháng 8, Quận 10, TP. HCM',450000000.00,92,0,4);
INSERT INTO `supplier` (`id`, `name`, `tax_code`, `email`, `supplier_status`, `phone`, `address`, `supplier_payable`, `reliability_score`, `failed_delivery_count`, `late_delivery_count`) VALUES (9,'Đại lý thiết bị mạng Phong Vũ','0301486000','doitac@phongvu.vn','ACTIVE','18006865','264A Nguyễn Thị Minh Khai, Quận 3, TP. HCM',210000000.00,88,1,6);
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

--
-- Table structure for table `ware_house`
--

DROP TABLE IF EXISTS `ware_house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ware_house` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `manager_name` varchar(255) DEFAULT NULL,
  `contact_phone` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','MAINTENANCE') DEFAULT 'ACTIVE',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_warehouse_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ware_house`
--

/*!40000 ALTER TABLE `ware_house` DISABLE KEYS */;
INSERT INTO `ware_house` (`id`, `code`, `name`, `location`, `manager_name`, `contact_phone`, `status`) VALUES (1,'WH-HN-01','Kho Tổng Hà Nội','Quận Long Biên, Hà Nội','Nguyễn Văn A','0912345678','ACTIVE');
INSERT INTO `ware_house` (`id`, `code`, `name`, `location`, `manager_name`, `contact_phone`, `status`) VALUES (2,'WH-HCM-02','Kho Miền Nam','Quận 7, TP. HCM','Trần Thị B','0987654321','ACTIVE');
/*!40000 ALTER TABLE `ware_house` ENABLE KEYS */;

--
-- Table structure for table `warehouse_location`
--

DROP TABLE IF EXISTS `warehouse_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse_location` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ware_house_id` int NOT NULL,
  `shelf_name` varchar(255) NOT NULL,
  `tier_name` varchar(255) NOT NULL,
  `bin_name` varchar(255) NOT NULL,
  `location_code` varchar(255) NOT NULL,
  `status` enum('EMPTY','OCCUPIED','MAINTENANCE') DEFAULT 'EMPTY',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_location_code` (`location_code`),
  KEY `fk_location_warehouse` (`ware_house_id`),
  CONSTRAINT `fk_location_warehouse` FOREIGN KEY (`ware_house_id`) REFERENCES `ware_house` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse_location`
--

/*!40000 ALTER TABLE `warehouse_location` DISABLE KEYS */;
INSERT INTO `warehouse_location` (`id`, `ware_house_id`, `shelf_name`, `tier_name`, `bin_name`, `location_code`, `status`, `created_at`) VALUES (1,1,'A1','01','01','HN1-A1-T1-01','OCCUPIED','2026-05-12 00:17:52');
INSERT INTO `warehouse_location` (`id`, `ware_house_id`, `shelf_name`, `tier_name`, `bin_name`, `location_code`, `status`, `created_at`) VALUES (2,1,'A1','01','02','HN1-A1-T1-02','OCCUPIED','2026-05-12 00:17:52');
INSERT INTO `warehouse_location` (`id`, `ware_house_id`, `shelf_name`, `tier_name`, `bin_name`, `location_code`, `status`, `created_at`) VALUES (3,1,'A1','02','01','HN1-A1-T2-01','OCCUPIED','2026-05-12 00:17:52');
INSERT INTO `warehouse_location` (`id`, `ware_house_id`, `shelf_name`, `tier_name`, `bin_name`, `location_code`, `status`, `created_at`) VALUES (4,1,'B1','01','01','HN1-B1-T1-01','EMPTY','2026-05-12 00:17:52');
INSERT INTO `warehouse_location` (`id`, `ware_house_id`, `shelf_name`, `tier_name`, `bin_name`, `location_code`, `status`, `created_at`) VALUES (5,1,'B1','01','02','HN1-B1-T1-02','EMPTY','2026-05-12 00:17:52');
INSERT INTO `warehouse_location` (`id`, `ware_house_id`, `shelf_name`, `tier_name`, `bin_name`, `location_code`, `status`, `created_at`) VALUES (6,2,'X1','01','01','HCM2-X1-T1-01','OCCUPIED','2026-05-12 00:17:52');
INSERT INTO `warehouse_location` (`id`, `ware_house_id`, `shelf_name`, `tier_name`, `bin_name`, `location_code`, `status`, `created_at`) VALUES (7,2,'X1','01','02','HCM2-X1-T1-02','OCCUPIED','2026-05-12 00:17:52');
/*!40000 ALTER TABLE `warehouse_location` ENABLE KEYS */;

--
-- Dumping events for database 'wms'
--
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed
