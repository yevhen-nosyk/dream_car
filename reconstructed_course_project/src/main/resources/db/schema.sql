CREATE DATABASE IF NOT EXISTS course_project;
USE course_project;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS request;
DROP TABLE IF EXISTS advertisement;
DROP TABLE IF EXISTS visit_to_office;
DROP TABLE IF EXISTS office;
DROP TABLE IF EXISTS car;
DROP TABLE IF EXISTS user_account;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE user_account (
    id BIGINT NOT NULL AUTO_INCREMENT,
    role VARCHAR(32) NOT NULL DEFAULT 'ROLE_AUTHORIZED',
    name VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(40),
    passport VARCHAR(80),
    address VARCHAR(255),
    rating FLOAT NOT NULL DEFAULT 0,
    account_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    PRIMARY KEY (id)
);

CREATE TABLE car (
    id BIGINT NOT NULL AUTO_INCREMENT,
    vin_code VARCHAR(64),
    brand VARCHAR(80) NOT NULL,
    model VARCHAR(80),
    car_year INT NOT NULL,
    owner_id BIGINT NOT NULL,
    suspended_by_ban BOOLEAN NOT NULL DEFAULT FALSE,
    mileage INT NOT NULL DEFAULT 0,
    vehicle_condition VARCHAR(30) NOT NULL DEFAULT 'USED',
    PRIMARY KEY (id),
    CONSTRAINT fk_car_owner FOREIGN KEY (owner_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE TABLE advertisement (
    id BIGINT NOT NULL AUTO_INCREMENT,
    description TEXT NOT NULL,
    creation_date DATETIME NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
    owner_id BIGINT NOT NULL,
    car_id BIGINT,
    suspended_by_ban BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT fk_ad_owner FOREIGN KEY (owner_id) REFERENCES user_account(id) ON DELETE CASCADE,
    CONSTRAINT fk_ad_car FOREIGN KEY (car_id) REFERENCES car(id) ON DELETE SET NULL
);

CREATE TABLE request (
    id BIGINT NOT NULL AUTO_INCREMENT,
    status VARCHAR(32) NOT NULL DEFAULT 'SENT',
    sending_date DATETIME NOT NULL,
    proposed_car_id BIGINT,
    receiver_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    deposit_sum INT NOT NULL DEFAULT 0,
    deposit_status VARCHAR(32) NOT NULL DEFAULT 'NONE',
    payment_method VARCHAR(64),
    PRIMARY KEY (id),
    CONSTRAINT fk_req_car FOREIGN KEY (proposed_car_id) REFERENCES car(id) ON DELETE SET NULL,
    CONSTRAINT fk_req_receiver FOREIGN KEY (receiver_id) REFERENCES user_account(id) ON DELETE CASCADE,
    CONSTRAINT fk_req_sender FOREIGN KEY (sender_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE TABLE office (
    id BIGINT NOT NULL AUTO_INCREMENT,
    address VARCHAR(255) NOT NULL,
    work_hours VARCHAR(255),
    accessability VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE visit_to_office (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    office_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_visit_user FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE,
    CONSTRAINT fk_visit_office FOREIGN KEY (office_id) REFERENCES office(id) ON DELETE CASCADE
);

CREATE TABLE car_photo (
    id BIGINT NOT NULL AUTO_INCREMENT,
    car_id BIGINT NOT NULL,
    stored_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    size_bytes BIGINT NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_car_photo_filename (stored_filename),
    INDEX idx_car_photo_car_order (car_id, sort_order),
    CONSTRAINT fk_car_photo_car FOREIGN KEY (car_id) REFERENCES car(id) ON DELETE CASCADE
);

INSERT INTO user_account (role, name, email, password, phone, passport, address, rating, account_status)
VALUES
('ADMIN', 'Administrator', 'admin@example.com', 'admin', '', '', '', 5, 'ACTIVE'),
('ROLE_AUTHORIZED', 'Demo User', 'demo@example.com', 'demo', '', '', '', 0, 'ACTIVE');

INSERT INTO office (address, work_hours, accessability)
VALUES ('Kharkiv, Main Office', '09:00-18:00', 'Accessible');

INSERT INTO car (vin_code, brand, model, car_year, mileage,vehicle_condition, owner_id, suspended_by_ban)
VALUES
('DEMO-VIN-00000001', 'Toyota', 'Corolla',
 2018, 82000, 'USED', 2, FALSE),
('DEMO-VIN-00000002', 'Volkswagen', 'Golf',
2020, 51000, 'USED', 2, FALSE);

INSERT INTO advertisement (description, creation_date, status, car_id, owner_id, suspended_by_ban)
VALUES
('Reliable daily driver. Looking for a similarly practical car.',
CURRENT_TIMESTAMP, 'Active', 1, 2, FALSE),
('Comfortable hatchback, regularly maintained. Open to reasonable exchange proposals.',
CURRENT_TIMESTAMP, 'Active', 2, 2, FALSE);

INSERT INTO car_photo (car_id, stored_filename, content_type, size_bytes, sort_order, uploaded_at)
VALUES
(1, '1cd48e91-c884-4df2-b7ae-f47db650373b.jpg', 'image/jpeg', 107344, 1, CURRENT_TIMESTAMP),
(2, '083bdc3f-0b4d-457b-b87f-667651e297bf.jpg', 'image/jpeg', 45420, 1, CURRENT_TIMESTAMP);
