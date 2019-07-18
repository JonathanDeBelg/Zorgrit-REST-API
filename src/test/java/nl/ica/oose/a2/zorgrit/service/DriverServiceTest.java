package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.NoRecordsFoundRuntimeException;
import nl.ica.oose.a2.zorgrit.persistance.IClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.IDriverDAO;
import nl.ica.oose.a2.zorgrit.persistance.IUserDAO;
import nl.ica.oose.a2.zorgrit.service.ridematcher.IRideMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DriverServiceTest {
    private static final String NO_RECORDS_WHERE_FOUND = "No records where found.";

    @Mock
    private IDriverDAO driverDAO;

    @Mock
    private IUserDAO userDAO;

    @Mock
    private IClientDAO clientDAO;

    @Mock
    private IRideMatcher rideMatcher;

    @InjectMocks
    private DriverService driverService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllFutureRidesForDriver_RidesExist_True() throws DataUnreachableException {
        // Setup
        int driverId = 2;

        // Test
        driverService.getAllFutureRides(driverId);

        // Verify
        verify(driverDAO).getAllFutureRides(driverId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetAllFutureRidesThrowDataUnreachableException() throws DataUnreachableException {
        //Setup
        int driverId = -769128;
        doThrow(DataUnreachableException.class).when(driverDAO).getAllFutureRides(driverId);

        // Test
        driverService.getAllFutureRides(driverId);
    }

    @Test
    public void testGetAllCompletedRidesForDriver_RidesExcist_True() throws DataUnreachableException {
        // Test
        int driverId = 1;
        driverService.getAllCompletedRides(driverId);

        // Verify
        verify(driverDAO).getAllCompletedRides(driverId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetAllCompletedRidesThrowDataUnreachableException() throws DataUnreachableException {
        //Setup
        int driverId = -769128;
        doThrow(DataUnreachableException.class).when(driverDAO).getAllCompletedRides(driverId);

        // Test
        driverService.getAllCompletedRides(driverId);
    }

    @Test
    public void testAcceptRide() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        int clientId = 2;

        //Test
        driverService.acceptRide(driverId, clientId);

        //Verify
        verify(driverDAO).acceptRide(driverId, clientId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testAcceptRideThrowDataUnreachableException() throws DataUnreachableException {
        //Setup
        int driverId = -769128;
        int rideId = 1;
        doThrow(DataUnreachableException.class).when(driverDAO).acceptRide(driverId, rideId);

        // Test
        driverService.acceptRide(driverId, rideId);
    }

    @Test
    public void testCancelRide() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        int rideId = 2;

        //Test
        driverService.cancelRide(driverId, rideId);

        //Verify
        verify(driverDAO).cancelRide(driverId, rideId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testCancelRideThrowDataUnreachableException() throws DataUnreachableException {
        //Setup
        int driverId = -769128;
        int rideId = 1;
        doThrow(DataUnreachableException.class).when(driverDAO).cancelRide(driverId, rideId);

        // Test
        driverService.cancelRide(driverId, rideId);
    }

    @Test
    public void testGetDrivers() throws DataUnreachableException {
        //Test
        ArrayList<DriverDTO> driverDTOS = driverDAO.getAllDriversForClient(2);

        //Verify
        assertEquals(0, driverDTOS.size(), 1);
    }

    @Test
    public void testGetAllDrivers() throws DataUnreachableException {
        //Test
        driverService.getAllDriversForClient(2);

        //Verify
        verify(driverDAO).getAllDriversForClient(2);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetAllCareDriversException() throws DataUnreachableException {
        //Setup
        when(driverDAO.getAllDriversForClient(2)).thenThrow(DataUnreachableException.class);

        //Test
        driverService.getAllDriversForClient(2);
    }

    @Test
    public void testGetAllRequestRidesForDriver() throws DataUnreachableException {
        //Setup
        int driverId = 4;

        //Test
        driverService.getAllRequestRides(driverId);

        //Verify
        verify(driverDAO).getAllRequestRidesForDriver(driverId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetRequestRidesThrowDataUnreachableException() throws DataUnreachableException {
        //Setup
        int driverId = -769128;
        doThrow(DataUnreachableException.class).when(driverDAO).getAllRequestRidesForDriver(driverId);

        // Test
        driverService.getAllRequestRides(driverId);
    }

    @Test
    public void testGetAllCancelledRidesForDriver() throws DataUnreachableException {
        //Setup
        int driverId = 4;

        //Test
        driverService.getAllCancelledRides(driverId);

        //Verify
        verify(driverDAO).getAllCancelledRides(driverId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetCancelledRidesThrowDataUnreachableException() throws DataUnreachableException {
        //Setup
        int driverId = -769128;
        doThrow(DataUnreachableException.class).when(driverDAO).getAllCancelledRides(driverId);

        // Test
        driverService.getAllCancelledRides(driverId);
    }

    @Test
    public void testUpdateProfile() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        DriverDTO driverDTO = new DriverDTO();

        //Test
        driverService.updateProfile(driverId, driverDTO);

        //Verify
        verify(driverDAO).updateProfile(driverId, driverDTO);
    }

    @Test(expected = DataUnreachableException.class)
    public void testExceptionUpdateProfile() throws DataUnreachableException {
        //Setup
        int driverId = -1;
        DriverDTO driverDTO = new DriverDTO();
        doThrow(DataUnreachableException.class).when(driverDAO).updateProfile(driverId, driverDTO);

        //Test
        driverService.updateProfile(driverId, driverDTO);
    }

    @Test
    public void testGetAllClients() throws DataUnreachableException {
        //Setup
        int driverId = 1;

        //Test
        driverService.getAllClientsForDriver(driverId);

        //Verify
        verify(driverDAO).getAllClientsForDriver(driverId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testExceptionGetAllClients() throws DataUnreachableException {
        //Setup
        int driverId = -1;
        doThrow(DataUnreachableException.class).when(driverDAO).getAllClientsForDriver(driverId);

        //Test
        driverService.getAllClientsForDriver(driverId);
    }

    @Test
    public void createDriverCallsUserDAO() {
        //Setup
        DriverDTO driverDTO = new DriverDTO();

        //Test
        driverService.createDriver(driverDTO);

        //Verify
        verify(userDAO).createUser(driverDTO);
    }

    @Test
    public void createDriverCallsDriverDAO() {
        //setup
        DriverDTO driverDTO = new DriverDTO();

        //Test
        driverService.createDriver(driverDTO);

        //Verify
        verify(driverDAO).createDriver(driverDTO);
    }

    @Test
    public void getAllDriversCareInstitutionCallsDAO() {
        //setup
        DriverDTO testDriver = new DriverDTO();
        ArrayList<DriverDTO> driverList = new ArrayList<>();
        driverList.add(testDriver);


        when(driverDAO.getAllDriversCareInstitution(1))
                .thenReturn(driverList);

        //verify
        assertEquals(driverList, driverService.getAllDriversCareInstitution(1));
    }

    @Test
    public void getAllDriversTownshipCallsDAO() {
        //setup
        DriverDTO testDriver = new DriverDTO();
        ArrayList<DriverDTO> driverList = new ArrayList<>();

        driverList.add(testDriver);

        when(driverDAO.getAllDriversTownship("Test"))
                .thenReturn(driverList);

        //verify
        assertEquals(driverList, driverService.getAllDriversTownship("Test"));
    }

    @Test
    public void getDriverCallsDAO() {
        //setup
        DriverDTO testDriver = new DriverDTO();
        testDriver.setId(1);

        when(driverDAO.getDriver(1))
                .thenReturn(testDriver);

        //verify
        assertEquals(testDriver, driverService.getDriver(1));
    }

    @Test
    public void getAllDriversCallsDAO() {
        //setup
        DriverDTO testDriver = new DriverDTO();
        ArrayList<DriverDTO> driverList = new ArrayList<>();

        driverList.add(testDriver);

        when(driverDAO.getAllDrivers())
                .thenReturn(driverList);

        //verify
        assertEquals(driverList, driverService.getAllDrivers());
    }

    @Test
    public void getAllDriversThrowsDataUnreachableException() {
        ArrayList<DriverDTO> drivers = new ArrayList<>();
        when(driverDAO.getAllDrivers())
                .thenReturn(drivers);

        Exception exception = assertThrows(NoRecordsFoundRuntimeException.class, () -> driverService.getAllDrivers());
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, exception.getMessage());
    }

    @Test
    public void getDriverThrowsDataUnreachableException() {
        DriverDTO driver = new DriverDTO();
        when(driverDAO.getDriver(1))
                .thenReturn(driver);

        Exception exception = assertThrows(NoRecordsFoundRuntimeException.class, () -> driverService.getDriver(1));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, exception.getMessage());
    }

    @Test
    public void getDriversTownshipThrowsDataUnreachableException() {
        ArrayList<DriverDTO> drivers = new ArrayList<>();
        when(driverDAO.getAllDriversTownship("Nijmegen"))
                .thenReturn(drivers);

        Exception exception = assertThrows(NoRecordsFoundRuntimeException.class, () -> driverService.getAllDriversTownship("Nijmegen"));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, exception.getMessage());
    }

    @Test
    public void getAllDriversCareInstitutionThrowsDataUnreachableException() {
        ArrayList<DriverDTO> drivers = new ArrayList<>();
        when(driverDAO.getAllDriversCareInstitution(1))
                .thenReturn(drivers);

        Exception exception = assertThrows(NoRecordsFoundRuntimeException.class, () -> driverService.getAllDriversCareInstitution(1));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, exception.getMessage());
    }

    @Test
    public void editDriverCallsDAO() {
        DriverDTO driver = new DriverDTO();
        driver.setId(1);

        when(driverDAO.getDriver(1))
                .thenReturn(driver);

        //Test
        driverService.editDriver(1, driver);

        //Verify
        verify(driverDAO).editDriver(1, driver);
    }

    @Test
    public void editDriverThrowsNoDataFoundRuntimeException() {
        DriverDTO driver = new DriverDTO();

        when(driverDAO.getDriver(1))
                .thenReturn(driver);

        Exception exception = assertThrows(NoRecordsFoundRuntimeException.class, () -> driverService.editDriver(1, driver));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, exception.getMessage());
    }


    @Test
    public void deleteDriverCallsUserDAO() {
        RideDTO ride = new RideDTO();
        ArrayList<RideDTO> rideList = new ArrayList<>();
        rideList.add(ride);

        when(driverDAO.checkIfDriverHasRides(1))
                .thenReturn(rideList);

        driverService.deleteDriver(1);

        verify(userDAO).setUserInActiveSince(1);
    }

    @Test
    public void deleteDriverCallsDriverDAO() {
        ArrayList<RideDTO> rideList = new ArrayList<>();

        when(driverDAO.checkIfDriverHasRides(1))
                .thenReturn(rideList);

        driverService.deleteDriver(1);

        verify(driverDAO).deleteDriver(1);
    }

    @Test
    public void getPreferredDriversToMatchCallsDAO() {
        int clientId = 1;
        ClientDTO client = new ClientDTO();
        client.setId(clientId);
        DriverDTO driver = new DriverDTO();
        driver.setId(client.getId());
        ArrayList<DriverDTO> drivers = new ArrayList<>();
        drivers.add(driver);

        when(clientDAO.getClient(clientId))
                .thenReturn(client);

        driverService.getPreferredDriversToMatch(clientId);

        verify(driverDAO).getPreferredDriversToMatch(clientId);
    }

    @Test
    public void getPreferredDriversThrowsDataUnreachableException() {
        int clientId = 1;
        ClientDTO client = new ClientDTO();
        DriverDTO driver = new DriverDTO();

        when(clientDAO.getClient(clientId))
                .thenReturn(client);

        Exception exception = assertThrows(NoRecordsFoundRuntimeException.class, () -> driverService.getPreferredDriversToMatch(clientId));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, exception.getMessage());
    }
}
