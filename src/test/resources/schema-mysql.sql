SET foreign_key_checks = 0;
DROP TABLE IF EXISTS `book`;
DROP TABLE IF EXISTS `author`;

CREATE TABLE IF NOT EXISTS `author` (
  `author_id` INTEGER UNSIGNED NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  PRIMARY KEY (`author_id`)
) AUTO_INCREMENT=1,
ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS `book` (
  `book_id` INTEGER NOT NULL auto_increment,
  `title` varchar(255) default NULL,
  `ISBN` varchar(255) UNIQUE,
  `author` INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY (`book_id`)
) AUTO_INCREMENT=1,
ENGINE = INNODB;

ALTER TABLE `book`
ADD CONSTRAINT `fk_author`
FOREIGN KEY (`author`)
REFERENCES `author`(`author_id`)
ON DELETE RESTRICT ON UPDATE RESTRICT;


