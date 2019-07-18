package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.service.IDriverService;
import nl.ica.oose.a2.zorgrit.service.IUserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class DriverControllerTest {
    @Mock
    private IDriverService driverService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private DriverController driverController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllFutureRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;

        //Test
        driverController.getAllFutureRides(driverId);

        //Verify
        verify(driverService).getAllFutureRides(driverId);
    }

    @Test
    public void testResponseStatusOkOfGetAllFutureRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;

        //Test
        Response response = driverController.getAllFutureRides(driverId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testResponseEntityOfGetAllFutureRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();
        RideDTO ride = mock(RideDTO.class);
        rides.add(ride);
        when(driverService.getAllFutureRides(driverId)).thenReturn(rides);

        //Test
        Response response = driverController.getAllFutureRides(driverId);

        //Verify
        assertEquals(response.getEntity(), rides);
    }

    @Test
    public void testResponseInternalServerErrorOfGetAllFutureRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();
        RideDTO ride = mock(RideDTO.class);
        rides.add(ride);
        when(driverService.getAllFutureRides(driverId)).thenThrow(DataUnreachableException.class);

        //Test
        Response response = driverController.getAllFutureRides(driverId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testGetAllDrivenRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;

        //Test
        driverController.getAllDrivenRides(driverId);

        //Verify
        verify(driverService).getAllCompletedRides(driverId);
    }

    @Test
    public void testResponseStatusOkOfGetAllDrivenRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;

        //Test
        Response response = driverController.getAllDrivenRides(driverId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testResponseEntityOfGetAllDrivenRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();
        RideDTO ride = mock(RideDTO.class);
        rides.add(ride);
        when(driverService.getAllCompletedRides(driverId)).thenReturn(rides);

        //Test
        Response response = driverController.getAllDrivenRides(driverId);

        //Verify
        assertEquals(response.getEntity(), rides);
    }

    @Test
    public void testResponseInternalServerErrorOfGetAllDrivenRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();
        RideDTO ride = mock(RideDTO.class);
        rides.add(ride);
        when(driverService.getAllCompletedRides(driverId)).thenThrow(DataUnreachableException.class);

        //Test
        Response response = driverController.getAllDrivenRides(driverId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testGetAllRequestRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;

        //Test
        driverController.getAllRequestRides(driverId);

        //Verify
        verify(driverService).getAllRequestRides(driverId);
    }

    @Test
    public void testResponseStatusOkOfGetRequestRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;

        //Test
        Response response = driverController.getAllRequestRides(driverId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testResponseEntityOfGetAllRequestRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();
        RideDTO ride = mock(RideDTO.class);
        rides.add(ride);
        when(driverService.getAllRequestRides(driverId)).thenReturn(rides);

        //Test
        Response response = driverController.getAllRequestRides(driverId);

        //Verify
        assertEquals(response.getEntity(), rides);
    }

    @Test
    public void testResponseInternalServerErrorOfGetAllRequestRides() throws DataUnreachableException {
        //Setup
        int rideId = -1;
        doThrow(DataUnreachableException.class).when(driverService).getAllRequestRides(rideId);

        //Test
        Response response = driverController.getAllRequestRides(rideId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testGetAllCancelledRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;

        //Test
        driverController.getAllCancelledRides(driverId);

        //Verify
        verify(driverService).getAllCancelledRides(driverId);
    }

    @Test
    public void testResponseStatusOkOfGetCancelledRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;

        //Test
        Response response = driverController.getAllCancelledRides(driverId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testResponseEntityOfGetAllCancelledRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();
        RideDTO ride = mock(RideDTO.class);
        rides.add(ride);
        when(driverService.getAllCancelledRides(driverId)).thenReturn(rides);

        //Test
        Response response = driverController.getAllCancelledRides(driverId);

        //Verify
        assertEquals(response.getEntity(), rides);
    }

    @Test
    public void testResponseInternalServerErrorOfGetAllCancelledRides() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();
        RideDTO ride = mock(RideDTO.class);
        rides.add(ride);
        when(driverService.getAllCancelledRides(driverId)).thenThrow(DataUnreachableException.class);

        //Test
        Response response = driverController.getAllCancelledRides(driverId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testgetAcceptRide() throws DataUnreachableException {
        //Setup
        int userId = 1;
        int rideId = 3;

        // Test
        driverController.acceptRide(userId, rideId);

        // Verify
        verify(driverService).acceptRide(userId, rideId);
    }

    @Test
    public void testResponseStatusOkOfAcceptRides() throws DataUnreachableException {
        //Setup
        int userId = 1;
        int rideId = 3;

        //Test
        Response response = driverController.acceptRide(userId, rideId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testResponseInternalServerErrorOfAcceptRide() throws DataUnreachableException {
        //Setup
        int userId = -15;
        int rideId = 3;
        doThrow(DataUnreachableException.class).when(driverService).acceptRide(userId, rideId);

        //Test
        Response response = driverController.acceptRide(userId, rideId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testCancelRideByDriver() throws DataUnreachableException {
        //Setup
        int rideId = 1;
        int driverId = 3;

        //Test
        driverController.cancelOneRideByDriver(driverId, rideId);

        //Verify
        verify(driverService).cancelRide(driverId, rideId);
    }

    @Test
    public void testCancelRideResponseOk() throws DataUnreachableException {
        //Setup
        int userId = 1;
        int rideId = 3;

        // Test
        Response response = driverController.cancelOneRideByDriver(userId, rideId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testResponseInternalServerErrorCancelRide() throws DataUnreachableException {
        //Setup
        int userId = -15;
        int rideId = 3;
        doThrow(DataUnreachableException.class).when(driverService).cancelRide(userId, rideId);

        //Test
        Response response = driverController.cancelOneRideByDriver(userId, rideId);

        //Verify
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateProfile() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        DriverDTO driverDTO = new DriverDTO();

        //Test
        driverController.updateProfile(driverId, driverDTO);

        //Verify
        verify(driverService).updateProfile(driverId, driverDTO);
    }

    @Test
    public void testStatusErrorUpdateProfile() throws DataUnreachableException {
        //Setup
        int driverId = -1;
        DriverDTO driverDTO = new DriverDTO();
        doThrow(DataUnreachableException.class).when(driverService).updateProfile(driverId, driverDTO);

        //Test
        Response response = driverController.updateProfile(driverId, driverDTO);

        //Verify
        assertThat(response.getStatus(), equalTo(500));
    }

    @Test
    public void testStatusOkUpdateProfile() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        DriverDTO driverDTO = new DriverDTO();

        //Test
        Response response = driverController.updateProfile(driverId, driverDTO);

        //Verify
        assertThat(response.getStatus(), equalTo(200));
    }

    @Test
    public void testGetAllClients() throws DataUnreachableException {
        //Setup
        int driverId = 1;

        //Test
        driverController.getAllClientsForDriver(driverId);

        //Verify
        verify(driverService).getAllClientsForDriver(driverId);
    }

    @Test
    public void testStatusErrorGetAllClients() throws DataUnreachableException {
        //Setup
        int driverId = -1;
        doThrow(DataUnreachableException.class).when(driverService).getAllClientsForDriver(driverId);

        //Test
        Response response = driverController.getAllClientsForDriver(driverId);

        //Verify
        assertThat(response.getStatus(), equalTo(500));
    }

    @Test
    public void testStatusOkGetAllClients() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        DriverDTO driverDTO = new DriverDTO();

        //Test
        Response response = driverController.getAllClientsForDriver(driverId);

        //Verify
        assertThat(response.getStatus(), equalTo(200));
    }

    @Test
    public void editClientCallsService() {
        //setup
        DriverDTO driver = new DriverDTO();

        //Setup
        driverController.editDriver(1, driver);

        //verify
        verify(driverService).editDriver(1, driver);
    }

    @Test
    public void deleteClientCallsService() {
        //Setup
        driverController.deleteDriver(1);

        //verify
        verify(driverService).deleteDriver(1);
    }

    @Test
    public void createDriverCallsService() {
        DriverDTO driverDTO = new DriverDTO();
        driverController.createDriver(driverDTO);

        verify(driverService).createDriver(driverDTO);
    }

    @Test
    public void getAllDriversCallsService() {
        driverController.getAllDrivers();

        verify(driverService).getAllDrivers();
    }

    @Test
    public void getAllDriversTownshipCallsService() {
        // Test
        driverController.getAllDriversTownship("Arnhem");

        // Verify
        verify(driverService).getAllDriversTownship("Arnhem");
    }

    @Test
    public void getDriverCallsService() throws DataUnreachableException {
        // Test
        driverController.getDriver(1);

        // Verify
        verify(userService).getProfile(1);
    }

    @Test
    public void getDriverReturnsStatusOK() throws DataUnreachableException {
        //setup
        DriverDTO driver = new DriverDTO();
        when(userService.getProfile(1)).thenReturn(driver);

        //Test
        Response response = driverController.getDriver(1);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void getDriverReturnsStatusInternalServerError() throws DataUnreachableException {
        //Test
        Response response = driverController.getDriver(1);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void getDriverThrowsDataUnreachableException() throws DataUnreachableException {
        //setup
        when(userService.getProfile(1)).thenThrow(DataUnreachableException.class);
        Response response = driverController.getDriver(1);

        try {
            //Test
            userService.getProfile(1);
        } catch (Throwable e) {
            //verify
            assertTrue(e instanceof DataUnreachableException);
            assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
        }
    }


    @Test
    public void getAllDriversCareInstitutionCallsService() throws DataUnreachableException {
        // Test
        driverController.getAllDiversCareInstitution(1);

        // Verify
        verify(driverService).getAllDriversCareInstitution(1);
    }

    @Test
    public void getPreferredDriversCallsService() {
        //Test
        driverController.getPreferredDrivers(1);

        //verify
        verify(driverService).getPreferredDriversToMatch(1);
    }
}
