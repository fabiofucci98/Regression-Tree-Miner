CREATE DATABASE MapDB;

CREATE USER 'MapUser'@'localhost' IDENTIFIED BY 'map';
GRANT SELECT ON MapDB.* TO 'MapUser'@'localhost';
