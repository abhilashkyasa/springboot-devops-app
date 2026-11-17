package com.abhi.devops;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

class HelloControllerTest {

    private final HelloController controller = new HelloController("1.2.3");

    @Test
    void helloReturnsRunningMessage() {
        assertTrue(controller.hello().contains("running successfully"));
    }

    @Test
    void versionReturnsInjectedVersion() {
        Map<String, String> body = controller.version();
        assertEquals("springboot-devops-app", body.get("app"));
        assertEquals("1.2.3", body.get("version"));
    }
}
