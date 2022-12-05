package com.ebay.tests.jersey.auto_discovery_providers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.stereotype.Controller;

@Path("/compute")
@Controller
public class ComputeResource {

    @Path("/err")
    @GET
    public String doErr() {
        throw new RuntimeException("problem!");
    }
}
