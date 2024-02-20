-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.4.0.6659
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for kumon_db
CREATE DATABASE IF NOT EXISTS `kumon_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `kumon_db`;

-- Dumping structure for table kumon_db.config
CREATE TABLE IF NOT EXISTS `config` (
  `property` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`property`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table kumon_db.contact
CREATE TABLE IF NOT EXISTS `contact` (
  `contact_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `phone` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `email` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `notifications` bit(1) NOT NULL DEFAULT b'0',
  `student_id` int NOT NULL,
  `relationship_id` int NOT NULL,
  PRIMARY KEY (`contact_id`),
  KEY `contact_student_fk` (`student_id`),
  KEY `contact_relationship_fk` (`relationship_id`),
  CONSTRAINT `contact_relationship_fk` FOREIGN KEY (`relationship_id`) REFERENCES `relationship` (`relationship_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `contact_student_fk` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=531 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table kumon_db.indicator
CREATE TABLE IF NOT EXISTS `indicator` (
  `indicator_id` int NOT NULL AUTO_INCREMENT,
  `color` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '#000000',
  `definition` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`indicator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=407 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table kumon_db.log
CREATE TABLE IF NOT EXISTS `log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `time_in` timestamp NOT NULL,
  `time_out` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`log_id`),
  KEY `log_student_fk` (`student_id`),
  CONSTRAINT `log_student_fk` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2009 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table kumon_db.relationship
CREATE TABLE IF NOT EXISTS `relationship` (
  `relationship_id` int NOT NULL AUTO_INCREMENT,
  `relationship` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`relationship_id`)
) ENGINE=InnoDB AUTO_INCREMENT=304 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table kumon_db.student
CREATE TABLE IF NOT EXISTS `student` (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `birthday` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `subject_id` tinyint NOT NULL,
  `notes` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `notes_expiry` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `indicator_id` int NOT NULL DEFAULT '401',
  PRIMARY KEY (`student_id`),
  KEY `student_subject_fk` (`subject_id`),
  KEY `student_indicator_fk` (`indicator_id`),
  CONSTRAINT `student_indicator_fk` FOREIGN KEY (`indicator_id`) REFERENCES `indicator` (`indicator_id`),
  CONSTRAINT `student_subject_fk` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1018 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table kumon_db.subject
CREATE TABLE IF NOT EXISTS `subject` (
  `subject_id` tinyint NOT NULL DEFAULT '0',
  `subject_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
