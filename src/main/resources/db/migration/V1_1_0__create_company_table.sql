create table company
(
    id         serial
        constraint company_pk primary key,
    name       varchar(100) not null,
    note       varchar(500),
    created_at timestamp    not null default current_timestamp,
    updated_at timestamp    not null default current_timestamp
);
