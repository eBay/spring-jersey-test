package org.example.mockjaxrs2.non_jerseytest;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@ApplicationPath("/")
@Configuration
public class ComputeResourceConfig extends ResourceConfig {

    public ComputeResourceConfig() {
        register(ComputeResource.class);
    }

}
