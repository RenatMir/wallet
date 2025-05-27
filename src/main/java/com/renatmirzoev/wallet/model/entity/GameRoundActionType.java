package com.renatmirzoev.wallet.model.entity;

import org.springframework.util.StringUtils;

public enum GameRoundActionType {
    WAGER,
    WIN,
    CLOSE;

    public static GameRoundActionType fromString(String transactionType) {
        if (!StringUtils.hasText(transactionType)) {
            return null;
        }

        return GameRoundActionType.valueOf(transactionType);
    }
}
