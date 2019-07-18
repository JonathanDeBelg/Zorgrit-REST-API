package nl.ica.oose.a2.zorgrit.service.notification;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class NotificationMessages {
    private static NotificationMessages instance;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NotificationMessages.class);
    private Properties properties;

    private NotificationMessages() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("notificationMessages.properties"));
        } catch (IOException e) {
            LOGGER.error("Can't access property file queries.properties", e);
        }
    }

    /**
     * Get an instance of NotificationMessages
     *
     * @return an instance of NotificationMessages
     */
    public static NotificationMessages getInstance() {
        if (instance == null) {
            instance = new NotificationMessages();
        }

        return instance;
    }

    /**
     * Get the specified message
     *
     * @param messageType The message to request
     * @return The requested message
     */
    public String getQuery(MessageType messageType) {
        return properties.getProperty(messageType.toString());
    }
}
