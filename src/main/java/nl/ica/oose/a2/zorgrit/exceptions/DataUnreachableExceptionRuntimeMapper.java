package nl.ica.oose.a2.zorgrit.exceptions;

import nl.ica.oose.a2.zorgrit.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DataUnreachableExceptionRuntimeMapper implements ExceptionMapper<DataUnreachableRuntimeException> {
    @Override
    public Response toResponse(DataUnreachableRuntimeException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorDTO(exception.getMessage()))
                .build();
    }
}
