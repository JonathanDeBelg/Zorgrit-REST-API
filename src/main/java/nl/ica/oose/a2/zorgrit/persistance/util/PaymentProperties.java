package nl.ica.oose.a2.zorgrit.persistance.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class PaymentProperties {
    private static PaymentProperties instance;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentProperties.class);
    private static final String MOLLIE_API_URL = "MOLLIE_API_URL";
    private static final String MOLLIE_API_KEY = "MOLLIE_API_KEY";
    private static final String APP_RETURN_URL = "APP_RETURN_URL";
    private static final String SERVER_WEBHOOK_URL = "SERVER_WEBHOOK_URL";
    private Properties properties;

    private PaymentProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("payment.properties"));
        } catch (IOException e) {
            LOGGER.error("Can't access payment.properties file", e);
        }
    }

    /**
     * Get an instance of DatabaseQueries
     *
     * @return an instance of DatabaseQueries
     */
    public static PaymentProperties getInstance() {
        if (instance == null) {
            instance = new PaymentProperties();
        }

        return instance;
    }

    public String getMollieApiUrl() {
        return properties.getProperty(MOLLIE_API_URL);
    }

    public String getMollieApiKey() {
        return properties.getProperty(MOLLIE_API_KEY);
    }

    public String getAppReturnUlr() {
        return properties.getProperty(APP_RETURN_URL);
    }

    public String getServerWebhookUrl() {
        return properties.getProperty(SERVER_WEBHOOK_URL);
    }
}
