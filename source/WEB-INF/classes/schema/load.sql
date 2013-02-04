# --------------------------------------------------------
# Host:                         127.0.0.1
# Server version:               5.1.47-community
# Server OS:                    Win32
# HeidiSQL version:             5.1.0.3378
# Date/time:                    2010-11-16 10:58:07
# --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

# Dumping structure for table jada.cache
CREATE TABLE IF NOT EXISTS `cache` (
  `cache_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cache_key` varchar(40) NOT NULL,
  `cache_text` longtext,
  `cache_type_code` char(1) NOT NULL,
  `cache_value` longblob,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  PRIMARY KEY (`cache_id`),
  UNIQUE KEY `cache_key` (`cache_key`,`site_id`),
  KEY `FK5A0AF8270EBDE65` (`site_id`),
  CONSTRAINT `FK5A0AF8270EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.cache: ~0 rows (approximately)
/*!40000 ALTER TABLE `cache` DISABLE KEYS */;
/*!40000 ALTER TABLE `cache` ENABLE KEYS */;


# Dumping structure for table jada.category
CREATE TABLE IF NOT EXISTS `category` (
  `cat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cat_natural_key` varchar(255) NOT NULL,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `cat_lang_id` bigint(20) DEFAULT NULL,
  `cat_parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`cat_id`),
  UNIQUE KEY `cat_natural_key` (`cat_natural_key`,`site_id`),
  KEY `FK302BCFE70EBDE65` (`site_id`),
  KEY `FK302BCFE886905A4` (`cat_lang_id`),
  KEY `FK302BCFE43B91D30` (`cat_parent_id`),
  CONSTRAINT `FK302BCFE43B91D30` FOREIGN KEY (`cat_parent_id`) REFERENCES `category` (`cat_id`),
  CONSTRAINT `FK302BCFE70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK302BCFE886905A4` FOREIGN KEY (`cat_lang_id`) REFERENCES `category_language` (`cat_lang_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.category: ~2 rows (approximately)
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`cat_id`, `cat_natural_key`, `published`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `seq_num`, `site_id`, `cat_lang_id`, `cat_parent_id`) VALUES
	(1, ' ', 'Y', 'system', '2009-08-10 17:51:20', 'system', '2009-08-10 17:51:13', 0, '_system', 1, NULL),
	(2, ' ', 'Y', 'system', '2009-08-10 17:52:11', 'system', '2009-08-10 17:52:04', 0, 'default', 2, NULL);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;


# Dumping structure for table jada.category_custom_attribute
CREATE TABLE IF NOT EXISTS `category_custom_attribute` (
  `cat_id` bigint(20) NOT NULL,
  `custom_attrib_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cat_id`,`custom_attrib_id`),
  KEY `FK7534D86F30F21FAD` (`cat_id`),
  KEY `FK7534D86F83C266A2` (`custom_attrib_id`),
  CONSTRAINT `FK7534D86F83C266A2` FOREIGN KEY (`custom_attrib_id`) REFERENCES `custom_attrib` (`custom_attrib_id`),
  CONSTRAINT `FK7534D86F30F21FAD` FOREIGN KEY (`cat_id`) REFERENCES `category` (`cat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.category_custom_attribute: ~0 rows (approximately)
/*!40000 ALTER TABLE `category_custom_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `category_custom_attribute` ENABLE KEYS */;


# Dumping structure for table jada.category_language
CREATE TABLE IF NOT EXISTS `category_language` (
  `cat_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cat_desc` longtext,
  `cat_short_title` varchar(20) DEFAULT NULL,
  `cat_title` varchar(40) DEFAULT NULL,
  `meta_keywords` varchar(1000) DEFAULT NULL,
  `meta_description` varchar(1000) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `cat_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`cat_lang_id`),
  KEY `FK3466C57930F21FAD` (`cat_id`),
  KEY `FK3466C579473E64D1` (`site_profile_class_id`),
  CONSTRAINT `FK3466C57930F21FAD` FOREIGN KEY (`cat_id`) REFERENCES `category` (`cat_id`),
  CONSTRAINT `FK3466C579473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.category_language: ~2 rows (approximately)
/*!40000 ALTER TABLE `category_language` DISABLE KEYS */;
INSERT INTO `category_language` (`cat_lang_id`, `cat_desc`, `cat_short_title`, `cat_title`, `meta_keywords`, `meta_description`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `cat_id`, `site_profile_class_id`) VALUES
	(1, NULL, 'Home', 'Home', '', '', 'system', '2009-08-10 17:50:34', 'system', '2009-08-10 17:50:25', 1, 1),
	(2, NULL, 'Home', 'Home', '', '', 'system', '2009-08-10 17:50:34', 'system', '2009-08-10 17:50:25', 2, 2);
/*!40000 ALTER TABLE `category_language` ENABLE KEYS */;


# Dumping structure for table jada.category_site_domain
CREATE TABLE IF NOT EXISTS `category_site_domain` (
  `cat_id` bigint(20) NOT NULL,
  `site_domain_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cat_id`,`site_domain_id`),
  KEY `FKDBCE12FB30F21FAD` (`cat_id`),
  KEY `FKDBCE12FBB8784434` (`site_domain_id`),
  CONSTRAINT `FKDBCE12FBB8784434` FOREIGN KEY (`site_domain_id`) REFERENCES `site_domain` (`site_domain_id`),
  CONSTRAINT `FKDBCE12FB30F21FAD` FOREIGN KEY (`cat_id`) REFERENCES `category` (`cat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.category_site_domain: ~0 rows (approximately)
/*!40000 ALTER TABLE `category_site_domain` DISABLE KEYS */;
/*!40000 ALTER TABLE `category_site_domain` ENABLE KEYS */;


# Dumping structure for table jada.comment
CREATE TABLE IF NOT EXISTS `comment` (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` char(1) NOT NULL,
  `comment` longtext NOT NULL,
  `comment_approved` char(1) DEFAULT NULL,
  `comment_rating` int(11) NOT NULL,
  `comment_title` varchar(255) NOT NULL,
  `moderation` char(1) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `cust_id` bigint(20) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `FK38A5EE5F71DEBAE5` (`item_id`),
  KEY `FK38A5EE5F6DA0AD2F` (`content_id`),
  KEY `FK38A5EE5FE6CEAEF0` (`cust_id`),
  CONSTRAINT `FK38A5EE5FE6CEAEF0` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`cust_id`),
  CONSTRAINT `FK38A5EE5F6DA0AD2F` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`),
  CONSTRAINT `FK38A5EE5F71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.comment: ~0 rows (approximately)
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;


# Dumping structure for table jada.comment_agree_customer
CREATE TABLE IF NOT EXISTS `comment_agree_customer` (
  `comment_id` bigint(20) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`comment_id`,`customer_id`),
  KEY `FKB42595F193629B6F` (`comment_id`),
  KEY `FKB42595F139F48585` (`customer_id`),
  CONSTRAINT `FKB42595F139F48585` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`cust_id`),
  CONSTRAINT `FKB42595F193629B6F` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.comment_agree_customer: ~0 rows (approximately)
/*!40000 ALTER TABLE `comment_agree_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_agree_customer` ENABLE KEYS */;


# Dumping structure for table jada.comment_disagree_customer
CREATE TABLE IF NOT EXISTS `comment_disagree_customer` (
  `comment_id` bigint(20) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`comment_id`,`customer_id`),
  KEY `FK8AFA923F93629B6F` (`comment_id`),
  KEY `FK8AFA923F39F48585` (`customer_id`),
  CONSTRAINT `FK8AFA923F39F48585` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`cust_id`),
  CONSTRAINT `FK8AFA923F93629B6F` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.comment_disagree_customer: ~0 rows (approximately)
/*!40000 ALTER TABLE `comment_disagree_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_disagree_customer` ENABLE KEYS */;


