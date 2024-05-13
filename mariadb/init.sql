CREATE SCHEMA IF NOT EXISTS Mlidb;

CREATE TABLE IF NOT EXISTS Mlidb.Users (
    id INT,
    username VARCHAR(255),
    passHash VARCHAR(255)
);

-- ignore is to avoid errors on insertions with duplicates
INSERT IGNORE INTO Mlidb.Users VALUES (1, 'Otac', 'la9psd71atbpgeg7fvvx');
INSERT IGNORE INTO Mlidb.Users VALUES (2, 'Sin', 'ox9w79g2jwctzww2hcyb');
INSERT IGNORE INTO Mlidb.Users VALUES (3, 'Duh', 'othyqhps18srg7fdj0p9');