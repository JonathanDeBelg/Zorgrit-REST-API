package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.UserDTO;
import nl.ica.oose.a2.zorgrit.dto.UserPreference;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableRuntimeException;
import nl.ica.oose.a2.zorgrit.persistance.util.Bind;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import nl.ica.oose.a2.zorgrit.persistance.util.QueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static nl.ica.oose.a2.zorgrit.persistance.DriverDAO.DATABASE_ERROR_MESSAGE;

public class UserDAO extends DatabaseConnection implements IUserDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);
    @Inject
    private IClientDAO clientDAO;
    @Inject
    private IDriverDAO driverDAO;

    private boolean isClient(final int userId, Connection connection) throws SQLException {
        PreparedStatement statement;

        statement = connection.prepareStatement(queries.getQuery(QueryType.USER_CLIENT_CHECK));
        statement.setInt(1, userId);
        ResultSet resultsSet = statement.executeQuery();

        return resultsSet.next();
    }

    private boolean isDriver(final int userId, Connection connection) throws SQLException {
        PreparedStatement statement;

        statement = connection.prepareStatement(queries.getQuery(QueryType.USER_DRIVER_CHECK));
        statement.setInt(1, userId);
        ResultSet resultsSet = statement.executeQuery();

        return resultsSet.next();
    }

    public UserDTO getProfile(final int userId) throws DataUnreachableException {
        Connection connection;

        try {
            connection = openConnection();
            if (isClient(userId, connection)) {
                UserDTO userDTO = clientDAO.getProfile(userId);
                connection.close();
                return userDTO;
            } else if (isDriver(userId, connection)) {
                UserDTO userDTO = driverDAO.getProfile(userId);
                connection.close();
                return userDTO;
            }
            return null;
        } catch (SQLException e) {
            LOGGER.error("UserDAO getProfile: ", e);
            throw new DataUnreachableException();
        }
    }

    public int getUserByUsernameAndPassword(final String username, final String passwordPlain) throws DataUnreachableException {
        try (
                Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(queries.getQuery(QueryType.USER_GET_PASSWORD_AND_USERNAME))
        ) {
            statement.setString(1, username.toLowerCase());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String password = resultSet.getString(2);
                String passwordSalt = resultSet.getString(3);

                byte[] salt = Base64.getDecoder().decode(passwordSalt);
                byte[] hash = Base64.getDecoder().decode(password);

                KeySpec spec = new PBEKeySpec(passwordPlain.toCharArray(), salt, 65536, 128);
                SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                byte[] hashedInput = f.generateSecret(spec).getEncoded();

                if (Arrays.equals(hashedInput, hash)) {
                    return resultSet.getInt(1);
                }
            }
        } catch (Exception e) {
            LOGGER.error("UserDAO getUserByUsernameAndPassword: ", e);
            throw new DataUnreachableException();
        }

        return Integer.MIN_VALUE;
    }

    public ArrayList<UserPreference> getUserPreferencesByUserId(final int userId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;
        ArrayList<UserPreference> userPreferences = new ArrayList<>();

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.USER_GET_PREFERENCES));
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userPreferences.add(new Bind().userPreference(resultSet));
            }
            return userPreferences;
        } catch (Exception e) {
            LOGGER.error("UserDAO getUserPreferencesByUserId: ", e);
            throw new DataUnreachableException();
        }
    }

    public void setUserPreferenceByUserIdAndKey(final int userId, final String key, final String value) throws DataUnreachableException {
        try (
                Connection connection = openConnection();
                PreparedStatement getUserPreferencesStatement = connection.prepareStatement(queries.getQuery(QueryType.USER_GET_PREFERENCES_BYIDANDKEY));
                PreparedStatement updateUserPreferencesStatement = connection.prepareStatement(queries.getQuery(QueryType.USER_UPDATE_PREFERENCES));
                PreparedStatement addUserPreferencesStatement = connection.prepareStatement(queries.getQuery(QueryType.USER_ADD_PREFERENCES))
        ) {
            getUserPreferencesStatement.setInt(1, userId);
            getUserPreferencesStatement.setString(2, key);
            ResultSet resultSet = getUserPreferencesStatement.executeQuery();
            if (resultSet.next()) {
                updateUserPreferencesStatement.setString(1, value);
                updateUserPreferencesStatement.setInt(2, userId);
                updateUserPreferencesStatement.setString(3, key);
                updateUserPreferencesStatement.execute();
            } else {
                addUserPreferencesStatement.setInt(1, userId);
                addUserPreferencesStatement.setString(2, key);
                addUserPreferencesStatement.setString(3, value);
                addUserPreferencesStatement.execute();
            }
        } catch (Exception e) {
            LOGGER.error("UserDAO setUserPreferencesByUserId");
            throw new DataUnreachableException();
        }
    }

    public void setUserPreferencesByUserId(final int userId, final List<UserPreference> list) throws DataUnreachableException {
        try (
                Connection connection = openConnection();
                PreparedStatement userGetPreferencesStatement = connection.prepareStatement(queries.getQuery(QueryType.USER_GET_PREFERENCES_BYIDANDKEY));
                PreparedStatement updateUserPreferencesStatement = connection.prepareStatement(queries.getQuery(QueryType.USER_UPDATE_PREFERENCES));
                PreparedStatement addUserPreferencesStatement = connection.prepareStatement(queries.getQuery(QueryType.USER_ADD_PREFERENCES))
        ) {
            for (UserPreference u : list) {
                userGetPreferencesStatement.setInt(1, userId);
                userGetPreferencesStatement.setString(2, u.preferenceKey);
                ResultSet resultSet = userGetPreferencesStatement.executeQuery();
                if (resultSet.next()) {
                    updateUserPreferencesStatement.setString(1, u.getPreferenceValue());
                    updateUserPreferencesStatement.setInt(2, userId);
                    updateUserPreferencesStatement.setString(3, u.getPreferenceKey());
                    updateUserPreferencesStatement.execute();
                } else {
                    addUserPreferencesStatement.setInt(1, userId);
                    addUserPreferencesStatement.setString(2, u.getPreferenceKey());
                    addUserPreferencesStatement.setString(3, u.getPreferenceValue());
                    addUserPreferencesStatement.execute();
                }
            }
        } catch (Exception e) {
            LOGGER.error("UserDAO setUserPreferencesByUserId");
            throw new DataUnreachableException();
        }
    }

    @Override
    public void createUser(DriverDTO driverDTO) {
        Connection connection;
        PreparedStatement createUserStatement;

        try {
            connection = openConnection();

            createUserStatement = connection.prepareStatement(queries.getQuery(QueryType.CREATE_USER));
            createUserStatement.setString(1, driverDTO.getFirstName());
            createUserStatement.setString(2, driverDTO.getLastName());
            createUserStatement.setString(3, driverDTO.getEmail());
            createUserStatement.setString(4, driverDTO.getPhoneNumber());
            createUserStatement.setString(5, driverDTO.getStreet());
            createUserStatement.setString(6, driverDTO.getHouseNumber());
            createUserStatement.setString(7, driverDTO.getZipCode());
            createUserStatement.setString(8, driverDTO.getResidence());
            createUserStatement.setString(9, driverDTO.getDateOfBirth());
            createUserStatement.executeUpdate();

            closeConnection(connection, createUserStatement);
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);

            try {
                throw new DataUnreachableException();
            } catch (DataUnreachableException e1) {
                LOGGER.error("DataUnreachable error", e1);
            }
        }
    }

    @Override
    public void setUserInActiveSince(int driverId) {
        try (
                Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(queries.getQuery(QueryType.USER_SET_ISINACTIVESINCE))
        ) {
            statement.setInt(1, driverId);
            statement.executeUpdate();
            closeConnection(connection, statement);
        } catch (SQLException e) {
            LOGGER.error(DATABASE_ERROR_MESSAGE, e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
    }
}
