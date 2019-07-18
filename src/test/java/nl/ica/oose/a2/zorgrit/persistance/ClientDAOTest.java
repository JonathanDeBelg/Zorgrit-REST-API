package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import nl.ica.oose.a2.zorgrit.persistance.util.Formatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
    public class ClientDAOTest extends DatabaseConnection {
    IClientDAO clientDAO;

    @Before
    public void setup() {
        clientDAO = new ClientDAO();
    }

    @Test
    public void testGetAllFutureRidesForClient() throws DataUnreachableException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();

        //Test
        ArrayList<RideDTO> rides = clientDAO.getAllFutureRides(2);

        //Verify
        assertEquals(2, rides.size(), 0);
    }

    @Test
    public void testGetAllCompletedRidesForClient() throws DataUnreachableException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        int clientId = 2;

        //Test
        ArrayList<RideDTO> rides = clientDAO.getAllCompletedRides(clientId);

        //Verify
        assertEquals(1, rides.size(), 0);
    }

    @Test
    public void testGetAllCancelledRidesForClient() throws DataUnreachableException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        int clientId = 2;

        //Test
        ArrayList<RideDTO> rides = clientDAO.getAllCancelledRides(clientId);

        //Verify
        assertEquals(1, rides.size(), 0);
    }

    @Test
    public void testGetAllCareInstitutions() throws DataUnreachableException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();

        //Test
        ArrayList<CareInstitutionDTO> careInstitutions = clientDAO.getAllCareInstitutions(2);

        //Verify
        assertEquals(5, careInstitutions.size(), 1);
    }

    @Test
    public void testGetProfileClient() throws DataUnreachableException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();

        //Test
        UserDTO user = clientDAO.getProfile(2);

        //Verify
        assertTrue(user instanceof ClientDTO);
    }

    @Test
    public void testGetProfile() throws DataUnreachableException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();

        //Test
        UserDTO userDTO = clientDAO.getProfile(2);

        //Verify
        assertTrue(userDTO.getFirstName().equalsIgnoreCase("Robin"));
    }

    @Test
    public void testCancelRideCorrectInDatabase() throws DataUnreachableException, SQLException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        int rideId = 2;
        Connection connection = openConnection();

        //Test
        clientDAO.cancelRide(rideId);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT cancelledByClient FROM ride WHERE id = ?");
        preparedStatement.setInt(1, rideId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Verify
        assertEquals(1, resultset.getInt("cancelledByClient"));
    }

    @Test(expected = SQLException.class)
    public void testCancelRideException() throws DataUnreachableException, SQLException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        int rideId = 2;
        Connection connection = openConnection();
        clientDAO.cancelRide(rideId);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT cancelledByClient FROM ride WHERE id = ?");
        preparedStatement.setInt(-99, rideId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Test
        clientDAO.cancelRide(rideId);
    }

    @Test
    public void testCancelRepeatRideCorrectInDatabase() throws DataUnreachableException, SQLException, InvalidDateFormatException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        int rideRepeatingRideId = 2;
        Connection connection = openConnection();

        //Test
        clientDAO.cancelRepeatingRides(rideRepeatingRideId);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT cancelledByClient FROM ride WHERE repeatingRideId = ?");
        preparedStatement.setInt(1, rideRepeatingRideId);
        ResultSet resultset = preparedStatement.executeQuery();

        //Verify
        //4 rows in database
        resultset.last();
        assertEquals(4, resultset.getRow());

        while (resultset.next()) {
            assertEquals(1, resultset.getInt("cancelledByClient"));
        }
    }

    @Test
    public void testAddRideCorrectInDatabase() throws SQLException, DataUnreachableException, InvalidDateFormatException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        RideDTO ride = new RideDTO();
        Connection connection = openConnection();

        int clientId = 2;
        int preferedCareInstitution = 1;
        int preferedDriver = 1;
        String pickUpDateTime = "2025-06-06 23:00:00";
        String pickUpLocation = "Dorpstraat 4";
        String dropOffLocation = "Dorplaan 35";
        int numberOfCompagnions = 2;
        int numberOffLuggage = 2;
        boolean returnRide = false;
        boolean callService = false;
        String utility = "rollator";
        int repeatingRideId = -1;
        boolean cancelledByClient = false;

        ride.setClientId(clientId);
        ride.setPreferedCareInstitution(preferedCareInstitution);
        ride.setPreferedDriver(preferedDriver);
        ride.setPickUpDateTime(pickUpDateTime);
        ride.setPickUpLocation(pickUpLocation);
        ride.setDropOffLocation(dropOffLocation);
        ride.setNumberOfCompanions(numberOfCompagnions);
        ride.setNumberOfLuggage(numberOffLuggage);
        ride.setReturnRide(returnRide);
        ride.setCallService(callService);
        ride.setUtility(utility);
        ride.setRepeatingRideId(repeatingRideId);
        ride.setCancelledByClient(cancelledByClient);

        //Test
        clientDAO.addRide(ride);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ride WHERE clientId = ? AND pickUpDateTime=?");
        preparedStatement.setInt(1, clientId);
        preparedStatement.setTimestamp(2, Timestamp.valueOf(pickUpDateTime));
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        //Verify
        assertEquals(clientId, resultSet.getInt("clientId"));
        assertEquals(preferedCareInstitution, resultSet.getInt("preferedCareInstitution"));
        assertEquals(pickUpDateTime, new Formatter().formatSQLTimestampToStringDateWithoutMilliseconds(resultSet.getTimestamp("PickUpDateTime")));
        assertEquals(pickUpLocation, resultSet.getString("pickUpLocation"));
        assertEquals(dropOffLocation, resultSet.getString("dropOffLocation"));
        assertEquals(numberOfCompagnions, resultSet.getInt("numberOfCompanions"));
        assertEquals(numberOffLuggage, resultSet.getInt("numberOfLuggage"));
        assertEquals(returnRide, resultSet.getBoolean("returnRide"));
        assertEquals(callService, resultSet.getBoolean("callService"));
        assertEquals(utility, resultSet.getString("utility"));
        assertEquals(repeatingRideId, resultSet.getInt("repeatingRideId"));
        assertEquals(cancelledByClient, resultSet.getBoolean("cancelledByClient"));
    }

    @Test
    public void testAddRideWithRepeatingRideIdValue0CorrectInDatabase() throws SQLException, DataUnreachableException, InvalidDateFormatException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        RideDTO ride = new RideDTO();
        Connection connection = openConnection();

        int clientId = 2;
        int preferedCareInstitution = 1;
        int preferedDriver = 1;
        String pickUpDateTime = "2025-06-06 23:00:00";
        String pickUpLocation = "Dorpstraat 4";
        String dropOffLocation = "Dorplaan 35";
        int numberOfCompagnions = 2;
        int numberOffLuggage = 2;
        boolean returnRide = false;
        boolean callService = false;
        String utility = "rollator";
        int repeatingRideId = 0;
        int generatedRepeatingRide = 3;
        boolean cancelledByClient = false;

        ride.setClientId(clientId);
        ride.setPreferedCareInstitution(preferedCareInstitution);
        ride.setPreferedDriver(preferedDriver);
        ride.setPickUpDateTime(pickUpDateTime);
        ride.setPickUpLocation(pickUpLocation);
        ride.setDropOffLocation(dropOffLocation);
        ride.setNumberOfCompanions(numberOfCompagnions);
        ride.setNumberOfLuggage(numberOffLuggage);
        ride.setReturnRide(returnRide);
        ride.setCallService(callService);
        ride.setUtility(utility);
        ride.setRepeatingRideId(repeatingRideId);
        ride.setCancelledByClient(cancelledByClient);

        //Test
        clientDAO.addRide(ride);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ride WHERE clientId = ? AND pickUpDateTime=?");
        preparedStatement.setInt(1, clientId);
        preparedStatement.setTimestamp(2, Timestamp.valueOf(pickUpDateTime));
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        //Verify
        assertEquals(clientId, resultSet.getInt("clientId"));
        assertEquals(preferedCareInstitution, resultSet.getInt("preferedCareInstitution"));
        assertEquals(pickUpDateTime, new Formatter().formatSQLTimestampToStringDateWithoutMilliseconds(resultSet.getTimestamp("PickUpDateTime")));
        assertEquals(pickUpLocation, resultSet.getString("pickUpLocation"));
        assertEquals(dropOffLocation, resultSet.getString("dropOffLocation"));
        assertEquals(numberOfCompagnions, resultSet.getInt("numberOfCompanions"));
        assertEquals(numberOffLuggage, resultSet.getInt("numberOfLuggage"));
        assertEquals(returnRide, resultSet.getBoolean("returnRide"));
        assertEquals(callService, resultSet.getBoolean("callService"));
        assertEquals(utility, resultSet.getString("utility"));
        assertEquals(generatedRepeatingRide, resultSet.getInt("repeatingRideId"));
        assertEquals(cancelledByClient, resultSet.getBoolean("cancelledByClient"));
    }

    @Test
    public void testUpdateProfileUpdateClientTableCorrectInDatabase() throws SQLException, DataUnreachableException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        int clientId = 2;
        int driverId = 1;
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
        String compagnion = "Henk Jansen";
        boolean isDriverPreferenceForced = false;
        boolean verification = false;
        boolean firstTimeProfileCheck = false;
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setId(driverId);
        ArrayList<DriverDTO> preferredDrivers = new ArrayList<DriverDTO>();
        preferredDrivers.add(driverDTO);

        ClientDTO client = new ClientDTO();
        client.setId(clientId);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhoneNumber(phoneNumber);
        client.setStreet(street);
        client.setHouseNumber(houseNumber);
        client.setZipCode(zipCode);
        client.setDateOfBirth(birthdate);
        client.setResidence(residence);
        client.setUtility(utility);
        client.setDriverPreferenceForced(verification);
        client.setCompanion(compagnion);
        client.setPreferredDrivers(preferredDrivers);
        client.setFirstTimeProfileCheck(firstTimeProfileCheck);

        Connection connection = openConnection();

        //Test
        clientDAO.updateProfile(clientId, client);

        //UserDTO table
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM client WHERE clientId = ?");
        preparedStatement.setInt(1, clientId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Verify
        assertEquals(compagnion, resultset.getString("companion"));
        assertEquals(utility.getName(), resultset.getString("utility"));
        assertEquals(isDriverPreferenceForced, resultset.getBoolean("driverPreferenceForced"));
    }

    @Test
    public void testUpdateProfileUpdateUserTableCorrectInDatabase() throws SQLException, DataUnreachableException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        int clientId = 2;
        int driverId = 1;
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
        String compagnion = "Henk Jansen";
        boolean isDriverPreferenceForced = false;
        boolean verification = false;
        boolean firstTimeProfileCheck = false;
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setId(driverId);
        ArrayList<DriverDTO> preferredDrivers = new ArrayList<DriverDTO>();
        preferredDrivers.add(driverDTO);

        ClientDTO client = new ClientDTO();
        client.setId(clientId);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhoneNumber(phoneNumber);
        client.setStreet(street);
        client.setHouseNumber(houseNumber);
        client.setZipCode(zipCode);
        client.setDateOfBirth(birthdate);
        client.setResidence(residence);
        client.setUtility(utility);
        client.setDriverPreferenceForced(verification);
        client.setCompanion(compagnion);
        client.setPreferredDrivers(preferredDrivers);
        client.setFirstTimeProfileCheck(firstTimeProfileCheck);

        Connection connection = openConnection();

        //Test
        clientDAO.updateProfile(clientId, client);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
        preparedStatement.setInt(1, clientId);
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
        assertEquals(clientId, resultset.getInt("id"));
    }

    @Test
    public void testUpdateProfileUpdateClientDriverPreferenceTableCorrectInDatabase() throws SQLException, DataUnreachableException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        int clientId = 2;
        int driverId = 1;
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
        String compagnion = "Henk Jansen";
        boolean isDriverPreferenceForced = false;
        boolean verification = false;
        boolean firstTimeProfileCheck = false;
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setId(driverId);
        ArrayList<DriverDTO> preferredDrivers = new ArrayList<DriverDTO>();
        preferredDrivers.add(driverDTO);

        ClientDTO client = new ClientDTO();
        client.setId(clientId);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhoneNumber(phoneNumber);
        client.setStreet(street);
        client.setHouseNumber(houseNumber);
        client.setZipCode(zipCode);
        client.setDateOfBirth(birthdate);
        client.setResidence(residence);
        client.setUtility(utility);
        client.setDriverPreferenceForced(verification);
        client.setCompanion(compagnion);
        client.setPreferredDrivers(preferredDrivers);
        client.setFirstTimeProfileCheck(firstTimeProfileCheck);

        Connection connection = openConnection();

        //Test
        clientDAO.updateProfile(clientId, client);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM clientDriverPreference WHERE clientId = ?");
        preparedStatement.setInt(1, clientId);
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Verify
        resultset.last();
        assertEquals(1, resultset.getRow());
        assertEquals(clientId, resultset.getInt("clientId"));
        assertEquals(driverId, resultset.getInt("driverId"));
    }

    @Test
    public void getAllClientsReturnsACorrectList() throws DataUnreachableException {
        //Test
        List<ClientDTO> clients = clientDAO.getAllClientsCareInstitution(1);

        //Verify
        assertEquals(1, clients.size(), 0);
    }

    @Test
    public void getClientReturnsTheCorrectClient() throws DataUnreachableException {
        int clientId = 2;
        String correctNameOfClient2 = "Robin";
        //Test
        ClientDTO client = clientDAO.getClient(clientId);

        //Verify
        assertEquals(correctNameOfClient2, client.getFirstName());
    }

    @Test
    public void addClientAddsAClientToTheDatabase() throws SQLException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        Connection connection = openConnection();

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
        String compagnion = "Henk Jansen";
        boolean isDriverPreferenceForced = false;
        boolean verification = false;
        boolean firstTimeProfileCheck = false;
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);

        CareInstitutionDTO careInstitution = new CareInstitutionDTO();
        careInstitution.setId(1);
        List<LimitationDTO> limitations = new ArrayList<>();
        LimitationDTO limitation = new LimitationDTO();
        limitation.setName("ouderen");
        limitations.add(limitation);

        ClientDTO client = new ClientDTO();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhoneNumber(phoneNumber);
        client.setStreet(street);
        client.setHouseNumber(houseNumber);
        client.setZipCode(zipCode);
        client.setDateOfBirth(birthdate);
        client.setResidence(residence);
        client.setUtility(utility);
        client.setDriverPreferenceForced(verification);
        client.setCompanion(compagnion);
        client.setFirstTimeProfileCheck(firstTimeProfileCheck);
        client.setCareInstitution(careInstitution);
        client.setLimitations(limitations);

        clientDAO.addClient(client);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
        preparedStatement.setInt(1, 14);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        assertEquals(firstName, resultSet.getString("firstname"));
    }

    @Test
    public void editClientChangesTheNameOfClientWiedo() throws SQLException {
        //Setup
        IClientDAO clientDAO = new ClientDAO();
        Connection connection = openConnection();

        int clientId = 2;
        String firstName = "Henk";
        String lastName = "Schuiling";
        String email = "robin@hotmail.com";
        String phoneNumber = "0687654321";
        String street = "Dorpstraat";
        String houseNumber = "33";
        String zipCode = "1234EF";
        String birthdate = "1997-10-07";
        String residence = "Arnhem";
        String utilityName = "rolstoel";
        String compagnion = "Sven";
        boolean isDriverPreferenceForced = false;
        boolean verification = false;
        boolean firstTimeProfileCheck = false;
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);

        CareInstitutionDTO careInstitution = new CareInstitutionDTO();
        careInstitution.setId(1);
        List<LimitationDTO> limitations = new ArrayList<>();
        LimitationDTO limitation = new LimitationDTO();
        limitation.setName("zware/Fysieke handicap");
        limitations.add(limitation);

        DriverDTO driver = new DriverDTO();
        driver.setId(4);
        List<DriverDTO> preferredDrivers = new ArrayList<>();
        preferredDrivers.add(driver);

        ClientDTO client = new ClientDTO();
        client.setId(clientId);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhoneNumber(phoneNumber);
        client.setStreet(street);
        client.setHouseNumber(houseNumber);
        client.setZipCode(zipCode);
        client.setDateOfBirth(birthdate);
        client.setResidence(residence);
        client.setUtility(utility);
        client.setDriverPreferenceForced(verification);
        client.setCompanion(compagnion);
        client.setFirstTimeProfileCheck(firstTimeProfileCheck);
        client.setCareInstitution(careInstitution);
        client.setLimitations(limitations);
        client.setPreferredDrivers(preferredDrivers);

        clientDAO.editClient(clientId, client);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
        preparedStatement.setInt(1, clientId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        assertEquals(firstName, resultSet.getString("firstname"));
    }

    @Test
    public void getAllLimitationsReturnsACorrectList() throws DataUnreachableException {
        //Test
        List<LimitationDTO> limitations = clientDAO.getLimitations();

        //Verify
        assertEquals(3, limitations.size(), 0);
    }

    @Test
    public void getAllUtilitiesReturnsACorrectList() throws DataUnreachableException {
        //Test
        List<UtilityDTO> utlities = clientDAO.getUtilities();

        //Verify
        assertEquals(3, utlities.size(), 0);
    }

