package com.ebay.tests.jersey.exception_mappers;

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
