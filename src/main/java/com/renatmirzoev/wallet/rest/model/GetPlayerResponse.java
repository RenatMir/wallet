package com.renatmirzoev.wallet.rest.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class GetPlayerResponse {
    private long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private BigDecimal balance;
}
