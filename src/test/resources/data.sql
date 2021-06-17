DROP TABLE IF EXISTS ExampleData;

CREATE TABLE ExampleData (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  foreign_id INTEGER NOT NULL,
  word VARCHAR(250) NOT NULL,
  created DATETIME NOT NULL
);

INSERT INTO ExampleData (foreign_id, word, created) VALUES
  (123, 'Something', '2018-04-21 08:32:50'),
  (456, 'Otherthing', '2018-04-21 08:32:50'),
  (678, 'Anotherthing', '2018-04-21 08:32:50');
