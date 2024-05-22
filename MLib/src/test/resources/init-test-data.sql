truncate table m_lidb.l_user restart IDENTITY;
truncate table m_lidb.version restart IDENTITY;

DELETE FROM m_lidb.s_library; -- Remove all existing rows

-- Reset identity column
ALTER TABLE m_lidb.s_library ALTER COLUMN id RESTART WITH 1;


-- korisnici i autentifikacija
INSERT INTO m_lidb.l_user (username, app_key) VALUES ('Otac', 'la9psd71atbpgeg7fvvx');
INSERT INTO m_lidb.l_user (username, app_key) VALUES ('Sin', 'ox9w79g2jwctzww2hcyb');
INSERT INTO m_lidb.l_user (username, app_key) VALUES ('Duh', 'othyqhps18srg7fdj0p9');

-- biblioteke
insert into m_lidb.s_library (lib_group_id, lib_artifact_id, lib_name, description)
values ('org.springframework', 'spring-core', 'Spring Core', 'Spring Core Framework');

insert into m_lidb.s_library (lib_group_id, lib_artifact_id, lib_name)
values ('org.moja', 'art1', 'Neka stvar');

insert into m_lidb.s_library (lib_group_id, lib_artifact_id, lib_name, description)
values ('org.tvoja', 'art2', 'Neka druga stvar', 'kaj te briga');

truncate table m_lidb.version restart identity;

-- verzije
insert into m_lidb.version (v_lib_id, v_semantic_version, v_description, v_release_date, v_deprecated)
values (1, '5.3.9', 'Spring Core Framework 5.3.9', '2024-05-19T12:34:56+00:00', false);

insert into m_lidb.version (v_lib_id, v_semantic_version, v_description, v_release_date, v_deprecated)
values (1, '5.3.10', 'Spring Core Framework 5.3.10', '2024-05-19T12:34:56+00:00', true);

insert into m_lidb.version (v_lib_id, v_semantic_version, v_description, v_release_date, v_deprecated)
values (2, '1.1.1', 'art1 prviii', '2024-05-19T12:34:56+00:00', false);

