package nl.ica.oose.a2.zorgrit.persistance.util;

import nl.ica.oose.a2.zorgrit.controller.DriverController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Database connection
 *
 * A class to be extended on which manages database connections.
 */
public class DatabaseConnection {
    private DatabaseProperties databaseProperties;
    protected DatabaseQueries queries;
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverController.class);

    protected DatabaseConnection() {

        this.databaseProperties = DatabaseProperties.getInstance();
        this.queries = DatabaseQueries.getInstance();

        try {
            Class.forName(databaseProperties.getDriver());
        } catch (ClassNotFoundException e) {
            LOGGER.error("DatabaseConnection constructor: ", e);
        }
    }

    /**
     * Open a connection with the database
     *
     * @return a database connection
     * @throws SQLException
     */
    public Connection openConnection() throws SQLException {
        try {
            return DriverManager.getConnection(
                    databaseProperties.getConnectionString(),
                    databaseProperties.getUser(),
                    databaseProperties.getPass());
        } catch (SQLException e) {
            LOGGER.error("DatabaseConnection openConnection: ", e);
            throw e;
        }
    }

    /**
     * Closes connections
     *
     * @param connection The connection to close
     * @param statement  The SQL statement to close
     */
    protected void closeConnection(Connection connection, PreparedStatement statement) {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("DatabaseConnection closeConnection: ", e);
        }
    }

}
