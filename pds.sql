-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 07, 2023 at 04:11 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pds`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `id` int(11) NOT NULL,
  `metamask_id` char(42) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(256) NOT NULL,
  `password` text NOT NULL,
  `job` text DEFAULT NULL,
  `company` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`id`, `metamask_id`, `name`, `email`, `password`, `job`, `company`) VALUES
(13, '0x68C267D8058d58140b428E8d794F5d3716a1dA8F', 'Delia Elena Barbuta', 'bdeliaelena@yahoo.com', '$2a$10$wPFnt4YQwsD/9iF4ZD7JguK3xZmVHvuEj84aL3NsR1pexVvVJK/cS', 'Student', 'AC Iasi'),
(18, '0x68C267D8058d58140b428E8d794F5d3716a1dA8A', 'Adrian Alexandrescu', 'adrian.alexandrescu@gmail.com', '$2a$10$gj4.FEjWlVaP9f4ZqfgduOb4VfusxRNbIF/Ba8rgjk92TH6BzqW/6', 'Profesor', 'Univeristatea Tehnica Gheorghe Asachi'),
(19, '0xB9d4d08AC4BdE559E45E6Ea49D777a5526bd3b7D', 'Ioana Prioteasa', 'priut@gmail.com', '$2a$10$VITcSaPkCyiMPhldPfwoW.4eiqdrt3VaI4q3Df/roFDqz5GBQnwS6', 'Student', 'Gheorghe Asachi Technical University'),
(20, '0x887019B853A0D5708401BCAbEFDFE5355717Ed53', 'Gabriel Scinteie', 'gabi.scinteie@gmail.com', '$2a$10$iExGzrnDGBrkyUogYeOBcuPD5v8iES.p/QwlvMe71/NnR3KAXoJcK', 'Student', 'Gheorghe Asachi Technical University'),
(21, '0x80600567eB258Ab855cA133cF94776ee33F7Ab7e', 'Stefan Paraschiv', 'stefan.paraschiv@gmail.com', '$2a$10$6YHDEgnff/iwIf3XbsSt8O.xiFCo4AP32RYi2fWy0ixiBxSOi9eoa', 'Student', 'Gheorghe Asachi Technical University'),
(22, '0x68C267D8058d68140b428E8d794F5d3716a1dA8F', 'Mircea Stefan', 'mircea.taune@gmail.com', '$2a$10$1h4jT0a1mogJrGiy4YPeou2Zy6ADh5crmuNiKdQQYI5nIIm0Uc/F.', 'Developer', 'Sigmatic'),
(26, '0xbcb3DC16aEe20F32c8bc775842D6c10C1d6dC18e', 'Test User', 'test.user@gmail.com', '$2a$10$/F2tUtVAG8775X2fHUV9PO8WBXdrXO0nW7d2uRr.TC9V2nvSYRCy6', 'test', 'test');

-- --------------------------------------------------------

--
-- Table structure for table `account_tests`
--

CREATE TABLE `account_tests` (
  `id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  `tag_level_id` int(11) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `received_badge` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `account_tests`
--

INSERT INTO `account_tests` (`id`, `account_id`, `tag_id`, `tag_level_id`, `created_at`, `received_badge`) VALUES
(28, 13, 2, 6, '2023-03-28 15:34:33', 1),
(30, 13, 3, 9, '2023-03-28 15:41:44', 1),
(34, 13, 1, 1, '2023-04-06 14:33:12', 1),
(35, 19, 3, 3, '2023-04-09 21:02:03', 1),
(36, 19, 2, 7, '2023-04-09 21:10:45', 1),
(39, 20, 2, 2, '2023-04-09 21:37:30', 1),
(40, 20, 1, 1, '2023-04-09 21:43:18', 1),
(41, 20, 3, 3, '2023-04-09 21:44:52', 1),
(42, 21, 2, 2, '2023-04-09 22:36:41', 1),
(43, 21, 3, 8, '2023-04-09 22:40:27', 1),
(45, 19, 1, 5, '2023-04-18 21:24:41', 1),
(46, 21, 1, 5, '2023-04-19 22:13:26', 1),
(47, 22, 1, NULL, '2023-04-23 21:24:32', 0),
(48, 22, 2, 7, '2023-04-23 21:29:33', 0),
(53, 26, 2, 7, '2023-05-03 20:46:06', 1),
(54, 26, 3, 8, '2023-05-16 16:33:26', 1),
(56, 26, 1, 4, '2023-05-16 21:38:31', 0);

