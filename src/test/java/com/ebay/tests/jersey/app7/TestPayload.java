package com.ebay.tests.jersey.app7;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ebay.tests.jersey.SpringJerseyTest;
import com.ebay.tests.jersey.TestWebTarget;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringJerseyTest(enable = TestProperties.LOG_TRAFFIC)
public class TestPayload {

    @Autowired
    TestWebTarget target;

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
