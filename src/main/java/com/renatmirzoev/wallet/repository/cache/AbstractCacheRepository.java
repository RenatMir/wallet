package com.renatmirzoev.wallet.repository.cache;

import com.renatmirzoev.wallet.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractCacheRepository {

    private static final String KEY_PREFIX = "Wallet:";

    private final StringRedisTemplate stringRedisTemplate;

    private static String key(String key) {
        return KEY_PREFIX + key;
    }

    protected Optional<String> get(String key) {
        String value = stringRedisTemplate.opsForValue().get(key(key));
        return Optional.ofNullable(value);
    }

    protected void save(String key, Object value) {
        stringRedisTemplate.opsForValue().set(key(key), JsonUtils.toJson(value));
    }
}
