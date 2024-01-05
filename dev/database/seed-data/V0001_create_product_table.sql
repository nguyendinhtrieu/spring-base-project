create table product
(
    id      serial
        constraint product_pk
            primary key,
    name    varchar(100) not null,
    company varchar(100)
);
