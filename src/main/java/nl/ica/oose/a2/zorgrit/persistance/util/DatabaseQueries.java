package nl.ica.oose.a2.zorgrit.persistance.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;


/**
 * Database queries
 *
 * Loads database queries
 */
public class DatabaseQueries {
    private static DatabaseQueries instance;

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseQueries.class);
    private Properties properties;

    private DatabaseQueries() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("queries.properties"));
        } catch (IOException e) {
            LOGGER.error("Can't access property file queries.properties", e);
        }
    }

    /**
     * Get an instance of DatabaseQueries
     *
     * @return an instance of DatabaseQueries
     */
    public static DatabaseQueries getInstance() {
        if (instance == null) {
            instance = new DatabaseQueries();
        }

        return instance;
    }

    /**
     * Get the specified database query
     *
     * @param queryType The query to request
     * @return The requested query
     */
    public String getQuery(QueryType queryType) {
        return properties.getProperty(queryType.toString());
    }
}
