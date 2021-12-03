-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: testmall
-- ------------------------------------------------------
-- Server version	8.0.23

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
-- Table structure for table `t_admin_info`
--

DROP TABLE IF EXISTS `t_admin_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_admin_info` (
  `ai_idx` int NOT NULL AUTO_INCREMENT,
  `ai_id` varchar(20) COLLATE utf8_bin NOT NULL,
  `ai_pwd` varchar(20) COLLATE utf8_bin NOT NULL,
  `ai_name` varchar(20) COLLATE utf8_bin NOT NULL,
  `ai_isrun` char(1) COLLATE utf8_bin DEFAULT 'y',
  `ai_regdate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ai_id`),
  UNIQUE KEY `ai_idx` (`ai_idx`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_admin_info`
--

LOCK TABLES `t_admin_info` WRITE;
/*!40000 ALTER TABLE `t_admin_info` DISABLE KEYS */;
INSERT INTO `t_admin_info` VALUES (2,'admin1','1111','전우치','y','2021-04-19 16:44:39'),(3,'admin2','1111','임꺽정','y','2021-04-19 16:50:22'),(1,'sa','1111','관리자','y','2021-04-16 09:01:19');
/*!40000 ALTER TABLE `t_admin_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_admin_login_log`
--

DROP TABLE IF EXISTS `t_admin_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_admin_login_log` (
  `al_idx` int NOT NULL AUTO_INCREMENT,
  `ai_idx` int DEFAULT NULL,
  `al_ip` varchar(15) COLLATE utf8_bin NOT NULL,
  `al_date` datetime DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `al_idx` (`al_idx`),
  KEY `fk_admin_login_log_ai_idx` (`ai_idx`),
  CONSTRAINT `fk_admin_login_log_ai_idx` FOREIGN KEY (`ai_idx`) REFERENCES `t_admin_info` (`ai_idx`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_admin_login_log`
--

LOCK TABLES `t_admin_login_log` WRITE;
/*!40000 ALTER TABLE `t_admin_login_log` DISABLE KEYS */;
INSERT INTO `t_admin_login_log` VALUES (1,3,'127.0.0.1','2021-04-19 16:55:45'),(2,3,'127.0.0.1','2021-04-19 16:55:48'),(3,3,'127.0.0.1','2021-04-19 16:55:49'),(4,2,'127.0.0.1','2021-04-19 16:55:52'),(5,2,'127.0.0.1','2021-04-19 17:53:45');
/*!40000 ALTER TABLE `t_admin_login_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_admin_menu`
--

DROP TABLE IF EXISTS `t_admin_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_admin_menu` (
  `am_idx` int NOT NULL,
  `am_name` varchar(20) COLLATE utf8_bin NOT NULL,
  `am_link` varchar(100) COLLATE utf8_bin DEFAULT 'unlink',
  `am_parent` int DEFAULT '0',
  `am_seq` int DEFAULT '0',
  `am_regdate` datetime DEFAULT CURRENT_TIMESTAMP,
  `ai_idx` int DEFAULT NULL,
  PRIMARY KEY (`am_idx`),
  KEY `fk_admin_menu_ai_idx` (`ai_idx`),
  CONSTRAINT `fk_admin_menu_ai_idx` FOREIGN KEY (`ai_idx`) REFERENCES `t_admin_info` (`ai_idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_admin_menu`
--

LOCK TABLES `t_admin_menu` WRITE;
/*!40000 ALTER TABLE `t_admin_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_admin_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_admin_permission`
--

DROP TABLE IF EXISTS `t_admin_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_admin_permission` (
  `ap_idx` int NOT NULL AUTO_INCREMENT,
  `ap_ai_idx` int NOT NULL,
  `am_idx` int NOT NULL,
  `ap_pms` int DEFAULT '0',
  `ap_regdate` datetime DEFAULT CURRENT_TIMESTAMP,
  `ai_idx` int DEFAULT NULL,
  PRIMARY KEY (`ap_ai_idx`,`am_idx`),
  UNIQUE KEY `ap_idx` (`ap_idx`),
  KEY `fk_admin_permission_am_idx` (`am_idx`),
  KEY `fk_admin_permission_ai_idx` (`ai_idx`),
  CONSTRAINT `fk_admin_permission_ai_idx` FOREIGN KEY (`ai_idx`) REFERENCES `t_admin_info` (`ai_idx`),
  CONSTRAINT `fk_admin_permission_am_idx` FOREIGN KEY (`am_idx`) REFERENCES `t_admin_menu` (`am_idx`),
  CONSTRAINT `fk_admin_permission_ap_ai_idx` FOREIGN KEY (`ap_ai_idx`) REFERENCES `t_admin_info` (`ai_idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_admin_permission`
--

LOCK TABLES `t_admin_permission` WRITE;
/*!40000 ALTER TABLE `t_admin_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_admin_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_brand`
--

DROP TABLE IF EXISTS `t_brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_brand` (
  `b_id` char(4) COLLATE utf8_bin NOT NULL COMMENT '브랜드ID',
  `b_name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '브랜드명',
  `b_company` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '회사명',
  PRIMARY KEY (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='브랜드 테이블 : 진로, 장수, 롯데, 한라산, 지평, OB';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_brand`
--

LOCK TABLES `t_brand` WRITE;
/*!40000 ALTER TABLE `t_brand` DISABLE KEYS */;
INSERT INTO `t_brand` VALUES ('BR01','하이트진로','진로'),('BR02','장수','서울탁주'),('BR03','처음처럼','롯데'),('BR04','한라산','한라산'),('BR05','지평','지평주조'),('BR06','OB','두산');
/*!40000 ALTER TABLE `t_brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_brd_notice`
--

DROP TABLE IF EXISTS `t_brd_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_brd_notice` (
  `bf_idx` int NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `bf_cata` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '분류(회원 관련, 상품 관련, 주문/배송 관련, 결제 관련, 후기 관련, 기타)',
  `bf_title` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '질문',
  `bf_content` text COLLATE utf8_bin NOT NULL COMMENT '답변',
  `bf_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
  `ai_idx` int DEFAULT NULL COMMENT '관리자 번호',
  PRIMARY KEY (`bf_idx`),
  KEY `fk_brd_notice_ai_idx` (`ai_idx`),
  CONSTRAINT `fk_brd_notice_ai_idx` FOREIGN KEY (`ai_idx`) REFERENCES `t_admin_info` (`ai_idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='공지사항 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_brd_notice`
--

LOCK TABLES `t_brd_notice` WRITE;
/*!40000 ALTER TABLE `t_brd_notice` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_brd_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_brd_qna`
--

DROP TABLE IF EXISTS `t_brd_qna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_brd_qna` (
  `bq_idx` int NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `mi_id` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '회원ID',
  `bq_cata` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '질문분류(회원 관련, 상품 관련, 주문/배송 관련, 결제 관련, 후기 관련, 기타)',
  `bq_title` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '질문제목',
  `bq_content` text COLLATE utf8_bin NOT NULL COMMENT '질문내용',
  `bq_img` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '이미지',
  `bq_qdate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '질문등록일',
  `bq_answer` text COLLATE utf8_bin COMMENT '답변내용',
  `ai_idx` int DEFAULT NULL COMMENT '답변관리자',
  `bq_adate` datetime DEFAULT NULL COMMENT '답변등록일',
  `bq_status` char(1) COLLATE utf8_bin DEFAULT 'a' COMMENT '상태(a: 답변전, b: 답변완료)',
  PRIMARY KEY (`bq_idx`),
  KEY `fk_brd_qna_mi_id` (`mi_id`),
  KEY `fk_brd_qna_ai_id` (`ai_idx`),
  CONSTRAINT `fk_brd_qna_ai_id` FOREIGN KEY (`ai_idx`) REFERENCES `t_admin_info` (`ai_idx`),
  CONSTRAINT `fk_brd_qna_mi_id` FOREIGN KEY (`mi_id`) REFERENCES `t_member_addr` (`mi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='QnA 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_brd_qna`
--

LOCK TABLES `t_brd_qna` WRITE;
/*!40000 ALTER TABLE `t_brd_qna` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_brd_qna` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_brd_review`
--

DROP TABLE IF EXISTS `t_brd_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_brd_review` (
  `br_idx` int NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `mi_id` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '회원ID',
  `oi_id` char(10) COLLATE utf8_bin NOT NULL COMMENT '주문번호',
  `pi_id` char(6) COLLATE utf8_bin NOT NULL COMMENT '상품ID',
  `br_opt` varchar(10) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '선택한 옵션',
  `br_title` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '제목',
  `br_content` text COLLATE utf8_bin NOT NULL COMMENT '내용',
  `br_cnt` int DEFAULT '0' COMMENT '댓글수',
  `br_img1` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '이미지1',
  `br_img2` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '이미지2',
  `br_star` float DEFAULT '0' COMMENT '상품별점',
  `br_score` float DEFAULT '0' COMMENT '상품후기점수',
  `br_isview` char(1) COLLATE utf8_bin DEFAULT 'y' COMMENT '게시여부',
  `br_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
  PRIMARY KEY (`mi_id`,`oi_id`,`pi_id`,`br_opt`),
  UNIQUE KEY `br_idx` (`br_idx`),
  KEY `fk_brd_review_oi_id` (`oi_id`),
  KEY `fk_brd_review_pi_id` (`pi_id`),
  CONSTRAINT `fk_brd_review_mi_id` FOREIGN KEY (`mi_id`) REFERENCES `t_member_info` (`mi_id`),
  CONSTRAINT `fk_brd_review_oi_id` FOREIGN KEY (`oi_id`) REFERENCES `t_order_info` (`oi_id`),
  CONSTRAINT `fk_brd_review_pi_id` FOREIGN KEY (`pi_id`) REFERENCES `t_product_info` (`pi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='상품후기 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_brd_review`
--

LOCK TABLES `t_brd_review` WRITE;
/*!40000 ALTER TABLE `t_brd_review` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_brd_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_brd_review_911`
--

DROP TABLE IF EXISTS `t_brd_review_911`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_brd_review_911` (
  `br9_idx` int NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `br_idx` int DEFAULT NULL COMMENT '후기 글 번호',
  `mi_id` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '회원ID',
  `br9_content` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '신고 내용(광고성 후기,욕설 및 비속어 포함,선정적 후기,관련없는 후기,기타 - 직접 입력받음)',
  `br9_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '신고일',
  `br9_status` char(1) COLLATE utf8_bin DEFAULT 'a' COMMENT '신고상태(a: 접수중,b: 접수 및 확인작업,c: 처리완료)',
  `br9_opdate` datetime DEFAULT NULL COMMENT '처리일',
  `ai_idx` int DEFAULT NULL COMMENT '관리자 번호',
  PRIMARY KEY (`br9_idx`),
  KEY `fk_brd_review_911_br_idx` (`br_idx`),
  KEY `fk_brd_review_911_mi_id` (`mi_id`),
  KEY `fk_brd_review_911_ai_idx` (`ai_idx`),
  CONSTRAINT `fk_brd_review_911_ai_idx` FOREIGN KEY (`ai_idx`) REFERENCES `t_admin_info` (`ai_idx`),
  CONSTRAINT `fk_brd_review_911_br_idx` FOREIGN KEY (`br_idx`) REFERENCES `t_brd_review` (`br_idx`),
  CONSTRAINT `fk_brd_review_911_mi_id` FOREIGN KEY (`mi_id`) REFERENCES `t_member_info` (`mi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='상품후기 신고 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_brd_review_911`
--

LOCK TABLES `t_brd_review_911` WRITE;
/*!40000 ALTER TABLE `t_brd_review_911` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_brd_review_911` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_brd_review_replay`
--

DROP TABLE IF EXISTS `t_brd_review_replay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_brd_review_replay` (
  `brr_idx` int NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `br_idx` int DEFAULT NULL COMMENT '후기 글번호',
  `mi_id` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '회원ID',
  `brr_content` text COLLATE utf8_bin NOT NULL COMMENT '내용',
  `brr_recc` float DEFAULT '0' COMMENT '추천수 평균',
  `brr_isview` char(1) COLLATE utf8_bin DEFAULT 'y' COMMENT '게시여부',
  `brr_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
  PRIMARY KEY (`brr_idx`),
  KEY `fk_brd_review_replay_br_idx` (`br_idx`),
  KEY `fk_brd_review_replay_mi_id` (`mi_id`),
  CONSTRAINT `fk_brd_review_replay_br_idx` FOREIGN KEY (`br_idx`) REFERENCES `t_brd_review` (`br_idx`),
  CONSTRAINT `fk_brd_review_replay_mi_id` FOREIGN KEY (`mi_id`) REFERENCES `t_member_info` (`mi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='상품후기 댓글 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_brd_review_replay`
--

LOCK TABLES `t_brd_review_replay` WRITE;
/*!40000 ALTER TABLE `t_brd_review_replay` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_brd_review_replay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_brd_review_replay_re`
--

DROP TABLE IF EXISTS `t_brd_review_replay_re`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_brd_review_replay_re` (
  `brrr_idx` int NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `brr_idx` int NOT NULL COMMENT '댓글번호',
  `mi_id` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '회원ID',
  `brs_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
  PRIMARY KEY (`brr_idx`,`mi_id`),
  UNIQUE KEY `brrr_idx` (`brrr_idx`),
  KEY `fk_brd_review_replay_re_mi_id` (`mi_id`),
  CONSTRAINT `fk_brd_review_replay_re_brr_idx` FOREIGN KEY (`brr_idx`) REFERENCES `t_brd_review_replay` (`brr_idx`),
  CONSTRAINT `fk_brd_review_replay_re_mi_id` FOREIGN KEY (`mi_id`) REFERENCES `t_member_info` (`mi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='상품후기 댓글 추천 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_brd_review_replay_re`
--

LOCK TABLES `t_brd_review_replay_re` WRITE;
/*!40000 ALTER TABLE `t_brd_review_replay_re` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_brd_review_replay_re` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_brd_review_star`
--

DROP TABLE IF EXISTS `t_brd_review_star`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_brd_review_star` (
  `brs_idx` int NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `mi_id` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '회원ID',
  `br_idx` int NOT NULL COMMENT '후기 글번호',
  `brs_score` float DEFAULT '0' COMMENT '별점',
  `brs_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
  PRIMARY KEY (`mi_id`,`br_idx`),
  UNIQUE KEY `brs_idx` (`brs_idx`),
  KEY `fk_brd_review_star_br_idx` (`br_idx`),
  CONSTRAINT `fk_brd_review_star_br_idx` FOREIGN KEY (`br_idx`) REFERENCES `t_brd_review` (`br_idx`),
  CONSTRAINT `fk_brd_review_star_mi_id` FOREIGN KEY (`mi_id`) REFERENCES `t_member_info` (`mi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='상품후기 별점 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_brd_review_star`
--

LOCK TABLES `t_brd_review_star` WRITE;
/*!40000 ALTER TABLE `t_brd_review_star` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_brd_review_star` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_cata_big`
--

DROP TABLE IF EXISTS `t_cata_big`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_cata_big` (
  `cb_id` char(2) COLLATE utf8_bin NOT NULL COMMENT '대분류ID',
  `cb_name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '대분류명',
  PRIMARY KEY (`cb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='대분류 테이블 : 발효주, 증류주';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_cata_big`
--

LOCK TABLES `t_cata_big` WRITE;
/*!40000 ALTER TABLE `t_cata_big` DISABLE KEYS */;
INSERT INTO `t_cata_big` VALUES ('B1','발효주'),('B2','발효주');
/*!40000 ALTER TABLE `t_cata_big` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_cata_small`
--

DROP TABLE IF EXISTS `t_cata_small`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_cata_small` (
  `cs_id` char(5) COLLATE utf8_bin NOT NULL COMMENT '소분류ID',
  `cb_id` char(2) COLLATE utf8_bin DEFAULT NULL COMMENT '대분류ID',
  `cs_name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '소분류명',
  PRIMARY KEY (`cs_id`),
  KEY `fk_cata_small_cs_id` (`cb_id`),
  CONSTRAINT `fk_cata_small_cs_id` FOREIGN KEY (`cb_id`) REFERENCES `t_cata_big` (`cb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='소분류 테이블 : 발효주 - 막걸리, 포도주, 맥주 / 증류주 - 소주, 고량주, 보드카';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_cata_small`
--

LOCK TABLES `t_cata_small` WRITE;
/*!40000 ALTER TABLE `t_cata_small` DISABLE KEYS */;
INSERT INTO `t_cata_small` VALUES ('B1S01','B1','막걸리'),('B1S02','B1','포도주'),('B1S03','B1','맥주'),('B2S01','B2','소주'),('B2S02','B2','고량주'),('B2S03','B2','소주');
/*!40000 ALTER TABLE `t_cata_small` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_main_banner`
--

DROP TABLE IF EXISTS `t_main_banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_main_banner` (
  `mb_idx` int NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `mb_img` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '이미지파일명',
  `mb_url` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '이동URL',
  `mb_seq` int DEFAULT '0' COMMENT '순서',
  `mb_isview` char(1) COLLATE utf8_bin DEFAULT 'n' COMMENT '게시여부',
  `mb_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `ai_idx` int DEFAULT NULL COMMENT '관리자 번호',
  PRIMARY KEY (`mb_idx`),
  KEY `fk_main_banner_ai_idx` (`ai_idx`),
  CONSTRAINT `fk_main_banner_ai_idx` FOREIGN KEY (`ai_idx`) REFERENCES `t_admin_info` (`ai_idx`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='메인화면 배너 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_main_banner`
--

LOCK TABLES `t_main_banner` WRITE;
/*!40000 ALTER TABLE `t_main_banner` DISABLE KEYS */;
INSERT INTO `t_main_banner` VALUES (1,'main01.jpg','/',1,'y','2021-04-26 18:25:20',1),(2,'main02.jpg','/',1,'n','2021-04-26 18:25:20',1),(3,'main03.jpg','/',2,'y','2021-04-26 18:25:20',1),(4,'main04.jpg','/',1,'y','2021-04-26 18:25:20',1),(5,'main05.jpg','/',5,'n','2021-04-26 18:25:20',1),(6,'main06.jpg','/',3,'y','2021-04-26 18:25:20',1);
/*!40000 ALTER TABLE `t_main_banner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_member_addr`
--

DROP TABLE IF EXISTS `t_member_addr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_member_addr` (
  `ma_idx` int NOT NULL AUTO_INCREMENT,
  `mi_id` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `ma_zip` char(5) COLLATE utf8_bin NOT NULL,
  `ma_addr1` varchar(50) COLLATE utf8_bin NOT NULL,
  `ma_addr2` varchar(50) COLLATE utf8_bin NOT NULL,
  `ma_basic` char(1) COLLATE utf8_bin DEFAULT 'y',
  `ma_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_idx`),
  KEY `fk_member_addr_mi_id` (`mi_id`),
  CONSTRAINT `fk_member_addr_mi_id` FOREIGN KEY (`mi_id`) REFERENCES `t_member_info` (`mi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_member_addr`
--

LOCK TABLES `t_member_addr` WRITE;
/*!40000 ALTER TABLE `t_member_addr` DISABLE KEYS */;
INSERT INTO `t_member_addr` VALUES (1,'test1','12345','서울','123-45','y','2021-04-16 09:01:20'),(2,'test2','22222','부산','222-45','y','2021-04-16 09:01:20'),(3,'test3','33333','대전','333-45','y','2021-04-16 09:01:20'),(4,'test4','11222','경상북도 문경시','456-456','y','2021-04-19 18:24:24');
/*!40000 ALTER TABLE `t_member_addr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_member_info`
--

DROP TABLE IF EXISTS `t_member_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_member_info` (
  `mi_id` varchar(20) COLLATE utf8_bin NOT NULL,
  `mi_pwd` varchar(20) COLLATE utf8_bin NOT NULL,
  `mi_name` varchar(20) COLLATE utf8_bin NOT NULL,
  `mi_birth` char(10) COLLATE utf8_bin NOT NULL,
  `mi_gender` char(1) COLLATE utf8_bin NOT NULL,
  `mi_phone` varchar(13) COLLATE utf8_bin NOT NULL,
  `mi_email` varchar(50) COLLATE utf8_bin NOT NULL,
  `mi_issns` char(1) COLLATE utf8_bin DEFAULT 'y',
  `mi_ismail` char(1) COLLATE utf8_bin DEFAULT 'y',
  `mi_point` int DEFAULT '0',
  `mi_rebank` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mi_account` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mi_recommend` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `mi_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `mi_isactive` char(1) COLLATE utf8_bin DEFAULT 'y',
  `mi_lastlogin` datetime DEFAULT NULL,
  PRIMARY KEY (`mi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_member_info`
--

LOCK TABLES `t_member_info` WRITE;
/*!40000 ALTER TABLE `t_member_info` DISABLE KEYS */;
INSERT INTO `t_member_info` VALUES ('test1','1111','홍길동','1988-05-15','m','010-1234-5678','hong@gmail.com','y','y',1000,NULL,NULL,NULL,'2021-04-16 09:01:20','y',NULL),('test2','1111','전우치','1991-08-05','m','010-2222-5678','woochi@nate.com','y','y',1000,NULL,NULL,NULL,'2021-04-16 09:01:20','y',NULL),('test3','1111','임꺽정','1998-11-23','m','010-1234-3333','lim@naver.com','y','y',1000,NULL,NULL,NULL,'2021-04-16 09:01:20','y',NULL),('test4','1111','둘리','1995-08-09','m','010-1234-4444','dooly@naver.com','y','n',1000,'국민은행','123-4567-890','test1','2021-04-19 18:24:24','y',NULL);
/*!40000 ALTER TABLE `t_member_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_member_point`
--

DROP TABLE IF EXISTS `t_member_point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_member_point` (
  `mp_idx` int NOT NULL AUTO_INCREMENT,
  `mi_id` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `mp_kind` char(1) COLLATE utf8_bin DEFAULT 's',
  `mp_point` int DEFAULT '0',
  `br_idx` int DEFAULT '0',
  `mp_content` varchar(50) COLLATE utf8_bin NOT NULL,
  `mp_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`mp_idx`),
  KEY `fk_member_point_mi_id` (`mi_id`),
  CONSTRAINT `fk_member_point_mi_id` FOREIGN KEY (`mi_id`) REFERENCES `t_member_info` (`mi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_member_point`
--

LOCK TABLES `t_member_point` WRITE;
/*!40000 ALTER TABLE `t_member_point` DISABLE KEYS */;
INSERT INTO `t_member_point` VALUES (1,'test1','s',1000,0,'가입축하금','2021-04-16 09:01:20'),(2,'test2','s',1000,0,'가입축하금','2021-04-16 09:01:20'),(3,'test3','s',1000,0,'가입축하금','2021-04-16 09:01:20'),(4,'test4','s',1000,0,'가입축하금','2021-04-19 18:24:24');
/*!40000 ALTER TABLE `t_member_point` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_order_cart`
--

DROP TABLE IF EXISTS `t_order_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_order_cart` (
  `oc_idx` int NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `mi_id` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '회원ID',
  `pi_id` char(6) COLLATE utf8_bin DEFAULT NULL COMMENT '상품ID',
  `oc_option` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '옵션',
  `oc_cnt` int DEFAULT '1' COMMENT '개수',
  `oc_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '일자',
  PRIMARY KEY (`oc_idx`),
  KEY `fk_order_cart_mi_id` (`mi_id`),
  KEY `fk_order_cart_pi_id` (`pi_id`),
  CONSTRAINT `fk_order_cart_mi_id` FOREIGN KEY (`mi_id`) REFERENCES `t_member_info` (`mi_id`),
  CONSTRAINT `fk_order_cart_pi_id` FOREIGN KEY (`pi_id`) REFERENCES `t_product_info` (`pi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='장바구니 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_order_cart`
--

LOCK TABLES `t_order_cart` WRITE;
/*!40000 ALTER TABLE `t_order_cart` DISABLE KEYS */;
INSERT INTO `t_order_cart` VALUES (4,'test1','pdt001','14도',2,'2021-04-27 15:44:26'),(5,'test1','pdt001','18도',1,'2021-04-27 15:44:26'),(6,'test1','pdt002','11도',3,'2021-04-27 15:44:26');
/*!40000 ALTER TABLE `t_order_cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_order_detail`
--

DROP TABLE IF EXISTS `t_order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_order_detail` (
  `od_idx` int NOT NULL COMMENT '일련번호',
  `oi_id` char(10) COLLATE utf8_bin DEFAULT NULL COMMENT '주문번호 : 년월일(yymmdd) + 일련번호(1000)',
  `pi_id` char(6) COLLATE utf8_bin DEFAULT NULL COMMENT '상품ID',
  `od_pdtname` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '상품명',
  `od_pdtimg` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '상품이미지',
  `od_pdtprice` int DEFAULT '0' COMMENT '단가',
  `od_cnt` int DEFAULT '1' COMMENT '개수',
  `od_option` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '옵션(선택한 옵션)',
  `od_saveptn` int DEFAULT NULL COMMENT '적립포인트 (후기를 입력한 경우 포인트가 적립됨)',
  PRIMARY KEY (`od_idx`),
  KEY `fk_order_detail_oi_id` (`oi_id`),
  KEY `fk_order_detail_pi_id` (`pi_id`),
  CONSTRAINT `fk_order_detail_oi_id` FOREIGN KEY (`oi_id`) REFERENCES `t_order_info` (`oi_id`),
  CONSTRAINT `fk_order_detail_pi_id` FOREIGN KEY (`pi_id`) REFERENCES `t_product_info` (`pi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='주문 상세 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_order_detail`
--

LOCK TABLES `t_order_detail` WRITE;
/*!40000 ALTER TABLE `t_order_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_order_info`
--

DROP TABLE IF EXISTS `t_order_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_order_info` (
  `oi_id` char(10) COLLATE utf8_bin NOT NULL COMMENT '주문번호(년월일(yymmdd) + 일련번호(1000))',
  `mi_id` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '회원ID',
  `oi_name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '수취인명',
  `oi_phone` varchar(13) COLLATE utf8_bin NOT NULL COMMENT '수취인 전화번호',
  `oi_zip` char(5) COLLATE utf8_bin NOT NULL COMMENT '배송지 우편번호',
  `oi_addr1` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '배송지 주소1',
  `oi_addr2` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '배송지 주소2',
  `oi_payment` char(1) COLLATE utf8_bin DEFAULT 'a' COMMENT '결제방법(a : 카드결제, b : 계좌이체, c : 무통장, d : 휴대폰결제)',
  `oi_pay` int DEFAULT '0' COMMENT '결제액',
  `oi_usepnt` int DEFAULT '0' COMMENT '사용포인트',
  `oi_delipay` int DEFAULT '0' COMMENT '배송비',
  `oi_status` char(1) COLLATE utf8_bin DEFAULT 'b' COMMENT '주문상태(a : 결제대기중, b : 배송대기중 , c : 배송중, d : 주문완료, e : 주문취소(환불요청-배송전), f : 주문취소(환불완료-배송전) ,g : 주문취소(반품요청-배송후), h : 주문취소(반품완료-)',
  `oi_cmt` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '요청사항',
  `oi_invoice` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '송장번호',
  `oi_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '주문일',
  `oi_modify` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '마지막 수정일자',
  `ai_admin` int DEFAULT '0' COMMENT '관리자번호\n',
  PRIMARY KEY (`oi_id`),
  KEY `fk_order_info_mi_id` (`mi_id`),
  CONSTRAINT `fk_order_info_mi_id` FOREIGN KEY (`mi_id`) REFERENCES `t_member_info` (`mi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='주문 정보 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_order_info`
--

LOCK TABLES `t_order_info` WRITE;
/*!40000 ALTER TABLE `t_order_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_order_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_poll_question`
--

DROP TABLE IF EXISTS `t_poll_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_poll_question` (
  `pq_idx` int NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `pq_question` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '설문내용',
  `pq_ex1` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '설문보기1',
  `pq_ex2` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '설문보기2',
  `pq_ex3` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '설문보기3',
  `pq_ex4` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '설문보기4',
  `pq_ex5` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '설문보기5',
  `pq_sdate` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '설문시작일(yyyy-mm-dd)',
  `pq_edate` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '설문종료일(yyyy-mm-dd)',
  `pq_endmsg` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '설문종료메시지',
  `pq_status` char(1) COLLATE utf8_bin DEFAULT 'a' COMMENT '설문상태(a : 대기중, b : 진행중, c : 종료)',
  `pd_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `ai_idx` int DEFAULT NULL COMMENT '관리자번호',
  PRIMARY KEY (`pq_idx`),
  KEY `fk_poll_question_ai_idx` (`ai_idx`),
  CONSTRAINT `fk_poll_question_ai_idx` FOREIGN KEY (`ai_idx`) REFERENCES `t_admin_info` (`ai_idx`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='설문조사 질문&보기 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_poll_question`
--

LOCK TABLES `t_poll_question` WRITE;
/*!40000 ALTER TABLE `t_poll_question` DISABLE KEYS */;
INSERT INTO `t_poll_question` VALUES (1,'주로 마시는 술은 어떤 술인가요?','소주','맥주','막걸리','와인','데킬라','2021-04-26','2021-06-27','이제 그만','b','2021-04-26 18:25:18',1);
/*!40000 ALTER TABLE `t_poll_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_poll_result`
--

DROP TABLE IF EXISTS `t_poll_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_poll_result` (
  `pr_idx` int NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `pq_idx` int NOT NULL COMMENT '설문조사번호',
  `mi_id` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '회원ID',
  `pr_ex` int DEFAULT NULL COMMENT '선택보기번호',
  `pr_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '설문참여일',
  PRIMARY KEY (`pq_idx`,`mi_id`),
  UNIQUE KEY `pr_idx` (`pr_idx`),
  KEY `fk_poll_result_mi_id` (`mi_id`),
  CONSTRAINT `fk_poll_result_mi_id` FOREIGN KEY (`mi_id`) REFERENCES `t_member_info` (`mi_id`),
  CONSTRAINT `fk_poll_result_pq_idx` FOREIGN KEY (`pq_idx`) REFERENCES `t_poll_question` (`pq_idx`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='설문조사 결과 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_poll_result`
--

LOCK TABLES `t_poll_result` WRITE;
/*!40000 ALTER TABLE `t_poll_result` DISABLE KEYS */;
INSERT INTO `t_poll_result` VALUES (1,1,'test1',1,'2021-04-26 18:25:19'),(2,1,'test2',1,'2021-04-26 18:25:19'),(3,1,'test3',2,'2021-04-26 18:25:19'),(4,1,'test4',3,'2021-04-26 18:25:19');
/*!40000 ALTER TABLE `t_poll_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_product_info`
--

DROP TABLE IF EXISTS `t_product_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_product_info` (
  `pi_id` char(6) COLLATE utf8_bin NOT NULL COMMENT '상품ID',
  `pi_name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '상품명',
  `cs_id` char(5) COLLATE utf8_bin DEFAULT NULL COMMENT '소분류ID',
  `b_id` char(4) COLLATE utf8_bin DEFAULT NULL COMMENT '브랜드ID',
  `pi_origin` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '원산지',
  `pi_cost` int DEFAULT NULL COMMENT '원가',
  `pi_price` int DEFAULT NULL COMMENT '판매가',
  `pi_discount` int DEFAULT NULL COMMENT '할인율',
  `pi_option` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '옵션(용량)',
  `pi_img1` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '이미지1',
  `pi_img2` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '이미지2',
  `pi_img3` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '이미지3',
  `pi_desc` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '설명이미지',
  `pi_stock` int DEFAULT '-1' COMMENT '재고량(-1:무제한)',
  `pi_salecnt` int DEFAULT '0' COMMENT '판매개수',
  `pi_review` int DEFAULT '0' COMMENT '후기개수',
  `pi_start` float DEFAULT '0' COMMENT '상품별점평균',
  `pi_isview` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '게시여부',
  `pi_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `ai_idx` int DEFAULT NULL COMMENT '관리자 번호',
  PRIMARY KEY (`pi_id`),
  KEY `fk_product_info_cs_id` (`cs_id`),
  KEY `fk_product_info_b_id` (`b_id`),
  KEY `fk_product_info_ai_idx` (`ai_idx`),
  CONSTRAINT `fk_product_info_ai_idx` FOREIGN KEY (`ai_idx`) REFERENCES `t_admin_info` (`ai_idx`),
  CONSTRAINT `fk_product_info_b_id` FOREIGN KEY (`b_id`) REFERENCES `t_brand` (`b_id`),
  CONSTRAINT `fk_product_info_cs_id` FOREIGN KEY (`cs_id`) REFERENCES `t_cata_small` (`cs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='상품 정보 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_product_info`
--

LOCK TABLES `t_product_info` WRITE;
/*!40000 ALTER TABLE `t_product_info` DISABLE KEYS */;
INSERT INTO `t_product_info` VALUES ('pdt001','지평막걸리','B1S01','BR05','대한민국',2000,3000,0,'14도;18도','pdt001_1.jpg',NULL,NULL,'pdt001_d.jpg',500,0,0,0,'y','2021-04-16 10:58:32',1),('pdt002','술01','B1S01','BR01','대한민국',1500,2500,10,'11도;15도','pdt002_1.jpg',NULL,NULL,'pdt002_d.jpg',550,0,0,0,'y','2021-04-16 10:58:32',1),('pdt003','술02','B1S01','BR02','대한민국',2000,4000,10,'15도;18도','pdt003_1.jpg',NULL,NULL,'pdt003_d.jpg',-1,0,0,0,'y','2021-04-16 10:58:32',1),('pdt004','술03','B1S01','BR03','대한민국',2500,4500,10,'11도;15도','pdt004_1.jpg',NULL,NULL,'pdt004_d.jpg',-1,0,0,0,'y','2021-04-16 10:58:32',1),('pdt005','술04','B1S02','BR04','대한민국',3000,5000,10,'15도;18도','pdt005_1.jpg',NULL,NULL,'pdt005_d.jpg',600,0,0,0,'y','2021-04-16 10:58:32',1),('pdt006','술05','B1S02','BR05','대한민국',3500,5500,10,'11도;15도','pdt006_1.jpg',NULL,NULL,'pdt006_d.jpg',300,0,0,0,'y','2021-04-16 10:58:32',1),('pdt007','술06','B1S02','BR06','대한민국',1200,3200,10,'15도;18도','pdt007_1.jpg',NULL,NULL,'pdt007_d.jpg',600,0,0,0,'y','2021-04-16 10:58:32',1),('pdt008','술07','B1S02','BR01','대한민국',1800,3800,10,'12도;19도;21도','pdt008_1.jpg',NULL,NULL,'pdt008_d.jpg',300,0,0,0,'y','2021-04-16 10:58:32',1),('pdt009','술08','B1S03','BR02','대한민국',2400,4400,10,'12도;19도;21도','pdt009_1.jpg',NULL,NULL,'pdt009_d.jpg',600,0,0,0,'y','2021-04-16 10:58:32',1),('pdt010','술09','B1S03','BR03','대한민국',3000,5000,10,'12도;19도;21도','pdt010_1.jpg',NULL,NULL,'pdt010_d.jpg',300,0,0,0,'y','2021-04-16 10:58:32',1),('pdt011','술10','B1S03','BR04','대한민국',3600,5600,10,'12도;19도;21도','pdt011_1.jpg',NULL,NULL,'pdt011_d.jpg',600,0,0,0,'y','2021-04-16 10:58:32',1),('pdt012','술11','B2S01','BR05','대한민국',5500,7500,0,'12도;19도;21도','pdt012_1.jpg',NULL,NULL,'pdt012_d.jpg',300,0,0,0,'y','2021-04-16 10:58:32',1),('pdt013','술12','B2S01','BR06','대한민국',8000,10000,0,'33도;39도;41도','pdt013_1.jpg',NULL,NULL,'pdt013_d.jpg',-1,0,0,0,'y','2021-04-16 10:58:32',1),('pdt014','술13','B2S01','BR01','대한민국',10500,12500,0,'33도;39도;41도','pdt014_1.jpg',NULL,NULL,'pdt014_d.jpg',-1,0,0,0,'y','2021-04-16 10:58:32',1),('pdt015','술14','B2S02','BR02','대한민국',13000,15000,0,'33도;39도;41도','pdt015_1.jpg',NULL,NULL,'pdt015_d.jpg',-1,0,0,0,'y','2021-04-16 10:58:32',1),('pdt016','술15','B2S02','BR03','대한민국',15500,17500,0,'33도;39도;41도','pdt016_1.jpg',NULL,NULL,'pdt016_d.jpg',-1,0,0,0,'y','2021-04-16 10:58:32',1),('pdt017','술16','B2S02','BR04','대한민국',18000,20000,0,'33도;39도;41도','pdt017_1.jpg',NULL,NULL,'pdt017_d.jpg',-1,0,0,0,'y','2021-04-16 10:58:32',1),('pdt018','술17','B2S03','BR05','대한민국',20500,22500,0,'45도;50도;60도','pdt018_1.jpg',NULL,NULL,'pdt018_d.jpg',-1,0,0,0,'y','2021-04-16 10:58:32',1),('pdt019','술18','B2S03','BR06','대한민국',23000,25000,0,'45도;50도;60도','pdt019_1.jpg',NULL,NULL,'pdt019_d.jpg',-1,0,0,0,'y','2021-04-16 10:58:32',1),('pdt020','술19','B2S03','BR01','대한민국',25500,27500,0,'45도;50도;60도','pdt020_1.jpg',NULL,NULL,'pdt020_d.jpg',-1,0,0,0,'n','2021-04-16 10:58:32',1);
/*!40000 ALTER TABLE `t_product_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_cart_list`
--

DROP TABLE IF EXISTS `v_cart_list`;
/*!50001 DROP VIEW IF EXISTS `v_cart_list`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_cart_list` AS SELECT 
 1 AS `pi_id`,
 1 AS `pi_price`,
 1 AS `pi_discount`,
 1 AS `oc_cnt`,
 1 AS `pi_option`,
 1 AS `oc_option`,
 1 AS `pi_name`,
 1 AS `pi_img1`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_order_list`
--

DROP TABLE IF EXISTS `v_order_list`;
/*!50001 DROP VIEW IF EXISTS `v_order_list`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_order_list` AS SELECT 
 1 AS `oi_id`,
 1 AS `mi_id`,
 1 AS `pi_id`,
 1 AS `od_pdtimg`,
 1 AS `od_pdtname`,
 1 AS `od_option`,
 1 AS `od_cnt`,
 1 AS `oi_pay`,
 1 AS `oi_date`,
 1 AS `oi_status`,
 1 AS `pi_isview`,
 1 AS `pi_stock`,
 1 AS `cb_id`,
 1 AS `cb_name`,
 1 AS `cs_id`,
 1 AS `cs_name`,
 1 AS `b_id`,
 1 AS `b_name`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_review_list`
--

DROP TABLE IF EXISTS `v_review_list`;
/*!50001 DROP VIEW IF EXISTS `v_review_list`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_review_list` AS SELECT 
 1 AS `br_idx`,
 1 AS `mi_id`,
 1 AS `concat(a.br_title, ' - ' , c.od_option )`,
 1 AS `br_date`,
 1 AS `br_star`,
 1 AS `br_img1`,
 1 AS `pi_name`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping events for database 'testmall'
--

--
-- Dumping routines for database 'testmall'
--
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_admin_insert` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_admin_insert`(aiid varchar(20), aipwd varchar(20), ainame varchar(20), aiisrun varchar(20))
begin
	insert into t_admin_info (ai_id, ai_pwd, ai_name, ai_isrun) values (aiid, aipwd, ainame, aiisrun);
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_admin_update` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_admin_update`(aipwd varchar(20), aiisrun varchar(20), aiid varchar(20))
begin
	update t_admin_info set ai_pwd = aipwd, ai_isrun = aiisrun where ai_id = aiid;
    -- id와 이름은 변경 불가로 하고 변경하려는 관리자의 id를 받아와 조건을 사용
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_banner_insert` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_banner_insert`(
	mbimg varchar(50), mburl varchar(100), mbseq int, mbisview char(1), aiidx int)
begin
	insert into t_main_banner (mb_img, mb_url, mb_seq, mb_isview, ai_idx) 
	values (mbimg, mburl, mbseq, mbisview, aiidx);
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_banner_insert2` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_banner_insert2`(oldidx int, -- 기존 이미지중 안보이게 하려는 이미지의 idx
	mbimg varchar(50), mburl varchar(100), mbseq int, mbisview char(1), aiidx int)
begin
	if mbisview = 'y' then -- 새로 추가하는 이미지를 보여주는 경우
		if oldidx > 0 then -- 기존의 이미지들(4개) 중 안보이게 할 이미지가 있을 경우
			update t_main_banner set mb_isview = 'n' where mb_idx = oldidx;
        end if;
    end if;
	insert into t_main_banner (mb_img, mb_url, mb_seq, mb_isview, ai_idx) 
	values (mbimg, mburl, mbseq, mbisview, aiidx);
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_banner_update` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_banner_update`(
	mbidx int, mbimg varchar(50), mburl varchar(100), mbseq int, mbisview char(1))
begin
	update t_main_banner set mb_img = mbimg, mb_url = mburl, mb_seq = mbseq, 
	mb_isview = mbisview where mb_idx = mbidx;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_banner_update2` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_banner_update2`(oldidx int, oldview char(1), 
-- oldidx : 새롭게 보여질 이미지를 위해 숨길 이미지번호, oldview : 수정하려는 이미지의 원래 보임여부
	mbidx int, mbimg varchar(50), mburl varchar(100), mbseq int, mbisview char(1))
begin
	if mbisview = 'y' and oldview = 'n' and oldidx > 0 then
    -- 수정하는 이미지가 보임으로 변경되고 기존의 보이는 이미지가 4개일 경우
		update t_main_banner set mb_isview = 'n' where mb_idx = oldidx;
    end if;
	update t_main_banner set mb_img = mbimg, mb_url = mburl, mb_seq = mbseq, 
	mb_isview = mbisview where mb_idx = mbidx;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_log_insert` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_log_insert`(_idx int, _ip varchar(15))
begin
	insert into t_admin_login_log (ai_idx, al_ip) values (_idx , _ip);
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_menu_insert` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_menu_insert`(amidx int, amname varchar(20), amlink varchar(100),amparent int, amseq int, aiidx int )
begin
	insert into t_admin_menu (am_idx, am_name, am_link, am_parent, am_seq, am_regdate, ai_idx) values (amidx, amname, amlink, amparent, amseq, amregdate, aiidx);
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_menu_update` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_menu_update`(amidx int, amname varchar(20), amlink varchar(100), amparent int, amseq int, aiidx int)
begin
update t_admin_menu set am_name = amname, am_link =amlink, am_parent = amparent, am_seq = amseq, ai_idx =aiidx
where am_idx = amidx;
-- id 와 이름은 변경불가로 하고 변경하려는 관리자의 id를 받아와 조건을 사용
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_pms_insert` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_pms_insert`(apaiidx int, amix int, appms int, aiidx int)
begin
insert into t_admin_permission (ap_ai_idx, am_idx, ap_pms, ai_idx) values (apaiidx, amix, appms, aiidx);
-- 한 관리자가 특정 메뉴에 대해 권한을 부여하는 쿼리
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_pms_update` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_pms_update`(apidx int, appms int)
begin
update t_admin_permission set ap_pms = appms where ap_idx = apidx;
-- 한 관리자가 특정 메뉴에 대해 권한을 부여하는 쿼리
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_poll_insert` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_poll_insert`(
	pqquestion varchar(200), pqex1 varchar(100), pqex2 varchar(100), pqex3 varchar(100), 
	pqex4 varchar(100), pqex5 varchar(100), pqsdate varchar(10), pqedate varchar(10),
	pqendmsg varchar(100), pqstatus char(1), aiidx int)
begin
	insert into t_poll_question (pq_question, pq_ex1, pq_ex2, pq_ex3, pq_ex4, pq_ex5, 
		pq_sdate, pq_edate, pq_endmsg, pq_status, ai_idx) 
	values (pqquestion, pqex1, pqex2, pqex3, pqex4, pqex5, pqsdate, pqedate, 
		pqendmsg, pqstatus, aiidx);
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_poll_update` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_poll_update`(
	pqidx int, pqquestion varchar(200), pqex1 varchar(100), pqex2 varchar(100), 
	pqex3 varchar(100), pqex4 varchar(100), pqex5 varchar(100), pqsdate varchar(10), 
	pqedate varchar(10), pqendmsg varchar(100), pqstatus char(1))
begin
	update t_poll_question set pq_question = pqquestion, pq_ex1 = pqex1, pq_ex2 = pqex2, 
		pq_ex3 = pqex3, pq_ex4 = pqex4, pq_ex5 = pqex5, pq_sdate = pqsdate, 
		pq_edate = pqedate, pq_endmsg = pqendmsg, pq_status = pqstatus 
	where pq_idx = pqidx;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_ad_qna_answer` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ad_qna_answer`(bqidx int, bqanswer text, aiidx int)
begin
	update t_brd_qna set bq_answer = bqanswer, bq_adate = now(), bq_status = 'b', ai_idx = aiidx
    where bq_idx = bqidx;
    -- 기존의 답변 내용에 추가하는 식으로 계속 답변을 늘릴 수 있
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_all_order_update` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_all_order_update`(aiidx int, miid varchar(20), oiid char(10), oistatus char(1), oiname varchar(20), oiphone varchar(13), oizip char(5), oiaddr1 varchar(50), oiaddr2 varchar(50))
begin

    if aiidx = 0 then 	-- 사용자가 변경하는 경우
		update t_order_info 
        set oiname = oiname, oi_phone = oiphone, oi_zip = oizip, oi_addr1 = oiaddr1, oi_addr2 = oiaddr2, oi_status = oistatus 
        where mi_id = miid and oi_id = oiid;
	else				-- 관리자가 변경하는 경우
		update t_order_info 
        set oiname = oiname, oi_phone = oiphone, oi_zip = oizip, oi_addr1 = oiaddr1, oi_addr2 = oiaddr2, oi_status = oistatus, 
			oi_modify = now(), ai_idx = aiidx
        where oi_id = oiid;
    end if;

end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_cart_delete` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_cart_delete`(ocidx int)
begin
	delete from t_order_cart where oc_idx = ocidx;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_cart_insert` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_cart_insert`(
	miid varchar(20), piid char(6), ocoption varchar(10), occnt int)
begin
	declare cnt int; -- 동일상품/옵션의 개수를 저장할 변수
    select count(oc_idx) into cnt from t_order_cart 
	where mi_id = miid and pi_id = piid and oc_option = ocoption;
    -- 동일 회원/상품/옵션을 가진 목록이 카트테이블에 몇 개인지를 추출하는 쿼리

	if cnt = 0 then -- 기존 목록에 동일한 상품이 없을 경우
		insert into t_order_cart (mi_id, pi_id, oc_option, oc_cnt) 
		values (miid, piid, ocoption, occnt);
	else
		update t_order_cart set oc_cnt = oc_cnt + occnt 
		where mi_id = miid and pi_id = piid and oc_option = ocoption;
	end if;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_cart_update` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_cart_update`(
	ocidx int, miid varchar(20), piid char(6), ocoption varchar(10), occnt int)
begin
	declare idx int; -- 동일상품/옵션의 개수를 저장할 변수
    select ifnull(oc_idx, 0) into idx from t_order_cart 
	where mi_id = miid and pi_id = piid and oc_option = ocoption;
    -- 동일 회원/상품/옵션을 가진 목록이 카트테이블에 있으면 idx를 추출하는 쿼리

	if idx = 0 then
		update t_order_cart set oc_option = ocoption, oc_cnt = occnt where oc_idx = ocidx;
		-- 옵션/수량을 변경해주는 쿼리
    else
    begin
		update t_order_cart set oc_cnt = oc_cnt + occnt where oc_idx = idx;
		-- 동일한 옵션을 가진 상품이 이미 있으므로 수량만 추가해 주는 쿼리
		delete from t_order_cart where oc_idx = ocidx;
        -- 변경한 상품은 수량을 추가했으므로 변경한 상품을 삭제해야 함
	end;
    end if;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_member_insert` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_member_insert`(miid varchar(20), mipwd varchar(20), 
   miname varchar(20), mibirth char(10), migender char(1), miphone varchar(13), 
   miemail varchar(50), miissns char(1), miismail char(1), mipoint int, 
    mirebank varchar(50), miaccount varchar(50), mirecommend varchar(20), 
   mazip char(5), maaddr1 varchar(50), maaddr2 varchar(50), 
    mppoint int, mpcontent varchar(50))
begin
   insert into t_member_info (mi_id, mi_pwd, mi_name, mi_birth, mi_gender, mi_phone, 
      mi_email, mi_issns, mi_ismail, mi_point, mi_rebank, mi_account, mi_recommend)
   values (miid, mipwd, miname, mibirth, migender, miphone, miemail, miissns, 
      miismail, mipoint, mirebank, miaccount, mirecommend);

   insert into t_member_addr (mi_id, ma_zip, ma_addr1, ma_addr2) 
    values (miid, mazip, maaddr1, maaddr2);

   insert into t_member_point (mi_id, mp_point, mp_content) 
    values (miid, mppoint, mpcontent);
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_order_insert` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_order_insert`(miid varchar(20), oiname varchar(20), oiphone varchar(13), oizip char(5), oiaddr1 varchar(50), oiaddr2 varchar(50), oipayment char(1), 
	oipay int, oiusepnt int, oidelipay int, oistatus char(1), oicmt varchar(100), pi_id char(6), od_pdtname varchar(20), od_pdtimg varchar(50), od_pdtprice int, od_cnt int, od_option varchar(10), 
    ocidxs varchar(50))
begin
	declare oiid char(10);
	declare end_row boolean default false;
    declare cpiid char(6);			-- 쇼핑 카트내 상품 id를 저정할 변수
    declare coption varchar(10);	-- 쇼핑 카트내 상품 옵션를 저정할 변수
    declare ccnt int;				-- 쇼핑 카트내 상품 개수를 저정할 변수
    declare cname varchar(20);		-- 쇼핑 카트내 상품 이름를 저정할 변수
    declare cimg varchar(50);		-- 쇼핑 카트내 상품 이미지를 저정할 변수
    declare cprice int;				-- 쇼핑 카트내 상품 단가를 저정할 변수
    
    declare c_set cursor for 
		select a.pi_id, a.oc_option, a.oc_cnt, b.pi_name, b.pi_img1, b.pi_price
        from t_order_cart a, t_product_info b
        where a.pi_id = b.pi_id and a.oc_idx in (ocidxs);	-- 구매하려는 목록을 카트에서 추출하여 커서로 생성
	declare continue handler for not found set end_row = true;
	
    -- 새 주문번호 생성
    -- 주문번호 생성 (년월일(yymmdd) + 일련번호(1001))
	-- yymmdd1001 : 오늘(주문일) 주문한 내역들 중 가장 마지막에 주문한 주문번호에 1을 더한 값
	-- 단, 오늘(주문일) 주문한 내역이 하나도 없을 경우 yymmdd1001로 생성해야 함
	select if(count(oi_id) = 0, concat(right(replace(date(now()), '-', ''), 6), '1000'), oi_id) + 1 into oiid from t_order_info 
	where date(oi_date) = curdate() order by oi_id desc limit 1;

	-- 주문 정보 등록 쿼리
	insert into t_order_info (oi_id, mi_id, oi_name, oi_phone, oi_zip, oi_addr1, oi_addr2, oi_payment, oi_pay, oi_usepnt, oi_delipay, oi_status, oi_cmt)  
	values(oiid, miid, oiname, oiphone, oizip, oiaddr1, oiaddr2, oipayment, oipay, oiusepnt, oidelipay, oistatus, oicmt);
    
	-- 포인트 사용내역 등록 및 보유포인트 변경 쿼리
    if oiusepnt > 0 then -- 주문시 포인트를 사용했을 경우
    begin
		insert into t_member_point (mi_id, mp_kind, mp_point, mp_linknum, mp_content) 
		values (miid, 'u', oiusepnt, oiid, '주문시 사용');

		update t_member_info set mi_point = mi_point - oiusepnt where mi_id = miid;
	end;
	end if;
        
	if ocidxs = '0' then -- 바로 구매로 구매시
    begin
		-- 주문 상세 등록 쿼리
		insert into t_order_detail (oi_id, pi_id, od_pdtname, od_pdtimg, od_pdtprice, od_cnt, od_option) 
		values (oiid, piid, odpdtname, odpdtimg, odpdtprice, odcnt, odoption);
        
		-- 상품 판매개수 및 재고량 변경 쿼리
		update t_product_info set pi_salecnt = pi_salecnt + odcnt, 
		pi_stock = pi_stock - if(pi_stock = -1, 0, odcnt) where pi_id = piid;
	end;
	else -- 쇼핑카트를 통해 구매시
    begin
        open c_set; -- c_set이라는 커서를 열어줌
        
		lblLoop:while true do
			fetch c_set into cpiid, coption , ccnt, cname, cimg, cprice;	-- 커서내에 있는 데이터들을 차례대로 변수에 저장
			if end_row then
				leave lblLoop;
			end if;
            
			-- 주문 상세 등록 쿼리
			insert into t_order_detail (oi_id, pi_id, od_pdtname, od_pdtimg, od_pdtprice, od_cnt, od_option) 
			values (oiid, cpiid, cname, cimg, cprice, ccnt, coption);
            
			-- 상품 판매개수 및 재고량 변경 쿼리
			update t_product_info set pi_salecnt = pi_salecnt + ccnt, 
			pi_stock = pi_stock - if(pi_stock = -1, 0, ccnt) where pi_id = cpiid;
		end while;

		-- 쇼핑카트에서 구매한 상품 삭제 쿼리
		delete from t_order_cart where oc_idx in (ocidxs);

		close c_set;
	end;
	end if;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_poll_result_insert` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_poll_result_insert`(pqidx int, miid varchar(20), prex int)
begin
	-- 현재 진행중인 설문인지 여부를 검사 후 참여시킴
	declare pqstatus char(1);
	select pq_status into pqstatus from t_poll_question where pq_idx = pqidx;
    if pqstatus = 'b' then -- 현재 진행중인 설문조사이면
    	insert into t_poll_result (pq_idx, mi_id, pr_ex) values (pqidx, miid, prex);
	end if;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_qna_delete` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_qna_delete`(bqidx int)
begin
	delete from t_brd_qna where bq_idx = bqidx and bq_status = 'a';
    -- 질문에 대한 수정으로 관리자가 답변을 달기 전에만 삭제가 가능함
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_qna_update` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_qna_update`(bqidx int, bqcata varchar(20), bqtitle varchar(100), bqcontent text, bqimg varchar(50))
begin
	update t_brd_qna set bq_cata = bqcata, bq_title = bqtitle, 
    bq_content = bqcontent, bq_img = bqimg
    where bq_idx = bqidx and bq_status = 'a';
    -- 질문에 대한 수정으로 관리자가 답변을 달기 전에만 수정이 가능함
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `v_cart_list`
--

/*!50001 DROP VIEW IF EXISTS `v_cart_list`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_cart_list` AS select `a`.`pi_id` AS `pi_id`,`a`.`pi_price` AS `pi_price`,`a`.`pi_discount` AS `pi_discount`,`b`.`oc_cnt` AS `oc_cnt`,`a`.`pi_option` AS `pi_option`,`b`.`oc_option` AS `oc_option`,`a`.`pi_name` AS `pi_name`,`a`.`pi_img1` AS `pi_img1` from (`t_product_info` `a` join `t_order_cart` `b`) where (`a`.`pi_id` = `b`.`pi_id`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_order_list`
--

/*!50001 DROP VIEW IF EXISTS `v_order_list`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_order_list` AS select `a`.`oi_id` AS `oi_id`,`a`.`mi_id` AS `mi_id`,`b`.`pi_id` AS `pi_id`,`b`.`od_pdtimg` AS `od_pdtimg`,`b`.`od_pdtname` AS `od_pdtname`,`b`.`od_option` AS `od_option`,`b`.`od_cnt` AS `od_cnt`,`a`.`oi_pay` AS `oi_pay`,`a`.`oi_date` AS `oi_date`,`a`.`oi_status` AS `oi_status`,`c`.`pi_isview` AS `pi_isview`,`c`.`pi_stock` AS `pi_stock`,`d`.`cb_id` AS `cb_id`,`d`.`cb_name` AS `cb_name`,`c`.`cs_id` AS `cs_id`,`e`.`cs_name` AS `cs_name`,`c`.`b_id` AS `b_id`,`f`.`b_name` AS `b_name` from (((((`t_order_info` `a` join `t_order_detail` `b`) join `t_product_info` `c`) join `t_cata_big` `d`) join `t_cata_small` `e`) join `t_brand` `f`) where ((`a`.`oi_id` = `b`.`oi_id`) and (`b`.`pi_id` = `c`.`pi_id`) and (`d`.`cb_id` = `e`.`cb_id`) and (`e`.`cs_id` = `c`.`cs_id`) and (`f`.`b_id` = `c`.`b_id`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_review_list`
--

/*!50001 DROP VIEW IF EXISTS `v_review_list`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_review_list` AS select `a`.`br_idx` AS `br_idx`,`a`.`mi_id` AS `mi_id`,concat(`a`.`br_title`,' - ',`c`.`od_option`) AS `concat(a.br_title, ' - ' , c.od_option )`,`a`.`br_date` AS `br_date`,`a`.`br_star` AS `br_star`,`a`.`br_img1` AS `br_img1`,`b`.`pi_name` AS `pi_name` from ((`t_brd_review` `a` join `t_product_info` `b`) join `t_order_detail` `c`) where ((`a`.`pi_id` = `b`.`pi_id`) and (`a`.`oi_id` = `c`.`oi_id`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-28 18:10:18
