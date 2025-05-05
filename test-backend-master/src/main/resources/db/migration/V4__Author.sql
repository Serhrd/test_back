create table if not exists Author
(
    id serial primary key,
    fullName text not null,
    created_at DATE
);