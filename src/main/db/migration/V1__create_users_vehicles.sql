-- create Users table and Vehicles table

CREATE TABLE IF NOT EXISTS Users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Vehicles (
    id SERIAL PRIMARY KEY,
    make VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year VARCHAR(4) NOT NULL,
    submodel VARCHAR(100),
    city_mpg DOUBLE PRECISION,
    owner_id INTEGER REFERENCES Users(id) ON DELETE CASCADE
);