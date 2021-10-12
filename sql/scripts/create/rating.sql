CREATE SCHEMA  IF NOT EXISTS movie_catalog;
USE movie_catalog;

DROP TABLE IF EXISTS rating;
CREATE TABLE rating (
  id int NOT NULL AUTO_INCREMENT,
  symbol varchar(10) NOT NULL,
  age_limit tinyint NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (symbol)
);