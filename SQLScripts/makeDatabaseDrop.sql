DROP DATABASE if exists MapDB;
CREATE DATABASE MapDB;
drop user if exists MapUser@localhost;
flush privileges;
CREATE USER 'MapUser'@'localhost' IDENTIFIED BY 'map';
GRANT SELECT ON MapDB.* TO 'MapUser'@'localhost';
