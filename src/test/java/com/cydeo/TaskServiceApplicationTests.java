package com.cydeo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Hafif doğrulama; tam {@code @SpringBootTest} Config Server, Eureka ve veritabanı gerektirir.
 */
class TaskServiceApplicationTests {

    @Test
    void applicationClassIsLoadable() {
        assertNotNull(TaskServiceApplication.class);
    }

}
