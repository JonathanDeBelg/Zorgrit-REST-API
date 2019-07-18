package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.UserDTO;
import nl.ica.oose.a2.zorgrit.dto.UserPreference;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import nl.ica.oose.a2.zorgrit.persistance.util.Formatter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserDAOTest extends DatabaseConnection {

    @Mock
    IClientDAO clientDAO;

    @Mock
    IDriverDAO driverDAO;

    @InjectMocks
    UserDAO sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetProfileClient() throws DataUnreachableException {
        //Setup
        int userId = 2;

        //Test
        sut.getProfile(userId);

        //Verify
        verify(clientDAO).getProfile(userId);
    }

    @Test
    public void testUserGetProfile() throws DataUnreachableException {
        //Setup
        UserDTO userDTO = mock(UserDTO.class);

        int driverId = 1;

        //Test
        when(driverDAO.getProfile(driverId)).thenReturn(userDTO);

        //Verify
        assertEquals(sut.getProfile(driverId), userDTO);
    }

    @Test
    public void getUserByUsernameAndPasswordReturnsMinimalInteger() {

    }

    @Test
    public void getUserByUsernameAndPasswordReturnsUserId() throws DataUnreachableException {
        String username = "wiede@gmail.com";
        String password = "test";

        int actualUserId = sut.getUserByUsernameAndPassword(username, password);

        assertEquals(1, actualUserId);
    }

    @Test
    public void setUserPreferenceByUserIdAndKeyChangesUserData() throws DataUnreachableException, SQLException {
        Connection connection = openConnection();
        int userId = 1;
        String key = "testkey";
        String value = "testvalue";

        sut.setUserPreferenceByUserIdAndKey(userId, key, value);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM userpreference WHERE userId = ?");
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        String actualUserpreference = resultSet.getString("settingKey");

        assertEquals(key, actualUserpreference);
    }

    @Test
    public void setUserPreferenceByUserIdChangesUserData() throws DataUnreachableException, SQLException {
        Connection connection = openConnection();
        int userId = 1;
        String key = "testkey";
        String value = "testvalue";

        UserPreference preference1 = new UserPreference();
        preference1.setPreferenceKey(key);
        preference1.setPreferenceValue(value);
        List<UserPreference> userPreferences = new ArrayList<>();
        userPreferences.add(preference1);

        sut.setUserPreferencesByUserId(userId, userPreferences);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM userpreference WHERE userId = ?");
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        int actualNumberOfPreferences = 0;

        while(resultSet.next()) {
            actualNumberOfPreferences++;
        }

        assertEquals(userPreferences.size(), actualNumberOfPreferences);
    }

    @Test
    public void createUserCreatesUserInDatabase() throws SQLException {
        Connection connection = openConnection();

        DriverDTO user = new DriverDTO();
        user.setFirstName("Henk");
        user.setLastName("Henksen");
        user.setEmail("henk@henk.nl");
        user.setPhoneNumber("0612345678");
        user.setStreet("Dorpstraat");
        user.setHouseNumber("33a");
        user.setZipCode("1234AB");
        user.setDateOfBirth("1980-04-04");
        user.setResidence("Arnhem");

        sut.createUser(user);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE firstName = ?");
        preparedStatement.setString(1, "Henk");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        assertEquals("Henk", resultSet.getString("firstname"));
    }

    @Test
    public void setUserInActiveSinceSetsUserInactive() throws SQLException {
        Connection connection = openConnection();

        Date date = new Date();
        Formatter formatter = new Formatter();

        String actualDate = formatter.standardDateFormat(date);

        int userId = 1;

        sut.setUserInActiveSince(userId);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        assertEquals(actualDate, resultSet.getString("inActiveSince"));
    }

    @Test(expected = DataUnreachableException.class)
    public void getUserByUsernameAndPasswordReturnsRuntimeException() throws SQLException, DataUnreachableException {
        String username = "Testuser";
        String password = "Tsestpass";

        //Setup
        UserDAO userDAO = Mockito.spy(new UserDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)userDAO).openConnection();

        //Test
        userDAO.getUserByUsernameAndPassword(username, password);
    }

    @Test(expected = DataUnreachableException.class)
    public void getProfileReturnsRuntimeException() throws SQLException, DataUnreachableException {
        int userId = 1;

        //Setup
        UserDAO userDAO = Mockito.spy(new UserDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)userDAO).openConnection();

        //Test
        userDAO.getProfile(userId);
    }

    @Test(expected = DataUnreachableException.class)
    public void getUserPreferencesByUserIdReturnsRuntimeException() throws SQLException, DataUnreachableException {
        int userId = 1;

        //Setup
        UserDAO userDAO = Mockito.spy(new UserDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)userDAO).openConnection();

        //Test
        userDAO.getUserPreferencesByUserId(userId);
    }

    @Test(expected = DataUnreachableException.class)
    public void setUserPreferenceByUserIdAndKeyReturnsRuntimeException() throws SQLException, DataUnreachableException {
        int userId = 1;
        String key = "testkey";
        String value = "testvalue";

        //Setup
        UserDAO userDAO = Mockito.spy(new UserDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)userDAO).openConnection();

        //Test
        userDAO.setUserPreferenceByUserIdAndKey(userId, key, value);
    }

    @Test(expected = DataUnreachableException.class)
    public void setUserPreferencesByUserIdReturnsRuntimeException() throws SQLException, DataUnreachableException {
        int userId = 1;
        String key = "testkey";
        String value = "testvalue";
        UserPreference preference = new UserPreference();
        preference.setPreferenceValue(value);
        preference.setPreferenceKey(key);

        List<UserPreference> userPreferences = new ArrayList<>();

        userPreferences.add(preference);

        //Setup
        UserDAO userDAO = Mockito.spy(new UserDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)userDAO).openConnection();

        //Test
        userDAO.setUserPreferenceByUserIdAndKey(userId, key, value);
    }

    @Test(expected = RuntimeException.class)
    public void setUserInActiveSinceReturnsRuntimeException() throws SQLException {
        int userId = 1;

        //Setup
        UserDAO userDAO = Mockito.spy(new UserDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)userDAO).openConnection();

        //Test
        userDAO.setUserInActiveSince(userId);
    }
}
