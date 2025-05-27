package com.renatmirzoev.wallet.service;

import com.renatmirzoev.wallet.model.entity.GameRound;
import com.renatmirzoev.wallet.model.entity.GameRoundAction;
import com.renatmirzoev.wallet.repository.db.GameRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GameRoundService {

    private final GameRoundRepository gameRoundRepository;
    private final PlayerService playerService;

    public Optional<GameRound> getByUuid(String uuid) {
        return gameRoundRepository.getGameRoundByUuid(uuid);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void processGameRound(GameRound gameRound) {
        Optional<GameRound> gameRoundOptional = getByUuid(gameRound.getUuid());
        if (gameRoundOptional.isPresent()) {
            // process existing round
            processExistingGameRound(gameRoundOptional.get(), gameRound);
        } else {
            //process new round
            processNewGameRound(gameRound);
        }

    }

    private void processExistingGameRound(GameRound existingGameRound, GameRound newGameRound) {
        if (existingGameRound.isClosed()) {
            //TODO: Another exception
            throw new RuntimeException("Game round has already been closed");
        }
        List<GameRoundAction> actions = newGameRound.getActions();

    }

    private void processNewGameRound(GameRound newGameRound) {
        List<GameRoundAction> actions = newGameRound.getActions();
        actions.forEach(action -> {
            BigDecimal amount = action.getAmount();
        });

        playerService.updatePlayerBalance(newGameRound.getPlayerId(), newGameRound.get);
    }
}
