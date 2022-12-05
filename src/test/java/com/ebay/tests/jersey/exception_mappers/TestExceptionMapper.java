package com.ebay.tests.jersey.exception_mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.ebay.tests.jersey.SpringJerseyTest;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestExceptionMapper {

    @SpringJerseyTest
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

    @SpringJerseyTest(ComputeResourceConfig.class)
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

    @SpringJerseyTest(components = {ComputeResource.class, AllExceptionMapper.class})
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

//    @SpringJerseyTest(components = {ComputeResource.class})
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
