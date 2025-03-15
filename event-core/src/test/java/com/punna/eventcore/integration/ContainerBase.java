package com.punna.eventcore.integration;

import com.punna.eventcore.TestConfigSettings;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@EmbeddedKafka
public abstract class ContainerBase {

  static final GenericContainer<?> mongoContainer = new GenericContainer<>("mongo:latest")
      .withExposedPorts(27017)
      .withCommand("--replSet", "rs0", "--bind_ip_all", "--port", "27017")
      .withReuse(true)
      .withStartupAttempts(3);


  static {
    mongoContainer.start();
    waitForReplicaSetSetup();
  }

  private static void waitForReplicaSetSetup() {
    long timeout = System.currentTimeMillis() + 60000; // wait up to 60 seconds
    while (System.currentTimeMillis() < timeout) {
      try {
        // Execute the command to get the replica set status.
        // We use mongosh with --quiet so only the output is returned.
        Container.ExecResult result = mongoContainer.execInContainer(
            "mongosh", "--quiet", "--eval", "try { rs.status().ok } catch(e){ 0 }"
        );
        if ("1".equals(result.getStdout().trim())) {
          System.out.println("Replication config state is Steady");
          return;
        } else {
          mongoContainer.execInContainer("mongosh", "--eval",
              "rs.initiate({_id:'rs0',members:[{_id:0,host:'localhost:27017'}]})");
        }
      } catch (Exception e) {
        // If an error occurs, it may be that MongoDB isn't ready yet.
      }
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Interrupted while waiting for replica set initialization", e);
      }
    }
    throw new RuntimeException("Timed out waiting for MongoDB replica set to initialize");

  }

  @DynamicPropertySource
  static void dynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.port", mongoContainer::getFirstMappedPort);
    registry.add("spring.data.mongodb.database", () -> TestConfigSettings.dbName);
  }


}
