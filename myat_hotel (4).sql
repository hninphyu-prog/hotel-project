-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 04, 2025 at 08:58 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `myat_hotel`
--

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `booking_id` int(11) NOT NULL,
  `guest_id` int(11) NOT NULL,
  `emp_id` int(11) NOT NULL,
  `booking_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`booking_id`, `guest_id`, `emp_id`, `booking_date`) VALUES
(8, 100006, 1, '2025-07-22'),
(9, 100006, 1, '2025-07-22'),
(10, 100007, 1, '2025-07-22'),
(11, 100008, 1, '2025-07-24'),
(12, 100009, 1, '2025-07-24'),
(13, 100010, 1, '2025-07-25'),
(14, 100011, 1, '2025-07-25'),
(15, 100012, 1, '2025-07-25'),
(16, 100013, 1, '2025-07-27'),
(17, 100014, 1, '2025-07-27'),
(18, 100015, 1, '2025-07-27'),
(19, 100016, 1, '2025-07-28'),
(20, 100017, 1, '2025-07-28'),
(21, 100018, 1, '2025-07-28'),
(22, 100019, 1, '2025-07-28'),
(23, 100020, 1, '2025-07-29'),
(24, 100021, 1, '2025-07-30'),
(25, 100022, 1, '2025-07-30'),
(26, 100023, 1, '2025-07-31'),
(27, 100024, 1, '2025-08-01'),
(28, 100025, 1, '2025-08-01'),
(29, 100026, 5, '2025-08-04');

-- --------------------------------------------------------

--
-- Table structure for table `booking_detail`
--

CREATE TABLE `booking_detail` (
  `booking_detail_id` int(11) NOT NULL,
  `booking_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `check_in_date` date NOT NULL,
  `check_out_date` date NOT NULL,
  `room_status` varchar(50) NOT NULL,
  `price` int(11) NOT NULL DEFAULT 0,
  `payment_status_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking_detail`
--

INSERT INTO `booking_detail` (`booking_detail_id`, `booking_id`, `room_id`, `check_in_date`, `check_out_date`, `room_status`, `price`, `payment_status_id`) VALUES
(11, 10, 1, '2025-07-25', '2025-07-30', 'Check-out', 100, 1),
(12, 11, 1, '2025-07-24', '2025-07-25', 'Check-out', 100, 1),
(13, 11, 2, '2025-07-24', '2025-07-25', 'Check-out', 100, 1),
(14, 11, 4, '2025-07-24', '2025-07-25', 'Check-out', 200, 1),
(15, 12, 1, '2025-07-26', '2025-07-28', 'Check-out', 100, 1),
(16, 12, 2, '2025-07-28', '2025-07-31', 'Check-out', 100, 1),
(17, 13, 3, '2025-07-25', '2025-07-30', 'Check-in', 200, 1),
(18, 14, 11, '2025-07-25', '2025-07-30', 'Check-out', 400, 1),
(19, 14, 12, '2025-07-25', '2025-07-30', 'Check-out', 400, 1),
(20, 15, 4, '2025-07-25', '2025-07-30', 'Check-out', 200, 1),
(21, 15, 5, '2025-07-25', '2025-07-30', 'Check-out', 300, 1),
(22, 16, 2, '2025-07-27', '2025-07-28', 'Check-out', 100, 1),
(23, 17, 1, '2025-07-31', '2025-08-01', 'Check-out', 100, 1),
(24, 17, 2, '2025-07-31', '2025-08-01', 'Check-out', 100, 1),
(25, 18, 6, '2025-07-27', '2025-07-29', 'Check-out', 300, 1),
(26, 18, 1, '2025-08-02', '2025-08-05', 'Check-out', 100, 1),
(27, 18, 1, '2025-08-09', '2025-08-19', 'Check-out', 100, 1),
(28, 19, 1, '2025-08-14', '2025-08-16', 'Booking', 100, 1),
(29, 20, 1, '2025-07-28', '2025-07-31', 'Booking', 100, 1),
(30, 21, 2, '2025-07-29', '2025-07-30', 'Booking', 100, 1),
(31, 22, 1, '2025-07-31', '2025-08-01', 'Booking', 100, 1),
(32, 23, 3, '2025-07-29', '2025-07-30', 'Booking', 200, 1),
(33, 23, 4, '2025-07-29', '2025-07-30', 'Booking', 200, 1),
(34, 23, 6, '2025-07-29', '2025-07-30', 'Booking', 300, 1),
(36, 24, 3, '2025-07-31', '2025-08-01', 'Check-in', 200, 1),
(37, 24, 9, '2025-08-01', '2025-08-04', 'Booking', 250, 1),
(38, 25, 1, '2025-08-01', '2025-08-03', 'Booking', 100, 1),
(40, 27, 2, '2025-08-01', '2025-08-02', 'Check-in', 100, 1),
(41, 28, 2, '2025-08-01', '2025-08-05', 'Check-out', 100, 1),
(42, 29, 1, '2025-08-04', '2025-08-05', 'Check-in', 100, 1);

-- --------------------------------------------------------

--
-- Table structure for table `capacity`
--

