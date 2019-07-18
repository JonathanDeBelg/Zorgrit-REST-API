package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.RideRating;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;
import nl.ica.oose.a2.zorgrit.service.IRideService;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class RideControllerTest {

    @Mock
    private IRideService rideService;

    @InjectMocks
    private RideController sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void deleteRideRatingThrowsException() throws DataUnreachableException {
        int rideId = 1;

        doThrow(DataUnreachableException.class).when(rideService).deleteRideRating(rideId);

        // Test
        Response actualResult = sut.deleteRideRating(rideId);

        // Verify
        assertThat(actualResult.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void deleteRideRatingCallsService() throws DataUnreachableException {
        int rideId = 1;
        // Test
        sut.deleteRideRating(rideId);

        // Verify
        verify(rideService).deleteRideRating(rideId);
    }

    @Test
    public void putRideRatingThrowsException() throws DataUnreachableException {
        int rideId = 1;
        RideRating rideRating = new RideRating();
        doThrow(DataUnreachableException.class).when(rideService).putRideRating(rideId, rideRating);

        // Test
        Response actualResult = sut.putRideRating(rideId, rideRating);

        // Verify
        assertThat(actualResult.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void putRideRatingCallsService() throws DataUnreachableException {
        int rideId = 1;
        RideRating rideRating = new RideRating();
        // Test
        sut.putRideRating(rideId, rideRating);

        // Verify
        verify(rideService).putRideRating(rideId, rideRating);
    }
    
    @Test
    public void getAllRatingsByDriverThrowsException() throws DataUnreachableException {
        int rideId = 1;
        when(rideService.getAllRatingsByDriver(rideId)).thenThrow(DataUnreachableException.class);
 
        // Test
        Response actualResult = sut.getAllRatingsByDriver(rideId);

        // Verify
        assertThat(actualResult.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void getAllRatingsByDriverCallsService() throws DataUnreachableException {
        int driverId = 1;
        // Test
        sut.getAllRatingsByDriver(driverId);

        // Verify
        verify(rideService).getAllRatingsByDriver(driverId);
    }

    @Test
    public void addRideRatingThrowsException() throws DataUnreachableException {
        int rideId = 1;
        RideRating rideRating = new RideRating();

        doThrow(DataUnreachableException.class).when(rideService).addRideRating(rideId, rideRating);

        // Test
        Response actualResult = sut.addRideRating(rideId, rideRating);

        // Verify
        assertThat(actualResult.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void addRideRatingCallsService() throws DataUnreachableException {
        int rideId = 1;
        RideRating rideRating = new RideRating();
        // Test
        sut.addRideRating(rideId, rideRating);

        // Verify
        verify(rideService).addRideRating(rideId, rideRating);
    }

    @Test
    public void getRideRatingThrowsException() throws DataUnreachableException {
        int rideId = 1;
        when(rideService.getRideRating(rideId)).thenThrow(DataUnreachableException.class);

        // Test
        Response actualResult = sut.getRideRating(rideId);

        // Verify
        assertThat(actualResult.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void getRideRatingCallsService() throws DataUnreachableException {
        int rideId = 1;
        // Test
        sut.getRideRating(rideId);

        // Verify
        verify(rideService).getRideRating(rideId);
    }

    @Test
    public void postStartRideThrowsException() throws DataUnreachableException {
        int rideId = 1;
        doThrow(DataUnreachableException.class).when(rideService).startRide(rideId);
        // Test1
        Response actualResult = sut.postStartRide(rideId);

        // Verify
        assertThat(actualResult.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void postStartRideCallsService() throws DataUnreachableException {
        int rideId = 1;
        // Test
        sut.postStartRide(rideId);

        // Verify
        verify(rideService).startRide(rideId);
    }

    @Test
    public void getCurrentRideThrowsException() throws DataUnreachableException {
        int rideId = 1;
        when(rideService.getCurrentRide(rideId)).thenThrow(DataUnreachableException.class);

        // Test
        Response actualResult = sut.getCurrentRide(rideId);

        // Verify
        assertThat(actualResult.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void getCurrentRideCallsService() throws DataUnreachableException {
        int clientId = 1;
        // Test
        sut.getCurrentRide(clientId);

        // Verify
        verify(rideService).getCurrentRide(clientId);
    }

    @Test
    public void getAllRidesInfoCallsService() {
        // Test
        sut.getAllRidesInfo(1);

        // Verify
        verify(rideService).getAllRidesInfo(1);
    }

    @Test
    public void getAllNotMatchedRidesCallsService() {
        // Test
        sut.getAllNotMatchedRides(1);

        // Verify
        verify(rideService).getAllNotMatchedRides(1);
    }

    @Test
    public void getAllMatchedRidesCallsService() {
        // Test
        sut.getAllMatchedRides(1);

        // Verify
        verify(rideService).getAllMatchedRides(1);
    }

    @Test
    public void testGetRide() throws DataUnreachableException {
        //Setup
        int rideId = 2;

        // Test
        sut.getRide(rideId);

        // Verify
        verify(rideService).getRide(rideId);
    }

    @Test
    public void testGetRideResponseOk() throws DataUnreachableException {
        //Setup
        int rideId = 1;

        //Test
        Response response = sut.getRide(rideId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testGetRideResponseEntity() throws DataUnreachableException {
        //Setup
        int rideId = 1;
        RideDTO ride = mock(RideDTO.class);
        when(rideService.getRide(rideId)).thenReturn(ride);

        // Test
        Response response = sut.getRide(rideId);

        // Verify
        assertEquals(response.getEntity(), ride);
    }

    @Test
    public void testGetRideThrowDataUnreachableException_ResponseCodeInternalServerError() throws DataUnreachableException {
        int rideId = 1;
        RideDTO ride = mock(RideDTO.class);
        when(rideService.getRide(rideId)).thenThrow(DataUnreachableException.class);

        // Test
        Response response = sut.getRide(rideId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testUpdateRide() throws DataUnreachableException, InvalidDateFormatException {
        // Setup
        int rideId = 1;
        RideDTO ride = new RideDTO();

        //Test
        sut.updateRide(rideId, ride);

        //Verify
        verify(rideService).updateRide(ride);
    }

    @Test
    public void testStatusErrorUpdateRideDataUnreachableException() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        int rideId = 1;
        RideDTO ride = new RideDTO();
        doThrow(DataUnreachableException.class).when(rideService).updateRide(ride);

        //Test
        Response response = sut.updateRide(rideId, ride);

        //Verify
        MatcherAssert.assertThat(response.getStatus(), equalTo(500));
    }

    @Test
    public void testStatusErrorUpdateRidInvalidDateFormatException() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        RideDTO ride = new RideDTO();
        doThrow(InvalidDateFormatException.class).when(rideService).updateRide(ride);

        //Test
        Response response = sut.updateRide(1, ride);

        //Verify
        MatcherAssert.assertThat(response.getStatus(), equalTo(500));
    }

    @Test
    public void testStatusOkUpdateRide(){
        //Setup
        int rideId = 1;
        RideDTO ride = new RideDTO();

        //Test
        Response response = sut.updateRide(rideId, ride);

        //Verify
        MatcherAssert.assertThat(response.getStatus(), equalTo(200));
    }

    @Test
    public void getAllRidesCallsService() {
        // Test
        sut.getAllRides();

        // Verify
        verify(rideService).getAllRides();
    }

    @Test
    public void getAllRidesTownshipCallsService() {
        // Test
        sut.getAllRidesTownship("Nijmegen");

        // Verify
        verify(rideService).getAllRidesTownship("Nijmegen");
    }

    @Test
    public void getAllRidesCareInstitutionCallsService() {
        // Test
        sut.getAllRidesCareInstitution(1);

        // Verify
        verify(rideService).getAllRidesCareInstitution(1);
    }

    @Test
    public void getRideToMathCallsService() {
        //Test
        sut.getRideToMatch(1);

        //Verify
        verify(rideService).getRideToMatch(1);
    }

    @Test
    public void matchRideToDriverCallsService() {
        //Test
        sut.matchRideToDriver(1, 1);

        //Verify
        verify(rideService).matchRideToDriver(1, 1);
    }
}
