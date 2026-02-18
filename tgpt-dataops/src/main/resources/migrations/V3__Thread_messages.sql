CREATE TYPE MESSAGE_ROLE AS ENUM ('system', 'user', 'assistant');
CREATE TYPE CONTENT_TYPE AS ENUM ('text', 'image_url', 'transcription');

CREATE TABLE THREAD_MESSAGE (
    id                  BIGSERIAL PRIMARY KEY,
    thread_id           BIGINT NOT NULL REFERENCES THREAD(id),
    role                MESSAGE_ROLE NOT NULL,
    content_type        CONTENT_TYPE NOT NULL DEFAULT 'text',
    content             TEXT NOT NULL,
    telegram_message_id INT,
    telegram_user_id    BIGINT,
    created             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_thread_msg_thread ON THREAD_MESSAGE (thread_id, created);
CREATE INDEX idx_thread_msg_tg ON THREAD_MESSAGE (thread_id, telegram_message_id);
