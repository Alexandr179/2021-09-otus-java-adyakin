-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
-- create sequence hibernate_sequence start with 1 increment by 1;

create table clients
(
    id bigserial not null primary key,
    name varchar(50),
    address_id bigint default null
);

create table addresses
(
    id bigserial not null primary key,
    street varchar(50)
);

create table phones
(
    id bigserial not null primary key,
    number varchar(50),
    client_id bigint,
    FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE
);