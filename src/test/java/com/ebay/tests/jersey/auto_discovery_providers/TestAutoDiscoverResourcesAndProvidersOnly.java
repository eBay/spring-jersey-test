package com.ebay.tests.jersey.auto_discovery_providers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ebay.tests.jersey.SpringJerseyTest;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringJerseyTest
public class TestAutoDiscoverResourcesAndProvidersOnly {

    @Autowired
    WebTarget target;

    @Test
    void shouldMapException() {
        Response response = target.path("/compute/err").request().get();
        assertThat(response.getStatus()).isEqualTo(500);
        String entity = response.readEntity(String.class);
        assertThat(entity).isEqualTo("err: problem!");
    }
}