CREATE TABLE `capacity` (
  `CapacityID` int(11) NOT NULL,
  `Capacity` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `capacity`
--

INSERT INTO `capacity` (`CapacityID`, `Capacity`) VALUES
(1, '25'),
(2, '50'),
(3, '100'),
(4, '300'),
(5, '500');

-- --------------------------------------------------------

--
-- Table structure for table `city`
--

CREATE TABLE `city` (
  `city_id` int(11) NOT NULL,
  `region_state_id` int(11) NOT NULL,
  `city_name` varchar(50) NOT NULL,
  `city_code` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `city`
--

INSERT INTO `city` (`city_id`, `region_state_id`, `city_name`, `city_code`) VALUES
(1, 1, 'Bhamo Township', 'BaMaNa'),
(2, 1, 'Chipwi Township', 'KhaHpaNa'),
(3, 1, 'Dawthponeyan Township', 'DaHpYa'),
(4, 1, 'Hopin Township', 'HaPaNa'),
(5, 1, 'Hpakant Township', 'HpaKaNa'),
(6, 1, 'Injangyang Township', 'AhGaYa'),
(7, 1, 'Kamaing Township', 'KaMaTa'),
(8, 1, 'Kan Paik Ti Township', 'KaPaTa'),
(9, 1, 'Khaunglanhpu Township', 'KhaLaHpa'),
(10, 1, 'Lwegel Township', 'LaGaNa'),
(11, 1, 'Machanbaw Township', 'MaKhaBa'),
(12, 1, 'Mansi Township', 'MaSaNa'),
(13, 1, 'Mogaung Township', 'MaKaTa'),
(14, 1, 'Mohnyin Township', 'MaNyaNa'),
(15, 1, 'Momauk Township', 'MaMaNa'),
(16, 1, 'Myitkynia Township', 'MaKaNa'),
(17, 1, 'Myo Hla Township', 'MaLaNa'),
(18, 1, 'Nawngmun Township', 'NaMaNa'),
(19, 1, 'Pang War Township', 'PaWaNa'),
(20, 1, 'Pannandin Township', 'PaNaDa'),
(21, 1, 'Puta-O Township', 'PaTaAh'),
(22, 1, 'Sadung Township', 'SaDaNa'),
(23, 1, 'Shin Bway Yang Township', 'YaBaYa'),
(24, 1, 'Shwegu Township', 'YaKaNa'),
(25, 1, 'Sinbo Township', 'SaBaNa'),
(26, 1, 'Sumprabum Township', 'SaPaYa'),
(27, 1, 'Tanai Township', 'TaNaNa'),
(28, 1, 'Tsawlaw Township', 'TaSaLa'),
(29, 1, 'Waingmaw Township', 'WaMaNa'),
(30, 2, 'Bawlakhe Township', 'baLaKha'),
(31, 2, 'Demoso Township', 'DaMaSa'),
(32, 2, 'Hpasawng Township', 'HpaSaNa'),
(33, 2, 'Hpruso Township', 'HpaYaSa'),
(34, 2, 'Loikaw Township', 'LakaNa'),
(35, 2, 'Mese Township', 'PaSaNa'),
(36, 2, 'Shadaw Township', 'YaTaNa'),
(37, 2, 'Ywar Thit Township', 'YaThaNa'),
(38, 3, 'Baw Ga Li Township', 'BaGaLa'),
(39, 3, 'Hlaingbwe Township', 'LaBaNa'),
(40, 3, 'Hpa-An Township', 'BaAhNa'),
(41, 3, 'Hpapun Township', 'HpaPaNa'),
(42, 3, 'Hpayarthonesu Township', 'BaThaSa'),
(43, 3, 'Kamarmaung Township', 'KaMaMa'),
(44, 3, 'Kawkareik Township', 'KaKaYa'),
(45, 3, 'Kyaikdon Township', 'KaDaNa'),
(46, 3, 'Kyainseikgyi Township', 'KaSaNa'),
(47, 3, 'Kyondoe Township', 'KaDaTa'),
(48, 3, 'Leik Tho Township', 'LaThaNa'),
(49, 3, 'Myawaddy Township', 'MaWaTa'),
(50, 3, 'Paingkyon Township', 'PaKaNa'),
(51, 3, 'Shan Ywar Thit Township', 'YaYaTha'),
(52, 3, 'Su Ka Li Township', 'SaKaLa'),
(53, 3, 'Thandaung Township', 'ThaTaNa'),
(54, 3, 'Thandaunggyi Township', 'ThaTaKa'),
(55, 3, 'Lay Myaing(Waw Lay) Tow Township', 'WaLaMa'),
(56, 4, 'Chikha Township', 'KaKhaNa'),
(57, 4, 'Falam Township', 'HpaLaNa'),
(58, 4, 'Hakha Township', 'HaKhaNa'),
(59, 4, 'Kanpetlet Township', 'KaPaLa'),
(60, 4, 'Matupi Township', 'MaTaPa'),
(61, 4, 'Mindat Township', 'MaTaNa'),
(62, 4, 'Paletwa Township', 'PaLaWa'),
(63, 4, 'Rezua Township', 'YaZaNa'),
(64, 4, 'Rihkhawdar Township', 'YaKhaDa'),
(65, 4, 'Samee Township', 'SaMaNa'),
(66, 4, 'Tedim Township', 'TaTaNa'),
(67, 4, 'Thantlang Township', 'HtaTaLa'),
(68, 4, 'Tonzang Township', 'TaZaNa'),
(69, 5, 'Ayadaw Township', 'AhYaTa'),
(70, 5, 'Banmauk Township', 'BaMaNa'),
(71, 5, 'Budalin Township', 'BaTaLa'),
(72, 5, 'Chaung-U Township', 'KhaOuTa'),
(73, 5, 'Hkamti Township', 'KhaTaNa'),
(74, 5, 'Homalin Township', 'HaMaLa'),
(75, 5, 'Indaw Township', 'AhTaNa'),
(76, 5, 'Kale Township', 'KaLaHta'),
(77, 5, 'KaLewa Township', 'KaLaWa'),
(78, 5, 'KanBaLu Township', 'KaBaLa'),
(79, 5, 'Kani Township', 'KaNaNa'),
(80, 5, 'Katha TownShip', 'KaThaNa'),
(81, 5, 'Kawlin Township', 'KaLaTa'),
(82, 5, 'Khin-U Township', 'KhaOuNa'),
(83, 5, 'Kyunhla Township', 'KaLaNa'),
(84, 5, 'Lahe Township', 'LaHaNa'),
(85, 5, 'Layshi Township', 'LaYaNa'),
(86, 5, 'Mawlaik Township', 'MaLaNa'),
(87, 5, 'Mingin Township', 'MaKaNa'),
(88, 5, 'Monywa Township', 'MaYaNa'),
(89, 5, 'Myaung Township', 'MaMaNa'),
(90, 5, 'MinMu Township', 'MaMaTa'),
(91, 5, 'Nanyun Township', 'NaYaNa'),
(92, 5, 'Ngazun Township', 'NgaZaNa'),
(93, 5, 'Pale township', 'PaLaNa'),
(94, 5, 'Paungbyin Township', 'HpaPaNa'),
(95, 5, 'Pinlebu Township', 'PalaBa'),
(96, 5, 'Sagaing Township', 'SaKaNa'),
(97, 5, 'Salingyi Township', 'SaLaKa'),
(98, 5, 'Shwebo Township', 'YaBaNa'),
(99, 5, 'Tabayin Township', 'DaPaYa'),
(100, 5, 'Tamu Townshio', 'TaMaNa'),
(101, 5, 'Taze Township', 'TaSaNa'),
(102, 5, 'Tigyaing', 'HtaKhaNa'),
(103, 5, 'Wetleet Township', 'WaLaNa'),
(104, 5, 'WunTho', 'WaThaNa'),
(105, 5, 'Ye-U Township', 'YaOuNa'),
(106, 5, 'Yinmabin Township', 'YaMaPa'),
(107, 5, 'Kyaukmyaung Township', 'KaMaNa'),
(108, 5, 'Khampat Township', 'KhaPaNa'),
(109, 6, 'Bokpyin Township', 'BaPaNa'),
(110, 6, 'Dawei Township', 'HtaWaNa'),
(111, 6, 'Kaleinaung Township', 'KaLaAh'),
(112, 6, 'Kawthoung Township', 'KaThaNa'),
(113, 6, 'Kyunsu Township', 'KaSaNa'),
(114, 6, 'Launglon Township', 'LaLaNa'),
(115, 6, 'Myeik Township', 'MaMaNa'),
(116, 6, 'Palaw Township', 'PaLaNa'),
(117, 6, 'Tanintharyi Township', 'TaThaYa'),
(118, 6, 'Thayetchaung Township', 'ThaYaKha'),
(119, 6, 'Yepyu Township', 'YaHpaNa'),
(120, 6, 'Khamaukgi Township', 'KhaMaNa'),
(121, 6, 'Myittar Township', 'MaTaNa'),
(122, 6, 'Palauk Myo Nal Khwal', 'PaLaTa'),
(123, 6, 'Karathuri Township', 'KaYaYa'),
(124, 7, 'Daik-U Township', 'DaOuNa'),
(125, 7, 'Gyobingauk Township', 'KaPaKa'),
(126, 7, 'Kawa Township', 'KaWaNa'),
(127, 7, 'Kyaukkyi Township', 'KaKaNa'),
(128, 7, 'Kyauktaga Township', 'KaTaKha'),
(129, 7, 'Letpadan Township', 'LaPaTa'),
(130, 7, 'Minhla Township', 'MaLaNa'),
(131, 7, 'Monyo Township', 'MaNyaNa'),
(132, 7, 'Nattalin Township', 'NaTaLa'),
(133, 7, 'Nyaunglebin Township', 'NyaLaPa'),
(134, 7, 'Okpho Township', 'AhHpaNa'),
(135, 7, 'Oktwin Township', 'AhTaNa'),
(136, 7, 'Padaung Township', 'PaTaNa'),
(137, 7, 'Pauk Kaung Township', 'PaKhaTa'),
(138, 7, 'Bago Township', 'PaKhaTa'),
(139, 7, 'Paungde Township', 'PaTaTa'),
(140, 7, 'Pennwegone Township', 'PaNaKa'),
(141, 7, 'Phyu Township', 'HpaMaNa'),
(142, 7, 'Pyay Township', 'PaNMaNa'),
(143, 7, 'Shwedaung Township', 'YaTaNa'),
(144, 7, 'Shwegyin Township', 'YaKaNa'),
(145, 7, 'Tantabin Township', 'HtaTaPa'),
(146, 7, 'Taungoo Township', 'TaNgaNa'),
(147, 7, 'Thanatpin Township', 'ThaNaPa'),
(148, 7, 'Thayarwady Township', 'ThaWaTa'),
(149, 7, 'Thegon Township', 'ThaKaNa'),
(150, 7, 'Thonze Township', 'ThaSaNa'),
(151, 7, 'Waw Township', 'WaMaNa'),
(152, 7, 'Yedashe Township', 'YaTaYa'),
(153, 7, 'Zigon Township', 'ZaKaNa'),
(154, 7, 'Pyontahsar Township', 'PaTaSa'),
(155, 8, 'Aunglan Township', 'AhLaNa'),
(156, 8, 'Chauk Township', 'KhaMaNa'),
(157, 8, 'Gangaw Township', 'GaGaNa'),
(158, 8, 'Kamma Township', 'KaMaNa'),
(159, 8, 'Magwe Township', 'MaKaNa'),
(160, 8, 'Minbu(Sagu) Township', 'MaBaNa'),
(161, 8, 'Mindon Township', 'MaTaNa'),
(162, 8, 'Minhla Township', 'MaLaNa'),
(163, 8, 'Myaing Township', 'MaMaNa'),
(164, 8, 'Myayhtae Township', 'MaHtaNa'),
(165, 8, 'Myothit Township', 'MaThaNa'),
(166, 8, 'Natmauk Township', 'NaMaNa'),
(167, 8, 'Ngape Township', 'NgaHpaNa'),
(168, 8, 'Pakokku Township', 'PaKhaKa'),
(169, 8, 'Pauk Township', 'PaMaNa'),
(170, 8, 'Pwintbyu Township', 'PaHpaNa'),
(171, 8, 'Salin Township', 'SaLaNa'),
(172, 8, 'Saw Township', 'saMaNa'),
(173, 8, 'seikphyu Township', 'SaHpaNa'),
(174, 8, 'Sidoktaya Township', 'SaTaYa'),
(175, 8, 'Sinbaungwe Township', 'SaPaWa'),
(176, 8, 'Taungdwingyi Township', 'TaTaKa'),
(177, 8, 'Thayet Township', 'ThaYaNa'),
(178, 8, 'Thayet Township', 'ThaYaNa'),
(179, 8, 'Tilin Township', 'HtaLaNa'),
(180, 8, 'Yenangyaung Township', 'YaNaKha'),
(181, 8, 'Yesagyo Township', 'YaSaKa'),
(182, 8, 'Kyaukhtu Myo Nal Khwal', 'KaHtaNa'),
(183, 9, 'Amarapura Township', 'AhMaYa'),
(184, 9, 'Aungmyaythazan Township', 'AhMaZa'),
(185, 9, 'Chanayethazan Township', 'KhaAhZa'),
(186, 9, 'Kyukpadaung Township', 'KaPaTa'),
(187, 9, 'Kyukse Township', 'KaSaNa'),
(188, 9, 'Madaya', 'MaTaYa'),
(189, 9, 'Mahaaungmyay Township', 'MaHaMa'),
(190, 9, 'Mahlaing Township', 'MaLaNa'),
(191, 9, 'Meiktila Township', 'MaHtaLa'),
(192, 9, 'Mogoke Township', 'MaKaNa'),
(193, 9, 'Myingyan Township', 'MaKhaNa'),
(194, 9, 'Myittha Township', 'MaThaNa'),
(195, 9, 'Natogyi Township', 'NaHtaKa'),
(196, 9, 'Ngathayouk Township', 'NgaThaYa'),
(197, 9, 'Ngazun Township', 'NgaZaNa'),
(198, 9, 'Nyaung-U Township', 'NyaOuNa'),
(199, 9, 'Patheingyi Township', 'PaThaKa'),
(200, 9, 'Pyawbwe Township', 'PaBaNa'),
(201, 9, 'Pyinoolwin Township', 'PaOuLa'),
(202, 9, 'Singu Township', 'SaKaNa'),
(203, 9, 'Sintgaing Township', 'SaKaTa'),
(204, 9, 'Tabeikkyin Township', 'ThaPaKa'),
(205, 9, 'Tada-U township', 'TaTaOu'),
(206, 9, 'Taungtha Township', 'TaThaNa'),
(207, 9, 'Thazi Township', 'ThaSaNa'),
(208, 9, 'Wundwin Township', 'WaTaNa'),
(209, 9, 'Yemathin Township', 'YaMaTha'),
(210, 9, 'Tagaung Township', 'TaKaTa'),
(211, 9, 'Maymyot', 'MaMaNa'),
(212, 9, 'Dekhinathiri Township', 'DaKhaTha'),
(213, 9, 'Lewe Township', 'LaWaNa'),
(214, 9, 'Ottarathiri Township', 'OuTaTha'),
(215, 9, 'Popathiri Township', 'PaBaTha'),
(216, 9, 'Pyinmana Township', 'PaMaNa'),
(217, 9, 'Tatkon Township', 'TaKaNa'),
(218, 9, 'Zabuthiri Township', 'ZaBaTha'),
(219, 9, 'Zayarthiri Township', 'ZaYaTha'),
(220, 10, 'Billin Township', 'BaLaNa'),
(221, 10, 'ChaungZon Township', 'KhaSaNa'),
(222, 10, 'Khawzar Township', 'KhaZaNa'),
(223, 10, 'Kyaikmaraw Township', 'KaMaYa'),
(224, 10, 'Kyaikto Township', 'KaHtaNa'),
(225, 10, 'Lamine Myo Nal Khwal', 'LaMaNa'),
(226, 10, 'Mawlamyine Township', 'MaLaMa'),
(227, 10, 'Mudon Township', 'MaDaNa'),
(228, 10, 'Paung Township', 'PaMaNa'),
(229, 10, 'Thanbyuzayat Township', 'ThaHpaYa'),
(230, 10, 'Thaton Township', 'ThaHtaNa'),
(231, 10, 'Ye Township', 'YaMaNa'),
(232, 11, 'Ann Township', 'AhMaNa'),
(233, 11, 'Buthidaung Township', 'BaThaTa'),
(234, 11, 'Gwa Township', 'GaMaNa'),
(235, 11, 'Kyaukpyu Township', 'KaHpaNa'),
(236, 11, 'Kyauktaw Township', 'KaTaNa'),
(237, 11, 'Maei Myo Nal Khwal', 'MaAhTa'),
(238, 11, 'maungdaw township', 'maTaNa'),
(239, 11, 'Minbya Township', 'MaPaNa'),
(240, 11, 'Munaung Township', 'MaAhNa'),
(241, 11, 'Myauk-U Township', 'MaOuNa'),
(242, 11, 'Myebon Township', 'MaPaTa'),
(243, 11, 'Pauktaw Township', 'PaTaNa'),
(244, 11, 'Ponnagyun Township', 'PaNaTa'),
(245, 11, 'Ramree Township', 'YaBaNa'),
(246, 11, 'Rathedaung Township', 'YaThaTa'),
(247, 11, 'Sittwe Township', 'SaTaNa'),
(248, 11, 'Thandwe Township', 'ThaTaNa'),
(249, 11, 'Toungup Township', 'TaKaNa'),
(250, 11, 'Kyeintail Township', 'KaTaLa'),
(251, 11, 'Taungpyolatwae Township', 'TaPaWa'),
(252, 12, 'Ahlone Township', 'AhLaNa'),
(253, 12, 'Bahan Township', 'BaHaNa'),
(254, 12, 'Botahtaung Township', 'BaTaHta'),
(255, 12, 'Cocokyun Township', 'KaKaKa'),
(256, 12, 'Dagon Myothit(East)', 'DaGaYa'),
(257, 12, 'Dagon Myothit(North)', 'DaGaMa'),
(258, 12, 'Dagon Myothit(Seikkan)', 'DaGaSa'),
(259, 12, 'Dagon Myothit(South)', 'DaGaTa'),
(260, 12, 'Dagon Township', 'DaGaNa'),
(261, 12, 'Dala Township', 'DaLaNa'),
(262, 12, 'Dawbon Township', 'DaPaNa'),
(263, 12, 'Hlaingtharya Township', 'LaThaYa'),
(264, 12, 'Hlaing Township', 'LaMaNa'),
(265, 12, 'Hlegu Township', 'LaKaNa'),
(266, 12, 'Hmawbi Township', 'MaBaNa'),
(267, 12, 'Htantabin Township', 'HtaTaPa'),
(268, 12, 'Insein Township', 'AhSaNa'),
(269, 12, 'Kamayut Township', 'KaMaYa'),
(270, 12, 'Kawhmu Township', 'KaMaNa'),
(271, 12, 'Kayan Township', 'KhaYaNa'),
(272, 12, 'Kungyangon Township', 'KaKhaKa'),
(273, 12, 'Kyauktada Township', 'KaTaTa'),
(274, 12, 'Kyauktan Township', 'KaTaNa'),
(275, 12, 'Kyimyindaing Township', 'KaMaTa'),
(276, 12, 'Lanmadaw Township', 'LaMaTa'),
(277, 12, 'Latha Township', 'LaThaNa'),
(278, 12, 'Mayangone Township', 'MaYaKa'),
(279, 12, 'Mingalardon Township', 'MaGaDa'),
(280, 12, 'Mingalartaungnyunt Township', 'MaGaTa'),
(281, 12, 'North Oakkalapa Township', 'OuKaMa'),
(282, 12, 'Pabedan Township', 'PaBaTa'),
(283, 12, 'Pazundaung Township', 'PaZaTa'),
(284, 12, 'Sanchaung Township', 'SaKhaNa'),
(285, 12, 'Seikgyikanaungto Township', 'SaKaKha'),
(286, 12, 'Seikkan Township', 'SaKaNa'),
(287, 12, 'Shwepyithar Township', 'YaPaTha'),
(288, 12, 'South Oakkalapa Township', 'OuKaTa'),
(289, 12, 'Tada Township', 'TaTaHta'),
(290, 12, 'Taikkyi Township', 'TaKaNa'),
(291, 12, 'Tamwe Township', 'TaMaNa'),
(292, 12, 'Thaketa Township', 'ThaKaTa'),
(293, 12, 'Thanlyin Township', 'ThaLaNa'),
(294, 12, 'Thingangkuun Township', 'ThaGaKa'),
(295, 12, 'Thongwa Township', 'ThaKhaNa'),
(296, 12, 'Twantay Township', 'TaTaNa'),
(297, 12, 'Yankin Township', 'YaKaNa'),
(298, 12, 'Oakkan Township', 'OuKaNa'),
(299, 12, 'Chanmyathazi Township', 'KhaMaSa'),
(300, 13, 'Aik Chan Township', 'AhKhaNa'),
(301, 13, 'Chinshwehaw Sub', 'KhaYaHa'),
(302, 13, 'Hkun Mar Township', 'KhaMaNa'),
(303, 13, 'Ho Taung Township', 'HaTaNa'),
(304, 13, 'Hopang Township', 'HaPaNa'),
(305, 13, 'Hopong Township', 'HaPaTa'),
(306, 13, 'Hsawng Hpa Township', 'SaHpaNa'),
(307, 13, 'Hseni Township', 'ThaNaNa'),
(308, 13, 'HsiHseng Township', 'SaSaNa'),
(309, 13, 'Hsipaw Township', 'ThaPaNa'),
(310, 13, 'Ka Lawng Hpar Township', 'KaLaHpa'),
(311, 13, 'KaLaw Township', 'KaLaNa'),
(312, 13, 'Kali Township ', 'KaLaDa'),
(313, 13, 'Kawng Min Hsang Township', 'KaMaSa'),
(314, 13, 'Kengtung Township', 'KaTaNa'),
(315, 13, 'Konkyun Township', 'KaYaNa'),
(316, 13, 'Kyaing Taung Township', 'KaTaTa'),
(317, 13, 'Kunhing Township', 'KaHaNa'),
(318, 13, 'Kuniong Township', 'KaLaTa'),
(319, 13, 'Kutkai Township', 'KaKhaNa'),
(320, 13, 'Kyauktalonegyi Township', 'KaTaLa'),
(321, 13, 'Keythi Township', 'KaThaNa'),
(322, 13, 'LaiHka Township', 'LaKhaNa'),
(323, 13, 'Lashio Township', 'LaYaNa'),
(324, 13, 'Laukking Township', 'LaKaNa'),
(325, 13, 'Lin Haw Township', 'LaHaNa'),
(326, 13, 'Loilen Township', 'LaLaNa'),
(327, 13, 'Mabein Township', 'MaBaNa'),
(328, 13, 'Man Man Hseng Township', 'MaMaSa'),
(329, 13, 'Man Tun Township', 'MaTaNa'),
(330, 13, 'Mat Man Township', 'MaMaNa'),
(331, 13, 'ManTon Township', 'MaTaTa'),
(332, 13, 'Mawkmai Township', 'MaMaNa'),
(333, 13, 'Mong Hpen Township', 'MaHpaNa'),
(334, 13, 'Mong Ker Township', 'MaKaNa'),
(335, 13, 'Mong Pawk Township', 'MaPaNa'),
(336, 13, 'Monghpyak Township', 'MaHpaNa'),
(337, 13, 'Mong Hsat Township', 'MaSaNa'),
(338, 13, 'Monghsu Township', 'MaYaNa'),
(339, 13, 'Mong Kung Township', 'MaKaNa'),
(340, 13, 'MongKhet Township', 'MaKhaNa'),
(341, 13, 'Mongla Township', 'MaLaNa'),
(342, 13, 'Mongmao Township', 'MaMaTa'),
(343, 13, 'Mongmit Township', 'MaMaTa'),
(344, 13, 'MongNai Township', 'MaNaNa'),
(345, 13, 'Mongping Township', 'MaPaNa'),
(346, 13, 'Mongton Township', 'MaTaNa'),
(347, 13, 'Mongyai Township', 'MaYaTa'),
(348, 13, 'Mongyang Township', 'MaYaNa'),
(349, 13, 'Mongyawng Township', 'MaYana'),
(350, 13, 'Muse Township', 'MaSaTa'),
(351, 13, 'Nam Khan Wu Township', 'NaKhaWa'),
(352, 13, 'Nam Tit Township', 'NaTaNa'),
(353, 13, 'Namhkan Township', 'NaKhaNa'),
(354, 13, 'Namtu Township', 'NaMaTa'),
(355, 13, 'Nan Hpai Township', 'NaHpaNa'),
(356, 13, 'Nanhsan Toenship', 'NaSaNa'),
(357, 13, 'Nar Kawng Township', 'NaKaNa'),
(358, 13, 'Nar Wee Township', 'NaWaNa'),
(359, 13, 'Narphan Township', 'NaPhaNa'),
(360, 13, 'Nawng Hkit', 'NaKaNa'),
(361, 13, 'NayungshweTownship', 'NaKhaNa'),
(362, 13, 'Nawnghkio Township', 'NaKhaTa'),
(363, 13, 'Pang Hkam Township', 'PaKhaNa'),
(364, 13, 'PangYang Township', 'PaYaNa'),
(365, 13, 'Pangsang Township', 'PaSaNa'),
(366, 13, 'Pangwaun Township', 'PaWaNa'),
(367, 13, 'Pekon Township', 'HpaKaNa'),
(368, 13, 'Pindaya Township', 'PaTaYa'),
(369, 13, 'PinLaung Township', 'PaLaNa'),
(370, 13, 'Tachileik Township', 'TaKhaLa'),
(371, 13, 'Tangyan Township', 'TaYaNa'),
(372, 13, 'Taunggyi Township', 'TaKaNa'),
(373, 13, 'Yawng Lin Township', 'YaLaNa'),
(374, 13, 'Yetsauk Township', 'YaSaNa'),
(375, 13, 'Yin pang Township', 'YaHpaNa'),
(376, 13, 'Ywangan Township', 'YaNgaNa'),
(377, 13, 'Naungtayar Township', 'NaTaYa'),
(378, 13, 'Pinlon Township', 'PaLaTa'),
(379, 13, 'Kholan Township', 'KhaLaNa'),
(380, 13, 'ManHero Township', 'MaHaYa'),
(381, 13, 'PonparKyin Township', 'PaPaKa'),
(382, 13, 'Tarmoenyae Township', 'TaMaNa'),
(383, 13, 'MoeByae Township', 'MaBaTa'),
(384, 13, 'Mongngawt Township', 'MaNgaNa'),
(385, 13, 'Intaw Township', 'AhTaNa'),
(386, 13, 'Tarlay Township', 'TaLaNa'),
(387, 13, 'Amar Township', 'AhMaTa'),
(388, 13, 'Bogale Township', 'BaKaLa'),
(389, 13, 'Danubyu Township', 'DaNaHpa'),
(390, 14, 'Amar Township', 'AhMaTa'),
(391, 14, 'Bogale Township', 'BaKaLa'),
(392, 14, 'Danubyu Township', 'DaNaHpa'),
(393, 14, 'Dedaye Township', 'DaDaYa'),
(394, 14, 'Einme Township', 'AhMaNa'),
(395, 14, 'Haigyi Island Township', 'HaKaKa'),
(396, 14, 'Hinthada Township', 'HaThaTa'),
(397, 14, 'Ingapu Township', 'AhGaPa'),
(398, 14, 'Kangyidaunt Township', 'KaKaHta'),
(399, 14, 'Kyaiklat Township', 'KaLaNa'),
(400, 14, 'Kyangin Township', 'KaKhaNa'),
(401, 14, 'Kyaunggon Township', 'KaKaNa'),
(402, 14, 'Kyonpyaw Township', 'KaPaNa'),
(403, 14, 'Labutta Township', 'LaPaTa'),
(404, 14, 'Lemyethana Township', 'LaMaNa'),
(405, 14, 'Maubin Township', 'MaAhPa'),
(406, 14, 'Mawlamyinegyun Township', 'MaMaKa'),
(407, 14, 'Myanaung Township', 'MaAhNa'),
(408, 14, 'Myaungmya Township', 'MaMaNa'),
(409, 14, 'Ngapudaw Township', 'NgaPaTa'),
(410, 14, 'Ngathinechaung Township', 'NgaThaKha'),
(411, 14, 'Ngayotekaung Township', 'NgaYaKa'),
(412, 14, 'Ngwesaung Township', 'NgaSaNa'),
(413, 14, 'Ngwethoungyan Township', 'NgaThaYa'),
(414, 14, 'Nyaungdon Township', 'NyaTaNa'),
(415, 14, 'Pantanaw Township', 'PaTaNa'),
(416, 14, 'Pathein Township', 'PaThaNa'),
(417, 14, 'Pyapon Township', 'HpaPaNa'),
(418, 14, 'Pyinsalu Township', 'PaSaLa'),
(419, 14, 'Shwethoungyan Township', 'YaThaYa'),
(420, 14, 'Thabaung Township', 'ThaPaNa'),
(421, 14, 'Wakema Township', 'WaKhaMa'),
(422, 14, 'Yegyi Township', 'YaKaNa'),
(423, 14, 'Zalun Township', 'ZaLaNa');

-- --------------------------------------------------------

--
-- Table structure for table `cloth`
--

CREATE TABLE `cloth` (
  `cloth_id` int(11) NOT NULL,
  `clothing_type_id` int(11) NOT NULL,
  `cloth_name` varchar(30) NOT NULL,
  `cloth_img` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cloth`
--

INSERT INTO `cloth` (`cloth_id`, `clothing_type_id`, `cloth_name`, `cloth_img`) VALUES
(1, 1, 'Short Shirt', 'S_Shirt.png'),
(2, 1, 'Long Shirt', 'L_Shirt.png'),
(3, 1, 'Long Trouser', 'L_Trouser.png'),
(4, 1, 'Short Trouser', 'S_Trouser.png'),
(5, 1, 'Men Suit', 'Man_suit.png'),
(6, 1, 'Jacket', 'Man_Jacket.png'),
(7, 1, 'Men Coat', 'Man_Coat.png'),
(8, 1, 'Men Pajama', 'Man_Pajama.png'),
(9, 1, 'Men Hoddie', 'man_hoodie.png'),
(10, 2, 'Lady Long Shirt', 'L_Shirt_Lady.png'),
(11, 2, 'Lady Short Shirt', 'S_Shirt_Lady.png'),
(12, 2, 'Lady Long Trouser', 'L_Trouser_Lady.png'),
(13, 2, 'Lady Short Trouser', 'S_Trouser_Lady.png'),
(14, 2, 'Lady Blouse', 'Lady_Blouse.png'),
(15, 2, 'Lady Coat', 'Lady_Coat.png'),
(16, 2, 'Lady Dress', 'Lady_Dress.png'),
(17, 2, 'Lady Hoddie', 'lady_hoodie.png'),
(18, 2, 'Lady Jacket', 'Lady_Jacket.png'),
(19, 2, 'Lady One piece', 'Lady_One_piece.png'),
(20, 2, 'Lady Pajama', 'Lady_Pajama.png'),
(21, 2, 'Lady Skirt', 'Lady_Skirt.png'),
(22, 2, 'Lady Suit', 'Lady_Suit.png');

-- --------------------------------------------------------

--
-- Table structure for table `clothing_type`
--

CREATE TABLE `clothing_type` (
  `clothing_type_id` int(11) NOT NULL,
  `clothing_type` varchar(25) NOT NULL,
  `icon_name` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `clothing_type`
--

INSERT INTO `clothing_type` (`clothing_type_id`, `clothing_type`, `icon_name`) VALUES
(1, 'Men', 'USER'),
(2, 'Women', 'FEMALE');

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE `department` (
  `department_id` int(11) NOT NULL,
  `department_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`department_id`, `department_name`) VALUES
(1, 'Front Office'),
(2, 'Housekeeping'),
(3, 'Food and Beverage'),
(4, 'Kitchen'),
(5, 'Maintenance'),
(6, 'Accounting'),
(7, 'Human Resources'),
(8, 'Sales and Marketing');

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `employee_id` int(11) NOT NULL,
  `employee_name` varchar(50) NOT NULL,
  `nrc` varchar(50) NOT NULL,
  `gender` char(6) NOT NULL,
  `DOB` date NOT NULL,
  `phone_no` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `position_id` int(11) NOT NULL,
  `department_id` int(11) NOT NULL,
  `salary` int(11) NOT NULL,
  `hire_date` date NOT NULL,
  `leave_date` varchar(25) NOT NULL,
  `Image` varchar(50) NOT NULL,
  `Status` varchar(50) NOT NULL,
  `Address` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`employee_id`, `employee_name`, `nrc`, `gender`, `DOB`, `phone_no`, `email`, `position_id`, `department_id`, `salary`, `hire_date`, `leave_date`, `Image`, `Status`, `Address`) VALUES
(1, 'John', '12/ThaKaTa(N)123455', 'Male', '2000-07-22', '0982377383', 'john@gmail.com', 1, 1, 600000, '2025-07-22', '', '', '', ''),
(5, 'Thi Ha', '12/ThaKaTa(N)123455', 'Male', '2001-06-01', '09823773333', 'mthi@gmail.com', 1, 1, 6000000, '2025-07-27', '', '', 'Active', '123/Thadaryone(1)street,Tharketa,Yangon'),
(6, 'Hh', '1/AhGaYa(N)123456', 'Male', '2003-08-08', '09171727722', 'h@gmail.com', 1, 1, 1000, '2024-08-07', 'xxxx-xx-xx', 'check out.png', 'Active', 'Yangon'),
(7, 'Aye', '1/AhGaYa(N)123456 ', '', '2004-08-18', '09474123456', 'ye@gmail.com', 1, 1, 120000, '2015-08-06', 'xxxx-xx-xx', 'image-removebg-preview (3).png', 'Active', 'Yangon');

-- --------------------------------------------------------

--
-- Table structure for table `food`
--

CREATE TABLE `food` (
  `food_id` int(11) NOT NULL,
  `food_type_id` int(11) NOT NULL,
  `food_name` varchar(50) NOT NULL,
  `food_price` int(11) NOT NULL,
  `food_image` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL,
  `meal_course_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `food`
--

INSERT INTO `food` (`food_id`, `food_type_id`, `food_name`, `food_price`, `food_image`, `status`, `meal_course_id`) VALUES
(1, 1, 'Burmese Fried Rice', 5000, 'biryani.png', 'available', 1),
(2, 1, 'Coconut Noodle', 5000, 'Burmese Fried Rice.png', 'unavailable', 1),
(3, 1, 'Nan Pyar Thoke', 5000, 'hotelLogin.jpg', 'available', 1),
(4, 2, 'Pauk Si(Chicken)', 3000, 'image', 'available', 3),
(5, 3, 'Grilled Pork with Sticky Rice (Moo Ping)', 8000, 'image', 'available', 1),
(6, 1, 'Shwe Kyi', 4000, 'image', 'available', 3),
(7, 1, 'Thar Ku', 4000, 'image', 'available', 3),
(8, 5, 'Gimbap', 6500, '', '', 1),
(9, 1, 'Mohinga', 4200, '', '', 1),
(10, 1, 'Nan Gyi Thoke', 4800, 'image', 'available', 1),
(11, 1, 'Palata', 3800, '', '', 1),
(12, 2, 'Dim sum', 9800, '', '', 1),
(13, 2, 'Dumplings', 8000, 'image', 'unavailable', 1),
(14, 2, 'Rice Noodles', 5600, '', '', 1),
(15, 3, 'Joke', 4500, 'image', 'available', 1),
(16, 3, 'Khao Neow Moo Ping', 10200, '', '', 1),
(17, 3, 'Nam Tao Hoo', 5800, '', '', 1),
(18, 4, 'Shan Noodles', 5200, '', '', 1),
(19, 4, 'Warm Tofu', 6200, '', '', 1),
(20, 4, 'Shan-style Rice', 5700, '', '', 1),
(21, 5, 'Kimchi Fried Rice', 7300, '', '', 1),
(22, 5, 'Korean Pancakes', 7800, '', '', 1),
(23, 5, 'Kimbap', 6800, '', '', 1),
(24, 1, 'Burmese Coconut Rice', 5500, 'image', 'unavailable', 2),
(25, 1, 'Burmese Biryani', 6800, '', '', 2),
(26, 1, 'Laphet Thohk', 5200, 'Copilot_20250624_000609.png', 'available', 2),
(27, 2, 'Peking Duck', 9600, 'image', 'available', 2),
(28, 2, 'Kung Pao Chicken', 6200, '', '', 2),
(29, 2, 'Hot Pot', 12000, '', '', 2),
(30, 3, 'Pad Thai Noodles', 6000, 'image', 'available', 2),
(31, 3, 'Tom Yum Goong', 5800, 'Screenshot 2025-07-23 213208.png', 'available', 2),
(32, 3, 'Chicken Satay', 7800, '', '', 2),
(33, 4, 'Htamin Jin', 6700, '', '', 2),
(34, 4, 'Meat Ball Noodles', 7000, '', '', 2),
(35, 4, 'Mee Shay', 5800, '', '', 2),
(36, 5, 'Bulgogi', 9800, '', '', 2),
(37, 5, 'Tteokbooki', 7100, '', '', 2),
(38, 5, 'Jajangmyeon', 6800, '', '', 2),
(39, 1, 'Htoe Mont', 6000, 'image', 'unavailable', 3),
(40, 1, 'Shwe Yin Aye', 5800, '', '', 3),
(41, 1, 'Kyauk Kyaw', 5200, '', '', 3),
(42, 2, 'Egg Tart', 6200, 'Copilot_20250624_000609.png', 'available', 3),
(43, 2, 'Mooncakes', 6500, '', '', 3),
(44, 2, 'Mango Pomelo Sago', 6500, '', '', 3),
(45, 3, 'Mango Sticky Rice', 6500, 'food.jpg', 'available', 3),
(46, 3, 'Streamed Coconut Milk Pudding', 6000, '', '', 3),
(47, 3, 'Crispy Ruby', 6700, '', '', 3),
(48, 4, 'Kheer', 5300, '', '', 3),
(49, 4, 'Gulab Jamun', 6000, '', '', 3),
(50, 4, 'Jelly Crystals', 6300, '', '', 3),
(51, 5, 'Rice Cakes', 6500, '', '', 3),
(52, 5, 'Fish Bread', 5500, '', '', 3),
(53, 5, 'Bingsu', 7300, '', '', 3),
(54, 1, 'Lahpet Yay Cho', 3800, '', '', 4),
(55, 1, 'Fruit Juices', 6000, '', '', 4),
(56, 1, 'Soft Drinks', 5600, '', '', 4),
(57, 2, 'Bubble Tea', 7800, '', '', 4),
(58, 2, 'Black Tea', 6800, '', '', 4),
(59, 2, 'Coconut Milk', 7000, '', '', 4),
(60, 3, 'Thai Milk Tea', 7600, '', '', 4),
(61, 3, 'Cha Yen', 6800, '', '', 4),
(62, 3, 'Roselle Juice', 6200, '', '', 4),
(63, 4, 'Plain Green Tea', 5500, '', '', 4),
(64, 4, 'Milk Tea', 6000, '', '', 4),
(65, 4, 'Herbal Teas', 6200, '', '', 4),
(66, 5, 'Banana Milk', 6800, '', '', 4),
(67, 5, 'Soju', 7700, '', '', 4),
(68, 5, 'Bacchus-d', 5100, '', '', 4),
(69, 2, 'Kyat Myat Lone', 120000, 'close-eye.png', 'available', 2),
(70, 4, 'Lu Shal', 90, 'food.jpg', 'available', 1),
(71, 3, 'Sate Myat Lone', 200, 'food.jpg', 'available', 3),
(72, 1, 'U', 3000, 'food.jpg', 'unavailable', 1);

-- --------------------------------------------------------

--
-- Table structure for table `food_order`
--

CREATE TABLE `food_order` (
  `food_order_id` int(11) NOT NULL,
  `booking_detail_id` int(11) NOT NULL,
  `food_order_date` date NOT NULL,
  `emp_id` int(11) NOT NULL,
  `food_order_status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `food_order`
--

INSERT INTO `food_order` (`food_order_id`, `booking_detail_id`, `food_order_date`, `emp_id`, `food_order_status`) VALUES
(1, 11, '2025-07-22', 1, 'Delivered'),
(2, 11, '2025-07-22', 1, 'Delivered'),
(3, 11, '2025-07-22', 1, 'Delivered'),
(4, 11, '2025-07-22', 1, 'Delivered'),
(5, 11, '2025-07-23', 1, 'Delivered'),
(6, 11, '2025-07-23', 1, 'Delivered'),
(7, 11, '2025-07-24', 1, 'Delivered'),
(8, 11, '2025-07-24', 1, 'Delivered'),
(9, 11, '2025-07-24', 1, 'Delivered'),
(10, 11, '2025-07-24', 1, 'Delivered'),
(11, 11, '2025-07-24', 1, '0'),
(12, 11, '2025-07-24', 1, '0'),
(13, 11, '2025-07-24', 1, '0'),
(14, 12, '2025-07-25', 1, 'Delivered'),
(15, 11, '2025-07-27', 1, 'Delivered'),
(16, 22, '2025-07-27', 1, 'Delivered'),
(17, 17, '2025-07-30', 1, 'Delivered'),
(24, 40, '2025-08-01', 1, 'Delivered'),
(25, 17, '2025-08-01', 1, 'Preparing'),
(26, 41, '2025-08-01', 1, 'Delivered');

-- --------------------------------------------------------

--
-- Table structure for table `food_order_detail`
--

CREATE TABLE `food_order_detail` (
  `food_order_detail_id` int(11) NOT NULL,
  `food_order_id` int(11) NOT NULL,
  `food_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `food_order_detail`
--

INSERT INTO `food_order_detail` (`food_order_detail_id`, `food_order_id`, `food_id`, `quantity`, `price`) VALUES
(1, 1, 24, 10, 5500),
(2, 1, 26, 1, 5200),
(3, 1, 28, 8, 6200),
(4, 2, 3, 5, 2000),
(5, 2, 24, 8, 5500),
(6, 2, 25, 1, 6800),
(7, 3, 4, 1, 3000),
(8, 3, 6, 1, 4000),
(9, 3, 41, 1, 5200),
(10, 4, 4, 1, 3000),
(11, 4, 24, 6, 5500),
(12, 5, 66, 10, 6800),
(13, 6, 69, 5, 120000),
(14, 7, 70, 1, 90),
(15, 8, 70, 1, 90),
(16, 9, 3, 1, 5000),
(17, 10, 3, 1, 5000),
(18, 11, 3, 1, 5000),
(19, 12, 70, 1, 90),
(20, 13, 3, 1, 5000),
(21, 14, 70, 22, 90),
(22, 15, 70, 1, 90),
(23, 16, 15, 1, 4500),
(24, 17, 1, 1, 5000),
(25, 17, 3, 1, 5000),
(34, 24, 1, 1, 5000),
(35, 25, 70, 1, 90),
(36, 26, 70, 12, 90);

-- --------------------------------------------------------

--
-- Table structure for table `food_type`
--

CREATE TABLE `food_type` (
  `food_type_id` int(11) NOT NULL,
  `food_type_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `food_type`
--

INSERT INTO `food_type` (`food_type_id`, `food_type_name`) VALUES
(1, 'Burma'),
(2, 'Chinese'),
(3, 'Thai'),
(4, 'Shan'),
(5, 'Korea'),
(7, 'India');

-- --------------------------------------------------------

--
-- Table structure for table `guest`
--

CREATE TABLE `guest` (
  `guest_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `nrc` varchar(50) NOT NULL,
  `ph_no` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `guest`
--

INSERT INTO `guest` (`guest_id`, `name`, `nrc`, `ph_no`, `email`) VALUES
(1, 'Xiao Yang', '7/AhPhaNa(N)13536912', '09-123456789', 'xiaoyang@gmail.com'),
(100005, 'Kyaw Kyaw', '1/DaHpYa(N)239033', '098747484', 'kyaw@gmail.com'),
(100006, 'Min Min', '12/ThaKaTa(N)123456', '0987373333', 'min@gmail.com'),
(100007, 'Min Myat', '5/BaMaNa(N)333455', '0933448393', 'min@gmail.com'),
(100008, 'Shwe Yee', '6/KaLaAh(E)123123', '09873833332', 'sh@gmail.com'),
(100009, 'Moe Moe ', '2/HpaYaSa(N)123456', '09838474748', 'moe@gmail.com'),
(100010, 'Gu Gu', '1/BaMaNa(N)123444', '0978786778', 'gu@gmail.com'),
(100011, 'Moe Lay', '1/DaHpYa(N)222890', '09888383833', 'moe@gmail.com'),
(100012, 'hi', '2/DaMaSa(N)787890', '0988877878', 'h@gmail.com'),
(100013, 'Yuki', '1/KhaHpaNa(N)123456', '0988737338', 'yu@gmail.com'),
(100014, 'Yo Yo', '2/baLaKha(N)123444', '09838384994', 'y@gmail.com'),
(100015, 'U Shee', '1/DaHpYa(E)123334', '0983838333', 'u@gmail.com'),
(100016, 'jone', '3/LaBaNa(N)467743', '09557644344', 'jone@gmail.com'),
(100017, 'Tu Tu', '2/HpaYaSa(P)123789', '09838383833', 't@gmail.com'),
(100018, 'Yu Yu', '1/KhaHpaNa(E)123898', '09838383939', 'yu@gmail.com'),
(100019, 'Yoe ', '4/MaTaPa(E)4535353', '0937378333', 'y@gamil.com'),
(100020, 'Hugo', '2/HpaSaNa(N)123333', '0973737322', 'h@gmail.com'),
(100021, 'Nadi', '1/HpaKaNa(P)234567', '09828288222', 'h@gmail.com'),
(100022, 'Uii', '4/MaTaPa(N)123445', '0933832222', 'h@gamil.com'),
(100023, 'Myag', '3/LaBaNa(P)123333', '0939393444', 'm@gmail.com'),
(100024, 'jiji', '3/BaAhNa(N)688727', '09487626272', 'jiji@gmail.com'),
(100025, 'Tester', '12/ThaKaTa(N)200010', '09838383333', 'test@gmail.com'),
(100026, 'Main Main', '1/KhaHpaNa(N)12345', '092348449', 'm@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `hall`
--

CREATE TABLE `hall` (
  `hall_id` int(11) NOT NULL,
  `hall_type_id` int(11) NOT NULL,
  `image` varchar(30) NOT NULL,
  `price` int(11) NOT NULL,
  `CapacityID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hall`
--

INSERT INTO `hall` (`hall_id`, `hall_type_id`, `image`, `price`, `CapacityID`) VALUES
(10, 1, 'single.png', 2000, 4),
(11, 1, 'single.png', 3000, 4),
(12, 2, 'single.png', 2500, 2);

-- --------------------------------------------------------

--
-- Table structure for table `hall_book`
--

CREATE TABLE `hall_book` (
  `Hall_Booked_id` int(11) NOT NULL,
  `guest_id` int(11) NOT NULL,
  `emp_id` int(11) NOT NULL,
  `book_Date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hall_book`
--

INSERT INTO `hall_book` (`Hall_Booked_id`, `guest_id`, `emp_id`, `book_Date`) VALUES
(19, 100023, 1, '2025-07-25');

-- --------------------------------------------------------

--
-- Table structure for table `hall_booked_detail`
--

CREATE TABLE `hall_booked_detail` (
  `Hall_Booked_Detail_id` int(11) NOT NULL,
  `Hall_Booked_id` int(11) NOT NULL,
  `hall_id` int(11) NOT NULL,
  `booking_status` varchar(20) NOT NULL,
  `payment_status_id` int(11) NOT NULL,
  `want_date` date NOT NULL,
  `start_time` time NOT NULL,
  `End_time` time NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hall_booked_detail`
--

INSERT INTO `hall_booked_detail` (`Hall_Booked_Detail_id`, `Hall_Booked_id`, `hall_id`, `booking_status`, `payment_status_id`, `want_date`, `start_time`, `End_time`, `price`) VALUES
(28, 19, 10, 'booking', 1, '2025-08-28', '10:00:00', '13:00:00', 2000);

-- --------------------------------------------------------

--
-- Table structure for table `hall_type`
--

CREATE TABLE `hall_type` (
  `hall_type_id` int(11) NOT NULL,
  `hall_type` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hall_type`
--

INSERT INTO `hall_type` (`hall_type_id`, `hall_type`) VALUES
(1, 'Wedding Hall'),
(2, 'Conference hall'),
(3, 'Meeting Hall');

-- --------------------------------------------------------

--
-- Table structure for table `laundary_service`
--

CREATE TABLE `laundary_service` (
  `service_id` int(11) NOT NULL,
  `service_type_id` int(11) NOT NULL,
  `cloth_id` int(11) NOT NULL,
  `base_price` int(11) NOT NULL,
  `status` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `laundary_service`
--

INSERT INTO `laundary_service` (`service_id`, `service_type_id`, `cloth_id`, `base_price`, `status`) VALUES
(1, 1, 1, 1000, '1'),
(2, 1, 2, 120, '1'),
(3, 1, 3, 180, '1'),
(4, 1, 4, 130, '1'),
(5, 1, 5, 300, '1'),
(6, 1, 6, 250, '1'),
(7, 1, 7, 300, '1'),
(8, 1, 8, 150, '1'),
(9, 1, 9, 150, '1'),
(10, 1, 10, 110, '1'),
(11, 1, 11, 100, '1'),
(12, 1, 12, 120, '1'),
(13, 1, 13, 100, '1'),
(14, 1, 14, 120, '1'),
(15, 1, 15, 250, '1'),
(16, 1, 16, 200, '1'),
(17, 1, 17, 200, '1'),
(18, 1, 18, 250, '1'),
(19, 1, 19, 200, '1'),
(20, 1, 20, 130, '1'),
(21, 1, 21, 100, '1'),
(22, 1, 22, 250, '1'),
(23, 2, 1, 300, '1'),
(24, 2, 2, 400, '1'),
(25, 2, 3, 400, '1'),
(26, 2, 4, 300, '1'),
(27, 2, 5, 500, '1'),
(28, 2, 6, 400, '1'),
(29, 2, 7, 500, '1'),
(30, 2, 8, 200, '1'),
(31, 2, 9, 400, '1'),
(32, 2, 10, 300, '1'),
(33, 2, 11, 200, '1'),
(34, 2, 12, 300, '1'),
(35, 2, 13, 100, '1'),
(36, 2, 14, 300, '1'),
(37, 2, 15, 400, '1'),
(38, 2, 16, 500, '1'),
(39, 2, 17, 400, '1'),
(40, 2, 18, 500, '1'),
(41, 2, 19, 300, '1'),
(42, 2, 20, 200, '1'),
(43, 2, 21, 300, '1'),
(44, 2, 22, 500, '1');

-- --------------------------------------------------------

--
-- Table structure for table `laundary_service_order`
--

CREATE TABLE `laundary_service_order` (
  `order_id` int(11) NOT NULL,
  `booking_detail_id` int(11) NOT NULL,
  `order_date` date NOT NULL,
  `delivery_date` date NOT NULL,
  `emp_id` int(11) NOT NULL,
  `Order_Status_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `laundary_service_order`
--

INSERT INTO `laundary_service_order` (`order_id`, `booking_detail_id`, `order_date`, `delivery_date`, `emp_id`, `Order_Status_id`) VALUES
(1, 11, '2025-07-24', '2025-07-25', 1, 1),
(2, 11, '2025-07-24', '2025-07-24', 1, 3),
(3, 11, '2025-07-24', '2025-07-29', 1, 1),
(4, 11, '2025-07-24', '2025-07-24', 1, 2),
(5, 11, '2025-07-24', '2025-07-29', 1, 1),
(6, 11, '2025-07-24', '2025-07-28', 1, 1),
(7, 11, '2025-07-24', '2025-07-25', 1, 1),
(8, 11, '2025-07-24', '2025-07-27', 1, 1),
(9, 11, '2025-07-24', '2025-07-27', 1, 1),
(10, 11, '2025-07-26', '2025-07-28', 1, 1),
(11, 22, '2025-07-27', '2025-07-27', 1, 1),
(12, 16, '2025-07-30', '2025-07-31', 1, 3),
(13, 40, '2025-08-01', '2025-08-02', 1, 2),
(14, 40, '2025-08-01', '2025-08-02', 1, 3),
(15, 40, '2025-08-01', '2025-08-02', 1, 3),
(16, 40, '2025-08-01', '2025-08-02', 1, 3),
(17, 40, '2025-08-01', '2025-08-02', 1, 3),
(18, 41, '2025-08-01', '2025-08-04', 1, 2),
(19, 41, '2025-08-01', '2025-08-04', 1, 2),
(20, 41, '2025-08-01', '2025-08-04', 1, 3),
(21, 41, '2025-08-02', '2025-08-03', 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `laundary_service_order_detail`
--

CREATE TABLE `laundary_service_order_detail` (
  `order_detail_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `laundary_service_order_detail`
--

INSERT INTO `laundary_service_order_detail` (`order_detail_id`, `order_id`, `service_id`, `quantity`, `price`) VALUES
(17, 1, 1, 11, 100),
(18, 2, 3, 9, 150),
(19, 3, 3, 4, 150),
(20, 4, 1, 9, 100),
(21, 5, 2, 2, 120),
(22, 6, 1, 9, 100),
(23, 7, 2, 9, 120),
(24, 8, 3, 1, 150),
(25, 9, 5, 1, 300),
(26, 10, 20, 1, 130),
(27, 11, 2, 9, 120),
(28, 12, 1, 8, 1000),
(33, 13, 3, 1, 180),
(34, 14, 3, 3, 180),
(35, 15, 1, 7, 1000),
(36, 16, 1, 8, 1000),
(37, 17, 2, 67, 120),
(38, 18, 1, 90, 1000),
(39, 19, 1, 9, 1000),
(40, 20, 1, 6, 1000),
(41, 21, 3, 6, 180);

-- --------------------------------------------------------

--
-- Table structure for table `laundary_service_type`
--

CREATE TABLE `laundary_service_type` (
  `service_type_id` int(11) NOT NULL,
  `service_type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `laundary_service_type`
--

INSERT INTO `laundary_service_type` (`service_type_id`, `service_type`) VALUES
(1, 'Washing'),
(2, 'Washing & Ironing'),
(3, 'jjj');

-- --------------------------------------------------------

--
-- Table structure for table `meal_course`
--

CREATE TABLE `meal_course` (
  `meal_course_id` int(11) NOT NULL,
  `meal_course_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `meal_course`
--

INSERT INTO `meal_course` (`meal_course_id`, `meal_course_name`) VALUES
(1, 'Breakfast '),
(2, 'Main Course '),
(3, 'Dessert'),
(4, 'Drink');

-- --------------------------------------------------------

--
-- Table structure for table `order_status`
--

CREATE TABLE `order_status` (
  `Order_Status_id` int(11) NOT NULL,
  `Order_Status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_status`
--

INSERT INTO `order_status` (`Order_Status_id`, `Order_Status`) VALUES
(1, 'At Laundary'),
(2, 'Ready For Delivery'),
(3, 'Delivered');

-- --------------------------------------------------------

--
-- Table structure for table `payment_status`
--

CREATE TABLE `payment_status` (
  `payment_status_id` int(11) NOT NULL,
  `payment_status` varchar(30) NOT NULL,
  `rate` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment_status`
--

INSERT INTO `payment_status` (`payment_status_id`, `payment_status`, `rate`) VALUES
(1, 'Deposit (20%)', 0.20),
(5, 'Full', 1.00);

-- --------------------------------------------------------

--
-- Table structure for table `position`
--

CREATE TABLE `position` (
  `position_id` int(11) NOT NULL,
  `position_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `position`
--

INSERT INTO `position` (`position_id`, `position_name`) VALUES
(1, 'Staff'),
(2, 'Manager');

-- --------------------------------------------------------

--
-- Table structure for table `region_state`
--

CREATE TABLE `region_state` (
  `region_state_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `region_state`
--

INSERT INTO `region_state` (`region_state_id`, `name`) VALUES
(1, 'Kachin'),
(2, 'Kayah'),
(3, 'Kayin'),
(4, 'Chin'),
(5, 'Sagaing'),
(6, 'Thathintharyi'),
(7, 'Bago'),
(8, 'Magway'),
(9, 'Mandalay'),
(10, 'Mon'),
(11, 'Rakhine'),
(12, 'Yangon'),
(13, 'Shan'),
(14, 'Ayarwaddy');

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

CREATE TABLE `room` (
  `room_id` int(11) NOT NULL,
  `room_type_id` int(11) NOT NULL,
  `floor_no` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`room_id`, `room_type_id`, `floor_no`, `status`) VALUES
(1, 1, 1, 1),
(2, 1, 1, 1),
(3, 2, 2, 0),
(4, 2, 2, 1),
(5, 3, 3, 0),
(6, 3, 3, 1),
(7, 4, 4, 0),
(8, 4, 4, 1),
(9, 5, 5, 0),
(10, 5, 5, 0),
(11, 6, 6, 0),
(12, 6, 6, 0);

-- --------------------------------------------------------

--
-- Table structure for table `room_type`
--

CREATE TABLE `room_type` (
  `room_type_id` int(11) NOT NULL,
  `room_type` varchar(50) NOT NULL,
  `capacity` int(11) NOT NULL,
  `image_path` varchar(50) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `room_type`
--

INSERT INTO `room_type` (`room_type_id`, `room_type`, `capacity`, `image_path`, `price`) VALUES
(1, 'Single', 1, 'single.png', 100),
(2, 'Double', 2, 'double.png', 200),
(3, 'Triple', 3, 'triple.png', 300),
(4, 'Twin', 2, 'twin.png', 200),
(5, 'Deluxe', 2, 'deluxe.png', 250),
(6, 'President', 6, 'president.png', 400),
(7, 'exanok', 999, 'double.png', 999);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `employee_id` int(11) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_role` varchar(30) NOT NULL,
  `status` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `employee_id`, `user_name`, `password`, `user_role`, `status`) VALUES
(1, 1, 'Khant Zaw Win', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', 'Admin', 'Active'),
(2, 5, 'thi123', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Staff', 'Active'),
(3, 6, 'Hh', '8f3181c6aab0419d6bb65559ae0fbef87ac45eb53c532fe3bf87ac46d99b7c9d', 'Staff', 'InActive'),
(4, 7, 'Aye', 'a0d00e5e8bbb9600a62d388a4be4ec3cd211dd497d24fcc6b22d4832a7636f77', 'Staff', 'Active');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`booking_id`),
  ADD KEY `guest_id` (`guest_id`),
  ADD KEY `user_id` (`emp_id`);

--
-- Indexes for table `booking_detail`
--
ALTER TABLE `booking_detail`
  ADD PRIMARY KEY (`booking_detail_id`),
  ADD KEY `booking_id` (`booking_id`),
  ADD KEY `room_id` (`room_id`),
  ADD KEY `payment_status_id` (`payment_status_id`);

--
-- Indexes for table `capacity`
--
ALTER TABLE `capacity`
  ADD PRIMARY KEY (`CapacityID`);

--
-- Indexes for table `city`
--
ALTER TABLE `city`
  ADD PRIMARY KEY (`city_id`),
  ADD KEY `region_state_id` (`region_state_id`);

--
-- Indexes for table `cloth`
--
ALTER TABLE `cloth`
  ADD PRIMARY KEY (`cloth_id`),
  ADD KEY `clothing_type_id` (`clothing_type_id`);

--
-- Indexes for table `clothing_type`
--
ALTER TABLE `clothing_type`
  ADD PRIMARY KEY (`clothing_type_id`);

--
-- Indexes for table `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`department_id`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`employee_id`),
  ADD KEY `position_id` (`position_id`),
  ADD KEY `department_id` (`department_id`);

--
-- Indexes for table `food`
--
ALTER TABLE `food`
  ADD PRIMARY KEY (`food_id`),
  ADD KEY `food_type_id` (`food_type_id`),
  ADD KEY `fk_food_meal_course` (`meal_course_id`);

--
-- Indexes for table `food_order`
--
ALTER TABLE `food_order`
  ADD PRIMARY KEY (`food_order_id`),
  ADD KEY `booking_detail_id` (`booking_detail_id`),
  ADD KEY `emp_id` (`emp_id`);

--
-- Indexes for table `food_order_detail`
--
ALTER TABLE `food_order_detail`
  ADD PRIMARY KEY (`food_order_detail_id`),
  ADD KEY `food_id` (`food_id`),
  ADD KEY `food_order_id` (`food_order_id`);

--
-- Indexes for table `food_type`
--
ALTER TABLE `food_type`
  ADD PRIMARY KEY (`food_type_id`);

--
-- Indexes for table `guest`
--
ALTER TABLE `guest`
  ADD PRIMARY KEY (`guest_id`);

--
-- Indexes for table `hall`
--
ALTER TABLE `hall`
  ADD PRIMARY KEY (`hall_id`),
  ADD KEY `hall_type_id` (`hall_type_id`),
  ADD KEY `CapacityID` (`CapacityID`);

--
-- Indexes for table `hall_book`
--
ALTER TABLE `hall_book`
  ADD PRIMARY KEY (`Hall_Booked_id`),
  ADD KEY `guest_id` (`guest_id`),
  ADD KEY `emp_id` (`emp_id`);

--
-- Indexes for table `hall_booked_detail`
--
ALTER TABLE `hall_booked_detail`
  ADD PRIMARY KEY (`Hall_Booked_Detail_id`),
  ADD KEY `Hall_Booked_id` (`Hall_Booked_id`),
  ADD KEY `hall_id` (`hall_id`),
  ADD KEY `payment_status_id` (`payment_status_id`);

--
-- Indexes for table `hall_type`
--
ALTER TABLE `hall_type`
  ADD PRIMARY KEY (`hall_type_id`);

--
-- Indexes for table `laundary_service`
--
ALTER TABLE `laundary_service`
  ADD PRIMARY KEY (`service_id`),
  ADD KEY `service_type_id` (`service_type_id`),
  ADD KEY `cloth_id` (`cloth_id`);

--
-- Indexes for table `laundary_service_order`
--
ALTER TABLE `laundary_service_order`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `booking_detail_id` (`booking_detail_id`),
  ADD KEY `emp_id` (`emp_id`),
  ADD KEY `Order_Status_id` (`Order_Status_id`);

--
-- Indexes for table `laundary_service_order_detail`
--
ALTER TABLE `laundary_service_order_detail`
  ADD PRIMARY KEY (`order_detail_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `service_id` (`service_id`);

--
-- Indexes for table `laundary_service_type`
--
ALTER TABLE `laundary_service_type`
  ADD PRIMARY KEY (`service_type_id`);

--
-- Indexes for table `meal_course`
--
ALTER TABLE `meal_course`
  ADD PRIMARY KEY (`meal_course_id`);

--
-- Indexes for table `order_status`
--
ALTER TABLE `order_status`
  ADD PRIMARY KEY (`Order_Status_id`);

--
-- Indexes for table `payment_status`
--
ALTER TABLE `payment_status`
  ADD PRIMARY KEY (`payment_status_id`);

--
-- Indexes for table `position`
--
ALTER TABLE `position`
  ADD PRIMARY KEY (`position_id`);

--
-- Indexes for table `region_state`
--
ALTER TABLE `region_state`
  ADD PRIMARY KEY (`region_state_id`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`room_id`),
  ADD KEY `room_type_id` (`room_type_id`);

--
-- Indexes for table `room_type`
--
ALTER TABLE `room_type`
  ADD PRIMARY KEY (`room_type_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `emp_id` (`employee_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `booking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `booking_detail`
--
ALTER TABLE `booking_detail`
  MODIFY `booking_detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `capacity`
--
ALTER TABLE `capacity`
  MODIFY `CapacityID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `city`
--
ALTER TABLE `city`
  MODIFY `city_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=424;

--
-- AUTO_INCREMENT for table `cloth`
--
ALTER TABLE `cloth`
  MODIFY `cloth_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `clothing_type`
--
ALTER TABLE `clothing_type`
  MODIFY `clothing_type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `department`
--
ALTER TABLE `department`
  MODIFY `department_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `employee`
--
ALTER TABLE `employee`
  MODIFY `employee_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `food`
--
ALTER TABLE `food`
  MODIFY `food_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- AUTO_INCREMENT for table `food_order`
--
ALTER TABLE `food_order`
  MODIFY `food_order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `food_order_detail`
--
ALTER TABLE `food_order_detail`
  MODIFY `food_order_detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `food_type`
--
ALTER TABLE `food_type`
  MODIFY `food_type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `guest`
--
ALTER TABLE `guest`
  MODIFY `guest_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100027;

--
-- AUTO_INCREMENT for table `hall`
--
ALTER TABLE `hall`
  MODIFY `hall_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `hall_book`
--
ALTER TABLE `hall_book`
  MODIFY `Hall_Booked_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `hall_booked_detail`
--
ALTER TABLE `hall_booked_detail`
  MODIFY `Hall_Booked_Detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `hall_type`
--
ALTER TABLE `hall_type`
  MODIFY `hall_type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `laundary_service`
--
ALTER TABLE `laundary_service`
  MODIFY `service_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;

--
-- AUTO_INCREMENT for table `laundary_service_order`
--
ALTER TABLE `laundary_service_order`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `laundary_service_order_detail`
--
ALTER TABLE `laundary_service_order_detail`
  MODIFY `order_detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT for table `laundary_service_type`
--
ALTER TABLE `laundary_service_type`
  MODIFY `service_type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `meal_course`
--
ALTER TABLE `meal_course`
  MODIFY `meal_course_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `order_status`
--
ALTER TABLE `order_status`
  MODIFY `Order_Status_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `payment_status`
--
ALTER TABLE `payment_status`
  MODIFY `payment_status_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `position`
--
ALTER TABLE `position`
  MODIFY `position_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `region_state`
--
ALTER TABLE `region_state`
  MODIFY `region_state_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `room`
--
ALTER TABLE `room`
  MODIFY `room_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `room_type`
--
ALTER TABLE `room_type`
  MODIFY `room_type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`guest_id`) REFERENCES `guest` (`guest_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`employee_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `booking_detail`
--
ALTER TABLE `booking_detail`
  ADD CONSTRAINT `booking_detail_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `booking_detail_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `booking_detail_ibfk_3` FOREIGN KEY (`payment_status_id`) REFERENCES `payment_status` (`payment_status_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `city`
--
ALTER TABLE `city`
  ADD CONSTRAINT `city_ibfk_1` FOREIGN KEY (`region_state_id`) REFERENCES `region_state` (`region_state_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `cloth`
--
ALTER TABLE `cloth`
  ADD CONSTRAINT `cloth_ibfk_1` FOREIGN KEY (`clothing_type_id`) REFERENCES `clothing_type` (`clothing_type_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `employee`
--
ALTER TABLE `employee`
  ADD CONSTRAINT `employee_ibfk_2` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `employee_ibfk_3` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `food`
--
ALTER TABLE `food`
  ADD CONSTRAINT `food_ibfk_1` FOREIGN KEY (`food_type_id`) REFERENCES `food_type` (`food_type_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `food_ibfk_2` FOREIGN KEY (`meal_course_id`) REFERENCES `meal_course` (`meal_course_id`);

--
-- Constraints for table `food_order`
--
ALTER TABLE `food_order`
  ADD CONSTRAINT `emp_id` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`employee_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `food_order_ibfk_1` FOREIGN KEY (`booking_detail_id`) REFERENCES `booking_detail` (`booking_detail_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `food_order_detail`
--
ALTER TABLE `food_order_detail`
  ADD CONSTRAINT `food_order_detail_ibfk_1` FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `food_order_detail_ibfk_2` FOREIGN KEY (`food_order_id`) REFERENCES `food_order` (`food_order_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `hall`
--
ALTER TABLE `hall`
  ADD CONSTRAINT `hall_ibfk_1` FOREIGN KEY (`CapacityID`) REFERENCES `capacity` (`CapacityID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `hall_ibfk_2` FOREIGN KEY (`hall_type_id`) REFERENCES `hall_type` (`hall_type_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `hall_book`
--
ALTER TABLE `hall_book`
  ADD CONSTRAINT `hall_book_ibfk_1` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`employee_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `hall_book_ibfk_2` FOREIGN KEY (`guest_id`) REFERENCES `guest` (`guest_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `hall_booked_detail`
--
ALTER TABLE `hall_booked_detail`
  ADD CONSTRAINT `hall_booked_detail_ibfk_1` FOREIGN KEY (`Hall_Booked_id`) REFERENCES `hall_book` (`Hall_Booked_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `hall_booked_detail_ibfk_2` FOREIGN KEY (`hall_id`) REFERENCES `hall` (`hall_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `hall_booked_detail_ibfk_4` FOREIGN KEY (`payment_status_id`) REFERENCES `payment_status` (`payment_status_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `laundary_service`
--
ALTER TABLE `laundary_service`
  ADD CONSTRAINT `laundary_service_ibfk_1` FOREIGN KEY (`service_type_id`) REFERENCES `laundary_service_type` (`service_type_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `laundary_service_ibfk_2` FOREIGN KEY (`cloth_id`) REFERENCES `cloth` (`cloth_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `laundary_service_order`
--
ALTER TABLE `laundary_service_order`
  ADD CONSTRAINT `laundary_service_order_ibfk_1` FOREIGN KEY (`booking_detail_id`) REFERENCES `booking_detail` (`booking_detail_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `laundary_service_order_ibfk_3` FOREIGN KEY (`Order_Status_id`) REFERENCES `order_status` (`Order_Status_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `laundary_service_order_ibfk_4` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`employee_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `laundary_service_order_detail`
--
ALTER TABLE `laundary_service_order_detail`
  ADD CONSTRAINT `laundary_service_order_detail_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `laundary_service_order` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `laundary_service_order_detail_ibfk_2` FOREIGN KEY (`service_id`) REFERENCES `laundary_service` (`service_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `room_ibfk_1` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
