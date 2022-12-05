package com.ebay.tests.jersey.exception_mappers;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@ApplicationPath("/")
@Configuration
public class ComputeResourceConfig extends ResourceConfig {

    public ComputeResourceConfig() {
        register(ComputeResource.class);
        register(AllExceptionMapper.class);
    }

}
