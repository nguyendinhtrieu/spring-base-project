create table product
(
    id         serial
        constraint product_pk
            primary key,
    name       varchar(100)                        not null,
    company_id integer                             not null
        constraint product_company_id_fk
            references company,
    note       varchar(500),
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null
);
