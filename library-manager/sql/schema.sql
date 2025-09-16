DROP TABLE IF EXISTS password_reset_token;
DROP TABLE IF EXISTS lending_detail;
DROP TABLE IF EXISTs member;
DROP TABLE IF EXISTS document;
DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user (
	id SERIAL PRIMARY KEY,
	username VARCHAR(127) NOT NULL UNIQUE CHECK(LENGTH(TRIM(username)) > 0),
	password VARCHAR(127) NOT NULL CHECK(LENGTH(TRIM(password)) > 0),
	email VARCHAR(127) NOT NULL UNIQUE CHECK(LENGTH(TRIM(email)) > 0)
);

CREATE TABLE document (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL CHECK(LENGTH(TRIM(name)) > 0),
	author VARCHAR(255),
	description BPCHAR,
	isbn VARCHAR(13) UNIQUE,
	upload_time TIMESTAMP,
	category VARCHAR(255),
	copies INTEGER NOT NULL,
	cover_image_url VARCHAR(255) NOT NULL DEFAULT 'threeoone/bigproject/controller/viewcontrollers/No_image_available.png',
	pdf_url VARCHAR(255),
	info_url VARCHAR(255),
	uploader_id INTEGER REFERENCES app_user NOT NULL
);

CREATE TABLE member (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL CHECK(LENGTH(TRIM(name)) > 0),
	phone_number VARCHAR(11) NOT NULL CHECK(LENGTH(TRIM(phone_number)) > 0),
	address VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL
);

CREATE TABLE lending_detail (
	id SERIAL PRIMARY KEY,
	lend_time TIMESTAMP NOT NULL,
	due_time TIMESTAMP NOT NULL,
	member_id INTEGER REFERENCES member NOT NULL,
	document_id INTEGER REFERENCES document NOT NULL
);

CREATE TABLE password_reset_token (
	id SERIAL PRIMARY KEY,
	value VARCHAR(64) NOT NULL UNIQUE,
	expire_time TIMESTAMP NOT NULL,
	user_id INTEGER REFERENCES app_user NOT NULL
);