-- --------------------------------------------------------

--
-- Table structure for table `articles`
--

CREATE TABLE `articles` (
  `id` int(11) NOT NULL,
  `uploaded_by` int(11) NOT NULL,
  `title` varchar(400) NOT NULL,
  `description` text DEFAULT NULL,
  `path` text DEFAULT NULL,
  `grade` double DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `review_until` datetime NOT NULL,
  `payment_status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `articles`
--

INSERT INTO `articles` (`id`, `uploaded_by`, `title`, `description`, `path`, `grade`, `created_at`, `review_until`, `payment_status`) VALUES
(92, 13, 'A decentralized paper dissemination system employing blockchain technology, peer review and expert badges', 'Peer review represents the status-quo when it comes to evaluating research articles that are submitted to conferences and journals. The significance of a computer science article is given by the prestige of the publication and is correlated with the inclusion in the ISI Web of Science index.\r\nThis paper discusses the insufficiency of the current paper publication control-flow paradigm and proposes a decentral- ized approach to the paper dissemination and the peer review processes. On the one hand, decentralization and transparency are obtained by employing smart contracts, through blockchain technology. On the other hand, an optimization of the paper rating system is obtained by employing a system of expert badges, based on NFTs, which ensure that the peer review process is just and that only specialists in the fields associated to the contributed paper offer proficient feedback. Other proposed facets include the control flows regarding the remuneration of reviewers, a method of allowing the proposed system to expand based on the community’s input, and a solution for allowing the organization of conferences.', 'files/13_A decentralized paper dissemination system employing blockchain technology, peer review and expert badges.pdf', 8, '2023-04-09 16:36:41', '2023-04-08 16:36:41', 1),
(93, 13, 'Natural Language Processing', 'Language is way of communicating your words. Language helps in understanding the world, we get a better insight of the world. Language\nhelps speakers to be as vague or as precise as they like. NLP Stands for natural language processing. . Natural languages are those languages that are\nspoken by the people.Natural language processing girdles everything a computer needs to understand natural language and also generates natural\nlanguage.Natural language processing (NLP) is a field of computer science, artificial intelligence, and linguistics mainly focuses on the interactions\nbetween computers and human languages or natural languages. NLP is focussed on the area of human computer interaction. The need for natural\nlanguage processing was also felt because there is a wide storage of information recorded or stored in natural language that could be accessible via\ncomputers. Information is constantly generated in the form of books, news, business and government reports, and scientific papers, many of which are\navailable online or even in some reports. A system requiring a great deal of information must be able to process natural language to retrieve much of the\ninformation available on computers. Natural language processing is an interesting and difficult field in which we have to develop and evaluate or analyse\nrepresentation and reasoning theories. All of the problems of AI arise in this domain; solving \"the natural language problem\" is as difficult as solving \"the\nAI problem\" because any field can be expressed or can be depicted in natural language.', 'files/13_Natural Language Processing.pdf', 5, '2023-04-09 16:39:17', '2023-04-08 16:39:17', 1),
(94, 13, 'Cyberthreat Hunting', 'Ransomware is currently one of the most significant\r\ncyberthreats to both national infrastructure and the individual, often requiring severe treatment as an antidote. Triaging ransomware based on its similarity with well-known ransomware\r\nsamples is an imperative preliminary step in preventing a\r\nransomware pandemic. Selecting the most appropriate triaging\r\nmethod can improve the precision of further static and dynamic\r\nanalysis in addition to saving significant t ime a nd e ffort. Currently, the most popular and proven triaging methods are fuzzy\r\nhashing, import hashing and YARA rules, which can ascertain\r\nwhether, or to what degree, two ransomware samples are similar\r\nto each other. However, the mechanisms of these three methods\r\nare quite different and their comparative assessment is difficult.\r\nTherefore, this paper presents an evaluation of these three\r\nmethods for triaging the four most pertinent ransomware categories WannaCry, Locky, Cerber and CryptoWall. It evaluates\r\ntheir triaging performance and run-time system performance,\r\nhighlighting the limitations of each method.', 'files/13_Cyberthreat_Hunting.pdf', 9, '2023-04-09 16:43:59', '2023-04-17 16:43:59', 1),
(95, 13, 'Security Services Using Blockchains: A State of the Art Survey', 'This paper surveys blockchain-based approaches for several security services. These services include authentication, confidentiality, privacy and access control list, data and resource\r\nprovenance, and integrity assurance. All these services are critical for the current distributed applications, especially due to the large amount of data being processed over the networks\r\nand the use of cloud computing. Authentication ensures that\r\nthe user is who he/she claims to be. Confidentiality guarantees\r\nthat data cannot be read by unauthorized users. Privacy provides the users the ability to control who can access their data.\r\nProvenance allows an efficient tracking of the data and resources\r\nalong with their ownership and utilization over the network.\r\nIntegrity helps in verifying that the data has not been modified\r\nor altered. These services are currently managed by centralized\r\ncontrollers, for example, a certificate authority. Therefore, the\r\nservices are prone to attacks on the centralized controller. On\r\nthe other hand, blockchain is a secured and distributed ledger\r\nthat can help resolve many of the problems with centralization. The objectives of this paper are to give insights on the\r\nuse of security services for current applications, to highlight the\r\nstate of the art techniques that are currently used to provide\r\nthese services, to describe their challenges, and to discuss how\r\nthe blockchain technology can resolve these challenges. Further,\r\nseveral blockchain-based approaches providing such security services are compared thoroughly. Challenges associated with using\r\nblockchain-based security services are also discussed to spur\r\nfurther research in this area.', 'files/13_Security Services Using Blockchains- A State of the Art Survey.pdf', 5, '2023-04-09 16:48:51', '2023-04-08 16:48:51', 1),
(104, 13, 'A survey on sentiment analysis challenges', 'With accelerated evolution of the internet as websites, social networks, blogs, online portals, reviews, opinions, recommendations, ratings, and feedback are generated by writers. This writer generated sentiment content can be about books, people, hotels, products, research, events, etc. These sentiments become very beneficial for businesses, governments, and individuals. While this content is meant to be useful, a bulk of this writer generated content require using the text mining techniques and sentiment analysis. But there are several challenges facing the sentiment analysis and evaluation process. These challenges become obstacles in analyzing the accurate meaning of sentiments and detecting the suitable sentiment polarity. Sentiment analysis is the practice of applying natural language processing and text analysis techniques to identify and extract subjective information from text. This paper presents a survey on the sentiment analysis challenges relevant to their approaches and techniques.', 'files/13_A survey on sentiment analysis challenges.pdf', 8, '2023-04-20 19:52:38', '2023-04-19 19:52:38', 1),
(105, 13, 'sadasdsadsa', 'asdsadsadsa', 'files/13_adeverinta_student.pdf', NULL, '2023-04-23 21:08:12', '2023-05-23 21:08:12', 0),
(112, 13, 'A decentralized paper dissemination system employing blockchain technology, peer review and expert badges', 'Abstract—Peer review represents the status-quo when it comes to evaluating research articles that are submitted to conferences and journals. The significance of a computer science article is given by the prestige of the publication and is correlated with the inclusion in the ISI Web of Science index.\r\nThis paper discusses the issues of the current paper publication paradigm and proposes a decentralized approach to the paper dissemination and the peer review processes. On the one hand, decentralization and transparency are obtained by employing smart contracts, through blockchain technology. On the other hand, an optimization of the paper rating system is obtained by employing a system of expert badges, based on NFTs, which ensure that the peer review process is just and that only specialists in the fields associated to the contributed paper offer proficient feedback. Other proposed facets include the remuneration of reviewers, a method of allowing the proposed system to expand based on the community’s input, and a solution for allowing the organization of conferences.', 'files/13_A decentralized paper dissemination system employing blockchain technology, peer review and expert badges.pdf', 9, '2023-05-03 20:44:20', '2023-05-08 20:44:20', 1);

-- --------------------------------------------------------

--
-- Table structure for table `article_authors`
--

CREATE TABLE `article_authors` (
  `id` int(11) NOT NULL,
  `article_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `article_authors`
--

INSERT INTO `article_authors` (`id`, `article_id`, `account_id`) VALUES
(98, 92, 13),
(99, 92, 18),
(100, 93, 13),
(101, 94, 13),
(102, 95, 13),
(111, 104, 13),
(112, 105, 13),
(119, 112, 13);

-- --------------------------------------------------------

--
-- Table structure for table `article_tag_levels`
--

CREATE TABLE `article_tag_levels` (
  `id` int(11) NOT NULL,
  `article_id` int(11) NOT NULL,
  `tag_level_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `article_tag_levels`
--

INSERT INTO `article_tag_levels` (`id`, `article_id`, `tag_level_id`) VALUES
(79, 92, 7),
(80, 93, 1),
(81, 94, 8),
(83, 95, 3),
(82, 95, 7),
(92, 104, 4),
(93, 105, 6),
(100, 112, 7);

-- --------------------------------------------------------

--
-- Table structure for table `levels`
--

CREATE TABLE `levels` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `levels`
--

INSERT INTO `levels` (`id`, `name`) VALUES
(1, 'Beginner'),
(2, 'Intermediate'),
(3, 'Expert');

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `id` int(11) NOT NULL,
  `article_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `recommendation` text DEFAULT NULL,
  `article_grade` double NOT NULL,
  `earned_amount` double DEFAULT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`id`, `article_id`, `account_id`, `recommendation`, `article_grade`, `earned_amount`, `created_at`) VALUES
(8, 92, 19, 'Great exploration of the subject', 9, NULL, '2023-04-09 21:21:16'),
(9, 92, 20, 'Decent', 7, NULL, '2023-04-09 21:47:16'),
(10, 93, 20, 'Brings nothing new to the table.', 5, NULL, '2023-04-09 21:48:07'),
(11, 95, 20, '.', 5, NULL, '2023-04-09 22:33:17'),
(13, 95, 21, '.', 5, NULL, '2023-04-09 22:41:37'),
(14, 94, 21, '.', 8.8, 1e16, '2023-04-18 20:50:18'),
(22, 104, 19, '.', 6.8, 0.005000000000000001, '2023-04-20 19:57:25'),
(23, 104, 21, '.', 8.6, 0.004999999999999997, '2023-04-20 19:58:04'),
(24, 112, 26, 'This should be the recommendation', 8.6, 0.01, '2023-05-03 20:47:23');

-- --------------------------------------------------------

--
-- Table structure for table `review_criteria`
--

CREATE TABLE `review_criteria` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `min_grade` int(11) NOT NULL,
  `max_grade` int(11) NOT NULL,
  `description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `review_criteria`
--

INSERT INTO `review_criteria` (`id`, `name`, `min_grade`, `max_grade`, `description`) VALUES
(1, 'Methodology', 1, 10, 'This refers to the scientific methods used by the authors to conduct their research. It should be determined whether the research was conducted in a rigorous and valid manner.'),
(2, 'Relevance', 1, 10, 'This criterion evaluates how relevant the research is to the field or discipline being studied. The research should address a significant gap in the current knowledge and should have the potential to influence future research.'),
(3, 'Originality', 1, 10, 'This criterion evaluates how original and innovative the research is. It\'s important to determine whether the study presents a new perspective or approach to the topic being studied and whether it contributes to the development of new theories or concepts.'),
(4, 'Clarity', 1, 10, 'This criterion evaluates how well the research is presented and how easy it is to understand. The paper should be well-organized, the methods and results clearly described, and the writing style appropriate for the intended audience.'),
(5, 'Ethical considerations', 1, 10, 'This criterion evaluates whether the research was conducted in an ethical manner and whether it presents unbiased results.');

-- --------------------------------------------------------

--
-- Table structure for table `review_criteria_grades`
--

CREATE TABLE `review_criteria_grades` (
  `id` int(11) NOT NULL,
  `review_id` int(11) NOT NULL,
  `criteria_id` int(11) NOT NULL,
  `value` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `review_criteria_grades`
--

INSERT INTO `review_criteria_grades` (`id`, `review_id`, `criteria_id`, `value`) VALUES
(31, 8, 1, 9),
(32, 8, 2, 9),
(33, 8, 3, 9),
(34, 8, 4, 9),
(35, 8, 5, 9),
(36, 9, 1, 7),
(37, 9, 2, 7),
(38, 9, 3, 7),
(39, 9, 4, 7),
(40, 9, 5, 7),
(41, 10, 1, 5),
(42, 10, 2, 5),
(43, 10, 3, 5),
(44, 10, 4, 5),
(45, 10, 5, 5),
(46, 11, 1, 5),
(47, 11, 2, 5),
(48, 11, 3, 5),
(49, 11, 4, 5),
(50, 11, 5, 5),
(56, 13, 1, 5),
(57, 13, 2, 5),
(58, 13, 3, 5),
(59, 13, 4, 5),
(60, 13, 5, 5),
(61, 14, 1, 8),
(62, 14, 2, 10),
(63, 14, 3, 7),
(64, 14, 4, 9),
(65, 14, 5, 10),
(101, 22, 1, 6),
(102, 22, 2, 7),
(103, 22, 3, 8),
(104, 22, 4, 6),
(105, 22, 5, 7),
(106, 23, 1, 9),
(107, 23, 2, 9),
(108, 23, 3, 8),
(109, 23, 4, 10),
(110, 23, 5, 7),
(111, 24, 1, 8),
(112, 24, 2, 9),
(113, 24, 3, 8),
(114, 24, 4, 8),
(115, 24, 5, 10);

-- --------------------------------------------------------

--
-- Table structure for table `tags`
--

CREATE TABLE `tags` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `tags`
--

INSERT INTO `tags` (`id`, `name`) VALUES
(1, 'Natural Language Processing'),
(2, 'Blockchain'),
(3, 'Security');

-- --------------------------------------------------------

--
-- Table structure for table `tag_levels`
--

CREATE TABLE `tag_levels` (
  `id` int(11) NOT NULL,
  `level_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  `CID` char(46) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `tag_levels`
--

INSERT INTO `tag_levels` (`id`, `level_id`, `tag_id`, `CID`) VALUES
(1, 1, 1, 'QmPuCUr6y4GVJabEBAVHNK7G2g1uC1avwW3SRtquxcLkkt'),
(2, 3, 2, 'QmQxpnJtRTRS9LFjyjKLyf2rT3GFJWBTBpfqEgdyQ59HKE'),
(3, 2, 3, 'QmcWDpuevjyPF5eeCswn3axdwabGwGnS2E42ZcpzNsGaHN'),
(4, 2, 1, 'QmWdK2YGoGUR1oobGjVHxjpP2jabB3jiWPCXYf8YqpfqEo'),
(5, 3, 1, 'QmX59nqeB5c45h48ee8WL4aeawdeL1xfaRRsmCWiXfcLe1'),
(6, 1, 2, 'QmWZhJdJW16nKigJXWT4PQurZjCTfPrRH15XRFdLftH8kX'),
(7, 2, 2, 'QmVbPEvHrVkqs98f72i6zWTZRzrsbtVnei6swiH3eWPxeE'),
(8, 3, 3, 'QmeGYXWZgAnb5C5rchkEZ6HGx82TYh8qiXvVnAV6jd1pcQ'),
(9, 1, 3, 'Qmc4U92d7bZZp5iiHzYuoHmt97p9h2EMsKVj7JAzsSrnHC');

-- --------------------------------------------------------

--
-- Table structure for table `test_answers`
--

CREATE TABLE `test_answers` (
  `id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  `answer` text NOT NULL,
  `valid` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `test_answers`
--

INSERT INTO `test_answers` (`id`, `question_id`, `answer`, `valid`) VALUES
(1, 1, 'A generative model learns the joint probability distribution of the input and output variables, while a discriminative model learns the conditional probability distribution of the output variable given the input variable.', 1),
(3, 1, 'A generative model learns the conditional probability distribution of the output variable given the input variable, while a discriminative model learns the joint probability distribution of the input and output variables.', 0),
(4, 1, 'A generative model learns the mean and variance of the input and output variables, while a discriminative model learns the median and mode of the input and output variables.', 0),
(5, 1, 'A generative model learns the probability of the input variable given the output variable, while a discriminative model learns the probability of the output variable given the input variable.', 0),
(6, 3, 'Stemming maps words to their dictionary form, while lemmatization reduces words to their root form.', 0),
(7, 3, 'Stemming reduces words to their root form, while lemmatization maps words to their dictionary form.', 1),
(8, 3, 'Stemming and lemmatization are the same thing.', 0),
(9, 3, 'Stemming and lemmatization are both used to remove stop words from a piece of text.', 0),
(10, 4, 'Precision is the ratio of true negatives to the sum of true negatives and false negatives, while recall is the ratio of true positives to the sum of true positives and false positives.', 0),
(11, 4, 'Precision is the ratio of true negatives to the sum of true negatives and false positives, while recall is the ratio of true positives to the sum of true positives and false negatives.', 0),
(12, 4, 'Precision is the ratio of true positives to the sum of true positives and false negatives, while recall is the ratio of true positives to the sum of true positives and false positives.', 0),
(13, 4, 'Precision is the ratio of true positives to the sum of true positives and false positives, while recall is the ratio of true positives to the sum of true positives and false negatives.', 1),
(14, 5, 'Removing unused groups of hexadecimal numbers', 0),
(15, 5, 'Removing a group of only 0\'s', 1),
(16, 5, 'Converting 8 groups of 4 hexadecimal numbers into a valid IPv4 address', 0),
(17, 5, 'All options are correct', 0),
(18, 6, 'Preparation', 0),
(19, 6, 'Containment', 1),
(20, 6, 'Identification', 0),
(21, 6, 'Eradication', 0),
(22, 7, 'ICMP Echo Request, TCP SYN, SW-1TCH, ICMP Timestamp Request', 0),
(23, 7, 'ICMP handshake Request, TCP ACK, NMAP', 0),
(24, 7, 'ICMP Echo Request, ICMP Timestamp Request, TCP SYN, TCP ACK', 1),
(25, 8, 'A type of cryptographic hash function used to secure transactions on the blockchain.', 0),
(26, 8, 'A data structure used to organize and verify transactions in a block.', 1),
(27, 8, 'A consensus mechanism that allows nodes to vote on the validity of transactions.', 0),
(28, 8, 'A method for compressing transaction data on the blockchain.', 0),
(29, 9, 'Permissioned blockchains allow anyone to participate in the consensus process, while permissionless blockchains require permission from a central authority.', 0),
(30, 9, 'Permissioned blockchains are more secure than permissionless blockchains because they require users to undergo a rigorous vetting process.', 0),
(31, 9, 'Permissionless blockchains are more decentralized than permissioned blockchains because they allow anyone to participate in the consensus process.', 1),
(32, 9, 'Permissioned blockchains are more scalable than permissionless blockchains because they can handle more transactions per second.', 0),
(33, 10, 'A method for encrypting private keys to secure transactions on the blockchain.', 0),
(34, 10, 'A consensus mechanism that allows nodes to vote on the validity of transactions without revealing their identities.', 0),
(35, 10, 'A cryptographic protocol that allows a user to prove that they know a certain piece of information without revealing it.', 1),
(36, 10, 'A method for validating transactions that is more energy-efficient than proof-of-work.', 0);

-- --------------------------------------------------------

--
-- Table structure for table `test_questions`
--

CREATE TABLE `test_questions` (
  `id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  `question` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `test_questions`
--

INSERT INTO `test_questions` (`id`, `tag_id`, `question`) VALUES
(1, 1, 'What is the difference between a generative and a discriminative model in machine learning?'),
(3, 1, 'What is the difference between stemming and lemmatization in text preprocessing?'),
(4, 1, 'What is the difference between precision and recall in a classification model?'),
(5, 3, 'Shortening an IPv6 address means:'),
(6, 3, 'In which phase of PICERL is blocking attackers usually done?'),
(7, 3, 'Which types of packets can be used to determine if a system is alive on the network?'),
(8, 2, 'What is a \"Merkle tree\" in the context of blockchain technology?'),
(9, 2, 'What is the difference between a \"permissioned\" and a \"permissionless\" blockchain?'),
(10, 2, 'What is a \"zero-knowledge proof\" in blockchain technology?');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email_UNIQUE` (`email`),
  ADD UNIQUE KEY `metamask_id_UNIQUE` (`metamask_id`);

--
-- Indexes for table `account_tests`
--
ALTER TABLE `account_tests`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_accounts_has_tags_accounts1_idx` (`account_id`),
  ADD KEY `fk_accounts_has_tags_tags1` (`tag_level_id`),
  ADD KEY `fk_account_tests_tag_id` (`tag_id`);

--
-- Indexes for table `articles`
--
ALTER TABLE `articles`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_article_uploaded_by` (`uploaded_by`);

--
-- Indexes for table `article_authors`
--
ALTER TABLE `article_authors`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `article_id` (`article_id`,`account_id`),
  ADD KEY `fk_article_authors_account_id` (`account_id`);

--
-- Indexes for table `article_tag_levels`
--
ALTER TABLE `article_tag_levels`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `article_id` (`article_id`,`tag_level_id`),
  ADD KEY `fk_article_tag_levels_tag_level_id` (`tag_level_id`);

--
-- Indexes for table `levels`
--
ALTER TABLE `levels`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_reviews_articles1_idx` (`article_id`),
  ADD KEY `fk_reviews_account_id` (`account_id`);

--
-- Indexes for table `review_criteria`
--
ALTER TABLE `review_criteria`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `review_criteria_grades`
--
ALTER TABLE `review_criteria_grades`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `review_id` (`review_id`,`criteria_id`),
  ADD KEY `fk_reviews_has_review_criterias_review_criterias1_idx` (`criteria_id`),
  ADD KEY `fk_reviews_has_review_criterias_reviews1_idx` (`review_id`);

--
-- Indexes for table `tags`
--
ALTER TABLE `tags`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tag_levels`
--
ALTER TABLE `tag_levels`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `level_id` (`level_id`,`tag_id`),
  ADD KEY `fk_tags_has_levels_levels1_idx` (`level_id`),
  ADD KEY `fk_tags_has_levels_tags1_idx` (`tag_id`,`level_id`);

--
-- Indexes for table `test_answers`
--
ALTER TABLE `test_answers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_test_answers_test_questions1_idx` (`question_id`);

--
-- Indexes for table `test_questions`
--
ALTER TABLE `test_questions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_test_questions_tags1_idx` (`tag_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `account_tests`
--
ALTER TABLE `account_tests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT for table `articles`
--
ALTER TABLE `articles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=117;

--
-- AUTO_INCREMENT for table `article_authors`
--
ALTER TABLE `article_authors`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=124;

--
-- AUTO_INCREMENT for table `article_tag_levels`
--
ALTER TABLE `article_tag_levels`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=105;

--
-- AUTO_INCREMENT for table `levels`
--
ALTER TABLE `levels`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `review_criteria`
--
ALTER TABLE `review_criteria`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `review_criteria_grades`
--
ALTER TABLE `review_criteria_grades`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=121;

--
-- AUTO_INCREMENT for table `tags`
--
ALTER TABLE `tags`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tag_levels`
--
ALTER TABLE `tag_levels`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `test_answers`
--
ALTER TABLE `test_answers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `test_questions`
--
ALTER TABLE `test_questions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account_tests`
--
ALTER TABLE `account_tests`
  ADD CONSTRAINT `fk_account_tests_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`),
  ADD CONSTRAINT `fk_accounts_has_tags_accounts1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_accounts_has_tags_tags1` FOREIGN KEY (`tag_level_id`) REFERENCES `tag_levels` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `articles`
--
ALTER TABLE `articles`
  ADD CONSTRAINT `fk_article_uploaded_by` FOREIGN KEY (`uploaded_by`) REFERENCES `accounts` (`id`);

--
-- Constraints for table `article_authors`
--
ALTER TABLE `article_authors`
  ADD CONSTRAINT `fk_article_authors_account_id` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  ADD CONSTRAINT `fk_article_authors_article_id` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`);

--
-- Constraints for table `article_tag_levels`
--
ALTER TABLE `article_tag_levels`
  ADD CONSTRAINT `fk_article_tag_levels_article_id` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`),
  ADD CONSTRAINT `fk_article_tag_levels_tag_level_id` FOREIGN KEY (`tag_level_id`) REFERENCES `tag_levels` (`id`);

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `fk_reviews_account_id` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  ADD CONSTRAINT `fk_reviews_articles1` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `review_criteria_grades`
--
ALTER TABLE `review_criteria_grades`
  ADD CONSTRAINT `fk_reviews_has_review_criterias_review_criterias1` FOREIGN KEY (`criteria_id`) REFERENCES `review_criteria` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_reviews_has_review_criterias_reviews1` FOREIGN KEY (`review_id`) REFERENCES `reviews` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `tag_levels`
--
ALTER TABLE `tag_levels`
  ADD CONSTRAINT `fk_tags_has_levels_levels1` FOREIGN KEY (`level_id`) REFERENCES `levels` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_tags_has_levels_tags1` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `test_answers`
--
ALTER TABLE `test_answers`
  ADD CONSTRAINT `fk_test_answers_test_questions1` FOREIGN KEY (`question_id`) REFERENCES `test_questions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `test_questions`
--
ALTER TABLE `test_questions`
  ADD CONSTRAINT `fk_test_questions_tags1` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
