package com.renatmirzoev.wallet.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Accessors(chain = true)
public class Player {
    private long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private BigDecimal balance;
    private Instant dateCreated;
}
