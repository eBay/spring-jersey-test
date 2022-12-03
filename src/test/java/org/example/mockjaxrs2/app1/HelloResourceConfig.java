package org.example.mockjaxrs2.app1;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@ApplicationPath("/v1")
@Configuration
public class HelloResourceConfig extends ResourceConfig {

    @PostConstruct
    public void init() {
        register(HelloResource.class);
    }
}
