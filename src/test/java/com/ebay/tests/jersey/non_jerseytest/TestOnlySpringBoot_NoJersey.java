package com.ebay.tests.jersey.non_jerseytest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * This test shows that it would not break any @SpringBootTest that are running while we have
 * SpringJerseyTest on the classpath
 */
@SpringBootTest(classes = ComputeResource.class)
public class TestOnlySpringBoot_NoJersey {

    @Autowired
    ComputeResource computeResource;

    @Test
    void testSpringBootTest() {
        assertThat(computeResource.computePayload()).isEqualTo("something");
    }
}
