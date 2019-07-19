DROP TABLE user IF EXISTS;

CREATE TABLE  user (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 100, INCREMENT BY 1) PRIMARY KEY,
 	name varchar(45)
);

INSERT INTO user(name) VALUES('Jack Rutorial demo 1'),('Jack Rutorial demo 2'),('Jack Rutorial demo 3');