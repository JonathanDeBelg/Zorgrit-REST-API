package nl.ica.oose.a2.zorgrit.exceptions;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

public class RideNotPayedExceptionMapperTest {
    private RideNotPayedExceptionMapper sut;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void exceptionMapperReturnsMessage() {
        sut = new RideNotPayedExceptionMapper();
        RideNotPayedException exception = new RideNotPayedException("message");

        Response actualResponse = sut.toResponse(exception);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), actualResponse.getStatus());
        assertEquals("message", exception.getMessage());
    }
}