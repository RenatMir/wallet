package com.renatmirzoev.wallet.repository.cache;

import com.renatmirzoev.wallet.AbstractIntegrationTest;
import com.renatmirzoev.wallet.ModelUtils;
import com.renatmirzoev.wallet.model.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerCacheRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PlayerCacheRepository playerCacheRepository;

    private final Player player = ModelUtils.createPlayer();

    @Test
    void shouldGetOptionalEmptyById() {
        Optional<Player> playerOptional = playerCacheRepository.getById(Long.MAX_VALUE);
        assertThat(playerOptional).isEmpty();
    }

    @Test
    void shouldSaveAndGetPlayerById() {
        long id = 1000;
        player.setId(id);

        playerCacheRepository.save(player);

        Optional<Player> playerOptional = playerCacheRepository.getById(id);
        assertThat(playerOptional).isPresent().contains(player);
    }
}