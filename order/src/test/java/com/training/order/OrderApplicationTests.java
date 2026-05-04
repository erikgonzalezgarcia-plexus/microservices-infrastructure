package com.training.order;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void pomContainsSpotlessPluginConfiguration() throws IOException {
        String pomContent = Files.readString(Path.of("pom.xml"));

        assertTrue(pomContent.contains("spotless-maven-plugin"));
        assertTrue(pomContent.contains("<goal>check</goal>"));
    }
}
