package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.OAuthClient;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

public interface IOAuthClientDAO {

    public OAuthClient getOAuthClient(final String token, final int userId) throws DataUnreachableException;

    public OAuthClient createOAuthClient(final int userId, final String token) throws DataUnreachableException;
}
