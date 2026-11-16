CREATE TABLE IF NOT EXISTS RapPhim (
    id INTEGER PRIMARY KEY,
    tenRap TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS PhongChieu (
    id INTEGER PRIMARY KEY,
    rapPhimId INTEGER NOT NULL REFERENCES RapPhim(id),
    tenPhong TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Phim (
    id INTEGER PRIMARY KEY,
    tenPhim TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS SuatChieu (
    id INTEGER PRIMARY KEY,
    phimId INTEGER NOT NULL REFERENCES Phim(id),
    phongChieuId INTEGER NOT NULL REFERENCES PhongChieu(id),
    gioBatDau TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS HoaDon (
    id INTEGER PRIMARY KEY,
    thoiDiemLap TEXT NOT NULL,
    tienDichVu INTEGER NOT NULL CHECK (tienDichVu >= 0)
);

CREATE TABLE IF NOT EXISTS VeXemPhim (
    id INTEGER PRIMARY KEY,
    suatChieuId INTEGER NOT NULL REFERENCES SuatChieu(id),
    hoaDonId INTEGER NOT NULL REFERENCES HoaDon(id),
    tienVeThucTra INTEGER NOT NULL CHECK (tienVeThucTra >= 0)
);

CREATE TABLE IF NOT EXISTS GiaoDichTraVe (
    id INTEGER PRIMARY KEY,
    veXemPhimId INTEGER NOT NULL REFERENCES VeXemPhim(id),
    thoiDiemTra TEXT NOT NULL,
    tienHoan INTEGER NOT NULL CHECK (tienHoan >= 0)
);

CREATE INDEX IF NOT EXISTS HoaDon_thoiDiemLap ON HoaDon(thoiDiemLap);
CREATE INDEX IF NOT EXISTS VeXemPhim_hoaDonId ON VeXemPhim(hoaDonId);
CREATE INDEX IF NOT EXISTS GiaoDichTraVe_thoiDiemTra ON GiaoDichTraVe(thoiDiemTra);

INSERT INTO RapPhim(id, tenRap) VALUES
    (1, 'Rạp Trung Tâm'),
    (2, 'Rạp Bờ Sông')
ON CONFLICT(id) DO NOTHING;

INSERT INTO PhongChieu(id, rapPhimId, tenPhong) VALUES
    (1, 1, 'Phòng 1'),
    (2, 1, 'Phòng 2'),
    (3, 2, 'Phòng 1'),
    (4, 2, 'Phòng 2')
ON CONFLICT(id) DO NOTHING;

INSERT INTO Phim(id, tenPhim) VALUES
    (1, 'Chuyến tàu đêm'),
    (2, 'Mùa hè ở phố'),
    (3, 'Ngọn đèn biển')
ON CONFLICT(id) DO NOTHING;

INSERT INTO SuatChieu(id, phimId, phongChieuId, gioBatDau) VALUES
    (11, 1, 1, '2026-12-20 19:30:00'),
    (12, 1, 3, '2026-12-19 20:00:00'),
    (21, 2, 2, '2026-12-18 19:00:00'),
    (31, 3, 4, '2026-12-21 19:45:00')
ON CONFLICT(id) DO NOTHING;

INSERT INTO HoaDon(id, thoiDiemLap, tienDichVu) VALUES
    (101, '2026-12-15 00:00:00', 35000),
    (102, '2026-12-17 23:59:59', 50000),
    (103, '2026-12-14 22:00:00', 27500),
    (104, '2026-12-18 00:00:00', 0),
    (105, '2026-12-16 14:25:00', 30000),
    (106, '2026-12-14 15:00:00', 20000)
ON CONFLICT(id) DO NOTHING;

INSERT INTO VeXemPhim(id, suatChieuId, hoaDonId, tienVeThucTra) VALUES
    (1, 11, 101, 90000),
    (2, 11, 101, 70000),
    (3, 11, 101, 0),
    (4, 12, 102, 110000),
    (5, 11, 103, 80000),
    (6, 21, 104, 90000),
    (7, 21, 105, 95000),
    (8, 31, 106, 75000)
ON CONFLICT(id) DO NOTHING;

INSERT INTO GiaoDichTraVe(id, veXemPhimId, thoiDiemTra, tienHoan) VALUES
    (1, 5, '2026-12-16 13:00:00', 60000),
    (2, 7, '2026-12-18 12:00:00', 95000),
    (3, 8, '2026-12-17 23:59:59', 75000),
    (4, 2, '2026-12-17 11:15:00', 70000)
ON CONFLICT(id) DO NOTHING;
