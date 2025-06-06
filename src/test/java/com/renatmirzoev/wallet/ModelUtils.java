package com.renatmirzoev.wallet;

import com.renatmirzoev.wallet.model.entity.GameRound;
import com.renatmirzoev.wallet.model.entity.GameRoundAction;
import com.renatmirzoev.wallet.model.entity.Player;
import com.renatmirzoev.wallet.rest.model.CreatePlayerRequest;
import org.instancio.Instancio;

import java.math.BigDecimal;

import static org.instancio.Select.field;

public class ModelUtils {

    private ModelUtils() {
    }

    public static CreatePlayerRequest createPlayerRequest() {
        return Instancio.of(CreatePlayerRequest.class)
            .generate(field(CreatePlayerRequest::getEmail), gen -> gen.net().email())
            .create();
    }

    public static Player createPlayer() {
        return Instancio.of(Player.class)
            .generate(field(Player::getEmail), gen -> gen.net().email())
            .set(field(Player::getBalance), BigDecimal.ZERO)
            .ignore(field(Player::getId))
            .ignore(field(Player::getDateCreated))
            .create();
    }

    public static GameRound createGameRound() {
        GameRound gameRound = Instancio.of(GameRound.class)
            .ignore(field(GameRound::getId))
            .ignore(field(GameRound::getDateCreated))
            .ignore(field(GameRoundAction::getId))
            .ignore(field(GameRoundAction::getDateCreated))
            .create();

        gameRound.getActions()
            .forEach(action -> {
                action.setGameRoundUuid(gameRound.getUuid());
            });

        return gameRound;
    }
}
