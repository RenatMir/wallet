package com.renatmirzoev.wallet.repository.db;

import com.renatmirzoev.wallet.AbstractIntegrationTest;
import com.renatmirzoev.wallet.ModelUtils;
import com.renatmirzoev.wallet.model.entity.GameRound;
import com.renatmirzoev.wallet.model.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GameRoundRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRoundRepository gameRoundRepository;

    @Test
    void shouldSaveGameRoundWithoutActions() {
        Player player = ModelUtils.createPlayer();
        long playerId = playerRepository.save(player);
        GameRound gameRound = ModelUtils.createGameRound();
        gameRound.setPlayerId(playerId);
        gameRound.setActions(Collections.emptyList());

        gameRoundRepository.save(gameRound);
        Optional<GameRound> getGameRound = gameRoundRepository.getGameRoundByUuid(gameRound.getUuid());

        assertThat(getGameRound).isPresent();
        assertThat(getGameRound.get())
            .usingRecursiveComparison()
            .ignoringFields("id", "dateCreated")
            .isEqualTo(gameRound);
    }

    @Test
    void shouldSaveGameRoundWithActions() {
        Player player = ModelUtils.createPlayer();
        long playerId = playerRepository.save(player);

        GameRound gameRound = ModelUtils.createGameRound();
        gameRound.setPlayerId(playerId);

        gameRoundRepository.save(gameRound);
        gameRoundRepository.batchInsert(gameRound.getActions());
        Optional<GameRound> getGameRound = gameRoundRepository.getGameRoundByUuid(gameRound.getUuid());

        assertThat(getGameRound).isPresent();
        assertThat(getGameRound.get())
            .usingRecursiveComparison()
            .ignoringFields("id", "dateCreated", "actions")
            .isEqualTo(gameRound);

        assertThat(getGameRound.get().getActions())
            .usingRecursiveComparison()
            .ignoringFields("id", "dateCreated")
            .isEqualTo(gameRound.getActions());
    }
}