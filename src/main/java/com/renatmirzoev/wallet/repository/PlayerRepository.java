package com.renatmirzoev.wallet.repository;

import com.renatmirzoev.wallet.model.entity.Player;
import io.r2dbc.spi.Parameter;
import io.r2dbc.spi.Parameters;
import io.r2dbc.spi.R2dbcType;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PlayerRepository {

    private static final String SQL_INSERT_PLAYER = """
        INSERT INTO players (full_name, email, phone_number)
        VALUES (:fullName, :email, :phoneNumber)
        RETURNING id;
        """;

    private final DatabaseClient databaseClient;

    public Mono<Long> save(Player player) {
        if (player == null) {
            return Mono.empty();
        }

        Map<String, Parameter> parameters = new HashMap<>();
        parameters.put("fullName", Parameters.in(R2dbcType.VARCHAR, player.getFullName()));
        parameters.put("email", Parameters.in(R2dbcType.VARCHAR, player.getEmail()));
        parameters.put("phoneNumber", Parameters.in(R2dbcType.VARCHAR, player.getPhoneNumber()));

        return databaseClient.sql(SQL_INSERT_PLAYER)
            .bindValues(parameters)
            .map(rs -> rs.get("id", Long.class))
            .one();
    }
}
