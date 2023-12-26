-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 26, 2023 at 12:01 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tugasakhir`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_barang`
--

CREATE TABLE `tb_barang` (
  `id_barang` int(5) NOT NULL,
  `id_pemasok` int(5) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `harga_beli` int(15) NOT NULL,
  `harga_jual` int(15) NOT NULL,
  `harga_rataRata` int(15) NOT NULL,
  `margin` int(10) NOT NULL,
  `stok` int(10) NOT NULL,
  `satuan` varchar(5) NOT NULL,
  `tanggal_masuk` date NOT NULL,
  `diedit_pada` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_barang`
--

INSERT INTO `tb_barang` (`id_barang`, `id_pemasok`, `nama`, `harga_beli`, `harga_jual`, `harga_rataRata`, `margin`, `stok`, `satuan`, `tanggal_masuk`, `diedit_pada`) VALUES
(1001, 1001, 'Indomie', 200000, 11000, 10000, 10, 10, 'Pack', '2023-12-26', '2023-12-26');

-- --------------------------------------------------------

--
-- Table structure for table `tb_cart`
--

CREATE TABLE `tb_cart` (
  `id` int(5) NOT NULL,
  `id_cart` int(5) NOT NULL,
  `id_barang` int(5) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `harga` int(20) NOT NULL,
  `qty` int(10) NOT NULL,
  `total_harga` int(20) NOT NULL,
  `total_hargaPokok` int(15) NOT NULL,
  `status_cart` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_cart`
--

INSERT INTO `tb_cart` (`id`, `id_cart`, `id_barang`, `nama`, `harga`, `qty`, `total_harga`, `total_hargaPokok`, `status_cart`) VALUES
(11, 1, 1001, 'Indomie', 11000, 2, 22000, 20000, 'checkout'),
(12, 1, 1001, 'Indomie', 11000, 2, 22000, 20000, 'checkout'),
(13, 2, 1001, 'Indomie', 11000, 4, 44000, 40000, 'checkout'),
(14, 3, 1001, 'Indomie', 11000, 2, 22000, 20000, 'checkout');

--
-- Triggers `tb_cart`
--
DELIMITER $$
CREATE TRIGGER `restoreStok` BEFORE DELETE ON `tb_cart` FOR EACH ROW BEGIN
    DECLARE produk_id INT;
    DECLARE jumlah_qty INT;

    SELECT id_barang, qty INTO produk_id, jumlah_qty FROM tb_cart WHERE id = OLD.id;

    UPDATE tb_barang SET stok = stok + jumlah_qty WHERE id_barang = produk_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `updateStok` AFTER INSERT ON `tb_cart` FOR EACH ROW BEGIN
    DECLARE produk_id INT;
    DECLARE jumlah_qty INT;
    
    SELECT id_barang, qty INTO produk_id, jumlah_qty FROM tb_cart WHERE id = NEW.id;
    
    UPDATE tb_barang SET stok = stok - jumlah_qty WHERE id_barang = produk_id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_diskon`
--

CREATE TABLE `tb_diskon` (
  `id_diskon` int(5) NOT NULL,
  `id_barang` int(5) NOT NULL,
  `harga_awal` int(20) NOT NULL,
  `jumlah_diskon` int(3) NOT NULL,
  `harga_diskon` int(20) NOT NULL,
  `berlaku` date NOT NULL,
  `sampai` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tb_member`
--

CREATE TABLE `tb_member` (
  `id_member` int(5) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `alamat` varchar(50) NOT NULL,
  `poin` int(10) NOT NULL,
  `tanggal_gabung` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_member`
--

INSERT INTO `tb_member` (`id_member`, `nama`, `alamat`, `poin`, `tanggal_gabung`) VALUES
(1001, 'Dodek Mardana', 'Tabanan', 12, '2023-12-01');

-- --------------------------------------------------------

--
-- Table structure for table `tb_pemasok`
--

CREATE TABLE `tb_pemasok` (
  `id_pemasok` int(5) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `alamat` text NOT NULL,
  `tanggal_masuk` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_pemasok`
--

INSERT INTO `tb_pemasok` (`id_pemasok`, `nama`, `alamat`, `tanggal_masuk`) VALUES
(1001, 'Bali Jaya', 'Jimbaran', '2023-12-16');

-- --------------------------------------------------------

--
-- Table structure for table `tb_transaksi`
--

CREATE TABLE `tb_transaksi` (
  `id_transaksi` int(5) NOT NULL,
  `id_cart` int(5) NOT NULL,
  `id_member` int(5) DEFAULT NULL,
  `total_harga` int(20) NOT NULL,
  `total_bayar` int(20) NOT NULL,
  `Jumlah_kembalian` int(20) NOT NULL,
  `total_modal` int(15) NOT NULL,
  `tanggal_transaksi` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_transaksi`
--

INSERT INTO `tb_transaksi` (`id_transaksi`, `id_cart`, `id_member`, `total_harga`, `total_bayar`, `Jumlah_kembalian`, `total_modal`, `tanggal_transaksi`) VALUES
(9, 1, 1001, 44000, 45000, 1000, 40000, '2023-12-26'),
(10, 2, NULL, 44000, 50000, 6000, 40000, '2023-12-26'),
(11, 3, NULL, 22000, 22000, 0, 20000, '2023-12-10');

-- --------------------------------------------------------

--
-- Table structure for table `tb_user`
--

CREATE TABLE `tb_user` (
  `id_user` int(5) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `alamat` varchar(50) NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `admin` int(2) NOT NULL,
  `gender` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_user`
--

INSERT INTO `tb_user` (`id_user`, `username`, `password`, `email`, `nama`, `alamat`, `tanggal_lahir`, `admin`, `gender`) VALUES
(1001, 'mardana', '1234', 'dewamardana@gmail.com', 'Dewa Made Mardana', 'Tabanan', '2003-09-06', 1, 'Pria'),
(1002, 'budi', '1234', 'budi@gmail.com', 'Budi Doraemon', 'Tabanan', '2004-09-06', 0, 'Pria'),
(2121, 'Lanang Purbhawa', '2121', 'bagus@gmail.com', 'Lanang', 'Bali', '2003-06-20', 0, 'Pria'),
(12347, 'LanangPB', '1234', 'lanang@gmail.com', 'Lanang', 'Gianyar', '2023-12-06', 0, 'Pria');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_barang`
--
ALTER TABLE `tb_barang`
  ADD PRIMARY KEY (`id_barang`),
  ADD KEY `id_pemasok` (`id_pemasok`);

--
-- Indexes for table `tb_cart`
--
ALTER TABLE `tb_cart`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_barang` (`id_barang`),
  ADD KEY `id_cart` (`id_cart`);

--
-- Indexes for table `tb_diskon`
--
ALTER TABLE `tb_diskon`
  ADD PRIMARY KEY (`id_diskon`),
  ADD UNIQUE KEY `id_barang_2` (`id_barang`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indexes for table `tb_member`
--
ALTER TABLE `tb_member`
  ADD PRIMARY KEY (`id_member`);

--
-- Indexes for table `tb_pemasok`
--
ALTER TABLE `tb_pemasok`
  ADD PRIMARY KEY (`id_pemasok`);

--
-- Indexes for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `id_cart` (`id_cart`,`id_member`),
  ADD KEY `id_member` (`id_member`);

--
-- Indexes for table `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_cart`
--
ALTER TABLE `tb_cart`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `tb_diskon`
--
ALTER TABLE `tb_diskon`
  MODIFY `id_diskon` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  MODIFY `id_transaksi` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_barang`
--
ALTER TABLE `tb_barang`
  ADD CONSTRAINT `tb_barang_ibfk_1` FOREIGN KEY (`id_pemasok`) REFERENCES `tb_pemasok` (`id_pemasok`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tb_cart`
--
ALTER TABLE `tb_cart`
  ADD CONSTRAINT `tb_cart_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `tb_barang` (`id_barang`);

--
-- Constraints for table `tb_diskon`
--
ALTER TABLE `tb_diskon`
  ADD CONSTRAINT `tb_diskon_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `tb_barang` (`id_barang`) ON UPDATE CASCADE;

--
-- Constraints for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  ADD CONSTRAINT `tb_transaksi_ibfk_2` FOREIGN KEY (`id_cart`) REFERENCES `tb_cart` (`id_cart`) ON DELETE NO ACTION ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
