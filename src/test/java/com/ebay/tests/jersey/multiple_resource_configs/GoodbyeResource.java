package com.ebay.tests.jersey.multiple_resource_configs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.stereotype.Component;

@Path("goodbye")
@Component
public class GoodbyeResource {

    @Path("/greet")
    @GET
    public String greet() {
        return "Goodbye, world :(";
    }
}
