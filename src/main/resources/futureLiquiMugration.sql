CREATE TABLE users (
    id bigserial PRIMARY KEY,
    username varchar(100) NOT NULL,
    password varchar(100) NOT NULL
)