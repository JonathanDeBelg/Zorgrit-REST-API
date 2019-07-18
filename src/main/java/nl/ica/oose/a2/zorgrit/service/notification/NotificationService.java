package nl.ica.oose.a2.zorgrit.service.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationService implements INotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    public void notify(final int userId, final String message) {
        LOGGER.info(userId + " " + message);
    }
}
