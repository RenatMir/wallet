package com.renatmirzoev.wallet.service;

import com.renatmirzoev.wallet.ModelUtils;
import com.renatmirzoev.wallet.exception.OutOfMoneyException;
import com.renatmirzoev.wallet.exception.PlayerAlreadyExistException;
import com.renatmirzoev.wallet.exception.PlayerNotFoundException;
import com.renatmirzoev.wallet.model.entity.Player;
import com.renatmirzoev.wallet.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;
    @InjectMocks
    private PlayerService playerService;

    private final Player player = ModelUtils.createPlayer();

    @Test
    void shouldCreatePlayer() {
        long expectedPlayerId = 1;

        when(playerRepository.save(any(Player.class))).thenReturn(expectedPlayerId);

        long playerId = playerService.createPlayer(player);

        assertThat(playerId).isEqualTo(expectedPlayerId);
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void shouldThrowPlayerAlreadyExistsExceptionOnCreatePlayer() {
        when(playerRepository.save(any(Player.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> playerService.createPlayer(player))
            .isInstanceOf(PlayerAlreadyExistException.class);

        verify(playerRepository).save(any(Player.class));
    }


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