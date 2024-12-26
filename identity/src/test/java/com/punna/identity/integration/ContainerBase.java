package com.punna.identity.integration;

import com.punna.identity.TestConfigSettings;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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


    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource:url",
//                () -> String.format("jdbc:postgresql://localhost:%s/%s",
//                        postgreSQLContainer.getFirstMappedPort(),
//                        TestConfigSettings.POSTGRES_DATABASE));
//        registry.add("spring.datasource.username", () -> TestConfigSettings.POSTGRES_USER);
//        registry.add("spring.datasource.password", () -> TestConfigSettings.POSTGRES_PASSWORD);
    }

}
