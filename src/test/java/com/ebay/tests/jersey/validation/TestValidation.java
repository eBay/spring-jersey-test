package com.ebay.tests.jersey.validation;


import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.tests.jersey.SpringJerseyTest;
import com.ebay.tests.jersey.TestWebTarget;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringJerseyTest
public class TestValidation {

    @Autowired
    TestWebTarget target;

    @Test
    void shouldValidateParameter() {
        try (Response response = call(3)) {
            assertResponse(response, 200, "Value is 3");
        }
    }

    @Test
    void shouldRejectTooLow() {
        try (Response response = call(1)) {
            assertResponse(response, 400, "call.value: must be greater than or equal to 2");
        }
    }

    @Test
    void shouldRejectTooHigh() {
        try (Response response = call(5)) {
            assertResponse(response, 400, "call.value: must be less than or equal to 4");
        }
    }

    private Response call(int value) {
        return target.path("validate/call")
            .queryParam("value", value)
            .request()
            .get();
    }

    private void assertResponse(Response response, int status, String message) {
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.readEntity(String.class)).isEqualTo(message);
    }

}
