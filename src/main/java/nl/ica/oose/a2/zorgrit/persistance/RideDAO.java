package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableRuntimeException;
import nl.ica.oose.a2.zorgrit.persistance.util.Bind;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import nl.ica.oose.a2.zorgrit.persistance.util.QueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RideDAO extends DatabaseConnection implements IRideDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(RideDAO.class);
    private RideDTO foundRide;
    private String databaseError = "Database error: ";

    public RideDTO getRide(final int rideId) throws DataUnreachableException, IllegalArgumentException {
        Connection connection;
        PreparedStatement statement;
        RideDTO ride;

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDE_GET));
            statement.setInt(1, rideId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ride = new Bind().ride(resultSet);
            } else {
                throw new IllegalArgumentException("There is no ride with the given id.");
            }

            closeConnection(connection, statement);

            return ride;
        } catch (SQLException e) {
            LOGGER.error("RideDAO getRide: ", e);
            throw new DataUnreachableException();
        }
    }

    public void updateRide(final RideDTO ride) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDE_UPDATE));
            statement.setInt(1, ride.getClientId());
            if(ride.getPreferedCareInstitution() != 0){
                statement.setInt(2, ride.getPreferedCareInstitution());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            if(ride.getPreferedDriver() != 0){
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
            statement.setInt(12, ride.getRepeatingRideId());
            statement.setBoolean(13, ride.isCancelledByClient());
            statement.setBoolean(14, ride.isExecuted());
            statement.setInt(15, ride.getId());
            statement.executeUpdate();

            closeConnection(connection, statement);
        } catch (SQLException e) {
            LOGGER.error("RideDAO updateRide: ", e);
            throw new DataUnreachableException();
        }
    }

    public RideDTO getCurrentRide(int clientId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;
        RideDTO ride = null;

        try {
            connection = openConnection();

            //Get ride by id.
            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDE_GET_CURRENT));
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                ride = new Bind().ride(resultSet);
                if (resultSet.getInt("rideId") != 0) {
                    ride.setRating(new Bind().rideRating(resultSet));
                }
            } else {
                //No ride found
                return null;
            }

            closeConnection(connection, statement);
            return ride;
        } catch (SQLException e) {
            LOGGER.error("RideDAO getCurrentRide: ", e);
            throw new DataUnreachableException();
        }
    }

    @Override
    public void startRide(int rideId) throws DataUnreachableException{
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDE_POST_START));
            statement.setInt(1, rideId);

            statement.execute();
            closeConnection(connection, statement);

        } catch (SQLException e) {
            LOGGER.error("RideDAO getCurrentRide: ", e);
            throw new DataUnreachableException();
        }
    }

    public RideRating getRideRating(int rideId) throws DataUnreachableException{
        Connection connection;
        PreparedStatement statement;
        RideRating rideRating;

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDE_GET_RATING));

            statement.setInt(1, rideId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                rideRating = new Bind().rideRating(resultSet);
            } else {
                throw new IllegalArgumentException("There is no rating for this ride.");
            }

            closeConnection(connection, statement);
            return rideRating;

        } catch (SQLException e) {
            LOGGER.error("RideDAO rateRide: ", e);
            throw new DataUnreachableException();
        }
    }

    public void addRideRating(int rideId, RideRating rating) throws DataUnreachableException{
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery((QueryType.RIDE_ADD_RATING)));
            statement.setInt(1, rating.getRideId());
            statement.setInt(2, rating.getRating());
            statement.setString(3, rating.getMessage());
            statement.setString(4, getNow());

            statement.execute();
            closeConnection(connection, statement);

        } catch (SQLException e) {
            LOGGER.error("RideDAO addRideRating: ", e);
            throw new DataUnreachableException();
        }
    }

    public ArrayList<RideRating> getAllRatingsByDriver(int driverId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;
        ArrayList<RideRating> ratings = new ArrayList<>();

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDE_GET_ALL_RATINGS_BY_DRIVER));

            statement.setInt(1,driverId);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                ratings.add(new Bind().rideRating(result));
            }

            closeConnection(connection, statement);
            return ratings;

        } catch (SQLException e) {
            LOGGER.error("RideDAO getAllRatingsByDriver: ", e);
            throw new DataUnreachableException();
        }
    }

    public void putRideRating (int rideId, RideRating rating) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDE_PUT_RATING));

            statement.setInt(1, rating.getRating());
            statement.setString(2, rating.getMessage());
            statement.setString(3, getNow());
            statement.setInt(4, rideId);

            statement.execute();
            closeConnection(connection, statement);

        } catch (SQLException e) {
            LOGGER.error("RideDAO putRideRating: ", e);
            throw new DataUnreachableException();
        }
    }

    public void deleteRideRating (int rideId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDE_DELETE_RATING));

            statement.setInt(1, rideId);

            statement.execute();
            closeConnection(connection, statement);

        } catch (SQLException e) {
            LOGGER.error("RideDAO putRideRating: ", e);
            throw new DataUnreachableException();
        }
    }

    public RidesDTO getAllRides() {
        Connection connection;
        PreparedStatement statement;
        RidesDTO foundRides = new RidesDTO();
        ArrayList<RideDTO> rides = new ArrayList<>();

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDES_GET_ALL));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RideDTO ride = new Bind().ride(resultSet);
                rides.add(ride);
            }

            closeConnection(connection, statement);

            foundRides.setRides(rides);
            return foundRides;
        } catch (SQLException e) {
            LOGGER.error(databaseError, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public RidesDTO getAllRidesTownship(String township) {
        Connection connection;
        PreparedStatement statement;
        RidesDTO foundRides = new RidesDTO();
        ArrayList<RideDTO> rides = new ArrayList<>();

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDES_GET_ALL_TOWNSHIP));
            statement.setString(1, township);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                foundRide = new Bind().ride(resultSet);
                rides.add(foundRide);
            }

            closeConnection(connection, statement);

            foundRides.setRides(rides);
            return foundRides;
        } catch (SQLException e) {
            LOGGER.error(databaseError, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public RidesDTO getAllRidesCareInstitution(int careInstitutionId) {
        Connection connection;
        PreparedStatement statement;
        RidesDTO foundRides = new RidesDTO();
        ArrayList<RideDTO> rides = new ArrayList<>();

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.RIDES_GET_ALL_CAREINSITUTION));
            statement.setInt(1, careInstitutionId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                foundRide = new Bind().ride(resultSet);
                rides.add(foundRide);
            }

            closeConnection(connection, statement);

            foundRides.setRides(rides);
            return foundRides;
        } catch (SQLException e) {
            LOGGER.error(databaseError, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public boolean checkIfClientHasUnPayedRides(int userId) {
        foundRide = null;
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(queries.getQuery(QueryType.CLIENT_CHECK_OPEN_RIDES))
        ) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
            closeConnection(connection, statement);
            return true;
        } catch (SQLException e) {
            LOGGER.error(databaseError, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public RidesInformationDTO getAllNotMatchedRides(int careInstitutionId) {
        RidesInformationDTO ridesInformationDTO = new RidesInformationDTO();
        ridesInformationDTO.setRidesInformation(new ArrayList<>());
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery(QueryType.GET_ALL_NOT_MATCHED_RIDES))
        ) {
            preparedStatement.setInt(1, careInstitutionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RideInformationDTO rideInformationDTO = setRideInfo(resultSet);
                rideInformationDTO.setClientDTO(setClient(resultSet));
                ridesInformationDTO.addRide(rideInformationDTO);
            }
            closeConnection(connection, preparedStatement);

        } catch (SQLException e) {
            LOGGER.error(databaseError, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return ridesInformationDTO;
    }

    @Override
    public RidesInformationDTO getAllMatchedRides(int careInstitutionId) {
        RidesInformationDTO ridesInformationDTO = new RidesInformationDTO();
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery(QueryType.GET_ALL_MATCHED_RIDES))
        ) {
            preparedStatement.setInt(1, careInstitutionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RideInformationDTO rideInformationDTO = setRideInfo(resultSet);
                rideInformationDTO.setDriverDTO(setDriver(resultSet));
                rideInformationDTO.setClientDTO(setClient(resultSet));
                ridesInformationDTO.addRide(rideInformationDTO);
            }
            closeConnection(connection, preparedStatement);

        } catch (SQLException e) {
            LOGGER.error(databaseError, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return ridesInformationDTO;
    }

    @Override
    public RidesInformationDTO getAllRidesInfo(int careInstitutionId) {
        RidesInformationDTO ridesInformationDTO = new RidesInformationDTO();
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery(QueryType.GET_ALL_RIDES_WITH_INFO))
        ) {
            preparedStatement.setInt(1, careInstitutionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RideInformationDTO rideInformationDTO = setRideInfo(resultSet);
                rideInformationDTO.setDriverDTO(setDriver(resultSet));
                rideInformationDTO.setClientDTO(setClient(resultSet));
                ridesInformationDTO.addRide(rideInformationDTO);
            }
            closeConnection(connection, preparedStatement);

        } catch (SQLException e) {
            LOGGER.error(databaseError, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return ridesInformationDTO;
    }

    private RideInformationDTO setRideInfo(ResultSet resultSet) throws SQLException {
        RideInformationDTO rideInformationDTO = new RideInformationDTO();
        rideInformationDTO.setId(resultSet.getInt("id"));
        rideInformationDTO.setClientId(resultSet.getInt("clientId"));
        rideInformationDTO.setDriverId(resultSet.getInt("driverId"));
        rideInformationDTO.setPreferedDriver(resultSet.getInt("preferedDriver"));
        rideInformationDTO.setPreferedCareInstitution(resultSet.getInt("preferedCareInstitution"));
        rideInformationDTO.setPickUpDateTime(resultSet.getString("pickUpDateTime"));
        rideInformationDTO.setPickUpLocation(resultSet.getString("pickUpLocation"));
        rideInformationDTO.setDropOffLocation(resultSet.getString("dropOffLocation"));
        rideInformationDTO.setNumberOfCompanions(resultSet.getInt("numberOfCompanions"));
        rideInformationDTO.setNumberOfLuggage(resultSet.getInt("numberOfLuggage"));
        rideInformationDTO.setReturnRide(resultSet.getBoolean("returnRide"));
        rideInformationDTO.setCallService(resultSet.getBoolean("callService"));
        rideInformationDTO.setUtility(resultSet.getString("utility"));
        rideInformationDTO.setRepeatingRideId(resultSet.getInt("repeatingRideId"));
        rideInformationDTO.setCancelledByClient(resultSet.getBoolean("cancelledByClient"));

        return rideInformationDTO;
    }

    private DriverDTO setDriver(ResultSet resultSet) throws SQLException {
        DriverDTO driverDTO = new DriverDTO();

        driverDTO.setId(resultSet.getInt("d.id"));
        driverDTO.setFirstName(resultSet.getString("d.firstName"));
        driverDTO.setLastName(resultSet.getString("d.lastName"));
        driverDTO.setEmail(resultSet.getString("d.email"));
        driverDTO.setPhoneNumber(resultSet.getString("d.phoneNumber"));
        driverDTO.setStreet(resultSet.getString("d.street"));
        driverDTO.setHouseNumber(resultSet.getString("d.houseNumber"));
        driverDTO.setZipCode(resultSet.getString("d.zipCode"));
        driverDTO.setResidence(resultSet.getString("d.residence"));
        driverDTO.setDateOfBirth(resultSet.getString("d.dateOfBirth"));

        return driverDTO;
    }

    private ClientDTO setClient(ResultSet resultSet) throws SQLException {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(resultSet.getInt("c.id"));
        clientDTO.setFirstName(resultSet.getString("c.firstName"));
        clientDTO.setLastName(resultSet.getString("c.lastName"));
        clientDTO.setEmail(resultSet.getString("c.email"));
        clientDTO.setPhoneNumber(resultSet.getString("c.phoneNumber"));
        clientDTO.setStreet(resultSet.getString("c.street"));
        clientDTO.setHouseNumber(resultSet.getString("c.houseNumber"));
        clientDTO.setZipCode(resultSet.getString("c.zipCode"));
        clientDTO.setResidence(resultSet.getString("c.residence"));
        clientDTO.setDateOfBirth(resultSet.getString("c.dateOfBirth"));
        clientDTO.setId(resultSet.getInt("clientId"));

        return clientDTO;
    }

    private String getNow() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }


    @Override
    public RideInformationDTO getRideToMatch(int rideId){
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery(QueryType.RIDE_GET_RIDE_WITH_CLIENT))
        ) {
            preparedStatement.setInt(1,rideId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                RideInformationDTO rideInformationDTO = setRideInfo(resultSet);
                rideInformationDTO.setClientDTO(setClient(resultSet));
              return rideInformationDTO;
            } else{
                return null;
            }


        } catch (SQLException e) {
            LOGGER.error(databaseError, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }

    @Override
    public void matchRideToDriver(int rideId, int driverId){
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery(QueryType.RIDE_UPDATE_DRIVERID))
        ) {
            preparedStatement.setInt(1,driverId);
            preparedStatement.setInt(2,rideId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(databaseError, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }


    }

}
