CREATE DATABASE  IF NOT EXISTS `coffee_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `coffee_db`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: coffee_db
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `order_product`
--

DROP TABLE IF EXISTS `order_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_product` (
  `order_product_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  `order_quantity` int NOT NULL,
  `price` bigint NOT NULL,
  PRIMARY KEY (`order_product_id`),
  KEY `product_id` (`product_id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `order_product_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `order_product_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_product`
--

LOCK TABLES `order_product` WRITE;
/*!40000 ALTER TABLE `order_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `total_price` bigint NOT NULL,
  `order_date` datetime NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT '결제완료',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `price` int NOT NULL,
  `quantity` int NOT NULL,
  `description` text,
  `image_url` varchar(255) DEFAULT NULL,
  `stock` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `uk_product_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'brazil-serrado',5000,100,NULL,'/img/coffee/brazil-serrado.jpg',0),(2,'colombia-narino',5000,100,NULL,'/img/coffee/colombia-narino.jpg',0),(3,'colombia-quindio',5000,100,NULL,'/img/coffee/colombia-quindio.jpg',0),(4,'ethiopia-sidamo',5000,100,NULL,'/img/coffee/ethiopia-sidamo.jpg',0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_detail`
--

DROP TABLE IF EXISTS `product_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_detail` (
  `detail_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `description` text,
  `origin` text,
  `flavor_aroma` text,
  `feature` text,
  PRIMARY KEY (`detail_id`),
  UNIQUE KEY `product_id` (`product_id`),
  CONSTRAINT `product_detail_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_detail`
--

LOCK TABLES `product_detail` WRITE;
/*!40000 ALTER TABLE `product_detail` DISABLE KEYS */;
INSERT INTO `product_detail` VALUES (1,1,NULL,'브라질 미나스 제라이스주(Minas Gerais)의 세라도(Cerrado) 지역에서 재배됩니다.','땅콩, 카라멜, 초콜릿, 견과류의 고소하고 달콤한 풍미가 특징입니다. 원목 향이 어우러져 전체적으로 밸런스가 좋습니다.','산미가 약하고 바디감이 좋습니다. 불균형 없이 균형 잡힌 맛으로 편안하게 마시기 좋습니다.'),(2,2,NULL,'콜롬비아 남서부 나리뇨(Nariño) 지역의 고산지대에서 재배됩니다. 해발 1,800m 이상의 높은 고도와 화산 토양이 특징입니다.','밝고 선명한 산미가 두드러지며, 시트러스(오렌지, 레몬)와 플로럴한 향, 달콤한 카라멜 뉘앙스가 조화를 이룹니다.','고도에서 자라 밀도가 높은 원두로, 복합적이고 화사한 맛을 내며 깔끔한 여운이 특징입니다. 스페셜티 커피에서 자주 사용됩니다.'),(3,3,NULL,'콜롬비아 중서부 안데스 산맥의 퀸디오(Quindío) 지역에서 재배됩니다. 전통적인 커피 벨트(Coffee Triangle)의 핵심 지역입니다.','부드럽고 중간 정도의 산미에, 캐러멜·초콜릿·붉은 과일류(체리, 베리)의 단맛과 과일 향이 어우러집니다.','깔끔하고 라운드한 밸런스, 중간 바디감으로 누구나 즐기기 좋은 전형적인 콜롬비아 커피의 특성을 보여줍니다.'),(4,4,NULL,'에티오피아 남부 시다모(Sidamo) 지역의 고지대(해발 1,500~2,200m)에서 재배됩니다. 에티오피아는 커피의 기원지로 알려져 있으며, 시다모는 그중에서도 대표적인 산지입니다.','밝고 복합적인 산미가 특징이며, 자몽·레몬 같은 시트러스와 베리류 과일향, 재스민·라벤더 같은 플로럴 향이 잘 어우러집니다. 가볍고 화사한 향미로 ‘티와 같은’ 느낌을 주기도 합니다.','바디감은 가볍거나 중간 정도이며, 향미가 풍부하고 청량감 있는 맛이 돋보입니다. 워시드(세척식) 가공 시에는 깔끔하고 맑은 산미, 내추럴 가공 시에는 과일향이 더욱 진하게 표현됩니다.');
/*!40000 ALTER TABLE `product_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'coffee_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-25 17:47:50
