package nl.ica.oose.a2.zorgrit.persistance.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database poperties
 *
 * Loads database settings
 */
public class DatabaseProperties {
    private static DatabaseProperties instance;

    private Logger logger = Logger.getLogger(getClass().getName());
    private Properties properties;

    private DatabaseProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can't access property file database.properties", e);
        }
    }

    /**
     * Get an instance of DatabaseProperties
     *
     * @return an instance of DatabaseProperties
     */
    public static DatabaseProperties getInstance() {
        if (instance == null) {
            instance = new DatabaseProperties();
        }

        return instance;
    }

    /**
     * Get a driver string
     *
     * @return the driver string
     */
    public String getDriver() {
        return properties.getProperty("driver");
    }

    public String getUser() {
        return properties.getProperty("user");
    }

    public String getPass() {
        return properties.getProperty("pass");
    }

    /**
     * Get a connection string
     *
     * @return the connection string
     */
    public String getConnectionString() {
        return properties.getProperty("connectionString");
    }

}
