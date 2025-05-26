package com.renatmirzoev.wallet.rest.controller;

import com.renatmirzoev.wallet.mapper.PlayerMapper;
import com.renatmirzoev.wallet.model.entity.Player;
import com.renatmirzoev.wallet.rest.model.CreatePlayerRequest;
import com.renatmirzoev.wallet.rest.model.CreatePlayerResponse;
import com.renatmirzoev.wallet.rest.model.GetPlayerResponse;
import com.renatmirzoev.wallet.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    @GetMapping("/{id}")
    public ResponseEntity<GetPlayerResponse> getPlayer(@PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        GetPlayerResponse getPlayerResponse = playerMapper.toGetPlayerResponse(player);
        return ResponseEntity.ok(getPlayerResponse);
    }

    @PostMapping
    public ResponseEntity<CreatePlayerResponse> createPlayer(@Valid @RequestBody CreatePlayerRequest request) {
        Player player = playerMapper.fromCreateRequest(request);
        long playerId = playerService.createPlayer(player);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(playerId)
            .toUri();

        CreatePlayerResponse response = new CreatePlayerResponse()
            .setPlayerId(playerId);

        return ResponseEntity.created(location)
            .body(response);
    }
}
