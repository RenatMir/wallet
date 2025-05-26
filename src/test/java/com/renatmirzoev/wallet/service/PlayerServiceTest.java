package com.renatmirzoev.wallet.service;

import com.renatmirzoev.wallet.ModelUtils;
import com.renatmirzoev.wallet.exception.OutOfMoneyException;
import com.renatmirzoev.wallet.exception.PlayerAlreadyExistException;
import com.renatmirzoev.wallet.exception.PlayerNotFoundException;
import com.renatmirzoev.wallet.model.entity.Player;
import com.renatmirzoev.wallet.repository.cache.PlayerCacheRepository;
import com.renatmirzoev.wallet.repository.db.PlayerRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private PlayerCacheRepository playerCacheRepository;
    @InjectMocks
    private PlayerService playerService;

    private final Player player = ModelUtils.createPlayer();

    @Nested
    class GetPlayerById {

        @Test
        void shouldGetPlayerFromCache() {
            when(playerCacheRepository.getById(anyLong())).thenReturn(Optional.of(player));

            Optional<Player> playerOptional = playerService.getPlayerById(Long.MAX_VALUE);
            assertThat(playerOptional).isPresent().contains(player);

            verify(playerCacheRepository).getById(anyLong());
            verifyNoInteractions(playerRepository);
        }

        @Test
        void shouldGetPlayerFromDb() {
            when(playerCacheRepository.getById(anyLong())).thenReturn(Optional.empty());
            when(playerRepository.getById(anyLong())).thenReturn(Optional.of(player));

            Optional<Player> playerOptional = playerService.getPlayerById(Long.MAX_VALUE);
            assertThat(playerOptional).isPresent().contains(player);

            InOrder inOrder = inOrder(playerRepository, playerCacheRepository);
            inOrder.verify(playerCacheRepository).getById(anyLong());
            inOrder.verify(playerRepository).getById(anyLong());
        }

        @Test
        void shouldGetOptionalEmptyFromDb() {
            when(playerCacheRepository.getById(anyLong())).thenReturn(Optional.empty());
            when(playerRepository.getById(anyLong())).thenReturn(Optional.empty());

            Optional<Player> playerOptional = playerService.getPlayerById(Long.MAX_VALUE);
            assertThat(playerOptional).isEmpty();

            InOrder inOrder = inOrder(playerRepository, playerCacheRepository);
            inOrder.verify(playerCacheRepository).getById(anyLong());
            inOrder.verify(playerRepository).getById(anyLong());
        }
    }

    @Nested
    class CreatePlayer {

        @Test
        void shouldCreatePlayer() {
            long expectedPlayerId = 1;

            when(playerRepository.save(any(Player.class))).thenReturn(expectedPlayerId);
            when(playerRepository.getByEmail(anyString())).thenReturn(Optional.empty());

            long playerId = playerService.createPlayer(player);

            assertThat(playerId).isEqualTo(expectedPlayerId);
            verify(playerRepository).getByEmail(anyString());
            verify(playerRepository).save(any(Player.class));
        }

        @Test
        void shouldThrowPlayerAlreadyExistsExceptionOnCreatePlayer() {
            when(playerRepository.getByEmail(anyString())).thenReturn(Optional.of(player));

            assertThatThrownBy(() -> playerService.createPlayer(player))
                .isInstanceOf(PlayerAlreadyExistException.class);

            verify(playerRepository).getByEmail(anyString());
        }
    }

    @Nested
    class UpdatePlayerBalance {

        @Test
        void shouldUpdatePlayerBalance() {
            when(playerRepository.getById(anyLong())).thenReturn(Optional.of(player));
            when(playerRepository.updateBalance(anyLong(), any(BigDecimal.class))).thenReturn(1);

            playerService.updatePlayerBalance(1, BigDecimal.TEN);

            verify(playerRepository).getById(anyLong());
            verify(playerRepository).updateBalance(anyLong(), any(BigDecimal.class));
        }

        @Test
        void shouldThrowPlayerNotFoundExceptionOnUpdatePlayerBalance() {
            when(playerRepository.getById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> playerService.updatePlayerBalance(1, BigDecimal.TEN))
                .isInstanceOf(PlayerNotFoundException.class);

            verify(playerRepository).getById(anyLong());

        }

        @Test
        void shouldThrowOutOfMoneyExceptionOnUpdatePlayerBalance() {
            when(playerRepository.getById(anyLong())).thenReturn(Optional.of(player));
            when(playerRepository.updateBalance(anyLong(), any(BigDecimal.class))).thenReturn(0);

            assertThatThrownBy(() -> playerService.updatePlayerBalance(1, BigDecimal.TEN))
                .isInstanceOf(OutOfMoneyException.class);

            verify(playerRepository).getById(anyLong());
            verify(playerRepository).updateBalance(anyLong(), any(BigDecimal.class));
        }
    }
}