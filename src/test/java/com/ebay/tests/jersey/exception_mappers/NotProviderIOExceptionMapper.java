package com.ebay.tests.jersey.exception_mappers;

import java.io.IOException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

// Not a Provider mapped
@Component
public class NotProviderIOExceptionMapper implements ExceptionMapper<IOException> {

    @Override
    public Response toResponse(IOException exception) {
        return Response.serverError().entity("io err: "+exception.getMessage()).build();
    }
}
