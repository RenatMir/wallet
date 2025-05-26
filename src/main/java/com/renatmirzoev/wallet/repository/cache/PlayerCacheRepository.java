package com.renatmirzoev.wallet.repository.cache;

import com.renatmirzoev.wallet.model.entity.Player;
import com.renatmirzoev.wallet.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class PlayerCacheRepository extends AbstractCacheRepository {

    private static final String KEY = "player:";

    static String key(long playerId) {
        return KEY + playerId;
    }

    public PlayerCacheRepository(StringRedisTemplate stringRedisTemplate) {
        super(stringRedisTemplate);
    }

    public void save(Player player) {
        save(key(player.getId()), player);
    }

    public Optional<Player> getById(long playerId) {
        return get(key(playerId))
            .map(value -> JsonUtils.fromJson(value, Player.class));
    }
}
