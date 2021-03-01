CREATE TABLE IF NOT EXISTS users
(
    username varchar(255),
    password varchar(255),
    enabled  boolean
);

CREATE TABLE IF NOT EXISTS authorities
(
    username varchar(255),
    authority varchar(255)
)
