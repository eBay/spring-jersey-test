package org.example.mockjaxrs2.app1;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import mockjaxrs2.testapp.MockJerseyTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestMultipleResourceConfigs {

    @MockJerseyTest(application = GoodbyeResourceConfig.class)
    @Nested
    public class TestGoodbyeResource {

        @Autowired
        WebTarget target;

        @Test
        void shouldSayGoodbye() {
            String result = target.path("goodbye/greet").request().get(String.class);
            assertThat(result).isEqualTo("Goodbye, world :(");
        }

        @Test
        public void shouldNotSayHello() {
            Response response = target.path("hello/greet").request().get();
            Assertions.assertThat(response.getStatus()).isEqualTo(404);
        }
    }

    @MockJerseyTest(application = HelloResourceConfig.class)
    @Nested
    public class TestHelloResource {

        @Autowired
        WebTarget target;

        @Test
        public void shouldGetHello() {
            String response = target.path("hello/greet").request().get(String.class);
            Assertions.assertThat(response).isEqualTo("Hello, world!");
        }

        @Test
        public void shouldNotSayGoodbye() {
            Response response = target.path("goodbye/greet").request().get();
            Assertions.assertThat(response.getStatus()).isEqualTo(404);
        }
    }

    @MockJerseyTest(components = {HelloResource.class, GoodbyeResource.class})
    @Nested
    public class TestTwoResources {

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

    @MockJerseyTest(components = {HowYouDoinResource.class})
    @Nested
    public class TestNonComponentResource {

        @Autowired
        WebTarget target;

        @Test
        void shouldAskHowyoudoin() {
            String result = target.path("how/youdoin").request().get(String.class);
            assertThat(result).isEqualTo("Fine, thank you.");
        }
    }
}
