package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.OAuthClient;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

public interface IOAuthService {
    boolean validateToken(final String token, final int userId) throws DataUnreachableException;

    OAuthClient requestNewToken(final String username, final String password) throws DataUnreachableException;
}
