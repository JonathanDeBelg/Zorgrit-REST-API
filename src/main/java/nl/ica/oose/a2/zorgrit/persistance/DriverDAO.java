package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableRuntimeException;
import nl.ica.oose.a2.zorgrit.persistance.util.Bind;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import nl.ica.oose.a2.zorgrit.persistance.util.QueryType;
import nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache.RideMatchCache;
import nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache.RideMatchProposedDriverCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DriverDAO extends DatabaseConnection implements IDriverDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverDAO.class);
    static final String DATABASE_ERROR_MESSAGE = "Database error: ";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String ID = "id";
    static final String UTILITY = "utility";

    public ArrayList<RideDTO> getAllFutureRides(final int driverId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_RIDES_GET_ALL_FUTURE));
            statement.setInt(1, driverId);
            statement.setInt(2, driverId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rides.add(new Bind().ride(resultSet));
            }

            closeConnection(connection, statement);

            return rides;
        } catch (SQLException e) {
            LOGGER.error("DriverDAO getAllFutureRides: ", e);
            throw new DataUnreachableException();
        }
    }

    public void cancelRide(final int driverId, final int rideId) throws DataUnreachableException {
        Connection connection;
        try {
            connection = openConnection();

            PreparedStatement cancelRideStatement = connection.prepareStatement(
                    queries.getQuery(QueryType.DRIVER_RIDE_CANCEL));
            cancelRideStatement.setInt(1, rideId);
            cancelRideStatement.setInt(2, driverId);
            cancelRideStatement.execute();
            cancelRideStatement.close();

            PreparedStatement removeDriverIdFromRideStatement = connection.prepareStatement(
                    queries.getQuery(QueryType.REMOVE_DRIVER_FROM_RIDE));
            removeDriverIdFromRideStatement.setInt(1, rideId);
            removeDriverIdFromRideStatement.execute();

            closeConnection(connection, removeDriverIdFromRideStatement);
        } catch (SQLException e) {
            LOGGER.error("DriverDAO cancelRide: ", e);
            throw new DataUnreachableException();
        }
    }

    public ArrayList<RideDTO> getAllRequestRidesForDriver(final int driverId) throws DataUnreachableException {
        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDE_GET));
            ArrayList<Integer> ids = new ArrayList<>(RideMatchProposedDriverCache.Instance().getRidesProposedForDriver(driverId));

            Collections.sort(ids);

            for (int i : ids) {
                statement.setInt(1, i);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    rides.add(new Bind().ride(resultSet));
                }
            }

            closeConnection(connection, statement);

            return rides;
        } catch (SQLException e) {
            LOGGER.error("DriverDAO getAllClientsCareInstitution: ", e);
            throw new DataUnreachableException();
        }
    }

    public List<ClientDTO> getAllClientsForDriver(final int driverId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;
        List<ClientDTO> clients = new ArrayList<>();

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_GET_ALL));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ClientDTO client = new ClientDTO();
                client.setId(resultSet.getInt(ID));
                client.setFirstName(resultSet.getString(FIRST_NAME));
                client.setLastName(resultSet.getString(LAST_NAME));
                client.setPhoneNumber(resultSet.getString("phoneNumber"));
                clients.add(client);
            }

        } catch (SQLException e) {
            LOGGER.error("DriverDAO getAllClientsCareInstitution: ", e);
            throw new DataUnreachableException();
        }
        return clients;
    }

    public ArrayList<RideDTO> getAllCompletedRides(final int driverId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        ArrayList<RideDTO> rides = new ArrayList<RideDTO>();

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_RIDES_GET_ALL_COMPLETED));
            statement.setInt(1, driverId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rides.add(new Bind().ride(resultSet));
            }

            closeConnection(connection, statement);

            return rides;
        } catch (SQLException e) {
            LOGGER.error("DriverDAO getAllCompletedRides: ", e);
            throw new DataUnreachableException();
        }
    }

    public ArrayList<RideDTO> getAllCancelledRides(final int driverId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        ArrayList<RideDTO> rides = new ArrayList<>();

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_RIDES_GET_ALL_CANCELLED));
            statement.setInt(1, driverId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rides.add(new Bind().ride(resultSet));
            }

            closeConnection(connection, statement);

            return rides;
        } catch (SQLException e) {
            LOGGER.error("DriverDAO getAllCancelledRides: ", e);
            throw new DataUnreachableException();
        }
    }

    @Override
    public List<DriverDTO> getAllDrivers() {
        List<DriverDTO> drivers = new ArrayList<>();
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery(QueryType.GET_ALL_DRIVERS))
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                drivers.add(setDriverDTO(resultSet));
            }
            closeConnection(connection, preparedStatement);

        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return drivers;
    }

    @Override
    public DriverDTO getDriver(int driverId) {
        DriverDTO driverDTO = new DriverDTO();
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery(QueryType.GET_DRIVER))
        ) {
            preparedStatement.setInt(1, driverId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                driverDTO = setDriverDTO(resultSet);
            }
            closeConnection(connection, preparedStatement);
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return driverDTO;
    }

    @Override
    public List<DriverDTO> getAllDriversTownship(String township) {
        List<DriverDTO> driversDTO = new ArrayList<>();
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery(QueryType.GET_ALL_DRIVERS_TOWNSHIP))
        ) {
            preparedStatement.setString(1, township);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                driversDTO.add(setDriverDTO(resultSet));
            }
            closeConnection(connection, preparedStatement);

        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return driversDTO;
    }

    @Override
    public List<DriverDTO> getAllDriversCareInstitution(int careInstitution) {
        List<DriverDTO> driversDTO = new ArrayList<>();
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery(QueryType.GET_ALL_DRIVERS_CAREINSTITUTION))
        ) {
            preparedStatement.setInt(1, careInstitution);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                driversDTO.add(setDriverDTO(resultSet));
            }
            closeConnection(connection, preparedStatement);

        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return driversDTO;
    }

    @Override
    public void createDriver(DriverDTO driverDTO) {
        try (
                Connection connection = openConnection();
                PreparedStatement setDriverPS = connection.prepareStatement(queries.getQuery(QueryType.SET_DRIVER));
                PreparedStatement setCareInstitutionPS = connection.prepareStatement(queries.getQuery(QueryType.ADD_DRIVER_TO_CAREINSITUTION));
                PreparedStatement setCarInfoPS = connection.prepareStatement(queries.getQuery(QueryType.SET_DRIVER_CARE_INFO))
        ) {
            int driverId = getDriverId(driverDTO);
            setDriverPS.setInt(1, driverId);
            setDriverPS.setInt(2, 0);
            setDriverPS.executeUpdate();

            setCareInstitutionPS.setInt(1, driverId);
            setCareInstitutionPS.setInt(2, driverDTO.getCareInstitution().getId());

            setCareInstitutionPS.executeUpdate();

            setCarInfoPS.setInt(1, driverId);
            setCarInfoPS.setString(2, driverDTO.getUtility().getName());
            setCarInfoPS.setString(3, driverDTO.getNumberPlate());
            setCarInfoPS.setInt(4, driverDTO.getNumberOfPassengers());
            setCarInfoPS.executeUpdate();

            closeConnection(connection, setDriverPS);
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    public DriverDTO getProfile(final int driverId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();
            DriverDTO driver = new DriverDTO();
            statement = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_PROFILE_GET));
            statement.setInt(1, driverId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                driver = new Bind().driver(resultSet);
                driver.setVerification(resultSet.getBoolean("verification"));
                driver.setUtility(createUtility(resultSet.getString(UTILITY)));
                driver.setCareInstitution(creatCareInstitution(resultSet.getString("name"), resultSet.getInt(1)));
            }
            driver.setPreferredClients(getPreferredClients(connection, driverId));

            //Get driven rides.
            ArrayList<RideDTO> rides = this.getAllCompletedRides(driverId);
            String thisYear = new SimpleDateFormat("yyyy").format(new Date());
            String thisMonth = new SimpleDateFormat("MM").format(new Date());

            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
            SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");

            float drivenThisYear = 0.0f, drivenThisMonth = 0.0f, totalEarnedYear = 0.0f, totalEarnedMonth = 0.0f;

            //People helped this month.
            int peopleHelped = 0;
            ArrayList<Integer> helpedPeoplelist = new ArrayList<Integer>();


            //Set driven distance this month.
            for (int i = 0; i < rides.size(); i++) {
                RideDTO currentRide = rides.get(i);
                Date rideDate = null;
                try {
                    rideDate = dateParser.parse(currentRide.getPickUpDateTime());
                } catch (ParseException e) {
                    LOGGER.error("DriverDAO parseDate ", e);
                }

                if (rideDate == null) {
                    continue;
                }

                //Check if it's the same year.
                if (yearFormatter.format(rideDate).equals(thisYear)) {
                    float distance = currentRide.getDistance() / 1000.0f;
                    drivenThisYear += distance;
                    totalEarnedYear += distance * 0.25f;

                    //Check if it's also the same month.
                    if (monthFormatter.format(rideDate).equals(thisMonth)) {
                        drivenThisMonth += distance;
                        totalEarnedMonth += distance * 0.25f;

                        if (!helpedPeoplelist.contains(currentRide.getClientId())) {
                            helpedPeoplelist.add(currentRide.getClientId());
                            peopleHelped++;
                        }
                    }
                }
            }
            //Apply values to driver object
            driver.setKmDrivenThisYear(drivenThisYear);
            driver.setKmDrivenThisMonth(drivenThisMonth);
            driver.setTotalEarnedThisMonth(totalEarnedMonth);
            driver.setTotalEarnedThisYear(totalEarnedYear);
            driver.setPeopleHelpedThisMonth(peopleHelped);

            closeConnection(connection, statement);
            return driver;

        } catch (SQLException e) {
            LOGGER.error("DriverDAO getProfile: ", e);
            throw new DataUnreachableException();
        }
    }

    public void updateProfile(final int driverId, final DriverDTO driver) throws DataUnreachableException {
        Connection connection;

        try {
            connection = openConnection();

            updateUserTable(connection, driver);
            updateDriverTable(connection, driver);
            deleteDriverClientPreference(connection, driverId);
            updateDriverClientPreference(connection, driverId, driver.getPreferredClients());

            connection.close();
        } catch (SQLException e) {
            LOGGER.error("DriverDAO updateProfile:", e);
            throw new DataUnreachableException();
        }
    }

    public ArrayList<DriverDTO> getAllDriversForClient(final int clientId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        ArrayList<DriverDTO> drivers = new ArrayList<>();

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.DRIVERS_GET_ALL));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                DriverDTO driver = new Bind().driver(resultSet);
                drivers.add(driver);
            }

            closeConnection(connection, statement);

            return drivers;
        } catch (SQLException e) {
            LOGGER.error("DriverDAO getAllDrivers: ", e);
            throw new DataUnreachableException();
        }
    }

    private void updateUserTable(Connection connection, final DriverDTO driver) throws SQLException {
        PreparedStatement statementUpdateUser;

        statementUpdateUser = connection.prepareStatement(queries.getQuery(QueryType.USER_UPDATE_PROFILE));
        statementUpdateUser.setString(1, driver.getFirstName());
        statementUpdateUser.setString(2, driver.getLastName());
        statementUpdateUser.setString(3, driver.getEmail());
        statementUpdateUser.setString(4, driver.getPhoneNumber());
        statementUpdateUser.setString(5, driver.getStreet());
        statementUpdateUser.setString(6, driver.getHouseNumber());
        statementUpdateUser.setString(7, driver.getZipCode());
        statementUpdateUser.setString(8, driver.getResidence());
        statementUpdateUser.setDate(9, java.sql.Date.valueOf(driver.getDateOfBirth()));
        statementUpdateUser.setBoolean(10, driver.isFirstTimeProfileCheck());
        statementUpdateUser.setInt(11, driver.getId());

        statementUpdateUser.executeUpdate();
        statementUpdateUser.close();
    }

    private void updateDriverTable(Connection connection, final DriverDTO driver) throws SQLException {
        PreparedStatement statementUpdateAll;

        statementUpdateAll = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_UPDATE_ALL));
        statementUpdateAll.setBoolean(1, driver.isVerified());
        statementUpdateAll.setString(2, driver.getUtility().getName());
        statementUpdateAll.setInt(3, driver.getId());
        statementUpdateAll.executeUpdate();
        statementUpdateAll.close();
    }

    private void deleteDriverClientPreference(Connection connection, final int driverId) throws SQLException {
        PreparedStatement statementDeleteDriverClientPreference;

        statementDeleteDriverClientPreference = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_PREFERRED_CLIENTS_DELETE));
        statementDeleteDriverClientPreference.setInt(1, driverId);
        statementDeleteDriverClientPreference.executeUpdate();
        statementDeleteDriverClientPreference.close();
    }

    private void updateDriverClientPreference(Connection connection, final int driverId, final List<ClientDTO> clients) throws SQLException {
        PreparedStatement statementUpdateDriverClientPreference;

        statementUpdateDriverClientPreference = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_PREFERRED_CLIENTS_UPDATE));
        for (ClientDTO client : clients) {
            statementUpdateDriverClientPreference.setInt(1, driverId);
            statementUpdateDriverClientPreference.setInt(2, client.getId());
            statementUpdateDriverClientPreference.executeUpdate();
        }
        statementUpdateDriverClientPreference.close();
    }

    public void acceptRide(final int driverId, final int rideId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();

            //Update ride object.
            statement = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_RIDE_ACCEPT));
            statement.setInt(1, driverId);
            statement.setInt(2, rideId);
            statement.executeUpdate();

            //Remove proposed matches from cache
            RideMatchCache.Instance().stopMatching(rideId);

            closeConnection(connection, statement);

        } catch (SQLException e) {
            LOGGER.error("DriverDAO acceptRide: ", e);
            throw new DataUnreachableException();
        }
    }

    private List<ClientDTO> getPreferredClients(Connection connection, final int driverId) throws SQLException {
        PreparedStatement statementGetPreferredClients;
        List<ClientDTO> clients = new ArrayList<>();

        statementGetPreferredClients = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_GET_PREFERRED_CLIENTS));
        statementGetPreferredClients.setInt(1, driverId);
        ResultSet resultset = statementGetPreferredClients.executeQuery();

        while (resultset.next()) {
            ClientDTO client = new ClientDTO();
            client.setId(resultset.getInt(ID));
            client.setFirstName(resultset.getString(FIRST_NAME));
            client.setLastName(resultset.getString(LAST_NAME));
            clients.add(client);
        }
        return clients;
    }

    private UtilityDTO createUtility(final String utilityName) {
        UtilityDTO utility = new UtilityDTO();
        utility.setName(utilityName);

        return utility;
    }

    private CareInstitutionDTO creatCareInstitution(final String careInstitutionName, final int id) {
        CareInstitutionDTO careInstitution = new CareInstitutionDTO();
        careInstitution.setName(careInstitutionName);
        careInstitution.setId(id);

        return careInstitution;
    }

    private DriverDTO setDriverDTO(ResultSet resultSet) throws SQLException {
        DriverDTO driverDTO = new DriverDTO();

        driverDTO.setId(resultSet.getInt(ID));
        driverDTO.setFirstName(resultSet.getString(FIRST_NAME));
        driverDTO.setLastName(resultSet.getString(LAST_NAME));
        driverDTO.setEmail(resultSet.getString("email"));
        driverDTO.setPhoneNumber(resultSet.getString("phoneNumber"));
        driverDTO.setStreet(resultSet.getString("street"));
        driverDTO.setHouseNumber(resultSet.getString("houseNumber"));
        driverDTO.setZipCode(resultSet.getString("zipCode"));
        driverDTO.setResidence(resultSet.getString("residence"));
        driverDTO.setDateOfBirth(resultSet.getString("dateOfBirth"));

        driverDTO.setNumberPlate(resultSet.getString("numberPlate"));
        driverDTO.setNumberOfPassengers(resultSet.getInt("numberOfPassengers"));

        driverDTO.setFirstTimeProfileCheck(resultSet.getBoolean("firstTimeProfileCheck"));
        driverDTO.setVerification(resultSet.getBoolean("verification"));

        if (resultSet.getString(UTILITY) != null) {
            UtilityDTO utilityDTO = new UtilityDTO();
            utilityDTO.setName(resultSet.getString(UTILITY));
            driverDTO.setUtility(utilityDTO);
        }
        if (resultSet.getString("careInstitutionId") != null) {
            CareInstitutionDTO careInstitutionDTO = new CareInstitutionDTO();
            careInstitutionDTO.setId(resultSet.getInt("careInstitutionId"));
            careInstitutionDTO.setName(resultSet.getString("name"));
            driverDTO.setCareInstitution(careInstitutionDTO);
        }
        return driverDTO;
    }

    public int getDriverId(DriverDTO driverDTO) {
        int id = -1;

        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery(QueryType.GET_USER_ID))
        ) {
            preparedStatement.setString(1, driverDTO.getFirstName());
            preparedStatement.setString(2, driverDTO.getLastName());
            preparedStatement.setString(3, driverDTO.getEmail());
            preparedStatement.setString(4, driverDTO.getPhoneNumber());
            preparedStatement.setString(5, driverDTO.getStreet());
            preparedStatement.setString(6, driverDTO.getHouseNumber());
            preparedStatement.setString(7, driverDTO.getZipCode());
            preparedStatement.setString(8, driverDTO.getResidence());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt(ID);
            }

            closeConnection(connection, preparedStatement);

        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return id;
    }

    public void editDriver(int driverId, DriverDTO driver) {
        Connection connection;

        try {
            connection = openConnection();

            //deze is niet relevant voor de beheeromgeving dus wordt op false gezet
            driver.setFirstTimeProfileCheck(false);
            driver.setId(driverId);
            updateUserTable(connection, driver);
            updateDriverTable(connection, driver);
            deleteDriverClientPreference(connection, driverId);
            updateDriverClientPreference(connection, driverId, driver.getPreferredClients());

            connection.close();
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public void deleteDriver(int driverId) {
        try (
                Connection connection = openConnection();
                PreparedStatement deleteDriverLimitationManageable = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_LIMITATIONMANAGABLE_DELETE));
                PreparedStatement deleteDriverAvailability = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_AVAILABILITY_DELETE));
                PreparedStatement deleteDriverCar = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_CAR_DELETE));
                PreparedStatement deleteDriverCareinstitution = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_CAREINSTITUTION_DELETE));
                PreparedStatement deleteClientDriverPreferenceDriver = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_CLIENT_PREFERENCE_DRIVER_DELETE));
                PreparedStatement deleteDriverClientPreference = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_CLIENTPREFERENCE_DELETE));
                PreparedStatement deleteDriver = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_DELETE));
                PreparedStatement deleteUserDriver = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_USER_DELETE))
        ) {
            deleteDriverLimitationManageable.setInt(1, driverId);
            deleteDriverAvailability.setInt(1, driverId);
            deleteDriverCar.setInt(1, driverId);
            deleteDriverCareinstitution.setInt(1, driverId);
            deleteClientDriverPreferenceDriver.setInt(1, driverId);
            deleteDriverClientPreference.setInt(1, driverId);
            deleteDriver.setInt(1, driverId);
            deleteUserDriver.setInt(1, driverId);

            deleteDriverLimitationManageable.executeUpdate();
            deleteDriverClientPreference.executeUpdate();
            deleteClientDriverPreferenceDriver.executeUpdate();
            deleteDriverCareinstitution.executeUpdate();
            deleteDriverCar.executeUpdate();
            deleteDriverAvailability.executeUpdate();
            deleteDriver.executeUpdate();
            deleteUserDriver.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public List<RideDTO> checkIfDriverHasRides(int driverId) {
        ArrayList<RideDTO> rides = new ArrayList<>();

        try (
                Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(queries.getQuery(QueryType.DRIVER_CHECK_RIDES))

        ) {
            statement.setInt(1, driverId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                RideDTO ride = new Bind().ride(resultSet);
                rides.add(ride);
            }


            closeConnection(connection, statement);
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return rides;
    }

    @Override
    public List<DriverDTO> getPreferredDriversToMatch(int clientId) {
        ArrayList<DriverDTO> drivers = new ArrayList<>();
        try (
                Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_GET_PREFERED_DRIVERS))
        ) {
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                DriverDTO driverDTO = new DriverDTO();
                driverDTO.setId(resultSet.getInt(ID));
                driverDTO.setFirstName(resultSet.getString(FIRST_NAME));
                driverDTO.setLastName(resultSet.getString(LAST_NAME));
                drivers.add(driverDTO);

            }
            closeConnection(connection, statement);
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return drivers;
    }
}
