-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 29, 2025 at 11:18 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kvisko`
--

-- --------------------------------------------------------

--
-- Table structure for table `answers`
--

CREATE TABLE `answers` (
  `id` int(11) NOT NULL,
  `question_id` int(11) DEFAULT NULL,
  `answer_text` text NOT NULL,
  `is_correct` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `answers`
--

INSERT INTO `answers` (`id`, `question_id`, `answer_text`, `is_correct`) VALUES
(13, 4, '', 0),
(14, 4, 'Filip IV lijepi', 0),
(15, 4, 'Džejms Kuk', 1),
(16, 4, 'Kristofer Kolumbo', 0),
(17, 4, 'Magelan', 0),
(18, 5, 'Barcelona 1992', 1),
(19, 5, 'Barcelona 1881', 0),
(20, 5, 'Barcelona 1999', 0),
(21, 5, 'Barcelona 1995', 0),
(22, 6, 'Justinijan', 0),
(23, 6, 'Heraklije', 0),
(24, 6, 'Konstantin VII', 0),
(25, 6, 'Teodozije', 1),
(26, 7, 'U uhu', 1),
(27, 7, 'U mozgu', 0),
(28, 7, 'U crijevima', 0),
(29, 7, 'U plućima', 0),
(30, 8, 'Blue Gene', 0),
(31, 8, 'Emanuel Lasker', 1),
(32, 8, 'Gary Kasparov', 0),
(33, 8, 'George Lasquet', 0),
(34, 9, 'Henri Mancini', 1),
(35, 9, 'Bob Ford', 0),
(36, 9, 'Džejms Luzon', 0),
(37, 9, 'Henri Simonija', 0),
(38, 10, 'Petrarca', 0),
(39, 10, 'Lamburdžini', 0),
(40, 10, 'Musolini', 0),
(41, 10, 'Pasolini', 1),
(42, 11, 'Džosef Melburn', 0),
(43, 11, 'Mark Atp', 0),
(44, 11, 'Roland Garos', 1),
(45, 11, 'Frank Vimbledon', 0),
(46, 12, 'Trgovački putnik', 1),
(47, 12, 'Turistički agent', 0),
(48, 12, 'Vozač taksija', 0),
(49, 12, 'Ulični čistač', 0),
(50, 13, 'Recenzant', 0),
(51, 13, 'Urednik', 0),
(52, 13, 'Mentor', 0),
(53, 13, 'Lektor', 1),
(54, 14, 'Sabor', 0),
(55, 14, 'Konkordat', 1),
(56, 14, 'Koncil', 0),
(57, 14, 'Povelja', 0),
(58, 15, 'Antigona', 1),
(59, 15, 'Domenika', 0),
(60, 15, 'Helena', 0),
(61, 15, 'Julija', 0),
(62, 16, 'Trepćete', 0),
(63, 16, 'Žvačete', 1),
(64, 16, 'Dišete', 0),
(65, 16, 'Mičete ušima', 0),
(66, 17, 'Artur', 0),
(67, 17, 'Vilim osvajač', 0),
(68, 17, 'Valiant', 1),
(69, 17, 'Arthur II', 0),
(70, 18, 'Vibrafon', 1),
(71, 18, 'Skilofon', 0),
(72, 18, 'Činele', 0),
(73, 18, 'Klavir', 0),
(74, 19, 'Rijeka', 0),
(75, 19, 'Sazviježđe', 1),
(76, 19, 'Galaksija', 0),
(77, 19, 'Zvijezda padalica', 0),
(78, 20, 'Finac', 1),
(79, 20, 'Norvežanin', 0),
(80, 20, 'Šveđanin', 0),
(81, 20, 'Estonac', 0),
(82, 21, 'Francuska', 0),
(83, 21, 'Italija', 0),
(84, 21, 'Holandija', 1),
(85, 21, 'Austrija', 0),
(86, 22, 'Srebra', 1),
(87, 22, 'Zlata', 0),
(88, 22, 'Žive', 0),
(89, 22, 'Bakra', 0),
(90, 23, 'Finska', 0),
(91, 23, 'Danska', 1),
(92, 23, 'Francuska', 0),
(93, 23, 'Njemačka', 0),
(94, 24, 'Kamen', 0),
(95, 24, 'Ramen', 0),
(96, 24, 'Amen', 0),
(97, 24, 'Omen', 1),
(98, 25, 'Colt 45', 0),
(99, 25, 'Magnum 44', 1),
(100, 25, 'Glock 9mm', 0),
(101, 25, 'Deagle 50cal.', 0),
(102, 26, 'Artemida', 0),
(103, 26, 'Kleopatra', 0),
(104, 26, 'Karolina', 0),
(105, 26, 'Semiramida', 1),
(106, 27, 'Dan nezavisnosti', 1),
(107, 27, 'Dan zahvalnosti', 0),
(108, 27, 'Noć vještica', 0),
(109, 27, 'Uskrs', 0),
(110, 28, 'Tarzana', 1),
(111, 28, 'Indiane Jonesa', 0),
(112, 28, 'Conana', 0),
(113, 28, 'Ramba', 0),
(114, 29, 'Fonetika', 1),
(115, 29, 'Akustika', 0),
(116, 29, 'Polemika', 0),
(117, 29, 'Morfologija', 0),
(118, 30, 'Odbojku', 0),
(119, 30, 'Rukomet', 0),
(120, 30, 'Fudbal', 0),
(121, 30, 'Plivanje', 1),
(122, 31, 'Betoven', 1),
(123, 31, 'Bah', 0),
(124, 31, 'Mocart', 0),
(125, 31, 'Hajdn', 0),
(126, 32, 'Platon', 0),
(127, 32, 'Ovidije', 0),
(128, 32, 'Ciceron', 0),
(129, 32, 'Demosten', 1),
(130, 33, 'Mars', 0),
(131, 33, 'Venera', 1),
(132, 33, 'Zemlja', 0),
(133, 33, 'Jupiter', 0),
(134, 34, 'Himalaja', 0),
(135, 34, 'Iransko-Kavkasko gorje', 0),
(136, 34, 'Ande', 1),
(137, 34, 'Alpe', 0),
(138, 35, 'Marku Fidelu', 0),
(139, 35, 'Krisu Kolumbu', 0),
(140, 35, 'Džonu Makaronu', 0),
(141, 35, 'Benu Džonsonu', 1),
(142, 36, 'u Đenovi', 0),
(143, 36, 'u Milanu', 0),
(144, 36, 'u Rimu', 0),
(145, 36, 'u Napulju', 1),
(146, 37, 'Sepuku', 1),
(147, 37, 'Hiragana', 0),
(148, 37, 'Kojoshi', 0),
(149, 37, 'Mordoshi', 0),
(150, 38, 'Allegro', 1),
(151, 38, 'Adagio', 0),
(152, 38, 'Moderato', 0),
(153, 38, 'Largo', 0),
(154, 39, 'Čaju', 0),
(155, 39, 'Kakau', 0),
(156, 39, 'Kafi', 1),
(157, 39, 'Vinu', 0),
(158, 40, 'Iraku', 0),
(159, 40, 'Pakistan', 1),
(160, 40, 'Afganistan', 0),
(161, 40, 'Ukrajina', 0),
(162, 41, 'Nikson', 1),
(163, 41, 'Klinton', 0),
(164, 41, 'Buš', 0),
(165, 41, 'Kenedi', 0),
(166, 42, 'ja ođoh', 0),
(167, 42, 'ja idaše', 0),
(168, 42, 'ja iđah', 0),
(169, 42, 'ja idoh', 1),
(170, 43, 'Urednik', 0),
(171, 43, 'Vozač', 0),
(172, 43, 'Trgovac', 0),
(173, 43, 'Pilot', 1),
(174, 44, 'Put', 0),
(175, 44, 'Sila', 1),
(176, 44, 'Brzina', 0),
(177, 44, 'Vrijeme', 0),
(178, 46, 'Jednorog', 0),
(179, 46, 'Grifon', 1),
(180, 46, 'Minotaur', 0),
(181, 46, 'Eaglion', 0),
(182, 47, 'SAD', 0),
(183, 47, 'Lebanon', 1),
(184, 47, 'Srbija', 0),
(185, 47, 'Velika Britanija', 0),
(190, 49, 'Franc Kafka', 1),
(191, 49, 'Agata Kristi', 0),
(192, 49, 'Alber Kami', 0),
(193, 49, 'Dostojevski', 0),
(194, 50, 'Turskoj', 1),
(195, 50, 'Grčkoj', 0),
(196, 50, 'Italiji', 0),
(197, 50, 'Španiji', 0),
(198, 51, 'Normani', 0),
(199, 51, 'Rimljani', 1),
(200, 51, 'Grci', 0),
(201, 51, 'Vikinzi', 0);

-- --------------------------------------------------------

--
-- Table structure for table `questions`
--

CREATE TABLE `questions` (
  `id` int(11) NOT NULL,
  `text` text NOT NULL,
  `times_asked` int(11) DEFAULT 0,
  `is_active` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `questions`
--

INSERT INTO `questions` (`id`, `text`, `times_asked`, `is_active`) VALUES
(4, 'Ko je bio prvi čovjek koji je nogom stupio na sve kontinente osim Antarktike?', 5, 1),
(5, 'Gdje je odapeta plamena strijela upalila olimpijski plamen?', 5, 1),
(6, 'Ko je 395. godine podijelio Rimsko carstvo na zapadno i istočno?', 6, 1),
(7, 'Gdje se nalazi lavirint u ljudskom tijelu?', 6, 1),
(8, 'Koji šahovski kralj je najduže bio na tronu svjetskog prvaka?', 6, 1),
(9, 'Ko je komponovao muziku koja prati karakterističan hod Pinka Pantera?', 6, 1),
(10, 'Kako se zove slavni italijanski filmski reditelj koji je 1975. pregažen na autocesti u Ostiji blizu Rima?', 6, 1),
(11, 'Koji je slavni avijatičar prvi preletio Sredozemno more, a po njegovom imenjaku se zove turnir u tenisu?', 6, 1),
(12, 'Šta je bio Kafkin Gregor Samsa prije nego se preobrazio u kukca?', 6, 1),
(13, 'Kako se zove osoba koja jezično-stilski uređuje rukopis?', 6, 1),
(14, 'Kako se zove sporazum između svjetovne i crkvene vlasti?', 6, 1),
(15, 'Ko je ne samo Edipova sestra nego i rođena kći?', 6, 1),
(16, 'Šta radite kad pokrećete mandibulu?', 5, 1),
(17, 'Kako se zove vitez crtača Harolda Rudolfa Fostera?', 6, 1),
(18, 'Muzički instrument u kojem tonove stvaraju batići, pločice, cijevi, propeleri i cvjetići?', 6, 1),
(19, 'Šta je Verenikina kosa?', 6, 1),
(20, 'Šta je porijeklom kompozitor Jan Sibelius?', 5, 1),
(21, 'Koja je zemlja prva od 1.1.2002. legalizirala eutanaziju?', 6, 1),
(22, 'Kog metala u zlatnoj olimpijskoj medalji ima 92,5 posto?', 5, 1),
(23, 'U kojoj je zemlji Legoland?', 5, 1),
(24, 'U kojem filmu se u 6 sati ujutro, 6. dana u sedmici, 6.mjeseca rodio Mali Sotona?', 5, 1),
(25, 'Koji je revolver nerazdvojiv od prljavog Harryja?', 6, 1),
(26, 'S kim se povezuju viseći vrtovi?', 6, 1),
(27, 'Koji se blagdan u Sjedinjenim Državama slavi 4. jula?', 5, 1),
(28, 'Edgar Rice Burroughs proslavio se kao autor kojeg lika?', 6, 1),
(29, 'Koja naučna disciplina se bavi artikulacijom i fiziološkim osobinama glasova?', 6, 1),
(30, 'Za koji je sport nadležna FINA?', 5, 1),
(31, 'Ko je komponovao čuvenu klavirsku sonatu u Cis duru nazvanu Mjesečeva sonata?', 6, 1),
(32, 'Ko je Filipu Makedonskom držao filipike?', 6, 1),
(33, 'Koji planet u Sunčevom sistavu se jedina kreće u smjeru kazaljke sata?', 6, 1),
(34, 'Koji je najduži planinski lanac na Zemlji?', 6, 1),
(35, 'Kojem atletičaru je 1988. oduzeta zlatna medalja i poništen rekord na 100 metara?', 6, 1),
(36, 'U kojem je italijanskom gradu prvi puta zamirisala pizza?', 5, 1),
(37, 'Kako se zove obredno samoubistvo japanskih samuraja? ', 6, 1),
(38, 'Koja je muzička oznaka za brz tempo?', 6, 1),
(39, 'Kom napitku je Johan Sebastijan Bah posvetio jednu od svoje 224 kantate?', 6, 1),
(40, 'Gdje se nalaze arheološka nalazišta Harappa i Mohenjo Daro?', 5, 1),
(41, 'Ko je bio predsjednik Sjedinjenih Država u doba kad je prvi čovjek stupio na Mjesec?', 5, 1),
(42, 'Kako glasi 1. lice aorista glagola ići?', 5, 1),
(43, 'Šta je po zanimanju Antoan de Sent Egziperi, pisac Malog princa?', 6, 1),
(44, 'Šta se mjeri njutnima?', 5, 1),
(46, 'Kako se zove mitološko čudovište u kojem su sjedinjeni lav i orao?', 6, 1),
(47, 'U kojoj državi je rođen Keanu Reeves?', 6, 1),
(49, 'Ko je napisao \"Proces\"?', 4, 1),
(50, 'Kojoj državi pripada Istočna Trakija do rijeke Marice?', 3, 1),
(51, 'Koji je narod prvi u Europi počeo upotrebljavati željezni mač?', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `results`
--

CREATE TABLE `results` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `num_correct_answers` int(11) NOT NULL,
  `quiz_date` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `results`
--

INSERT INTO `results` (`id`, `user_id`, `num_correct_answers`, `quiz_date`) VALUES
(1, 8, 0, '2025-05-25 11:19:25'),
(2, 8, 15, '2025-05-25 11:22:11'),
(3, 8, 1, '2025-05-26 12:55:57'),
(4, 8, 0, '2025-05-26 15:47:47'),
(5, 8, 2, '2025-05-26 15:49:32'),
(6, 8, 2, '2025-05-26 15:59:39'),
(7, 8, 1, '2025-05-26 16:09:22'),
(8, 8, 7, '2025-05-26 17:08:23'),
(9, 8, 13, '2025-05-26 17:12:48'),
(10, 9, 0, '2025-05-26 17:21:10'),
(11, 9, 3, '2025-05-26 17:32:57'),
(13, 8, 0, '2025-05-26 18:13:11'),
(14, 8, 1, '2025-05-26 18:13:26'),
(15, 8, 6, '2025-05-26 18:13:56'),
(16, 8, 0, '2025-05-26 18:25:27'),
(17, 8, 0, '2025-05-26 18:25:43'),
(18, 8, 5, '2025-05-26 18:26:11'),
(19, 8, 0, '2025-05-28 15:14:24'),
(20, 8, 5, '2025-05-29 09:03:49'),
(24, 11, 5, '2025-05-29 09:19:48'),
(25, 12, 3, '2025-05-29 09:35:27'),
(26, 11, 1, '2025-05-29 10:14:23'),
(27, 11, 10, '2025-05-29 10:15:05'),
(28, 9, 7, '2025-05-29 10:16:28'),
(29, 13, 1, '2025-05-29 10:19:14'),
(30, 13, 15, '2025-05-29 10:20:00'),
(31, 8, 3, '2025-05-29 10:21:43'),
(32, 14, 5, '2025-05-29 10:23:21'),
(33, 15, 1, '2025-05-29 10:24:03'),
(34, 16, 1, '2025-05-29 10:24:41'),
(35, 16, 8, '2025-05-29 10:25:08'),
(36, 17, 0, '2025-05-29 10:25:55'),
(37, 17, 1, '2025-05-29 10:26:02'),
(38, 17, 2, '2025-05-29 10:26:12'),
(39, 17, 0, '2025-05-29 10:26:19'),
(40, 18, 7, '2025-05-29 10:27:11'),
(41, 19, 7, '2025-05-29 10:28:15'),
(42, 19, 2, '2025-05-29 10:30:36'),
(43, 8, 1, '2025-05-29 11:03:10'),
(44, 8, 8, '2025-05-29 11:05:40'),
(45, 20, 1, '2025-05-29 11:06:48');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `name` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `correct_answers` int(11) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `is_admin` tinyint(1) DEFAULT 0,
  `is_suspended` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `name`, `surname`, `password`, `correct_answers`, `username`, `is_admin`, `is_suspended`) VALUES
(4, 'ha@test.com', 'ha', 'ha', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'admin01', 1, 0),
(8, 'talizams@gmail.com', 'Sara', 'Talic', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'sara', 0, 0),
(9, 'stefotala@gmail.com', 'stefan', 'stefan', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'stefo', 0, 0),
(11, 'sara.talic@student.pmf.unibl.org', 'Marko', 'Maric', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'marko01', 0, 0),
(12, 'tupperware.zorica@gmail.com', 'Mara', 'Maric', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'mara', 0, 0),
(13, 'n.djajic20@gmail.com', 'Pero', 'Peric', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'pero', 0, 0),
(14, 'ana123@example.test', 'Ana', 'Antic', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'ana', 0, 0),
(15, 'milica@example.test', 'Milica', 'Micic', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'milica77', 0, 0),
(16, 'janko@example.test', 'Janko', 'Jaric', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'janko12', 0, 0),
(17, 'nole1234@example.test', 'Novak', 'Djokovic', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'nolethebest', 0, 0),
(18, 'blackjohn@test.example', 'John', 'Black', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'john', 0, 0),
(19, 'jovvan@example.test', 'Jovan', 'Jovic', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'kafka', 0, 0),
(20, 'sara.talic@example.test', 'Sara', 'Sara', 'f5ebb11c65ce6bf63d43b88d1129c8ca', NULL, 'sara1', 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `answers`
--
ALTER TABLE `answers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `question_id` (`question_id`);

--
-- Indexes for table `questions`
--
ALTER TABLE `questions`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `results`
--
ALTER TABLE `results`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `unique_email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `answers`
--
ALTER TABLE `answers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=202;

--
-- AUTO_INCREMENT for table `questions`
--
ALTER TABLE `questions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- AUTO_INCREMENT for table `results`
--
ALTER TABLE `results`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `answers`
--
ALTER TABLE `answers`
  ADD CONSTRAINT `answers_ibfk_1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `results`
--
ALTER TABLE `results`
  ADD CONSTRAINT `results_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
