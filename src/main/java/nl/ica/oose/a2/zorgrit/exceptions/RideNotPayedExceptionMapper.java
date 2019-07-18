package nl.ica.oose.a2.zorgrit.exceptions;

import nl.ica.oose.a2.zorgrit.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class RideNotPayedExceptionMapper implements ExceptionMapper<RideNotPayedException> {

    @Override
    public Response toResponse(RideNotPayedException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorDTO(exception.getMessage()))
                .build();
    }
}
