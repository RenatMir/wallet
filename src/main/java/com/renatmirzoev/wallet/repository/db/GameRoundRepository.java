package com.renatmirzoev.wallet.repository.db;

import com.renatmirzoev.wallet.model.entity.GameRound;
import com.renatmirzoev.wallet.model.entity.GameRoundAction;
import com.renatmirzoev.wallet.model.entity.GameRoundActionType;
import com.renatmirzoev.wallet.utils.JdbcUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GameRoundRepository {

    private static final String SQL_INSERT_GAME_ROUND = """
        INSERT INTO game_rounds (uuid, player_id, closed)
        VALUES (:uuid, :playerId, :closed)
        RETURNING id;
        """;

    private static final String SQL_INSERT_GAME_ROUND_ACTION = """
        INSERT INTO game_round_actions (uuid, type, game_round_uuid, amount)
        VALUES (:uuid, :type, :gameRoundUuid, :amount)
        """;

    private static final String SQL_SELECT_GAME_ROUND_BY_UUID = """
        SELECT
        gr.id as game_round_id,
        gr.uuid as game_round_uuid,
        gr.player_id,
        gr.closed as game_round_closed,
        gr.date_created as game_round_date_created,
        gra.id as game_round_action_id,
        gra.uuid as game_round_action_uuid,
        gra.type as game_round_action_type,
        gra.amount as game_round_action_amount,
        gra.date_created as game_round_action_date_created
        FROM game_rounds as gr
        LEFT JOIN game_round_actions as gra
        on gr.uuid = gra.game_round_uuid
        WHERE gr.uuid = :uuid
        ORDER BY gra.id
        """;

    private final JdbcClient jdbcClient;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<GameRound> getGameRoundByUuid(String uuid) {
        return jdbcClient.sql(SQL_SELECT_GAME_ROUND_BY_UUID)
            .param("uuid", uuid)
            .query(RESULT_SET_EXTRACTOR_GAME_ROUND);
    }

    public long save(GameRound gameRound) {
        return jdbcClient.sql(SQL_INSERT_GAME_ROUND)
            .param("uuid", gameRound.getUuid())
            .param("playerId", gameRound.getPlayerId())
            .param("closed", gameRound.isClosed())
            .query(Long.class)
            .single();
    }

    public int[] batchInsert(List<GameRoundAction> actions) {
        Map<String, Object>[] params = actions.stream()
            .map(action -> Map.<String, Object>ofEntries(
                Map.entry("uuid", action.getUuid()),
                Map.entry("type", action.getType().name()),
                Map.entry("gameRoundUuid", action.getGameRoundUuid()),
                Map.entry("amount", action.getAmount())
            ))
            .<Map<String, Object>>toArray(Map[]::new);
        return namedParameterJdbcTemplate.batchUpdate(SQL_INSERT_GAME_ROUND_ACTION, params);
    }

    private static final ResultSetExtractor<Optional<GameRound>> RESULT_SET_EXTRACTOR_GAME_ROUND = rs -> {
        GameRound gameRound = null;
        while (rs.next()) {
            if (rs.getRow() == 1) {
                gameRound = new GameRound()
                    .setId(rs.getLong("game_round_id"))
                    .setUuid(rs.getString("game_round_uuid"))
                    .setPlayerId(rs.getLong("player_id"))
                    .setClosed(rs.getBoolean("game_round_closed"))
                    .setDateCreated(JdbcUtils.instantOrNull(rs.getTimestamp("game_round_date_created")))
                    .setActions(new ArrayList<>());
            }

            long actionId = rs.getLong("game_round_action_id");
            if (!rs.wasNull() && gameRound != null) {
                GameRoundAction gameRoundAction = new GameRoundAction()
                    .setId(actionId)
                    .setUuid(rs.getString("game_round_action_uuid"))
                    .setType(GameRoundActionType.fromString(rs.getString("game_round_action_type")))
                    .setGameRoundUuid(gameRound.getUuid())
                    .setAmount(rs.getBigDecimal("game_round_action_amount"))
                    .setDateCreated(JdbcUtils.instantOrNull(rs.getTimestamp("game_round_action_date_created")));

                gameRound.getActions().add(gameRoundAction);
            }
        }
        return Optional.ofNullable(gameRound);
    };

}
