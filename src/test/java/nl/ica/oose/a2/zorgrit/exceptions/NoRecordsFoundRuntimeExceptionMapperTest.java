package nl.ica.oose.a2.zorgrit.exceptions;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NoRecordsFoundRuntimeExceptionMapperTest {
    private NoRecordsFoundExceptionRuntimeMapper sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void exceptionMapperReturnsMessage() {
        sut = new NoRecordsFoundExceptionRuntimeMapper();
        NoRecordsFoundRuntimeException exception = new NoRecordsFoundRuntimeException("message");

        Response actualResponse = sut.toResponse(exception);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), actualResponse.getStatus());
        assertEquals("message", exception.getMessage());
    }
}
