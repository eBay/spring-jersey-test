package org.example.mockjaxrs2.non_jerseytest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.stereotype.Controller;

@Path("/compute")
@Controller
public class ComputeResource {


    @Path("/something")
    @GET
    public String computePayload() {
        return "something";
    }
}
