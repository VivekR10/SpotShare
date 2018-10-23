-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: localhost    Database: SpotShare
-- ------------------------------------------------------
-- Server version	5.7.17
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `follow`
--
use spotshare ;

DROP TABLE IF EXISTS `follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `follow` (
  `follow_username_following` varchar(50) NOT NULL,
  `follow_username_being_followed` varchar(50) NOT NULL,
  PRIMARY KEY (`follow_username_following`,`follow_username_being_followed`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follow`
--

LOCK TABLES `follow` WRITE;
/*!40000 ALTER TABLE `follow` DISABLE KEYS */;
INSERT INTO `follow` VALUES ('joannlin','brooks'),('joannlin','vivek');
/*!40000 ALTER TABLE `follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `location_id` int(11) NOT NULL AUTO_INCREMENT,
  `location_name` varchar(225) DEFAULT NULL,
  `location_latitude` decimal(9,6) NOT NULL,
  `location_longitude` decimal(9,6) NOT NULL,
  `location_first_username` varchar(50) NOT NULL,
  `location_number_pins` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`location_id`),
  KEY `location_first_username_idx` (`location_first_username`),
  CONSTRAINT `location_first_username` FOREIGN KEY (`location_first_username`) REFERENCES `user` (`user_username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (1,'Santa Monica Beach',34.012527,-118.499954,'joannlin',2),(2,'University of Southern California',34.022350,-118.285118,'joannlin',3),(3,'Empire State Building',40.748440,-73.985664,'joannlin',2),(4,'South Beach',25.782612,-80.134079,'joannlin',10),(5,'Lorenzo',34.027462,-118.272988,'vivek',1),(6,'Glenbrook South High School',42.089143,-87.851330,'joannlin',2),(7,'Rhino Records',34.096329,-117.718027,'vivek',1),(8,'The Broad',34.054471,-118.250558,'joannlin',1),(9,'Griffith Park', 34.136555,-118.294197,'brooks',1),(10,'Nickel Diner',34.045909,-118.248799,'brooks',1),(11,'GameHaus Cafe',34.123216,-118.254742,'vivek',1),(12,'The Line',34.062071,-118.301033,'joannlin',1);
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pinned_location`
--

DROP TABLE IF EXISTS `pinned_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pinned_location` (
  `pl_id` int(11) NOT NULL AUTO_INCREMENT,
  `pl_location_id` int(11) NOT NULL,
  `pl_username` varchar(50) NOT NULL,
  `pl_review` varchar(225) DEFAULT NULL,
  `pl_date_pinned` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `pl_category` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`pl_id`),
  KEY `pl_username_idx` (`pl_username`),
  KEY `pl_location_id_idx` (`pl_location_id`),
  CONSTRAINT `pl_location_id` FOREIGN KEY (`pl_location_id`) REFERENCES `location` (`location_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `pl_username` FOREIGN KEY (`pl_username`) REFERENCES `user` (`user_username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pinned_location`
--

LOCK TABLES `pinned_location` WRITE;
/*!40000 ALTER TABLE `pinned_location` DISABLE KEYS */;
INSERT INTO `pinned_location` VALUES (1,1,'joannlin','Great Beach!','2017-04-10 23:56:27','Beach'),(2,2,'joannlin','Fight On!','2017-04-10 23:56:48','School'),(20,5,'vivek','Nice pool','2017-04-14 20:52:01','Attraction'),(24,6,'joannlin','My old school','2017-04-14 21:00:52','School'),(25,7,'vivek','Cool music store','2017-04-17 21:56:00','Shopping'),(26,8,'joannlin','Great art and architecture','2017-04-17 22:01:00','Museum'),(27,9,'brooks','Beautiful hikes','2017-04-17 22:03:00','Hike'),(28,10,'brooks','Open all night, cash only though!','2017-04-17 22:03:00','Restaurant'),(29,11,'vivek','Tons of board games','2017-04-17 22:05:00','Restaurant'),(30,12,'joannlin','Awesome club to go to','2017-04-17 22:06:00','Entertainment');
/*!40000 ALTER TABLE `pinned_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_username` varchar(50) NOT NULL,
  `user_password` int(11) NOT NULL,
  `user_fname` varchar(50) DEFAULT NULL,
  `user_lname` varchar(50) DEFAULT NULL,
  `user_image` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`user_username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('guest', 0, 'Guest', 'G.', 'http://megaconorlando.com/wp-content/uploads/guess-who1.jpg'), ('brooks',-1380612200,'Brooks','K.','https://scontent-lax3-1.xx.fbcdn.net/v/t1.0-9/15032109_1858944011005619_2649454290485610510_n.jpg?oh=e217512e72fba8fccbf51916810321fd&oe=598266B5'),('drew',3091904,'Drew','P.','https://scontent-lax3-1.xx.fbcdn.net/v/t1.0-9/995353_953573524713215_7659526677841041864_n.jpg?oh=297f69b93270e1c12a3989ba075669dd&oe=59548F49'),('joannlin',-1630005611,'Joann','L.','https://scontent-lax3-1.xx.fbcdn.net/v/t1.0-9/1162_748777838587852_4690520839059034478_n.jpg?oh=ac197d6618f1c96b93dd53b67a826855&oe=5999AF2A'),('vivek',112220169,'Vivek','R.','https://scontent-lax3-1.xx.fbcdn.net/v/t1.0-9/14479506_1195989603800112_9027664968875431181_n.jpg?oh=3e7716d7e97b1a9e41f849d2eeb70b7b&oe=598D35AC'),('robert',-925713022,'Robert','D.','https://scontent-lax3-1.xx.fbcdn.net/v/t1.0-9/16143180_1317630958294843_7647925491845321171_n.jpg?oh=747727c1cd1206c70aa77a27ce08d8ed&oe=59885D06');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-14 21:09:08
