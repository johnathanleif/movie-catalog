CREATE SCHEMA  IF NOT EXISTS movie_catalog;
USE movie_catalog;

DROP TABLE IF EXISTS movie_director;
CREATE TABLE movie_director (
  id int NOT NULL AUTO_INCREMENT,
  movie_id int NOT NULL,
  director_id int NOT NULL,
  PRIMARY KEY (id)
);
ALTER TABLE movie_director ADD FOREIGN KEY (director_id) REFERENCES director(id);
ALTER TABLE movie_director ADD FOREIGN KEY (movie_id) REFERENCES movie(id);