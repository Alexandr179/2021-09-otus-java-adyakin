-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
-- create sequence hibernate_sequence start with 1 increment by 1;

create table users
(
    id bigserial not null primary key,
    name varchar(50),
    login varchar(50),
    password varchar(50)
);
