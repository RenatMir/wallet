package com.renatmirzoev.wallet.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PlayerBalanceRepository {

    private static final String SQL_CREATE_BALANCE = """
        INSERT INTO player_balance (player_id)
        VALUES (:playerId)
        """;

    private static final String SQL_UPDATE_BALANCE = """
        UPDATE players
        SET balance = balance + :amount
        WHERE id = :playerId
        AND balance >= :amount
        """;

    private final DatabaseClient databaseClient;

    public Mono<Long> createBalance(long playerId) {
        return databaseClient.sql(SQL_CREATE_BALANCE)
            .bind("playerId", playerId)
            .fetch()
            .rowsUpdated();
    }

    public Mono<Long> updateBalance(long playerId, BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount must not be null");

        return databaseClient.sql(SQL_UPDATE_BALANCE)
            .bind("amount", amount)
            .bind("playerId", playerId)
            .fetch()
            .rowsUpdated();
    }
}
