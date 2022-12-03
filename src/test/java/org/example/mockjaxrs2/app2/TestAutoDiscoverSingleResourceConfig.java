package org.example.mockjaxrs2.app2;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import javax.ws.rs.client.WebTarget;
import mockjaxrs2.testapp.MockJerseyTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MockJerseyTest
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
