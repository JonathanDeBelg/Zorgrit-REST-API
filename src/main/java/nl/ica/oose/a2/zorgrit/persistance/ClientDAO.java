package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableRuntimeException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;
import nl.ica.oose.a2.zorgrit.persistance.util.Bind;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import nl.ica.oose.a2.zorgrit.persistance.util.QueryType;
import nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache.RideMatchCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static nl.ica.oose.a2.zorgrit.persistance.DriverDAO.DATABASE_ERROR_MESSAGE;
import static nl.ica.oose.a2.zorgrit.persistance.DriverDAO.UTILITY;

public class ClientDAO extends DatabaseConnection implements IClientDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDAO.class);

    public int addRide(final RideDTO ride) throws DataUnreachableException, InvalidDateFormatException {
        Connection connection;
        PreparedStatement statement;
        int repeatingId = ride.getRepeatingRideId();

        try {
            connection = openConnection();
            int key;

            if (ride.getRepeatingRideId() == 0) {
                repeatingId = nextMaxReaptingId(connection);
            }

            statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_RIDE_ADD), Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, ride.getClientId());
            if (ride.getPreferedCareInstitution() != 0) {
                statement.setInt(2, ride.getPreferedCareInstitution());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            if (ride.getPreferedDriver() != 0) {
                statement.setInt(3, ride.getPreferedDriver());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            statement.setTimestamp(4, Timestamp.valueOf(ride.getPickUpDateTime()));
            statement.setString(5, ride.getPickUpLocation());
            statement.setString(6, ride.getDropOffLocation());
            statement.setInt(7, ride.getNumberOfCompanions());
            statement.setInt(8, ride.getNumberOfLuggage());
            statement.setBoolean(9, ride.isReturnRide());
            statement.setBoolean(10, ride.isCallService());
            statement.setString(11, ride.getUtility());
            statement.setInt(12, repeatingId);
            statement.setBoolean(13, ride.isCancelledByClient());
            statement.setInt(14, ride.getDistance());
            statement.setInt(15, ride.getDuration());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) {
                key = keys.getInt(1);
            } else {
                throw new DataUnreachableException();
            }

            closeConnection(connection, statement);

            return key;
        } catch (SQLException e) {
            LOGGER.error("clientDAO addRide: " + e);
            throw new DataUnreachableException();
        }
    }

    public ArrayList<RideDTO> getAllCancelledRides(final int clientId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_RIDES_GET_ALL_CANCELLED));
            statement.setInt(1, clientId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rides.add(new Bind().ride(resultSet));
            }

            closeConnection(connection, statement);

            return rides;
        } catch (SQLException e) {
            LOGGER.error("clientDAO getAllCancelledRides: " + e);
            throw new DataUnreachableException();
        }
    }

    public ArrayList<RideDTO> getAllCompletedRides(final int clientId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_RIDES_GET_ALL_COMPLETED));
            statement.setInt(1, clientId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RideDTO temp = new Bind().ride(resultSet);
                if (resultSet.getInt("rideId") != 0) {
                    temp.setRating(new Bind().rideRating(resultSet));
                }
                rides.add(temp);
            }

            closeConnection(connection, statement);

            return rides;
        } catch (SQLException e) {
            LOGGER.error("clientDAO getAllCompletedRides: " + e);
            throw new DataUnreachableException();
        }
    }

    public ArrayList<RideDTO> getAllFutureRides(final int clientId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_RIDES_GET_ALL_FUTURE));
            statement.setInt(1, clientId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rides.add(new Bind().ride(resultSet));
            }

            closeConnection(connection, statement);

            return rides;
        } catch (SQLException e) {
            LOGGER.error("clientDAO getAllFutureRides: " + e);
            throw new DataUnreachableException();
        }
    }

    public ArrayList<CareInstitutionDTO> getAllCareInstitutions(final int clientId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        ArrayList<CareInstitutionDTO> careInstitutions = new ArrayList<CareInstitutionDTO>();

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.CARE_INSTITUTION_GET_ALL));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                CareInstitutionDTO careInstitution = new CareInstitutionDTO();
                careInstitution.setId(resultSet.getInt("id"));
                careInstitution.setName(resultSet.getString("name"));

                careInstitutions.add(careInstitution);
            }

            closeConnection(connection, statement);

            return careInstitutions;
        } catch (SQLException e) {
            LOGGER.error("clientDAO getAllCareInstitutions: " + e);
            throw new DataUnreachableException();
        }
    }

    public void cancelRide(final int rideId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_RIDE_CANCEL));
            statement.setInt(1, rideId);
            statement.execute();

            RideMatchCache.Instance().stopMatching(rideId);
            closeConnection(connection, statement);
        } catch (SQLException e) {
            LOGGER.error("clientDAO cancelRide: " + e);
            throw new DataUnreachableException();
        }
    }

    public void cancelRepeatingRides(final int repeatRideId) throws DataUnreachableException, InvalidDateFormatException {
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_RIDE_CANCEL_REPEATING));
            statement.setInt(1, repeatRideId);
            statement.execute();
            closeConnection(connection, statement);
        } catch (SQLException e) {
            LOGGER.error("clientDAO cancelRepeatingRides: " + e);
            throw new DataUnreachableException();
        }
    }

    public void updateProfile(final int clientId, final ClientDTO client) throws DataUnreachableException {
        Connection connection;

        try {
            connection = openConnection();
            client.setId(clientId);
            updateProfileClientTable(connection, client);
            updateProfileUserTable(connection, client);
            deletePreferredDrivers(connection, clientId);
            updatePreferredDrivers(connection, clientId, client.getPreferredDrivers());

            connection.close();
        } catch (SQLException e) {
            LOGGER.error("clientDAO updateProfile: " + e);
            throw new DataUnreachableException();
        }
    }

    private void updateClientLimitations(Connection connection, int clientId, List<LimitationDTO> limitations) throws SQLException {
        PreparedStatement statementClientLimitationsUpdate;

        statementClientLimitationsUpdate = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_ADD_LIMITATION));
        for (LimitationDTO limitationDTO : limitations) {
            statementClientLimitationsUpdate.setInt(1, clientId);
            statementClientLimitationsUpdate.setString(2, limitationDTO.getName());
            statementClientLimitationsUpdate.executeUpdate();
        }
        statementClientLimitationsUpdate.close();
    }

    private void deleteClientLimitations(Connection connection, int clientId) throws SQLException {
        PreparedStatement statementClientLimitationsDelete;

        statementClientLimitationsDelete = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_DELETE_LIMITATION));
        statementClientLimitationsDelete.setInt(1, clientId);
        statementClientLimitationsDelete.executeUpdate();
        statementClientLimitationsDelete.close();
    }

    private void updateProfileUserTable(Connection connection, ClientDTO client) throws SQLException {

        PreparedStatement statement;

        statement = connection.prepareStatement(queries.getQuery(QueryType.USER_UPDATE_PROFILE));
        statement.setString(1, client.getFirstName());
        statement.setString(2, client.getLastName());
        statement.setString(3, client.getEmail());
        statement.setString(4, client.getPhoneNumber());
        statement.setString(5, client.getStreet());
        statement.setString(6, client.getHouseNumber());
        statement.setString(7, client.getZipCode());
        statement.setString(8, client.getResidence());
        statement.setDate(9, java.sql.Date.valueOf(client.getDateOfBirth()));
        statement.setBoolean(10, client.isFirstTimeProfileCheck());
        statement.setInt(11, client.getId());

        statement.executeUpdate();
        statement.close();
    }

    private void updateProfileClientTable(Connection connection, ClientDTO client) throws SQLException {
        PreparedStatement statement;

        statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_UPDATE_PROFILE));
        statement.setString(1, client.getCompanion());
        statement.setString(2, client.getUtility().getName());
        statement.setBoolean(3, client.isDriverPreferenceForced());
        statement.setInt(4, client.getId());

        statement.executeUpdate();
        statement.close();
    }

    public ClientDTO getProfile(final int clientId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statementProfile;

        try {
            connection = openConnection();
            ClientDTO client = new ClientDTO();
            statementProfile = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_PROFILE_GET));
            statementProfile.setInt(1, clientId);
            ResultSet resultset = statementProfile.executeQuery();

            if (resultset.next()) {
                client = new Bind().client(resultset);
                client.setUtility(createUtility(resultset.getString(UTILITY)));
                client.setPreferredDrivers(getPreferredDrivers(clientId));
                client.setLimitations(getLimitations(connection, clientId));
                client.setCareInstitution(getCareInstitution(connection, clientId));
            }

            closeConnection(connection, statementProfile);
            client.setPreferences(new UserDAO().getUserPreferencesByUserId(clientId));

            return client;
        } catch (SQLException e) {
            LOGGER.error("clientDAO getProfile: " + e);
            throw new DataUnreachableException();
        }
    }

    public ArrayList<DriverDTO> getPreferredDrivers(final int clientId) throws DataUnreachableException {
        PreparedStatement statementDrivers;
        Connection connection;

        ArrayList<DriverDTO> driverDTOS = new ArrayList<DriverDTO>();

        try {
            connection = openConnection();
            statementDrivers = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_PREFERED_DRIVERS_GET));
            statementDrivers.setInt(1, clientId);
            ResultSet resultSetDriver = statementDrivers.executeQuery();

            while (resultSetDriver.next()) {
                DriverDTO driverDTO = new Bind().driver(resultSetDriver);
                driverDTO.setUtility(createUtility(resultSetDriver.getString(UTILITY)));
                driverDTO.setNumberOfPassengers(resultSetDriver.getInt("numberOfPassengers"));
                driverDTOS.add(driverDTO);
            }

            closeConnection(connection, statementDrivers);
        } catch (SQLException e) {
            LOGGER.error("clientDAO getPreferredDrivers: " + e);
            throw new DataUnreachableException();
        }

        return driverDTOS;
    }

    private UtilityDTO createUtility(final String utilityName) {
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);

        return utility;
    }

    private CareInstitutionDTO getCareInstitution(Connection connection, final int clientId) throws SQLException {
        PreparedStatement statementCareInstitution;

        CareInstitutionDTO careInstitution = new CareInstitutionDTO();
        statementCareInstitution = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_CAREINSTITUTION_GET));
        statementCareInstitution.setInt(1, clientId);
        ResultSet resultSetCareInstitution = statementCareInstitution.executeQuery();

        while (resultSetCareInstitution.next()) {
            careInstitution.setId(resultSetCareInstitution.getInt("id"));
            careInstitution.setName(resultSetCareInstitution.getString("name"));
        }

        statementCareInstitution.close();
        return careInstitution;
    }

    private List<LimitationDTO> getLimitations(Connection connection, final int clientId) throws SQLException {
        PreparedStatement statementLimitations;

        List<LimitationDTO> limitations = new ArrayList<LimitationDTO>();
        statementLimitations = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_LIMITATIONS_GET));
        statementLimitations.setInt(1, clientId);
        ResultSet resultSetLimitations = statementLimitations.executeQuery();

        while (resultSetLimitations.next()) {
            LimitationDTO limitation = new LimitationDTO();
            limitation.setName(resultSetLimitations.getString("name"));
            limitations.add(limitation);
        }
        statementLimitations.close();
        return limitations;
    }

    private void deletePreferredDrivers(Connection connection, final int clientId) throws SQLException {
        PreparedStatement statementPreferredDriversDelete;

        statementPreferredDriversDelete = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_PREFERRED_DRIVERS_DELETE));
        statementPreferredDriversDelete.setInt(1, clientId);
        statementPreferredDriversDelete.executeUpdate();
        statementPreferredDriversDelete.close();
    }

    private void updatePreferredDrivers(Connection connection, final int clientId, final List<DriverDTO> driverDTOS) throws SQLException {
        PreparedStatement statementPreferredDrivers;

        statementPreferredDrivers = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_PREFERRED_DRIVERS_UPDATE));
        for (DriverDTO driverDTO : driverDTOS) {
            statementPreferredDrivers.setInt(1, clientId);
            statementPreferredDrivers.setInt(2, driverDTO.getId());
            statementPreferredDrivers.executeUpdate();
        }
        statementPreferredDrivers.close();
    }

    private int nextMaxReaptingId(Connection connection) throws SQLException {
        PreparedStatement statementgetMax;
        int max = 1;
        statementgetMax = connection.prepareStatement(queries.getQuery(QueryType.RIDE_REPEATING_MAX_ID));
        ResultSet resultSet = statementgetMax.executeQuery();

        while (resultSet.next()) {
            max = resultSet.getInt("max") + 1;
        }
        return max;
    }

    @Override
    public List<ClientDTO> getAllClientsCareInstitution(int careInstitution) {
        Connection connection;
        PreparedStatement statement;

        List<ClientDTO> clientsList = new ArrayList<>();

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENTS_GET_ALL));
            statement.setInt(1, careInstitution);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ClientDTO client = new ClientDTO();
                fillClientDTO(client, resultSet);
                clientsList.add(client);
            }

            closeConnection(connection, statement);
            return clientsList;
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public ClientDTO getClient(int clientId) {
        Connection connection;
        PreparedStatement statement;
        ClientDTO client = null;

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_GET));
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                client = new ClientDTO();
                fillClientDTO(client, resultSet);
            }

            closeConnection(connection, statement);

            return client;
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public void addClient(ClientDTO client) {
        try (
                Connection connection = openConnection();
                PreparedStatement addClient = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_ADD));
                PreparedStatement addUser = connection.prepareStatement(queries.getQuery(QueryType.USER_ADD), Statement.RETURN_GENERATED_KEYS);
                PreparedStatement addClientCareInstitution = connection.prepareStatement(queries.getQuery(QueryType.ADD_CLIENT_TO_CAREINSTITUTION));
                PreparedStatement addClientLimitation = connection.prepareStatement(queries.getQuery(QueryType.ADD_LIMITATION_TO_CLIENT))
        ) {
            addUser.setString(1, client.getFirstName());
            addUser.setString(2, client.getLastName());
            addUser.setString(3, client.getEmail());
            addUser.setString(4, client.getPhoneNumber());
            addUser.setString(5, client.getStreet());
            addUser.setString(6, client.getHouseNumber());
            addUser.setString(7, client.getZipCode());
            addUser.setString(8, client.getResidence());
            addUser.setString(9, client.getDateOfBirth());

            addUser.executeUpdate();

            ResultSet generatedID = addUser.getGeneratedKeys();

            if (generatedID.next()) {
                addClient.setInt(1, generatedID.getInt(1));
                addClient.setString(2, client.getCompanion());
                addClient.setString(3, client.getUtility().getName());
                addClient.setBoolean(4, client.isDriverPreferenceForced());

                addClientCareInstitution.setInt(1, generatedID.getInt(1));
                addClientCareInstitution.setInt(2, client.getCareInstitution().getId());

                List<LimitationDTO> limitations = client.getLimitations();

                for (LimitationDTO limitation : limitations) {
                    addClientLimitation.setInt(1, generatedID.getInt(1));
                    addClientLimitation.setString(2, limitation.getName());
                }
            }

            addClient.executeUpdate();
            addClientCareInstitution.executeUpdate();
            addClientLimitation.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Database error: ", e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public void editClient(int userId, ClientDTO client) {
        try (
                Connection connection = openConnection()
        ) {
            //deze is niet relevant voor de beheeromgeving dus wordt op false gezet
            client.setFirstTimeProfileCheck(false);
            client.setId(userId);
            updateProfileClientTable(connection, client);
            updateProfileUserTable(connection, client);
            deletePreferredDrivers(connection, userId);
            updatePreferredDrivers(connection, userId, client.getPreferredDrivers());
            deleteClientLimitations(connection, userId);
            updateClientLimitations(connection, userId, client.getLimitations());
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public List<LimitationDTO> getLimitations() {
        ArrayList<LimitationDTO> limitationArray = new ArrayList<>();

        try (
                Connection connection = openConnection();
                PreparedStatement limitations = connection.prepareStatement(queries.getQuery(QueryType.LIMITATIONS_GET));
                ResultSet resultSet = limitations.executeQuery()
        ) {

            while (resultSet.next()) {
                LimitationDTO limitation = new LimitationDTO();
                limitation.setName(resultSet.getString("name"));

                limitationArray.add(limitation);
            }
            return limitationArray;
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public List<UtilityDTO> getUtilities() {
        ArrayList<UtilityDTO> utilitiesArray = new ArrayList<>();

        try (
                Connection connection = openConnection();
                PreparedStatement utilities = connection.prepareStatement(queries.getQuery(QueryType.UTILITIES_GET));
                ResultSet resultSet = utilities.executeQuery()
        ) {

            while (resultSet.next()) {
                UtilityDTO utilityDTO = new UtilityDTO();
                utilityDTO.setName(resultSet.getString("name"));

                utilitiesArray.add(utilityDTO);
            }

            return utilitiesArray;
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public List<UtilityDTO> getClientUtilities(int userId) {
        ArrayList<UtilityDTO> utilitiesArray = new ArrayList<>();

        try (
                Connection connection = openConnection();
                PreparedStatement utilities = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_UTILITIES_GET))
        ) {
            utilities.setInt(1, userId);
            ResultSet resultSet = utilities.executeQuery();

            while (resultSet.next()) {
                UtilityDTO utilityDTO = new UtilityDTO();
                utilityDTO.setName(resultSet.getString(UTILITY));

                utilitiesArray.add(utilityDTO);
            }
            return utilitiesArray;
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public List<RideDTO> checkIfClientHasRides(int clientId) {
        ArrayList<RideDTO> rides = new ArrayList<>();
        try (
                Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_CHECK_RIDES))
        ) {
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                RideDTO ride = new Bind().ride(resultSet);
                rides.add(ride);
            }
            if (!rides.isEmpty()) {
                return rides;
            }

            closeConnection(connection, statement);
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return rides;
    }

    @Override
    public void deleteAllTablesForClient(int clientId) {
        try (
                Connection connection = openConnection();
                PreparedStatement clientLimitationDelete = connection.prepareStatement(queries.getQuery(QueryType.CLIENTLIMITATION_DELETE));
                PreparedStatement clientDeleteDriverpreference = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_DELETE_DRIVERPREFERENCE));
                PreparedStatement clientDriverPreferenceDelete = connection.prepareStatement(queries.getQuery(QueryType.CLIENTDRIVERPREFERENCE_DELETE));
                PreparedStatement clientCareinstitutionDelete = connection.prepareStatement(queries.getQuery(QueryType.CLIENTCAREINSTITUTION_DELETE));
                PreparedStatement clientDelete = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_DELETE));
                PreparedStatement userDelete = connection.prepareStatement(queries.getQuery(QueryType.USER_DELETE))
        ) {
            statementExecuteUpdateForDeleteClient(clientId, clientLimitationDelete, clientDeleteDriverpreference, clientDriverPreferenceDelete, clientCareinstitutionDelete, clientDelete, userDelete);
        } catch (SQLException e) {
            LOGGER.error("Database error: ", e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    private void statementExecuteUpdateForDeleteClient(
            int clientId, PreparedStatement clientLimitationDelete, PreparedStatement clientDeleteDriverpreference,
            PreparedStatement clientDriverPreferenceDelete, PreparedStatement clientCareinstitutionDelete,
            PreparedStatement clientDelete, PreparedStatement userDelete
    ) throws SQLException {
        clientDeleteDriverpreference.setInt(1, clientId);
        clientDeleteDriverpreference.executeUpdate();

        clientLimitationDelete.setInt(1, clientId);
        clientLimitationDelete.executeUpdate();

        clientCareinstitutionDelete.setInt(1, clientId);
        clientCareinstitutionDelete.executeUpdate();

        clientDriverPreferenceDelete.setInt(1, clientId);
        clientDriverPreferenceDelete.executeUpdate();

        clientDelete.setInt(1, clientId);
        clientDelete.executeUpdate();

        userDelete.setInt(1, clientId);
        userDelete.executeUpdate();
    }

    private void fillClientDTO(ClientDTO user, ResultSet resultSet) throws SQLException {
        UtilityDTO utility = new UtilityDTO();
        utility.setName(resultSet.getString(UTILITY));
        user.setId(resultSet.getInt("id"));
        user.setFirstName(resultSet.getString("firstName"));
        user.setLastName(resultSet.getString("lastName"));
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNumber(resultSet.getString("phoneNumber"));
        user.setStreet(resultSet.getString("street"));
        user.setHouseNumber(resultSet.getString("houseNumber"));
        user.setZipCode(resultSet.getString("zipCode"));
        user.setResidence(resultSet.getString("residence"));
        user.setDateOfBirth(resultSet.getString("dateOfBirth"));
        user.setCompanion(resultSet.getString("companion"));
        user.setUtility(utility);
        user.setDriverPreferenceForced(resultSet.getBoolean("driverPreferenceForced"));
    }
}
