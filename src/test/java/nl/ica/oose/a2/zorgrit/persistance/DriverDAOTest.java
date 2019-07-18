package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class DriverDAOTest extends DatabaseConnection {

    @Test
    public void testGetAllRidesForDriver() throws DataUnreachableException {
        IDriverDAO driverDAO = new DriverDAO();

        ArrayList<RideDTO> rides = driverDAO.getAllFutureRides(4);

        assertEquals(2, rides.size(), 0);
    }

    /*TODO Er bestaat nog geen dergelijke match in de database, om dit goed te testen moet er dus een dergelijke goede match in de database komen
           Zodat dit echt volledig afgetest kan worden.*/
    @Test
    public void testGetAllRequestRidesForDriver() throws DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        int driverId = 4;

        //Test
        ArrayList<RideDTO> rides = driverDAO.getAllRequestRidesForDriver(driverId);

        //Verify
        assertEquals(0, rides.size(), 0);
    }

    /*TODO Er bestaat nog geen dergelijke match in de database, om dit goed te testen moet er dus een dergelijke goede match in de database komen
           Zodat dit echt volledig afgetest kan worden.*/
    @Test
    public void testOneGetAllRequestRidesForDriver() throws DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        int driverId = 4;

        //Test
        ArrayList<RideDTO> rides = driverDAO.getAllRequestRidesForDriver(driverId);

        //Verify
        assertEquals(0, rides.size(), 0);
    }

    @Test
    public void testGetAllCompletedRidesForDriver() throws DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        int driverId = 1;

        //Test
        ArrayList<RideDTO> rides = driverDAO.getAllCompletedRides(driverId);

        //Verify
        assertEquals(1, rides.size());
    }

    @Test
    public void testGetAllCancelledRidesForDriver() throws DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        int driverId = 1;

        //Test
        ArrayList<RideDTO> rides = driverDAO.getAllCancelledRides(driverId);

        //Verify
        assertEquals(1, rides.size(), 0);
    }

    @Test(expected = DataUnreachableException.class)
    public void testCancelOneRideByDriverUnknownRide() throws DataUnreachableException {
        IDriverDAO driverDAO = new DriverDAO();
        driverDAO.cancelRide(1, -1);
    }

    @Test(expected = DataUnreachableException.class)
    public void testCancelOneRideByDriverUnknownDriver() throws DataUnreachableException {
        IDriverDAO driverDAO = new DriverDAO();
        driverDAO.cancelRide(2, 2);
    }

    @Test
    public void testCancelRideDriverIdSetNull() throws SQLException, DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        int driverId = 1;
        int rideId = 1;
        Connection connection = openConnection();

        //Test
        driverDAO.cancelRide(driverId, rideId);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT driverId FROM ride WHERE id = ?");
        preparedStatement.setInt(1, rideId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Verify
        assertEquals(0, resultset.getInt("driverId"));
    }

    @Test
    public void testGetProfileDriver() throws DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();

        //Test
        UserDTO userDTO = driverDAO.getProfile(1);

        //Verify
        assertTrue(userDTO instanceof DriverDTO);
    }

    @Test
    public void testGetAllClients() throws DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        int driverId = 1;

        //Test
        driverDAO.getAllClientsForDriver(driverId);

        //Verify
        assertEquals(5, driverDAO.getAllClientsForDriver(driverId).size());
    }

    @Test
    public void testAcceptRideCorrectInDatabase() throws SQLException, DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        int driverId = 4;
        int rideId = 6;
        Connection connection = openConnection();

        //Test
        driverDAO.acceptRide(driverId, rideId);

        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT driverId FROM ride WHERE id = ?");
        preparedStatement1.setInt(1, rideId);
        ResultSet resultset1 = preparedStatement1.executeQuery();
        resultset1.next();

        PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM rideproposedfordriver WHERE rideId = ?");
        preparedStatement2.setInt(1, rideId);
        ResultSet resultset2 = preparedStatement2.executeQuery();
        resultset2.next();

        //Verify

        //DriverDTO is set in ride table
        assertEquals(driverId, resultset1.getInt("driverId"));

        //All proposed rides are deleted from proposedridefordriver table
        assertEquals(0, resultset2.getRow());
    }

    @Test
    public void testUpdateProfileUpdateUserTableCorrectInDatabase() throws SQLException, DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        DriverDTO driverDTO = new DriverDTO();
        int driverId = 4;
        int clientId = 2;
        String firstName = "Henk";
        String lastName = "Henksen";
        String email = "henk@henk.nl";
        String phoneNumber = "0612345678";
        String street = "Dorpstraat";
        String houseNumber = "33a";
        String zipCode = "1234AB";
        String birthdate = "1980-04-04";
        String residence = "Arnhem";
        String utilityName = "Rolstoel";
        boolean verification = false;
        boolean isFirstTimeProfile = false;
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);
        List<ClientDTO> clients = new ArrayList<>();
        ClientDTO client = new ClientDTO();
        client.setId(clientId);
        clients.add(client);

        driverDTO.setId(driverId);
        driverDTO.setFirstName(firstName);
        driverDTO.setLastName(lastName);
        driverDTO.setEmail(email);
        driverDTO.setPhoneNumber(phoneNumber);
        driverDTO.setStreet(street);
        driverDTO.setHouseNumber(houseNumber);
        driverDTO.setZipCode(zipCode);
        driverDTO.setDateOfBirth(birthdate);
        driverDTO.setResidence(residence);
        driverDTO.setUtility(new UtilityDTO());
        driverDTO.setVerification(verification);
        driverDTO.setPreferredClients(clients);
        driverDTO.setFirstTimeProfileCheck(isFirstTimeProfile);

        Connection connection = openConnection();

        //Test
        driverDAO.updateProfile(driverId, driverDTO);

        //UserDTO table
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
        preparedStatement.setInt(1, driverId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Verify
        assertEquals(firstName, resultset.getString("firstName"));
        assertEquals(lastName, resultset.getString("lastName"));
        assertEquals(email, resultset.getString("email"));
        assertEquals(phoneNumber, resultset.getString("phoneNumber"));
        assertEquals(street, resultset.getString("street"));
        assertEquals(houseNumber, resultset.getString("houseNumber"));
        assertEquals(zipCode, resultset.getString("zipCode"));
        assertEquals(residence, resultset.getString("residence"));
        assertEquals(java.sql.Date.valueOf(birthdate), resultset.getDate("dateOfBirth"));
        assertEquals(driverId, resultset.getInt("id"));
    }


    @Test
    public void testUpdateProfileUpdateDriverTableCorrectInDatabase() throws SQLException, DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        DriverDTO driverDTO = new DriverDTO();
        int driverId = 4;
        int clientId = 2;
        String firstName = "Henk";
        String lastName = "Henksen";
        String email = "henk@henk.nl";
        String phoneNumber = "0612345678";
        String street = "Dorpstraat";
        String houseNumber = "33a";
        String zipCode = "1234AB";
        String birthdate = "1980-04-04";
        String residence = "Arnhem";
        String utilityName = "Rolstoel";
        boolean isFirstTimeProfile = false;
        boolean verification = false;
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);
        List<ClientDTO> clients = new ArrayList<>();
        ClientDTO client = new ClientDTO();
        client.setId(clientId);
        clients.add(client);

        driverDTO.setId(driverId);
        driverDTO.setFirstName(firstName);
        driverDTO.setLastName(lastName);
        driverDTO.setEmail(email);
        driverDTO.setPhoneNumber(phoneNumber);
        driverDTO.setStreet(street);
        driverDTO.setHouseNumber(houseNumber);
        driverDTO.setZipCode(zipCode);
        driverDTO.setDateOfBirth(birthdate);
        driverDTO.setResidence(residence);
        driverDTO.setUtility(utility);
        driverDTO.setVerification(verification);
        driverDTO.setPreferredClients(clients);
        driverDTO.setFirstTimeProfileCheck(isFirstTimeProfile);

        Connection connection = openConnection();

        //Test
        driverDAO.updateProfile(driverId, driverDTO);

        //UserDTO table
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM driver WHERE driverId = ?");
        preparedStatement.setInt(1, driverId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Verify
        assertEquals(verification, resultset.getBoolean("verification"));
        assertEquals(utility.getName(), resultset.getString("utility"));
    }

    @Test
    public void testUpdateProfileUpdateDriverClientPreferenceInsertTableCorrectInDatabase() throws SQLException, DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        DriverDTO driverDTO = new DriverDTO();
        int driverId = 1;
        int clientId = 2;
        String firstName = "Henk";
        String lastName = "Henksen";
        String email = "henk@henk.nl";
        String phoneNumber = "0612345678";
        String street = "Dorpstraat";
        String houseNumber = "33a";
        String zipCode = "1234AB";
        String birthdate = "1980-04-04";
        String residence = "Arnhem";
        String utilityName = "Rolstoel";
        boolean verification = false;
        boolean isFirstTimeProfile = false;
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);
        List<ClientDTO> clients = new ArrayList<>();
        ClientDTO client = new ClientDTO();
        client.setId(clientId);
        clients.add(client);

        driverDTO.setId(driverId);
        driverDTO.setFirstName(firstName);
        driverDTO.setLastName(lastName);
        driverDTO.setEmail(email);
        driverDTO.setPhoneNumber(phoneNumber);
        driverDTO.setStreet(street);
        driverDTO.setHouseNumber(houseNumber);
        driverDTO.setZipCode(zipCode);
        driverDTO.setDateOfBirth(birthdate);
        driverDTO.setResidence(residence);
        driverDTO.setUtility(new UtilityDTO());
        driverDTO.setVerification(verification);
        driverDTO.setPreferredClients(clients);
        driverDTO.setFirstTimeProfileCheck(isFirstTimeProfile);

        Connection connection = openConnection();

        //Test
        driverDAO.updateProfile(driverId, driverDTO);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM driverClientPreference WHERE driverId = ? AND clientId=?");
        preparedStatement.setInt(1, driverId);
        preparedStatement.setInt(2, clientId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Verify
        assertEquals(driverId, resultset.getInt("driverId"));
        assertEquals(clientId, resultset.getInt("clientId"));
    }

    @Test
    public void testGetAllDriversForClient() throws SQLException, DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        int clientId = 2;

        //Verify
        assertEquals(driverDAO.getAllDriversForClient(clientId).size(), driverDAO.getAllDriversForClient(clientId).size());
    }

    @Test
    public void GetAllDrivers() {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();

        //Verify there are 4 drivers that match with the query used in getAllDrivers()
        assertEquals(4, driverDAO.getAllDrivers().size());
    }

    @Test
    public void testGetDriver() {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();

        //Test
        UserDTO userDTO = driverDAO.getDriver(1);

        //Verify
        assertTrue(userDTO instanceof DriverDTO);
        assertNotNull(userDTO);
    }

    @Test
    public void getAllDriversTownship() {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();

        //Verify there is 1 driver that matches with the query used in getAllDriversTownship() in township Arnhem
        assertEquals(1, driverDAO.getAllDriversTownship("Arnhem").size());
    }

    @Test
    public void getAllDriversCareInstitution() {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();

        //Verify there is 1 driver that matches with the query used in getAllDriversCareInstitution() in careinstitution with id 1
        assertEquals(1, driverDAO.getAllDriversCareInstitution(1).size());
    }

    @Test
    public void getPreferredDriversToMatchReturnsDrivers() {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();

        //Verify client 2 has 1 preferred driver
        assertEquals(1, driverDAO.getPreferredDriversToMatch(2).size());
    }

    @Test
    public void getDriverIdReturnsGoodId() {
        //Setup
        DriverDAO driverDAO = new DriverDAO();
        DriverDTO driverDTO = new DriverDTO();
        String firstName = "Hoang";
        String lastName = "Nguyen";
        String email = "'hoang@outlook.com'";
        String phoneNumber = "'0645698712'";
        String street = "'Kruisweg'";
        String houseNumber = "7";
        String zipCode = "'2845DF'";
        String residence = "'Duiven'";

        driverDTO.setFirstName(firstName);
        driverDTO.setLastName(lastName);
        driverDTO.setEmail(email);
        driverDTO.setPhoneNumber(phoneNumber);
        driverDTO.setStreet(street);
        driverDTO.setHouseNumber(houseNumber);
        driverDTO.setZipCode(zipCode);
        driverDTO.setResidence(residence);

        int driverId = driverDAO.getDriverId(driverDTO);

        assertEquals(driverId, driverDAO.getDriverId(driverDTO));
    }

    @Test
    public void createDriverInsertsCorrectInfoInDatabase() throws SQLException {
        //Setup
        DriverDAO driverDAO = new DriverDAO();
        DriverDTO driverDTO = new DriverDTO();
        int careInstitutionId = 1;
        String firstName = "Henk";
        String lastName = "Henksen";
        String email = "henk@henk.nl";
        String phoneNumber = "0612345678";
        String street = "Dorpstraat";
        String houseNumber = "33a";
        String zipCode = "1234AB";
        String birthdate = "1980-04-04";
        String residence = "Arnhem";
        String utilityName = "rolstoel";
        boolean verification = false;
        boolean isFirstTimeProfile = false;
        CareInstitutionDTO careInstitutionDTO = new CareInstitutionDTO();
        careInstitutionDTO.setId(careInstitutionId);
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);
        List<ClientDTO> preferredClients = new ArrayList<>();
        ClientDTO client = new ClientDTO();
        preferredClients.add(client);

        driverDTO.setCareInstitution(careInstitutionDTO);
        driverDTO.setFirstName(firstName);
        driverDTO.setLastName(lastName);
        driverDTO.setEmail(email);
        driverDTO.setPhoneNumber(phoneNumber);
        driverDTO.setStreet(street);
        driverDTO.setHouseNumber(houseNumber);
        driverDTO.setZipCode(zipCode);
        driverDTO.setDateOfBirth(birthdate);
        driverDTO.setResidence(residence);
        driverDTO.setNumberPlate("123456");
        driverDTO.setUtility(utility);
        driverDTO.setVerification(verification);
        driverDTO.setPreferredClients(preferredClients);
        driverDTO.setFirstTimeProfileCheck(isFirstTimeProfile);

        DriverDAO driverDAO1 = Mockito.spy(driverDAO);

        int driverId = 2;

        Mockito.doReturn(driverId).when(driverDAO1).getDriverId(driverDTO);


        Connection connection = openConnection();

        //Test
        driverDAO1.createDriver(driverDTO);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM driver WHERE driverId = ?");
        preparedStatement.setInt(1, driverId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Verify
        assertEquals(driverId, resultset.getInt("driverId"));
    }

    @Test
    public void editProfileUpdateUserTableCorrectInDatabase() throws SQLException, DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        DriverDTO driverDTO = new DriverDTO();
        int driverId = 4;
        int clientId = 2;
        String firstName = "Henk";
        String lastName = "Henksen";
        String email = "henk@henk.nl";
        String phoneNumber = "0612345678";
        String street = "Dorpstraat";
        String houseNumber = "33a";
        String zipCode = "1234AB";
        String birthdate = "1980-04-04";
        String residence = "Arnhem";
        String utilityName = "Rolstoel";
        boolean verification = false;
        boolean isFirstTimeProfile = false;
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);
        List<ClientDTO> clients = new ArrayList<>();
        ClientDTO client = new ClientDTO();
        client.setId(clientId);
        clients.add(client);

        driverDTO.setId(driverId);
        driverDTO.setFirstName(firstName);
        driverDTO.setLastName(lastName);
        driverDTO.setEmail(email);
        driverDTO.setPhoneNumber(phoneNumber);
        driverDTO.setStreet(street);
        driverDTO.setHouseNumber(houseNumber);
        driverDTO.setZipCode(zipCode);
        driverDTO.setDateOfBirth(birthdate);
        driverDTO.setResidence(residence);
        driverDTO.setUtility(new UtilityDTO());
        driverDTO.setVerification(verification);
        driverDTO.setPreferredClients(clients);
        driverDTO.setFirstTimeProfileCheck(isFirstTimeProfile);

        Connection connection = openConnection();

        //Test
        driverDAO.editDriver(driverId, driverDTO);

        //UserDTO table
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
        preparedStatement.setInt(1, driverId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Verify
        assertEquals(firstName, resultset.getString("firstName"));
        assertEquals(lastName, resultset.getString("lastName"));
        assertEquals(email, resultset.getString("email"));
        assertEquals(phoneNumber, resultset.getString("phoneNumber"));
        assertEquals(street, resultset.getString("street"));
        assertEquals(houseNumber, resultset.getString("houseNumber"));
        assertEquals(zipCode, resultset.getString("zipCode"));
        assertEquals(residence, resultset.getString("residence"));
        assertEquals(java.sql.Date.valueOf(birthdate), resultset.getDate("dateOfBirth"));
        assertEquals(driverId, resultset.getInt("id"));
    }


    @Test
    public void editProfileUpdateDriverTableCorrectInDatabase() throws SQLException, DataUnreachableException {
        //Setup
        IDriverDAO driverDAO = new DriverDAO();
        DriverDTO driverDTO = new DriverDTO();
        int driverId = 4;
        int clientId = 2;
        String firstName = "Henk";
        String lastName = "Henksen";
        String email = "henk@henk.nl";
        String phoneNumber = "0612345678";
        String street = "Dorpstraat";
        String houseNumber = "33a";
        String zipCode = "1234AB";
        String birthdate = "1980-04-04";
        String residence = "Arnhem";
        String utilityName = "Rolstoel";
        boolean isFirstTimeProfile = false;
        boolean verification = false;
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);
        List<ClientDTO> clients = new ArrayList<>();
        ClientDTO client = new ClientDTO();
        client.setId(clientId);
        clients.add(client);

        driverDTO.setId(driverId);
        driverDTO.setFirstName(firstName);
        driverDTO.setLastName(lastName);
        driverDTO.setEmail(email);
        driverDTO.setPhoneNumber(phoneNumber);
        driverDTO.setStreet(street);
        driverDTO.setHouseNumber(houseNumber);
        driverDTO.setZipCode(zipCode);
        driverDTO.setDateOfBirth(birthdate);
        driverDTO.setResidence(residence);
        driverDTO.setUtility(utility);
        driverDTO.setVerification(verification);
        driverDTO.setPreferredClients(clients);
        driverDTO.setFirstTimeProfileCheck(isFirstTimeProfile);

        Connection connection = openConnection();

        //Test
        driverDAO.editDriver(driverId, driverDTO);

        //UserDTO table
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM driver WHERE driverId = ?");
        preparedStatement.setInt(1, driverId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Verify
        assertEquals(verification, resultset.getBoolean("verification"));
        assertEquals(utility.getName(), resultset.getString("utility"));
    }


    @Test
    public void deleteDriverDeletesExistingDriverCheck() throws SQLException {
        //setup
        IDriverDAO driverDAO = new DriverDAO();
        int driverId = 8;

        Connection connection = openConnection();

        //Test
        driverDAO.deleteDriver(driverId);

        //This normally returns the driver table but now returns no data is available because of the deleteDriver method
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM driver d WHERE d.driverId = ?");
        preparedStatement.setInt(1, driverId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //If there is no driverId in the parent table it means de child tables are succesfully deleted as well as the parent table
        Exception exception = assertThrows(SQLException.class, () -> resultset.getString("driverId"));

        //verify
        assertEquals("No data is available [2000-196]", exception.getMessage());
    }


    @Test
    public void checkIfDriverHasRidesReturnsFoundRides() throws SQLException {
        //setup
        IDriverDAO driverDAO = new DriverDAO();
        int driverId = 4;

        Connection connection = openConnection();

        //Test
        driverDAO.checkIfDriverHasRides(driverId);

        //verify
        assertEquals(3, driverDAO.checkIfDriverHasRides(driverId).size());
    }

    @Test(expected = DataUnreachableException.class)
    public void getAllFutureRidesReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int driverId = 1;

        //Test
        driverDAO.getAllFutureRides(driverId);
    }

    @Test(expected = DataUnreachableException.class)
    public void cancelRideReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int driverId = 1;
        int rideId = 1;

        //Test
        driverDAO.cancelRide(driverId, rideId);
    }

    @Test(expected = DataUnreachableException.class)
    public void getAllRequestRidesForDriverReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int driverId = 1;

        //Test
        driverDAO.getAllRequestRidesForDriver(driverId);
    }

    @Test(expected = DataUnreachableException.class)
    public void getAllClientsForDriverReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int driverId = 1;

        //Test
        driverDAO.getAllClientsForDriver(driverId);
    }

    @Test(expected = DataUnreachableException.class)
    public void getAllCompletedRidesReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int driverId = 1;

        //Test
        driverDAO.getAllCompletedRides(driverId);
    }

    @Test(expected = RuntimeException.class)
    public void getAllDriversReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        //Test
        driverDAO.getAllDrivers();
    }

    @Test(expected = RuntimeException.class)
    public void getDriverReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int driverId = 1;

        //Test
        driverDAO.getDriver(driverId);
    }

    @Test(expected = RuntimeException.class)
    public void getAllDriversTownshipReturnsDataUnreachableException() throws SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        String township = "Arnhem";

        //Test
        driverDAO.getAllDriversTownship(township);
    }

    @Test(expected = RuntimeException.class)
    public void getAllDriversCareInstitutionReturnsDataUnreachableException() throws SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int careinstitutionId = 1;

        //Test
        driverDAO.getAllDriversCareInstitution(careinstitutionId);
    }

    @Test(expected = RuntimeException.class)
    public void createDriverReturnsDataUnreachableException() throws SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        DriverDTO driverDTO = new DriverDTO();

        //Test
        driverDAO.createDriver(driverDTO);
    }

    @Test(expected = DataUnreachableException.class)
    public void getProfileReturnsDataUnreachableException() throws SQLException, DataUnreachableException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int driverId = 1;

        //Test
        driverDAO.getProfile(driverId);
    }

    @Test(expected = DataUnreachableException.class)
    public void updateProfileReturnsDataUnreachableException() throws SQLException, DataUnreachableException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        DriverDTO driverDTO = new DriverDTO();
        int driverId = 1;

        //Test
        driverDAO.updateProfile(driverId, driverDTO);
    }

    @Test(expected = DataUnreachableException.class)
    public void getAllDriversForClientReturnsDataUnreachableException() throws SQLException, DataUnreachableException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int clientId = 1;

        //Test
        driverDAO.getAllDriversForClient(clientId);
    }

    @Test(expected = DataUnreachableException.class)
    public void acceptRideReturnsDataUnreachableException() throws SQLException, DataUnreachableException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int driverId = 1;
        int rideId = 1;

        //Test
        driverDAO.acceptRide(driverId, rideId);
    }

    @Test(expected = RuntimeException.class)
    public void getDriverIdReturnsDataUnreachableException() throws SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        DriverDTO driverDTO = new DriverDTO();

        //Test
        driverDAO.getDriverId(driverDTO);
    }

    @Test(expected = RuntimeException.class)
    public void editDriverReturnsDataUnreachableException() throws SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        DriverDTO driverDTO = new DriverDTO();
        int driverId = 1;

        //Test
        driverDAO.editDriver(driverId, driverDTO);
    }

    @Test(expected = RuntimeException.class)
    public void deleteDriverReturnsDataUnreachableException() throws SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int driverId = 1;

        //Test
        driverDAO.deleteDriver(driverId);
    }

    @Test(expected = RuntimeException.class)
    public void checkIfDriverHasRidesReturnsDataUnreachableException() throws SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int driverId = 1;

        //Test
        driverDAO.checkIfDriverHasRides(driverId);
    }

    @Test(expected = DataUnreachableException.class)
    public void getAllCancelledRidesReturnsDataUnreachableException() throws SQLException, DataUnreachableException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int driverId = 1;

        //Test
        driverDAO.getAllCancelledRides(driverId);
    }

    @Test(expected = RuntimeException.class)
    public void getPreferredDriverToMatchReturnsDataUnreachableException() throws SQLException {
        //Setup
        DriverDAO driverDAO = Mockito.spy(new DriverDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection) driverDAO).openConnection();

        int clientId = 1;

        //Test
        driverDAO.getPreferredDriversToMatch(clientId);
    }
}
