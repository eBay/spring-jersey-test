package org.example.mockjaxrs2.app4;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

@Provider
@Component
public class AllExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        return Response.serverError().entity("err: "+exception.getMessage()).build();
    }
}
