package com.renatmirzoev.wallet;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@Slf4j
public class TestEnvironment {

    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = createPostgresContainer();
        POSTGRES_CONTAINER.start();

        log.info("Postgres started at host={} port={}", POSTGRES_CONTAINER.getHost(), POSTGRES_CONTAINER.getFirstMappedPort());

    }

    public static String postgresUrl() {
        return String.format("jdbc:postgresql://%s:%s/wallet",
            POSTGRES_CONTAINER.getHost(),
            POSTGRES_CONTAINER.getFirstMappedPort().toString()
        );
    }

    @SuppressWarnings("resource")
    private static PostgreSQLContainer<?> createPostgresContainer() {
        return new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("wallet")
            .withUsername("wallet")
            .withPassword("wallet")
            .waitingFor(Wait.forListeningPort());
    }
}
