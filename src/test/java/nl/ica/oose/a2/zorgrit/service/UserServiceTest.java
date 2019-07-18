package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.UserPreference;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableRuntimeException;
import nl.ica.oose.a2.zorgrit.persistance.IUserDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    @Mock
    IUserDAO userDAO;

    @InjectMocks
    UserService sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetProfile() throws DataUnreachableException {
        //Setup
        int userId = 2;

        //Test
        sut.getProfile(userId);

        //Verify
        verify(userDAO).getProfile(userId);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetProfielDataUnreachableException() throws DataUnreachableException {
        //Setup
        int userId = -15;
        when(userDAO.getProfile(userId)).thenThrow(DataUnreachableException.class);

        //Test
        sut.getProfile(userId);
    }

    @Test
    public void getUserPreferenceByUserIdCallsFunctionInDao() throws DataUnreachableException {
        //Setup
        int userId = 2;

        //Test
        sut.getUserPreferenceByUserId(userId);

        //Verify
        verify(userDAO).getUserPreferencesByUserId(userId);
    }

    @Test(expected = DataUnreachableException.class)
    public void getUserPreferenceByUserIdThrowsDataUnreachableException() throws DataUnreachableException {
        //Setup
        int userId = -15;
        when(userDAO.getUserPreferencesByUserId(userId)).thenThrow(DataUnreachableException.class);

        //Test
        sut.getUserPreferenceByUserId(userId);
    }

    @Test
    public void setUserPreferenceByUserIdAndKeyCallsFunctionInDao() throws DataUnreachableException {
        //Setup
        int userId = -15;
        String key = "key";
        String value = "value";

        //Test
        sut.setUserPreferenceByUserIdAndKey(userId, key, value);

        //Verify
        verify(userDAO).setUserPreferenceByUserIdAndKey(userId, key, value);
    }

    @Test(expected = DataUnreachableRuntimeException.class)
    public void setUserPreferenceByUserIdAndKeyThrowsDataUnreachableException() throws DataUnreachableException {
        //Setup
        int userId = -15;
        String key = "key";
        String value = "value";

        doThrow(DataUnreachableRuntimeException.class).when(userDAO).setUserPreferenceByUserIdAndKey(userId, key, value);

        //Test
        sut.setUserPreferenceByUserIdAndKey(userId, key, value);
    }

    @Test
    public void setUserPreferencesByUserIdCallsFunctionInDao() throws DataUnreachableException {
        //Setup
        int userId = -15;
        List<UserPreference> userPreferenceList = new ArrayList<>();

        //Test
        sut.setUserPreferencesByUserId(userId, userPreferenceList);

        //Verify
        verify(userDAO).setUserPreferencesByUserId(userId, userPreferenceList);
    }

    @Test(expected = DataUnreachableRuntimeException.class)
    public void setUserPreferencesByUserIdThrowsDataUnreachableException() throws DataUnreachableException {
        //Setup
        int userId = -15;
        List<UserPreference> userPreferenceList = new ArrayList<>();

        doThrow(DataUnreachableRuntimeException.class).when(userDAO).setUserPreferencesByUserId(userId, userPreferenceList);

        //Test
        sut.setUserPreferencesByUserId(userId, userPreferenceList);
    }
}
