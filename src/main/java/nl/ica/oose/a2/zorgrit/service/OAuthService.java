package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.OAuthClient;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.IOAuthClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.IUserDAO;

import javax.inject.Inject;
import java.security.SecureRandom;
import java.util.Base64;

public class OAuthService implements IOAuthService {

    @Inject
    private IOAuthClientDAO dao;

    @Inject
    private IUserDAO userDAO;

    public boolean validateToken(String token, int userId) throws DataUnreachableException {
        OAuthClient client = dao.getOAuthClient(token, userId);
        return client.getUserId() != 0;
    }

    public OAuthClient requestNewToken(String username, String password) throws DataUnreachableException {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        String keySource = username + (System.currentTimeMillis() / 1000L) + Base64.getEncoder().encodeToString(bytes);
        String token = Base64.getEncoder().encodeToString(keySource.getBytes());

        int userId = userDAO.getUserByUsernameAndPassword(username, password);

        //USER SUCCESFULLY AUTHENTICATED
        if(userId != Integer.MIN_VALUE) {
            return dao.createOAuthClient(userId, token);
        }
        return null;
    }
}
