package com.ebay.tests.jersey.auto_discovery_resource;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.tests.jersey.SpringJerseyTest;
import com.ebay.tests.jersey.TestWebTarget;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringJerseyTest
public class TestAutoDiscoverSingleResourceConfig {

    @Autowired
    TestWebTarget target;

    @Test
    void shouldSayHello() {
        String result = target.path("goodbye/greet").request().get(String.class);
        assertThat(result).isEqualTo("Goodbye, world :(");
    }

    @Test
    public void shouldGetHello() {
        String response = target.path("hello/greet").request().get(String.class);
        assertThat(response).isEqualTo("Hello, world!");
    }

    @Test
    public void shouldHaveBothRegistered() {
        assertThat(target.application().getClasses())
            .containsExactlyInAnyOrder(HelloResource.class, GoodbyeResource.class);
    }
}
