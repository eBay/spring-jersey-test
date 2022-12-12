package com.ebay.tests.jersey.request_scope_injections;


import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.tests.jersey.SpringJerseyTest;
import com.ebay.tests.jersey.TestWebTarget;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRequestScopeInjection {
    private List<Object> list(Object... obj) {
        return Arrays.asList(obj);
    }

    @Nested
    @SpringJerseyTest
    class TestFoundResources {

        @Autowired
        TestWebTarget target;

        @Test
        void testSpringBootTest() {
            String res = target.path("compute/autowired").request().get(String.class);
            assertThat(res).isEqualTo("autowired GET");
        }

        @Test
        void testContextInjection() {
            String res = target.path("compute/context").request().get(String.class);
            assertThat(res).isEqualTo("context GET");
        }



        @Test
        void testFilterHttpReqInjection() {
            try (Response res = target.path("compute/context").queryParam("foo", "bar").request()
                .header("custom", "myheader").get()) {
                assertThat(res.getHeaders())
                    .containsEntry("echo", list("myheader"))
                    .containsEntry("query-string", list("foo=bar"))
                    .containsEntry("local-port", list("9998"))
                    .containsEntry("remote-port", list("9998"))
                    .containsEntry("remote-address", list("127.0.0.1"))
                    .containsEntry("remote-host", list("localhost"))
                    .containsEntry("local-host", list("localhost"))
                    .containsEntry("local-address", list("127.0.0.1"))
                    .containsEntry("path-info", list("compute/context"));
            }
        }
    }

    @Nested
    @SpringJerseyTest
    class TestNotFoundResource {

        @Autowired
        TestWebTarget target;

        @Test
        void testFilterHttpReqInjectionOnNotFound() {
            try (Response res = target.path("nothere").request().header("custom", "myheader")
                .get()) {
                assertThat(res.getHeaders())
                    .containsEntry("echo", list("myheader"));
            }
        }
    }


}
