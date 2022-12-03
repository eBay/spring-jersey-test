package org.example.mockjaxrs2.app7;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import mockjaxrs2.testapp.MockJerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MockJerseyTest(enable = TestProperties.LOG_TRAFFIC)
public class TestPayload {

    @Autowired
    WebTarget target;

    @Test
    void shouldSendAndReceivePayloadJson() {
        Req req = new Req("my-input-json");
        Res res = target.path("compute/payload")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(req, MediaType.APPLICATION_JSON_TYPE), Res.class);
        assertThat(res.getOutput()).isEqualTo("Output for my-input-json");
    }

    @Test
    void shouldRejectXmlPayload() {
        assertThatThrownBy(() -> target.path("compute/payload")
            .request(MediaType.APPLICATION_XML_TYPE)
            .post(Entity.entity("", MediaType.APPLICATION_XML_TYPE), Res.class))
            .isInstanceOf(NotSupportedException.class);
    }

}
