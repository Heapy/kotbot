ALTER TABLE CALLBACK_DATA
    DROP COLUMN TYPE;

ALTER TABLE CALLBACK_DATA
    ADD COLUMN CONSUMED BOOLEAN DEFAULT FALSE NOT NULL;
