CREATE SCHEMA  IF NOT EXISTS movie_catalog;
USE movie_catalog;

DROP TABLE IF EXISTS movie;
CREATE TABLE movie (
  id int NOT NULL AUTO_INCREMENT,
  title varchar(255) DEFAULT NULL,
  rating_id int DEFAULT NULL,
  PRIMARY KEY (id)
);
ALTER TABLE movie ADD FOREIGN KEY (rating_id) REFERENCES rating(id);