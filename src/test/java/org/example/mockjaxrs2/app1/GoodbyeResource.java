package org.example.mockjaxrs2.app1;

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
