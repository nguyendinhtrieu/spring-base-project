create table users
(
    id      int auto_increment,
    name    varchar(100) not null,
    company varchar(100) null,
    constraint product_pk
        primary key (id)
);
