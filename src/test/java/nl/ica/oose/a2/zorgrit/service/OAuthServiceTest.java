package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.OAuthClient;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.IOAuthClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.IUserDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OAuthServiceTest {

        @Mock
        private IOAuthClientDAO authClientDAO;

        @Mock
        private IUserDAO userDAO;

        @InjectMocks
        private OAuthService sut;

        @Before
        public void setup() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
    public void validateTokenCallsDAO() throws DataUnreachableException {
        String token = "token";
        int userId = 1;
        OAuthClient client = new OAuthClient();

        when(authClientDAO.getOAuthClient(token, userId)).thenReturn(client);

        sut.validateToken(token, userId);

        verify(authClientDAO).getOAuthClient(token, userId);
    }

    @Test
    public void validateTokenReturnsFalse() throws DataUnreachableException {
        String token = "token";
        int userId = 1;
        OAuthClient client = new OAuthClient();

        when(authClientDAO.getOAuthClient(token, userId)).thenReturn(client);

        sut.validateToken(token, userId);

        verify(authClientDAO).getOAuthClient(token, userId);
    }

    @Test
    public void requestNewTokenCallsUserDAO() throws DataUnreachableException {
        String username = "user";
        String password = "pass";

        when(userDAO.getUserByUsernameAndPassword(username, password)).thenReturn(1);

        sut.requestNewToken(username, password);

        verify(userDAO).getUserByUsernameAndPassword(username, password);
    }

}
