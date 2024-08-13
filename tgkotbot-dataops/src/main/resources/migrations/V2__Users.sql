CREATE TYPE TELEGRAM_USER_STATUS AS ENUM ('UNAPPROVED', 'APPROVED', 'BANNED');
CREATE TYPE TELEGRAM_USER_ROLE AS ENUM ('USER', 'MODERATOR', 'ADMIN');

CREATE TABLE TELEGRAM_USER
(
    internal_id   BIGSERIAL            NOT NULL,
    telegram_id   BIGINT               NOT NULL,
    created       TIMESTAMP            NOT NULL default CURRENT_TIMESTAMP,
    last_message  TIMESTAMP            NOT NULL default CURRENT_TIMESTAMP,
    message_count INT                  NOT NULL default 0,
    status        TELEGRAM_USER_STATUS NOT NULL default 'UNAPPROVED',
    role          TELEGRAM_USER_ROLE   NOT NULL default 'USER',
    badge         VARCHAR(255)         NULL,
    version       INT                  NOT NULL default 0,
    CONSTRAINT TELEGRAM_USER_PK PRIMARY KEY (internal_id)
);
