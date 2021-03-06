CREATE TABLE USERS (
	ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	AGE INTEGER NOT NULL,
	FIRSTNAME VARCHAR(50) NOT NULL,
	LASTNAME VARCHAR(50) NOT NULL,
	USERNAME VARCHAR(50) NOT NULL
);

CREATE SEQUENCE USERS_SEQUENCE START WITH 1 INCREMENT BY 1;

ALTER TABLE USERS ADD CONSTRAINT UQ_USERS_USERNAME UNIQUE(USERNAME);