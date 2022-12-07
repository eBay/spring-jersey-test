package com.ebay.tests.jersey.multiple_resource_configs;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.tests.jersey.SpringJerseyTest;
import com.ebay.tests.jersey.TestWebTarget;
import javax.ws.rs.core.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestMultipleResourceConfigs {

    @SpringJerseyTest(application = GoodbyeResourceConfig.class)
    @Nested
    public class TestGoodbyeResource {

        @Autowired
        TestWebTarget target;

        @Test
        void shouldSayGoodbye() {
            String result = target.path("goodbye/greet").request().get(String.class);
            assertThat(result).isEqualTo("Goodbye, world :(");
        }

        @Test
        public void shouldNotSayHello() {
            try (Response response = target.path("hello/greet").request().get()) {
                Assertions.assertThat(response.getStatus()).isEqualTo(404);
            }
        }
    }

    @SpringJerseyTest(application = HelloResourceConfig.class)
    @Nested
    public class TestHelloResource {

        @Autowired
        TestWebTarget target;

        @Test
        public void shouldGetHello() {
            String response = target.path("hello/greet").request().get(String.class);
            Assertions.assertThat(response).isEqualTo("Hello, world!");
        }

        @Test
        public void shouldNotSayGoodbye() {
            try (Response response = target.path("goodbye/greet").request().get()) {
                Assertions.assertThat(response.getStatus()).isEqualTo(404);
            }
        }
    }

    @SpringJerseyTest(components = {HelloResource.class, GoodbyeResource.class})
    @Nested
    public class TestTwoResources {

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
            Assertions.assertThat(response).isEqualTo("Hello, world!");
        }
    }

    @SpringJerseyTest(components = {HowYouDoinResource.class})
    @Nested
    public class TestNonComponentResource {

        @Autowired
        TestWebTarget target;

        @Test
        void shouldAskHowyoudoin() {
            String result = target.path("how/youdoin").request().get(String.class);
            assertThat(result).isEqualTo("Fine, thank you.");
        }
    }
}
