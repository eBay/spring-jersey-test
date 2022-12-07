package com.ebay.tests.jersey.auto_discovery_providers;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.tests.jersey.SpringJerseyTest;
import com.ebay.tests.jersey.TestWebTarget;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringJerseyTest
public class TestAutoDiscoverResourcesAndProvidersOnly {

    @Autowired
    TestWebTarget target;

    @Test
    void shouldMapException() {
        try (Response response = target.path("/compute/err").request().get()) {
            assertThat(response.getStatus()).isEqualTo(500);
            String entity = response.readEntity(String.class);
            assertThat(entity).isEqualTo("err: problem!");
        }
    }
}