# Dumping structure for table jada.contact_us
CREATE TABLE IF NOT EXISTS `contact_us` (
  `contact_us_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` char(1) NOT NULL,
  `contact_us_address_line1` varchar(30) NOT NULL,
  `contact_us_address_line2` varchar(30) NOT NULL,
  `contact_us_city_name` varchar(25) NOT NULL,
  `contact_us_country_code` varchar(2) NOT NULL,
  `contact_us_country_name` varchar(40) NOT NULL,
  `contact_us_email` varchar(50) NOT NULL,
  `contact_us_phone` varchar(20) DEFAULT NULL,
  `contact_us_state_code` varchar(2) NOT NULL,
  `contact_us_state_name` varchar(40) NOT NULL,
  `contact_us_zip_code` varchar(10) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `contact_us_lang_id` bigint(20) NOT NULL,
  PRIMARY KEY (`contact_us_id`),
  KEY `FK8565B1D8445CA15` (`contact_us_lang_id`),
  KEY `FK8565B1D70EBDE65` (`site_id`),
  CONSTRAINT `FK8565B1D70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK8565B1D8445CA15` FOREIGN KEY (`contact_us_lang_id`) REFERENCES `contact_us_language` (`contact_us_lang_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.contact_us: ~0 rows (approximately)
/*!40000 ALTER TABLE `contact_us` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact_us` ENABLE KEYS */;


# Dumping structure for table jada.contact_us_language
CREATE TABLE IF NOT EXISTS `contact_us_language` (
  `contact_us_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contact_us_desc` longtext,
  `contact_us_name` varchar(50) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `contact_us_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`contact_us_lang_id`),
  KEY `FK65299A3A473E64D1` (`site_profile_class_id`),
  KEY `FK65299A3A8FB39530` (`contact_us_id`),
  CONSTRAINT `FK65299A3A8FB39530` FOREIGN KEY (`contact_us_id`) REFERENCES `contact_us` (`contact_us_id`),
  CONSTRAINT `FK65299A3A473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.contact_us_language: ~0 rows (approximately)
/*!40000 ALTER TABLE `contact_us_language` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact_us_language` ENABLE KEYS */;


# Dumping structure for table jada.content
CREATE TABLE IF NOT EXISTS `content` (
  `content_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_expire_on` date NOT NULL,
  `content_hit_counter` int(11) NOT NULL,
  `content_natural_key` varchar(255) NOT NULL,
  `content_publish_on` date NOT NULL,
  `content_rating` float NOT NULL,
  `content_rating_count` int(11) NOT NULL,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `content_lang_id` bigint(20) NOT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  KEY `FK38B73479EAFC5FE5` (`user_id`),
  KEY `FK38B7347970EBDE65` (`site_id`),
  KEY `FK38B73479E9C13B4C` (`content_lang_id`),
  CONSTRAINT `FK38B73479E9C13B4C` FOREIGN KEY (`content_lang_id`) REFERENCES `content_language` (`content_lang_id`),
  CONSTRAINT `FK38B7347970EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK38B73479EAFC5FE5` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.content: ~0 rows (approximately)
/*!40000 ALTER TABLE `content` DISABLE KEYS */;
/*!40000 ALTER TABLE `content` ENABLE KEYS */;


# Dumping structure for table jada.content_category
CREATE TABLE IF NOT EXISTS `content_category` (
  `content_id` bigint(20) NOT NULL,
  `cat_id` bigint(20) NOT NULL,
  PRIMARY KEY (`content_id`,`cat_id`),
  KEY `FKF41B2046DA0AD2F` (`content_id`),
  KEY `FKF41B20430F21FAD` (`cat_id`),
  CONSTRAINT `FKF41B20430F21FAD` FOREIGN KEY (`cat_id`) REFERENCES `category` (`cat_id`),
  CONSTRAINT `FKF41B2046DA0AD2F` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.content_category: ~0 rows (approximately)
/*!40000 ALTER TABLE `content_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `content_category` ENABLE KEYS */;


# Dumping structure for table jada.content_desc_search
CREATE TABLE IF NOT EXISTS `content_desc_search` (
  `content_desc_search_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_title` varchar(128) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`content_desc_search_id`),
  KEY `FKCEEFB4506DA0AD2F` (`content_id`),
  KEY `FKCEEFB450473E64D1` (`site_profile_class_id`),
  CONSTRAINT `FKCEEFB450473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FKCEEFB4506DA0AD2F` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.content_desc_search: ~0 rows (approximately)
/*!40000 ALTER TABLE `content_desc_search` DISABLE KEYS */;
/*!40000 ALTER TABLE `content_desc_search` ENABLE KEYS */;


# Dumping structure for table jada.content_image
CREATE TABLE IF NOT EXISTS `content_image` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_type` varchar(20) NOT NULL,
  `image_height` int(11) NOT NULL,
  `image_name` varchar(40) NOT NULL,
  `image_value` longblob NOT NULL,
  `image_width` int(11) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `content_lang_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`image_id`),
  KEY `FK694215E9C13B4C` (`content_lang_id`),
  CONSTRAINT `FK694215E9C13B4C` FOREIGN KEY (`content_lang_id`) REFERENCES `content_language` (`content_lang_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.content_image: ~0 rows (approximately)
/*!40000 ALTER TABLE `content_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `content_image` ENABLE KEYS */;


# Dumping structure for table jada.content_image_language
CREATE TABLE IF NOT EXISTS `content_image_language` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_type` varchar(20) NOT NULL,
  `image_height` int(11) NOT NULL,
  `image_name` varchar(40) NOT NULL,
  `image_value` longblob NOT NULL,
  `image_width` int(11) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `content_lang_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`image_id`),
  KEY `FK41F0A42473E64D1` (`site_profile_class_id`),
  KEY `FK41F0A42E9C13B4C` (`content_lang_id`),
  CONSTRAINT `FK41F0A42E9C13B4C` FOREIGN KEY (`content_lang_id`) REFERENCES `content_language` (`content_lang_id`),
  CONSTRAINT `FK41F0A42473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.content_image_language: ~0 rows (approximately)
/*!40000 ALTER TABLE `content_image_language` DISABLE KEYS */;
/*!40000 ALTER TABLE `content_image_language` ENABLE KEYS */;


# Dumping structure for table jada.content_language
CREATE TABLE IF NOT EXISTS `content_language` (
  `content_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_desc` longtext,
  `content_image_override` varchar(1) NOT NULL,
  `meta_keywords` varchar(1000) DEFAULT NULL,
  `meta_description` varchar(1000) DEFAULT NULL,
  `content_short_desc` longtext,
  `content_title` varchar(128) DEFAULT NULL,
  `page_title` varchar(255) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `image_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`content_lang_id`),
  UNIQUE KEY `image_id` (`image_id`),
  KEY `FKAC11885EF7B439EC` (`image_id`),
  KEY `FKAC11885E6DA0AD2F` (`content_id`),
  KEY `FKAC11885E473E64D1` (`site_profile_class_id`),
  CONSTRAINT `FKAC11885E473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FKAC11885E6DA0AD2F` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`),
  CONSTRAINT `FKAC11885EF7B439EC` FOREIGN KEY (`image_id`) REFERENCES `content_image` (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.content_language: ~0 rows (approximately)
/*!40000 ALTER TABLE `content_language` DISABLE KEYS */;
/*!40000 ALTER TABLE `content_language` ENABLE KEYS */;


# Dumping structure for table jada.content_related
CREATE TABLE IF NOT EXISTS `content_related` (
  `content_id` bigint(20) NOT NULL,
  `item_related_id` bigint(20) NOT NULL,
  PRIMARY KEY (`content_id`,`item_related_id`),
  KEY `FK5A2AEE656DA0AD2F` (`content_id`),
  KEY `FK5A2AEE65242D10E9` (`item_related_id`),
  CONSTRAINT `FK5A2AEE65242D10E9` FOREIGN KEY (`item_related_id`) REFERENCES `content` (`content_id`),
  CONSTRAINT `FK5A2AEE656DA0AD2F` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.content_related: ~0 rows (approximately)
/*!40000 ALTER TABLE `content_related` DISABLE KEYS */;
/*!40000 ALTER TABLE `content_related` ENABLE KEYS */;


# Dumping structure for table jada.control
CREATE TABLE IF NOT EXISTS `control` (
  `control_key` varchar(20) NOT NULL,
  `control_value` varchar(50) NOT NULL,
  PRIMARY KEY (`control_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.control: ~0 rows (approximately)
/*!40000 ALTER TABLE `control` DISABLE KEYS */;
/*!40000 ALTER TABLE `control` ENABLE KEYS */;


# Dumping structure for table jada.country
CREATE TABLE IF NOT EXISTS `country` (
  `country_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country_code` varchar(2) NOT NULL,
  `country_name` varchar(80) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `shipping_region_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`country_id`),
  UNIQUE KEY `country_code` (`country_code`,`site_id`),
  KEY `FK39175796EDA4B782` (`shipping_region_id`),
  KEY `FK3917579670EBDE65` (`site_id`),
  CONSTRAINT `FK3917579670EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK39175796EDA4B782` FOREIGN KEY (`shipping_region_id`) REFERENCES `shipping_region` (`shipping_region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=491 DEFAULT CHARSET=utf8;

# Dumping data for table jada.country: ~565 rows (approximately)
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` (`country_id`, `country_code`, `country_name`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `site_id`, `shipping_region_id`) VALUES
	(1, 'AE', 'United Arab Emirates', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(2, 'AF', 'Afghanistan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(3, 'AG', 'Antigua and Barbuda', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(4, 'AI', 'Anguilla', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(5, 'AL', 'Albania', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(6, 'AM', 'Armenia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(7, 'AN', 'Netherlands Antilles', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(8, 'AO', 'Angola', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(9, 'AQ', 'Antarctica', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(10, 'AR', 'Argentina', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(11, 'AS', 'American Samoa', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(12, 'AT', 'Austria', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(13, 'AU', 'Australia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(14, 'AW', 'Aruba', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(15, 'AX', 'Aland Islands ?Ã…land Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(16, 'AZ', 'Azerbaijan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(17, 'BA', 'Bosnia and Herzegovina', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(18, 'BB', 'Barbados', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(19, 'BD', 'Bangladesh', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(20, 'BE', 'Belgium', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(21, 'BF', 'Burkina Faso', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(22, 'BG', 'Bulgaria', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(23, 'BH', 'Bahrain', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(24, 'BI', 'Burundi', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(25, 'BJ', 'Benin', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(26, 'BL', 'Saint BarthÃ©lemy', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(27, 'BM', 'Bermuda', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(28, 'BN', 'Brunei Darussalam', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(29, 'BO', 'Bolivia, Plurinational State of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(30, 'BR', 'Brazil', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(31, 'BS', 'Bahamas', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(32, 'BT', 'Bhutan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(33, 'BV', 'Bouvet Island', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(34, 'BW', 'Botswana', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(35, 'BY', 'Belarus', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(36, 'BZ', 'Belize', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(37, 'CA', 'Canada', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(38, 'CC', 'Cocos (Keeling) Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(39, 'CD', 'Congo, the Democratic Republic of the', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(40, 'CF', 'Central African Republic', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(41, 'CG', 'Congo', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(42, 'CH', 'Switzerland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(43, 'CI', 'Cote d\'Ivoire ?CÃ´te d\'Ivoire', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(44, 'CK', 'Cook Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(45, 'CL', 'Chile', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(46, 'CM', 'Cameroon', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(47, 'CN', 'China', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(48, 'CO', 'Colombia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(49, 'CR', 'Costa Rica', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(50, 'CU', 'Cuba', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(51, 'CV', 'Cape Verde', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(52, 'CX', 'Christmas Island', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(53, 'CY', 'Cyprus', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(54, 'CZ', 'Czech Republic', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(55, 'DE', 'Germany', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(56, 'DJ', 'Djibouti', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(57, 'DK', 'Denmark', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(58, 'DM', 'Dominica', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(59, 'DO', 'Dominican Republic', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(60, 'DZ', 'Algeria', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(61, 'EC', 'Ecuador', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(62, 'EE', 'Estonia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(63, 'EG', 'Egypt', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(64, 'EH', 'Western Sahara', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(65, 'ER', 'Eritrea', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(66, 'ES', 'Spain', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(67, 'ET', 'Ethiopia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(68, 'FI', 'Finland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(69, 'FJ', 'Fiji', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(70, 'FK', 'Falkland Islands (Malvinas)', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(71, 'FM', 'Micronesia, Federated States of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(72, 'FO', 'Faroe Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(73, 'FR', 'France', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(74, 'GA', 'Gabon', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(75, 'GB', 'United Kingdom', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(76, 'GD', 'Grenada', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(77, 'GE', 'Georgia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(78, 'GF', 'French Guiana', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(79, 'GG', 'Guernsey', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(80, 'GH', 'Ghana', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(81, 'GI', 'Gibraltar', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(82, 'GL', 'Greenland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(83, 'GM', 'Gambia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(84, 'GN', 'Guinea', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(85, 'GP', 'Guadeloupe', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(86, 'GQ', 'Equatorial Guinea', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(87, 'GR', 'Greece', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(88, 'GS', 'South Georgia and the South Sandwich Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(89, 'GT', 'Guatemala', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(90, 'GU', 'Guam', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(91, 'GW', 'Guinea-Bissau', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(92, 'GY', 'Guyana', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(93, 'HK', 'Hong Kong', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(94, 'HM', 'Heard Island and McDonald Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(95, 'HN', 'Honduras', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(96, 'HR', 'Croatia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(97, 'HT', 'Haiti', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(98, 'HU', 'Hungary', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(99, 'ID', 'Indonesia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(100, 'IE', 'Ireland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(101, 'IL', 'Israel', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(102, 'IM', 'Isle of Man', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(103, 'IN', 'India', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(104, 'IO', 'British Indian Ocean Territory', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(105, 'IQ', 'Iraq', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(106, 'IR', 'Iran, Islamic Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(107, 'IS', 'Iceland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(108, 'IT', 'Italy', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(109, 'JE', 'Jersey', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(110, 'JM', 'Jamaica', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(111, 'JO', 'Jordan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(112, 'JP', 'Japan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(113, 'KE', 'Kenya', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(114, 'KG', 'Kyrgyzstan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(115, 'KH', 'Cambodia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(116, 'KI', 'Kiribati', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(117, 'KM', 'Comoros', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(118, 'KN', 'Saint Kitts and Nevis', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(119, 'KP', 'Korea, Democratic People\'s Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(120, 'KR', 'Korea, Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(121, 'KW', 'Kuwait', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(122, 'KY', 'Cayman Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(123, 'KZ', 'Kazakhstan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(124, 'LA', 'Lao People\'s Democratic Republic', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(125, 'LB', 'Lebanon', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(126, 'LC', 'Saint Lucia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(127, 'LI', 'Liechtenstein', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(128, 'LK', 'Sri Lanka', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(129, 'LR', 'Liberia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(130, 'LS', 'Lesotho', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(131, 'LT', 'Lithuania', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(132, 'LU', 'Luxembourg', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(133, 'LV', 'Latvia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(134, 'LY', 'Libyan Arab Jamahiriya', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(135, 'MA', 'Morocco', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(136, 'MC', 'Monaco', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(137, 'MD', 'Moldova, Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(138, 'ME', 'Montenegro', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(139, 'MF', 'Saint Martin (French part)', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(140, 'MG', 'Madagascar', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(141, 'MH', 'Marshall Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(142, 'MK', 'Macedonia, the former Yugoslav Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(143, 'ML', 'Mali', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(144, 'MM', 'Myanmar', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(145, 'MN', 'Mongolia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(146, 'MO', 'Macao', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(147, 'MP', 'Northern Mariana Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(148, 'MQ', 'Martinique', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(149, 'MR', 'Mauritania', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(150, 'MS', 'Montserrat', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(151, 'MT', 'Malta', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(152, 'MU', 'Mauritius', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(153, 'MV', 'Maldives', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(154, 'MW', 'Malawi', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(155, 'MX', 'Mexico', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(156, 'MY', 'Malaysia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(157, 'MZ', 'Mozambique', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(158, 'NA', 'Namibia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(159, 'NC', 'New Caledonia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(160, 'NE', 'Niger', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(161, 'NF', 'Norfolk Island', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(162, 'NG', 'Nigeria', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(163, 'NI', 'Nicaragua', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(164, 'NL', 'Netherlands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(165, 'NO', 'Norway', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(166, 'NP', 'Nepal', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(167, 'NR', 'Nauru', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(168, 'NU', 'Niue', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(169, 'NZ', 'New Zealand', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(170, 'OM', 'Oman', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(171, 'PA', 'Panama', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(172, 'PE', 'Peru', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(173, 'PF', 'French Polynesia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(174, 'PG', 'Papua New Guinea', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(175, 'PH', 'Philippines', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(176, 'PK', 'Pakistan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(177, 'PL', 'Poland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(178, 'PM', 'Saint Pierre and Miquelon', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(179, 'PN', 'Pitcairn', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(180, 'PR', 'Puerto Rico', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(181, 'PS', 'Palestinian Territory, Occupied', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(182, 'PT', 'Portugal', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(183, 'PW', 'Palau', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(184, 'PY', 'Paraguay', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(185, 'QA', 'Qatar', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(186, 'RE', 'Reunion ?RÃ©union', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(187, 'RO', 'Romania', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(188, 'RS', 'Serbia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(189, 'RU', 'Russian Federation', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(190, 'RW', 'Rwanda', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(191, 'SA', 'Saudi Arabia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(192, 'SB', 'Solomon Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(193, 'SC', 'Seychelles', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(194, 'SD', 'Sudan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(195, 'SE', 'Sweden', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(196, 'SG', 'Singapore', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(197, 'SH', 'Saint Helena', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(198, 'SI', 'Slovenia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(199, 'SJ', 'Svalbard and Jan Mayen', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(200, 'SK', 'Slovakia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(201, 'SL', 'Sierra Leone', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(202, 'SM', 'San Marino', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(203, 'SN', 'Senegal', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(204, 'SO', 'Somalia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(205, 'SR', 'Suriname', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(206, 'ST', 'Sao Tome and Principe', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(207, 'SV', 'El Salvador', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(208, 'SY', 'Syrian Arab Republic', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(209, 'SZ', 'Swaziland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(210, 'TC', 'Turks and Caicos Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(211, 'TD', 'Chad', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(212, 'TF', 'French Southern Territories', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(213, 'TG', 'Togo', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(214, 'TH', 'Thailand', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(215, 'TJ', 'Tajikistan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(216, 'TK', 'Tokelau', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(217, 'TL', 'Timor-Leste', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(218, 'TM', 'Turkmenistan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(219, 'TN', 'Tunisia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(220, 'TO', 'Tonga', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(221, 'TR', 'Turkey', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(222, 'TT', 'Trinidad and Tobago', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(223, 'TV', 'Tuvalu', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(224, 'TW', 'Taiwan, Province of China', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(225, 'TZ', 'Tanzania, United Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(226, 'UA', 'Ukraine', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(227, 'UG', 'Uganda', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(228, 'UM', 'United States Minor Outlying Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(229, 'US', 'United States', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(230, 'UY', 'Uruguay', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(231, 'UZ', 'Uzbekistan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(232, 'VA', 'Holy See (Vatican City State)', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(233, 'VC', 'Saint Vincent and the Grenadines', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(234, 'VE', 'Venezuela, Bolivarian Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(235, 'VG', 'Virgin Islands, British', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(236, 'VI', 'Virgin Islands, U.S.', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(237, 'VN', 'Viet Nam', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(238, 'VU', 'Vanuatu', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(239, 'WF', 'Wallis and Futuna', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(240, 'WS', 'Samoa', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(241, 'YE', 'Yemen', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(242, 'YT', 'Mayotte', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(243, 'ZA', 'South Africa', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(244, 'ZM', 'Zambia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(245, 'ZW', 'Zimbabwe', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', '_system', NULL),
	(246, 'AE', 'United Arab Emirates', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(247, 'AF', 'Afghanistan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(248, 'AG', 'Antigua and Barbuda', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(249, 'AI', 'Anguilla', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(250, 'AL', 'Albania', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(251, 'AM', 'Armenia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(252, 'AN', 'Netherlands Antilles', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(253, 'AO', 'Angola', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(254, 'AQ', 'Antarctica', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(255, 'AR', 'Argentina', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(256, 'AS', 'American Samoa', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(257, 'AT', 'Austria', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(258, 'AU', 'Australia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(259, 'AW', 'Aruba', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(260, 'AX', 'Aland Islands ?Ã…land Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(261, 'AZ', 'Azerbaijan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(262, 'BA', 'Bosnia and Herzegovina', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(263, 'BB', 'Barbados', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(264, 'BD', 'Bangladesh', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(265, 'BE', 'Belgium', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(266, 'BF', 'Burkina Faso', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(267, 'BG', 'Bulgaria', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(268, 'BH', 'Bahrain', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(269, 'BI', 'Burundi', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(270, 'BJ', 'Benin', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(271, 'BL', 'Saint BarthÃ©lemy', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(272, 'BM', 'Bermuda', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(273, 'BN', 'Brunei Darussalam', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(274, 'BO', 'Bolivia, Plurinational State of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(275, 'BR', 'Brazil', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(276, 'BS', 'Bahamas', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(277, 'BT', 'Bhutan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(278, 'BV', 'Bouvet Island', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(279, 'BW', 'Botswana', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(280, 'BY', 'Belarus', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(281, 'BZ', 'Belize', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(282, 'CA', 'Canada', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', 1),
	(283, 'CC', 'Cocos (Keeling) Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(284, 'CD', 'Congo, the Democratic Republic of the', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(285, 'CF', 'Central African Republic', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(286, 'CG', 'Congo', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(287, 'CH', 'Switzerland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(288, 'CI', 'Cote d\'Ivoire ?CÃ´te d\'Ivoire', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(289, 'CK', 'Cook Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(290, 'CL', 'Chile', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(291, 'CM', 'Cameroon', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(292, 'CN', 'China', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(293, 'CO', 'Colombia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(294, 'CR', 'Costa Rica', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(295, 'CU', 'Cuba', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(296, 'CV', 'Cape Verde', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(297, 'CX', 'Christmas Island', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(298, 'CY', 'Cyprus', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(299, 'CZ', 'Czech Republic', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(300, 'DE', 'Germany', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(301, 'DJ', 'Djibouti', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(302, 'DK', 'Denmark', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(303, 'DM', 'Dominica', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(304, 'DO', 'Dominican Republic', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(305, 'DZ', 'Algeria', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(306, 'EC', 'Ecuador', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(307, 'EE', 'Estonia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(308, 'EG', 'Egypt', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(309, 'EH', 'Western Sahara', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(310, 'ER', 'Eritrea', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(311, 'ES', 'Spain', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(312, 'ET', 'Ethiopia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(313, 'FI', 'Finland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(314, 'FJ', 'Fiji', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(315, 'FK', 'Falkland Islands (Malvinas)', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(316, 'FM', 'Micronesia, Federated States of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(317, 'FO', 'Faroe Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(318, 'FR', 'France', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(319, 'GA', 'Gabon', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(320, 'GB', 'United Kingdom', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(321, 'GD', 'Grenada', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(322, 'GE', 'Georgia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(323, 'GF', 'French Guiana', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(324, 'GG', 'Guernsey', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(325, 'GH', 'Ghana', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(326, 'GI', 'Gibraltar', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(327, 'GL', 'Greenland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(328, 'GM', 'Gambia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(329, 'GN', 'Guinea', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(330, 'GP', 'Guadeloupe', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(331, 'GQ', 'Equatorial Guinea', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(332, 'GR', 'Greece', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(333, 'GS', 'South Georgia and the South Sandwich Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(334, 'GT', 'Guatemala', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(335, 'GU', 'Guam', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(336, 'GW', 'Guinea-Bissau', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(337, 'GY', 'Guyana', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(338, 'HK', 'Hong Kong', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(339, 'HM', 'Heard Island and McDonald Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(340, 'HN', 'Honduras', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(341, 'HR', 'Croatia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(342, 'HT', 'Haiti', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(343, 'HU', 'Hungary', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(344, 'ID', 'Indonesia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(345, 'IE', 'Ireland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(346, 'IL', 'Israel', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(347, 'IM', 'Isle of Man', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(348, 'IN', 'India', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(349, 'IO', 'British Indian Ocean Territory', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(350, 'IQ', 'Iraq', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(351, 'IR', 'Iran, Islamic Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(352, 'IS', 'Iceland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(353, 'IT', 'Italy', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(354, 'JE', 'Jersey', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(355, 'JM', 'Jamaica', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(356, 'JO', 'Jordan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(357, 'JP', 'Japan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(358, 'KE', 'Kenya', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(359, 'KG', 'Kyrgyzstan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(360, 'KH', 'Cambodia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(361, 'KI', 'Kiribati', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(362, 'KM', 'Comoros', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(363, 'KN', 'Saint Kitts and Nevis', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(364, 'KP', 'Korea, Democratic People\'s Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(365, 'KR', 'Korea, Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(366, 'KW', 'Kuwait', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(367, 'KY', 'Cayman Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(368, 'KZ', 'Kazakhstan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(369, 'LA', 'Lao People\'s Democratic Republic', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(370, 'LB', 'Lebanon', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(371, 'LC', 'Saint Lucia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(372, 'LI', 'Liechtenstein', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(373, 'LK', 'Sri Lanka', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(374, 'LR', 'Liberia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(375, 'LS', 'Lesotho', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(376, 'LT', 'Lithuania', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(377, 'LU', 'Luxembourg', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(378, 'LV', 'Latvia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(379, 'LY', 'Libyan Arab Jamahiriya', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(380, 'MA', 'Morocco', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(381, 'MC', 'Monaco', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(382, 'MD', 'Moldova, Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(383, 'ME', 'Montenegro', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(384, 'MF', 'Saint Martin (French part)', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(385, 'MG', 'Madagascar', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(386, 'MH', 'Marshall Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(387, 'MK', 'Macedonia, the former Yugoslav Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(388, 'ML', 'Mali', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(389, 'MM', 'Myanmar', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(390, 'MN', 'Mongolia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(391, 'MO', 'Macao', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(392, 'MP', 'Northern Mariana Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(393, 'MQ', 'Martinique', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(394, 'MR', 'Mauritania', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(395, 'MS', 'Montserrat', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(396, 'MT', 'Malta', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(397, 'MU', 'Mauritius', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(398, 'MV', 'Maldives', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(399, 'MW', 'Malawi', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(400, 'MX', 'Mexico', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(401, 'MY', 'Malaysia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(402, 'MZ', 'Mozambique', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(403, 'NA', 'Namibia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(404, 'NC', 'New Caledonia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(405, 'NE', 'Niger', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(406, 'NF', 'Norfolk Island', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(407, 'NG', 'Nigeria', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(408, 'NI', 'Nicaragua', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(409, 'NL', 'Netherlands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(410, 'NO', 'Norway', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(411, 'NP', 'Nepal', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(412, 'NR', 'Nauru', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(413, 'NU', 'Niue', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(414, 'NZ', 'New Zealand', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(415, 'OM', 'Oman', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(416, 'PA', 'Panama', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(417, 'PE', 'Peru', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(418, 'PF', 'French Polynesia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(419, 'PG', 'Papua New Guinea', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(420, 'PH', 'Philippines', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(421, 'PK', 'Pakistan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(422, 'PL', 'Poland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(423, 'PM', 'Saint Pierre and Miquelon', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(424, 'PN', 'Pitcairn', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(425, 'PR', 'Puerto Rico', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(426, 'PS', 'Palestinian Territory, Occupied', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(427, 'PT', 'Portugal', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(428, 'PW', 'Palau', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(429, 'PY', 'Paraguay', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(430, 'QA', 'Qatar', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(431, 'RE', 'Reunion ?RÃ©union', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(432, 'RO', 'Romania', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(433, 'RS', 'Serbia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(434, 'RU', 'Russian Federation', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(435, 'RW', 'Rwanda', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(436, 'SA', 'Saudi Arabia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(437, 'SB', 'Solomon Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(438, 'SC', 'Seychelles', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(439, 'SD', 'Sudan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(440, 'SE', 'Sweden', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(441, 'SG', 'Singapore', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(442, 'SH', 'Saint Helena', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(443, 'SI', 'Slovenia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(444, 'SJ', 'Svalbard and Jan Mayen', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(445, 'SK', 'Slovakia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(446, 'SL', 'Sierra Leone', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(447, 'SM', 'San Marino', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(448, 'SN', 'Senegal', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(449, 'SO', 'Somalia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(450, 'SR', 'Suriname', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(451, 'ST', 'Sao Tome and Principe', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(452, 'SV', 'El Salvador', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(453, 'SY', 'Syrian Arab Republic', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(454, 'SZ', 'Swaziland', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(455, 'TC', 'Turks and Caicos Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(456, 'TD', 'Chad', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(457, 'TF', 'French Southern Territories', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(458, 'TG', 'Togo', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(459, 'TH', 'Thailand', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(460, 'TJ', 'Tajikistan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(461, 'TK', 'Tokelau', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(462, 'TL', 'Timor-Leste', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(463, 'TM', 'Turkmenistan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(464, 'TN', 'Tunisia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(465, 'TO', 'Tonga', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(466, 'TR', 'Turkey', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(467, 'TT', 'Trinidad and Tobago', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(468, 'TV', 'Tuvalu', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(469, 'TW', 'Taiwan, Province of China', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(470, 'TZ', 'Tanzania, United Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(471, 'UA', 'Ukraine', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(472, 'UG', 'Uganda', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(473, 'UM', 'United States Minor Outlying Islands', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(474, 'US', 'United States', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(475, 'UY', 'Uruguay', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(476, 'UZ', 'Uzbekistan', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(477, 'VA', 'Holy See (Vatican City State)', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(478, 'VC', 'Saint Vincent and the Grenadines', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(479, 'VE', 'Venezuela, Bolivarian Republic of', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(480, 'VG', 'Virgin Islands, British', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(481, 'VI', 'Virgin Islands, U.S.', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(482, 'VN', 'Viet Nam', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(483, 'VU', 'Vanuatu', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(484, 'WF', 'Wallis and Futuna', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(485, 'WS', 'Samoa', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(486, 'YE', 'Yemen', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(487, 'YT', 'Mayotte', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(488, 'ZA', 'South Africa', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(489, 'ZM', 'Zambia', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL),
	(490, 'ZW', 'Zimbabwe', 'system', '2009-06-10 21:22:05', 'system', '2009-06-10 21:22:05', 'default', NULL);
/*!40000 ALTER TABLE `country` ENABLE KEYS */;


# Dumping structure for table jada.coupon
CREATE TABLE IF NOT EXISTS `coupon` (
  `coupon_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coupon_apply_all` char(1) NOT NULL,
  `coupon_auto_apply` char(1) NOT NULL,
  `coupon_code` varchar(20) NOT NULL,
  `couponDiscountPercent` float DEFAULT NULL,
  `coupon_end_date` date NOT NULL,
  `coupon_max_cust_use` int(11) DEFAULT NULL,
  `coupon_max_use` int(11) DEFAULT NULL,
  `coupon_priority` int(11) DEFAULT NULL,
  `coupon_scope` char(1) NOT NULL,
  `coupon_start_date` date NOT NULL,
  `coupon_total_used` int(11) NOT NULL,
  `coupon_type` char(1) NOT NULL,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `coupon_curr_id` bigint(20) NOT NULL,
  `coupon_lang_id` bigint(20) NOT NULL,
  PRIMARY KEY (`coupon_id`),
  UNIQUE KEY `coupon_code` (`coupon_code`,`site_id`),
  KEY `FKAF42D826BA76F9D1` (`coupon_curr_id`),
  KEY `FKAF42D826F1B767DC` (`coupon_lang_id`),
  KEY `FKAF42D82670EBDE65` (`site_id`),
  CONSTRAINT `FKAF42D82670EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FKAF42D826BA76F9D1` FOREIGN KEY (`coupon_curr_id`) REFERENCES `coupon_currency` (`coupon_curr_id`),
  CONSTRAINT `FKAF42D826F1B767DC` FOREIGN KEY (`coupon_lang_id`) REFERENCES `coupon_language` (`coupon_lang_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.coupon: ~0 rows (approximately)
/*!40000 ALTER TABLE `coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupon` ENABLE KEYS */;


# Dumping structure for table jada.coupon_category
CREATE TABLE IF NOT EXISTS `coupon_category` (
  `coupon_id` bigint(20) NOT NULL,
  `cat_id` bigint(20) NOT NULL,
  PRIMARY KEY (`coupon_id`,`cat_id`),
  KEY `FK9DBEE0F73E04E745` (`coupon_id`),
  KEY `FK9DBEE0F730F21FAD` (`cat_id`),
  CONSTRAINT `FK9DBEE0F730F21FAD` FOREIGN KEY (`cat_id`) REFERENCES `category` (`cat_id`),
  CONSTRAINT `FK9DBEE0F73E04E745` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.coupon_category: ~0 rows (approximately)
/*!40000 ALTER TABLE `coupon_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupon_category` ENABLE KEYS */;


# Dumping structure for table jada.coupon_currency
CREATE TABLE IF NOT EXISTS `coupon_currency` (
  `coupon_curr_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `couponDiscountAmount` float DEFAULT NULL,
  `couponOrderAmount` float DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `coupon_id` bigint(20) DEFAULT NULL,
  `site_currency_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`coupon_curr_id`),
  KEY `FKBD08140A3E04E745` (`coupon_id`),
  KEY `FKBD08140A96324A6D` (`site_currency_class_id`),
  CONSTRAINT `FKBD08140A96324A6D` FOREIGN KEY (`site_currency_class_id`) REFERENCES `site_currency_class` (`site_currency_class_id`),
  CONSTRAINT `FKBD08140A3E04E745` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.coupon_currency: ~0 rows (approximately)
/*!40000 ALTER TABLE `coupon_currency` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupon_currency` ENABLE KEYS */;


# Dumping structure for table jada.coupon_item
CREATE TABLE IF NOT EXISTS `coupon_item` (
  `coupon_id` bigint(20) NOT NULL,
  `item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`coupon_id`,`item_id`),
  KEY `FK6708838C3E04E745` (`coupon_id`),
  KEY `FK6708838C71DEBAE5` (`item_id`),
  CONSTRAINT `FK6708838C71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`),
  CONSTRAINT `FK6708838C3E04E745` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.coupon_item: ~0 rows (approximately)
/*!40000 ALTER TABLE `coupon_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupon_item` ENABLE KEYS */;


# Dumping structure for table jada.coupon_language
CREATE TABLE IF NOT EXISTS `coupon_language` (
  `coupon_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coupon_name` varchar(50) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `coupon_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`coupon_lang_id`),
  KEY `FK3A8EB7513E04E745` (`coupon_id`),
  KEY `FK3A8EB751473E64D1` (`site_profile_class_id`),
  CONSTRAINT `FK3A8EB751473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FK3A8EB7513E04E745` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.coupon_language: ~0 rows (approximately)
/*!40000 ALTER TABLE `coupon_language` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupon_language` ENABLE KEYS */;


# Dumping structure for table jada.credit_card
CREATE TABLE IF NOT EXISTS `credit_card` (
  `creditcard_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creditcard_desc` varchar(40) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  PRIMARY KEY (`creditcard_id`),
  UNIQUE KEY `creditcard_desc` (`creditcard_desc`,`site_id`),
  KEY `FKEDE47C9670EBDE65` (`site_id`),
  CONSTRAINT `FKEDE47C9670EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

# Dumping data for table jada.credit_card: ~8 rows (approximately)
/*!40000 ALTER TABLE `credit_card` DISABLE KEYS */;
INSERT INTO `credit_card` (`creditcard_id`, `creditcard_desc`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `seq_num`, `site_id`) VALUES
	(1, 'American Express', 'system', '2009-08-16 08:45:07', 'system', '2009-08-16 08:44:59', 1, '_system'),
	(2, 'Discover Card', 'system', '2009-08-16 08:45:07', 'system', '2009-08-16 08:44:59', 1, '_system'),
	(3, 'Master Card', 'system', '2009-08-16 08:45:07', 'system', '2009-08-16 08:44:59', 1, '_system'),
	(4, 'Visa', 'system', '2009-08-16 08:45:07', 'system', '2009-08-16 08:44:59', 1, '_system'),
	(5, 'American Express', 'system', '2009-08-16 08:45:07', 'system', '2009-08-16 08:44:59', 1, 'default'),
	(6, 'Discover Card', 'system', '2009-08-16 08:45:07', 'system', '2009-08-16 08:44:59', 1, 'default'),
	(7, 'Master Card', 'system', '2009-08-16 08:45:07', 'system', '2009-08-16 08:44:59', 1, 'default'),
	(8, 'Visa', 'system', '2009-08-16 08:45:07', 'system', '2009-08-16 08:44:59', 1, 'default');
/*!40000 ALTER TABLE `credit_card` ENABLE KEYS */;


# Dumping structure for table jada.credit_detail
CREATE TABLE IF NOT EXISTS `credit_detail` (
  `credit_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_credit_amount` float NOT NULL,
  `item_credit_qty` int(11) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `credit_header_id` bigint(20) DEFAULT NULL,
  `order_item_detail_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`credit_detail_id`),
  KEY `FK8A5D05762F25038` (`credit_header_id`),
  KEY `FK8A5D05747EC5AF5` (`order_item_detail_id`),
  CONSTRAINT `FK8A5D05747EC5AF5` FOREIGN KEY (`order_item_detail_id`) REFERENCES `order_item_detail` (`order_item_detail_id`),
  CONSTRAINT `FK8A5D05762F25038` FOREIGN KEY (`credit_header_id`) REFERENCES `credit_header` (`credit_header_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.credit_detail: ~0 rows (approximately)
/*!40000 ALTER TABLE `credit_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `credit_detail` ENABLE KEYS */;


# Dumping structure for table jada.credit_detail_tax
CREATE TABLE IF NOT EXISTS `credit_detail_tax` (
  `credit_detail_tax_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `tax_amount` float NOT NULL,
  `tax_name` varchar(40) NOT NULL,
  `credit_detail_id` bigint(20) DEFAULT NULL,
  `credit_header_id` bigint(20) DEFAULT NULL,
  `tax_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`credit_detail_tax_id`),
  KEY `FKA634BBC3F34349B8` (`credit_detail_id`),
  KEY `FKA634BBC362F25038` (`credit_header_id`),
  KEY `FKA634BBC3C2D9DA6F` (`tax_id`),
  CONSTRAINT `FKA634BBC3C2D9DA6F` FOREIGN KEY (`tax_id`) REFERENCES `tax` (`tax_id`),
  CONSTRAINT `FKA634BBC362F25038` FOREIGN KEY (`credit_header_id`) REFERENCES `credit_header` (`credit_header_id`),
  CONSTRAINT `FKA634BBC3F34349B8` FOREIGN KEY (`credit_detail_id`) REFERENCES `credit_detail` (`credit_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.credit_detail_tax: ~0 rows (approximately)
/*!40000 ALTER TABLE `credit_detail_tax` DISABLE KEYS */;
/*!40000 ALTER TABLE `credit_detail_tax` ENABLE KEYS */;


# Dumping structure for table jada.credit_header
CREATE TABLE IF NOT EXISTS `credit_header` (
  `credit_header_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `credit_date` datetime NOT NULL,
  `credit_num` varchar(20) NOT NULL,
  `credit_status` varchar(1) NOT NULL,
  `credit_total` float NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `shipping_total` float NOT NULL,
  `update_inventory` varchar(1) NOT NULL,
  `order_header_id` bigint(20) DEFAULT NULL,
  `payment_tran_id` bigint(20) DEFAULT NULL,
  `void_payment_tran_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`credit_header_id`),
  KEY `FKF709A93B7B46AB1` (`void_payment_tran_id`),
  KEY `FKF709A93267D6B6C` (`order_header_id`),
  KEY `FKF709A93EEE96B5C` (`payment_tran_id`),
  CONSTRAINT `FKF709A93EEE96B5C` FOREIGN KEY (`payment_tran_id`) REFERENCES `payment_tran` (`payment_tran_id`),
  CONSTRAINT `FKF709A93267D6B6C` FOREIGN KEY (`order_header_id`) REFERENCES `order_header` (`order_header_id`),
  CONSTRAINT `FKF709A93B7B46AB1` FOREIGN KEY (`void_payment_tran_id`) REFERENCES `payment_tran` (`payment_tran_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.credit_header: ~0 rows (approximately)
/*!40000 ALTER TABLE `credit_header` DISABLE KEYS */;
/*!40000 ALTER TABLE `credit_header` ENABLE KEYS */;


# Dumping structure for table jada.currency
CREATE TABLE IF NOT EXISTS `currency` (
  `currency_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `currency_code` varchar(3) NOT NULL,
  `currency_name` varchar(80) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  PRIMARY KEY (`currency_id`),
  UNIQUE KEY `currency_code` (`currency_code`,`site_id`),
  KEY `FK224BF01170EBDE65` (`site_id`),
  CONSTRAINT `FK224BF01170EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=357 DEFAULT CHARSET=utf8;

# Dumping data for table jada.currency: ~259 rows (approximately)
/*!40000 ALTER TABLE `currency` DISABLE KEYS */;
INSERT INTO `currency` (`currency_id`, `currency_code`, `currency_name`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `site_id`) VALUES
	(1, 'AED', 'United Arab Emirates dirham', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(2, 'AFN', 'Afghani', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(3, 'ALL', 'Lek', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(4, 'AMD', 'Armenian dram', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(5, 'ANG', 'Netherlands Antillean guilder', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(6, 'AOA', 'Kwanza', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(7, 'ARS', 'Argentine peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(8, 'AUD', 'Australian dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(9, 'AWG', 'Aruban guilder', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(10, 'AZN', 'Azerbaijanian manat', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(11, 'BAM', 'Convertible marks', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(12, 'BBD', 'Barbados dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(13, 'BDT', 'Bangladeshi taka', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(14, 'BGN', 'Bulgarian lev', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(15, 'BHD', 'Bahraini dinar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(16, 'BIF', 'Burundian franc', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(17, 'BMD', 'Bermudian dollar (customarily known as Bermuda dollar)', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(18, 'BND', 'Brunei dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(19, 'BOB', 'Boliviano', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(20, 'BOV', 'Bolivian Mvdol (funds code)', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(21, 'BRL', 'Brazilian real', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(22, 'BSD', 'Bahamian dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(23, 'BTN', 'Ngultrum', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(24, 'BWP', 'Pula', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(25, 'BYR', 'Belarussian ruble', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(26, 'BZD', 'Belize dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(27, 'CAD', 'Canadian dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(28, 'CDF', 'Franc Congolais', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(29, 'CHE', 'WIR euro (complementary currency)', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(30, 'CHF', 'Swiss franc', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(31, 'CHW', 'WIR franc (complementary currency)', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(32, 'CLF', 'Unidad de Fomento (funds code)', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(33, 'CLP', 'Chilean peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(34, 'CNY', 'Renminbi', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(35, 'COP', 'Colombian peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(36, 'COU', 'Unidad de Valor Real', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(37, 'CRC', 'Costa Rican colon', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(38, 'CUC', 'Cuban convertible peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(39, 'CUP', 'Cuban peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(40, 'CVE', 'Cape Verde escudo', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(41, 'CZK', 'Czech Koruna', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(42, 'DJF', 'Djibouti franc', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(43, 'DKK', 'Danish krone', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(44, 'DOP', 'Dominican peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(45, 'DZD', 'Algerian dinar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(46, 'EEK', 'Kroon', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(47, 'EGP', 'Egyptian pound', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(48, 'ERN', 'Nakfa', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(49, 'ETB', 'Ethiopian birr', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(50, 'EUR', 'Euro', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(51, 'FJD', 'Fiji dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(52, 'FKP', 'Falkland Islands pound', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(53, 'GBP', 'Pound sterling', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(54, 'GEL', 'Lari', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(55, 'GHS', 'Cedi', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(56, 'GIP', 'Gibraltar pound', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(57, 'GMD', 'Dalasi', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(58, 'GNF', 'Guinea franc', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(59, 'GTQ', 'Quetzal', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(60, 'GYD', 'Guyana dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(61, 'HKD', 'Hong Kong dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(62, 'HNL', 'Lempira', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(63, 'HRK', 'Croatian kuna', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(64, 'HTG', 'Haiti gourde', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(65, 'HUF', 'Forint', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(66, 'IDR', 'Rupiah', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(67, 'ILS', 'Israeli new sheqel', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(68, 'INR', 'Indian rupee', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(69, 'IQD', 'Iraqi dinar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(70, 'IRR', 'Iranian rial', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(71, 'ISK', 'Iceland krona', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(72, 'JMD', 'Jamaican dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(73, 'JOD', 'Jordanian dinar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(74, 'JPY', 'Japanese yen', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(75, 'KES', 'Kenyan shilling', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(76, 'KGS', 'Som', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(77, 'KHR', 'Riel', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(78, 'KMF', 'Comoro franc', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(79, 'KPW', 'North Korean won', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(80, 'KRW', 'South Korean won', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(81, 'KWD', 'Kuwaiti dinar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(82, 'KYD', 'Cayman Islands dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(83, 'KZT', 'Tenge', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(84, 'LAK', 'Kip', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(85, 'LBP', 'Lebanese pound', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(86, 'LKR', 'Sri Lanka rupee', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(87, 'LRD', 'Liberian dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(88, 'LSL', 'Lesotho loti', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(89, 'LTL', 'Lithuanian litas', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(90, 'LVL', 'Latvian lats', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', '_system'),
	(91, 'LYD', 'Libyan dinar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(92, 'MAD', 'Moroccan dirham', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(93, 'MDL', 'Moldovan leu', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(94, 'MGA', 'Malagasy ariary', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(95, 'MKD', 'Denar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(96, 'MMK', 'Kyat', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(97, 'MNT', 'Tugrik', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(98, 'MOP', 'Pataca', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(99, 'MRO', 'Ouguiya', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(100, 'MUR', 'Mauritius rupee', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(101, 'MVR', 'Rufiyaa', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(102, 'MWK', 'Kwacha', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(103, 'MXN', 'Mexican peso', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(104, 'MXV', 'Mexican Unidad de Inversion (UDI) (funds code)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(105, 'MYR', 'Malaysian ringgit', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(106, 'MZN', 'Metical', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(107, 'NAD', 'Namibian dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(108, 'NGN', 'Naira', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(109, 'NIO', 'Cordoba oro', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(110, 'NOK', 'Norwegian krone', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(111, 'NPR', 'Nepalese rupee', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(112, 'NZD', 'New Zealand dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(113, 'OMR', 'Rial Omani', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(114, 'PAB', 'Balboa', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(115, 'PEN', 'Nuevo sol', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(116, 'PGK', 'Kina', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(117, 'PHP', 'Philippine peso', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(118, 'PKR', 'Pakistan rupee', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(119, 'PLN', 'Z?oty', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(120, 'PYG', 'Guarani', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(121, 'QAR', 'Qatari rial', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(122, 'RON', 'Romanian new leu', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(123, 'RSD', 'Serbian dinar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(124, 'RUB', 'Russian rouble', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(125, 'RWF', 'Rwanda franc', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(126, 'SAR', 'Saudi riyal', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(127, 'SBD', 'Solomon Islands dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(128, 'SCR', 'Seychelles rupee', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(129, 'SDG', 'Sudanese pound', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(130, 'SEK', 'Swedish krona/kronor', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(131, 'SGD', 'Singapore dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(132, 'SHP', 'Saint Helena pound', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(133, 'SLL', 'Leone', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(134, 'SOS', 'Somali shilling', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(135, 'SRD', 'Surinam dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(136, 'STD', 'Dobra', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(137, 'SYP', 'Syrian pound', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(138, 'SZL', 'Lilangeni', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(139, 'THB', 'Baht', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(140, 'TJS', 'Somoni', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(141, 'TMT', 'Manat', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(142, 'TND', 'Tunisian dinar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(143, 'TOP', 'Pa\'anga', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(144, 'TRY', 'Turkish lira', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(145, 'TTD', 'Trinidad and Tobago dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(146, 'TWD', 'New Taiwan dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(147, 'TZS', 'Tanzanian shilling', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(148, 'UAH', 'Hryvnia', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(149, 'UGX', 'Uganda shilling', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(150, 'USD', 'US dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(151, 'USN', 'United States dollar (next day) (funds code)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(152, 'USS', 'United States dollar (same day) (funds code)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(153, 'UYU', 'Peso Uruguayo', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(154, 'UZS', 'Uzbekistan som', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(155, 'VEF', 'Venezuelan bolÃ­var fuerte', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(156, 'VND', 'Vietnamese ??ng', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(157, 'VUV', 'Vatu', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(158, 'WST', 'Samoan tala', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(159, 'XAF', 'CFA franc BEAC', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(160, 'XAG', 'Silver (one troy ounce)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(161, 'XAU', 'Gold (one troy ounce)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(162, 'XBA', 'European Composite Unit (EURCO) (bond market unit)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(163, 'XBB', 'European Monetary Unit (E.M.U.-6) (bond market unit)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(164, 'XBC', 'European Unit of Account 9 (E.U.A.-9) (bond market unit)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(165, 'XBD', 'European Unit of Account 17 (E.U.A.-17) (bond market unit)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(166, 'XCD', 'East Caribbean dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(167, 'XDR', 'Special Drawing Rights', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(168, 'XFU', 'UIC franc (special settlement currency)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(169, 'XOF', 'CFA Franc BCEAO', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(170, 'XPD', 'Palladium (one troy ounce)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(171, 'XPF', 'CFP franc', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(172, 'XPT', 'Platinum (one troy ounce)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(173, 'XTS', 'Code reserved for testing purposes', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(174, 'XXX', 'No currency', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(175, 'YER', 'Yemeni rial', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(176, 'ZAR', 'South African rand', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(177, 'ZMK', 'Kwacha', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(178, 'ZWL', 'Zimbabwe dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', '_system'),
	(179, 'AED', 'United Arab Emirates dirham', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(180, 'AFN', 'Afghani', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(181, 'ALL', 'Lek', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(182, 'AMD', 'Armenian dram', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(183, 'ANG', 'Netherlands Antillean guilder', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(184, 'AOA', 'Kwanza', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(185, 'ARS', 'Argentine peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(186, 'AUD', 'Australian dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(187, 'AWG', 'Aruban guilder', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(188, 'AZN', 'Azerbaijanian manat', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(189, 'BAM', 'Convertible marks', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(190, 'BBD', 'Barbados dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(191, 'BDT', 'Bangladeshi taka', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(192, 'BGN', 'Bulgarian lev', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(193, 'BHD', 'Bahraini dinar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(194, 'BIF', 'Burundian franc', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(195, 'BMD', 'Bermudian dollar (customarily known as Bermuda dollar)', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(196, 'BND', 'Brunei dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(197, 'BOB', 'Boliviano', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(198, 'BOV', 'Bolivian Mvdol (funds code)', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(199, 'BRL', 'Brazilian real', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(200, 'BSD', 'Bahamian dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(201, 'BTN', 'Ngultrum', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(202, 'BWP', 'Pula', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(203, 'BYR', 'Belarussian ruble', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(204, 'BZD', 'Belize dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(205, 'CAD', 'Canadian dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(206, 'CDF', 'Franc Congolais', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(207, 'CHE', 'WIR euro (complementary currency)', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(208, 'CHF', 'Swiss franc', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(209, 'CHW', 'WIR franc (complementary currency)', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(210, 'CLF', 'Unidad de Fomento (funds code)', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(211, 'CLP', 'Chilean peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(212, 'CNY', 'Renminbi', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(213, 'COP', 'Colombian peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(214, 'COU', 'Unidad de Valor Real', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(215, 'CRC', 'Costa Rican colon', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(216, 'CUC', 'Cuban convertible peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(217, 'CUP', 'Cuban peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(218, 'CVE', 'Cape Verde escudo', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(219, 'CZK', 'Czech Koruna', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(220, 'DJF', 'Djibouti franc', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(221, 'DKK', 'Danish krone', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(222, 'DOP', 'Dominican peso', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(223, 'DZD', 'Algerian dinar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(224, 'EEK', 'Kroon', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(225, 'EGP', 'Egyptian pound', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(226, 'ERN', 'Nakfa', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(227, 'ETB', 'Ethiopian birr', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(228, 'EUR', 'Euro', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(229, 'FJD', 'Fiji dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(230, 'FKP', 'Falkland Islands pound', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(231, 'GBP', 'Pound sterling', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(232, 'GEL', 'Lari', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(233, 'GHS', 'Cedi', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(234, 'GIP', 'Gibraltar pound', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(235, 'GMD', 'Dalasi', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(236, 'GNF', 'Guinea franc', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(237, 'GTQ', 'Quetzal', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(238, 'GYD', 'Guyana dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(239, 'HKD', 'Hong Kong dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(240, 'HNL', 'Lempira', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(241, 'HRK', 'Croatian kuna', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(242, 'HTG', 'Haiti gourde', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(243, 'HUF', 'Forint', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(244, 'IDR', 'Rupiah', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(245, 'ILS', 'Israeli new sheqel', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(246, 'INR', 'Indian rupee', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(247, 'IQD', 'Iraqi dinar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(248, 'IRR', 'Iranian rial', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(249, 'ISK', 'Iceland krona', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(250, 'JMD', 'Jamaican dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(251, 'JOD', 'Jordanian dinar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(252, 'JPY', 'Japanese yen', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(253, 'KES', 'Kenyan shilling', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(254, 'KGS', 'Som', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(255, 'KHR', 'Riel', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(256, 'KMF', 'Comoro franc', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(257, 'KPW', 'North Korean won', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(258, 'KRW', 'South Korean won', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(259, 'KWD', 'Kuwaiti dinar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(260, 'KYD', 'Cayman Islands dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(261, 'KZT', 'Tenge', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(262, 'LAK', 'Kip', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(263, 'LBP', 'Lebanese pound', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(264, 'LKR', 'Sri Lanka rupee', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(265, 'LRD', 'Liberian dollar', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(266, 'LSL', 'Lesotho loti', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(267, 'LTL', 'Lithuanian litas', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(268, 'LVL', 'Latvian lats', 'system', '2009-06-10 22:05:13', 'system', '2009-06-10 22:05:13', 'default'),
	(269, 'LYD', 'Libyan dinar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(270, 'MAD', 'Moroccan dirham', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(271, 'MDL', 'Moldovan leu', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(272, 'MGA', 'Malagasy ariary', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(273, 'MKD', 'Denar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(274, 'MMK', 'Kyat', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(275, 'MNT', 'Tugrik', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(276, 'MOP', 'Pataca', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(277, 'MRO', 'Ouguiya', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(278, 'MUR', 'Mauritius rupee', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(279, 'MVR', 'Rufiyaa', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(280, 'MWK', 'Kwacha', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(281, 'MXN', 'Mexican peso', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(282, 'MXV', 'Mexican Unidad de Inversion (UDI) (funds code)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(283, 'MYR', 'Malaysian ringgit', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(284, 'MZN', 'Metical', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(285, 'NAD', 'Namibian dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(286, 'NGN', 'Naira', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(287, 'NIO', 'Cordoba oro', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(288, 'NOK', 'Norwegian krone', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(289, 'NPR', 'Nepalese rupee', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(290, 'NZD', 'New Zealand dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(291, 'OMR', 'Rial Omani', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(292, 'PAB', 'Balboa', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(293, 'PEN', 'Nuevo sol', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(294, 'PGK', 'Kina', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(295, 'PHP', 'Philippine peso', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(296, 'PKR', 'Pakistan rupee', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(297, 'PLN', 'Z?oty', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(298, 'PYG', 'Guarani', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(299, 'QAR', 'Qatari rial', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(300, 'RON', 'Romanian new leu', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(301, 'RSD', 'Serbian dinar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(302, 'RUB', 'Russian rouble', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(303, 'RWF', 'Rwanda franc', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(304, 'SAR', 'Saudi riyal', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(305, 'SBD', 'Solomon Islands dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(306, 'SCR', 'Seychelles rupee', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(307, 'SDG', 'Sudanese pound', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(308, 'SEK', 'Swedish krona/kronor', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(309, 'SGD', 'Singapore dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(310, 'SHP', 'Saint Helena pound', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(311, 'SLL', 'Leone', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(312, 'SOS', 'Somali shilling', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(313, 'SRD', 'Surinam dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(314, 'STD', 'Dobra', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(315, 'SYP', 'Syrian pound', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(316, 'SZL', 'Lilangeni', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(317, 'THB', 'Baht', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(318, 'TJS', 'Somoni', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(319, 'TMT', 'Manat', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(320, 'TND', 'Tunisian dinar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(321, 'TOP', 'Pa\'anga', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(322, 'TRY', 'Turkish lira', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(323, 'TTD', 'Trinidad and Tobago dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(324, 'TWD', 'New Taiwan dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(325, 'TZS', 'Tanzanian shilling', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(326, 'UAH', 'Hryvnia', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(327, 'UGX', 'Uganda shilling', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(328, 'USD', 'US dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(329, 'USN', 'United States dollar (next day) (funds code)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(330, 'USS', 'United States dollar (same day) (funds code)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(331, 'UYU', 'Peso Uruguayo', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(332, 'UZS', 'Uzbekistan som', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(333, 'VEF', 'Venezuelan bolÃ­var fuerte', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(334, 'VND', 'Vietnamese ??ng', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(335, 'VUV', 'Vatu', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(336, 'WST', 'Samoan tala', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(337, 'XAF', 'CFA franc BEAC', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(338, 'XAG', 'Silver (one troy ounce)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(339, 'XAU', 'Gold (one troy ounce)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(340, 'XBA', 'European Composite Unit (EURCO) (bond market unit)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(341, 'XBB', 'European Monetary Unit (E.M.U.-6) (bond market unit)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(342, 'XBC', 'European Unit of Account 9 (E.U.A.-9) (bond market unit)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(343, 'XBD', 'European Unit of Account 17 (E.U.A.-17) (bond market unit)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(344, 'XCD', 'East Caribbean dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(345, 'XDR', 'Special Drawing Rights', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(346, 'XFU', 'UIC franc (special settlement currency)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(347, 'XOF', 'CFA Franc BCEAO', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(348, 'XPD', 'Palladium (one troy ounce)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(349, 'XPF', 'CFP franc', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(350, 'XPT', 'Platinum (one troy ounce)', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(351, 'XTS', 'Code reserved for testing purposes', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(352, 'XXX', 'No currency', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(353, 'YER', 'Yemeni rial', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(354, 'ZAR', 'South African rand', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(355, 'ZMK', 'Kwacha', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default'),
	(356, 'ZWL', 'Zimbabwe dollar', 'system', '2009-06-10 22:05:14', 'system', '2009-06-10 22:05:14', 'default');
/*!40000 ALTER TABLE `currency` ENABLE KEYS */;


# Dumping structure for table jada.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `cust_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` char(1) NOT NULL,
  `cust_comments` longtext,
  `cust_email` varchar(255) NOT NULL,
  `cust_password` varchar(128) DEFAULT NULL,
  `cust_public_name` varchar(20) NOT NULL,
  `cust_source` varchar(10) NOT NULL,
  `cust_source_ref` varchar(20) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `cust_address_id` bigint(20) NOT NULL,
  `cust_class_id` bigint(20) DEFAULT NULL,
  `site_domain_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cust_id`),
  KEY `FK24217FDE5A4B183D` (`cust_class_id`),
  KEY `FK24217FDE70EBDE65` (`site_id`),
  KEY `FK24217FDEB8784434` (`site_domain_id`),
  KEY `FK24217FDEDDDDE1FD` (`cust_address_id`),
  CONSTRAINT `FK24217FDEDDDDE1FD` FOREIGN KEY (`cust_address_id`) REFERENCES `customer_address` (`cust_address_id`),
  CONSTRAINT `FK24217FDE5A4B183D` FOREIGN KEY (`cust_class_id`) REFERENCES `customer_class` (`cust_class_id`),
  CONSTRAINT `FK24217FDE70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK24217FDEB8784434` FOREIGN KEY (`site_domain_id`) REFERENCES `site_domain` (`site_domain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.customer: ~0 rows (approximately)
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;


# Dumping structure for table jada.customer_address
CREATE TABLE IF NOT EXISTS `customer_address` (
  `cust_address_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cust_address_line1` varchar(30) DEFAULT NULL,
  `cust_address_line2` varchar(30) DEFAULT NULL,
  `cust_address_type` varchar(1) DEFAULT NULL,
  `cust_city_name` varchar(25) DEFAULT NULL,
  `cust_country_code` varchar(2) DEFAULT NULL,
  `cust_country_name` varchar(40) DEFAULT NULL,
  `cust_fax_num` varchar(15) DEFAULT NULL,
  `cust_first_name` varchar(40) DEFAULT NULL,
  `cust_last_name` varchar(40) DEFAULT NULL,
  `cust_middle_name` varchar(40) DEFAULT NULL,
  `cust_phone_num` varchar(15) DEFAULT NULL,
  `cust_prefix` varchar(20) DEFAULT NULL,
  `cust_state_code` varchar(2) DEFAULT NULL,
  `cust_state_name` varchar(40) DEFAULT NULL,
  `cust_suffix` varchar(20) DEFAULT NULL,
  `cust_use_address` varchar(1) DEFAULT NULL,
  `cust_zip_code` varchar(10) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `country_id` bigint(20) DEFAULT NULL,
  `state_id` bigint(20) DEFAULT NULL,
  `cust_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`cust_address_id`),
  KEY `FK41B20493D2E3176F` (`state_id`),
  KEY `FK41B204932196FC0F` (`country_id`),
  KEY `FK41B20493E6CEAEF0` (`cust_id`),
  CONSTRAINT `FK41B20493E6CEAEF0` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`cust_id`),
  CONSTRAINT `FK41B204932196FC0F` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`),
  CONSTRAINT `FK41B20493D2E3176F` FOREIGN KEY (`state_id`) REFERENCES `state` (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.customer_address: ~0 rows (approximately)
/*!40000 ALTER TABLE `customer_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_address` ENABLE KEYS */;


# Dumping structure for table jada.customer_class
CREATE TABLE IF NOT EXISTS `customer_class` (
  `cust_class_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cust_class_name` varchar(20) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `system_record` char(1) NOT NULL,
  PRIMARY KEY (`cust_class_id`),
  UNIQUE KEY `cust_class_name` (`cust_class_name`,`site_id`),
  KEY `FK862FADD770EBDE65` (`site_id`),
  CONSTRAINT `FK862FADD770EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.customer_class: ~0 rows (approximately)
/*!40000 ALTER TABLE `customer_class` DISABLE KEYS */;
INSERT INTO `customer_class` (`cust_class_id`, `cust_class_name`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `site_id`, `system_record`) VALUES
	(1, 'Regular', 'system', '2011-02-28 09:25:22', 'system', '2011-02-28 09:25:29', '_system', 'Y'),
	(2, 'Regular', 'system', '2011-02-28 09:25:53', 'system', '2011-02-28 09:25:57', 'default', 'Y');
/*!40000 ALTER TABLE `customer_class` ENABLE KEYS */;


# Dumping structure for table jada.customer_credit_card
CREATE TABLE IF NOT EXISTS `customer_credit_card` (
  `cust_creditcard_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cust_creditcard_expiry_month` varchar(2) NOT NULL,
  `cust_creditcard_expiry_year` varchar(4) NOT NULL,
  `cust_creditcard_full_name` varchar(40) NOT NULL,
  `cust_creditcard_num` varchar(192) NOT NULL,
  `cust_creditcard_ver_num` varchar(40) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `credit_card_id` bigint(20) DEFAULT NULL,
  `cust_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`cust_creditcard_id`),
  KEY `FK994B7DB51B87ACD8` (`credit_card_id`),
  KEY `FK994B7DB5E6CEAEF0` (`cust_id`),
  CONSTRAINT `FK994B7DB5E6CEAEF0` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`cust_id`),
  CONSTRAINT `FK994B7DB51B87ACD8` FOREIGN KEY (`credit_card_id`) REFERENCES `credit_card` (`creditcard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.customer_credit_card: ~0 rows (approximately)
/*!40000 ALTER TABLE `customer_credit_card` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_credit_card` ENABLE KEYS */;


# Dumping structure for table jada.custom_attrib
CREATE TABLE IF NOT EXISTS `custom_attrib` (
  `custom_attrib_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `custom_attrib_data_type_code` char(1) NOT NULL,
  `custom_attrib_name` varchar(40) DEFAULT NULL,
  `custom_attrib_type_code` char(1) NOT NULL,
  `item_compare` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `system_record` char(1) NOT NULL,
  `custom_attrib_lang_id` bigint(20) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  PRIMARY KEY (`custom_attrib_id`),
  KEY `FK26FB0F5832CF69DD` (`custom_attrib_lang_id`),
  KEY `FK26FB0F5870EBDE65` (`site_id`),
  CONSTRAINT `FK26FB0F5870EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK26FB0F5832CF69DD` FOREIGN KEY (`custom_attrib_lang_id`) REFERENCES `custom_attrib_language` (`custom_attrib_lang_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table jada.custom_attrib: ~1 rows (approximately)
/*!40000 ALTER TABLE `custom_attrib` DISABLE KEYS */;
INSERT INTO `custom_attrib` (`custom_attrib_id`, `custom_attrib_data_type_code`, `custom_attrib_name`, `custom_attrib_type_code`, `item_compare`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `system_record`, `custom_attrib_lang_id`, `site_id`) VALUES
	(1, '4', 'Price', '2', 'Y', 'admin', '2010-02-20 11:36:44', 'admin', '2010-02-20 17:32:50', 'Y', 1, 'default');
/*!40000 ALTER TABLE `custom_attrib` ENABLE KEYS */;


# Dumping structure for table jada.custom_attrib_detail
CREATE TABLE IF NOT EXISTS `custom_attrib_detail` (
  `custom_attrib_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `custom_attrib_id` bigint(20) DEFAULT NULL,
  `custom_attrib_group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`custom_attrib_detail_id`),
  KEY `FK51879318BA900701` (`custom_attrib_group_id`),
  KEY `FK5187931883C266A2` (`custom_attrib_id`),
  CONSTRAINT `FK5187931883C266A2` FOREIGN KEY (`custom_attrib_id`) REFERENCES `custom_attrib` (`custom_attrib_id`),
  CONSTRAINT `FK51879318BA900701` FOREIGN KEY (`custom_attrib_group_id`) REFERENCES `custom_attrib_group` (`custom_attrib_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.custom_attrib_detail: ~0 rows (approximately)
/*!40000 ALTER TABLE `custom_attrib_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_attrib_detail` ENABLE KEYS */;


# Dumping structure for table jada.custom_attrib_group
CREATE TABLE IF NOT EXISTS `custom_attrib_group` (
  `custom_attrib_group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `custom_attrib_group_name` varchar(40) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  PRIMARY KEY (`custom_attrib_group_id`),
  UNIQUE KEY `custom_attrib_group_name` (`custom_attrib_group_name`,`site_id`),
  KEY `FK135586D870EBDE65` (`site_id`),
  CONSTRAINT `FK135586D870EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.custom_attrib_group: ~0 rows (approximately)
/*!40000 ALTER TABLE `custom_attrib_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_attrib_group` ENABLE KEYS */;


# Dumping structure for table jada.custom_attrib_language
CREATE TABLE IF NOT EXISTS `custom_attrib_language` (
  `custom_attrib_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `custom_attrib_desc` varchar(40) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `custom_attrib_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`custom_attrib_lang_id`),
  KEY `FK6F7C785F83C266A2` (`custom_attrib_id`),
  KEY `FK6F7C785F473E64D1` (`site_profile_class_id`),
  CONSTRAINT `FK6F7C785F473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FK6F7C785F83C266A2` FOREIGN KEY (`custom_attrib_id`) REFERENCES `custom_attrib` (`custom_attrib_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table jada.custom_attrib_language: ~1 rows (approximately)
/*!40000 ALTER TABLE `custom_attrib_language` DISABLE KEYS */;
INSERT INTO `custom_attrib_language` (`custom_attrib_lang_id`, `custom_attrib_desc`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `custom_attrib_id`, `site_profile_class_id`) VALUES
	(1, 'Price', 'admin', '2010-02-20 11:37:46', 'admin', '2010-02-20 17:32:50', 1, 2);
/*!40000 ALTER TABLE `custom_attrib_language` ENABLE KEYS */;


# Dumping structure for table jada.custom_attrib_option
CREATE TABLE IF NOT EXISTS `custom_attrib_option` (
  `custom_attrib_option_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `custom_attrib_sku_code` varchar(3) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `custom_attrib_id` bigint(20) NOT NULL,
  `custom_attrib_option_curr_id` bigint(20) DEFAULT NULL,
  `custom_attrib_option_lang_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`custom_attrib_option_id`),
  KEY `FK64E8033CD3846B4B` (`custom_attrib_option_curr_id`),
  KEY `FK64E8033C83C266A2` (`custom_attrib_id`),
  KEY `FK64E8033CAC4D956` (`custom_attrib_option_lang_id`),
  CONSTRAINT `FK64E8033CAC4D956` FOREIGN KEY (`custom_attrib_option_lang_id`) REFERENCES `custom_attrib_option_language` (`custom_attrib_option_lang_id`),
  CONSTRAINT `FK64E8033C83C266A2` FOREIGN KEY (`custom_attrib_id`) REFERENCES `custom_attrib` (`custom_attrib_id`),
  CONSTRAINT `FK64E8033CD3846B4B` FOREIGN KEY (`custom_attrib_option_curr_id`) REFERENCES `custom_attrib_option_currency` (`custom_attrib_option_curr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.custom_attrib_option: ~0 rows (approximately)
/*!40000 ALTER TABLE `custom_attrib_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_attrib_option` ENABLE KEYS */;


# Dumping structure for table jada.custom_attrib_option_currency
CREATE TABLE IF NOT EXISTS `custom_attrib_option_currency` (
  `custom_attrib_option_curr_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `custom_attrib_value` varchar(40) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `custom_attrib_option_id` bigint(20) DEFAULT NULL,
  `site_currency_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`custom_attrib_option_curr_id`),
  KEY `FKE55301B46B5C3CD3` (`custom_attrib_option_id`),
  KEY `FKE55301B496324A6D` (`site_currency_class_id`),
  CONSTRAINT `FKE55301B496324A6D` FOREIGN KEY (`site_currency_class_id`) REFERENCES `site_currency_class` (`site_currency_class_id`),
  CONSTRAINT `FKE55301B46B5C3CD3` FOREIGN KEY (`custom_attrib_option_id`) REFERENCES `custom_attrib_option` (`custom_attrib_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.custom_attrib_option_currency: ~0 rows (approximately)
/*!40000 ALTER TABLE `custom_attrib_option_currency` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_attrib_option_currency` ENABLE KEYS */;


# Dumping structure for table jada.custom_attrib_option_language
CREATE TABLE IF NOT EXISTS `custom_attrib_option_language` (
  `custom_attrib_option_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `custom_attrib_value` varchar(40) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `custom_attrib_option_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`custom_attrib_option_lang_id`),
  KEY `FK62D9A4FB6B5C3CD3` (`custom_attrib_option_id`),
  KEY `FK62D9A4FB473E64D1` (`site_profile_class_id`),
  CONSTRAINT `FK62D9A4FB473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FK62D9A4FB6B5C3CD3` FOREIGN KEY (`custom_attrib_option_id`) REFERENCES `custom_attrib_option` (`custom_attrib_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.custom_attrib_option_language: ~0 rows (approximately)
/*!40000 ALTER TABLE `custom_attrib_option_language` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_attrib_option_language` ENABLE KEYS */;


# Dumping structure for table jada.home_page
CREATE TABLE IF NOT EXISTS `home_page` (
  `home_page_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `home_page_feature_id` bigint(20) DEFAULT NULL,
  `home_page_lang_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`home_page_id`),
  KEY `FK7E3F59EF75AC2DCD` (`home_page_lang_id`),
  KEY `FK7E3F59EF22F0859E` (`home_page_feature_id`),
  CONSTRAINT `FK7E3F59EF22F0859E` FOREIGN KEY (`home_page_feature_id`) REFERENCES `home_page_detail` (`home_page_detail_id`),
  CONSTRAINT `FK7E3F59EF75AC2DCD` FOREIGN KEY (`home_page_lang_id`) REFERENCES `home_page_language` (`home_page_lang_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table jada.home_page: ~1 rows (approximately)
/*!40000 ALTER TABLE `home_page` DISABLE KEYS */;
INSERT INTO `home_page` (`home_page_id`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `home_page_feature_id`, `home_page_lang_id`) VALUES
	(1, 'admin', '2009-08-10 20:44:09', 'admin', '2009-09-19 11:14:31', NULL, 1);
/*!40000 ALTER TABLE `home_page` ENABLE KEYS */;


# Dumping structure for table jada.home_page_detail
CREATE TABLE IF NOT EXISTS `home_page_detail` (
  `home_page_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feature_data` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `home_page_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`home_page_detail_id`),
  KEY `FK628D80A171DEBAE5` (`item_id`),
  KEY `FK628D80A16DA0AD2F` (`content_id`),
  KEY `FK628D80A1F0B9EA04` (`home_page_id`),
  CONSTRAINT `FK628D80A1F0B9EA04` FOREIGN KEY (`home_page_id`) REFERENCES `home_page` (`home_page_id`),
  CONSTRAINT `FK628D80A16DA0AD2F` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`),
  CONSTRAINT `FK628D80A171DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.home_page_detail: ~0 rows (approximately)
/*!40000 ALTER TABLE `home_page_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `home_page_detail` ENABLE KEYS */;


# Dumping structure for table jada.home_page_language
CREATE TABLE IF NOT EXISTS `home_page_language` (
  `home_page_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `home_page_title` varchar(255) DEFAULT NULL,
  `meta_keywords` varchar(1000) DEFAULT NULL,
  `meta_description` varchar(1000) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `home_page_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`home_page_lang_id`),
  KEY `FK56BD27A8473E64D1` (`site_profile_class_id`),
  KEY `FK56BD27A8F0B9EA04` (`home_page_id`),
  CONSTRAINT `FK56BD27A8473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FK56BD27A8F0B9EA04` FOREIGN KEY (`home_page_id`) REFERENCES `home_page` (`home_page_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table jada.home_page_language: ~1 rows (approximately)
/*!40000 ALTER TABLE `home_page_language` DISABLE KEYS */;
INSERT INTO `home_page_language` (`home_page_lang_id`, `home_page_title`, `meta_keywords`, `meta_description`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `home_page_id`, `site_profile_class_id`) VALUES
	(1, 'This is the home page for localhost', '', '', 'admin', '2009-08-10 20:42:47', 'admin', '2009-08-10 20:42:41', 1, 2);
/*!40000 ALTER TABLE `home_page_language` ENABLE KEYS */;

# Dumping structure for table jadaold.ie_profile_detail
CREATE TABLE IF NOT EXISTS `ie_profile_detail` (
  `ie_profile_detail_id` bigint(20) NOT NULL auto_increment,
  `ie_profile_field_name` varchar(80) NOT NULL,
  `ie_profile_field_value` longtext,
  `ie_profile_group_index` int(11) default NULL,
  `ie_profile_group_name` varchar(80) default NULL,
  `ie_profile_position` int(11) default NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `ie_profile_header_id` bigint(20) default NULL,
  PRIMARY KEY  (`ie_profile_detail_id`),
  KEY `FK29DB112AACD8EF83` (`ie_profile_header_id`),
  CONSTRAINT `FK29DB112AACD8EF83` FOREIGN KEY (`ie_profile_header_id`) REFERENCES `ie_profile_header` (`ie_profile_header_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7767 DEFAULT CHARSET=utf8;

# Dumping data for table jadaold.ie_profile_detail: ~0 rows (approximately)
/*!40000 ALTER TABLE `ie_profile_detail` DISABLE KEYS */;
INSERT INTO `ie_profile_detail` (`ie_profile_detail_id`, `ie_profile_field_name`, `ie_profile_field_value`, `ie_profile_group_index`, `ie_profile_group_name`, `ie_profile_position`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `seq_num`, `ie_profile_header_id`) VALUES
	(7523, 'siteProfileClassId', NULL, NULL, '', 1, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 0, 3),
	(7524, 'siteProfileClassName', NULL, NULL, '', 2, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 1, 3),
	(7525, 'siteCurrencyClassId', NULL, NULL, '', 3, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 2, 3),
	(7526, 'siteCurrencyClassName', NULL, NULL, '', 4, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 3, 3),
	(7527, 'itemId', NULL, NULL, '', 5, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 4, 3),
	(7528, 'itemNum', NULL, NULL, '', 6, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 5, 3),
	(7529, 'itemUpcCd', NULL, NULL, '', 7, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 6, 3),
	(7530, 'itemSkuCd', NULL, NULL, '', 8, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 7, 3),
	(7531, 'itemTypeCd', NULL, NULL, '', 9, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 8, 3),
	(7532, 'itemSellable', NULL, NULL, '', 10, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 9, 3),
	(7533, 'itemCost', NULL, NULL, '', 11, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 10, 3),
	(7534, 'itemHitCounter', NULL, NULL, '', 12, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 11, 3),
	(7535, 'itemRating', NULL, NULL, '', 13, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 12, 3),
	(7536, 'itemRatingCount', NULL, NULL, '', 14, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 13, 3),
	(7537, 'itemQty', NULL, NULL, '', 15, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 14, 3),
	(7538, 'itemBookedQty', NULL, NULL, '', 16, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 15, 3),
	(7539, 'itemPublishOn', NULL, NULL, '', 17, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 16, 3),
	(7540, 'itemExpireOn', NULL, NULL, '', 18, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 17, 3),
	(7541, 'published', NULL, NULL, '', 19, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 18, 3),
	(7542, 'itemShortDesc', NULL, NULL, '', 20, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 19, 3),
	(7543, 'itemDesc', NULL, NULL, '', 21, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 20, 3),
	(7544, 'pageTitle', NULL, NULL, '', 22, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 21, 3),
	(7545, 'itemPrice', NULL, NULL, '', 23, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 22, 3),
	(7546, 'itemSpecPrice', NULL, NULL, '', 24, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 23, 3),
	(7547, 'itemSpecPricePublishOn', NULL, NULL, '', 25, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 24, 3),
	(7548, 'itemSpecPriceExpireOn', NULL, NULL, '', 26, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 25, 3),
	(7549, 'shippingTypeId', NULL, NULL, '', 27, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 26, 3),
	(7550, 'shippingTypeName', NULL, NULL, '', 28, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 27, 3),
	(7551, 'productClassId', NULL, NULL, '', 29, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 28, 3),
	(7552, 'productClassName', NULL, NULL, '', 30, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 29, 3),
	(7553, 'itemParentId', NULL, NULL, '', 31, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 30, 3),
	(7554, 'itemParentSkuCd', NULL, NULL, '', 32, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 31, 3),
	(7555, 'customAttributeGroupId', NULL, NULL, '', 33, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 32, 3),
	(7556, 'customAttributeGroupName', NULL, NULL, '', 34, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 33, 3),
	(7557, 'metaKeywords', NULL, NULL, '', 35, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 34, 3),
	(7558, 'metaDescription', NULL, NULL, '', 36, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 35, 3),
	(7559, 'itemImageOverride', NULL, NULL, '', 37, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 36, 3),
	(7560, 'catId', NULL, 0, 'categories', 38, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 37, 3),
	(7561, 'catShortTitle', NULL, 0, 'categories', 39, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 38, 3),
	(7562, 'catId', NULL, 1, 'categories', 40, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 39, 3),
	(7563, 'catShortTitle', NULL, 1, 'categories', 41, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 40, 3),
	(7564, 'catId', NULL, 2, 'categories', 42, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 41, 3),
	(7565, 'catShortTitle', NULL, 2, 'categories', 43, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 42, 3),
	(7566, 'catId', NULL, 3, 'categories', 44, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 43, 3),
	(7567, 'catShortTitle', NULL, 3, 'categories', 45, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 44, 3),
	(7568, 'catId', NULL, 4, 'categories', 46, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 45, 3),
	(7569, 'catShortTitle', NULL, 4, 'categories', 47, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 46, 3),
	(7570, 'itemId', NULL, 0, 'itemsRelated', 48, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 47, 3),
	(7571, 'itemSkuCd', NULL, 0, 'itemsRelated', 49, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 48, 3),
	(7572, 'itemId', NULL, 1, 'itemsRelated', 50, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 49, 3),
	(7573, 'itemSkuCd', NULL, 1, 'itemsRelated', 51, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 50, 3),
	(7574, 'itemId', NULL, 2, 'itemsRelated', 52, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 51, 3),
	(7575, 'itemSkuCd', NULL, 2, 'itemsRelated', 53, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 52, 3),
	(7576, 'itemId', NULL, 3, 'itemsRelated', 54, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 53, 3),
	(7577, 'itemSkuCd', NULL, 3, 'itemsRelated', 55, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 54, 3),
	(7578, 'itemId', NULL, 4, 'itemsRelated', 56, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 55, 3),
	(7579, 'itemSkuCd', NULL, 4, 'itemsRelated', 57, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 56, 3),
	(7580, 'itemId', NULL, 0, 'itemsUpSell', 68, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 57, 3),
	(7581, 'itemSkuCd', NULL, 0, 'itemsUpSell', 69, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 58, 3),
	(7582, 'itemId', NULL, 1, 'itemsUpSell', 70, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 59, 3),
	(7583, 'itemSkuCd', NULL, 1, 'itemsUpSell', 71, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 60, 3),
	(7584, 'itemId', NULL, 2, 'itemsUpSell', 72, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 61, 3),
	(7585, 'itemSkuCd', NULL, 2, 'itemsUpSell', 73, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 62, 3),
	(7586, 'itemId', NULL, 3, 'itemsUpSell', 74, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 63, 3),
	(7587, 'itemSkuCd', NULL, 3, 'itemsUpSell', 75, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 64, 3),
	(7588, 'itemId', NULL, 4, 'itemsUpSell', 76, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 65, 3),
	(7589, 'itemSkuCd', NULL, 4, 'itemsUpSell', 77, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 66, 3),
	(7590, 'itemId', NULL, 0, 'itemsCrossSell', 58, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 67, 3),
	(7591, 'itemSkuCd', NULL, 0, 'itemsCrossSell', 59, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 68, 3),
	(7592, 'itemId', NULL, 1, 'itemsCrossSell', 60, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 69, 3),
	(7593, 'itemSkuCd', NULL, 1, 'itemsCrossSell', 61, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 70, 3),
	(7594, 'itemId', NULL, 2, 'itemsCrossSell', 62, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 71, 3),
	(7595, 'itemSkuCd', NULL, 2, 'itemsCrossSell', 63, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 72, 3),
	(7596, 'itemId', NULL, 3, 'itemsCrossSell', 64, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 73, 3),
	(7597, 'itemSkuCd', NULL, 3, 'itemsCrossSell', 65, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 74, 3),
	(7598, 'itemId', NULL, 4, 'itemsCrossSell', 66, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 75, 3),
	(7599, 'itemSkuCd', NULL, 4, 'itemsCrossSell', 67, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 76, 3),
	(7600, 'custClassId', NULL, 0, 'itemTierPrices', 78, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 77, 3),
	(7601, 'itemTierQty', NULL, 0, 'itemTierPrices', 79, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 78, 3),
	(7602, 'itemPrice', NULL, 0, 'itemTierPrices', 80, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 79, 3),
	(7603, 'itemTierPricePublishOn', NULL, 0, 'itemTierPrices', 81, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 80, 3),
	(7604, 'itemTierPriceExpireOn', NULL, 0, 'itemTierPrices', 82, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 81, 3),
	(7605, 'custClassId', NULL, 1, 'itemTierPrices', 83, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 82, 3),
	(7606, 'itemTierQty', NULL, 1, 'itemTierPrices', 84, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 83, 3),
	(7607, 'itemPrice', NULL, 1, 'itemTierPrices', 85, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 84, 3),
	(7608, 'itemTierPricePublishOn', NULL, 1, 'itemTierPrices', 86, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 85, 3),
	(7609, 'itemTierPriceExpireOn', NULL, 1, 'itemTierPrices', 87, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 86, 3),
	(7610, 'custClassId', NULL, 2, 'itemTierPrices', 88, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 87, 3),
	(7611, 'itemTierQty', NULL, 2, 'itemTierPrices', 89, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 88, 3),
	(7612, 'itemPrice', NULL, 2, 'itemTierPrices', 90, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 89, 3),
	(7613, 'itemTierPricePublishOn', NULL, 2, 'itemTierPrices', 91, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 90, 3),
	(7614, 'itemTierPriceExpireOn', NULL, 2, 'itemTierPrices', 92, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 91, 3),
	(7615, 'custClassId', NULL, 3, 'itemTierPrices', 93, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 92, 3),
	(7616, 'itemTierQty', NULL, 3, 'itemTierPrices', 94, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 93, 3),
	(7617, 'itemPrice', NULL, 3, 'itemTierPrices', 95, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 94, 3),
	(7618, 'itemTierPricePublishOn', NULL, 3, 'itemTierPrices', 96, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 95, 3),
	(7619, 'itemTierPriceExpireOn', NULL, 3, 'itemTierPrices', 97, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 96, 3),
	(7620, 'custClassId', NULL, 4, 'itemTierPrices', 98, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 97, 3),
	(7621, 'itemTierQty', NULL, 4, 'itemTierPrices', 99, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 98, 3),
	(7622, 'itemPrice', NULL, 4, 'itemTierPrices', 100, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 99, 3),
	(7623, 'itemTierPricePublishOn', NULL, 4, 'itemTierPrices', 101, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 100, 3),
	(7624, 'itemTierPriceExpireOn', NULL, 4, 'itemTierPrices', 102, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 101, 3),
	(7625, 'customAttribId', NULL, 0, 'itemAttributeDetails', 103, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 102, 3),
	(7626, 'customAttribName', NULL, 0, 'itemAttributeDetails', 104, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 103, 3),
	(7627, 'customAttribOptionId', NULL, 0, 'itemAttributeDetails', 105, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 104, 3),
	(7628, 'customAttribValue', NULL, 0, 'itemAttributeDetails', 106, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 105, 3),
	(7629, 'customAttribId', NULL, 1, 'itemAttributeDetails', 107, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 106, 3),
	(7630, 'customAttribName', NULL, 1, 'itemAttributeDetails', 108, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 107, 3),
	(7631, 'customAttribOptionId', NULL, 1, 'itemAttributeDetails', 109, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 108, 3),
	(7632, 'customAttribValue', NULL, 1, 'itemAttributeDetails', 110, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 109, 3),
	(7633, 'customAttribId', NULL, 2, 'itemAttributeDetails', 111, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 110, 3),
	(7634, 'customAttribName', NULL, 2, 'itemAttributeDetails', 112, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 111, 3),
	(7635, 'customAttribOptionId', NULL, 2, 'itemAttributeDetails', 113, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 112, 3),
	(7636, 'customAttribValue', NULL, 2, 'itemAttributeDetails', 114, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 113, 3),
	(7637, 'customAttribId', NULL, 3, 'itemAttributeDetails', 115, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 114, 3),
	(7638, 'customAttribName', NULL, 3, 'itemAttributeDetails', 116, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 115, 3),
	(7639, 'customAttribOptionId', NULL, 3, 'itemAttributeDetails', 117, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 116, 3),
	(7640, 'customAttribValue', NULL, 3, 'itemAttributeDetails', 118, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 117, 3),
	(7641, 'customAttribId', NULL, 4, 'itemAttributeDetails', 119, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 118, 3),
	(7642, 'customAttribName', NULL, 4, 'itemAttributeDetails', 120, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 119, 3),
	(7643, 'customAttribOptionId', NULL, 4, 'itemAttributeDetails', 121, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 120, 3),
	(7644, 'customAttribValue', NULL, 4, 'itemAttributeDetails', 122, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 121, 3),
	(7645, 'siteProfileClassId', NULL, NULL, '', 1, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 0, 1),
	(7646, 'siteProfileClassName', NULL, NULL, '', 2, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 1, 1),
	(7647, 'siteCurrencyClassId', NULL, NULL, '', 3, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 2, 1),
	(7648, 'siteCurrencyClassName', NULL, NULL, '', 4, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 3, 1),
	(7649, 'itemId', NULL, NULL, '', 5, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 4, 1),
	(7650, 'itemNum', NULL, NULL, '', 6, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 5, 1),
	(7651, 'itemUpcCd', NULL, NULL, '', 7, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 6, 1),
	(7652, 'itemSkuCd', NULL, NULL, '', 8, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 7, 1),
	(7653, 'itemTypeCd', NULL, NULL, '', 9, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 8, 1),
	(7654, 'itemSellable', NULL, NULL, '', 10, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 9, 1),
	(7655, 'itemCost', NULL, NULL, '', 11, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 10, 1),
	(7656, 'itemHitCounter', NULL, NULL, '', 12, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 11, 1),
	(7657, 'itemRating', NULL, NULL, '', 13, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 12, 1),
	(7658, 'itemRatingCount', NULL, NULL, '', 14, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 13, 1),
	(7659, 'itemQty', NULL, NULL, '', 15, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 14, 1),
	(7660, 'itemBookedQty', NULL, NULL, '', 16, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 15, 1),
	(7661, 'itemPublishOn', NULL, NULL, '', 17, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 16, 1),
	(7662, 'itemExpireOn', NULL, NULL, '', 18, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 17, 1),
	(7663, 'published', NULL, NULL, '', 19, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 18, 1),
	(7664, 'itemShortDesc', NULL, NULL, '', 20, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 19, 1),
	(7665, 'itemDesc', NULL, NULL, '', 21, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 20, 1),
	(7666, 'pageTitle', NULL, NULL, '', 22, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 21, 1),
	(7667, 'itemPrice', NULL, NULL, '', 23, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 22, 1),
	(7668, 'itemSpecPrice', NULL, NULL, '', 24, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 23, 1),
	(7669, 'itemSpecPricePublishOn', NULL, NULL, '', 25, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 24, 1),
	(7670, 'itemSpecPriceExpireOn', NULL, NULL, '', 26, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 25, 1),
	(7671, 'shippingTypeId', NULL, NULL, '', 27, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 26, 1),
	(7672, 'shippingTypeName', NULL, NULL, '', 28, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 27, 1),
	(7673, 'productClassId', NULL, NULL, '', 29, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 28, 1),
	(7674, 'productClassName', NULL, NULL, '', 30, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 29, 1),
	(7675, 'itemParentId', NULL, NULL, '', 31, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 30, 1),
	(7676, 'itemParentSkuCd', NULL, NULL, '', 32, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 31, 1),
	(7677, 'customAttributeGroupId', NULL, NULL, '', 33, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 32, 1),
	(7678, 'customAttributeGroupName', NULL, NULL, '', 34, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 33, 1),
	(7679, 'metaKeywords', NULL, NULL, '', 35, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 34, 1),
	(7680, 'metaDescription', NULL, NULL, '', 36, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 35, 1),
	(7681, 'itemImageOverride', NULL, NULL, '', 37, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 36, 1),
	(7682, 'catId', NULL, 0, 'categories', 38, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 37, 1),
	(7683, 'catShortTitle', NULL, 0, 'categories', 39, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 38, 1),
	(7684, 'catId', NULL, 1, 'categories', 40, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 39, 1),
	(7685, 'catShortTitle', NULL, 1, 'categories', 41, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 40, 1),
	(7686, 'catId', NULL, 2, 'categories', 42, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 41, 1),
	(7687, 'catShortTitle', NULL, 2, 'categories', 43, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 42, 1),
	(7688, 'catId', NULL, 3, 'categories', 44, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 43, 1),
	(7689, 'catShortTitle', NULL, 3, 'categories', 45, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 44, 1),
	(7690, 'catId', NULL, 4, 'categories', 46, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 45, 1),
	(7691, 'catShortTitle', NULL, 4, 'categories', 47, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 46, 1),
	(7692, 'itemId', NULL, 0, 'itemsRelated', 48, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 47, 1),
	(7693, 'itemSkuCd', NULL, 0, 'itemsRelated', 49, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 48, 1),
	(7694, 'itemId', NULL, 1, 'itemsRelated', 50, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 49, 1),
	(7695, 'itemSkuCd', NULL, 1, 'itemsRelated', 51, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 50, 1),
	(7696, 'itemId', NULL, 2, 'itemsRelated', 52, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 51, 1),
	(7697, 'itemSkuCd', NULL, 2, 'itemsRelated', 53, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 52, 1),
	(7698, 'itemId', NULL, 3, 'itemsRelated', 54, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 53, 1),
	(7699, 'itemSkuCd', NULL, 3, 'itemsRelated', 55, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 54, 1),
	(7700, 'itemId', NULL, 4, 'itemsRelated', 56, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 55, 1),
	(7701, 'itemSkuCd', NULL, 4, 'itemsRelated', 57, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 56, 1),
	(7702, 'itemId', NULL, 0, 'itemsUpSell', 68, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 57, 1),
	(7703, 'itemSkuCd', NULL, 0, 'itemsUpSell', 69, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 58, 1),
	(7704, 'itemId', NULL, 1, 'itemsUpSell', 70, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 59, 1),
	(7705, 'itemSkuCd', NULL, 1, 'itemsUpSell', 71, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 60, 1),
	(7706, 'itemId', NULL, 2, 'itemsUpSell', 72, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 61, 1),
	(7707, 'itemSkuCd', NULL, 2, 'itemsUpSell', 73, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 62, 1),
	(7708, 'itemId', NULL, 3, 'itemsUpSell', 74, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 63, 1),
	(7709, 'itemSkuCd', NULL, 3, 'itemsUpSell', 75, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 64, 1),
	(7710, 'itemId', NULL, 4, 'itemsUpSell', 76, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 65, 1),
	(7711, 'itemSkuCd', NULL, 4, 'itemsUpSell', 77, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 66, 1),
	(7712, 'itemId', NULL, 0, 'itemsCrossSell', 58, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 67, 1),
	(7713, 'itemSkuCd', NULL, 0, 'itemsCrossSell', 59, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 68, 1),
	(7714, 'itemId', NULL, 1, 'itemsCrossSell', 60, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 69, 1),
	(7715, 'itemSkuCd', NULL, 1, 'itemsCrossSell', 61, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 70, 1),
	(7716, 'itemId', NULL, 2, 'itemsCrossSell', 62, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 71, 1),
	(7717, 'itemSkuCd', NULL, 2, 'itemsCrossSell', 63, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 72, 1),
	(7718, 'itemId', NULL, 3, 'itemsCrossSell', 64, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 73, 1),
	(7719, 'itemSkuCd', NULL, 3, 'itemsCrossSell', 65, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 74, 1),
	(7720, 'itemId', NULL, 4, 'itemsCrossSell', 66, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 75, 1),
	(7721, 'itemSkuCd', NULL, 4, 'itemsCrossSell', 67, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 76, 1),
	(7722, 'custClassId', NULL, 0, 'itemTierPrices', 78, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 77, 1),
	(7723, 'itemTierQty', NULL, 0, 'itemTierPrices', 79, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 78, 1),
	(7724, 'itemPrice', NULL, 0, 'itemTierPrices', 80, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 79, 1),
	(7725, 'itemTierPricePublishOn', NULL, 0, 'itemTierPrices', 81, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 80, 1),
	(7726, 'itemTierPriceExpireOn', NULL, 0, 'itemTierPrices', 82, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 81, 1),
	(7727, 'custClassId', NULL, 1, 'itemTierPrices', 83, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 82, 1),
	(7728, 'itemTierQty', NULL, 1, 'itemTierPrices', 84, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 83, 1),
	(7729, 'itemPrice', NULL, 1, 'itemTierPrices', 85, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 84, 1),
	(7730, 'itemTierPricePublishOn', NULL, 1, 'itemTierPrices', 86, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 85, 1),
	(7731, 'itemTierPriceExpireOn', NULL, 1, 'itemTierPrices', 87, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 86, 1),
	(7732, 'custClassId', NULL, 2, 'itemTierPrices', 88, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 87, 1),
	(7733, 'itemTierQty', NULL, 2, 'itemTierPrices', 89, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 88, 1),
	(7734, 'itemPrice', NULL, 2, 'itemTierPrices', 90, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 89, 1),
	(7735, 'itemTierPricePublishOn', NULL, 2, 'itemTierPrices', 91, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 90, 1),
	(7736, 'itemTierPriceExpireOn', NULL, 2, 'itemTierPrices', 92, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 91, 1),
	(7737, 'custClassId', NULL, 3, 'itemTierPrices', 93, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 92, 1),
	(7738, 'itemTierQty', NULL, 3, 'itemTierPrices', 94, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 93, 1),
	(7739, 'itemPrice', NULL, 3, 'itemTierPrices', 95, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 94, 1),
	(7740, 'itemTierPricePublishOn', NULL, 3, 'itemTierPrices', 96, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 95, 1),
	(7741, 'itemTierPriceExpireOn', NULL, 3, 'itemTierPrices', 97, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 96, 1),
	(7742, 'custClassId', NULL, 4, 'itemTierPrices', 98, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 97, 1),
	(7743, 'itemTierQty', NULL, 4, 'itemTierPrices', 99, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 98, 1),
	(7744, 'itemPrice', NULL, 4, 'itemTierPrices', 100, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 99, 1),
	(7745, 'itemTierPricePublishOn', NULL, 4, 'itemTierPrices', 101, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 100, 1),
	(7746, 'itemTierPriceExpireOn', NULL, 4, 'itemTierPrices', 102, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 101, 1),
	(7747, 'customAttribId', NULL, 0, 'itemAttributeDetails', 103, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 102, 1),
	(7748, 'customAttribName', NULL, 0, 'itemAttributeDetails', 104, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 103, 1),
	(7749, 'customAttribOptionId', NULL, 0, 'itemAttributeDetails', 105, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 104, 1),
	(7750, 'customAttribValue', NULL, 0, 'itemAttributeDetails', 106, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 105, 1),
	(7751, 'customAttribId', NULL, 1, 'itemAttributeDetails', 107, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 106, 1),
	(7752, 'customAttribName', NULL, 1, 'itemAttributeDetails', 108, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 107, 1),
	(7753, 'customAttribOptionId', NULL, 1, 'itemAttributeDetails', 109, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 108, 1),
	(7754, 'customAttribValue', NULL, 1, 'itemAttributeDetails', 110, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 109, 1),
	(7755, 'customAttribId', NULL, 2, 'itemAttributeDetails', 111, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 110, 1),
	(7756, 'customAttribName', NULL, 2, 'itemAttributeDetails', 112, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 111, 1),
	(7757, 'customAttribOptionId', NULL, 2, 'itemAttributeDetails', 113, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 112, 1),
	(7758, 'customAttribValue', NULL, 2, 'itemAttributeDetails', 114, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 113, 1),
	(7759, 'customAttribId', NULL, 3, 'itemAttributeDetails', 115, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 114, 1),
	(7760, 'customAttribName', NULL, 3, 'itemAttributeDetails', 116, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 115, 1),
	(7761, 'customAttribOptionId', NULL, 3, 'itemAttributeDetails', 117, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 116, 1),
	(7762, 'customAttribValue', NULL, 3, 'itemAttributeDetails', 118, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 117, 1),
	(7763, 'customAttribId', NULL, 4, 'itemAttributeDetails', 119, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 118, 1),
	(7764, 'customAttribName', NULL, 4, 'itemAttributeDetails', 120, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 119, 1),
	(7765, 'customAttribOptionId', NULL, 4, 'itemAttributeDetails', 121, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 120, 1),
	(7766, 'customAttribValue', NULL, 4, 'itemAttributeDetails', 122, 'admin', '2011-03-02 10:15:03', 'admin', '2011-03-02 10:15:03', 121, 1);
/*!40000 ALTER TABLE `ie_profile_detail` ENABLE KEYS */;


# Dumping structure for table jadaold.ie_profile_header
CREATE TABLE IF NOT EXISTS `ie_profile_header` (
  `ie_profile_header_id` bigint(20) NOT NULL auto_increment,
  `ie_profile_header_name` varchar(80) NOT NULL,
  `ie_profile_type` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `system_record` char(1) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  PRIMARY KEY  (`ie_profile_header_id`),
  KEY `FK30A5DB6670EBDE65` (`site_id`),
  CONSTRAINT `FK30A5DB6670EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

# Dumping data for table jadaold.ie_profile_header: ~0 rows (approximately)
/*!40000 ALTER TABLE `ie_profile_header` DISABLE KEYS */;
INSERT INTO `ie_profile_header` (`ie_profile_header_id`, `ie_profile_header_name`, `ie_profile_type`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `system_record`, `site_id`) VALUES
	(1, 'Standard', 'E', 'admin', '2011-03-02 10:19:14', 'admin', '2011-03-02 10:19:19', 'Y', 'default'),
	(3, 'Standard', 'I', 'admin', '2010-06-05 22:11:29', 'admin', '2011-03-02 10:15:03', 'Y', 'default');
/*!40000 ALTER TABLE `ie_profile_header` ENABLE KEYS */;


# Dumping structure for table jada.invoice_detail
CREATE TABLE IF NOT EXISTS `invoice_detail` (
  `invoice_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_invoice_amount` float NOT NULL,
  `item_invoice_qty` int(11) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `invoice_header_id` bigint(20) DEFAULT NULL,
  `order_item_detail_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`invoice_detail_id`),
  KEY `FK119755A347EC5AF5` (`order_item_detail_id`),
  KEY `FK119755A3E5DAA3EA` (`invoice_header_id`),
  CONSTRAINT `FK119755A3E5DAA3EA` FOREIGN KEY (`invoice_header_id`) REFERENCES `invoice_header` (`invoice_header_id`),
  CONSTRAINT `FK119755A347EC5AF5` FOREIGN KEY (`order_item_detail_id`) REFERENCES `order_item_detail` (`order_item_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.invoice_detail: ~0 rows (approximately)
/*!40000 ALTER TABLE `invoice_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_detail` ENABLE KEYS */;


# Dumping structure for table jada.invoice_detail_tax
CREATE TABLE IF NOT EXISTS `invoice_detail_tax` (
  `invoice_detail_tax_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `tax_amount` float NOT NULL,
  `tax_name` varchar(40) NOT NULL,
  `invoice_detail_id` bigint(20) DEFAULT NULL,
  `invoice_header_id` bigint(20) DEFAULT NULL,
  `tax_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`invoice_detail_tax_id`),
  KEY `FK250ABB0F762B9D6A` (`invoice_detail_id`),
  KEY `FK250ABB0FC2D9DA6F` (`tax_id`),
  KEY `FK250ABB0FE5DAA3EA` (`invoice_header_id`),
  CONSTRAINT `FK250ABB0FE5DAA3EA` FOREIGN KEY (`invoice_header_id`) REFERENCES `invoice_header` (`invoice_header_id`),
  CONSTRAINT `FK250ABB0F762B9D6A` FOREIGN KEY (`invoice_detail_id`) REFERENCES `invoice_detail` (`invoice_detail_id`),
  CONSTRAINT `FK250ABB0FC2D9DA6F` FOREIGN KEY (`tax_id`) REFERENCES `tax` (`tax_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.invoice_detail_tax: ~0 rows (approximately)
/*!40000 ALTER TABLE `invoice_detail_tax` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_detail_tax` ENABLE KEYS */;


# Dumping structure for table jada.invoice_header
CREATE TABLE IF NOT EXISTS `invoice_header` (
  `invoice_header_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_date` datetime NOT NULL,
  `invoice_num` varchar(20) NOT NULL,
  `invoice_status` varchar(1) NOT NULL,
  `invoice_total` float NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `shipping_total` float NOT NULL,
  `order_header_id` bigint(20) DEFAULT NULL,
  `payment_tran_id` bigint(20) DEFAULT NULL,
  `void_payment_tran_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`invoice_header_id`),
  UNIQUE KEY `invoice_num` (`invoice_num`),
  KEY `FK18621FDFB7B46AB1` (`void_payment_tran_id`),
  KEY `FK18621FDF267D6B6C` (`order_header_id`),
  KEY `FK18621FDFEEE96B5C` (`payment_tran_id`),
  CONSTRAINT `FK18621FDFEEE96B5C` FOREIGN KEY (`payment_tran_id`) REFERENCES `payment_tran` (`payment_tran_id`),
  CONSTRAINT `FK18621FDF267D6B6C` FOREIGN KEY (`order_header_id`) REFERENCES `order_header` (`order_header_id`),
  CONSTRAINT `FK18621FDFB7B46AB1` FOREIGN KEY (`void_payment_tran_id`) REFERENCES `payment_tran` (`payment_tran_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.invoice_header: ~0 rows (approximately)
/*!40000 ALTER TABLE `invoice_header` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_header` ENABLE KEYS */;


# Dumping structure for table jada.item
CREATE TABLE IF NOT EXISTS `item` (
  `item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_booked_qty` int(11) NOT NULL,
  `item_cost` float DEFAULT NULL,
  `item_expire_on` date NOT NULL,
  `item_hit_counter` int(11) NOT NULL,
  `item_natural_key` varchar(255) NOT NULL,
  `item_num` varchar(20) NOT NULL,
  `item_publish_on` date NOT NULL,
  `item_qty` int(11) NOT NULL,
  `item_rating` float NOT NULL,
  `item_rating_count` int(11) NOT NULL,
  `item_sellable` char(1) NOT NULL,
  `item_sku_cd` varchar(40) NOT NULL,
  `item_type_cd` varchar(2) NOT NULL,
  `item_upc_cd` varchar(20) NOT NULL,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `custom_attrib_group_id` bigint(20) DEFAULT NULL,
  `item_lang_id` bigint(20) NOT NULL,
  `item_price_curr_id` bigint(20) NOT NULL,
  `item_sku_id` bigint(20) DEFAULT NULL,
  `item_spec_price_curr_id` bigint(20) DEFAULT NULL,
  `product_class_id` bigint(20) DEFAULT NULL,
  `shipping_type_id` bigint(20) DEFAULT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  UNIQUE KEY `item_natural_key` (`item_natural_key`,`site_id`),
  KEY `FK317B13BA900701` (`custom_attrib_group_id`),
  KEY `FK317B13FBF32B66` (`product_class_id`),
  KEY `FK317B131E1D7BC2` (`shipping_type_id`),
  KEY `FK317B138D4ABC76` (`item_spec_price_curr_id`),
  KEY `FK317B13EAFC5FE5` (`user_id`),
  KEY `FK317B1370EBDE65` (`site_id`),
  KEY `FK317B136AECCA76` (`item_lang_id`),
  KEY `FK317B13DC783B47` (`item_sku_id`),
  KEY `FK317B135765B122` (`item_price_curr_id`),
  CONSTRAINT `FK317B135765B122` FOREIGN KEY (`item_price_curr_id`) REFERENCES `item_price_currency` (`item_curr_id`),
  CONSTRAINT `FK317B131E1D7BC2` FOREIGN KEY (`shipping_type_id`) REFERENCES `shipping_type` (`shipping_type_id`),
  CONSTRAINT `FK317B136AECCA76` FOREIGN KEY (`item_lang_id`) REFERENCES `item_language` (`item_lang_id`),
  CONSTRAINT `FK317B1370EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK317B138D4ABC76` FOREIGN KEY (`item_spec_price_curr_id`) REFERENCES `item_price_currency` (`item_curr_id`),
  CONSTRAINT `FK317B13BA900701` FOREIGN KEY (`custom_attrib_group_id`) REFERENCES `custom_attrib_group` (`custom_attrib_group_id`),
  CONSTRAINT `FK317B13DC783B47` FOREIGN KEY (`item_sku_id`) REFERENCES `item` (`item_id`),
  CONSTRAINT `FK317B13EAFC5FE5` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FK317B13FBF32B66` FOREIGN KEY (`product_class_id`) REFERENCES `product_class` (`product_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item: ~0 rows (approximately)
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
/*!40000 ALTER TABLE `item` ENABLE KEYS */;


# Dumping structure for table jada.item_attrib_detail
CREATE TABLE IF NOT EXISTS `item_attrib_detail` (
  `item_attrib_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `custom_attrib_detail_id` bigint(20) DEFAULT NULL,
  `custom_attrib_option_id` bigint(20) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `item_attrib_detail_lang_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`item_attrib_detail_id`),
  KEY `FK41AD7F3A6B5C3CD3` (`custom_attrib_option_id`),
  KEY `FK41AD7F3A7461DF53` (`custom_attrib_detail_id`),
  KEY `FK41AD7F3A71DEBAE5` (`item_id`),
  KEY `FK41AD7F3AA63B168E` (`item_attrib_detail_lang_id`),
  CONSTRAINT `FK41AD7F3AA63B168E` FOREIGN KEY (`item_attrib_detail_lang_id`) REFERENCES `item_attrib_detail_language` (`item_attrib_detail_lang_id`),
  CONSTRAINT `FK41AD7F3A6B5C3CD3` FOREIGN KEY (`custom_attrib_option_id`) REFERENCES `custom_attrib_option` (`custom_attrib_option_id`),
  CONSTRAINT `FK41AD7F3A71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`),
  CONSTRAINT `FK41AD7F3A7461DF53` FOREIGN KEY (`custom_attrib_detail_id`) REFERENCES `custom_attrib_detail` (`custom_attrib_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_attrib_detail: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_attrib_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_attrib_detail` ENABLE KEYS */;


# Dumping structure for table jada.item_attrib_detail_language
CREATE TABLE IF NOT EXISTS `item_attrib_detail_language` (
  `item_attrib_detail_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_attrib_detail_value` varchar(80) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `item_attrib_detail_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`item_attrib_detail_lang_id`),
  KEY `FKBDECC6BD851D14F` (`item_attrib_detail_id`),
  KEY `FKBDECC6BD473E64D1` (`site_profile_class_id`),
  CONSTRAINT `FKBDECC6BD473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FKBDECC6BD851D14F` FOREIGN KEY (`item_attrib_detail_id`) REFERENCES `item_attrib_detail` (`item_attrib_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_attrib_detail_language: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_attrib_detail_language` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_attrib_detail_language` ENABLE KEYS */;


# Dumping structure for table jada.item_category
CREATE TABLE IF NOT EXISTS `item_category` (
  `item_id` bigint(20) NOT NULL,
  `cat_id` bigint(20) NOT NULL,
  PRIMARY KEY (`item_id`,`cat_id`),
  KEY `FK1A2438AA71DEBAE5` (`item_id`),
  KEY `FK1A2438AA30F21FAD` (`cat_id`),
  CONSTRAINT `FK1A2438AA30F21FAD` FOREIGN KEY (`cat_id`) REFERENCES `category` (`cat_id`),
  CONSTRAINT `FK1A2438AA71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_category: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_category` ENABLE KEYS */;


# Dumping structure for table jada.item_children
CREATE TABLE IF NOT EXISTS `item_children` (
  `item_id` bigint(20) NOT NULL,
  `item_child_id` bigint(20) NOT NULL,
  PRIMARY KEY (`item_id`,`item_child_id`),
  KEY `FK7A0BD9AB71DEBAE5` (`item_id`),
  KEY `FK7A0BD9AB6DC793E8` (`item_child_id`),
  CONSTRAINT `FK7A0BD9AB6DC793E8` FOREIGN KEY (`item_child_id`) REFERENCES `item` (`item_id`),
  CONSTRAINT `FK7A0BD9AB71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_children: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_children` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_children` ENABLE KEYS */;


# Dumping structure for table jada.item_crosssell
CREATE TABLE IF NOT EXISTS `item_crosssell` (
  `item_id` bigint(20) NOT NULL,
  `item_crosssell_id` bigint(20) NOT NULL,
  PRIMARY KEY (`item_id`,`item_crosssell_id`),
  KEY `FK206641664F51EFB2` (`item_crosssell_id`),
  KEY `FK2066416671DEBAE5` (`item_id`),
  CONSTRAINT `FK2066416671DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`),
  CONSTRAINT `FK206641664F51EFB2` FOREIGN KEY (`item_crosssell_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_crosssell: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_crosssell` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_crosssell` ENABLE KEYS */;


# Dumping structure for table jada.item_currency
CREATE TABLE IF NOT EXISTS `item_currency` (
  `item_curr_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_eff_price` float NOT NULL,
  `item_eff_price_xfactor` int(11) NOT NULL,
  `item_eff_spec_price` float DEFAULT NULL,
  `item_eff_spec_price_xfactor` int(11) NOT NULL,
  `item_price` float NOT NULL,
  `item_spec_price` float DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `site_currency_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`item_curr_id`),
  KEY `FK396D6BBD71DEBAE5` (`item_id`),
  KEY `FK396D6BBD96324A6D` (`site_currency_class_id`),
  CONSTRAINT `FK396D6BBD96324A6D` FOREIGN KEY (`site_currency_class_id`) REFERENCES `site_currency_class` (`site_currency_class_id`),
  CONSTRAINT `FK396D6BBD71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_currency: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_currency` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_currency` ENABLE KEYS */;


# Dumping structure for table jada.item_desc_search
CREATE TABLE IF NOT EXISTS `item_desc_search` (
  `item_desc_search_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_short_desc` varchar(128) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`item_desc_search_id`),
  KEY `FK7E02E3EA71DEBAE5` (`item_id`),
  KEY `FK7E02E3EA473E64D1` (`site_profile_class_id`),
  CONSTRAINT `FK7E02E3EA473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FK7E02E3EA71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_desc_search: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_desc_search` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_desc_search` ENABLE KEYS */;


# Dumping structure for table jada.item_image
CREATE TABLE IF NOT EXISTS `item_image` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_type` varchar(20) NOT NULL,
  `image_height` int(11) NOT NULL,
  `image_name` varchar(40) NOT NULL,
  `image_value` longblob NOT NULL,
  `image_width` int(11) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `item_lang_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`image_id`),
  KEY `FK8B16AD2F6AECCA76` (`item_lang_id`),
  CONSTRAINT `FK8B16AD2F6AECCA76` FOREIGN KEY (`item_lang_id`) REFERENCES `item_language` (`item_lang_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_image: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_image` ENABLE KEYS */;


# Dumping structure for table jada.item_image_language
CREATE TABLE IF NOT EXISTS `item_image_language` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_type` varchar(20) NOT NULL,
  `image_height` int(11) NOT NULL,
  `image_name` varchar(40) NOT NULL,
  `image_value` longblob NOT NULL,
  `image_width` int(11) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `item_lang_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`image_id`),
  KEY `FKADCF7C68473E64D1` (`site_profile_class_id`),
  KEY `FKADCF7C686AECCA76` (`item_lang_id`),
  CONSTRAINT `FKADCF7C686AECCA76` FOREIGN KEY (`item_lang_id`) REFERENCES `item_language` (`item_lang_id`),
  CONSTRAINT `FKADCF7C68473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_image_language: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_image_language` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_image_language` ENABLE KEYS */;


# Dumping structure for table jada.item_language
CREATE TABLE IF NOT EXISTS `item_language` (
  `item_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_desc` longtext,
  `item_image_override` varchar(1) NOT NULL,
  `meta_keywords` varchar(1000) DEFAULT NULL,
  `meta_description` varchar(1000) DEFAULT NULL,
  `item_short_desc` varchar(128) DEFAULT NULL,
  `page_title` varchar(255) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `image_id` bigint(20) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`item_lang_id`),
  UNIQUE KEY `image_id` (`image_id`),
  KEY `FKB6F40F04BAC9415C` (`image_id`),
  KEY `FKB6F40F0471DEBAE5` (`item_id`),
  KEY `FKB6F40F04473E64D1` (`site_profile_class_id`),
  CONSTRAINT `FKB6F40F04473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FKB6F40F0471DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`),
  CONSTRAINT `FKB6F40F04BAC9415C` FOREIGN KEY (`image_id`) REFERENCES `item_image` (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_language: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_language` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_language` ENABLE KEYS */;


# Dumping structure for table jada.item_price_currency
CREATE TABLE IF NOT EXISTS `item_price_currency` (
  `item_curr_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_price` float NOT NULL,
  `item_price_expire_on` date DEFAULT NULL,
  `item_price_publish_on` date DEFAULT NULL,
  `item_price_type_code` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `site_currency_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`item_curr_id`),
  KEY `FKEFE2DCF371DEBAE5` (`item_id`),
  KEY `FKEFE2DCF396324A6D` (`site_currency_class_id`),
  CONSTRAINT `FKEFE2DCF396324A6D` FOREIGN KEY (`site_currency_class_id`) REFERENCES `site_currency_class` (`site_currency_class_id`),
  CONSTRAINT `FKEFE2DCF371DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_price_currency: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_price_currency` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_price_currency` ENABLE KEYS */;


# Dumping structure for table jada.item_price_search
CREATE TABLE IF NOT EXISTS `item_price_search` (
  `item_price_search_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `exchange_factor` int(11) NOT NULL,
  `item_price` float NOT NULL,
  `item_price_expire_on` date NOT NULL,
  `item_price_publish_on` date NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `site_currency_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`item_price_search_id`),
  KEY `FK8CB230A71DEBAE5` (`item_id`),
  KEY `FK8CB230A96324A6D` (`site_currency_class_id`),
  CONSTRAINT `FK8CB230A96324A6D` FOREIGN KEY (`site_currency_class_id`) REFERENCES `site_currency_class` (`site_currency_class_id`),
  CONSTRAINT `FK8CB230A71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_price_search: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_price_search` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_price_search` ENABLE KEYS */;


# Dumping structure for table jada.item_related
CREATE TABLE IF NOT EXISTS `item_related` (
  `item_id` bigint(20) NOT NULL,
  `item_related_id` bigint(20) NOT NULL,
  PRIMARY KEY (`item_id`,`item_related_id`),
  KEY `FKEF29FAFF71DEBAE5` (`item_id`),
  KEY `FKEF29FAFFBA0D2379` (`item_related_id`),
  CONSTRAINT `FKEF29FAFFBA0D2379` FOREIGN KEY (`item_related_id`) REFERENCES `item` (`item_id`),
  CONSTRAINT `FKEF29FAFF71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_related: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_related` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_related` ENABLE KEYS */;


# Dumping structure for table jada.item_site_domain
CREATE TABLE IF NOT EXISTS `item_site_domain` (
  `item_id` bigint(20) NOT NULL,
  `site_domain_id` bigint(20) NOT NULL,
  PRIMARY KEY (`item_id`,`site_domain_id`),
  KEY `FK538F599071DEBAE5` (`item_id`),
  KEY `FK538F5990B8784434` (`site_domain_id`),
  CONSTRAINT `FK538F5990B8784434` FOREIGN KEY (`site_domain_id`) REFERENCES `site_domain` (`site_domain_id`),
  CONSTRAINT `FK538F599071DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_site_domain: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_site_domain` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_site_domain` ENABLE KEYS */;


# Dumping structure for table jada.item_tier_price
CREATE TABLE IF NOT EXISTS `item_tier_price` (
  `item_tier_price_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_tier_price_expire_on` date DEFAULT NULL,
  `item_tier_price_publish_on` date DEFAULT NULL,
  `item_tier_qty` int(11) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `customer_class_id` bigint(20) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `item_tier_price_curr_id` bigint(20) NOT NULL,
  PRIMARY KEY (`item_tier_price_id`),
  KEY `FK88E3355871DEBAE5` (`item_id`),
  KEY `FK88E335583D38EB12` (`customer_class_id`),
  KEY `FK88E33558292BB29B` (`item_tier_price_curr_id`),
  CONSTRAINT `FK88E33558292BB29B` FOREIGN KEY (`item_tier_price_curr_id`) REFERENCES `item_tier_price_currency` (`item_tier_price_curr_id`),
  CONSTRAINT `FK88E335583D38EB12` FOREIGN KEY (`customer_class_id`) REFERENCES `customer_class` (`cust_class_id`),
  CONSTRAINT `FK88E3355871DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_tier_price: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_tier_price` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_tier_price` ENABLE KEYS */;


# Dumping structure for table jada.item_tier_price_currency
CREATE TABLE IF NOT EXISTS `item_tier_price_currency` (
  `item_tier_price_curr_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_price` float DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `item_tier_price_id` bigint(20) DEFAULT NULL,
  `site_currency_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`item_tier_price_curr_id`),
  KEY `FK1BD86F1896324A6D` (`site_currency_class_id`),
  KEY `FK1BD86F181218EA6B` (`item_tier_price_id`),
  CONSTRAINT `FK1BD86F181218EA6B` FOREIGN KEY (`item_tier_price_id`) REFERENCES `item_tier_price` (`item_tier_price_id`),
  CONSTRAINT `FK1BD86F1896324A6D` FOREIGN KEY (`site_currency_class_id`) REFERENCES `site_currency_class` (`site_currency_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_tier_price_currency: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_tier_price_currency` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_tier_price_currency` ENABLE KEYS */;


# Dumping structure for table jada.item_upsell
CREATE TABLE IF NOT EXISTS `item_upsell` (
  `item_id` bigint(20) NOT NULL,
  `item_upsell_id` bigint(20) NOT NULL,
  PRIMARY KEY (`item_id`,`item_upsell_id`),
  KEY `FKEC6B8F1971DEBAE5` (`item_id`),
  KEY `FKEC6B8F196C7EE11F` (`item_upsell_id`),
  CONSTRAINT `FKEC6B8F196C7EE11F` FOREIGN KEY (`item_upsell_id`) REFERENCES `item` (`item_id`),
  CONSTRAINT `FKEC6B8F1971DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.item_upsell: ~0 rows (approximately)
/*!40000 ALTER TABLE `item_upsell` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_upsell` ENABLE KEYS */;


# Dumping structure for table jada.language
CREATE TABLE IF NOT EXISTS `language` (
  `lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `google_translate_locale` varchar(10) NOT NULL,
  `lang_locale_country` varchar(2) NOT NULL,
  `lang_locale_language` varchar(2) NOT NULL,
  `lang_name` varchar(50) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `system_record` char(1) NOT NULL,
  PRIMARY KEY (`lang_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table jada.language: ~1 rows (approximately)
/*!40000 ALTER TABLE `language` DISABLE KEYS */;
INSERT INTO `language` (`lang_id`, `google_translate_locale`, `lang_locale_country`, `lang_locale_language`, `lang_name`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `system_record`) VALUES
	(1, 'en', 'CA', 'en', 'English', 'system', '2008-12-29 18:20:33', 'admin', '2009-12-22 16:25:17', 'Y');
/*!40000 ALTER TABLE `language` ENABLE KEYS */;


# Dumping structure for table jada.language_translation
CREATE TABLE IF NOT EXISTS `language_translation` (
  `lang_tran_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lang_source` char(1) NOT NULL,
  `lang_tran_key` varchar(50) NOT NULL,
  `lang_tran_value` varchar(255) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `lang_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`lang_tran_id`),
  KEY `FK1BCEA04A1C2A8CEF` (`lang_id`),
  CONSTRAINT `FK1BCEA04A1C2A8CEF` FOREIGN KEY (`lang_id`) REFERENCES `language` (`lang_id`)
) ENGINE=InnoDB AUTO_INCREMENT=717 DEFAULT CHARSET=utf8;

# Dumping data for table jada.language_translation: ~142 rows (approximately)
/*!40000 ALTER TABLE `language_translation` DISABLE KEYS */;
INSERT INTO `language_translation` (`lang_tran_id`, `lang_source`, `lang_tran_key`, `lang_tran_value`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `lang_id`) VALUES
	(480, 'S', 'content.menu.nolink', 'Menu currently not assigned with any content.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(481, 'S', 'content.error.string.required', 'Required', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(482, 'S', 'content.error.int.invalid', 'Invalid value', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(483, 'S', 'content.error.login.invalid', 'E-mail address and Password combination does not match any account on record.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(484, 'S', 'content.error.login.suspended', 'Sorry, your account has been suspended.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(485, 'S', 'content.error.email.duplicate', 'Your email address is already on file.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(486, 'S', 'content.error.email.nomatch', 'Email and Verification email do not match', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(487, 'S', 'content.error.password.nomatch', 'Password and Verification password do not match', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(488, 'S', 'content.error.password.invalidRule', 'Password must be between 8 to 12 characters and contains both alpha and numeric characters.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(489, 'S', 'content.error.publicName.duplicate', 'Public name is already in use.  Please pick another one.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(490, 'S', 'content.error.shippingLocation.unsupported', 'Sorry.  We are currently unable to ship or bill to your location.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(491, 'S', 'content.error.creditcard.invalid', 'You have entered an invalid credit card number.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(492, 'S', 'content.error.coupon.invalid', 'Sorry. We are unable to find this coupon.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(493, 'S', 'content.error.coupon.notApplicable', 'Sorry. This coupon is not applicable.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(494, 'S', 'content.error.coupon.notRegister', 'Sorry. This coupon is only valid for registered user.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(495, 'S', 'content.error.register.unable', 'Unable to register', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(496, 'S', 'content.text.language', 'Language', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(497, 'S', 'content.text.currency', 'Currency', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(498, 'S', 'content.text.search', 'Search', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(499, 'S', 'content.text.keywordSearch', 'Keyword Search', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(500, 'S', 'content.text.by', 'By', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(501, 'S', 'content.text.from', 'From', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(502, 'S', 'content.text.to', 'to', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(503, 'S', 'content.text.updated', 'Updated', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(504, 'S', 'content.text.contentComment.heading', 'Comments on this story', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(505, 'S', 'content.text.contentComment.link', 'Be first to comment on this article', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(506, 'S', 'content.text.more', 'more', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(507, 'S', 'content.text.outOfStock', 'Out of Stock', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(508, 'S', 'content.text.itemNumber', 'Item number', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(509, 'S', 'content.text.itemReview', 'Review on this item', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(510, 'S', 'content.text.itemComment.link', 'Be first to comment on this item', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(511, 'S', 'content.text.addToCart', 'Add to Cart', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(512, 'S', 'content.text.currentRating', 'Current Rating', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(513, 'S', 'content.text.highestRating', 'Highest', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(514, 'S', 'content.text.rateItem', 'Rate this item', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(515, 'S', 'content.text.specialPrice', 'Special', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(516, 'S', 'content.text.myCart', 'My Cart', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(517, 'S', 'content.text.itemsInCart', 'Items in your cart', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(518, 'S', 'content.text.quantity', 'Quantity', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(519, 'S', 'content.text.itemPrice', 'Item Price', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(520, 'S', 'content.text.itemTotal', 'Item Total', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(521, 'S', 'content.text.estimateTotal', 'Estimated total', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(522, 'S', 'content.text.contactUs', 'Contact Us', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(523, 'S', 'content.text.email', 'Email', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(524, 'S', 'content.text.phone', 'Phone', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(525, 'S', 'content.text.itemDetails', 'Item details', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(526, 'S', 'content.text.update', 'Update', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:18', 1),
	(527, 'S', 'content.text.subTotal', 'Sub-total', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(528, 'S', 'content.text.totalPrice', 'Total price', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(529, 'S', 'content.text.shippingHandling', 'Shipping and Handling', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(530, 'S', 'content.text.contineShopping', 'Continue Shopping', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(531, 'S', 'content.text.checkOutCreditCard', 'Check-out with Credit Card', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(532, 'S', 'content.text.signinNewUser', 'Sign-in / New User', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(533, 'S', 'content.text.reviewUser', 'Review User Information', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(534, 'S', 'content.text.reviewPurchase', 'Review Purchase', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(535, 'S', 'content.text.creditCardInformation', 'Credit Card Information', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(536, 'S', 'content.text.tranComplete', 'Transaction Complete', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(537, 'S', 'content.text.signIn', 'Sign-in', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(538, 'S', 'content.text.emailAddress', 'E-mail address', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(539, 'S', 'content.text.password', 'Password', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(540, 'S', 'content.text.newVisitorAccount', 'New visitors? Click here to create new account', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(541, 'S', 'content.text.cancelCheckout', 'Cancel Checkout', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(542, 'S', 'content.text.newUser', 'New User', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(543, 'S', 'content.text.emailAddressLoginName', 'Email address (login name)', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(544, 'S', 'content.text.reenterEmailAddress', 'Re-enter Email address', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(545, 'S', 'content.text.passwordMessage', 'Password is used to secure your account. Please make sure your new password is between 8 to 12 characters and contains both alpha and numeric characters.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(546, 'S', 'content.text.reenterPassword', 'Re-enter Password', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(547, 'S', 'content.text.registerUser', 'Register New User', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(548, 'S', 'content.text.forgotPassword', 'Forgot your password?', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(549, 'S', 'content.text.userInfoText', 'Please complete your information. Please be aware this information will be used to ship the products you purchased from us. You should also ensure all information is consistent with information on file at your financial institution.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(550, 'S', 'content.text.prefix', 'Prefix', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(551, 'S', 'content.text.firstName', 'First Name', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(552, 'S', 'content.text.middleName', 'Middle Name', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(553, 'S', 'content.text.lastName', 'Last Name', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(554, 'S', 'content.text.suffix', 'Suffix', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(555, 'S', 'content.text.address', 'Address', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(556, 'S', 'content.text.city', 'City', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(557, 'S', 'content.text.state', 'State/Province', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(558, 'S', 'content.text.country', 'Country', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(559, 'S', 'content.text.zipCode', 'Zip/Postal', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(560, 'S', 'content.text.fax', 'Fax', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(561, 'S', 'content.text.updateContinue', 'Update and Continue', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(562, 'S', 'content.text.pleaseSelect', 'Please select', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(563, 'S', 'content.text.payPal', 'PayPal', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(564, 'S', 'content.text.myAccount', 'My account', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(565, 'S', 'content.text.verifyPassword', 'Verify Password', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(566, 'S', 'content.text.shippingMethod', 'Shipping method', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(567, 'S', 'content.text.recalculate', 'Recalculate', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(568, 'S', 'content.text.confirmCheckout', 'Confirm Checkout', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(569, 'S', 'content.text.nameOnCard', 'Name as it appears on the card', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(570, 'S', 'content.text.creditCardType', 'Credit card type', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(571, 'S', 'content.text.cardNumber', 'Card number (no spaces)', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(572, 'S', 'content.text.expirationDate', 'Expiration date', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(573, 'S', 'content.text.month', 'Month', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(574, 'S', 'content.text.year', 'Year', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(575, 'S', 'content.text.cardCodeCCV', 'Card code verification number (CCV)', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(576, 'S', 'content.text.makePayment', 'Make Payment', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(577, 'S', 'content.text.tranCompleted', 'Thank You.  Transaction has been successfully completed.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(578, 'S', 'content.text.orderInformation', 'Order Information', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(579, 'S', 'content.text.orderNumber', 'Order number', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(580, 'S', 'content.text.date', 'Date', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(581, 'S', 'content.text.paymentType', 'Payment type', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(582, 'S', 'content.text.creditCard', 'Credit card', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(583, 'S', 'content.text.authorizationNumber', 'Authorization number', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(584, 'S', 'content.text.printView', 'print view', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(585, 'S', 'content.text.useBillingAddress', 'Using Billing Address', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(586, 'S', 'content.text.billingAddress', 'Billing Address', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(587, 'S', 'content.text.shippingAddress', 'Shipping Address', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(588, 'S', 'content.text.sameAsBilling', 'Same as Billing Address', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(589, 'S', 'content.text.useBilling', 'Use Billing Address', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(590, 'S', 'content.text.coupon', 'Coupon', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(591, 'S', 'content.text.applyCoupon', 'Apply Coupon', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(592, 'S', 'content.text.remove', 'Remove', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(593, 'S', 'content.text.updateShoppingCart', 'Update Shopping Cart', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(594, 'S', 'content.text.cart.subtotal', 'Cart sub-total', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(595, 'S', 'content.text.shippping', 'Shipping', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(596, 'S', 'content.text.shippingDiscount', 'Shipping Discount', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(597, 'S', 'content.text.relatedItems', 'Related items', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(598, 'S', 'content.text.upSellItems', 'You may also be interested in these items ', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(599, 'S', 'content.text.crossSellItems', 'You may also be interested in these items', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(600, 'S', 'content.text.pageNotFound', 'Page not found', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(601, 'S', 'content.text.itemCompare', 'Item Compare', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(602, 'S', 'content.text.static', 'Static', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(603, 'S', 'content.text.compareResult', 'Compare Result', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(604, 'S', 'content.text.relatedArticles', 'Related Articles', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(605, 'S', 'content.text.select', 'Select', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(606, 'S', 'content.text.makeSelection', 'Please make your selection.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(607, 'S', 'content.text.itemNotAvailable', 'Item is not available at this time.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(608, 'S', 'content.text.categories', 'Categories', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(609, 'S', 'content.text.page', 'Page', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(610, 'S', 'content.text.sortBy', 'Sort by', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(611, 'S', 'content.text.priceHighest', 'Price - from highest', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(612, 'S', 'content.text.priceLowest', 'Price - from lowest', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(613, 'S', 'content.text.description', 'Description', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(614, 'S', 'content.text.descriptionDescending', 'Description descending', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(615, 'S', 'content.text.agree', 'Agree', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(616, 'S', 'content.text.disagree', 'Disagree', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(617, 'S', 'content.text.moderation.approved', 'Moderator approved.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(618, 'S', 'content.text.moderation.inprocess', 'This posting is being moderated.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(619, 'S', 'content.text.moderation', 'Posting are moderated.', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(620, 'S', 'content.text.moderation.alert', 'Alert moderator', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(621, 'S', 'content.text.account.create', 'Create a new account', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(622, 'S', 'content.text.account.signin.existing', 'Sign-in using existing account', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(623, 'S', 'content.text.account.emailAddress', 'Email address', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(624, 'S', 'content.text.account.register', 'Register', 'admin', '2009-12-22 16:25:17', 'admin', '2009-12-22 16:25:17', 1),
	(625, 'S', 'content.text.compare.item', 'Items to compare', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(626, 'S', 'content.text.compare', 'Compare', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(627, 'S', 'content.text.compare.add', 'Add to compare', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(628, 'S', 'content.text.poll.message', 'This is not a scientific poll', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(629, 'S', 'content.text.comment.leave', 'Leave a comment', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(630, 'S', 'content.text.comment.title', 'Title', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(631, 'S', 'content.text.comments', 'Comments', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(632, 'S', 'content.text.comment.postby', 'Posted by', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(633, 'S', 'content.text.comment.post', 'Post comment', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(634, 'S', 'content.text.buy.notavailable', 'Item is not available at this time.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(635, 'S', 'content.text.buy.select', 'Please make your selection.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(636, 'S', 'content.text.item.product', 'Product description', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(637, 'S', 'content.text.item.moreinfo', 'More information', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(638, 'S', 'content.text.item.interested', 'You may also be interested in', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(639, 'S', 'content.text.item.together', 'Frequently brought together', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(640, 'S', 'content.text.item.rating', 'Rating', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(641, 'S', 'content.text.item.viewmore', 'View more', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(642, 'S', 'content.text.item.reviews', 'Reviews', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(643, 'S', 'content.text.item.leavereview', 'Leave a review', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(644, 'S', 'content.text.item.rateitem', 'Rate this item', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(645, 'S', 'content.text.item.postcomment', 'Post comment', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(646, 'S', 'content.text.item.mostViewed', 'Most viewed articles', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(647, 'S', 'content.text.item.topRated', 'Top rated articles', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(648, 'S', 'content.text.item.mostPopular', 'Most popular items', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(649, 'S', 'content.text.search.result', 'Result for', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(650, 'S', 'content.text.search.matches', 'matches', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(651, 'S', 'content.text.search.first', 'First', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(652, 'S', 'content.text.search.previous', 'Previous', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(653, 'S', 'content.text.search.next', 'Next', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(654, 'S', 'content.text.search.last', 'Last', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(655, 'S', 'content.text.cart.view', 'View your cart', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(656, 'S', 'content.text.ordertotal', 'Order total', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(657, 'S', 'content.text.poll', 'Quick poll', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(658, 'S', 'content.text.poll.vote', 'Vote', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(659, 'S', 'content.text.poll.viewresult', 'or see results', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(660, 'S', 'content.text.news', 'News feed', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(661, 'S', 'content.text.information.updated', 'Information updated successfully.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(662, 'S', 'content.text.on', 'on', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(663, 'S', 'content.text.or', 'or', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(664, 'S', 'content.text.edit', 'edit', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(665, 'S', 'content.text.continue', 'Continue', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(666, 'S', 'content.text.selectall', 'Select all', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(667, 'S', 'content.text.item.not.available', 'Item is not available at this time.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(668, 'S', 'content.text.item.attribute.select', 'Please make your selection.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(669, 'S', 'content.text.checkout.myInformation', 'My information', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(670, 'S', 'content.text.checkout.billingInformation', 'Billing information', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(671, 'S', 'content.text.checkout.shippingInformation', 'Shipping information', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(672, 'S', 'content.text.checkout.myInformation.same', 'Same as my information', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(673, 'S', 'content.text.checkout.address.choose', 'Choose a different address', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(674, 'S', 'content.text.checkout.billingInformation.same', 'Same as billing information', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(675, 'S', 'content.text.myaccount.signin.message', 'To sign in, please enter your email address and password below.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(676, 'S', 'content.text.myaccount.logout.successful', 'You have successfully signed out from the existing session.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(677, 'S', 'content.text.myaccount.forgot.successful', 'Your password has been successfully sent to your email address.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(678, 'S', 'content.text.myaccount.forgot.message', 'Please enter your email address below and clicks continue.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(679, 'S', 'content.text.myaccount.forgot.invalid', 'This e-mail address does not exist in the database.  You can create new account during checkout.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(680, 'S', 'content.text.myaccount.mycart', 'My Cart - view my current shopping cart order', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(681, 'S', 'content.text.myaccount.signout', 'Sign-out', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(682, 'S', 'content.text.myaccount.setting', 'My account settings', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(683, 'S', 'content.text.myaccount.menu.email', 'Email and password', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(684, 'S', 'content.text.myaccount.menu.address', 'Address information', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(685, 'S', 'content.text.myaccount.menu.orders', 'My orders', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(686, 'S', 'content.text.myaccount.menu.payment', 'Payment information', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(687, 'S', 'content.text.myaccount.menu.orderStatus', 'Order Status - track and view status of current orders', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(688, 'S', 'content.text.myaccount.menu.orderHistory', 'Order History - view history of past orders', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(689, 'S', 'content.text.myaccount.identity.email', 'Email address (login name)', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(690, 'S', 'content.text.myaccount.identity.email.desc', 'If you wish to change your email address or password, you may do so here.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(691, 'S', 'content.text.myaccount.identity.publicName', 'Public name', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(692, 'S', 'content.text.myaccount.identity.publicName.desc', 'Name that will be known to public', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(693, 'S', 'content.text.myaccount.identity.password', 'Password', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(694, 'S', 'content.text.myaccount.identity.password.desc', 'Password is used to secure your account.  Please make sure your new password is between 8 to 12 characters and contains both alpha and numeric characters.', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(695, 'S', 'content.text.myaccount.identity.reenterpassword', 'Re-enter password', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(696, 'S', 'content.text.myaccount.payment.message', 'Enter or update the following fields to speed the checkout process.  For billing please ensure all information is consistent with information on file at your financial institution. ', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(697, 'S', 'content.text.myaccount.payment.name', 'Name as it appears on card', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(698, 'S', 'content.text.myaccount.payment.type', 'Credit card type', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(699, 'S', 'content.text.myaccount.payment.cardnum', 'Card number (no spaces)', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(700, 'S', 'content.text.myaccount.payment.expiry', 'Expiration date', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(701, 'S', 'content.text.myaccount.payment.ccv', 'Card Code Verification Number (CCV)', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(702, 'S', 'content.text.myaccount.order.message', 'Track and view status of recent and pending orders. ', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(703, 'S', 'content.text.myaccount.order.date', 'Order date', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(704, 'S', 'content.text.myaccount.order.number', 'Order number', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(705, 'S', 'content.text.myaccount.order.status', 'Status', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(706, 'S', 'content.text.myaccount.order.shipdate', 'Ship date', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(707, 'S', 'content.text.myaccount.order.total', 'Total', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(708, 'S', 'content.text.order.status.O', 'Open', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(709, 'S', 'content.text.order.status.C', 'Closed', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(710, 'S', 'content.text.order.status.S', 'Shipped', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(711, 'S', 'content.text.order.status.V', 'Void', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(712, 'S', 'content.text.credit.status.O', 'Open', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(713, 'S', 'content.text.credit.status.C', 'Closed', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(714, 'S', 'content.text.credit.status.V', 'Void', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(715, 'S', 'content.text.myaccount.payment', 'Payment', 'admin', '2009-12-22 16:25:18', 'admin', '2009-12-22 16:25:18', 1),
	(716, 'S', 'content.text.checkOutCash', 'Cash On Delivery', 'admin', '2010-01-06 20:51:48', 'admin', '2010-01-06 20:51:48', 1);
/*!40000 ALTER TABLE `language_translation` ENABLE KEYS */;


# Dumping structure for table jada.menu
CREATE TABLE IF NOT EXISTS `menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_set_name` varchar(20) NOT NULL,
  `menu_type` varchar(5) NOT NULL,
  `menu_url` varchar(255) NOT NULL,
  `menu_window_mode` varchar(255) NOT NULL,
  `menu_window_target` varchar(20) NOT NULL,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `cat_id` bigint(20) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `menu_lang_id` bigint(20) DEFAULT NULL,
  `menu_parent_id` bigint(20) DEFAULT NULL,
  `site_domain_id` bigint(20) NOT NULL,
  PRIMARY KEY (`menu_id`),
  KEY `FK33155F71DEBAE5` (`item_id`),
  KEY `FK33155F6DA0AD2F` (`content_id`),
  KEY `FK33155FB660715A` (`menu_parent_id`),
  KEY `FK33155F30F21FAD` (`cat_id`),
  KEY `FK33155FB8784434` (`site_domain_id`),
  KEY `FK33155FA71DE70E` (`menu_lang_id`),
  CONSTRAINT `FK33155FA71DE70E` FOREIGN KEY (`menu_lang_id`) REFERENCES `menu_language` (`menu_lang_id`),
  CONSTRAINT `FK33155F30F21FAD` FOREIGN KEY (`cat_id`) REFERENCES `category` (`cat_id`),
  CONSTRAINT `FK33155F6DA0AD2F` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`),
  CONSTRAINT `FK33155F71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`),
  CONSTRAINT `FK33155FB660715A` FOREIGN KEY (`menu_parent_id`) REFERENCES `menu` (`menu_id`),
  CONSTRAINT `FK33155FB8784434` FOREIGN KEY (`site_domain_id`) REFERENCES `site_domain` (`site_domain_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.menu: ~2 rows (approximately)
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` (`menu_id`, `menu_set_name`, `menu_type`, `menu_url`, `menu_window_mode`, `menu_window_target`, `published`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `seq_num`, `cat_id`, `content_id`, `item_id`, `menu_lang_id`, `menu_parent_id`, `site_domain_id`) VALUES
	(1, 'MAIN', '', '', '', '', 'Y', 'admin', '2009-08-10 20:58:18', 'admin', '2009-08-10 20:58:10', 0, NULL, NULL, NULL, 1, NULL, 2),
	(2, 'SECONDARY', '', '', '', '', 'Y', 'admin', '2009-08-10 21:00:47', 'admin', '2009-08-10 21:00:41', 0, NULL, NULL, NULL, 1, NULL, 2);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;


# Dumping structure for table jada.menu_language
CREATE TABLE IF NOT EXISTS `menu_language` (
  `menu_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(40) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `menu_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`menu_lang_id`),
  KEY `FKDBED4A382C630765` (`menu_id`),
  KEY `FKDBED4A38473E64D1` (`site_profile_class_id`),
  CONSTRAINT `FKDBED4A38473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FKDBED4A382C630765` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.menu_language: ~2 rows (approximately)
/*!40000 ALTER TABLE `menu_language` DISABLE KEYS */;
INSERT INTO `menu_language` (`menu_lang_id`, `menu_name`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `menu_id`, `site_profile_class_id`) VALUES
	(1, '', 'admin', '2009-08-10 21:23:11', 'admin', '2009-08-10 21:23:05', 1, 2),
	(2, '', 'admin', '2009-08-10 21:23:56', 'admin', '2009-08-10 21:23:50', 2, 2);
/*!40000 ALTER TABLE `menu_language` ENABLE KEYS */;


# Dumping structure for table jada.order_address
CREATE TABLE IF NOT EXISTS `order_address` (
  `order_address_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cust_address_line1` varchar(30) DEFAULT NULL,
  `cust_address_line2` varchar(30) DEFAULT NULL,
  `cust_city_name` varchar(25) DEFAULT NULL,
  `cust_country_code` varchar(2) DEFAULT NULL,
  `cust_country_name` varchar(40) DEFAULT NULL,
  `cust_fax_num` varchar(15) DEFAULT NULL,
  `cust_first_name` varchar(20) DEFAULT NULL,
  `cust_last_name` varchar(20) DEFAULT NULL,
  `cust_middle_name` varchar(20) DEFAULT NULL,
  `cust_phone_num` varchar(15) DEFAULT NULL,
  `cust_prefix` varchar(20) DEFAULT NULL,
  `cust_state_code` varchar(2) DEFAULT NULL,
  `cust_state_name` varchar(40) DEFAULT NULL,
  `cust_suffix` varchar(20) DEFAULT NULL,
  `cust_use_address` varchar(1) DEFAULT NULL,
  `cust_zip_code` varchar(10) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `country_id` bigint(20) DEFAULT NULL,
  `state_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_address_id`),
  KEY `FKB0DB9C03D2E3176F` (`state_id`),
  KEY `FKB0DB9C032196FC0F` (`country_id`),
  CONSTRAINT `FKB0DB9C032196FC0F` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`),
  CONSTRAINT `FKB0DB9C03D2E3176F` FOREIGN KEY (`state_id`) REFERENCES `state` (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.order_address: ~0 rows (approximately)
/*!40000 ALTER TABLE `order_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_address` ENABLE KEYS */;


# Dumping structure for table jada.order_attrib_detail
CREATE TABLE IF NOT EXISTS `order_attrib_detail` (
  `order_attrib_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_attrib_value` varchar(40) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `custom_attrib_detail_id` bigint(20) DEFAULT NULL,
  `custom_attrib_option_id` bigint(20) DEFAULT NULL,
  `order_item_detail_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_attrib_detail_id`),
  KEY `FKE5A08356B5C3CD3` (`custom_attrib_option_id`),
  KEY `FKE5A08357461DF53` (`custom_attrib_detail_id`),
  KEY `FKE5A083547EC5AF5` (`order_item_detail_id`),
  CONSTRAINT `FKE5A083547EC5AF5` FOREIGN KEY (`order_item_detail_id`) REFERENCES `order_item_detail` (`order_item_detail_id`),
  CONSTRAINT `FKE5A08356B5C3CD3` FOREIGN KEY (`custom_attrib_option_id`) REFERENCES `custom_attrib_option` (`custom_attrib_option_id`),
  CONSTRAINT `FKE5A08357461DF53` FOREIGN KEY (`custom_attrib_detail_id`) REFERENCES `custom_attrib_detail` (`custom_attrib_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.order_attrib_detail: ~0 rows (approximately)
/*!40000 ALTER TABLE `order_attrib_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_attrib_detail` ENABLE KEYS */;


# Dumping structure for table jada.order_detail_tax
CREATE TABLE IF NOT EXISTS `order_detail_tax` (
  `order_detail_tax_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `tax_amount` float NOT NULL,
  `tax_name` varchar(40) NOT NULL,
  `order_header_id` bigint(20) DEFAULT NULL,
  `order_item_detail_id` bigint(20) DEFAULT NULL,
  `tax_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_detail_tax_id`),
  KEY `FK9683484E267D6B6C` (`order_header_id`),
  KEY `FK9683484E47EC5AF5` (`order_item_detail_id`),
  KEY `FK9683484EC2D9DA6F` (`tax_id`),
  CONSTRAINT `FK9683484EC2D9DA6F` FOREIGN KEY (`tax_id`) REFERENCES `tax` (`tax_id`),
  CONSTRAINT `FK9683484E267D6B6C` FOREIGN KEY (`order_header_id`) REFERENCES `order_header` (`order_header_id`),
  CONSTRAINT `FK9683484E47EC5AF5` FOREIGN KEY (`order_item_detail_id`) REFERENCES `order_item_detail` (`order_item_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.order_detail_tax: ~0 rows (approximately)
/*!40000 ALTER TABLE `order_detail_tax` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_detail_tax` ENABLE KEYS */;


# Dumping structure for table jada.order_header
CREATE TABLE IF NOT EXISTS `order_header` (
  `order_header_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creditcard_desc` varchar(40) DEFAULT NULL,
  `cust_creditcard_num` varchar(192) DEFAULT NULL,
  `cust_email` varchar(255) NOT NULL,
  `order_date` datetime NOT NULL,
  `order_num` varchar(20) NOT NULL,
  `order_abundant_loc` varchar(10) DEFAULT NULL,
  `shipping_valid_until` datetime DEFAULT NULL,
  `shipping_pick_up` varchar(1) NOT NULL,
  `order_status` varchar(1) NOT NULL,
  `order_total` float NOT NULL,
  `payment_gateway` varchar(20) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `shipping_discount_total` float NOT NULL,
  `shipping_method_name` varchar(40) DEFAULT NULL,
  `shipping_total` float NOT NULL,
  `billing_address_id` bigint(20) DEFAULT NULL,
  `cust_address_id` bigint(20) DEFAULT NULL,
  `cust_id` bigint(20) DEFAULT NULL,
  `payment_tran_id` bigint(20) DEFAULT NULL,
  `shipping_address_id` bigint(20) DEFAULT NULL,
  `shipping_method_id` bigint(20) DEFAULT NULL,
  `site_currency_id` bigint(20) DEFAULT NULL,
  `site_domain_id` bigint(20) NOT NULL,
  `site_profile_id` bigint(20) DEFAULT NULL,
  `void_payment_tran_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_header_id`),
  KEY `FK2A79249EB7B46AB1` (`void_payment_tran_id`),
  KEY `FK2A79249EADC3D462` (`shipping_method_id`),
  KEY `FK2A79249EDEFE8B60` (`site_profile_id`),
  KEY `FK2A79249E90E6448` (`shipping_address_id`),
  KEY `FK2A79249EEEE96B5C` (`payment_tran_id`),
  KEY `FK2A79249EB8784434` (`site_domain_id`),
  KEY `FK2A79249E1C640D5B` (`billing_address_id`),
  KEY `FK2A79249E17991283` (`cust_address_id`),
  KEY `FK2A79249E77370E94` (`site_currency_id`),
  KEY `FK2A79249EE6CEAEF0` (`cust_id`),
  CONSTRAINT `FK2A79249E17991283` FOREIGN KEY (`cust_address_id`) REFERENCES `order_address` (`order_address_id`),
  CONSTRAINT `FK2A79249E1C640D5B` FOREIGN KEY (`billing_address_id`) REFERENCES `order_address` (`order_address_id`),
  CONSTRAINT `FK2A79249E77370E94` FOREIGN KEY (`site_currency_id`) REFERENCES `site_currency` (`site_currency_id`),
  CONSTRAINT `FK2A79249E90E6448` FOREIGN KEY (`shipping_address_id`) REFERENCES `order_address` (`order_address_id`),
  CONSTRAINT `FK2A79249EADC3D462` FOREIGN KEY (`shipping_method_id`) REFERENCES `shipping_method` (`shipping_method_id`),
  CONSTRAINT `FK2A79249EB7B46AB1` FOREIGN KEY (`void_payment_tran_id`) REFERENCES `payment_tran` (`payment_tran_id`),
  CONSTRAINT `FK2A79249EB8784434` FOREIGN KEY (`site_domain_id`) REFERENCES `site_domain` (`site_domain_id`),
  CONSTRAINT `FK2A79249EDEFE8B60` FOREIGN KEY (`site_profile_id`) REFERENCES `site_profile` (`site_profile_id`),
  CONSTRAINT `FK2A79249EE6CEAEF0` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`cust_id`),
  CONSTRAINT `FK2A79249EEEE96B5C` FOREIGN KEY (`payment_tran_id`) REFERENCES `payment_tran` (`payment_tran_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.order_header: ~0 rows (approximately)
/*!40000 ALTER TABLE `order_header` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_header` ENABLE KEYS */;


# Dumping structure for table jada.order_item_detail
CREATE TABLE IF NOT EXISTS `order_item_detail` (
  `order_item_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `iitem_detail_amount` float NOT NULL,
  `item_detail_discount_amount` float NOT NULL,
  `item_num` varchar(20) NOT NULL,
  `item_order_qty` int(11) NOT NULL,
  `item_short_desc` varchar(80) NOT NULL,
  `item_tier_price` float DEFAULT NULL,
  `item_tier_qty` int(11) DEFAULT NULL,
  `item_upc_cd` varchar(20) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `order_header_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_item_detail_id`),
  KEY `FK82E5EF8C71DEBAE5` (`item_id`),
  KEY `FK82E5EF8C267D6B6C` (`order_header_id`),
  CONSTRAINT `FK82E5EF8C267D6B6C` FOREIGN KEY (`order_header_id`) REFERENCES `order_header` (`order_header_id`),
  CONSTRAINT `FK82E5EF8C71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.order_item_detail: ~0 rows (approximately)
/*!40000 ALTER TABLE `order_item_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_item_detail` ENABLE KEYS */;


# Dumping structure for table jada.order_other_detail
CREATE TABLE IF NOT EXISTS `order_other_detail` (
  `order_other_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_other_detail_amount` float DEFAULT NULL,
  `order_other_detail_desc` varchar(80) NOT NULL,
  `order_other_detail_num` varchar(20) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) DEFAULT NULL,
  `coupon_id` bigint(20) DEFAULT NULL,
  `order_header_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_other_detail_id`),
  KEY `FKED7235B13E04E745` (`coupon_id`),
  KEY `FKED7235B1267D6B6C` (`order_header_id`),
  CONSTRAINT `FKED7235B1267D6B6C` FOREIGN KEY (`order_header_id`) REFERENCES `order_header` (`order_header_id`),
  CONSTRAINT `FKED7235B13E04E745` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.order_other_detail: ~0 rows (approximately)
/*!40000 ALTER TABLE `order_other_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_other_detail` ENABLE KEYS */;


# Dumping structure for table jada.order_tracking
CREATE TABLE IF NOT EXISTS `order_tracking` (
  `order_tracking_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_tracking_code` varchar(3) NOT NULL,
  `order_tracking_internal` varchar(1) NOT NULL,
  `order_tracking_message` varchar(255) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `credit_header_id` bigint(20) DEFAULT NULL,
  `invoice_header_id` bigint(20) DEFAULT NULL,
  `order_header_id` bigint(20) DEFAULT NULL,
  `ship_header_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_tracking_id`),
  KEY `FKFEF62088267D6B6C` (`order_header_id`),
  KEY `FKFEF62088E8BE3F7E` (`ship_header_id`),
  KEY `FKFEF6208862F25038` (`credit_header_id`),
  KEY `FKFEF62088E5DAA3EA` (`invoice_header_id`),
  CONSTRAINT `FKFEF62088E5DAA3EA` FOREIGN KEY (`invoice_header_id`) REFERENCES `invoice_header` (`invoice_header_id`),
  CONSTRAINT `FKFEF62088267D6B6C` FOREIGN KEY (`order_header_id`) REFERENCES `order_header` (`order_header_id`),
  CONSTRAINT `FKFEF6208862F25038` FOREIGN KEY (`credit_header_id`) REFERENCES `credit_header` (`credit_header_id`),
  CONSTRAINT `FKFEF62088E8BE3F7E` FOREIGN KEY (`ship_header_id`) REFERENCES `ship_header` (`ship_header_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.order_tracking: ~0 rows (approximately)
/*!40000 ALTER TABLE `order_tracking` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_tracking` ENABLE KEYS */;


# Dumping structure for table jada.payment_gateway
CREATE TABLE IF NOT EXISTS `payment_gateway` (
  `payment_gateway_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `payment_gateway_firm` longtext,
  `payment_gateway_name` varchar(40) NOT NULL,
  `payment_gateway_provider` varchar(40) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `currency_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`payment_gateway_id`),
  UNIQUE KEY `payment_gateway_name` (`payment_gateway_name`,`site_id`),
  KEY `FK4DAEB90BC4B5B4A5` (`currency_id`),
  KEY `FK4DAEB90B70EBDE65` (`site_id`),
  CONSTRAINT `FK4DAEB90B70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK4DAEB90BC4B5B4A5` FOREIGN KEY (`currency_id`) REFERENCES `currency` (`currency_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.payment_gateway: ~0 rows (approximately)
/*!40000 ALTER TABLE `payment_gateway` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_gateway` ENABLE KEYS */;


# Dumping structure for table jada.payment_tran
CREATE TABLE IF NOT EXISTS `payment_tran` (
  `payment_tran_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `auth_code` varchar(30) NOT NULL,
  `payment_reference1` varchar(30) DEFAULT NULL,
  `payment_reference2` varchar(30) DEFAULT NULL,
  `payment_reference3` varchar(30) DEFAULT NULL,
  `payment_reference4` varchar(30) DEFAULT NULL,
  `payment_reference5` varchar(30) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `tran_datetime` datetime NOT NULL,
  PRIMARY KEY (`payment_tran_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.payment_tran: ~0 rows (approximately)
/*!40000 ALTER TABLE `payment_tran` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_tran` ENABLE KEYS */;


# Dumping structure for table jada.poll_detail
CREATE TABLE IF NOT EXISTS `poll_detail` (
  `poll_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `poll_option` varchar(80) NOT NULL,
  `poll_vote_count` int(11) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `poll_header_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`poll_detail_id`),
  KEY `FKC19E18D121498484` (`poll_header_id`),
  CONSTRAINT `FKC19E18D121498484` FOREIGN KEY (`poll_header_id`) REFERENCES `poll_header` (`poll_header_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.poll_detail: ~0 rows (approximately)
/*!40000 ALTER TABLE `poll_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `poll_detail` ENABLE KEYS */;


# Dumping structure for table jada.poll_header
CREATE TABLE IF NOT EXISTS `poll_header` (
  `poll_header_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `poll_expire_on` date NOT NULL,
  `poll_publish_on` date NOT NULL,
  `poll_topic` varchar(255) NOT NULL,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  PRIMARY KEY (`poll_header_id`),
  KEY `FKC868E30D70EBDE65` (`site_id`),
  CONSTRAINT `FKC868E30D70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.poll_header: ~0 rows (approximately)
/*!40000 ALTER TABLE `poll_header` DISABLE KEYS */;
/*!40000 ALTER TABLE `poll_header` ENABLE KEYS */;


# Dumping structure for table jada.product_class
CREATE TABLE IF NOT EXISTS `product_class` (
  `product_class_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_class_name` varchar(20) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `system_record` char(1) NOT NULL,
  `site_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`product_class_id`),
  KEY `FK51B82A2870EBDE65` (`site_id`),
  CONSTRAINT `FK51B82A2870EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.product_class: ~2 rows (approximately)
/*!40000 ALTER TABLE `product_class` DISABLE KEYS */;
INSERT INTO `product_class` (`product_class_id`, `product_class_name`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `system_record`, `site_id`) VALUES
	(1, 'Regular', 'system', '2009-06-11 10:18:04', 'system', '2009-06-11 10:17:59', 'Y', '_system'),
	(2, 'Regular', 'system', '2009-06-11 10:18:27', 'system', '2009-06-11 10:18:21', 'Y', 'default');
/*!40000 ALTER TABLE `product_class` ENABLE KEYS */;


# Dumping structure for table jada.report
CREATE TABLE IF NOT EXISTS `report` (
  `report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `last_run_by` varchar(20) DEFAULT NULL,
  `last_run_datetime` datetime DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `report_desc` varchar(255) NOT NULL,
  `report_name` varchar(40) NOT NULL,
  `report_text` longtext NOT NULL,
  `system_record` char(1) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  PRIMARY KEY (`report_id`),
  KEY `FK91B1415470EBDE65` (`site_id`),
  CONSTRAINT `FK91B1415470EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.report: ~1 rows (approximately)
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
INSERT INTO `report` (`report_id`, `last_run_by`, `last_run_datetime`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `report_desc`, `report_name`, `report_text`, `system_record`, `site_id`) VALUES
	(1, NULL, NULL, 'admin', '2011-03-02 00:00:00', 'admin', '2011-03-02 00:00:00', 'Orders by sub-sites', 'Orders', '<?xml version="1.0" encoding="UTF-8"?>\r\n<report xmlns="http://www.eclipse.org/birt/2005/design" version="3.2.21" id="1">\r\n    <property name="createdBy">Eclipse BIRT Designer Version 2.5.2.v20100208 Build &lt;2.5.2.v20100210-0630></property>\r\n    <property name="units">in</property>\r\n    <list-property name="propertyBindings">\r\n        <structure>\r\n            <property name="name">queryText</property>\r\n            <property name="id">433</property>\r\n        </structure>\r\n        <structure>\r\n            <property name="name">queryTimeOut</property>\r\n            <property name="id">433</property>\r\n        </structure>\r\n        <structure>\r\n            <property name="name">queryText</property>\r\n            <property name="id">146</property>\r\n        </structure>\r\n        <structure>\r\n            <property name="name">queryTimeOut</property>\r\n            <property name="id">146</property>\r\n        </structure>\r\n    </list-property>\r\n    <property name="iconFile">/templates/simple_listing.gif</property>\r\n    <property name="cheatSheet">org.eclipse.birt.report.designer.ui.cheatsheet.simplelisting</property>\r\n    <property name="layoutPreference">fixed layout</property>\r\n    <property name="bidiLayoutOrientation">ltr</property>\r\n    <parameters>\r\n        <scalar-parameter name="pSiteId" id="332">\r\n            <text-property name="promptText">Select Site to report</text-property>\r\n            <property name="valueType">dynamic</property>\r\n            <property name="isRequired">true</property>\r\n            <property name="dataSetName">Sites</property>\r\n            <expression name="valueExpr">dataSetRow["site_id"]</expression>\r\n            <expression name="sortByColumn">dataSetRow["site_id"]</expression>\r\n            <property name="sortDirection">asc</property>\r\n            <property name="dataType">string</property>\r\n            <property name="distinct">true</property>\r\n            <simple-property-list name="defaultValue">\r\n                <value type="constant">default</value>\r\n            </simple-property-list>\r\n            <property name="paramType">simple</property>\r\n            <property name="concealValue">false</property>\r\n            <property name="controlType">list-box</property>\r\n            <property name="mustMatch">true</property>\r\n            <property name="fixedOrder">false</property>\r\n            <property name="autoSuggestThreshold">1</property>\r\n            <structure name="format">\r\n                <property name="category">Unformatted</property>\r\n            </structure>\r\n        </scalar-parameter>\r\n        <scalar-parameter name="pOrderDateStart" id="31">\r\n            <text-property name="promptText">Enter Start Order Date</text-property>\r\n            <property name="valueType">static</property>\r\n            <property name="dataType">date</property>\r\n            <property name="distinct">true</property>\r\n            <simple-property-list name="defaultValue">\r\n                <value type="javascript">new Date()</value>\r\n            </simple-property-list>\r\n            <property name="paramType">simple</property>\r\n            <property name="concealValue">false</property>\r\n            <property name="controlType">text-box</property>\r\n            <structure name="format">\r\n                <property name="category">Unformatted</property>\r\n            </structure>\r\n        </scalar-parameter>\r\n        <scalar-parameter name="pOrderDateEnd" id="434">\r\n            <text-property name="promptText">Enter End Order Date</text-property>\r\n            <property name="valueType">static</property>\r\n            <property name="dataType">date</property>\r\n            <property name="distinct">true</property>\r\n            <simple-property-list name="defaultValue">\r\n                <value type="javascript">new Date()</value>\r\n            </simple-property-list>\r\n            <property name="paramType">simple</property>\r\n            <property name="concealValue">false</property>\r\n            <property name="controlType">text-box</property>\r\n            <structure name="format">\r\n                <property name="category">Unformatted</property>\r\n            </structure>\r\n        </scalar-parameter>\r\n    </parameters>\r\n    <data-sources>\r\n        <oda-data-source extensionID="org.eclipse.birt.report.data.oda.jdbc" name="Onlinestore" id="27">\r\n            <list-property name="privateDriverProperties">\r\n                <ex-property>\r\n                    <name>contentBidiFormatStr</name>\r\n                    <value>ILYNN</value>\r\n                </ex-property>\r\n                <ex-property>\r\n                    <name>metadataBidiFormatStr</name>\r\n                    <value>ILYNN</value>\r\n                </ex-property>\r\n            </list-property>\r\n            <property name="odaDriverClass">com.mysql.jdbc.Driver</property>\r\n            <property name="odaURL">jdbc:mysql://localhost/jadademo1</property>\r\n            <property name="odaUser">root</property>\r\n            <encrypted-property name="odaPassword" encryptionID="base64">c3lzdGVt</encrypted-property>\r\n        </oda-data-source>\r\n    </data-sources>\r\n    <data-sets>\r\n        <oda-data-set extensionID="org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet" name="SiteOrders" id="146">\r\n            <list-property name="columnHints">\r\n                <structure>\r\n                    <property name="columnName">site_id</property>\r\n                    <text-property name="displayName">site_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_name</property>\r\n                    <text-property name="displayName">site_name</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">order_header_id</property>\r\n                    <text-property name="displayName">order_header_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">creditcard_desc</property>\r\n                    <text-property name="displayName">creditcard_desc</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">cust_creditcard_num</property>\r\n                    <text-property name="displayName">cust_creditcard_num</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">cust_email</property>\r\n                    <text-property name="displayName">cust_email</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">order_date</property>\r\n                    <text-property name="displayName">order_date</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">order_num</property>\r\n                    <text-property name="displayName">order_num</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">order_status</property>\r\n                    <text-property name="displayName">order_status</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">order_total</property>\r\n                    <text-property name="displayName">order_total</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">payment_gateway</property>\r\n                    <text-property name="displayName">payment_gateway</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">rec_create_by</property>\r\n                    <text-property name="displayName">rec_create_by</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">rec_create_datetime</property>\r\n                    <text-property name="displayName">rec_create_datetime</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">rec_update_by</property>\r\n                    <text-property name="displayName">rec_update_by</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">rec_update_datetime</property>\r\n                    <text-property name="displayName">rec_update_datetime</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">shipping_discount_total</property>\r\n                    <text-property name="displayName">shipping_discount_total</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">shipping_method_name</property>\r\n                    <text-property name="displayName">shipping_method_name</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">shipping_total</property>\r\n                    <text-property name="displayName">shipping_total</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">billing_address_id</property>\r\n                    <text-property name="displayName">billing_address_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">cust_address_id</property>\r\n                    <text-property name="displayName">cust_address_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">cust_id</property>\r\n                    <text-property name="displayName">cust_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">payment_tran_id</property>\r\n                    <text-property name="displayName">payment_tran_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">shipping_address_id</property>\r\n                    <text-property name="displayName">shipping_address_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">shipping_method_id</property>\r\n                    <text-property name="displayName">shipping_method_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_currency_id</property>\r\n                    <text-property name="displayName">site_currency_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_domain_id</property>\r\n                    <text-property name="displayName">site_domain_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_profile_id</property>\r\n                    <text-property name="displayName">site_profile_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">void_payment_tran_id</property>\r\n                    <text-property name="displayName">void_payment_tran_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">cust_first_name</property>\r\n                    <text-property name="displayName">cust_first_name</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">cust_last_name</property>\r\n                    <text-property name="displayName">cust_last_name</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_currency_class_name</property>\r\n                    <text-property name="displayName">site_currency_class_name</text-property>\r\n                </structure>\r\n            </list-property>\r\n            <list-property name="parameters">\r\n                <structure>\r\n                    <property name="name">param_1</property>\r\n                    <property name="paramName">pSiteId</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="position">1</property>\r\n                    <expression name="defaultValue" type="javascript">Electronics</expression>\r\n                    <property name="isInput">true</property>\r\n                    <property name="isOutput">false</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">param_2</property>\r\n                    <property name="paramName">pOrderDateStart</property>\r\n                    <property name="dataType">date</property>\r\n                    <property name="position">2</property>\r\n                    <expression name="defaultValue" type="javascript">new Date()</expression>\r\n                    <property name="isInput">true</property>\r\n                    <property name="isOutput">false</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">param_3</property>\r\n                    <property name="paramName">pOrderDateEnd</property>\r\n                    <property name="dataType">date</property>\r\n                    <property name="position">3</property>\r\n                    <property name="isInput">true</property>\r\n                    <property name="isOutput">false</property>\r\n                </structure>\r\n            </list-property>\r\n            <structure name="cachedMetaData">\r\n                <list-property name="resultSet">\r\n                    <structure>\r\n                        <property name="position">1</property>\r\n                        <property name="name">site_id</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">2</property>\r\n                        <property name="name">site_name</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">3</property>\r\n                        <property name="name">order_header_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">4</property>\r\n                        <property name="name">creditcard_desc</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">5</property>\r\n                        <property name="name">cust_creditcard_num</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">6</property>\r\n                        <property name="name">cust_email</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">7</property>\r\n                        <property name="name">order_date</property>\r\n                        <property name="dataType">date-time</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">8</property>\r\n                        <property name="name">order_num</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">9</property>\r\n                        <property name="name">order_status</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">10</property>\r\n                        <property name="name">order_total</property>\r\n                        <property name="dataType">float</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">11</property>\r\n                        <property name="name">payment_gateway</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">12</property>\r\n                        <property name="name">rec_create_by</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">13</property>\r\n                        <property name="name">rec_create_datetime</property>\r\n                        <property name="dataType">date-time</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">14</property>\r\n                        <property name="name">rec_update_by</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">15</property>\r\n                        <property name="name">rec_update_datetime</property>\r\n                        <property name="dataType">date-time</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">16</property>\r\n                        <property name="name">shipping_discount_total</property>\r\n                        <property name="dataType">float</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">17</property>\r\n                        <property name="name">shipping_method_name</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">18</property>\r\n                        <property name="name">shipping_total</property>\r\n                        <property name="dataType">float</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">19</property>\r\n                        <property name="name">billing_address_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">20</property>\r\n                        <property name="name">cust_address_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">21</property>\r\n                        <property name="name">cust_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">22</property>\r\n                        <property name="name">payment_tran_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">23</property>\r\n                        <property name="name">shipping_address_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">24</property>\r\n                        <property name="name">shipping_method_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">25</property>\r\n                        <property name="name">site_currency_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">26</property>\r\n                        <property name="name">site_domain_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">27</property>\r\n                        <property name="name">site_profile_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">28</property>\r\n                        <property name="name">void_payment_tran_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">29</property>\r\n                        <property name="name">cust_first_name</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">30</property>\r\n                        <property name="name">cust_last_name</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">31</property>\r\n                        <property name="name">site_currency_class_name</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                </list-property>\r\n            </structure>\r\n            <property name="dataSource">Onlinestore</property>\r\n            <list-property name="resultSet">\r\n                <structure>\r\n                    <property name="position">1</property>\r\n                    <property name="name">site_id</property>\r\n                    <property name="nativeName">site_id</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">2</property>\r\n                    <property name="name">site_name</property>\r\n                    <property name="nativeName">site_name</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">3</property>\r\n                    <property name="name">order_header_id</property>\r\n                    <property name="nativeName">order_header_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">4</property>\r\n                    <property name="name">creditcard_desc</property>\r\n                    <property name="nativeName">creditcard_desc</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">5</property>\r\n                    <property name="name">cust_creditcard_num</property>\r\n                    <property name="nativeName">cust_creditcard_num</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">6</property>\r\n                    <property name="name">cust_email</property>\r\n                    <property name="nativeName">cust_email</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">7</property>\r\n                    <property name="name">order_date</property>\r\n                    <property name="nativeName">order_date</property>\r\n                    <property name="dataType">date-time</property>\r\n                    <property name="nativeDataType">93</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">8</property>\r\n                    <property name="name">order_num</property>\r\n                    <property name="nativeName">order_num</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">9</property>\r\n                    <property name="name">order_status</property>\r\n                    <property name="nativeName">order_status</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">10</property>\r\n                    <property name="name">order_total</property>\r\n                    <property name="nativeName">order_total</property>\r\n                    <property name="dataType">float</property>\r\n                    <property name="nativeDataType">7</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">11</property>\r\n                    <property name="name">payment_gateway</property>\r\n                    <property name="nativeName">payment_gateway</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">12</property>\r\n                    <property name="name">rec_create_by</property>\r\n                    <property name="nativeName">rec_create_by</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">13</property>\r\n                    <property name="name">rec_create_datetime</property>\r\n                    <property name="nativeName">rec_create_datetime</property>\r\n                    <property name="dataType">date-time</property>\r\n                    <property name="nativeDataType">93</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">14</property>\r\n                    <property name="name">rec_update_by</property>\r\n                    <property name="nativeName">rec_update_by</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">15</property>\r\n                    <property name="name">rec_update_datetime</property>\r\n                    <property name="nativeName">rec_update_datetime</property>\r\n                    <property name="dataType">date-time</property>\r\n                    <property name="nativeDataType">93</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">16</property>\r\n                    <property name="name">shipping_discount_total</property>\r\n                    <property name="nativeName">shipping_discount_total</property>\r\n                    <property name="dataType">float</property>\r\n                    <property name="nativeDataType">7</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">17</property>\r\n                    <property name="name">shipping_method_name</property>\r\n                    <property name="nativeName">shipping_method_name</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">18</property>\r\n                    <property name="name">shipping_total</property>\r\n                    <property name="nativeName">shipping_total</property>\r\n                    <property name="dataType">float</property>\r\n                    <property name="nativeDataType">7</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">19</property>\r\n                    <property name="name">billing_address_id</property>\r\n                    <property name="nativeName">billing_address_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">20</property>\r\n                    <property name="name">cust_address_id</property>\r\n                    <property name="nativeName">cust_address_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">21</property>\r\n                    <property name="name">cust_id</property>\r\n                    <property name="nativeName">cust_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">22</property>\r\n                    <property name="name">payment_tran_id</property>\r\n                    <property name="nativeName">payment_tran_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">23</property>\r\n                    <property name="name">shipping_address_id</property>\r\n                    <property name="nativeName">shipping_address_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">24</property>\r\n                    <property name="name">shipping_method_id</property>\r\n                    <property name="nativeName">shipping_method_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">25</property>\r\n                    <property name="name">site_currency_id</property>\r\n                    <property name="nativeName">site_currency_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">26</property>\r\n                    <property name="name">site_domain_id</property>\r\n                    <property name="nativeName">site_domain_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">27</property>\r\n                    <property name="name">site_profile_id</property>\r\n                    <property name="nativeName">site_profile_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">28</property>\r\n                    <property name="name">void_payment_tran_id</property>\r\n                    <property name="nativeName">void_payment_tran_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">29</property>\r\n                    <property name="name">cust_first_name</property>\r\n                    <property name="nativeName">cust_first_name</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">30</property>\r\n                    <property name="name">cust_last_name</property>\r\n                    <property name="nativeName">cust_last_name</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">31</property>\r\n                    <property name="name">site_currency_class_name</property>\r\n                    <property name="nativeName">site_currency_class_name</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n            </list-property>\r\n            <xml-property name="queryText"><![CDATA[select	s.site_id, l.site_name, o.*, ca.cust_first_name, ca.cust_last_name, scc.site_currency_class_name\r\nfrom	order_header o\r\nleft	outer join site_domain s\r\non 		o.site_domain_id = s.site_domain_id\r\nleft	outer join site_domain_language l\r\non		s.site_domain_lang_id = l.site_domain_lang_id\r\nleft    outer join customer c\r\non      o.cust_id = c.cust_id\r\nleft    outer join customer_address ca\r\non 	    c.cust_address_id = ca.cust_address_id\r\nleft    outer join site_currency sc\r\non      o.site_currency_id = sc.site_currency_id\r\nleft    outer join site_currency_class scc\r\non      sc.site_currency_class_id = scc.site_currency_class_id\r\nWhere	s.site_id = ?\r\nand order_date >= ?\r\nand order_date <= ?]]></xml-property>\r\n            <xml-property name="designerValues"><![CDATA[<?xml version="1.0" encoding="UTF-8"?>\r\n<model:DesignValues xmlns:design="http://www.eclipse.org/datatools/connectivity/oda/design" xmlns:model="http://www.eclipse.org/birt/report/model/adapter/odaModel">\r\n  <Version>1.0</Version>\r\n  <design:ResultSets derivedMetaData="true">\r\n    <design:resultSetDefinitions>\r\n      <design:resultSetColumns>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_name</design:name>\r\n            <design:position>1</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>50</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_name</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_name</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>50</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>order_header_id</design:name>\r\n            <design:position>2</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>order_header_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>order_header_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>creditcard_desc</design:name>\r\n            <design:position>3</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>40</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>creditcard_desc</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>creditcard_desc</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>40</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>cust_creditcard_num</design:name>\r\n            <design:position>4</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>192</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>cust_creditcard_num</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>cust_creditcard_num</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>192</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>cust_email</design:name>\r\n            <design:position>5</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>255</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>cust_email</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>cust_email</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>255</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>order_date</design:name>\r\n            <design:position>6</design:position>\r\n            <design:nativeDataTypeCode>93</design:nativeDataTypeCode>\r\n            <design:precision>19</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>order_date</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>order_date</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>19</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>order_num</design:name>\r\n            <design:position>7</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>order_num</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>order_num</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>order_status</design:name>\r\n            <design:position>8</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>1</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>order_status</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>order_status</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>1</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>order_total</design:name>\r\n            <design:position>9</design:position>\r\n            <design:nativeDataTypeCode>7</design:nativeDataTypeCode>\r\n            <design:precision>12</design:precision>\r\n            <design:scale>31</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>order_total</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>order_total</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>12</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>payment_gateway</design:name>\r\n            <design:position>10</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>payment_gateway</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>payment_gateway</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>rec_create_by</design:name>\r\n            <design:position>11</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>rec_create_by</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>rec_create_by</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>rec_create_datetime</design:name>\r\n            <design:position>12</design:position>\r\n            <design:nativeDataTypeCode>93</design:nativeDataTypeCode>\r\n            <design:precision>19</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>rec_create_datetime</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>rec_create_datetime</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>19</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>rec_update_by</design:name>\r\n            <design:position>13</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>rec_update_by</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>rec_update_by</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>rec_update_datetime</design:name>\r\n            <design:position>14</design:position>\r\n            <design:nativeDataTypeCode>93</design:nativeDataTypeCode>\r\n            <design:precision>19</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>rec_update_datetime</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>rec_update_datetime</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>19</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>shipping_discount_total</design:name>\r\n            <design:position>15</design:position>\r\n            <design:nativeDataTypeCode>7</design:nativeDataTypeCode>\r\n            <design:precision>12</design:precision>\r\n            <design:scale>31</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>shipping_discount_total</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>shipping_discount_total</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>12</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>shipping_method_name</design:name>\r\n            <design:position>16</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>40</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>shipping_method_name</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>shipping_method_name</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>40</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>shipping_total</design:name>\r\n            <design:position>17</design:position>\r\n            <design:nativeDataTypeCode>7</design:nativeDataTypeCode>\r\n            <design:precision>12</design:precision>\r\n            <design:scale>31</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>shipping_total</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>shipping_total</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>12</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>billing_address_id</design:name>\r\n            <design:position>18</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>billing_address_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>billing_address_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>cust_address_id</design:name>\r\n            <design:position>19</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>cust_address_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>cust_address_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>cust_id</design:name>\r\n            <design:position>20</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>cust_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>cust_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>payment_tran_id</design:name>\r\n            <design:position>21</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>payment_tran_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>payment_tran_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>shipping_address_id</design:name>\r\n            <design:position>22</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>shipping_address_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>shipping_address_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>shipping_method_id</design:name>\r\n            <design:position>23</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>shipping_method_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>shipping_method_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_currency_id</design:name>\r\n            <design:position>24</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_currency_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_currency_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_domain_id</design:name>\r\n            <design:position>25</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_domain_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_domain_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_profile_id</design:name>\r\n            <design:position>26</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_profile_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_profile_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>void_payment_tran_id</design:name>\r\n            <design:position>27</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>void_payment_tran_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>void_payment_tran_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n      </design:resultSetColumns>\r\n    </design:resultSetDefinitions>\r\n  </design:ResultSets>\r\n</model:DesignValues>]]></xml-property>\r\n        </oda-data-set>\r\n        <oda-data-set extensionID="org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet" name="Sites" id="432">\r\n            <list-property name="columnHints">\r\n                <structure>\r\n                    <property name="columnName">site_domain_id</property>\r\n                    <text-property name="displayName">site_domain_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">active</property>\r\n                    <text-property name="displayName">active</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">rec_create_by</property>\r\n                    <text-property name="displayName">rec_create_by</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">rec_create_datetime</property>\r\n                    <text-property name="displayName">rec_create_datetime</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">rec_update_by</property>\r\n                    <text-property name="displayName">rec_update_by</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">rec_update_datetime</property>\r\n                    <text-property name="displayName">rec_update_datetime</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_domain_name</property>\r\n                    <text-property name="displayName">site_domain_name</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_domain_prefix</property>\r\n                    <text-property name="displayName">site_domain_prefix</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_public_port_num</property>\r\n                    <text-property name="displayName">site_public_port_num</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_secure_port_num</property>\r\n                    <text-property name="displayName">site_secure_port_num</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_ssl_enabled</property>\r\n                    <text-property name="displayName">site_ssl_enabled</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_currency_class_id</property>\r\n                    <text-property name="displayName">site_currency_class_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">home_page_id</property>\r\n                    <text-property name="displayName">home_page_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_id</property>\r\n                    <text-property name="displayName">site_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_currency_default_id</property>\r\n                    <text-property name="displayName">site_currency_default_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_domain_lang_id</property>\r\n                    <text-property name="displayName">site_domain_lang_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">site_profile_default_id</property>\r\n                    <text-property name="displayName">site_profile_default_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">template_id</property>\r\n                    <text-property name="displayName">template_id</text-property>\r\n                </structure>\r\n            </list-property>\r\n            <structure name="cachedMetaData">\r\n                <list-property name="resultSet">\r\n                    <structure>\r\n                        <property name="position">1</property>\r\n                        <property name="name">site_domain_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">2</property>\r\n                        <property name="name">active</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">3</property>\r\n                        <property name="name">rec_create_by</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">4</property>\r\n                        <property name="name">rec_create_datetime</property>\r\n                        <property name="dataType">date-time</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">5</property>\r\n                        <property name="name">rec_update_by</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">6</property>\r\n                        <property name="name">rec_update_datetime</property>\r\n                        <property name="dataType">date-time</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">7</property>\r\n                        <property name="name">site_domain_name</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">8</property>\r\n                        <property name="name">site_domain_prefix</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">9</property>\r\n                        <property name="name">site_public_port_num</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">10</property>\r\n                        <property name="name">site_secure_port_num</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">11</property>\r\n                        <property name="name">site_ssl_enabled</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">12</property>\r\n                        <property name="name">site_currency_class_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">13</property>\r\n                        <property name="name">home_page_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">14</property>\r\n                        <property name="name">site_id</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">15</property>\r\n                        <property name="name">site_currency_default_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">16</property>\r\n                        <property name="name">site_domain_lang_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">17</property>\r\n                        <property name="name">site_profile_default_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">18</property>\r\n                        <property name="name">template_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                </list-property>\r\n            </structure>\r\n            <property name="dataSource">Onlinestore</property>\r\n            <list-property name="resultSet">\r\n                <structure>\r\n                    <property name="position">1</property>\r\n                    <property name="name">site_domain_id</property>\r\n                    <property name="nativeName">site_domain_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">2</property>\r\n                    <property name="name">active</property>\r\n                    <property name="nativeName">active</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">3</property>\r\n                    <property name="name">rec_create_by</property>\r\n                    <property name="nativeName">rec_create_by</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">4</property>\r\n                    <property name="name">rec_create_datetime</property>\r\n                    <property name="nativeName">rec_create_datetime</property>\r\n                    <property name="dataType">date-time</property>\r\n                    <property name="nativeDataType">93</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">5</property>\r\n                    <property name="name">rec_update_by</property>\r\n                    <property name="nativeName">rec_update_by</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">6</property>\r\n                    <property name="name">rec_update_datetime</property>\r\n                    <property name="nativeName">rec_update_datetime</property>\r\n                    <property name="dataType">date-time</property>\r\n                    <property name="nativeDataType">93</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">7</property>\r\n                    <property name="name">site_domain_name</property>\r\n                    <property name="nativeName">site_domain_name</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">8</property>\r\n                    <property name="name">site_domain_prefix</property>\r\n                    <property name="nativeName">site_domain_prefix</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">9</property>\r\n                    <property name="name">site_public_port_num</property>\r\n                    <property name="nativeName">site_public_port_num</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">10</property>\r\n                    <property name="name">site_secure_port_num</property>\r\n                    <property name="nativeName">site_secure_port_num</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">11</property>\r\n                    <property name="name">site_ssl_enabled</property>\r\n                    <property name="nativeName">site_ssl_enabled</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">12</property>\r\n                    <property name="name">site_currency_class_id</property>\r\n                    <property name="nativeName">site_currency_class_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">13</property>\r\n                    <property name="name">home_page_id</property>\r\n                    <property name="nativeName">home_page_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">14</property>\r\n                    <property name="name">site_id</property>\r\n                    <property name="nativeName">site_id</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">15</property>\r\n                    <property name="name">site_currency_default_id</property>\r\n                    <property name="nativeName">site_currency_default_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">16</property>\r\n                    <property name="name">site_domain_lang_id</property>\r\n                    <property name="nativeName">site_domain_lang_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">17</property>\r\n                    <property name="name">site_profile_default_id</property>\r\n                    <property name="nativeName">site_profile_default_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">18</property>\r\n                    <property name="name">template_id</property>\r\n                    <property name="nativeName">template_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n            </list-property>\r\n            <xml-property name="queryText"><![CDATA[Select *\r\nfrom site_domain\r\nWhere site_id != \'_system\']]></xml-property>\r\n            <xml-property name="designerValues"><![CDATA[<?xml version="1.0" encoding="UTF-8"?>\r\n<model:DesignValues xmlns:design="http://www.eclipse.org/datatools/connectivity/oda/design" xmlns:model="http://www.eclipse.org/birt/report/model/adapter/odaModel">\r\n  <Version>1.0</Version>\r\n  <design:ResultSets derivedMetaData="true">\r\n    <design:resultSetDefinitions>\r\n      <design:resultSetColumns>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_domain_id</design:name>\r\n            <design:position>1</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_domain_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_domain_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>active</design:name>\r\n            <design:position>2</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>1</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>active</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>active</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>1</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>rec_create_by</design:name>\r\n            <design:position>3</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>rec_create_by</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>rec_create_by</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>rec_create_datetime</design:name>\r\n            <design:position>4</design:position>\r\n            <design:nativeDataTypeCode>93</design:nativeDataTypeCode>\r\n            <design:precision>19</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>rec_create_datetime</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>rec_create_datetime</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>19</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>rec_update_by</design:name>\r\n            <design:position>5</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>rec_update_by</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>rec_update_by</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>rec_update_datetime</design:name>\r\n            <design:position>6</design:position>\r\n            <design:nativeDataTypeCode>93</design:nativeDataTypeCode>\r\n            <design:precision>19</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>rec_update_datetime</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>rec_update_datetime</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>19</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_domain_name</design:name>\r\n            <design:position>7</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>50</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_domain_name</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_domain_name</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>50</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_domain_prefix</design:name>\r\n            <design:position>8</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>50</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_domain_prefix</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_domain_prefix</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>50</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_public_port_num</design:name>\r\n            <design:position>9</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>4</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_public_port_num</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_public_port_num</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>4</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_secure_port_num</design:name>\r\n            <design:position>10</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>4</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_secure_port_num</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_secure_port_num</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>4</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_ssl_enabled</design:name>\r\n            <design:position>11</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>1</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_ssl_enabled</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_ssl_enabled</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>1</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_currency_class_id</design:name>\r\n            <design:position>12</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_currency_class_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_currency_class_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>home_page_id</design:name>\r\n            <design:position>13</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>home_page_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>home_page_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_id</design:name>\r\n            <design:position>14</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_currency_default_id</design:name>\r\n            <design:position>15</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_currency_default_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_currency_default_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_domain_lang_id</design:name>\r\n            <design:position>16</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_domain_lang_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_domain_lang_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_profile_default_id</design:name>\r\n            <design:position>17</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_profile_default_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_profile_default_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>template_id</design:name>\r\n            <design:position>18</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>Nullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>template_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>template_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n      </design:resultSetColumns>\r\n    </design:resultSetDefinitions>\r\n  </design:ResultSets>\r\n</model:DesignValues>]]></xml-property>\r\n        </oda-data-set>\r\n        <oda-data-set extensionID="org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet" name="SiteCurrency" id="433">\r\n            <list-property name="columnHints">\r\n                <structure>\r\n                    <property name="columnName">site_currency_id</property>\r\n                    <text-property name="displayName">site_currency_id</text-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="columnName">currency_locale_country</property>\r\n                    <text-property name="displayName">currency_locale_country</text-property>\r\n                </structure>\r\n            </list-property>\r\n            <structure name="cachedMetaData">\r\n                <list-property name="resultSet">\r\n                    <structure>\r\n                        <property name="position">1</property>\r\n                        <property name="name">site_currency_id</property>\r\n                        <property name="dataType">decimal</property>\r\n                    </structure>\r\n                    <structure>\r\n                        <property name="position">2</property>\r\n                        <property name="name">currency_locale_country</property>\r\n                        <property name="dataType">string</property>\r\n                    </structure>\r\n                </list-property>\r\n            </structure>\r\n            <property name="dataSource">Onlinestore</property>\r\n            <list-property name="resultSet">\r\n                <structure>\r\n                    <property name="position">1</property>\r\n                    <property name="name">site_currency_id</property>\r\n                    <property name="nativeName">site_currency_id</property>\r\n                    <property name="dataType">decimal</property>\r\n                    <property name="nativeDataType">-5</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="position">2</property>\r\n                    <property name="name">currency_locale_country</property>\r\n                    <property name="nativeName">currency_locale_country</property>\r\n                    <property name="dataType">string</property>\r\n                    <property name="nativeDataType">12</property>\r\n                </structure>\r\n            </list-property>\r\n            <xml-property name="queryText"><![CDATA[select  sc.site_currency_id, scc.currency_locale_country\r\nfrom site_currency sc, site_currency_class scc\r\nwhere sc.site_currency_class_id = scc.site_currency_class_id\r\n]]></xml-property>\r\n            <xml-property name="designerValues"><![CDATA[<?xml version="1.0" encoding="UTF-8"?>\r\n<model:DesignValues xmlns:design="http://www.eclipse.org/datatools/connectivity/oda/design" xmlns:model="http://www.eclipse.org/birt/report/model/adapter/odaModel">\r\n  <Version>1.0</Version>\r\n  <design:ResultSets derivedMetaData="true">\r\n    <design:resultSetDefinitions>\r\n      <design:resultSetColumns>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>site_currency_id</design:name>\r\n            <design:position>1</design:position>\r\n            <design:nativeDataTypeCode>-5</design:nativeDataTypeCode>\r\n            <design:precision>20</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>site_currency_id</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>site_currency_id</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>20</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n        <design:resultColumnDefinitions>\r\n          <design:attributes>\r\n            <design:name>currency_locale_country</design:name>\r\n            <design:position>2</design:position>\r\n            <design:nativeDataTypeCode>12</design:nativeDataTypeCode>\r\n            <design:precision>2</design:precision>\r\n            <design:scale>0</design:scale>\r\n            <design:nullability>NotNullable</design:nullability>\r\n            <design:uiHints>\r\n              <design:displayName>currency_locale_country</design:displayName>\r\n            </design:uiHints>\r\n          </design:attributes>\r\n          <design:usageHints>\r\n            <design:label>currency_locale_country</design:label>\r\n            <design:formattingHints>\r\n              <design:displaySize>2</design:displaySize>\r\n            </design:formattingHints>\r\n          </design:usageHints>\r\n        </design:resultColumnDefinitions>\r\n      </design:resultSetColumns>\r\n    </design:resultSetDefinitions>\r\n  </design:ResultSets>\r\n</model:DesignValues>]]></xml-property>\r\n        </oda-data-set>\r\n    </data-sets>\r\n    <styles>\r\n        <style name="report" id="24">\r\n            <property name="fontFamily">sans-serif</property>\r\n            <property name="fontSize">10pt</property>\r\n            <structure name="numberFormat">\r\n                <property name="category">Currency</property>\r\n                <property name="pattern">$#,##0.00;$(#,##0.00)</property>\r\n            </structure>\r\n        </style>\r\n        <style name="crosstab-cell" id="25">\r\n            <property name="borderBottomColor">#CCCCCC</property>\r\n            <property name="borderBottomStyle">solid</property>\r\n            <property name="borderBottomWidth">1pt</property>\r\n            <property name="borderLeftColor">#CCCCCC</property>\r\n            <property name="borderLeftStyle">solid</property>\r\n            <property name="borderLeftWidth">1pt</property>\r\n            <property name="borderRightColor">#CCCCCC</property>\r\n            <property name="borderRightStyle">solid</property>\r\n            <property name="borderRightWidth">1pt</property>\r\n            <property name="borderTopColor">#CCCCCC</property>\r\n            <property name="borderTopStyle">solid</property>\r\n            <property name="borderTopWidth">1pt</property>\r\n        </style>\r\n        <style name="crosstab" id="26">\r\n            <property name="borderBottomColor">#CCCCCC</property>\r\n            <property name="borderBottomStyle">solid</property>\r\n            <property name="borderBottomWidth">1pt</property>\r\n            <property name="borderLeftColor">#CCCCCC</property>\r\n            <property name="borderLeftStyle">solid</property>\r\n            <property name="borderLeftWidth">1pt</property>\r\n            <property name="borderRightColor">#CCCCCC</property>\r\n            <property name="borderRightStyle">solid</property>\r\n            <property name="borderRightWidth">1pt</property>\r\n            <property name="borderTopColor">#CCCCCC</property>\r\n            <property name="borderTopStyle">solid</property>\r\n            <property name="borderTopWidth">1pt</property>\r\n        </style>\r\n        <style name="table-footer" id="353"/>\r\n        <style name="footer" id="354">\r\n            <property name="fontFamily">sans-serif</property>\r\n            <property name="color">#008000</property>\r\n        </style>\r\n        <style name="table-header" id="360">\r\n            <property name="fontSize">large</property>\r\n            <property name="color">#0080FF</property>\r\n            <property name="textUnderline">none</property>\r\n            <structure name="dateTimeFormat">\r\n                <property name="category">Custom</property>\r\n                <property name="pattern">dd-MM-yy hh:mm a</property>\r\n            </structure>\r\n            <structure name="numberFormat">\r\n                <property name="category">General Number</property>\r\n                <property name="pattern">General Number</property>\r\n            </structure>\r\n        </style>\r\n        <style name="ReportHeader" id="395">\r\n            <property name="fontFamily">sans-serif</property>\r\n            <property name="fontSize">large</property>\r\n            <property name="fontWeight">700</property>\r\n            <property name="fontStyle">normal</property>\r\n            <property name="color">#0000FF</property>\r\n            <property name="textLineThrough">none</property>\r\n            <property name="textOverline">none</property>\r\n            <property name="textUnderline">none</property>\r\n        </style>\r\n    </styles>\r\n    <page-setup>\r\n        <simple-master-page name="Simple MasterPage" id="2">\r\n            <property name="type">us-letter</property>\r\n            <property name="leftMargin">0.25in</property>\r\n            <property name="bottomMargin">0.25in</property>\r\n            <property name="rightMargin">0.25in</property>\r\n            <property name="showHeaderOnFirst">false</property>\r\n            <property name="showFooterOnLast">false</property>\r\n            <page-footer>\r\n                <text id="3">\r\n                    <property name="contentType">html</property>\r\n                </text>\r\n            </page-footer>\r\n        </simple-master-page>\r\n    </page-setup>\r\n    <body>\r\n        <grid id="378">\r\n            <property name="width">7.5in</property>\r\n            <list-property name="boundDataColumns">\r\n                <structure>\r\n                    <property name="name">Column Binding</property>\r\n                    <expression name="expression" type="javascript">new Date()</expression>\r\n                    <property name="dataType">date-time</property>\r\n                </structure>\r\n            </list-property>\r\n            <column id="379">\r\n                <property name="width">2.5in</property>\r\n            </column>\r\n            <column id="380">\r\n                <property name="width">2.5in</property>\r\n            </column>\r\n            <column id="389">\r\n                <property name="width">2.5in</property>\r\n            </column>\r\n            <row id="381">\r\n                <property name="style">ReportHeader</property>\r\n                <cell id="382">\r\n                    <text-data id="394">\r\n                        <property name="style">footer</property>\r\n                        <property name="fontSize">12pt</property>\r\n                        <property name="fontWeight">bold</property>\r\n                        <property name="color">#004080</property>\r\n                        <expression name="valueExpr" type="javascript">"Site: " + params["pSiteId"].value</expression>\r\n                        <property name="contentType">html</property>\r\n                    </text-data>\r\n                </cell>\r\n                <cell id="383">\r\n                    <text id="392">\r\n                        <property name="style">footer</property>\r\n                        <property name="color">#004080</property>\r\n                        <property name="contentType">html</property>\r\n                        <text-property name="content"><![CDATA[<CENTER>\r\n<FONT size="5">\r\nSales Order Report\r\n</FONT>\r\n<BR>\r\n<FONT size="1">\r\n<I>For internal use only</I>\r\n<br>\r\n</FONT>\r\n</CENTER>]]></text-property>\r\n                    </text>\r\n                </cell>\r\n                <cell id="387">\r\n                    <data id="653">\r\n                        <property name="fontSize">12pt</property>\r\n                        <property name="color">#004080</property>\r\n                        <property name="textAlign">right</property>\r\n                        <property name="resultSetColumn">Column Binding</property>\r\n                    </data>\r\n                </cell>\r\n            </row>\r\n            <row id="384">\r\n                <cell id="385">\r\n                    <data id="37">\r\n                        <property name="style">footer</property>\r\n                        <property name="fontSize">10pt</property>\r\n                        <property name="fontWeight">bold</property>\r\n                        <property name="color">#004080</property>\r\n                        <structure name="dateTimeFormat">\r\n                            <property name="category">Long Date</property>\r\n                            <property name="pattern">Long Date</property>\r\n                        </structure>\r\n                        <list-property name="boundDataColumns">\r\n                            <structure>\r\n                                <property name="name">new Date( )</property>\r\n                                <text-property name="displayName">OrderDate</text-property>\r\n                                <expression name="expression" type="javascript">"From: " + params["pOrderDateStart"].value + " To: " + params["pOrderDateEnd"].value</expression>\r\n                                <property name="dataType">string</property>\r\n                            </structure>\r\n                        </list-property>\r\n                        <property name="resultSetColumn">new Date( )</property>\r\n                    </data>\r\n                </cell>\r\n                <cell id="386">\r\n                    <label id="618">\r\n                        <text-property name="text"> </text-property>\r\n                    </label>\r\n                </cell>\r\n                <cell id="388"/>\r\n            </row>\r\n            <row id="396">\r\n                <cell id="397">\r\n                    <label id="619">\r\n                        <text-property name="text"> </text-property>\r\n                    </label>\r\n                </cell>\r\n                <cell id="398"/>\r\n                <cell id="399"/>\r\n            </row>\r\n        </grid>\r\n        <table id="4">\r\n            <property name="width">7.5in</property>\r\n            <property name="dataSet">SiteOrders</property>\r\n            <list-property name="paramBindings">\r\n                <structure>\r\n                    <property name="paramName">param_3</property>\r\n                    <expression name="expression" type="javascript">new Date()</expression>\r\n                </structure>\r\n            </list-property>\r\n            <list-property name="boundDataColumns">\r\n                <structure>\r\n                    <property name="name">site_currency_id</property>\r\n                    <text-property name="displayName">site_currency_id</text-property>\r\n                    <expression name="expression">dataSetRow["site_currency_id"]</expression>\r\n                    <property name="dataType">decimal</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">site_domain_id</property>\r\n                    <text-property name="displayName">site_domain_id</text-property>\r\n                    <expression name="expression">dataSetRow["site_domain_id"]</expression>\r\n                    <property name="dataType">decimal</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">order_status</property>\r\n                    <text-property name="displayName">order_status</text-property>\r\n                    <expression name="expression">dataSetRow["order_status"]</expression>\r\n                    <property name="dataType">string</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">order_total</property>\r\n                    <text-property name="displayName">order_total</text-property>\r\n                    <expression name="expression">dataSetRow["order_total"]</expression>\r\n                    <property name="dataType">float</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">OrderCount</property>\r\n                    <text-property name="displayName">OrderCount</text-property>\r\n                    <property name="dataType">integer</property>\r\n                    <property name="aggregateFunction">RUNNINGCOUNT</property>\r\n                    <list-property name="arguments">\r\n                        <structure>\r\n                            <property name="name">Expression</property>\r\n                            <expression name="value" type="javascript">row["order_status"]</expression>\r\n                        </structure>\r\n                    </list-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">GrandTotal</property>\r\n                    <text-property name="displayName">GrandTotal</text-property>\r\n                    <property name="dataType">float</property>\r\n                    <property name="aggregateFunction">RUNNINGSUM</property>\r\n                    <list-property name="arguments">\r\n                        <structure>\r\n                            <property name="name">Expression</property>\r\n                            <expression name="value" type="javascript">row["order_total"]</expression>\r\n                        </structure>\r\n                    </list-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">Aggregation</property>\r\n                    <text-property name="displayName">OrderTotal</text-property>\r\n                    <property name="dataType">float</property>\r\n                    <simple-property-list name="aggregateOn">\r\n                        <value>CurrGroup</value>\r\n                    </simple-property-list>\r\n                    <property name="aggregateFunction">RUNNINGSUM</property>\r\n                    <list-property name="arguments">\r\n                        <structure>\r\n                            <property name="name">Expression</property>\r\n                            <expression name="value" type="javascript">row["order_total"]</expression>\r\n                        </structure>\r\n                    </list-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">Aggregation_1</property>\r\n                    <text-property name="displayName">CountOrders</text-property>\r\n                    <property name="dataType">integer</property>\r\n                    <simple-property-list name="aggregateOn">\r\n                        <value>CurrGroup</value>\r\n                    </simple-property-list>\r\n                    <property name="aggregateFunction">RUNNINGCOUNT</property>\r\n                    <list-property name="arguments">\r\n                        <structure>\r\n                            <property name="name">Expression</property>\r\n                        </structure>\r\n                    </list-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">shipping_total</property>\r\n                    <text-property name="displayName">shipping_total</text-property>\r\n                    <expression name="expression">dataSetRow["shipping_total"]</expression>\r\n                    <property name="dataType">float</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">ShipTotal</property>\r\n                    <text-property name="displayName">ShipTotal</text-property>\r\n                    <property name="dataType">float</property>\r\n                    <simple-property-list name="aggregateOn">\r\n                        <value>CurrGroup</value>\r\n                    </simple-property-list>\r\n                    <property name="aggregateFunction">RUNNINGSUM</property>\r\n                    <list-property name="arguments">\r\n                        <structure>\r\n                            <property name="name">Expression</property>\r\n                            <expression name="value" type="javascript">row["shipping_total"]</expression>\r\n                        </structure>\r\n                    </list-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">cust_id</property>\r\n                    <text-property name="displayName">cust_id</text-property>\r\n                    <expression name="expression">dataSetRow["cust_id"]</expression>\r\n                    <property name="dataType">decimal</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">order_date</property>\r\n                    <text-property name="displayName">order_date</text-property>\r\n                    <expression name="expression" type="javascript">dataSetRow["order_date"]</expression>\r\n                    <property name="dataType">date-time</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">order_num</property>\r\n                    <text-property name="displayName">order_num</text-property>\r\n                    <expression name="expression">dataSetRow["order_num"]</expression>\r\n                    <property name="dataType">string</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">GCountOrders</property>\r\n                    <text-property name="displayName">GCountOrders</text-property>\r\n                    <property name="dataType">integer</property>\r\n                    <property name="aggregateFunction">RUNNINGCOUNT</property>\r\n                    <list-property name="arguments">\r\n                        <structure>\r\n                            <property name="name">Expression</property>\r\n                        </structure>\r\n                    </list-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">GOrderTotal</property>\r\n                    <text-property name="displayName">GOrderTotal</text-property>\r\n                    <property name="dataType">float</property>\r\n                    <property name="aggregateFunction">RUNNINGSUM</property>\r\n                    <list-property name="arguments">\r\n                        <structure>\r\n                            <property name="name">Expression</property>\r\n                            <expression name="value" type="javascript">row["order_total"]</expression>\r\n                        </structure>\r\n                    </list-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">GShipTotal</property>\r\n                    <text-property name="displayName">ShipTotal</text-property>\r\n                    <property name="dataType">float</property>\r\n                    <property name="aggregateFunction">RUNNINGSUM</property>\r\n                    <list-property name="arguments">\r\n                        <structure>\r\n                            <property name="name">Expression</property>\r\n                            <expression name="value" type="javascript">row["shipping_total"]</expression>\r\n                        </structure>\r\n                    </list-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">site_name</property>\r\n                    <text-property name="displayName">site_name</text-property>\r\n                    <expression name="expression" type="javascript">\'Sub-site: \' +  dataSetRow["site_name"]</expression>\r\n                    <property name="dataType">string</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">cust_first_name</property>\r\n                    <text-property name="displayName">cust_first_name</text-property>\r\n                    <expression name="expression">dataSetRow["cust_first_name"]</expression>\r\n                    <property name="dataType">string</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">cust_last_name</property>\r\n                    <text-property name="displayName">cust_last_name</text-property>\r\n                    <expression name="expression">dataSetRow["cust_last_name"]</expression>\r\n                    <property name="dataType">string</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">site_currency_class_name</property>\r\n                    <text-property name="displayName">site_currency_class_name</text-property>\r\n                    <expression name="expression" type="javascript">\'Currency: \' +  dataSetRow["site_currency_class_name"]</expression>\r\n                    <property name="dataType">string</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">SiteGrp</property>\r\n                    <text-property name="displayName">CountOrders</text-property>\r\n                    <property name="dataType">integer</property>\r\n                    <simple-property-list name="aggregateOn">\r\n                        <value>SiteGroup</value>\r\n                    </simple-property-list>\r\n                    <property name="aggregateFunction">RUNNINGCOUNT</property>\r\n                    <list-property name="arguments">\r\n                        <structure>\r\n                            <property name="name">Expression</property>\r\n                        </structure>\r\n                    </list-property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">site_name+lit</property>\r\n                    <text-property name="displayName">site_name</text-property>\r\n                    <expression name="expression" type="javascript">dataSetRow["site_name"] + \' =\'</expression>\r\n                    <property name="dataType">string</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">Column Binding</property>\r\n                    <text-property name="displayName">Total Order</text-property>\r\n                    <expression name="expression" type="javascript">\'Total number of order for \' +  dataSetRow["site_name"]</expression>\r\n                    <property name="dataType">string</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">Column Binding_1</property>\r\n                    <expression name="expression" type="javascript">new Date()</expression>\r\n                    <property name="dataType">date-time</property>\r\n                </structure>\r\n                <structure>\r\n                    <property name="name">Column Binding_2</property>\r\n                    <expression name="expression" type="javascript">\'Page \' + pageNumber</expression>\r\n                    <property name="dataType">string</property>\r\n                </structure>\r\n            </list-property>\r\n            <property name="pageBreakInterval">50</property>\r\n            <list-property name="sort">\r\n                <structure>\r\n                    <expression name="key" type="javascript">row["site_currency_id"]</expression>\r\n                    <property name="direction">asc</property>\r\n                </structure>\r\n                <structure>\r\n                    <expression name="key" type="javascript">row["order_total"]</expression>\r\n                    <property name="direction">desc</property>\r\n                </structure>\r\n            </list-property>\r\n            <column id="445">\r\n                <property name="width">1.1041666666666667in</property>\r\n            </column>\r\n            <column id="414">\r\n                <property name="width">1.0833333333333333in</property>\r\n            </column>\r\n            <column id="510">\r\n                <property name="width">1.1458333333333333in</property>\r\n            </column>\r\n            <column id="534">\r\n                <property name="width">1.1979166666666667in</property>\r\n            </column>\r\n            <column id="23">\r\n                <property name="width">1.1979166666666667in</property>\r\n            </column>\r\n            <column id="405">\r\n                <property name="width">1.46875in</property>\r\n            </column>\r\n            <header>\r\n                <row id="5">\r\n                    <cell id="438">\r\n                        <label id="446">\r\n                            <property name="fontSize">small</property>\r\n                            <property name="fontWeight">bold</property>\r\n                            <property name="color">black</property>\r\n                            <property name="textAlign">left</property>\r\n                            <text-property name="text">Order Num</text-property>\r\n                        </label>\r\n                    </cell>\r\n                    <cell id="409">\r\n                        <label id="322">\r\n                            <property name="fontSize">small</property>\r\n                            <property name="fontWeight">bold</property>\r\n                            <property name="color">black</property>\r\n                            <property name="textAlign">left</property>\r\n                            <text-property name="text">Order Date  </text-property>\r\n                        </label>\r\n                    </cell>\r\n                    <cell id="499">\r\n                        <label id="535">\r\n                            <property name="fontSize">small</property>\r\n                            <property name="fontWeight">bold</property>\r\n                            <property name="color">black</property>\r\n                            <property name="textAlign">left</property>\r\n                            <text-property name="text">First Name</text-property>\r\n                        </label>\r\n                    </cell>\r\n                    <cell id="523">\r\n                        <label id="537">\r\n                            <property name="fontSize">small</property>\r\n                            <property name="fontWeight">bold</property>\r\n                            <property name="color">black</property>\r\n                            <property name="textAlign">left</property>\r\n                            <text-property name="text">Last name</text-property>\r\n                        </label>\r\n                    </cell>\r\n                    <cell id="9">\r\n                        <label id="326">\r\n                            <property name="fontSize">small</property>\r\n                            <property name="fontWeight">bold</property>\r\n                            <property name="color">black</property>\r\n                            <property name="textAlign">right</property>\r\n                            <text-property name="text">Order Total</text-property>\r\n                        </label>\r\n                    </cell>\r\n                    <cell id="400">\r\n                        <label id="406">\r\n                            <property name="fontSize">small</property>\r\n                            <property name="fontWeight">bold</property>\r\n                            <property name="color">black</property>\r\n                            <property name="textAlign">right</property>\r\n                            <text-property name="text">Shipping Total</text-property>\r\n                        </label>\r\n                    </cell>\r\n                </row>\r\n                <row id="623">\r\n                    <cell id="626">\r\n                        <property name="borderTopColor">#333333</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                    </cell>\r\n                    <cell id="627">\r\n                        <property name="borderTopColor">#333333</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                    </cell>\r\n                    <cell id="628">\r\n                        <property name="borderTopColor">#333333</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                    </cell>\r\n                    <cell id="629">\r\n                        <property name="borderTopColor">#333333</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                    </cell>\r\n                    <cell id="630">\r\n                        <property name="borderTopColor">#333333</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                    </cell>\r\n                    <cell id="631">\r\n                        <property name="borderTopColor">#333333</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                    </cell>\r\n                </row>\r\n            </header>\r\n            <group id="559">\r\n                <property name="groupName">SiteGroup</property>\r\n                <property name="interval">none</property>\r\n                <property name="sortDirection">asc</property>\r\n                <expression name="keyExpr" type="javascript">row["site_name"]</expression>\r\n                <structure name="toc">\r\n                    <expression name="expressionValue" type="javascript">row["site_name"]</expression>\r\n                    <property name="TOCStyle">ReportHeader</property>\r\n                </structure>\r\n                <property name="repeatHeader">true</property>\r\n                <property name="hideDetail">false</property>\r\n                <property name="pageBreakAfter">auto</property>\r\n                <property name="pageBreakBefore">auto</property>\r\n                <property name="pageBreakInside">auto</property>\r\n                <list-property name="sort">\r\n                    <structure>\r\n                        <expression name="key" type="javascript">row["site_name"]</expression>\r\n                        <property name="direction">asc</property>\r\n                    </structure>\r\n                </list-property>\r\n                <header>\r\n                    <row id="560">\r\n                        <cell id="563">\r\n                            <property name="colSpan">3</property>\r\n                            <property name="rowSpan">1</property>\r\n                            <data id="578">\r\n                                <property name="fontSize">10pt</property>\r\n                                <property name="fontWeight">bold</property>\r\n                                <property name="color">black</property>\r\n                                <property name="paddingTop">10pt</property>\r\n                                <structure name="numberFormat">\r\n                                    <property name="category">Custom</property>\r\n                                </structure>\r\n                                <property name="textAlign">left</property>\r\n                                <property name="resultSetColumn">site_name</property>\r\n                            </data>\r\n                        </cell>\r\n                        <cell id="566"/>\r\n                        <cell id="567"/>\r\n                        <cell id="568"/>\r\n                    </row>\r\n                </header>\r\n                <footer>\r\n                    <row id="632">\r\n                        <cell id="633">\r\n                            <property name="borderTopColor">#000000</property>\r\n                            <property name="borderTopStyle">solid</property>\r\n                            <property name="borderTopWidth">thin</property>\r\n                            <property name="paddingTop">5pt</property>\r\n                            <property name="paddingBottom">5pt</property>\r\n                        </cell>\r\n                        <cell id="634">\r\n                            <property name="borderTopColor">#000000</property>\r\n                            <property name="borderTopStyle">solid</property>\r\n                            <property name="borderTopWidth">thin</property>\r\n                            <property name="paddingTop">5pt</property>\r\n                            <property name="paddingBottom">5pt</property>\r\n                        </cell>\r\n                        <cell id="635">\r\n                            <property name="borderTopColor">#000000</property>\r\n                            <property name="borderTopStyle">solid</property>\r\n                            <property name="borderTopWidth">thin</property>\r\n                            <property name="paddingTop">5pt</property>\r\n                            <property name="paddingBottom">5pt</property>\r\n                        </cell>\r\n                        <cell id="636"/>\r\n                        <cell id="637"/>\r\n                        <cell id="638"/>\r\n                    </row>\r\n                    <row id="591">\r\n                        <cell id="594">\r\n                            <property name="colSpan">2</property>\r\n                            <property name="rowSpan">1</property>\r\n                            <property name="paddingTop">10pt</property>\r\n                            <property name="paddingBottom">10pt</property>\r\n                            <data id="639">\r\n                                <property name="fontWeight">bold</property>\r\n                                <property name="resultSetColumn">Column Binding</property>\r\n                            </data>\r\n                        </cell>\r\n                        <cell id="596">\r\n                            <property name="paddingTop">10pt</property>\r\n                            <property name="paddingBottom">10pt</property>\r\n                            <data id="580">\r\n                                <property name="backgroundAttachment">scroll</property>\r\n                                <property name="backgroundPositionX">0%</property>\r\n                                <property name="backgroundPositionY">0%</property>\r\n                                <property name="backgroundRepeat">repeat</property>\r\n                                <property name="fontFamily">sans-serif</property>\r\n                                <property name="fontSize">10pt</property>\r\n                                <property name="fontWeight">bold</property>\r\n                                <property name="fontStyle">normal</property>\r\n                                <property name="fontVariant">normal</property>\r\n                                <property name="color">black</property>\r\n                                <property name="textLineThrough">none</property>\r\n                                <property name="textOverline">none</property>\r\n                                <property name="textUnderline">none</property>\r\n                                <property name="borderBottomColor">black</property>\r\n                                <property name="borderBottomStyle">none</property>\r\n                                <property name="borderBottomWidth">medium</property>\r\n                                <property name="borderLeftColor">black</property>\r\n                                <property name="borderLeftStyle">none</property>\r\n                                <property name="borderLeftWidth">medium</property>\r\n                                <property name="borderRightColor">black</property>\r\n                                <property name="borderRightStyle">none</property>\r\n                                <property name="borderRightWidth">medium</property>\r\n                                <property name="borderTopColor">black</property>\r\n                                <property name="borderTopStyle">none</property>\r\n                                <property name="borderTopWidth">medium</property>\r\n                                <property name="marginTop">0pt</property>\r\n                                <property name="marginLeft">0pt</property>\r\n                                <property name="marginBottom">0pt</property>\r\n                                <property name="marginRight">0pt</property>\r\n                                <property name="paddingTop">1pt</property>\r\n                                <property name="paddingLeft">1pt</property>\r\n                                <property name="paddingBottom">1pt</property>\r\n                                <property name="paddingRight">1pt</property>\r\n                                <structure name="numberFormat">\r\n                                    <property name="category">Fixed</property>\r\n                                    <property name="pattern">#,##0;(#,##0)</property>\r\n                                </structure>\r\n                                <property name="textAlign">right</property>\r\n                                <property name="letterSpacing">normal</property>\r\n                                <property name="lineHeight">normal</property>\r\n                                <property name="orphans">2</property>\r\n                                <property name="textTransform">none</property>\r\n                                <property name="whiteSpace">normal</property>\r\n                                <property name="widows">2</property>\r\n                                <property name="wordSpacing">normal</property>\r\n                                <property name="display">block</property>\r\n                                <property name="pageBreakAfter">auto</property>\r\n                                <property name="pageBreakBefore">auto</property>\r\n                                <property name="pageBreakInside">auto</property>\r\n                                <property name="showIfBlank">false</property>\r\n                                <property name="canShrink">false</property>\r\n                                <property name="resultSetColumn">SiteGrp</property>\r\n                            </data>\r\n                        </cell>\r\n                        <cell id="597"/>\r\n                        <cell id="598"/>\r\n                        <cell id="599"/>\r\n                    </row>\r\n                </footer>\r\n            </group>\r\n            <group id="339">\r\n                <property name="groupName">CurrGroup</property>\r\n                <property name="interval">none</property>\r\n                <property name="sortDirection">asc</property>\r\n                <expression name="keyExpr" type="javascript">row["site_currency_class_name"]</expression>\r\n                <structure name="toc">\r\n                    <expression name="expressionValue" type="javascript">row["site_currency_class_name"]</expression>\r\n                    <property name="TOCStyle">ReportHeader</property>\r\n                </structure>\r\n                <property name="repeatHeader">true</property>\r\n                <property name="hideDetail">false</property>\r\n                <property name="pageBreakAfter">auto</property>\r\n                <property name="pageBreakBefore">auto</property>\r\n                <property name="pageBreakInside">auto</property>\r\n                <list-property name="sort">\r\n                    <structure>\r\n                        <expression name="key" type="javascript">row["site_currency_class_name"]</expression>\r\n                        <property name="direction">asc</property>\r\n                    </structure>\r\n                </list-property>\r\n                <header>\r\n                    <row id="340">\r\n                        <cell id="439">\r\n                            <property name="colSpan">3</property>\r\n                            <property name="rowSpan">1</property>\r\n                            <data id="558">\r\n                                <property name="fontSize">10pt</property>\r\n                                <property name="fontWeight">bold</property>\r\n                                <property name="paddingTop">10pt</property>\r\n                                <property name="paddingBottom">5pt</property>\r\n                                <property name="textAlign">left</property>\r\n                                <property name="resultSetColumn">site_currency_class_name</property>\r\n                            </data>\r\n                        </cell>\r\n                        <cell id="524"/>\r\n                        <cell id="344"/>\r\n                        <cell id="401"/>\r\n                    </row>\r\n                </header>\r\n                <footer>\r\n                    <row id="345">\r\n                        <cell id="441">\r\n                            <property name="colSpan">2</property>\r\n                            <property name="rowSpan">1</property>\r\n                            <property name="paddingTop">10pt</property>\r\n                            <label id="448">\r\n                                <property name="style">footer</property>\r\n                                <property name="fontWeight">normal</property>\r\n                                <property name="color">black</property>\r\n                                <property name="textAlign">left</property>\r\n                                <text-property name="text">Number of orders:</text-property>\r\n                            </label>\r\n                        </cell>\r\n                        <cell id="502">\r\n                            <property name="paddingTop">10pt</property>\r\n                            <data id="449">\r\n                                <property name="backgroundAttachment">scroll</property>\r\n                                <property name="backgroundPositionX">0%</property>\r\n                                <property name="backgroundPositionY">0%</property>\r\n                                <property name="backgroundRepeat">repeat</property>\r\n                                <property name="fontFamily">sans-serif</property>\r\n                                <property name="fontSize">10pt</property>\r\n                                <property name="fontWeight">normal</property>\r\n                                <property name="fontStyle">normal</property>\r\n                                <property name="fontVariant">normal</property>\r\n                                <property name="color">black</property>\r\n                                <property name="textLineThrough">none</property>\r\n                                <property name="textOverline">none</property>\r\n                                <property name="textUnderline">none</property>\r\n                                <property name="borderBottomColor">black</property>\r\n                                <property name="borderBottomStyle">none</property>\r\n                                <property name="borderBottomWidth">medium</property>\r\n                                <property name="borderLeftColor">black</property>\r\n                                <property name="borderLeftStyle">none</property>\r\n                                <property name="borderLeftWidth">medium</property>\r\n                                <property name="borderRightColor">black</property>\r\n                                <property name="borderRightStyle">none</property>\r\n                                <property name="borderRightWidth">medium</property>\r\n                                <property name="borderTopColor">black</property>\r\n                                <property name="borderTopStyle">none</property>\r\n                                <property name="borderTopWidth">medium</property>\r\n                                <property name="marginTop">0pt</property>\r\n                                <property name="marginLeft">0pt</property>\r\n                                <property name="marginBottom">0pt</property>\r\n                                <property name="marginRight">0pt</property>\r\n                                <property name="paddingTop">1pt</property>\r\n                                <property name="paddingLeft">1pt</property>\r\n                                <property name="paddingBottom">1pt</property>\r\n                                <property name="paddingRight">1pt</property>\r\n                                <structure name="numberFormat">\r\n                                    <property name="category">Fixed</property>\r\n                                    <property name="pattern">#,##0;(#,##0)</property>\r\n                                </structure>\r\n                                <property name="textAlign">right</property>\r\n                                <property name="letterSpacing">normal</property>\r\n                                <property name="lineHeight">normal</property>\r\n                                <property name="orphans">2</property>\r\n                                <property name="textTransform">none</property>\r\n                                <property name="whiteSpace">normal</property>\r\n                                <property name="widows">2</property>\r\n                                <property name="wordSpacing">normal</property>\r\n                                <property name="display">block</property>\r\n                                <property name="pageBreakAfter">auto</property>\r\n                                <property name="pageBreakBefore">auto</property>\r\n                                <property name="pageBreakInside">auto</property>\r\n                                <property name="showIfBlank">false</property>\r\n                                <property name="canShrink">false</property>\r\n                                <property name="resultSetColumn">Aggregation_1</property>\r\n                            </data>\r\n                        </cell>\r\n                        <cell id="526"/>\r\n                        <cell id="349"/>\r\n                        <cell id="403"/>\r\n                    </row>\r\n                    <row id="450">\r\n                        <cell id="452">\r\n                            <property name="colSpan">2</property>\r\n                            <property name="rowSpan">1</property>\r\n                            <label id="457">\r\n                                <property name="style">footer</property>\r\n                                <property name="fontWeight">normal</property>\r\n                                <property name="color">black</property>\r\n                                <property name="textAlign">left</property>\r\n                                <text-property name="text">Order Total:</text-property>\r\n                            </label>\r\n                        </cell>\r\n                        <cell id="503">\r\n                            <data id="458">\r\n                                <property name="backgroundAttachment">scroll</property>\r\n                                <property name="backgroundPositionX">0%</property>\r\n                                <property name="backgroundPositionY">0%</property>\r\n                                <property name="backgroundRepeat">repeat</property>\r\n                                <property name="fontFamily">sans-serif</property>\r\n                                <property name="fontSize">10pt</property>\r\n                                <property name="fontWeight">normal</property>\r\n                                <property name="fontStyle">normal</property>\r\n                                <property name="fontVariant">normal</property>\r\n                                <property name="color">black</property>\r\n                                <property name="textLineThrough">none</property>\r\n                                <property name="textOverline">none</property>\r\n                                <property name="textUnderline">none</property>\r\n                                <property name="borderBottomColor">black</property>\r\n                                <property name="borderBottomStyle">none</property>\r\n                                <property name="borderBottomWidth">medium</property>\r\n                                <property name="borderLeftColor">black</property>\r\n                                <property name="borderLeftStyle">none</property>\r\n                                <property name="borderLeftWidth">medium</property>\r\n                                <property name="borderRightColor">black</property>\r\n                                <property name="borderRightStyle">none</property>\r\n                                <property name="borderRightWidth">medium</property>\r\n                                <property name="borderTopColor">black</property>\r\n                                <property name="borderTopStyle">none</property>\r\n                                <property name="borderTopWidth">medium</property>\r\n                                <property name="marginTop">0pt</property>\r\n                                <property name="marginLeft">0pt</property>\r\n                                <property name="marginBottom">0pt</property>\r\n                                <property name="marginRight">0pt</property>\r\n                                <property name="paddingTop">1pt</property>\r\n                                <property name="paddingLeft">1pt</property>\r\n                                <property name="paddingBottom">1pt</property>\r\n                                <property name="paddingRight">1pt</property>\r\n                                <structure name="numberFormat">\r\n                                    <property name="category">Currency</property>\r\n                                    <property name="pattern">$#,##0.00;$(#,##0.00)</property>\r\n                                </structure>\r\n                                <property name="textAlign">right</property>\r\n                                <property name="letterSpacing">normal</property>\r\n                                <property name="lineHeight">normal</property>\r\n                                <property name="orphans">2</property>\r\n                                <property name="textTransform">none</property>\r\n                                <property name="whiteSpace">normal</property>\r\n                                <property name="widows">2</property>\r\n                                <property name="wordSpacing">normal</property>\r\n                                <property name="display">block</property>\r\n                                <property name="pageBreakAfter">auto</property>\r\n                                <property name="pageBreakBefore">auto</property>\r\n                                <property name="pageBreakInside">auto</property>\r\n                                <property name="showIfBlank">false</property>\r\n                                <property name="canShrink">false</property>\r\n                                <property name="resultSetColumn">Aggregation</property>\r\n                            </data>\r\n                        </cell>\r\n                        <cell id="527"/>\r\n                        <cell id="455"/>\r\n                        <cell id="456"/>\r\n                    </row>\r\n                    <row id="459">\r\n                        <cell id="461">\r\n                            <property name="colSpan">2</property>\r\n                            <property name="rowSpan">1</property>\r\n                            <label id="466">\r\n                                <property name="style">footer</property>\r\n                                <property name="fontWeight">normal</property>\r\n                                <property name="color">black</property>\r\n                                <property name="textAlign">left</property>\r\n                                <text-property name="text">Shipping Total:</text-property>\r\n                            </label>\r\n                        </cell>\r\n                        <cell id="504">\r\n                            <data id="467">\r\n                                <property name="backgroundAttachment">scroll</property>\r\n                                <property name="backgroundPositionX">0%</property>\r\n                                <property name="backgroundPositionY">0%</property>\r\n                                <property name="backgroundRepeat">repeat</property>\r\n                                <property name="fontFamily">sans-serif</property>\r\n                                <property name="fontSize">10pt</property>\r\n                                <property name="fontWeight">normal</property>\r\n                                <property name="fontStyle">normal</property>\r\n                                <property name="fontVariant">normal</property>\r\n                                <property name="color">black</property>\r\n                                <property name="textLineThrough">none</property>\r\n                                <property name="textOverline">none</property>\r\n                                <property name="textUnderline">none</property>\r\n                                <property name="borderBottomColor">black</property>\r\n                                <property name="borderBottomStyle">none</property>\r\n                                <property name="borderBottomWidth">medium</property>\r\n                                <property name="borderLeftColor">black</property>\r\n                                <property name="borderLeftStyle">none</property>\r\n                                <property name="borderLeftWidth">medium</property>\r\n                                <property name="borderRightColor">black</property>\r\n                                <property name="borderRightStyle">none</property>\r\n                                <property name="borderRightWidth">medium</property>\r\n                                <property name="borderTopColor">black</property>\r\n                                <property name="borderTopStyle">none</property>\r\n                                <property name="borderTopWidth">medium</property>\r\n                                <property name="marginTop">0pt</property>\r\n                                <property name="marginLeft">0pt</property>\r\n                                <property name="marginBottom">0pt</property>\r\n                                <property name="marginRight">0pt</property>\r\n                                <property name="paddingTop">1pt</property>\r\n                                <property name="paddingLeft">1pt</property>\r\n                                <property name="paddingBottom">1pt</property>\r\n                                <property name="paddingRight">1pt</property>\r\n                                <structure name="numberFormat">\r\n                                    <property name="category">Currency</property>\r\n                                    <property name="pattern">$#,##0.00;$(#,##0.00)</property>\r\n                                </structure>\r\n                                <property name="textAlign">right</property>\r\n                                <property name="letterSpacing">normal</property>\r\n                                <property name="lineHeight">normal</property>\r\n                                <property name="orphans">2</property>\r\n                                <property name="textTransform">none</property>\r\n                                <property name="whiteSpace">normal</property>\r\n                                <property name="widows">2</property>\r\n                                <property name="wordSpacing">normal</property>\r\n                                <property name="display">block</property>\r\n                                <property name="pageBreakAfter">auto</property>\r\n                                <property name="pageBreakBefore">auto</property>\r\n                                <property name="pageBreakInside">auto</property>\r\n                                <property name="showIfBlank">false</property>\r\n                                <property name="canShrink">false</property>\r\n                                <property name="resultSetColumn">ShipTotal</property>\r\n                            </data>\r\n                        </cell>\r\n                        <cell id="528"/>\r\n                        <cell id="464"/>\r\n                        <cell id="465"/>\r\n                    </row>\r\n                </footer>\r\n            </group>\r\n            <detail>\r\n                <row id="10">\r\n                    <cell id="440">\r\n                        <data id="447">\r\n                            <property name="backgroundAttachment">scroll</property>\r\n                            <property name="backgroundPositionX">0%</property>\r\n                            <property name="backgroundPositionY">0%</property>\r\n                            <property name="backgroundRepeat">repeat</property>\r\n                            <property name="fontFamily">sans-serif</property>\r\n                            <property name="fontSize">10pt</property>\r\n                            <property name="fontWeight">normal</property>\r\n                            <property name="fontStyle">normal</property>\r\n                            <property name="fontVariant">normal</property>\r\n                            <property name="color">black</property>\r\n                            <property name="textLineThrough">none</property>\r\n                            <property name="textOverline">none</property>\r\n                            <property name="textUnderline">none</property>\r\n                            <property name="borderBottomColor">black</property>\r\n                            <property name="borderBottomStyle">none</property>\r\n                            <property name="borderBottomWidth">medium</property>\r\n                            <property name="borderLeftColor">black</property>\r\n                            <property name="borderLeftStyle">none</property>\r\n                            <property name="borderLeftWidth">medium</property>\r\n                            <property name="borderRightColor">black</property>\r\n                            <property name="borderRightStyle">none</property>\r\n                            <property name="borderRightWidth">medium</property>\r\n                            <property name="borderTopColor">black</property>\r\n                            <property name="borderTopStyle">none</property>\r\n                            <property name="borderTopWidth">medium</property>\r\n                            <property name="marginTop">0pt</property>\r\n                            <property name="marginLeft">0pt</property>\r\n                            <property name="marginBottom">0pt</property>\r\n                            <property name="marginRight">0pt</property>\r\n                            <property name="paddingTop">1pt</property>\r\n                            <property name="paddingLeft">1pt</property>\r\n                            <property name="paddingBottom">1pt</property>\r\n                            <property name="paddingRight">1pt</property>\r\n                            <structure name="numberFormat">\r\n                                <property name="category">General Number</property>\r\n                                <property name="pattern">General Number</property>\r\n                            </structure>\r\n                            <property name="textAlign">left</property>\r\n                            <property name="letterSpacing">normal</property>\r\n                            <property name="lineHeight">normal</property>\r\n                            <property name="orphans">2</property>\r\n                            <property name="textTransform">none</property>\r\n                            <property name="whiteSpace">normal</property>\r\n                            <property name="widows">2</property>\r\n                            <property name="wordSpacing">normal</property>\r\n                            <property name="display">block</property>\r\n                            <property name="pageBreakAfter">auto</property>\r\n                            <property name="pageBreakBefore">auto</property>\r\n                            <property name="pageBreakInside">auto</property>\r\n                            <property name="showIfBlank">false</property>\r\n                            <property name="canShrink">false</property>\r\n                            <property name="resultSetColumn">order_num</property>\r\n                        </data>\r\n                    </cell>\r\n                    <cell id="411">\r\n                        <data id="436">\r\n                            <structure name="dateTimeFormat">\r\n                                <property name="category">Medium Date</property>\r\n                                <property name="pattern">Medium Date</property>\r\n                            </structure>\r\n                            <structure name="numberFormat">\r\n                                <property name="category">Unformatted</property>\r\n                            </structure>\r\n                            <property name="textAlign">left</property>\r\n                            <property name="resultSetColumn">order_date</property>\r\n                        </data>\r\n                    </cell>\r\n                    <cell id="501">\r\n                        <data id="536">\r\n                            <property name="textAlign">left</property>\r\n                            <property name="resultSetColumn">cust_first_name</property>\r\n                        </data>\r\n                    </cell>\r\n                    <cell id="525">\r\n                        <data id="538">\r\n                            <property name="textAlign">left</property>\r\n                            <property name="resultSetColumn">cust_last_name</property>\r\n                        </data>\r\n                    </cell>\r\n                    <cell id="14">\r\n                        <data id="327">\r\n                            <property name="textAlign">right</property>\r\n                            <property name="resultSetColumn">order_total</property>\r\n                        </data>\r\n                    </cell>\r\n                    <cell id="402">\r\n                        <data id="407">\r\n                            <property name="backgroundAttachment">scroll</property>\r\n                            <property name="backgroundPositionX">0%</property>\r\n                            <property name="backgroundPositionY">0%</property>\r\n                            <property name="backgroundRepeat">repeat</property>\r\n                            <property name="fontFamily">sans-serif</property>\r\n                            <property name="fontSize">10pt</property>\r\n                            <property name="fontWeight">normal</property>\r\n                            <property name="fontStyle">normal</property>\r\n                            <property name="fontVariant">normal</property>\r\n                            <property name="color">black</property>\r\n                            <property name="textLineThrough">none</property>\r\n                            <property name="textOverline">none</property>\r\n                            <property name="textUnderline">none</property>\r\n                            <property name="borderBottomColor">black</property>\r\n                            <property name="borderBottomStyle">none</property>\r\n                            <property name="borderBottomWidth">medium</property>\r\n                            <property name="borderLeftColor">black</property>\r\n                            <property name="borderLeftStyle">none</property>\r\n                            <property name="borderLeftWidth">medium</property>\r\n                            <property name="borderRightColor">black</property>\r\n                            <property name="borderRightStyle">none</property>\r\n                            <property name="borderRightWidth">medium</property>\r\n                            <property name="borderTopColor">black</property>\r\n                            <property name="borderTopStyle">none</property>\r\n                            <property name="borderTopWidth">medium</property>\r\n                            <property name="marginTop">0pt</property>\r\n                            <property name="marginLeft">0pt</property>\r\n                            <property name="marginBottom">0pt</property>\r\n                            <property name="marginRight">0pt</property>\r\n                            <property name="paddingTop">1pt</property>\r\n                            <property name="paddingLeft">1pt</property>\r\n                            <property name="paddingBottom">1pt</property>\r\n                            <property name="paddingRight">1pt</property>\r\n                            <structure name="numberFormat">\r\n                                <property name="category">Currency</property>\r\n                                <property name="pattern">$#,##0.00;$(#,##0.00)</property>\r\n                            </structure>\r\n                            <property name="textAlign">right</property>\r\n                            <property name="letterSpacing">normal</property>\r\n                            <property name="lineHeight">normal</property>\r\n                            <property name="orphans">2</property>\r\n                            <property name="textTransform">none</property>\r\n                            <property name="whiteSpace">normal</property>\r\n                            <property name="widows">2</property>\r\n                            <property name="wordSpacing">normal</property>\r\n                            <property name="display">block</property>\r\n                            <property name="pageBreakAfter">auto</property>\r\n                            <property name="pageBreakBefore">auto</property>\r\n                            <property name="pageBreakInside">auto</property>\r\n                            <property name="showIfBlank">false</property>\r\n                            <property name="canShrink">false</property>\r\n                            <property name="resultSetColumn">shipping_total</property>\r\n                        </data>\r\n                    </cell>\r\n                </row>\r\n            </detail>\r\n            <footer>\r\n                <row id="641">\r\n                    <cell id="642">\r\n                        <property name="borderTopColor">#000000</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                        <property name="paddingTop">10pt</property>\r\n                        <property name="paddingBottom">10pt</property>\r\n                    </cell>\r\n                    <cell id="643">\r\n                        <property name="borderTopColor">#000000</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                        <property name="paddingTop">10pt</property>\r\n                        <property name="paddingBottom">10pt</property>\r\n                    </cell>\r\n                    <cell id="644">\r\n                        <property name="borderTopColor">#000000</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                        <property name="paddingTop">10pt</property>\r\n                        <property name="paddingBottom">10pt</property>\r\n                    </cell>\r\n                    <cell id="645">\r\n                        <property name="borderTopColor">#000000</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                        <property name="paddingTop">10pt</property>\r\n                        <property name="paddingBottom">10pt</property>\r\n                    </cell>\r\n                    <cell id="646">\r\n                        <property name="borderTopColor">#000000</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                        <property name="paddingTop">10pt</property>\r\n                        <property name="paddingBottom">10pt</property>\r\n                    </cell>\r\n                    <cell id="647">\r\n                        <property name="borderTopColor">#000000</property>\r\n                        <property name="borderTopStyle">solid</property>\r\n                        <property name="borderTopWidth">thin</property>\r\n                        <property name="paddingTop">10pt</property>\r\n                        <property name="paddingBottom">10pt</property>\r\n                    </cell>\r\n                </row>\r\n                <row id="355">\r\n                    <cell id="442">\r\n                        <property name="colSpan">1</property>\r\n                        <property name="rowSpan">1</property>\r\n                        <property name="paddingTop">1pt</property>\r\n                        <label id="651">\r\n                            <text-property name="text">Run on</text-property>\r\n                        </label>\r\n                    </cell>\r\n                    <cell id="650">\r\n                        <property name="colSpan">2</property>\r\n                        <property name="rowSpan">1</property>\r\n                        <property name="paddingTop">1pt</property>\r\n                        <data id="649">\r\n                            <property name="resultSetColumn">Column Binding_1</property>\r\n                        </data>\r\n                    </cell>\r\n                    <cell id="531">\r\n                        <property name="paddingTop">1pt</property>\r\n                    </cell>\r\n                    <cell id="358">\r\n                        <property name="paddingTop">1pt</property>\r\n                    </cell>\r\n                    <cell id="404">\r\n                        <property name="paddingTop">1pt</property>\r\n                        <property name="textAlign">right</property>\r\n                        <data id="652">\r\n                            <property name="resultSetColumn">Column Binding_2</property>\r\n                        </data>\r\n                    </cell>\r\n                </row>\r\n            </footer>\r\n        </table>\r\n    </body>\r\n</report>\r\n', 'Y', 'default');
/*!40000 ALTER TABLE `report` ENABLE KEYS */;


# Dumping structure for table jada.sequence
CREATE TABLE IF NOT EXISTS `sequence` (
  `sequence_id` varchar(20) NOT NULL,
  `next_seqience_num` bigint(20) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  PRIMARY KEY (`sequence_id`),
  KEY `FK507077C170EBDE65` (`site_id`),
  CONSTRAINT `FK507077C170EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.sequence: ~0 rows (approximately)
/*!40000 ALTER TABLE `sequence` DISABLE KEYS */;
/*!40000 ALTER TABLE `sequence` ENABLE KEYS */;


# Dumping structure for table jada.shipping_method
CREATE TABLE IF NOT EXISTS `shipping_method` (
  `shipping_method_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `shipping_method_lang_id` bigint(20) NOT NULL,
  PRIMARY KEY (`shipping_method_id`),
  KEY `FK8984D092915DE851` (`shipping_method_lang_id`),
  KEY `FK8984D09270EBDE65` (`site_id`),
  CONSTRAINT `FK8984D09270EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK8984D092915DE851` FOREIGN KEY (`shipping_method_lang_id`) REFERENCES `shipping_method_language` (`shipping_method_lang_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table jada.shipping_method: ~1 rows (approximately)
/*!40000 ALTER TABLE `shipping_method` DISABLE KEYS */;
INSERT INTO `shipping_method` (`shipping_method_id`, `published`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `seq_num`, `site_id`, `shipping_method_lang_id`) VALUES
	(1, 'Y', 'admin', '2009-08-16 08:00:53', 'admin', '2009-08-16 08:37:55', 0, 'default', 1);
/*!40000 ALTER TABLE `shipping_method` ENABLE KEYS */;


# Dumping structure for table jada.shipping_method_language
CREATE TABLE IF NOT EXISTS `shipping_method_language` (
  `shipping_method_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `shipping_method_name` varchar(40) DEFAULT NULL,
  `shipping_method_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`shipping_method_lang_id`),
  KEY `FK8DFF7865ADC3D462` (`shipping_method_id`),
  KEY `FK8DFF7865473E64D1` (`site_profile_class_id`),
  CONSTRAINT `FK8DFF7865473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FK8DFF7865ADC3D462` FOREIGN KEY (`shipping_method_id`) REFERENCES `shipping_method` (`shipping_method_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table jada.shipping_method_language: ~1 rows (approximately)
/*!40000 ALTER TABLE `shipping_method_language` DISABLE KEYS */;
INSERT INTO `shipping_method_language` (`shipping_method_lang_id`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `shipping_method_name`, `shipping_method_id`, `site_profile_class_id`) VALUES
	(1, 'admin', '2009-12-29 16:08:50', 'admin', '2009-12-29 16:08:50', 'Ground', 1, 2);
/*!40000 ALTER TABLE `shipping_method_language` ENABLE KEYS */;


# Dumping structure for table jada.shipping_method_region
CREATE TABLE IF NOT EXISTS `shipping_method_region` (
  `shipping_method_region_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `shipping_method_id` bigint(20) DEFAULT NULL,
  `shipping_region_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`shipping_method_region_id`),
  KEY `FK1694EB41ADC3D462` (`shipping_method_id`),
  KEY `FK1694EB41EDA4B782` (`shipping_region_id`),
  CONSTRAINT `FK1694EB41EDA4B782` FOREIGN KEY (`shipping_region_id`) REFERENCES `shipping_region` (`shipping_region_id`),
  CONSTRAINT `FK1694EB41ADC3D462` FOREIGN KEY (`shipping_method_id`) REFERENCES `shipping_method` (`shipping_method_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table jada.shipping_method_region: ~1 rows (approximately)
/*!40000 ALTER TABLE `shipping_method_region` DISABLE KEYS */;
INSERT INTO `shipping_method_region` (`shipping_method_region_id`, `published`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `shipping_method_id`, `shipping_region_id`) VALUES
	(1, 'Y', 'admin', '2009-12-29 16:09:39', 'admin', '2009-12-29 16:09:39', 1, 1);
/*!40000 ALTER TABLE `shipping_method_region` ENABLE KEYS */;


# Dumping structure for table jada.shipping_method_region_type
CREATE TABLE IF NOT EXISTS `shipping_method_region_type` (
  `shipping_method_region_type_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `shipping_method_id` bigint(20) DEFAULT NULL,
  `shipping_method_region_id` bigint(20) DEFAULT NULL,
  `shipping_rate_id` bigint(20) DEFAULT NULL,
  `shipping_region_id` bigint(20) DEFAULT NULL,
  `shipping_type_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`shipping_method_region_type_id`),
  UNIQUE KEY `shipping_rate_id` (`shipping_rate_id`),
  KEY `FK5D223D78ADC3D462` (`shipping_method_id`),
  KEY `FK5D223D781E1D7BC2` (`shipping_type_id`),
  KEY `FK5D223D7832CB56E7` (`shipping_method_region_id`),
  KEY `FK5D223D78EDA4B782` (`shipping_region_id`),
  KEY `FK5D223D788B93D202` (`shipping_rate_id`),
  CONSTRAINT `FK5D223D788B93D202` FOREIGN KEY (`shipping_rate_id`) REFERENCES `shipping_rate` (`shipping_rate_id`),
  CONSTRAINT `FK5D223D781E1D7BC2` FOREIGN KEY (`shipping_type_id`) REFERENCES `shipping_type` (`shipping_type_id`),
  CONSTRAINT `FK5D223D7832CB56E7` FOREIGN KEY (`shipping_method_region_id`) REFERENCES `shipping_method_region` (`shipping_method_region_id`),
  CONSTRAINT `FK5D223D78ADC3D462` FOREIGN KEY (`shipping_method_id`) REFERENCES `shipping_method` (`shipping_method_id`),
  CONSTRAINT `FK5D223D78EDA4B782` FOREIGN KEY (`shipping_region_id`) REFERENCES `shipping_region` (`shipping_region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table jada.shipping_method_region_type: ~1 rows (approximately)
/*!40000 ALTER TABLE `shipping_method_region_type` DISABLE KEYS */;
INSERT INTO `shipping_method_region_type` (`shipping_method_region_type_id`, `published`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `shipping_method_id`, `shipping_method_region_id`, `shipping_rate_id`, `shipping_region_id`, `shipping_type_id`) VALUES
	(1, 'N', 'admin', '2009-12-29 16:09:39', 'admin', '2009-12-29 16:09:39', 1, 1, NULL, 1, 1);
/*!40000 ALTER TABLE `shipping_method_region_type` ENABLE KEYS */;


# Dumping structure for table jada.shipping_rate
CREATE TABLE IF NOT EXISTS `shipping_rate` (
  `shipping_rate_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `shipping_additional_rate_percentage` float DEFAULT NULL,
  `shipping_rate_percentage` float DEFAULT NULL,
  `shipping_rate_curr_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`shipping_rate_id`),
  KEY `FK206284D110255EC4` (`shipping_rate_curr_id`),
  CONSTRAINT `FK206284D110255EC4` FOREIGN KEY (`shipping_rate_curr_id`) REFERENCES `shipping_rate_currency` (`shipping_rate_curr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.shipping_rate: ~0 rows (approximately)
/*!40000 ALTER TABLE `shipping_rate` DISABLE KEYS */;
/*!40000 ALTER TABLE `shipping_rate` ENABLE KEYS */;


# Dumping structure for table jada.shipping_rate_currency
CREATE TABLE IF NOT EXISTS `shipping_rate_currency` (
  `shipping_rate_curr_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `shipping_additional_rate_fee` float DEFAULT NULL,
  `shipping_rate_fee` float DEFAULT NULL,
  `shipping_rate_id` bigint(20) DEFAULT NULL,
  `site_currency_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`shipping_rate_curr_id`),
  KEY `FKA3D877BF96324A6D` (`site_currency_class_id`),
  KEY `FKA3D877BF8B93D202` (`shipping_rate_id`),
  CONSTRAINT `FKA3D877BF8B93D202` FOREIGN KEY (`shipping_rate_id`) REFERENCES `shipping_rate` (`shipping_rate_id`),
  CONSTRAINT `FKA3D877BF96324A6D` FOREIGN KEY (`site_currency_class_id`) REFERENCES `site_currency_class` (`site_currency_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.shipping_rate_currency: ~0 rows (approximately)
/*!40000 ALTER TABLE `shipping_rate_currency` DISABLE KEYS */;
/*!40000 ALTER TABLE `shipping_rate_currency` ENABLE KEYS */;


# Dumping structure for table jada.shipping_region
CREATE TABLE IF NOT EXISTS `shipping_region` (
  `shipping_region_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `shipping_region_name` varchar(40) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `system_record` char(1) NOT NULL,
  PRIMARY KEY (`shipping_region_id`),
  UNIQUE KEY `shipping_region_name` (`shipping_region_name`,`site_id`),
  KEY `FK920726A570EBDE65` (`site_id`),
  CONSTRAINT `FK920726A570EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.shipping_region: ~2 rows (approximately)
/*!40000 ALTER TABLE `shipping_region` DISABLE KEYS */;
INSERT INTO `shipping_region` (`shipping_region_id`, `published`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `shipping_region_name`, `site_id`, `system_record`) VALUES
	(1, 'Y', 'root', '2009-09-21 21:12:42', 'root', '2009-09-21 21:12:34', 'default', 'default', 'Y'),
	(2, 'Y', 'system', '2009-09-29 19:18:45', 'system', '2009-09-29 19:18:33', 'default', '_system', 'Y');
/*!40000 ALTER TABLE `shipping_region` ENABLE KEYS */;


# Dumping structure for table jada.shipping_region_zip
CREATE TABLE IF NOT EXISTS `shipping_region_zip` (
  `shipping_region_zip_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `zip_code_end` varchar(40) NOT NULL,
  `zip_code_expression` char(1) NOT NULL,
  `zip_code_start` varchar(40) NOT NULL,
  `shipping_region_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`shipping_region_zip_id`),
  KEY `FK57465287EDA4B782` (`shipping_region_id`),
  CONSTRAINT `FK57465287EDA4B782` FOREIGN KEY (`shipping_region_id`) REFERENCES `shipping_region` (`shipping_region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.shipping_region_zip: ~0 rows (approximately)
/*!40000 ALTER TABLE `shipping_region_zip` DISABLE KEYS */;
/*!40000 ALTER TABLE `shipping_region_zip` ENABLE KEYS */;


# Dumping structure for table jada.shipping_type
CREATE TABLE IF NOT EXISTS `shipping_type` (
  `shipping_type_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `shipping_type_name` varchar(40) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `system_record` char(1) NOT NULL,
  PRIMARY KEY (`shipping_type_id`),
  UNIQUE KEY `shipping_type_name` (`shipping_type_name`,`site_id`),
  KEY `FK2063C72B70EBDE65` (`site_id`),
  CONSTRAINT `FK2063C72B70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table jada.shipping_type: ~1 rows (approximately)
/*!40000 ALTER TABLE `shipping_type` DISABLE KEYS */;
INSERT INTO `shipping_type` (`shipping_type_id`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `shipping_type_name`, `site_id`, `system_record`) VALUES
	(1, 'admin', '2009-08-15 14:39:10', 'admin', '2009-08-15 14:39:10', 'Small', 'default', 'Y');
/*!40000 ALTER TABLE `shipping_type` ENABLE KEYS */;


# Dumping structure for table jada.ship_detail
CREATE TABLE IF NOT EXISTS `ship_detail` (
  `ship_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_ship_qty` int(11) DEFAULT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `order_item_detail_id` bigint(20) DEFAULT NULL,
  `ship_header_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ship_detail_id`),
  KEY `FKB5017FB4E8BE3F7E` (`ship_header_id`),
  KEY `FKB5017FB447EC5AF5` (`order_item_detail_id`),
  CONSTRAINT `FKB5017FB447EC5AF5` FOREIGN KEY (`order_item_detail_id`) REFERENCES `order_item_detail` (`order_item_detail_id`),
  CONSTRAINT `FKB5017FB4E8BE3F7E` FOREIGN KEY (`ship_header_id`) REFERENCES `ship_header` (`ship_header_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.ship_detail: ~0 rows (approximately)
/*!40000 ALTER TABLE `ship_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `ship_detail` ENABLE KEYS */;


# Dumping structure for table jada.ship_header
CREATE TABLE IF NOT EXISTS `ship_header` (
  `ship_header_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `ship_date` datetime NOT NULL,
  `ship_num` varchar(20) NOT NULL,
  `ship_status` varchar(1) NOT NULL,
  `update_inventory` varchar(1) NOT NULL,
  `order_header_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ship_header_id`),
  KEY `FKBBCC49F0267D6B6C` (`order_header_id`),
  CONSTRAINT `FKBBCC49F0267D6B6C` FOREIGN KEY (`order_header_id`) REFERENCES `order_header` (`order_header_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.ship_header: ~0 rows (approximately)
/*!40000 ALTER TABLE `ship_header` DISABLE KEYS */;
/*!40000 ALTER TABLE `ship_header` ENABLE KEYS */;


# Dumping structure for table jada.site
CREATE TABLE IF NOT EXISTS `site` (
  `site_id` varchar(20) NOT NULL,
  `active` char(1) NOT NULL,
  `listing_page_size` int(11) NOT NULL,
  `mail_smtp_account` varchar(50) NOT NULL,
  `mail_smtp_host` varchar(50) NOT NULL,
  `mail_smtp_password` varchar(50) NOT NULL,
  `mail_smtp_port` varchar(4) NOT NULL,
  `manage_inventory` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `share_inventory` char(1) NOT NULL,
  `single_checkout` char(1) NOT NULL,
  `site_desc` varchar(50) NOT NULL,
  `system_record` char(1) NOT NULL,
  `site_currency_class_default_id` bigint(20) DEFAULT NULL,
  `site_domain_default_id` bigint(20) DEFAULT NULL,
  `site_profile_class_default_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`site_id`),
  KEY `FK35DF47F27CB82B` (`site_currency_class_default_id`),
  KEY `FK35DF47BD2C8BF2` (`site_domain_default_id`),
  KEY `FK35DF477408FA8F` (`site_profile_class_default_id`),
  CONSTRAINT `FK35DF477408FA8F` FOREIGN KEY (`site_profile_class_default_id`) REFERENCES `site_profile_class` (`site_profile_class_id`),
  CONSTRAINT `FK35DF47BD2C8BF2` FOREIGN KEY (`site_domain_default_id`) REFERENCES `site_domain` (`site_domain_id`),
  CONSTRAINT `FK35DF47F27CB82B` FOREIGN KEY (`site_currency_class_default_id`) REFERENCES `site_currency_class` (`site_currency_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.site: ~2 rows (approximately)
/*!40000 ALTER TABLE `site` DISABLE KEYS */;
INSERT INTO `site` (`site_id`, `active`, `listing_page_size`, `mail_smtp_account`, `mail_smtp_host`, `mail_smtp_password`, `mail_smtp_port`, `manage_inventory`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `share_inventory`, `single_checkout`, `site_desc`, `system_record`, `site_currency_class_default_id`, `site_domain_default_id`, `site_profile_class_default_id`) VALUES
	('default', 'Y', 50, ' ', '', '', ' ', 'Y', 'system', '2009-06-10 18:44:30', 'admin', '2009-09-13 16:56:28', 'Y', 'N', 'Default site', 'Y', 2, 2, 2),
	('_system', 'N', 50, ' ', '', ' ', ' ', 'Y', 'system', '2009-06-10 18:44:30', 'system', '2009-06-10 18:44:17', 'Y', 'N', 'System', 'Y', 1, 1, 1);
/*!40000 ALTER TABLE `site` ENABLE KEYS */;


# Dumping structure for table jada.site_currency
CREATE TABLE IF NOT EXISTS `site_currency` (
  `site_currency_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` char(1) NOT NULL,
  `cash_payment` char(1) DEFAULT NULL,
  `exchange_rate` float NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `paypal_payment_gateway_id` bigint(20) DEFAULT NULL,
  `payment_gateway_id` bigint(20) DEFAULT NULL,
  `site_currency_class_id` bigint(20) DEFAULT NULL,
  `site_domain_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`site_currency_id`),
  KEY `FK4D04820996324A6D` (`site_currency_class_id`),
  KEY `FK4D04820971A2EB44` (`paypal_payment_gateway_id`),
  KEY `FK4D048209B8784434` (`site_domain_id`),
  KEY `FK4D04820963814678` (`payment_gateway_id`),
  CONSTRAINT `FK4D04820963814678` FOREIGN KEY (`payment_gateway_id`) REFERENCES `payment_gateway` (`payment_gateway_id`),
  CONSTRAINT `FK4D04820971A2EB44` FOREIGN KEY (`paypal_payment_gateway_id`) REFERENCES `payment_gateway` (`payment_gateway_id`),
  CONSTRAINT `FK4D04820996324A6D` FOREIGN KEY (`site_currency_class_id`) REFERENCES `site_currency_class` (`site_currency_class_id`),
  CONSTRAINT `FK4D048209B8784434` FOREIGN KEY (`site_domain_id`) REFERENCES `site_domain` (`site_domain_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.site_currency: ~2 rows (approximately)
/*!40000 ALTER TABLE `site_currency` DISABLE KEYS */;
INSERT INTO `site_currency` (`site_currency_id`, `active`, `cash_payment`, `exchange_rate`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `seq_num`, `paypal_payment_gateway_id`, `payment_gateway_id`, `site_currency_class_id`, `site_domain_id`) VALUES
	(1, 'Y', 'Y', 1, 'system', '2009-08-10 18:22:38', 'system', '2009-08-10 18:22:29', 1, NULL, NULL, 1, 1),
	(2, 'Y', 'Y', 1, 'system', '2009-08-10 18:23:13', 'admin', '2009-11-07 09:00:29', 2, NULL, NULL, 2, 2);
/*!40000 ALTER TABLE `site_currency` ENABLE KEYS */;


# Dumping structure for table jada.site_currency_class
CREATE TABLE IF NOT EXISTS `site_currency_class` (
  `site_currency_class_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `currency_locale_country` varchar(2) NOT NULL,
  `currency_locale_language` varchar(2) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_currency_class_name` varchar(40) NOT NULL,
  `system_record` char(1) NOT NULL,
  `currency_id` bigint(20) DEFAULT NULL,
  `site_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`site_currency_class_id`),
  KEY `FKE7F523C2C4B5B4A5` (`currency_id`),
  KEY `FKE7F523C270EBDE65` (`site_id`),
  CONSTRAINT `FKE7F523C270EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FKE7F523C2C4B5B4A5` FOREIGN KEY (`currency_id`) REFERENCES `currency` (`currency_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.site_currency_class: ~2 rows (approximately)
/*!40000 ALTER TABLE `site_currency_class` DISABLE KEYS */;
INSERT INTO `site_currency_class` (`site_currency_class_id`, `currency_locale_country`, `currency_locale_language`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `site_currency_class_name`, `system_record`, `currency_id`, `site_id`) VALUES
	(1, 'US', 'en', 'root', '2009-06-11 08:28:22', 'root', '2009-06-11 08:28:22', 'USD', 'Y', 150, '_system'),
	(2, 'US', 'en', 'root', '2009-06-11 09:46:37', 'root', '2009-06-11 09:46:32', 'USD', 'Y', 328, 'default');
/*!40000 ALTER TABLE `site_currency_class` ENABLE KEYS */;


# Dumping structure for table jada.site_domain
CREATE TABLE IF NOT EXISTS `site_domain` (
  `site_domain_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_domain_name` varchar(50) NOT NULL,
  `site_domain_prefix` varchar(50) NOT NULL,
  `site_public_port_num` varchar(4) NOT NULL,
  `site_secure_port_num` varchar(4) NOT NULL,
  `site_ssl_enabled` char(1) NOT NULL,
  `site_currency_class_id` bigint(20) DEFAULT NULL,
  `home_page_id` bigint(20) DEFAULT NULL,
  `site_id` varchar(20) DEFAULT NULL,
  `site_currency_default_id` bigint(20) DEFAULT NULL,
  `site_domain_lang_id` bigint(20) DEFAULT NULL,
  `site_profile_default_id` bigint(20) DEFAULT NULL,
  `template_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`site_domain_id`),
  KEY `FK921884BC123762B7` (`site_domain_lang_id`),
  KEY `FK921884BCE4A41352` (`site_currency_default_id`),
  KEY `FK921884BCEA29B81E` (`site_profile_default_id`),
  KEY `FK921884BC96324A6D` (`site_currency_class_id`),
  KEY `FK921884BC9A27F005` (`template_id`),
  KEY `FK921884BC70EBDE65` (`site_id`),
  KEY `FK921884BCF0B9EA04` (`home_page_id`),
  CONSTRAINT `FK921884BCF0B9EA04` FOREIGN KEY (`home_page_id`) REFERENCES `home_page` (`home_page_id`),
  CONSTRAINT `FK921884BC123762B7` FOREIGN KEY (`site_domain_lang_id`) REFERENCES `site_domain_language` (`site_domain_lang_id`),
  CONSTRAINT `FK921884BC70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK921884BC96324A6D` FOREIGN KEY (`site_currency_class_id`) REFERENCES `site_currency_class` (`site_currency_class_id`),
  CONSTRAINT `FK921884BC9A27F005` FOREIGN KEY (`template_id`) REFERENCES `template` (`template_id`),
  CONSTRAINT `FK921884BCE4A41352` FOREIGN KEY (`site_currency_default_id`) REFERENCES `site_currency` (`site_currency_id`),
  CONSTRAINT `FK921884BCEA29B81E` FOREIGN KEY (`site_profile_default_id`) REFERENCES `site_profile` (`site_profile_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.site_domain: ~2 rows (approximately)
/*!40000 ALTER TABLE `site_domain` DISABLE KEYS */;
INSERT INTO `site_domain` (`site_domain_id`, `active`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `site_domain_name`, `site_domain_prefix`, `site_public_port_num`, `site_secure_port_num`, `site_ssl_enabled`, `site_currency_class_id`, `home_page_id`, `site_id`, `site_currency_default_id`, `site_domain_lang_id`, `site_profile_default_id`, `template_id`) VALUES
	(1, 'Y', 'system', '2009-06-10 22:26:48', 'system', '2009-06-10 22:26:41', 'localhost', '', '', ' ', 'N', 1, NULL, '_system', 1, 1, 1, 3),
	(2, 'Y', 'admin', '2009-08-22 17:39:29', 'admin', '2009-11-07 09:00:29', 'localhost', 'default', '8080', ' ', 'N', 2, 1, 'default', 2, 2, 2, 1);
/*!40000 ALTER TABLE `site_domain` ENABLE KEYS */;


# Dumping structure for table jada.site_domain_language
CREATE TABLE IF NOT EXISTS `site_domain_language` (
  `site_domain_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_domain_param` longtext,
  `site_logo_content_type` varchar(20) DEFAULT NULL,
  `site_logo_value` longblob,
  `site_name` varchar(50) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  `site_domain_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`site_domain_lang_id`),
  KEY `FK3262D37B473E64D1` (`site_profile_class_id`),
  KEY `FK3262D37BB8784434` (`site_domain_id`),
  CONSTRAINT `FK3262D37BB8784434` FOREIGN KEY (`site_domain_id`) REFERENCES `site_domain` (`site_domain_id`),
  CONSTRAINT `FK3262D37B473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.site_domain_language: ~2 rows (approximately)
/*!40000 ALTER TABLE `site_domain_language` DISABLE KEYS */;
INSERT INTO `site_domain_language` (`site_domain_lang_id`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `site_domain_param`, `site_logo_content_type`, `site_logo_value`, `site_name`, `site_profile_class_id`, `site_domain_id`) VALUES
	(1, 'system', '2009-08-10 18:01:15', 'system', '2009-08-10 18:01:03', '<?xml version="1.0" encoding="ISO-8859-1"?><SiteDomainParamBean></SiteDomainParamBean>', NULL, NULL, 'Local host', 1, 1),
	(2, 'admin', '2009-11-07 09:00:27', 'admin', '2009-11-07 09:00:29', '<?xml version="1.0" encoding="UTF-8"?>\n<site-domain-param-bean><template-footer>This is a site footer.</template-footer><business-address2></business-address2><checkout-notification-email></checkout-notification-email><business-address1></business-address1><checkout-shopping-cart-message></checkout-shopping-cart-message><subject-notification></subject-notification><business-postal-code></business-postal-code><mail-from-cust-sales></mail-from-cust-sales><business-email></business-email><mail-from-pwd-reset></mail-from-pwd-reset><subject-pwd-reset></subject-pwd-reset><payment-process-type>A</payment-process-type><mail-from-notification></mail-from-notification><business-fax></business-fax><business-contact-name></business-contact-name><subject-cust-sales></subject-cust-sales><business-city></business-city><category-page-size></category-page-size><business-company></business-company><business-country-code>AF</business-country-code><business-state-code>AB</business-state-code><business-phone></business-phone></site-domain-param-bean>', '', NULL, 'default', 2, 2);
/*!40000 ALTER TABLE `site_domain_language` ENABLE KEYS */;


# Dumping structure for table jada.site_param
CREATE TABLE IF NOT EXISTS `site_param` (
  `site_param_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_param_name` varchar(60) NOT NULL,
  `site_param_value` longtext NOT NULL,
  PRIMARY KEY (`site_param_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.site_param: ~0 rows (approximately)
/*!40000 ALTER TABLE `site_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `site_param` ENABLE KEYS */;


# Dumping structure for table jada.site_profile
CREATE TABLE IF NOT EXISTS `site_profile` (
  `site_profile_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `site_domain_id` bigint(20) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`site_profile_id`),
  KEY `FK30FFD8B1473E64D1` (`site_profile_class_id`),
  KEY `FK30FFD8B1B8784434` (`site_domain_id`),
  CONSTRAINT `FK30FFD8B1B8784434` FOREIGN KEY (`site_domain_id`) REFERENCES `site_domain` (`site_domain_id`),
  CONSTRAINT `FK30FFD8B1473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.site_profile: ~2 rows (approximately)
/*!40000 ALTER TABLE `site_profile` DISABLE KEYS */;
INSERT INTO `site_profile` (`site_profile_id`, `active`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `seq_num`, `site_domain_id`, `site_profile_class_id`) VALUES
	(1, 'Y', 'system', '2009-08-10 18:24:46', 'system', '2009-08-10 18:24:40', 1, 1, 1),
	(2, 'Y', 'system', '2009-08-10 18:25:34', 'admin', '2009-11-07 09:00:29', 1, 2, 2);
/*!40000 ALTER TABLE `site_profile` ENABLE KEYS */;


# Dumping structure for table jada.site_profile_class
CREATE TABLE IF NOT EXISTS `site_profile_class` (
  `site_profile_class_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_profile_class_name` varchar(40) NOT NULL,
  `site_profile_class_native_name` varchar(40) NOT NULL,
  `system_record` char(1) NOT NULL,
  `lang_id` bigint(20) DEFAULT NULL,
  `site_id` varchar(20) DEFAULT NULL,
  `site_domain_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`site_profile_class_id`),
  KEY `FKB2AEDC6A70EBDE65` (`site_id`),
  KEY `FKB2AEDC6AB8784434` (`site_domain_id`),
  KEY `FKB2AEDC6A1C2A8CEF` (`lang_id`),
  CONSTRAINT `FKB2AEDC6A1C2A8CEF` FOREIGN KEY (`lang_id`) REFERENCES `language` (`lang_id`),
  CONSTRAINT `FKB2AEDC6A70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FKB2AEDC6AB8784434` FOREIGN KEY (`site_domain_id`) REFERENCES `site_domain` (`site_domain_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# Dumping data for table jada.site_profile_class: ~2 rows (approximately)
/*!40000 ALTER TABLE `site_profile_class` DISABLE KEYS */;
INSERT INTO `site_profile_class` (`site_profile_class_id`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `site_profile_class_name`, `site_profile_class_native_name`, `system_record`, `lang_id`, `site_id`, `site_domain_id`) VALUES
	(1, 'system', '2009-08-10 18:05:54', 'system', '2009-08-10 18:05:44', 'English', 'English', 'Y', 1, '_system', 1),
	(2, 'system', '2009-08-10 18:07:08', 'admin', '2009-09-28 19:31:26', 'English', 'English', 'Y', 1, 'default', 2);
/*!40000 ALTER TABLE `site_profile_class` ENABLE KEYS */;


# Dumping structure for table jada.state
CREATE TABLE IF NOT EXISTS `state` (
  `state_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `state_code` varchar(2) NOT NULL,
  `state_name` varchar(40) NOT NULL,
  `country_id` bigint(20) DEFAULT NULL,
  `shipping_region_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`state_id`),
  KEY `FK68AC4912196FC0F` (`country_id`),
  KEY `FK68AC491EDA4B782` (`shipping_region_id`),
  CONSTRAINT `FK68AC491EDA4B782` FOREIGN KEY (`shipping_region_id`) REFERENCES `shipping_region` (`shipping_region_id`),
  CONSTRAINT `FK68AC4912196FC0F` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8;

# Dumping data for table jada.state: ~134 rows (approximately)
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
INSERT INTO `state` (`state_id`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `state_code`, `state_name`, `country_id`, `shipping_region_id`) VALUES
	(1, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AL', 'Alabama', 229, NULL),
	(2, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AK', 'Alaska', 229, NULL),
	(3, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AZ', 'Arizona', 229, NULL),
	(4, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AR', 'Arkansas', 229, NULL),
	(5, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'CA', 'California', 229, NULL),
	(6, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'CO', 'Colorado', 229, NULL),
	(7, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'CT', 'Connecticut', 229, NULL),
	(8, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'DE', 'Delaware', 229, NULL),
	(9, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'DC', 'District of Columbia', 229, NULL),
	(10, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'FL', 'Florida', 229, NULL),
	(11, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'GA', 'Georgia', 229, NULL),
	(12, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'HI', 'Hawaii', 229, NULL),
	(13, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'ID', 'Idaho', 229, NULL),
	(14, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'IL', 'Illinois', 229, NULL),
	(15, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'IN', 'Indiana', 229, NULL),
	(16, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'IA', 'Iowa', 229, NULL),
	(17, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'KS', 'Kansas', 229, NULL),
	(18, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'KY', 'Kentucky', 229, NULL),
	(19, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'LA', 'Louisiana', 229, NULL),
	(20, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'ME', 'Maine', 229, NULL),
	(21, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MD', 'Maryland', 229, NULL),
	(22, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MA', 'Massachusetts', 229, NULL),
	(23, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MI', 'Michigan', 229, NULL),
	(24, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MN', 'Minnesota', 229, NULL),
	(25, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MS', 'Mississippi', 229, NULL),
	(26, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MO', 'Missouri', 229, NULL),
	(27, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MT', 'Montana', 229, NULL),
	(28, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NE', 'Nebraska', 229, NULL),
	(29, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NV', 'Nevada', 229, NULL),
	(30, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NH', 'New Hampshire', 229, NULL),
	(31, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NJ', 'New Jersey', 229, NULL),
	(32, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NM', 'New Mexico', 229, NULL),
	(33, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NY', 'New York', 229, NULL),
	(34, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NC', 'North Carolina', 229, NULL),
	(35, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'ND', 'North Dakota', 229, NULL),
	(36, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'OH', 'Ohio', 229, NULL),
	(37, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'OK', 'Oklahoma', 229, NULL),
	(38, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'OR', 'Oregon', 229, NULL),
	(39, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'PA', 'Pennsylvania', 229, NULL),
	(40, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'RI', 'Rhode Island', 229, NULL),
	(41, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'SC', 'South Carolina', 229, NULL),
	(42, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'SD', 'South Dakota', 229, NULL),
	(43, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'TN', 'Tennessee', 229, NULL),
	(44, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'TX', 'Texas', 229, NULL),
	(45, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'UT', 'Utah', 229, NULL),
	(46, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'VT', 'Vermont', 229, NULL),
	(47, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'VA', 'Virginia', 229, NULL),
	(48, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'WA', 'Washington', 229, NULL),
	(49, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'WV', 'West Virginia', 229, NULL),
	(50, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'WI', 'Wisconsin', 229, NULL),
	(51, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'WY', 'Wyoming', 229, NULL),
	(52, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AA', 'Armed Forces-Americas', 229, NULL),
	(53, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AE', 'Armed Forces-Europe', 229, NULL),
	(54, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AP', 'Armed Forces-Pacific', 229, NULL),
	(55, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'AB', 'Alberta', 37, NULL),
	(56, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'BC', 'British Columbia', 37, NULL),
	(57, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'MB', 'Manitoba', 37, NULL),
	(58, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'NL', 'Newfoundland and Labrador', 37, NULL),
	(59, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'NB', 'New Brunswick', 37, NULL),
	(60, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'NT', 'Northwest Territories', 37, NULL),
	(61, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'NS', 'Nova Scotia', 37, NULL),
	(62, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'NU', 'Nunavut', 37, NULL),
	(63, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'ON', 'Ontario', 37, NULL),
	(64, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'PE', 'Prince Edward Island', 37, NULL),
	(65, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'QC', 'Quebec', 37, NULL),
	(66, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'SK', 'Saskatchewan', 37, NULL),
	(67, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'YT', 'Yukon', 37, NULL),
	(68, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AL', 'Alabama', 474, NULL),
	(69, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AK', 'Alaska', 474, NULL),
	(70, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AZ', 'Arizona', 474, NULL),
	(71, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AR', 'Arkansas', 474, NULL),
	(72, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'CA', 'California', 474, NULL),
	(73, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'CO', 'Colorado', 474, NULL),
	(74, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'CT', 'Connecticut', 474, NULL),
	(75, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'DE', 'Delaware', 474, NULL),
	(76, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'DC', 'District of Columbia', 474, NULL),
	(77, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'FL', 'Florida', 474, NULL),
	(78, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'GA', 'Georgia', 474, NULL),
	(79, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'HI', 'Hawaii', 474, NULL),
	(80, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'ID', 'Idaho', 474, NULL),
	(81, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'IL', 'Illinois', 474, NULL),
	(82, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'IN', 'Indiana', 474, NULL),
	(83, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'IA', 'Iowa', 474, NULL),
	(84, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'KS', 'Kansas', 474, NULL),
	(85, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'KY', 'Kentucky', 474, NULL),
	(86, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'LA', 'Louisiana', 474, NULL),
	(87, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'ME', 'Maine', 474, NULL),
	(88, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MD', 'Maryland', 474, NULL),
	(89, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MA', 'Massachusetts', 474, NULL),
	(90, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MI', 'Michigan', 474, NULL),
	(91, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MN', 'Minnesota', 474, NULL),
	(92, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MS', 'Mississippi', 474, NULL),
	(93, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MO', 'Missouri', 474, NULL),
	(94, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'MT', 'Montana', 474, NULL),
	(95, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NE', 'Nebraska', 474, NULL),
	(96, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NV', 'Nevada', 474, NULL),
	(97, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NH', 'New Hampshire', 474, NULL),
	(98, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NJ', 'New Jersey', 474, NULL),
	(99, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NM', 'New Mexico', 474, NULL),
	(100, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NY', 'New York', 474, NULL),
	(101, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'NC', 'North Carolina', 474, NULL),
	(102, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'ND', 'North Dakota', 474, NULL),
	(103, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'OH', 'Ohio', 474, NULL),
	(104, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'OK', 'Oklahoma', 474, NULL),
	(105, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'OR', 'Oregon', 474, NULL),
	(106, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'PA', 'Pennsylvania', 474, NULL),
	(107, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'RI', 'Rhode Island', 474, NULL),
	(108, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'SC', 'South Carolina', 474, NULL),
	(109, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'SD', 'South Dakota', 474, NULL),
	(110, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'TN', 'Tennessee', 474, NULL),
	(111, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'TX', 'Texas', 474, NULL),
	(112, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'UT', 'Utah', 474, NULL),
	(113, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'VT', 'Vermont', 474, NULL),
	(114, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'VA', 'Virginia', 474, NULL),
	(115, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'WA', 'Washington', 474, NULL),
	(116, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'WV', 'West Virginia', 474, NULL),
	(117, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'WI', 'Wisconsin', 474, NULL),
	(118, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'WY', 'Wyoming', 474, NULL),
	(119, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AA', 'Armed Forces-Americas', 474, NULL),
	(120, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AE', 'Armed Forces-Europe', 474, NULL),
	(121, 'system', '2009-06-10 21:22:06', 'system', '2009-06-10 21:22:06', 'AP', 'Armed Forces-Pacific', 474, NULL),
	(122, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'AB', 'Alberta', 282, NULL),
	(123, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'BC', 'British Columbia', 282, NULL),
	(124, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'MB', 'Manitoba', 282, NULL),
	(125, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'NL', 'Newfoundland and Labrador', 282, NULL),
	(126, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'NB', 'New Brunswick', 282, NULL),
	(127, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'NT', 'Northwest Territories', 282, NULL),
	(128, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'NS', 'Nova Scotia', 282, NULL),
	(129, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'NU', 'Nunavut', 282, NULL),
	(130, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'ON', 'Ontario', 282, NULL),
	(131, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'PE', 'Prince Edward Island', 282, NULL),
	(132, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'QC', 'Quebec', 282, NULL),
	(133, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'SK', 'Saskatchewan', 282, NULL),
	(134, 'system', '2009-06-10 21:23:52', 'system', '2009-06-10 21:23:52', 'YT', 'Yukon', 282, NULL);
/*!40000 ALTER TABLE `state` ENABLE KEYS */;


# Dumping structure for table jada.syndication
CREATE TABLE IF NOT EXISTS `syndication` (
  `syn_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `site_url` varchar(255) NOT NULL,
  PRIMARY KEY (`syn_id`),
  KEY `FK3BB4BFF70EBDE65` (`site_id`),
  CONSTRAINT `FK3BB4BFF70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.syndication: ~0 rows (approximately)
/*!40000 ALTER TABLE `syndication` DISABLE KEYS */;
/*!40000 ALTER TABLE `syndication` ENABLE KEYS */;


# Dumping structure for table jada.tax
CREATE TABLE IF NOT EXISTS `tax` (
  `tax_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `tax_rate` float NOT NULL,
  `tax_lang_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tax_id`),
  KEY `FK1BFAB6D938030` (`tax_lang_id`),
  KEY `FK1BFAB70EBDE65` (`site_id`),
  CONSTRAINT `FK1BFAB70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK1BFAB6D938030` FOREIGN KEY (`tax_lang_id`) REFERENCES `tax_language` (`tax_lang_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.tax: ~0 rows (approximately)
/*!40000 ALTER TABLE `tax` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax` ENABLE KEYS */;


# Dumping structure for table jada.tax_country
CREATE TABLE IF NOT EXISTS `tax_country` (
  `tax_id` bigint(20) NOT NULL,
  `country_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tax_id`,`country_id`),
  KEY `FK4D2FE3022196FC0F` (`country_id`),
  KEY `FK4D2FE302C2D9DA6F` (`tax_id`),
  CONSTRAINT `FK4D2FE302C2D9DA6F` FOREIGN KEY (`tax_id`) REFERENCES `tax` (`tax_id`),
  CONSTRAINT `FK4D2FE3022196FC0F` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.tax_country: ~0 rows (approximately)
/*!40000 ALTER TABLE `tax_country` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_country` ENABLE KEYS */;


# Dumping structure for table jada.tax_language
CREATE TABLE IF NOT EXISTS `tax_language` (
  `tax_lang_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `tax_code` varchar(10) DEFAULT NULL,
  `tax_name` varchar(40) DEFAULT NULL,
  `site_profile_class_id` bigint(20) DEFAULT NULL,
  `tax_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tax_lang_id`),
  KEY `FKECB756C473E64D1` (`site_profile_class_id`),
  KEY `FKECB756CC2D9DA6F` (`tax_id`),
  CONSTRAINT `FKECB756CC2D9DA6F` FOREIGN KEY (`tax_id`) REFERENCES `tax` (`tax_id`),
  CONSTRAINT `FKECB756C473E64D1` FOREIGN KEY (`site_profile_class_id`) REFERENCES `site_profile_class` (`site_profile_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.tax_language: ~0 rows (approximately)
/*!40000 ALTER TABLE `tax_language` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_language` ENABLE KEYS */;


# Dumping structure for table jada.tax_region
CREATE TABLE IF NOT EXISTS `tax_region` (
  `taxRegionId` bigint(20) NOT NULL AUTO_INCREMENT,
  `published` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `taxRegionDesc` varchar(40) NOT NULL,
  `product_class_id` bigint(20) DEFAULT NULL,
  `site_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`taxRegionId`),
  KEY `FKFA7A7E08FBF32B66` (`product_class_id`),
  KEY `FKFA7A7E0870EBDE65` (`site_id`),
  CONSTRAINT `FKFA7A7E0870EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FKFA7A7E08FBF32B66` FOREIGN KEY (`product_class_id`) REFERENCES `product_class` (`product_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.tax_region: ~0 rows (approximately)
/*!40000 ALTER TABLE `tax_region` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_region` ENABLE KEYS */;


# Dumping structure for table jada.tax_region_country
CREATE TABLE IF NOT EXISTS `tax_region_country` (
  `tax_region_id` bigint(20) NOT NULL,
  `country_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tax_region_id`,`country_id`),
  KEY `FKF6E6F45F2196FC0F` (`country_id`),
  KEY `FKF6E6F45FCC5D8606` (`tax_region_id`),
  CONSTRAINT `FKF6E6F45FCC5D8606` FOREIGN KEY (`tax_region_id`) REFERENCES `tax_region` (`taxRegionId`),
  CONSTRAINT `FKF6E6F45F2196FC0F` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.tax_region_country: ~0 rows (approximately)
/*!40000 ALTER TABLE `tax_region_country` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_region_country` ENABLE KEYS */;


# Dumping structure for table jada.tax_region_product
CREATE TABLE IF NOT EXISTS `tax_region_product` (
  `tax_region_product_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `product_class_id` bigint(20) DEFAULT NULL,
  `tax_region_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tax_region_product_id`),
  KEY `FKAB5D69B8FBF32B66` (`product_class_id`),
  KEY `FKAB5D69B8CC5D8606` (`tax_region_id`),
  CONSTRAINT `FKAB5D69B8CC5D8606` FOREIGN KEY (`tax_region_id`) REFERENCES `tax_region` (`taxRegionId`),
  CONSTRAINT `FKAB5D69B8FBF32B66` FOREIGN KEY (`product_class_id`) REFERENCES `product_class` (`product_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.tax_region_product: ~0 rows (approximately)
/*!40000 ALTER TABLE `tax_region_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_region_product` ENABLE KEYS */;


# Dumping structure for table jada.tax_region_product_cust
CREATE TABLE IF NOT EXISTS `tax_region_product_cust` (
  `tax_region_product_cust_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `customer_class_id` bigint(20) DEFAULT NULL,
  `tax_region_product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tax_region_product_cust_id`),
  KEY `FK5301687A9DBD9BFD` (`tax_region_product_id`),
  KEY `FK5301687A3D38EB12` (`customer_class_id`),
  CONSTRAINT `FK5301687A3D38EB12` FOREIGN KEY (`customer_class_id`) REFERENCES `customer_class` (`cust_class_id`),
  CONSTRAINT `FK5301687A9DBD9BFD` FOREIGN KEY (`tax_region_product_id`) REFERENCES `tax_region_product` (`tax_region_product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.tax_region_product_cust: ~0 rows (approximately)
/*!40000 ALTER TABLE `tax_region_product_cust` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_region_product_cust` ENABLE KEYS */;


# Dumping structure for table jada.tax_region_product_cust_tax
CREATE TABLE IF NOT EXISTS `tax_region_product_cust_tax` (
  `tax_region_product_cust_tax_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `seq_num` int(11) NOT NULL,
  `tax_id` bigint(20) DEFAULT NULL,
  `tax_region_product_cust_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tax_region_product_cust_tax_id`),
  KEY `FKAAF18A66C2D9DA6F` (`tax_id`),
  KEY `FKAAF18A66DD0352EE` (`tax_region_product_cust_id`),
  CONSTRAINT `FKAAF18A66DD0352EE` FOREIGN KEY (`tax_region_product_cust_id`) REFERENCES `tax_region_product_cust` (`tax_region_product_cust_id`),
  CONSTRAINT `FKAAF18A66C2D9DA6F` FOREIGN KEY (`tax_id`) REFERENCES `tax` (`tax_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.tax_region_product_cust_tax: ~0 rows (approximately)
/*!40000 ALTER TABLE `tax_region_product_cust_tax` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_region_product_cust_tax` ENABLE KEYS */;


# Dumping structure for table jada.tax_region_state
CREATE TABLE IF NOT EXISTS `tax_region_state` (
  `tax_region_id` bigint(20) NOT NULL,
  `state_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tax_region_id`,`state_id`),
  KEY `FK57FCFF9AD2E3176F` (`state_id`),
  KEY `FK57FCFF9ACC5D8606` (`tax_region_id`),
  CONSTRAINT `FK57FCFF9ACC5D8606` FOREIGN KEY (`tax_region_id`) REFERENCES `tax_region` (`taxRegionId`),
  CONSTRAINT `FK57FCFF9AD2E3176F` FOREIGN KEY (`state_id`) REFERENCES `state` (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.tax_region_state: ~0 rows (approximately)
/*!40000 ALTER TABLE `tax_region_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_region_state` ENABLE KEYS */;


# Dumping structure for table jada.tax_region_zip
CREATE TABLE IF NOT EXISTS `tax_region_zip` (
  `tax_region_zip_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `zip_code_end` varchar(10) NOT NULL,
  `zip_code_start` varchar(10) NOT NULL,
  `tax_region_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tax_region_zip_id`),
  KEY `FK1DA9406ACC5D8606` (`tax_region_id`),
  CONSTRAINT `FK1DA9406ACC5D8606` FOREIGN KEY (`tax_region_id`) REFERENCES `tax_region` (`taxRegionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.tax_region_zip: ~0 rows (approximately)
/*!40000 ALTER TABLE `tax_region_zip` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_region_zip` ENABLE KEYS */;


# Dumping structure for table jada.tax_state
CREATE TABLE IF NOT EXISTS `tax_state` (
  `tax_id` bigint(20) NOT NULL,
  `state_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tax_id`,`state_id`),
  KEY `FKFFE73AFDD2E3176F` (`state_id`),
  KEY `FKFFE73AFDC2D9DA6F` (`tax_id`),
  CONSTRAINT `FKFFE73AFDC2D9DA6F` FOREIGN KEY (`tax_id`) REFERENCES `tax` (`tax_id`),
  CONSTRAINT `FKFFE73AFDD2E3176F` FOREIGN KEY (`state_id`) REFERENCES `state` (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.tax_state: ~0 rows (approximately)
/*!40000 ALTER TABLE `tax_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `tax_state` ENABLE KEYS */;


# Dumping structure for table jada.template
CREATE TABLE IF NOT EXISTS `template` (
  `template_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `template_desc` varchar(40) NOT NULL,
  `template_name` varchar(20) NOT NULL,
  PRIMARY KEY (`template_id`),
  UNIQUE KEY `template_name` (`template_name`,`site_id`),
  KEY `FKB13ACC7A70EBDE65` (`site_id`),
  CONSTRAINT `FKB13ACC7A70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

# Dumping data for table jada.template: ~2 rows (approximately)
/*!40000 ALTER TABLE `template` DISABLE KEYS */;
INSERT INTO `template` (`template_id`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `site_id`, `template_desc`, `template_name`) VALUES
	(1, 'root', '2009-06-11 08:45:55', 'root', '2009-06-11 08:45:52', 'default', 'Basic template', 'basic'),
	(3, 'system', '2009-09-30 17:12:06', 'system', '2009-09-30 17:12:00', '_system', 'Basic template', 'basic');
/*!40000 ALTER TABLE `template` ENABLE KEYS */;


# Dumping structure for table jada.transaction
CREATE TABLE IF NOT EXISTS `transaction` (
  `tran_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `site_id` varchar(20) NOT NULL,
  `tran_datetime` datetime NOT NULL,
  `tran_qty` int(11) NOT NULL,
  `tran_reference` varchar(20) NOT NULL,
  `tran_remarks` varchar(255) NOT NULL,
  `tran_type` varchar(1) NOT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tran_id`),
  KEY `FK7FA0D2DE71DEBAE5` (`item_id`),
  KEY `FK7FA0D2DE70EBDE65` (`site_id`),
  CONSTRAINT `FK7FA0D2DE70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK7FA0D2DE71DEBAE5` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.transaction: ~0 rows (approximately)
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;


# Dumping structure for table jada.user
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` varchar(20) NOT NULL,
  `active` char(1) NOT NULL,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `user_address_line1` varchar(30) NOT NULL,
  `user_address_line2` varchar(30) NOT NULL,
  `user_city_name` varchar(25) NOT NULL,
  `user_country_code` varchar(2) NOT NULL,
  `user_country_name` varchar(40) NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_last_login_datetime` datetime DEFAULT NULL,
  `user_last_visit_site_id` varchar(20) DEFAULT NULL,
  `user_name` varchar(50) NOT NULL,
  `user_password` varchar(128) NOT NULL,
  `user_phone` varchar(20) DEFAULT NULL,
  `user_state_code` varchar(2) NOT NULL,
  `user_state_name` varchar(40) NOT NULL,
  `user_type` varchar(1) NOT NULL,
  `user_zip_code` varchar(10) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.user: ~1 rows (approximately)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `active`, `rec_create_by`, `rec_create_datetime`, `rec_update_by`, `rec_update_datetime`, `user_address_line1`, `user_address_line2`, `user_city_name`, `user_country_code`, `user_country_name`, `user_email`, `user_last_login_datetime`, `user_last_visit_site_id`, `user_name`, `user_password`, `user_phone`, `user_state_code`, `user_state_name`, `user_type`, `user_zip_code`) VALUES
	('admin', 'Y', 'root', '2008-08-09 19:54:41', 'admin', '2009-08-13 15:07:03', '', '', 'Toronto', 'CA', 'Canada', 'philip_wong_101@hotmail.com', '2009-12-29 16:09:11', 'default', 'Administrator', '/OF11541pyEUQtbLqNrYIA==', '', 'ON', 'Ontario', 'S', '');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;


# Dumping structure for table jada.user_site
CREATE TABLE IF NOT EXISTS `user_site` (
  `user_id` varchar(20) NOT NULL,
  `site_id` varchar(20) NOT NULL,
  PRIMARY KEY (`user_id`,`site_id`),
  KEY `FK143C533BEAFC5FE5` (`user_id`),
  KEY `FK143C533B70EBDE65` (`site_id`),
  CONSTRAINT `FK143C533B70EBDE65` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`),
  CONSTRAINT `FK143C533BEAFC5FE5` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.user_site: ~0 rows (approximately)
/*!40000 ALTER TABLE `user_site` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_site` ENABLE KEYS */;


# Dumping structure for table jada.void_order
CREATE TABLE IF NOT EXISTS `void_order` (
  `void_order_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_create_by` varchar(20) NOT NULL,
  `rec_create_datetime` datetime NOT NULL,
  `rec_update_by` varchar(20) NOT NULL,
  `rec_update_datetime` datetime NOT NULL,
  `void_order_datetime` datetime NOT NULL,
  `void_order_num` varchar(20) NOT NULL,
  `payment_tran_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`void_order_id`),
  KEY `FKD13EBCE3EEE96B5C` (`payment_tran_id`),
  CONSTRAINT `FKD13EBCE3EEE96B5C` FOREIGN KEY (`payment_tran_id`) REFERENCES `payment_tran` (`payment_tran_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table jada.void_order: ~0 rows (approximately)
/*!40000 ALTER TABLE `void_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `void_order` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
