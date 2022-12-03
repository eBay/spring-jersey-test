package org.example.mockjaxrs2.app3;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.stereotype.Controller;

@Path("/compute")
@Controller
public class ComputeResource {

    private final ExpensiveService expensiveService;

    public ComputeResource(ExpensiveService expensiveService) {
        this.expensiveService = expensiveService;
    }


    @Path("/expensive")
    @GET
    public String doExpensiveStuff() {
        return expensiveService.computeSomethingExpensive();
    }
}