//    @Test
//    public void getClientUtilitiesReturnsACorrectList() throws DataUnreachableException {
//        int clientId = 2;
//
//        //Test
//        List<UtilityDTO> utlities = clientDAO.getClientUtilities(clientId);
//
//        //Verify
//        assertEquals(3, utlities.size(), 0);
//    }

    @Test
    public void getRidesOfClientReturnsACorrectList() throws DataUnreachableException {
        int clientId = 2;

        //Test
        List<RideDTO> rides = clientDAO.getAllFutureRides(clientId);

        //Verify
        assertEquals(2, rides.size(), 0);
    }

    @Test
    public void deleteAllClientsTablesEmptyList() throws DataUnreachableException, SQLException {
        int clientId = 5;
        Connection connection = openConnection();

        clientDAO.deleteAllTablesForClient(clientId);

        //Test
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
        preparedStatement.setInt(1, clientId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        //Verify
        assertEquals(null, resultSet.getRow(), 0);
    }

    @Test(expected = RuntimeException.class)
    public void getAllClientsReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        ClientDAO clientDAO = Mockito.spy(new ClientDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)clientDAO).openConnection();

        //Test
        clientDAO.getAllClientsCareInstitution(1);
    }

    @Test(expected = RuntimeException.class)
    public void getClientReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        ClientDAO clientDAO = Mockito.spy(new ClientDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)clientDAO).openConnection();

        int clientId = 1;
        //Test
        clientDAO.getClient(clientId);
    }

    @Test(expected = RuntimeException.class)
    public void addClientReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        ClientDAO clientDAO = Mockito.spy(new ClientDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)clientDAO).openConnection();

        ClientDTO clientDTO = new ClientDTO();
        //Test
        clientDAO.addClient(clientDTO);
    }

    @Test(expected = RuntimeException.class)
    public void editClientReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        ClientDAO clientDAO = Mockito.spy(new ClientDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)clientDAO).openConnection();

        ClientDTO clientDTO = new ClientDTO();
        int clientId = 1;
        //Test
        clientDAO.editClient(clientId, clientDTO);
    }

    @Test(expected = RuntimeException.class)
    public void getLimitationsReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        ClientDAO clientDAO = Mockito.spy(new ClientDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)clientDAO).openConnection();

        //Test
        clientDAO.getLimitations();
    }

    @Test(expected = RuntimeException.class)
    public void getUtilitiesReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        ClientDAO clientDAO = Mockito.spy(new ClientDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)clientDAO).openConnection();

        //Test
        clientDAO.getUtilities();
    }

    @Test(expected = RuntimeException.class)
    public void getClientUtilitiesReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        ClientDAO clientDAO = Mockito.spy(new ClientDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)clientDAO).openConnection();

        int clientId = 1;

        //Test
        clientDAO.getClientUtilities(clientId);
    }

    @Test(expected = RuntimeException.class)
    public void checkIfClientHasRidesReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        ClientDAO clientDAO = Mockito.spy(new ClientDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)clientDAO).openConnection();

        int clientId = 1;

        //Test
        clientDAO.checkIfClientHasRides(clientId);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllTablesForClientReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        ClientDAO clientDAO = Mockito.spy(new ClientDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)clientDAO).openConnection();

        int clientId = 1;

        //Test
        clientDAO.deleteAllTablesForClient(clientId);
    }

    @Test(expected = DataUnreachableException.class)
    public void getPreferredDriversReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        ClientDAO clientDAO = Mockito.spy(new ClientDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)clientDAO).openConnection();

        int clientId = 1;

        //Test
        clientDAO.getPreferredDrivers(clientId);
    }
}