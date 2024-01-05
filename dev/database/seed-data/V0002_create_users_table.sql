create table users
(
    id      serial
        constraint users_pk
            primary key,
    name    varchar(100) not null,
    company varchar(100)
);
