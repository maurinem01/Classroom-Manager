-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.39 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.8.0.6908
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

-- Dumping structure for table kumon_db.acuity_log
CREATE TABLE IF NOT EXISTS `acuity_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `time_in` timestamp NOT NULL,
  `time_out` timestamp NULL DEFAULT NULL,
  `appointment` timestamp NULL DEFAULT NULL,
  `on_time` bit(1) NOT NULL DEFAULT (0),
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `log_student_fk` (`student_id`) USING BTREE,
  CONSTRAINT `acuity_log_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- Dumping data for table kumon_db.acuity_log: ~0 rows (approximately)

-- Dumping structure for table kumon_db.config
CREATE TABLE IF NOT EXISTS `config` (
  `property` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`property`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table kumon_db.config: ~19 rows (approximately)
INSERT INTO `config` (`property`, `value`) VALUES
	('centre_name', 'CentreName'),
	('centre_phone', '204-123-4567'),
	('color_notes_bg', '#00FF33'),
	('color_notes_fg', '#000000'),
	('color_no_status_bg', '#FFFFFF'),
	('color_no_status_fg', '#000000'),
	('color_over_time_bg', '#F5AA96'),
	('color_over_time_fg', '#70291B'),
	('color_subject_change_bg', '#96C8F5'),
	('color_subject_change_fg', '#000000'),
	('color_warning_time_bg', '#FCF4A3'),
	('color_warning_time_fg', '#000000'),
	('link_acuity', 'true'),
	('send_messages', 'false'),
	('session_length', '30'),
	('text_in', 'MSG FROM #CENTRE_NAME# KUMON: #Student_First# has arrived at the center. This is an automated message, call #CENTRE_PHONE# for assistance. Text STOP to unsubscribe.'),
	('text_out', 'MSG FROM #CENTRE_NAME# KUMON: #Student_First# is ready for pickup. This is an automated message, call #CENTRE_PHONE# for assistance. Text STOP to unsubscribe.'),
	('time_format', 'hh:mm'),
	('warning_time', '10');

-- Dumping structure for table kumon_db.contact
CREATE TABLE IF NOT EXISTS `contact` (
  `contact_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `last_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `notifications` bit(1) NOT NULL DEFAULT b'0',
  `student_id` int NOT NULL,
  `relationship_id` int NOT NULL,
  PRIMARY KEY (`contact_id`),
  KEY `contact_student_fk` (`student_id`),
  KEY `contact_relationship_fk` (`relationship_id`),
  CONSTRAINT `contact_relationship_fk` FOREIGN KEY (`relationship_id`) REFERENCES `relationship` (`relationship_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `contact_student_fk` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table kumon_db.contact: ~0 rows (approximately)

-- Dumping structure for table kumon_db.indicator
CREATE TABLE IF NOT EXISTS `indicator` (
  `indicator_id` int NOT NULL AUTO_INCREMENT,
  `color` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '#000000',
  `definition` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`indicator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=405 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table kumon_db.indicator: ~4 rows (approximately)
INSERT INTO `indicator` (`indicator_id`, `color`, `definition`) VALUES
	(401, '#FFFFFF', ' '),
	(402, '#FF0000', 'Centre Graded'),
	(403, '#00FF00', 'Home Graded'),
	(404, '#0000FF', 'Kumon Connect');

-- Dumping structure for table kumon_db.relationship
CREATE TABLE IF NOT EXISTS `relationship` (
  `relationship_id` int NOT NULL AUTO_INCREMENT,
  `relationship` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`relationship_id`)
) ENGINE=InnoDB AUTO_INCREMENT=304 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table kumon_db.relationship: ~3 rows (approximately)
INSERT INTO `relationship` (`relationship_id`, `relationship`) VALUES
	(301, 'Contact 1'),
	(302, 'Contact 2'),
	(303, 'Other');

-- Dumping structure for table kumon_db.student
CREATE TABLE IF NOT EXISTS `student` (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `tag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `first_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `birthday` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `subject_id` tinyint NOT NULL,
  `notes_expiry` timestamp NOT NULL DEFAULT (now()),
  `notes` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `indicator_id` int NOT NULL DEFAULT '401',
  PRIMARY KEY (`student_id`),
  KEY `student_subject_fk` (`subject_id`),
  KEY `student_indicator_fk` (`indicator_id`),
  CONSTRAINT `student_indicator_fk` FOREIGN KEY (`indicator_id`) REFERENCES `indicator` (`indicator_id`),
  CONSTRAINT `student_subject_fk` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table kumon_db.student: ~0 rows (approximately)

-- Dumping structure for table kumon_db.subject
CREATE TABLE IF NOT EXISTS `subject` (
  `subject_id` tinyint NOT NULL DEFAULT '0',
  `subject_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table kumon_db.subject: ~3 rows (approximately)
INSERT INTO `subject` (`subject_id`, `subject_name`) VALUES
	(101, 'Math'),
	(102, 'Reading'),
	(103, 'Math and Reading');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
