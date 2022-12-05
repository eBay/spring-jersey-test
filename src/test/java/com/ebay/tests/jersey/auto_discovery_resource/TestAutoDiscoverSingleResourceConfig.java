package com.ebay.tests.jersey.auto_discovery_resource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ebay.tests.jersey.SpringJerseyTest;
import javax.ws.rs.client.WebTarget;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringJerseyTest
public class TestAutoDiscoverSingleResourceConfig {

    @Autowired
    WebTarget target;

    @Test
    void shouldSayHello() {
        String result = target.path("goodbye/greet").request().get(String.class);
        assertThat(result).isEqualTo("Goodbye, world :(");
    }

    @Test
    public void shouldGetHello() {
        String response = target.path("hello/greet").request().get(String.class);
        Assertions.assertThat(response).isEqualTo("Hello, world!");
    }
}
