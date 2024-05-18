CREATE SCHEMA IF NOT EXISTS m_lidb;

-- ignore is to avoid errors on insertions with duplicates
INSERT IGNORE INTO m_lidb.l_user (username, pass_hash) VALUES ('Otac', 'la9psd71atbpgeg7fvvx');
INSERT IGNORE INTO m_lidb.l_user (username, pass_hash) VALUES ('Sin', 'ox9w79g2jwctzww2hcyb');
INSERT IGNORE INTO m_lidb.l_user (username, pass_hash) VALUES ('Duh', 'othyqhps18srg7fdj0p9');