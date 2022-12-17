package com.ebay.tests.jersey.spring_security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ebay.tests.jersey.SpringJerseyTest;
import com.ebay.tests.jersey.TestWebTarget;
import javax.ws.rs.ProcessingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

public class TestSecurity {


    // this config should only use the Spring proxies
    @Nested
    @SpringJerseyTest
    public class TestSecureResourceFromApplicationConfig extends AbsSecurityTest {

    }


    // this config should make sure to not register the same resource twice - once for the proxy and
    // once for the raw component
    @Nested
    @SpringJerseyTest(components = ComputeResource.class)
    public class TestSecureResourceFromComponent extends AbsSecurityTest {

    }

    // Run the same suite twice, with different configurations
    static abstract class AbsSecurityTest {

        @Autowired
        TestWebTarget target;

        @Test
        @WithMockUser(username = "admin", authorities = {
            "https://api.ebay.com/oauth/scope/@public"})
        void shouldWorkWithAuthority() {
            String response = target.path("compute/secured").request().get(String.class);
            assertThat(response).isEqualTo("secret");
        }

        @Test
        @WithMockUser(username = "admin", authorities = {
            "https://api.ebay.com/oauth/scope/@private"})
        void shouldRejectWrongAuthority() {
            assertThatThrownBy(() -> target.path("compute/secured").request().get(String.class))
                .isInstanceOf(ProcessingException.class)
                .hasRootCauseMessage("Access Denied");
        }

        @Test
        @WithAnonymousUser
        void shouldRejectWithAnnonymousUser() {
            assertThatThrownBy(() -> target.path("compute/secured").request().get(String.class))
                .isInstanceOf(ProcessingException.class)
                .hasRootCauseMessage("Access Denied");
        }

        @Test
        void shouldRejectWithNoSecureContext() {
            assertThatThrownBy(() -> target.path("compute/secured").request().get(String.class))
                .isInstanceOf(ProcessingException.class)
                .hasRootCauseMessage(
                    "An Authentication object was not found in the SecurityContext");
        }
    }
}
