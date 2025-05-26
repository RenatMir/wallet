package com.renatmirzoev.wallet.repository;

import com.renatmirzoev.wallet.AbstractIntegrationTest;
import com.renatmirzoev.wallet.ModelUtils;
import com.renatmirzoev.wallet.model.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;

    private final Player player = ModelUtils.createPlayer();

    @Test
    void shouldReturnEmptyOptionalWhenPlayerIsEmptyById() {
        Optional<Player> playerOptional = playerRepository.getById(Long.MAX_VALUE);
        assertThat(playerOptional).isEmpty();
    }

    @Test
    void shouldReturnEmptyOptionalWhenPlayerIsEmptyByEmail() {
        Optional<Player> playerOptional = playerRepository.getByEmail(UUID.randomUUID().toString());
        assertThat(playerOptional).isEmpty();
    }

    @Test
    void shouldSavePlayerAndGet() {
        long playerId = playerRepository.save(player);

        Optional<Player> playerOptionalById = playerRepository.getById(playerId);
        assertThat(playerOptionalById).isPresent();

        Optional<Player> playerOptionalByEmail = playerRepository.getByEmail(player.getEmail());
        assertThat(playerOptionalByEmail).isPresent();
    }

    @Test
    void shouldIncreasePlayerBalance() {
        BigDecimal updateBalance = BigDecimal.valueOf(100);

        long playerId = playerRepository.save(player);

        int rowsAffected = playerRepository.updateBalance(playerId, updateBalance);
        assertThat(rowsAffected).isEqualTo(1);

        Optional<Player> playerOptionalById = playerRepository.getById(playerId);
        assertThat(playerOptionalById).isPresent();

        Player getPlayerById = playerOptionalById.get();
        assertThat(getPlayerById.getBalance()).isEqualByComparingTo(updateBalance);
    }

    @Test
    void shouldNotDecreaseMoreThanPlayerBalance() {
        BigDecimal updateBalance = BigDecimal.valueOf(-100);

        long playerId = playerRepository.save(player);

        int rowsAffected = playerRepository.updateBalance(playerId, updateBalance);
        assertThat(rowsAffected).isZero();

        Optional<Player> playerOptionalById = playerRepository.getById(playerId);
        assertThat(playerOptionalById).isPresent();

        Player getPlayerById = playerOptionalById.get();
        assertThat(getPlayerById.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}