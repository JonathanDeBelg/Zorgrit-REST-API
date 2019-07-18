package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.OAuthClient;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.util.Bind;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import nl.ica.oose.a2.zorgrit.persistance.util.QueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OAuthClientDAO extends DatabaseConnection implements IOAuthClientDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthClientDAO.class);

    public OAuthClient getOAuthClient(final String token, final int userId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;
        OAuthClient client;

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.OAUTH_GET));
            statement.setString(1, token);
            statement.setInt(2, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                client = new Bind().oAuthClient(resultSet);
            } else {
                return null;
            }

            closeConnection(connection, statement);

            return client;
        } catch (SQLException e)  {
            LOGGER.error("OAuthDAO getToken: ", e);
            throw new DataUnreachableException();
        }
    }

    public OAuthClient createOAuthClient(final int userId, final String token) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;
        OAuthClient client = new OAuthClient();

        try {
            connection = openConnection();

            statement = connection.prepareStatement(queries.getQuery(QueryType.OAUTH_POST));
            statement.setString(1, token);
            statement.setInt(2, userId);
            statement.executeUpdate();

            client.setUserId(userId);
            client.setToken(token);
            return client;
        } catch (SQLException e) {
            LOGGER.error("OAUTHDAO createOAuthClient: ", e);
            throw new DataUnreachableException();
        }
    }
}
