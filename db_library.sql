-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.29 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.0.0.6468
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping data for table db_library.tbl_book: ~5 rows (approximately)
REPLACE INTO `tbl_book` (`book_id`, `book_title`, `book_author`, `book_isbn`, `book_edition`) VALUES
	(1, 'The Guide', 'R.K. Narayan', 'bk_01', '1st edition'),
	(2, 'God of Small Things', 'Arundhati Roy', 'bk_02', '2nd edition'),
	(3, 'Gitanjali', 'Rabindranath Tagore', 'bk_03', '1st edition'),
	(4, 'A Fine Balance', 'Rohinton Mistry', 'bk_04', '3rd edition'),
	(5, 'The Guide', 'R.K. Narayan', 'bk_05', '2nd edition');

-- Dumping data for table db_library.tbl_book_copies: ~10 rows (approximately)
REPLACE INTO `tbl_book_copies` (`id`, `book_id`, `book_serial`, `availability_status`) VALUES
	(1, 1, '11111', 1),
	(2, 1, '12222', 0),
	(3, 1, '13333', 1),
	(4, 1, '14444', 0),
	(5, 2, '21111', 1),
	(6, 2, '22222', 0),
	(7, 2, '23333', 0),
	(8, 3, '31111', 0),
	(9, 3, '32222', 0),
	(10, 4, '41111', 0),
	(11, 5, '51111', 0);

-- Dumping data for table db_library.tbl_lend: ~6 rows (approximately)
REPLACE INTO `tbl_lend` (`id`, `book_id`, `book_serial`, `borrower_name`, `borrower_id`, `borrower_contact`, `borrower_dob`, `date_of_issue`, `return_date`, `return_status`) VALUES
	(2, 2, '21111', 'Ajay', '3546584', '9876543210', '01/01/2022', '2022-05-20', '2022-05-26', 0),
	(8, 1, '12222', 'Ajay', '123', '123', '01/01/2022', '2022-06-10', '0000-00-00', 0),
	(9, 1, '11111', 'The Guide', '121432', '324532', '26/01/2022', '2022-05-24', '2022-06-10', 0),
	(10, 1, '11111', 'The Guide', '121432', '324532', '26/01/2022', '2022-05-24', '2022-06-10', 0),
	(11, 1, '11111', 'The Guide', '121432', '324532', '26/01/2022', '2022-05-24', '2022-06-10', 0);

-- Dumping data for table db_library.tbl_library: ~4 rows (approximately)
REPLACE INTO `tbl_library` (`id`, `book_id`, `total_copies`, `available_copies`) VALUES
	(1, 1, 4, 3),
	(2, 2, 3, 2),
	(3, 3, 2, 2),
	(4, 4, 1, 1),
	(5, 5, 1, 1);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
