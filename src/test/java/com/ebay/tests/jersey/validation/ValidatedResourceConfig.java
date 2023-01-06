package com.ebay.tests.jersey.validation;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@ApplicationPath("/")
@Configuration
public class ValidatedResourceConfig extends ResourceConfig {

    public ValidatedResourceConfig() {
        register(ValidatedResource.class);
    }

}
