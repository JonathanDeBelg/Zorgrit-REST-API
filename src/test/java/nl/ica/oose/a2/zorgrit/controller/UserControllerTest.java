package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableRuntimeException;
import nl.ica.oose.a2.zorgrit.service.IUserService;
import nl.ica.oose.a2.zorgrit.service.OAuthService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private OAuthService oAuthService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetProfile() throws DataUnreachableException {
        //Setup
        int userId = 2;

        // Test
        sut.getProfile(userId);

        // Verify
        verify(userService).getProfile(userId);
    }

    @Test
    public void testGetProfileResponseOkDriver() throws DataUnreachableException {
        //Setup
        int driverId = 4;
        UserDTO userDTO = mock(DriverDTO.class);
        when(userService.getProfile(driverId)).thenReturn(userDTO);

        // Test
        Response response = sut.getProfile(driverId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testGetRideResponseEntityDriver() throws DataUnreachableException {
        //Setup
        int driverId = 1;
        UserDTO userDTO = mock(DriverDTO.class);
        when(userService.getProfile(driverId)).thenReturn(userDTO);

        // Test
        Response response = sut.getProfile(driverId);

        // Verify
        assertEquals(response.getEntity(), userDTO);
    }

    @Test
    public void testGetProfileResponseOkClient() throws DataUnreachableException {
        //Setup
        int clientId = 2;
        UserDTO userDTO = mock(ClientDTO.class);
        when(userService.getProfile(clientId)).thenReturn(userDTO);

        // Test
        Response response = sut.getProfile(clientId);

        //Verify
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testGetRideResponseEntityClient() throws DataUnreachableException {
        //Setup
        int clientId = 2;
        UserDTO userDTO = mock(ClientDTO.class);
        when(userService.getProfile(clientId)).thenReturn(userDTO);

        // Test
        Response response = sut.getProfile(clientId);

        // Verify
        assertEquals(response.getEntity(), userDTO);
    }

    @Test
    public void testGetRideThrowDataUnreachableException_ResponseCodeInternalServerError() throws DataUnreachableException {
        int userId = -1262;
        UserDTO userDTO = mock(DriverDTO.class);
        when(userService.getProfile(userId)).thenThrow(DataUnreachableException.class);

        // Test
        Response response = sut.getProfile(userId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void loginActionCallsFunctionInService() throws DataUnreachableException {
        String username = "Testuser";
        String userpass = "Testpass";

        sut.loginAction(username, userpass);

        verify(oAuthService).requestNewToken(username, userpass);
    }

    @Test
    public void loginActionReturnsOkStatus() throws DataUnreachableException {
        String username = "Testuser";
        String userpass = "Testpass";

        Response actualResponse = sut.loginAction(username, userpass);

        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());
    }

    @Test
    public void loginActionThrowsDataUnreachableException_ResponseCodeInternalServerError() throws DataUnreachableException {
        String username = "Testuser";
        String userpass = "Testpass";

        UserDTO userDTO = mock(DriverDTO.class);
        when(oAuthService.requestNewToken(username, userpass)).thenThrow(DataUnreachableException.class);

        // Test
        Response response = sut.loginAction(username, userpass);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void getUserPreferencesCallsFunctionInService() throws DataUnreachableException {
        int userId = 1;

        sut.getUserPreferences(userId);

        verify(userService).getUserPreferenceByUserId(userId);
    }

    @Test
    public void getUserPreferencesReturnsOkStatus() throws DataUnreachableException {
        int userId = 1;

        Response actualResponse = sut.getUserPreferences(userId);

        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());
    }

    @Test
    public void getUserPreferencesThrowsDataUnreachableException_ResponseCodeInternalServerError() throws DataUnreachableException {
        int userId = 1;

        when(userService.getUserPreferenceByUserId(userId)).thenThrow(DataUnreachableException.class);

        // Test
        Response response = sut.getUserPreferences(userId);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void setUserPreferenceCallsFunctionInService() throws DataUnreachableException {
        int userId = 1;
        String key = "key";
        String value = "value";

        sut.setUserPreferences(userId, key, value);

        verify(userService).setUserPreferenceByUserIdAndKey(userId, key, value);
    }

    @Test
    public void setUserPreferenceReturnsOkStatus() throws DataUnreachableException {
        int userId = 1;
        String key = "key";
        String value = "value";

        Response actualResponse = sut.setUserPreferences(userId, key, value);

        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());
    }

    @Test
    public void setUserPreferenceThrowsDataUnreachableException_ResponseCodeInternalServerError() throws DataUnreachableException {
        int userId = 1;
        String key = "key";
        String value = "value";

        doThrow(DataUnreachableRuntimeException.class).when(userService).setUserPreferenceByUserIdAndKey(userId, key, value);

        // Test
        Response response = sut.setUserPreferences(userId, key, value);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Test
    public void setUserPreferencesCallsFunctionInService() throws DataUnreachableException {
        int userId = 1;
        List<UserPreference> userPreferenceList = new ArrayList<>();

        sut.setUserPreferences(userId, userPreferenceList);

        verify(userService).setUserPreferencesByUserId(userId, userPreferenceList);
    }

    @Test
    public void setUserPreferencesReturnsOkStatus() throws DataUnreachableException {
        int userId = 1;
        List<UserPreference> userPreferenceList = new ArrayList<>();

        Response actualResponse = sut.setUserPreferences(userId, userPreferenceList);

        assertEquals(Response.Status.OK.getStatusCode(), actualResponse.getStatus());
    }

    @Test
    public void setUserPreferencesThrowsDataUnreachableException_ResponseCodeInternalServerError() throws DataUnreachableException {
        int userId = 1;
        List<UserPreference> userPreferenceList = new ArrayList<>();

        doThrow(DataUnreachableRuntimeException.class).when(userService).setUserPreferencesByUserId(userId, userPreferenceList);

        // Test
        Response response = sut.setUserPreferences(userId, userPreferenceList);

        // Verify
        assertThat(response.getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }
}
