package com.punna.eventcatalog.integration;

import com.punna.eventcatalog.TestConfigSettings;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class ContainerBase {

    static final GenericContainer<?> mongoContainer = new GenericContainer<>("mongo:latest")
            .withEnv(TestConfigSettings.MONGO_CONTAINER_USER.getKey(),
                    TestConfigSettings.MONGO_CONTAINER_USER.getValue()
                    )
            .withEnv(TestConfigSettings.MONGO_CONTAINER_PASSWORD.getKey(),
                    TestConfigSettings.MONGO_CONTAINER_PASSWORD.getValue()
                    )
            .withExposedPorts(27017)
            .withReuse(true)
            .waitingFor(Wait.forLogMessage(".*Waiting for connections.*", 2));


    static {
        mongoContainer.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.port", mongoContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", TestConfigSettings.MONGO_CONTAINER_USER::getValue);
        registry.add("spring.data.mongodb.password", TestConfigSettings.MONGO_CONTAINER_PASSWORD::getValue);
        registry.add("spring.data.mongodb.database", () -> TestConfigSettings.dbName);
        registry.add("spring.data.mongodb.authentication-database", () -> "admin");
    }

//    Reactive Mongo and TestContainers are not working well with @Container and having issues with stop
//    https://github.com/testcontainers/testcontainers-java/issues/4378
//    @AfterAll
//    static void cleanUp() {
//        mongoContainer.stop();
//    }

}
