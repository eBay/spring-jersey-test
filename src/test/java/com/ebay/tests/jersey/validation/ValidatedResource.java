package com.ebay.tests.jersey.validation;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.springframework.stereotype.Controller;

@Path("/validate")
@Controller
@Valid
public class ValidatedResource {

    @Path("/call")
    @GET
    public String call(
        @Min(2)
        @Max(4)
        @QueryParam("value")
        int value) {

        return "Value is " + value;
    }

}
