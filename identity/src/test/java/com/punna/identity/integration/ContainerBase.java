package com.punna.identity.integration;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class ContainerBase {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres")
            .withExposedPorts(5432)
            .withDatabaseName("identity")
            .withUsername("identity")
            .withPassword("identity")
            .withReuse(true);
}
