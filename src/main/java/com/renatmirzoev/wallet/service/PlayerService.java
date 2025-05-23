package com.renatmirzoev.wallet.service;

import com.renatmirzoev.wallet.model.entity.Player;
import com.renatmirzoev.wallet.repository.PlayerBalanceRepository;
import com.renatmirzoev.wallet.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerBalanceRepository playerBalanceRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Mono<Long> createPlayer(Player player) {
        return playerRepository.save(player)
            .flatMap(savedPlayerId -> playerBalanceRepository.createBalance(savedPlayerId)
                .thenReturn(savedPlayerId));
    }
}
