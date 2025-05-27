package com.renatmirzoev.wallet.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@Accessors(chain = true)
public class GameRound {
    private long id;
    private String uuid;
    private long playerId;
    private boolean closed;
    private List<GameRoundAction> actions;
    private Instant dateCreated;
}
