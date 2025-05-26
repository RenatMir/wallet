package com.renatmirzoev.wallet.rest.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatePlayerResponse {
    private long playerId;
}
