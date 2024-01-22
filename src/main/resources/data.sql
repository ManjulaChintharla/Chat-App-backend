-- author: Mirva
-- tietokannan luontikoodi
--- tietokanta on luotu pilviympäristössä sijitsevaan MariaDB-kantaan
--tämän tiedoston tarkoitus on siis vain kuvata tietokannan rakenne

DROP DATABASE IF EXISTS chatapp_database;
CREATE DATABASE chatapp_database;
USE chatapp_database;

CREATE TABLE user
(
  user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  username VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  picture VARCHAR(250),
  description VARCHAR(500)
);

CREATE TABLE message
(
  message_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  message VARCHAR(2000) NOT NULL,
  file VARCHAR(250),
  date DATETIME NOT NULL,
  deleted INT,
  sender_id INT NOT NULL,
  receiver_id INT NOT NULL,
  FOREIGN KEY (sender_id) REFERENCES user(user_id),
  FOREIGN KEY (receiver_id) REFERENCES user(user_id)
);