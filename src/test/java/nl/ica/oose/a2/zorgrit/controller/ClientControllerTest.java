package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.dto.CareInstitutionDTO;
import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableRuntimeException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;
import nl.ica.oose.a2.zorgrit.service.IClientService;
import nl.ica.oose.a2.zorgrit.service.IDriverService;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ClientControllerTest {

    @Mock
    private IClientService clientService;

    @Mock
    private IDriverService driverService;

    @InjectMocks
    private ClientController sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetFutureRides() throws DataUnreachableException {
        //Setup
        int clientId = 2;
        // Test
        sut.getAllFutureRides(clientId);

        // Verify
        verify(clientService).getAllFutureRides(clientId);
    }

    @Test
    public void testGetFutureRides_ResponseOk() throws DataUnreachableException {
        // Test
        Response response = sut.getAllFutureRides(2);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testGetFutureRides_ResponseEntity() throws DataUnreachableException {
        //Setup
        int clientId = 2;
        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();
        RideDTO ride = mock(RideDTO.class);
        rides.add(ride);
        when(clientService.getAllFutureRides(clientId)).thenReturn(rides);

        // Test
        Response response = sut.getAllFutureRides(clientId);

        // Verify
        assertEquals(response.getEntity(), rides);
    }

    @Test
    public void testGetFutureRides_ResponseCodeInternalServerError() throws DataUnreachableException {
        //Setup
        int clientId = -15;
        when(clientService.getAllFutureRides(clientId)).thenThrow(DataUnreachableException.class);

        // Test
        Response response = sut.getAllFutureRides(clientId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testAddRide() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        RideDTO ride = mock(RideDTO.class);

        //Test
        sut.addRide(1, ride);

        // Verify
        verify(clientService).addRide(ride);
    }

    @Test
    public void testAddRide_ResponseOk() throws DataUnreachableException {
        //Setup
        int userId = 1;
        RideDTO ride = mock(RideDTO.class);

        // Test
        Response response = sut.addRide(userId, ride);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testAddRideThrowDataUnreachableException_ResponseCodeInternalServerError() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        RideDTO ride = mock(RideDTO.class);
        doThrow(DataUnreachableException.class).when(clientService).addRide(ride);

        // Test
        Response response = sut.addRide(2, ride);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testAddRideThrowInvalidDateFormatException_ResponseCode500() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        RideDTO ride = mock(RideDTO.class);
        doThrow(InvalidDateFormatException.class).when(clientService).addRide(ride);

        // Test
        Response response = sut.addRide(2, ride);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testResponseStatusOfCancelOneRide() throws DataUnreachableException {
        //Setup
        int rideId = 1;

        //Test
        Response response = sut.cancelRide(rideId);

        //Verify
        verify(clientService).cancelRide(1);
    }

    @Test
    public void testCancelRide_ResponseOk() throws DataUnreachableException {
        //Setup
        int rideId = 1;

        // Test
        Response response = sut.cancelRide(rideId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testCancelRideThrowDataUnreachableException_ResponseCodeInternalServerError() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        int rideId = 1;
        doThrow(DataUnreachableException.class).when(clientService).cancelRide(rideId);

        // Test
        Response response = sut.cancelRide(rideId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testResponseStatusOfCancelRepeatRide() throws InvalidDateFormatException, DataUnreachableException {
        //Setup
        int clientId = 1;
        int repeatingRideId = 2;

        //Test
        sut.cancelRepeatingRides(clientId, repeatingRideId);

        //Verify
        verify(clientService).cancelRepeatingRides(repeatingRideId);
    }

    @Test
    public void testCancelRepeatRide_ResponseOk() throws DataUnreachableException {
        //Setup
        int clientId = 1;
        int repeatingRideId = 2;

        // Test
        Response response = sut.cancelRepeatingRides(clientId, repeatingRideId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testCancelRepeatRideThrowDataUnreachableException_ResponseCodeInternalServerError() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        int clientId = 1;
        int repeatingRideId = 2;

        doThrow(DataUnreachableException.class).when(clientService).cancelRepeatingRides(repeatingRideId);

        // Test
        Response response = sut.cancelRepeatingRides(clientId, repeatingRideId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testCancelRepeatRideThrowInvalidDateFormatException_ResponseCodeInternalServerError() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        int clientId = 1;
        int repeatingRideId = 2;

        doThrow(InvalidDateFormatException.class).when(clientService).cancelRepeatingRides(repeatingRideId);

        // Test
        Response response = sut.cancelRepeatingRides(clientId, repeatingRideId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testGetAllCareInstitutions() throws DataUnreachableException {
        //Setup
        int userId = 2;

        //Test
        sut.getAllCareInstitutions(userId);

        //Verify
        verify(clientService).getAllCareInstitutions(userId);
    }

    @Test
    public void testGetCareInstitutions_ResponseOk() throws DataUnreachableException {
        // Test
        Response response = sut.getAllCareInstitutions(2);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testGetAllCareInstitutions_ResponseEntity() throws DataUnreachableException {
        //Setup
        int clientId = 2;
        ArrayList<CareInstitutionDTO> careInstitutions = new ArrayList<CareInstitutionDTO>();
        CareInstitutionDTO careInstitution = mock(CareInstitutionDTO.class);
        careInstitutions.add(careInstitution);
        when(clientService.getAllCareInstitutions(clientId)).thenReturn(careInstitutions);

        // Test
        Response response = sut.getAllCareInstitutions(clientId);

        // Verify
        assertEquals(response.getEntity(), careInstitutions);
    }

    @Test
    public void testGetAllCareInstitutions_ResponseCodeInternalServerError() throws DataUnreachableException {
        //Setup
        int clientId = -15;
        when(clientService.getAllCareInstitutions(clientId)).thenThrow(DataUnreachableException.class);

        // Test
        Response response = sut.getAllCareInstitutions(clientId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testGetProfile() throws DataUnreachableException {
        //Setup
        int clientId = 1;

        //Test
        sut.getProfile(clientId);

        // Verify
        verify(clientService).getProfile(clientId);
    }

    @Test
    public void testGetProfileResponseOk() throws DataUnreachableException {
        //Setup
        int userId = 1;

        // Test
        Response response = sut.getProfile(userId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testGetProfileThrowDataUnreachableException_ResponseCodeInternalServerError() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        int userId = 1;
        RideDTO ride = mock(RideDTO.class);
        doThrow(DataUnreachableException.class).when(clientService).getProfile(userId);

        // Test
        Response response = sut.getProfile(userId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testUpdateProfile() throws DataUnreachableException {
        //Setup
        int userId = 1;
        ClientDTO client = mock(ClientDTO.class);

        //Test
        sut.updateProfile(userId, client);

        // Verify
        verify(clientService).updateProfile(userId, client);
    }

    @Test
    public void testUpdateProfileResponseOk() throws DataUnreachableException {
        //Setup
        int userId = 1;
        ClientDTO client = mock(ClientDTO.class);

        // Test
        Response response = sut.updateProfile(userId, client);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testGetProfileResponseEntity() throws DataUnreachableException {
        //Setup
        int userId = 1;
        ClientDTO client = mock(ClientDTO.class);
        when(clientService.getProfile(userId)).thenReturn(client);

        // Test
        Response response = sut.getProfile(userId);

        // Verify
        assertEquals(response.getEntity(), client);
    }

    @Test
    public void testUpdateProfileThrowDataUnreachableException_ResponseCodeInternalServerError() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        int userId = 1;
        ClientDTO client = mock(ClientDTO.class);
        doThrow(DataUnreachableException.class).when(clientService).updateProfile(userId, client);

        // Test
        Response response = sut.updateProfile(userId, client);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void testGetAllCancelledRides() throws DataUnreachableException {
        //Setup
        int clientId = 2;

        //Test
        sut.getClientAllCancelledRides(clientId);

        //Verify
        verify(clientService).getClientAllCancelledRides(clientId);
    }

    @Test
    public void testResponseStatusOkOfGetCancelledRides() throws DataUnreachableException {
        //Setup
        int clientId = 2;

        //Test
        Response response = sut.getClientAllCancelledRides(clientId);

        //Verify
        MatcherAssert.assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testResponseEntityOfGetAllCancelledRides() throws DataUnreachableException {
        //Setup
        int clientId = 2;
        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();
        RideDTO ride = mock(RideDTO.class);
        rides.add(ride);
        when(clientService.getClientAllCancelledRides(clientId)).thenReturn(rides);

        //Test
        Response response = sut.getClientAllCancelledRides(clientId);

        //Verify
        assertEquals(response.getEntity(), rides);
    }

    @Test
    public void testResponseInternalServerErrorOfGetAllCancelledRides() throws DataUnreachableException {
        //Setup
        int clientId = 1;
        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();
        RideDTO ride = mock(RideDTO.class);
        rides.add(ride);
        when(clientService.getClientAllCancelledRides(clientId)).thenThrow(DataUnreachableException.class);

        //Test
        Response response = sut.getClientAllCancelledRides(clientId);

        //Verify
        MatcherAssert.assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void getAllClientsReturnsAllClients() {
        //Test
        sut.getAllClientsCareInstitution(1);

        //Verify
        verify(clientService).getAllClientsCareInstitution(1);
    }

    @Test
    public void getClientReturnsSpecifiedClient() throws DataUnreachableException {
        sut.getClient(1);

        verify(clientService).getProfile(1);
    }

    @Test
    public void editClientCallsClientService() {
        ClientDTO client = new ClientDTO();
        sut.editClient(1, client);

        verify(clientService).editClient(1, client);
    }

    @Test
    public void addClientReturnsAResponse() {
        ClientDTO clientDTO = new ClientDTO();

        sut.addClient(clientDTO);

        verify(clientService).createClient(clientDTO);
    }

    @Test
    public void getUtilitiesReturnsAllUtilities() {
        sut.getAllUtilities();

        verify(clientService).getAllUtilities();
    }

    @Test
    public void getLimitationsReturnsAllLimitations() {
        sut.getAllLimitations();

        verify(clientService).getAllLimitations();
    }

    @Test
    public void getClientUtilitiesReturnsAllClientUtilities() {
        sut.getClientUtilities(1);

        verify(clientService).getClientUtilities(1);
    }

    @Test
    public void deleteClientCallsFunctionInService() {
        //setup
        int clientId = 1;

        //test
        sut.deleteClient(clientId);

        //Verify
        verify(clientService).deleteClient(clientId);
    }

    @Test
    public void getClientAllCompletedRidesCallsMethodInService() throws DataUnreachableException {
        sut.getClientAllCompletedRides(1);

        verify(clientService).getClientAllCompletedRides(1);
    }

    @Test
    public void getClientAllCompletedRidesThrowsDataUncreachableException() throws DataUnreachableException {
        when(clientService.getClientAllCompletedRides(1))
                .thenThrow(new DataUnreachableRuntimeException("Data is unreachable"));

        DataUnreachableRuntimeException dataUnreachableRuntimeException = assertThrows(DataUnreachableRuntimeException.class, () -> {
            sut.getClientAllCompletedRides(1);
        });

        assertEquals("Data is unreachable", dataUnreachableRuntimeException.getMessage());
    }

    @Test
    public void getClientAllCompletedRidesReturnsDataUncreachableExceptionResponse() throws DataUnreachableException {
        //Setup
        int clientId = -15;
        when(clientService.getClientAllCompletedRides(clientId)).thenThrow(DataUnreachableException.class);

        // Test
        Response response = sut.getClientAllCompletedRides(clientId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));

    }

    @Test
    public void getAllDriversCallsMethodInService() throws DataUnreachableException {
        sut.getAllDrivers(1);

        verify(driverService).getAllDriversForClient(1);
    }

    @Test
    public void getAllDriversReturnsDataUncreachableExceptionResponse() throws DataUnreachableException {
        //Setup
        int clientId = -15;
        when(driverService.getAllDriversForClient(clientId)).thenThrow(new DataUnreachableException());

        // Test
        Response response = sut.getAllDrivers(clientId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));

    }

}
