package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.service.ridematcher.IRideMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MatchControllerTest {

    @InjectMocks
    private MatchController sut;

    @Mock
    private IRideMatcher rideMatcher;

    @Test
    void updateMatchingCallsMethodInService() {
        // Test
        sut.updateMatching();

        // Verify
        verify(rideMatcher).updateMatching();
    }

    @Test
    void updateMatchingReturnsOkResponse() {
        Response actualResponse = sut.updateMatching();

        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());
    }
}