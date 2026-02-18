CREATE TABLE THREAD (
    id                    BIGSERIAL PRIMARY KEY,
    chat_id               BIGINT NOT NULL,
    created_by            BIGINT NOT NULL,
    forked_from_thread_id BIGINT REFERENCES THREAD(id),
    forked_at_message_id  BIGINT,
    created               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_thread_chat ON THREAD (chat_id);
