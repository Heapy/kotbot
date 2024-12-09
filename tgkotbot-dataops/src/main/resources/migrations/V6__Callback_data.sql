CREATE TABLE CALLBACK_DATA
(
    id   UUID DEFAULT UUID_GENERATE_V4() PRIMARY KEY,
    data JSONB not null,
    type VARCHAR(255) not null,
    created TIMESTAMP not null
);
