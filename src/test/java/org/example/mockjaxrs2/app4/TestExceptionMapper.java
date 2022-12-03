package org.example.mockjaxrs2.app4;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import mockjaxrs2.testapp.MockJerseyTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestExceptionMapper {

    @MockJerseyTest
    @Nested
    @DisplayName("Exception Mapper auto discovery")
    public class TestExceptionMapperAutoRegister {

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

    @MockJerseyTest(ComputeResourceConfig.class)
    @Nested
    @DisplayName("Exception Mapper application based discovery")
    public class TestExceptionMapperApplicationRegister {

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

    @MockJerseyTest(components = {ComputeResource.class, AllExceptionMapper.class})
    @Nested
    @DisplayName("Exception Mapper explicit discovery")
    public class TestExceptionMapperExplicitDiscovery {

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

//    @MockJerseyTest(components = {ComputeResource.class})
//    @Nested
//    @DisplayName("Exception Mapper not discovered")
//    public class TestExceptionMapperShouldntDiscover {
//
//        @Autowired
//        WebTarget target;
//
//        @Test
//        void shouldNotMapException() {
//            assertThatThrownBy(() -> target.path("/compute/err").request().get())
//                .isInstanceOf(ProcessingException.class)
//                .hasRootCauseMessage("problem!");
//        }
//    }

}
