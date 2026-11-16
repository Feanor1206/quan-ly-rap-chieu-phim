
CREATE DATABASE IF NOT EXISTS `cinema_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `cinema_db`;

DROP TABLE IF EXISTS `tbl_ve`;
DROP TABLE IF EXISTS `ve_xem_phim`;
DROP TABLE IF EXISTS `tbl_hoa_don`;
DROP TABLE IF EXISTS `hoa_don`;
DROP TABLE IF EXISTS `tbl_khach_hang`;
DROP TABLE IF EXISTS `khach_hang`;
DROP TABLE IF EXISTS `tbl_lich_chieu`;
DROP TABLE IF EXISTS `lich_chieu`;
DROP TABLE IF EXISTS `tbl_phong_chieu`;
DROP TABLE IF EXISTS `phong_chieu`;
DROP TABLE IF EXISTS `tbl_phim`;
DROP TABLE IF EXISTS `phim`;

CREATE TABLE `tbl_khach_hang` (
    `id_kh` VARCHAR(20) NOT NULL PRIMARY KEY,
    `ho_ten` VARCHAR(100) NOT NULL,
    `so_dien_thoai` VARCHAR(15) NOT NULL UNIQUE,
    `diem_tich_luy` INT DEFAULT 0,
    `loai_kh` VARCHAR(20) DEFAULT 'Standard'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `tbl_phim` (
    `id_phim` VARCHAR(20) NOT NULL PRIMARY KEY,
    `ten_phim` VARCHAR(255) NOT NULL,
    `the_loai` VARCHAR(100),
    `thoi_luong` INT NOT NULL,
    `dao_dien` VARCHAR(100),
    `gioi_han_tuoi` VARCHAR(10),
    `mo_ta` TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `tbl_phong_chieu` (
    `id_phong` VARCHAR(20) NOT NULL PRIMARY KEY,
    `ten_phong` VARCHAR(100) NOT NULL,
    `so_hang` INT NOT NULL,
    `so_cot` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `tbl_lich_chieu` (
    `id_lich_chieu` VARCHAR(20) NOT NULL PRIMARY KEY,
    `id_phim` VARCHAR(20) NOT NULL,
    `id_phong` VARCHAR(20) NOT NULL,
    `ngay_chieu` DATE NOT NULL,
    `gio_bat_dau` TIME NOT NULL,
    `gio_ket_thuc` TIME NOT NULL,
    `gia_ve_co_ban` DOUBLE NOT NULL DEFAULT 80000,
    CONSTRAINT `fk_lc_phim` FOREIGN KEY (`id_phim`) REFERENCES `tbl_phim` (`id_phim`) ON DELETE CASCADE,
    CONSTRAINT `fk_lc_phong` FOREIGN KEY (`id_phong`) REFERENCES `tbl_phong_chieu` (`id_phong`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `tbl_hoa_don` (
    `id_hoa_don` VARCHAR(30) NOT NULL PRIMARY KEY,
    `ngay_lap` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `tong_tien_goc` DOUBLE NOT NULL DEFAULT 0,
    `discount` DOUBLE NOT NULL DEFAULT 0,
    `tong_thanh_toan` DOUBLE NOT NULL DEFAULT 0,
    `phuong_thuc_tt` VARCHAR(50) DEFAULT 'TIEN_MAT',
    `id_kh` VARCHAR(20) NULL,
    `nhan_vien_lap` VARCHAR(50) DEFAULT 'NV_QUAY',
    CONSTRAINT `fk_hd_kh` FOREIGN KEY (`id_kh`) REFERENCES `tbl_khach_hang` (`id_kh`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `tbl_ve` (
    `id_ve` VARCHAR(30) NOT NULL PRIMARY KEY,
    `id_lich_chieu` VARCHAR(20) NOT NULL,
    `ma_ghe` VARCHAR(10) NOT NULL,
    `loai_ghe` VARCHAR(20) NOT NULL DEFAULT 'Regular',
    `gia_ve` DOUBLE NOT NULL,
    `trang_thai` VARCHAR(20) NOT NULL DEFAULT 'TRONG',
    `id_hoa_don` VARCHAR(30) NULL,
    `thoi_gian_ban` DATETIME NULL,
    CONSTRAINT `fk_ve_lichchieu` FOREIGN KEY (`id_lich_chieu`) REFERENCES `tbl_lich_chieu` (`id_lich_chieu`) ON DELETE CASCADE,
    CONSTRAINT `fk_ve_hoadon` FOREIGN KEY (`id_hoa_don`) REFERENCES `tbl_hoa_don` (`id_hoa_don`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE OR REPLACE VIEW `khach_hang` AS SELECT * FROM `tbl_khach_hang`;
CREATE OR REPLACE VIEW `phim` AS SELECT * FROM `tbl_phim`;
CREATE OR REPLACE VIEW `phong_chieu` AS SELECT * FROM `tbl_phong_chieu`;
CREATE OR REPLACE VIEW `lich_chieu` AS SELECT * FROM `tbl_lich_chieu`;
CREATE OR REPLACE VIEW `hoa_don` AS SELECT * FROM `tbl_hoa_don`;
CREATE OR REPLACE VIEW `ve_xem_phim` AS SELECT * FROM `tbl_ve`;

INSERT INTO `tbl_khach_hang` (`id_kh`, `ho_ten`, `so_dien_thoai`, `diem_tich_luy`, `loai_kh`) VALUES
('KH001', 'Nguyễn Văn An', '0901234567', 850, 'VIP'),
('KH002', 'Trần Thị Bình', '0912345678', 1200, 'Diamond'),
('KH003', 'Lê Hoàng Cường', '0988776655', 200, 'Standard');

INSERT INTO `tbl_phim` (`id_phim`, `ten_phim`, `the_loai`, `thoi_luong`, `dao_dien`, `gioi_han_tuoi`, `mo_ta`) VALUES
('P01', 'Đào, Phở và Piano', 'Lịch sử, Tâm lý', 100, 'Phi Tiến Sơn', 'T13', 'Tái hiện không khí Hà Nội 60 ngày đêm khói lửa năm 1946'),
('P02', 'Mai', 'Tâm lý, Tình cảm', 131, 'Trấn Thành', 'T18', 'Câu chuyện về cuộc đời nhiều sóng gió của người phụ nữ tên Mai'),
('P03', 'Lật Mặt 7: Một Điều Ước', 'Gia đình, Tâm lý', 138, 'Lý Hải', 'T13', 'Câu chuyện cảm động về tình mẫu tử');

INSERT INTO `tbl_phong_chieu` (`id_phong`, `ten_phong`, `so_hang`, `so_cot`) VALUES
('PC01', 'Phòng 01 (Dolby Atmos)', 5, 8),
('PC02', 'Phòng 02 (IMAX Laser)', 6, 10);

INSERT INTO `tbl_lich_chieu` (`id_lich_chieu`, `id_phim`, `id_phong`, `ngay_chieu`, `gio_bat_dau`, `gio_ket_thuc`, `gia_ve_co_ban`) VALUES
('LC01', 'P01', 'PC01', CURDATE(), '09:30:00', '11:25:00', 80000),
('LC02', 'P02', 'PC02', CURDATE(), '14:00:00', '16:30:00', 110000),
('LC03', 'P03', 'PC01', CURDATE(), '19:00:00', '21:35:00', 95000);

INSERT INTO `tbl_hoa_don` (`id_hoa_don`, `ngay_lap`, `tong_tien_goc`, `discount`, `tong_thanh_toan`, `phuong_thuc_tt`, `id_kh`, `nhan_vien_lap`) VALUES
('HD20261116001', NOW(), 160000, 0, 160000, 'TIEN_MAT', 'KH001', 'NV_QUAY');

INSERT INTO `tbl_ve` (`id_ve`, `id_lich_chieu`, `ma_ghe`, `loai_ghe`, `gia_ve`, `trang_thai`, `id_hoa_don`, `thoi_gian_ban`) VALUES
('VE-LC01-A3', 'LC01', 'A3', 'Regular', 80000, 'DA_BAN', 'HD20261116001', NOW()),
('VE-LC01-A4', 'LC01', 'A4', 'Regular', 80000, 'DA_BAN', 'HD20261116001', NOW());
