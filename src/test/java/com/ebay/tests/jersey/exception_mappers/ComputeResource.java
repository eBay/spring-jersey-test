package com.ebay.tests.jersey.exception_mappers;

import java.io.IOException;
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

    @Path("/ioerr")
    @GET
    public String doIOErr() throws IOException {
        throw new IOException("IO problem!");
    }
}
