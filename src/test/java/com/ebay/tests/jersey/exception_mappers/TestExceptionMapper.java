package com.ebay.tests.jersey.exception_mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ebay.tests.jersey.SpringJerseyTest;
import com.ebay.tests.jersey.TestWebTarget;
import java.io.IOException;
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
        TestWebTarget target;

        @Test
        void shouldMapException() {
            try (Response response = target.path("/compute/err").request().get()) {
                assertThat(response.getStatus()).isEqualTo(500);
                String entity = response.readEntity(String.class);
                assertThat(entity).isEqualTo("runtime err: problem!");
            }
        }

        @Test
        void shouldNotMapNonProviderIOException() {
            assertThatThrownBy(()->target.path("/compute/ioerr").request().get())
                .hasRootCauseExactlyInstanceOf(IOException.class)
                .hasRootCauseMessage("IO problem!");
        }
    }

    @SpringJerseyTest(ComputeResourceConfig.class)
    @Nested
    @DisplayName("Exception Mapper application based discovery")
    public class TestExceptionMapperApplicationRegister {

        @Autowired
        TestWebTarget target;

        @Test
        void shouldMapRuntimeException() {
            try (Response response = target.path("/compute/err").request().get()) {
                assertThat(response.getStatus()).isEqualTo(500);
                String entity = response.readEntity(String.class);
                assertThat(entity).isEqualTo("runtime err: problem!");
            }
        }
    }

    @SpringJerseyTest(components = {ComputeResource.class, ProviderRuntimeExceptionMapper.class, NotProviderIOExceptionMapper.class})
    @Nested
    @DisplayName("Exception Mapper explicit discovery")
    public class TestExceptionMapperExplicitDiscovery {

        @Autowired
        TestWebTarget target;

        @Test
        void shouldMapException() {
            try (Response response = target.path("/compute/err").request().get()) {
                assertThat(response.getStatus()).isEqualTo(500);
                String entity = response.readEntity(String.class);
                assertThat(entity).isEqualTo("runtime err: problem!");
            }
        }

        @Test
        void shouldNonProviderMapException() {
            try (Response response = target.path("/compute/ioerr").request().get()) {
                assertThat(response.getStatus()).isEqualTo(500);
                String entity = response.readEntity(String.class);
                assertThat(entity).isEqualTo("io err: IO problem!");
            }
        }
    }


//    @SpringJerseyTest(components = {ComputeResource.class})
//    @Nested
//    @DisplayName("Exception Mapper not discovered")
//    public class TestExceptionMapperShouldntDiscover {
//
//        @Autowired
//        TestWebTarget target;
//
//        @Test
//        void shouldNotMapException() {
//            assertThatThrownBy(() -> target.path("/compute/err").request().get())
//                .isInstanceOf(ProcessingException.class)
//                .hasRootCauseMessage("problem!");
//        }
//    }

}
