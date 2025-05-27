package com.renatmirzoev.wallet.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Accessors(chain = true)
public class GameRoundAction {
    private long id;
    private String uuid;
    private String gameRoundUuid;
    private GameRoundActionType type;
    private BigDecimal amount;
    private Instant dateCreated;
}
