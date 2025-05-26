package com.renatmirzoev.wallet.service;

import com.renatmirzoev.wallet.exception.OutOfMoneyException;
import com.renatmirzoev.wallet.exception.PlayerAlreadyExistException;
import com.renatmirzoev.wallet.exception.PlayerNotFoundException;
import com.renatmirzoev.wallet.model.entity.Player;
import com.renatmirzoev.wallet.repository.cache.PlayerCacheRepository;
import com.renatmirzoev.wallet.repository.db.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PlayerService {

    private final PlayerCacheRepository playerCacheRepository;
    private final PlayerRepository playerRepository;

    public Optional<Player> getPlayerById(long playerId) {
        return playerCacheRepository.getById(playerId)
            .or(() -> playerRepository.getById(playerId));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public long createPlayer(Player player) {
        Optional<Player> playerOptional = playerRepository.getByEmail(player.getEmail());
        if (playerOptional.isPresent()) {
            throw new PlayerAlreadyExistException("Player already exists");
        }

        return playerRepository.save(player);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updatePlayerBalance(long playerId, BigDecimal balance) {
        Optional<Player> playerOptional = getPlayerById(playerId);
        if (playerOptional.isEmpty()) {
            throw new PlayerNotFoundException("Player %s not found".formatted(playerId));
        }

        int updated = playerRepository.updateBalance(playerId, balance);
        if (updated == 0) {
            throw new OutOfMoneyException("Player %s has no enough money".formatted(playerId));
        }
    }

}
