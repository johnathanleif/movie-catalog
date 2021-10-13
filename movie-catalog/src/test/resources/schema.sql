CREATE SCHEMA  IF NOT EXISTS movie_catalog;
USE movie_catalog;

CREATE TABLE IF NOT EXISTS director (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(70) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rating (
  id int NOT NULL AUTO_INCREMENT,
  symbol varchar(10) NOT NULL,
  age_limit tinyint NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS movie (
  id int NOT NULL AUTO_INCREMENT,
  title varchar(255) DEFAULT NULL,
  rating_id int DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (rating_id) REFERENCES rating(id)
);

CREATE TABLE IF NOT EXISTS movie_director (
  id int NOT NULL AUTO_INCREMENT,
  movie_id int NOT NULL,
  director_id int NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (director_id) REFERENCES director(id),
  FOREIGN KEY (movie_id) REFERENCES movie(id)
);