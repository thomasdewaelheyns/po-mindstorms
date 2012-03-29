DROP DATABASE IF EXISTS robots;
CREATE DATABASE robots;
USE robots;

CREATE TABLE model (
  id            INTEGER       PRIMARY KEY AUTO_INCREMENT,
  ts            TIMESTAMP     DEFAULT NOW(),

  robot         VARCHAR(50),  -- robot name
  lightValue    INTEGER,      -- value read by the light sensor
  lightColor    VARCHAR(10),  -- interpreted color by the robot
  avgLightValue INTEGER,      -- current average lightValue
  barcode       INTEGER,      -- detected barcode
  sonarAngle    INTEGER,      -- current sonar angle
  sonarDistance INTEGER,      -- current sonar distance
  ir1           INTEGER,      -- IR sensor data
  ir2           INTEGER,      -- for each sensor bearing the distance
  ir3           INTEGER,
  ir4           INTEGER,
  ir5           INTEGER,
  ir_dist       INTEGER,      -- the shortes distance
  walls         INTEGER,      -- walls configuration of current sector
  value_n       INTEGER,      -- value of the sector north of me
  value_e       INTEGER,      --                     east
  value_s       INTEGER,      --                     south
  value_w       INTEGER,      --                     west
  event         VARCHAR(25),  -- interesting event (e.g. pacman detected)
  source        VARCHAR(128), -- info about the event
  plan          VARCHAR(25),  -- navigator plan (e.g.: go north)
  queue         VARCHAR(128), --                (e.g.: turn left, go forward)
  action        VARCHAR(25),  -- current action (e.g.: go forward)
  argument      VARCHAR(25),  -- e.g.: (distance)20cm (turn)-90deg
  rate          INTEGER       -- frame rate
);

CREATE TABLE sectorWalls (
  id            INTEGER       PRIMARY KEY AUTO_INCREMENT,
  ts            TIMESTAMP     DEFAULT NOW(),

  robot         VARCHAR(50),  -- robot name
  grid          VARCHAR(50),  -- myGrid, others[0], others[1], others[2]
  x             INTEGER,      -- sector left
  y             INTEGER,      -- sector top
  walls         INTEGER       -- wall configuration (bitfield)
);

CREATE TABLE sectorValues (
  id            INTEGER       PRIMARY KEY AUTO_INCREMENT,
  ts            TIMESTAMP     DEFAULT NOW(),

  robot         VARCHAR(50),  -- robot name
  grid          VARCHAR(50),  -- myGrid, others[0], others[1], others[2]
  x             INTEGER,      -- sector left
  y             INTEGER,      -- sector top
  value         INTEGER       -- value of the sector
);

CREATE TABLE sectorAgents (
  id            INTEGER       PRIMARY KEY AUTO_INCREMENT,
  ts            TIMESTAMP     DEFAULT NOW(),

  robot         VARCHAR(50),  -- robot name
  grid          VARCHAR(50),  -- myGrid, others[0], others[1], others[2]
  name          VARCHAR(50),  -- name of the gateway
  x             INTEGER,      -- sector left
  y             INTEGER,      -- sector top
  bearing       INTEGER,      -- bearing 1,2,3,4
  color         VARCHAR(50)   -- e.g.: yellow, white
);
