CREATE TYPE JOIN_SESSION_STATUS AS ENUM ('ACTIVE', 'PASSED', 'FAILED', 'EXPIRED');
CREATE TYPE VERIFICATION_SOURCE AS ENUM ('CHALLENGE', 'EXISTING_MEMBER', 'MANUAL');

-- Verified users (common scope, one row per user)
CREATE TABLE VERIFIED_USER (
    id          BIGSERIAL PRIMARY KEY,
    telegram_id BIGINT NOT NULL,
    source      VERIFICATION_SOURCE NOT NULL,
    verified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    session_id  BIGINT NULL
);
CREATE UNIQUE INDEX VERIFIED_USER_TELEGRAM_ID_UINDEX ON VERIFIED_USER (telegram_id);

-- Join challenge sessions (current challenge denormalized inline)
CREATE TABLE JOIN_SESSION (
    id                BIGSERIAL PRIMARY KEY,
    telegram_id       BIGINT NOT NULL,
    chat_id           BIGINT NOT NULL,
    user_chat_id      BIGINT NOT NULL,
    status            JOIN_SESSION_STATUS NOT NULL DEFAULT 'ACTIVE',
    max_attempts      INT NOT NULL,
    attempts_used     INT NOT NULL DEFAULT 0,
    expires_at        TIMESTAMP NOT NULL,
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at       TIMESTAMP NULL,
    challenge_id      UUID NULL,
    template_key      VARCHAR(50) NULL,
    seed              BIGINT NULL,
    snippet           TEXT NULL,
    correct_answer    TEXT NULL,
    options           JSONB NULL,
    challenge_sent_at TIMESTAMP NULL,
    message_id        INT NULL
);
CREATE INDEX JOIN_SESSION_ACTIVE_IDX ON JOIN_SESSION (status, expires_at) WHERE status = 'ACTIVE';
CREATE UNIQUE INDEX JOIN_SESSION_ACTIVE_USER_CHAT_UINDEX ON JOIN_SESSION (telegram_id, chat_id) WHERE status = 'ACTIVE';

-- Attempt log (append-only, for analytics)
CREATE TABLE CHALLENGE_ATTEMPT (
    id                BIGSERIAL PRIMARY KEY,
    session_id        BIGINT NOT NULL REFERENCES JOIN_SESSION(id),
    telegram_id       BIGINT NOT NULL,
    challenge_id      UUID NOT NULL,
    template_key      VARCHAR(50) NOT NULL,
    selected_answer   TEXT NOT NULL,
    correct_answer    TEXT NOT NULL,
    is_correct        BOOLEAN NOT NULL,
    challenge_sent_at TIMESTAMP NOT NULL,
    answered_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    latency_ms        BIGINT NOT NULL
);
CREATE INDEX CHALLENGE_ATTEMPT_SESSION_IDX ON CHALLENGE_ATTEMPT (session_id);
CREATE INDEX CHALLENGE_ATTEMPT_TELEGRAM_IDX ON CHALLENGE_ATTEMPT (telegram_id);
