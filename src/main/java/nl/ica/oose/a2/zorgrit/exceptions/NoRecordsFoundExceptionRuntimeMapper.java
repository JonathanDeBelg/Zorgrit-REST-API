package nl.ica.oose.a2.zorgrit.exceptions;

import nl.ica.oose.a2.zorgrit.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class NoRecordsFoundExceptionRuntimeMapper implements ExceptionMapper<NoRecordsFoundRuntimeException> {
    @Override
    public Response toResponse(NoRecordsFoundRuntimeException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorDTO(e.getMessage()))
                .build();
    }
}
