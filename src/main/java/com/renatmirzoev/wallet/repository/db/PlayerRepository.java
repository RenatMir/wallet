package com.renatmirzoev.wallet.repository.db;

import com.renatmirzoev.wallet.model.entity.Player;
import com.renatmirzoev.wallet.utils.JdbcUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlayerRepository {

    private static final String SQL_SELECT_PLAYER_BY_ID = """
        SELECT * FROM players
        WHERE id = :id;
        """;

    private static final String SQL_SELECT_PLAYER_BY_EMAIL = """
        SELECT * FROM players
        WHERE email = :email;
        """;

    private static final String SQL_INSERT_PLAYER = """
        INSERT INTO players (full_name, email, phone_number)
        VALUES (:fullName, :email, :phoneNumber)
        RETURNING id;
        """;

    private static final String SQL_UPDATE_BALANCE = """
        UPDATE players
        SET balance = balance + :amount
        WHERE id = :playerId
        AND balance + :amount >= 0;
        """;

    private final JdbcClient jdbcClient;

    public Optional<Player> getById(long playerId) {
        return jdbcClient.sql(SQL_SELECT_PLAYER_BY_ID)
            .param("id", playerId)
            .query(ROW_MAPPER_PLAYER)
            .optional();
    }

    public Optional<Player> getByEmail(String email) {
        return jdbcClient.sql(SQL_SELECT_PLAYER_BY_EMAIL)
            .param("email", email)
            .query(ROW_MAPPER_PLAYER)
            .optional();
    }

    public long save(Player player) {
        return jdbcClient.sql(SQL_INSERT_PLAYER)
            .param("fullName", player.getFullName())
            .param("email", player.getEmail())
            .param("phoneNumber", player.getPhoneNumber())
            .query(Long.class)
            .single();
    }

    public int updateBalance(long playerId, BigDecimal amount) {
        return jdbcClient.sql(SQL_UPDATE_BALANCE)
            .param("playerId", playerId)
            .param("amount", amount)
            .update();
    }

    private static final RowMapper<Player> ROW_MAPPER_PLAYER = (rs, rowNum) ->
        new Player()
            .setId(rs.getInt("id"))
            .setFullName(rs.getString("full_name"))
            .setEmail(rs.getString("email"))
            .setPhoneNumber(rs.getString("phone_number"))
            .setBalance(rs.getBigDecimal("balance"))
            .setDateCreated(JdbcUtils.instantOrNull(rs.getTimestamp("date_created")));
}
