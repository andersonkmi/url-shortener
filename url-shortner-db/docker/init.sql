CREATE SEQUENCE urlid_seq;

CREATE TABLE url (
    urlid bigint not null primary key default nextval('urlid_seq'),
    url varchar not null unique,
    short_url varchar not null
);

CREATE SEQUENCE url_id_sequence
CACHE 1000
;