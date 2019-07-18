package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.LimitationDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.UtilityDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;
import nl.ica.oose.a2.zorgrit.exceptions.NoRecordsFoundRuntimeException;
import nl.ica.oose.a2.zorgrit.exceptions.RideNotPayedException;
import nl.ica.oose.a2.zorgrit.persistance.IClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.IRideDAO;
import nl.ica.oose.a2.zorgrit.persistance.IUserDAO;
import nl.ica.oose.a2.zorgrit.service.ridematcher.IRideMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ClientServiceTest {
    private static final String NO_RECORDS_WHERE_FOUND = "No records where found.";
    @Mock
    private IClientDAO clientDAO;

    @Mock
    private IRideDAO rideDAO;

    @Mock
    private IUserDAO userDAO;

    @Mock
    private IRideMatcher rideMatcher;

    @InjectMocks
    private ClientService sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllFutureRidesForClient_RidesExist_True() throws DataUnreachableException {
        //        // Test
        int clientId = 2;
        sut.getAllFutureRides(clientId);

        // Verify
        verify(clientDAO).getAllFutureRides(clientId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetAllFutureRidesForClient_RidesExist_False() throws DataUnreachableException {
        //Setup
        int rideId = -15;
        when(clientDAO.getAllFutureRides(rideId)).thenThrow(DataUnreachableException.class);

        // Test
        sut.getAllFutureRides(rideId);
    }

    @Test
    public void testGetAllCompletedRidesForClient_RidesExist_True() throws DataUnreachableException {
        // Test
        int clientId = 2;
        sut.getClientAllCompletedRides(clientId);

        // Verify
        verify(clientDAO).getAllCompletedRides(clientId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetAllCompletedRidesThrowDataUnreachableException() throws DataUnreachableException {
        //Setup
        int clientId = -769128;
        doThrow(DataUnreachableException.class).when(clientDAO).getAllCompletedRides(clientId);

        // Test
        sut.getClientAllCompletedRides(clientId);
    }

    @Test
    public void testGetAllCancelledRidesForClient_RidesExist_True() throws DataUnreachableException {
        // Test
        int clientId = 2;
        sut.getClientAllCancelledRides(clientId);

        // Verify
        verify(clientDAO).getAllCancelledRides(clientId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetAllCancelledRidesThrowDataUnreachableException() throws DataUnreachableException {
        //Setup
        int clientId = -769128;
        doThrow(DataUnreachableException.class).when(clientDAO).getAllCancelledRides(clientId);

        // Test
        sut.getClientAllCancelledRides(clientId);
    }

    @Test
    public void testCancelOneRide() throws DataUnreachableException {
        //Test
        int rideId = 1;
        sut.cancelRide(rideId);

        //Verify
        verify(clientDAO).cancelRide(rideId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testCancelOneRideThrowDataUnreachableException() throws DataUnreachableException {
        //Setup
        int rideId = -15;
        doThrow(DataUnreachableException.class).when(clientDAO).cancelRide(rideId);

        // Test
        sut.cancelRide(rideId);
    }

    @Test
    public void testGetAllCareInstitutions() throws DataUnreachableException {
        //Test
        int clientId = 2;
        sut.getAllCareInstitutions(clientId);

        //Verify
        verify(clientDAO).getAllCareInstitutions(clientId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetAllCareInstitutionsException() throws DataUnreachableException {
        //Setup
        int clientId = -15;
        when(clientDAO.getAllCareInstitutions(clientId)).thenThrow(DataUnreachableException.class);

        //Test
        sut.getAllCareInstitutions(clientId);
    }

    @Test
    public void testGetProfile() throws DataUnreachableException {
        //Test
        int clientId = 2;
        sut.getProfile(clientId);

        //Verify
        verify(clientDAO).getProfile(clientId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetProfileDataUnreachableException() throws DataUnreachableException {
        //Setup
        int clientId = -15;
        when(clientDAO.getProfile(clientId)).thenThrow(DataUnreachableException.class);

        //Test
        sut.getProfile(clientId);
    }

    @Test
    public void testUpdateProfile() throws DataUnreachableException {
        //Setup
        ClientDTO client = mock(ClientDTO.class);
        client.setId(1);

        //Test
        sut.updateProfile(client.getId(), client);

        //Verify
        verify(clientDAO).updateProfile(client.getId(), client);
    }

    @Test(expected = DataUnreachableException.class)
    public void testUpdateProfileThrowDataUnreachableException() throws DataUnreachableException {
        //Setup
        int clientId = -15;
        ClientDTO client = mock(ClientDTO.class);
        doThrow(DataUnreachableException.class).when(clientDAO).updateProfile(clientId, client);

        // Test
        sut.updateProfile(clientId, client);
    }

    @Test
    public void testAddRide() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        RideDTO ride = mock(RideDTO.class);

        //Test
        sut.addRide(ride);

        //Verify
        verify(clientDAO).addRide(ride);
        verify(rideMatcher).startMatching(ride);
    }

    @Test(expected = DataUnreachableException.class)
    public void testAddRideThrowDataUnreachableException() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        RideDTO ride = mock(RideDTO.class);
        doThrow(DataUnreachableException.class).when(clientDAO).addRide(ride);

        // Test
        sut.addRide(ride);
    }

    @Test(expected = InvalidDateFormatException.class)
    public void testAddRideThrowInvalidDateFormatException() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        RideDTO ride = mock(RideDTO.class);
        doThrow(InvalidDateFormatException.class).when(clientDAO).addRide(ride);

        // Test
        sut.addRide(ride);
    }

    @Test
    public void testCancelRepeatRide() throws InvalidDateFormatException, DataUnreachableException {
        //Setup
        int repeatingRideId = 1;
        //Test
        sut.cancelRepeatingRides(repeatingRideId);

        //Verify
        verify(clientDAO).cancelRepeatingRides(repeatingRideId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testCancelRepeatingRideThrowDataUnreachableException() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        int repeatingRideId = 769128;
        doThrow(DataUnreachableException.class).when(clientDAO).cancelRepeatingRides(repeatingRideId);

        // Test
        sut.cancelRepeatingRides(repeatingRideId);
    }

    @Test(expected = InvalidDateFormatException.class)
    public void testCancelRepeatingRideThrowInvalidDateFormatException() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        int repeatingRideId = 769128;
        String date = "2017-08-06 23:23:05";
        doThrow(InvalidDateFormatException.class).when(clientDAO).cancelRepeatingRides(repeatingRideId);

        // Test
        sut.cancelRepeatingRides(repeatingRideId);
    }

    @Test
    public void getAllClientsReturnsClientsList(){
        ClientDTO client = new ClientDTO();
        List<ClientDTO> clients = new ArrayList<>();
        clients.add(client);

        when(clientDAO.getAllClientsCareInstitution(1)).thenReturn(clients);

        assertEquals(clients, sut.getAllClientsCareInstitution(1));
    }

    @Test
    public void getAllClientsReturnsDataUnreachableException(){
        ArrayList<ClientDTO> clients = new ArrayList<>();
        when(clientDAO.getAllClientsCareInstitution(1)).thenReturn(clients);

        assertThrows(NoRecordsFoundRuntimeException.class, () -> {
            List<ClientDTO> actualResult = sut.getAllClientsCareInstitution(1);
        });
    }

    @Test
    public void getClientReturnsClientsList(){
        int clientId = 1;
        ClientDTO client = new ClientDTO();
        client.setId(clientId);

        when(clientDAO.getClient(clientId)).thenReturn(client);

        assertEquals(client, sut.getClient(1));
    }

    @Test
    public void getClientReturnsDataUnreachableException(){
        int clientId = -1;
        ClientDTO client = new ClientDTO();
        when(clientDAO.getClient(clientId)).thenReturn(client);

        Exception actualResult = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.getClient(clientId));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, actualResult.getMessage());
    }

    @Test
    public void editClientCallsMethodInDAO(){
        int clientId = 1;
        ClientDTO client = new ClientDTO();
        client.setId(clientId);

        when(clientDAO.getClient(clientId)).thenReturn(client);

        sut.editClient(clientId, client);

        verify(clientDAO).editClient(clientId, client);
    }

    @Test
    public void editClientReturnsDataUnreachableException(){
        int clientId = 1;
        ClientDTO client = new ClientDTO();

        when(clientDAO.getClient(clientId)).thenReturn(client);

        Exception actualResult = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.editClient(clientId, client));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, actualResult.getMessage());
    }

    @Test
    public void createClientCallsMethodInDAO(){
        ClientDTO client = new ClientDTO();

        sut.createClient(client);

        verify(clientDAO).addClient(client);
    }

    @Test
    public void getAllLimitationsReturnsLimitationList(){
        LimitationDTO limitationDTO = new LimitationDTO();
        List<LimitationDTO> limitations = new ArrayList<>();
        limitations.add(limitationDTO);

        when(clientDAO.getLimitations()).thenReturn(limitations);

        assertEquals(limitations, sut.getAllLimitations());
    }

    @Test
    public void getAllLimitationsReturnsDataUnreachableException(){
        ArrayList<LimitationDTO> limitations = new ArrayList<>();
        when(clientDAO.getLimitations()).thenReturn(limitations);

        Exception actualResult = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.getAllLimitations());
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, actualResult.getMessage());
    }

    @Test
    public void getAllUtilitiesReturnsUtilityList(){
        UtilityDTO utilityDTO = new UtilityDTO();
        List<UtilityDTO> utilities = new ArrayList<>();
        utilities.add(utilityDTO);

        when(clientDAO.getUtilities()).thenReturn(utilities);

        assertEquals(utilities, sut.getAllUtilities());
    }

    @Test
    public void getAllUtilitiesReturnsDataUnreachableException(){
        ArrayList<UtilityDTO> utilities = new ArrayList<>();
        when(clientDAO.getUtilities()).thenReturn(utilities);

        Exception actualResult = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.getAllUtilities());
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, actualResult.getMessage());
    }

    @Test
    public void getClientLimitationsReturnsLimitationList(){
        int clientId = 1;
        ClientDTO user = new ClientDTO();
        List<UtilityDTO> clientUtilities = new ArrayList<>();

        user.setId(clientId);
        clientUtilities.add(new UtilityDTO());

        when(clientDAO.getClient(clientId)).thenReturn(user);
        when(clientDAO.getClientUtilities(clientId)).thenReturn(clientUtilities);

        assertEquals(clientUtilities, sut.getClientUtilities(1));
    }

    @Test
    public void getClientLimitationsReturnsDataUnreachableException(){
        int clientId = 1;
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(clientId);
        when(clientDAO.getClient(clientId)).thenReturn(clientDTO);

        Exception actualResult = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.getClientUtilities(clientId));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, actualResult.getMessage());
    }

    @Test
    public void deleteClient_ClientStillHasUnpayedRidesReturnsRideNotPayedException() {
        int clientId = 1;

        when(rideDAO.checkIfClientHasUnPayedRides(clientId)).thenReturn(true);

        Exception actualResult = assertThrows(RideNotPayedException.class, () -> sut.deleteClient(clientId));
        Assertions.assertEquals("Er zijn nog niet betaalde ritten, zolang deze open staan kan de client niet verwijderd worden", actualResult.getMessage());
    }

    @Test
    public void deleteClient_ClientHasPayedAllRidesButOpenRides_CallsMethodInService() {
        int clientId = 1;
        List<RideDTO> rides = new ArrayList<>();
        RideDTO ride = new RideDTO();

        rides.add(ride);

        when(rideDAO.checkIfClientHasUnPayedRides(clientId)).thenReturn(false);
        when(clientDAO.checkIfClientHasRides(clientId)).thenReturn(rides);

        sut.deleteClient(clientId);

        verify(userDAO).setUserInActiveSince(clientId);
    }

    @Test
    public void deleteClient_ClientHasPayedAllRidesAndNoOpenRides_CallsMethodInService() {
        int clientId = 1;

        when(rideDAO.checkIfClientHasUnPayedRides(clientId)).thenReturn(false);
        when(clientDAO.checkIfClientHasRides(clientId)).thenReturn(null);

        sut.deleteClient(clientId);

        verify(clientDAO).deleteAllTablesForClient(clientId);
    }
}