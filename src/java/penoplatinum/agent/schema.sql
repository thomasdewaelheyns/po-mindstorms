DROP DATABASE IF EXISTS robots;
CREATE DATABASE robots;
USE robots;

CREATE TABLE model (
  id            INTEGER       PRIMARY KEY AUTO_INCREMENT,
  ts            TIMESTAMP     DEFAULT NOW(),
  robot         VARCHAR(50)   NOT NULL,
  
  lightValue    INTEGER,
  lightColor    VARCHAR(10),
  barcode       INTEGER,
  sonarAngle    INTEGER,
  sonarDistance INTEGER,
  pushLeft      BIT,
  pushRight     BIT,
  event         VARCHAR(25),
  source        VARCHAR(128),
  plan          VARCHAR(25),
  queue         VARCHAR(128),
  action        VARCHAR(25),
  argument      VARCHAR(25)
);

CREATE TABLE walls (
  id            INTEGER       PRIMARY KEY AUTO_INCREMENT,
  ts            TIMESTAMP     DEFAULT NOW(),
  robot         VARCHAR(50)   NOT NULL,

  left          INTEGER,
  top           INTEGER,
  value         INTEGER
);

CREATE TABLE values (
  id            INTEGER       PRIMARY KEY AUTO_INCREMENT,
  ts            TIMESTAMP     DEFAULT NOW(),
  robot         VARCHAR(50)   NOT NULL,

  
);

CREATE TABLE agents (
  id            INTEGER       PRIMARY KEY AUTO_INCREMENT,
  ts            TIMESTAMP     DEFAULT NOW(),
  robot         VARCHAR(50)   NOT NULL,

  
);