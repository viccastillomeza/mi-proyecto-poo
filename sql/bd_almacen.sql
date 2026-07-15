CREATE DATABASE  IF NOT EXISTS `constructora_almacen` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `constructora_almacen`;
-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: constructora_almacen
-- ------------------------------------------------------
-- Server version	8.4.8

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
-- Table structure for table `items_almacen`
--

DROP TABLE IF EXISTS `items_almacen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items_almacen` (
  `id_item` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `tipo_item` varchar(30) NOT NULL,
  `unidad_medida` varchar(20) NOT NULL,
  `stock_actual` int DEFAULT '0',
  `stock_minimo` int DEFAULT '0',
  `desperdicio_acumulado` int DEFAULT '0',
  PRIMARY KEY (`id_item`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items_almacen`
--

LOCK TABLES `items_almacen` WRITE;
/*!40000 ALTER TABLE `items_almacen` DISABLE KEYS */;
INSERT INTO `items_almacen` VALUES (1,'Cemento Sol','MaterialObra','Bolsas',120,30,5),(2,'Ladrillo King Kong 18 huecos','MaterialObra','Millares',15,3,1),(3,'Arena Fina','MaterialObra','Metros Cúbicos',25,5,2),(4,'Arena Gruesa','MaterialObra','Metros Cúbicos',30,8,3),(5,'Piedra Chancada 1/2','MaterialObra','Metros Cúbicos',20,5,1),(6,'Fierro Corrugado 1/2 pulgada','MaterialObra','Varillas',200,50,0),(7,'Fierro Corrugado 3/8 pulgada','MaterialObra','Varillas',300,50,6),(8,'Yeso de Construcción','MaterialObra','Bolsas',60,15,2),(9,'Alambre Negro Nro 8','MaterialObra','Kilos',80,20,5),(10,'Alambre Negro Nro 16','MaterialObra','Kilos',100,20,4),(11,'Taladro Percutor Bosch 750W','Herramienta','Unidades',6,2,0),(12,'Amoladora Angular Makita 4.5\"','Herramienta','Unidades',8,2,0),(13,'Martillo Demoledor Dewalt','Herramienta','Unidades',3,1,0),(14,'Mezcladora de Concreto 9p3','Herramienta','Unidades',2,1,0),(15,'Carretilla Buggy','Herramienta','Unidades',12,4,0),(16,'Huincha de medir 50m','Herramienta','Unidades',5,2,0),(17,'Nivel de Mano de Aluminio 24\"','Herramienta','Unidades',10,3,0),(18,'Pala Cuchara','Herramienta','Unidades',15,5,0),(19,'Pico con Mango de Madera','Herramienta','Unidades',10,3,0),(20,'Winche Eléctrico 500Kg','Herramienta','Unidades',2,1,0),(21,'Clavos para madera 2.5 pulgadas','Consumible','Cajas',40,10,2),(22,'Clavos para madera 3 pulgadas','Consumible','Cajas',35,10,3),(23,'Disco de corte para metal 4.5\"','Consumible','Unidades',100,20,15),(24,'Disco diamantado continuo 4.5\"','Consumible','Unidades',50,10,5),(25,'Guantes de Cuero reforzado','Consumible','Pares',60,15,0),(26,'Lentes de Seguridad transparentes','Consumible','Unidades',80,20,0),(27,'Casco de Seguridad Blanco','Consumible','Unidades',30,5,0),(28,'Mascarilla contra polvo N95','Consumible','Cajas',15,4,1),(29,'Electrodo Celulósico 6011 1/8\"','Consumible','Kilos',50,10,4),(30,'Hoja de Sierra bimetálica','Consumible','Unidades',120,30,10);
/*!40000 ALTER TABLE `items_almacen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimientos`
--

DROP TABLE IF EXISTS `movimientos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movimientos` (
  `id_movimiento` int NOT NULL AUTO_INCREMENT,
  `id_item` int NOT NULL,
  `id_usuario` int NOT NULL,
  `tipo_movimiento` varchar(20) NOT NULL,
  `cantidad` int NOT NULL,
  `fecha` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_movimiento`),
  KEY `id_item` (`id_item`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `movimientos_ibfk_1` FOREIGN KEY (`id_item`) REFERENCES `items_almacen` (`id_item`) ON DELETE CASCADE,
  CONSTRAINT `movimientos_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimientos`
--

LOCK TABLES `movimientos` WRITE;
/*!40000 ALTER TABLE `movimientos` DISABLE KEYS */;
INSERT INTO `movimientos` VALUES (1,1,1,'INGRESO',50,'2026-06-25 10:43:38');
/*!40000 ALTER TABLE `movimientos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `dni` varchar(8) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `rol` varchar(30) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `dni` (`dni`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'47024828','VICTOR DANIEL','CASTILLO MEZA','vcastillo','1530050','Administrador'),(2,'22222222','JOEL ANGEL','CORTEZ TOMA','jcortez','U23224468','Almacenero'),(3,'33333333','JAIR INTI','NUÑEZ CUSIHUALLPA','jnunez','U22321082','Supervisor'),(4,'44444444','PAUL CLYDE','FOSTER ESPINOZA','pfoster','U20307998','Almacenero');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-15  1:15:10
