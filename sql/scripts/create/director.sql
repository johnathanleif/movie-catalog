CREATE SCHEMA  IF NOT EXISTS  movie_catalog;
USE movie_catalog;

DROP TABLE IF EXISTS director;
CREATE TABLE director (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(70) DEFAULT NULL,
  PRIMARY KEY (id)
);