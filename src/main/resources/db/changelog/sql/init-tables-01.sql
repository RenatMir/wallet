CREATE TABLE players
(
    id           SERIAL PRIMARY KEY,
    full_name    VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255),
    date_created timestamptz  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE player_balance
(
    player_id    INTEGER PRIMARY KEY REFERENCES players (id),
    amount       NUMERIC(12, 2) NOT NULL DEFAULT 0,
    date_created timestamptz    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions
(
    id           SERIAL PRIMARY KEY,
    player_id    INTEGER        NOT NULL REFERENCES players (id),
    amount       NUMERIC(12, 2) NOT NULL,
    date_created timestamptz    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_transactions_player_id ON transactions (player_id);
