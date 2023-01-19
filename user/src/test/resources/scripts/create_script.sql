BEGIN;

CREATE TABLE IF NOT EXISTS users
(
    id                 int NOT NULL PRIMARY KEY,
    firstName          varchar(45) DEFAULT NULL,
    lastName           varchar(45) DEFAULT NULL,
    address            varchar(45) DEFAULT NULL
);

COMMIT;