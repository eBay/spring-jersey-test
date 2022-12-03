package org.example.mockjaxrs2.app1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("how")
public class HowYouDoinResource {

    @Path("youdoin")
    @GET
    public String howyoudoing() {
        return "Fine, thank you.";
    }
}
