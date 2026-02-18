CREATE TABLE API_CALL (
    id                 BIGSERIAL PRIMARY KEY,
    thread_id          BIGINT NOT NULL REFERENCES THREAD(id),
    telegram_user_id   BIGINT NOT NULL,
    model              VARCHAR(100) NOT NULL,
    prompt_tokens      INT NOT NULL,
    completion_tokens  INT NOT NULL,
    total_tokens       INT NOT NULL,
    estimated_cost_usd NUMERIC(10,6) NOT NULL,
    created            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_api_call_user ON API_CALL (telegram_user_id, created);
