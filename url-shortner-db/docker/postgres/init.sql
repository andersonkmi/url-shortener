CREATE TABLE url (
    url_id bigint not null primary key,
    url varchar not null unique,
    short_url varchar not null
);

CREATE SEQUENCE url_id_sequence;