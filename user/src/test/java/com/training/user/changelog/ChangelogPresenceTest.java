package com.training.user.changelog;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChangelogPresenceTest {

    @Test
    void shouldContainChangelogFileAtMicroserviceRoot() {
        InputStream changelog = getClass().getClassLoader().getResourceAsStream("../../CHANGELOG.md");
        assertNotNull(changelog);
    }
}
