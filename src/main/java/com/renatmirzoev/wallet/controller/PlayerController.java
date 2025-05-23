package com.renatmirzoev.wallet.controller;

import com.renatmirzoev.wallet.mapper.PlayerMapper;
import com.renatmirzoev.wallet.model.entity.Player;
import com.renatmirzoev.wallet.model.rest.CreatePlayerRequest;
import com.renatmirzoev.wallet.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    @PostMapping
    public ResponseEntity<Mono<Long>> createPlayer(@Valid @RequestBody CreatePlayerRequest request) {
        Player player = playerMapper.fromCreateRequest(request);
        Mono<Long> playerId = playerService.createPlayer(player);
        return ResponseEntity.ok(playerId);
    }
}
