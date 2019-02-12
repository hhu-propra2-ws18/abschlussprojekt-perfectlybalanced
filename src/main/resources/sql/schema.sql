CREATE TABLE IF NOT EXISTS user(
    id LONG not NULL AUTO_INCREMENT,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    userName VARCHAR(255),
    password VARCHAR(255),
    mail VARCHAR(255),
    primary key (id)
  );

CREATE TABLE IF NOT EXISTS product(
    id LONG not NULL AUTO_INCREMENT,
    description VARCHAR(255),
    title VARCHAR(255),
    surety INTEGER,
    cost INTEGER,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS address(
    id LONG not NULL AUTO_INCREMENT,
    street VARCHAR(255),
    housenumber INTEGER,
    postcode INTEGER,
    city VARCHAR(255),
    primary key (id)
);
