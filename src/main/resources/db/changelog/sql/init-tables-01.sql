CREATE TABLE players
(
    id           SERIAL PRIMARY KEY,
    full_name    VARCHAR(255)   NOT NULL,
    email        VARCHAR(255)   NOT NULL UNIQUE,
    phone_number VARCHAR(255),
    balance      NUMERIC(12, 2) NOT NULL DEFAULT 0,
    date_created timestamptz    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_players_email ON players (email);

CREATE TABLE game_rounds
(
    id           SERIAL PRIMARY KEY,
    uuid         VARCHAR(128) NOT NULL,
    player_id    INTEGER      NOT NULL REFERENCES players (id),
    closed       BOOLEAN      NOT NULL DEFAULT FALSE,
    date_created timestamptz  NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_game_rounds_uuid ON game_rounds (uuid);
CREATE INDEX idx_game_rounds_player_id ON game_rounds (player_id);
CREATE INDEX idx_game_rounds_closed ON game_rounds (closed);

CREATE TABLE game_round_actions
(
    id              SERIAL PRIMARY KEY,
    uuid            VARCHAR(128)   NOT NULL,
    type            VARCHAR(128)   NOT NULL,
    game_round_uuid VARCHAR(128)   NOT NULL,
    amount          NUMERIC(12, 2) NOT NULL,
    date_created    timestamptz    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_game_round_actions_uuid ON game_round_actions (uuid);
CREATE INDEX idx_game_round_actions_type ON game_round_actions (type);
CREATE INDEX idx_game_round_actions_game_round_uuid ON game_round_actions (game_round_uuid);
