package nl.ica.oose.a2.zorgrit.service.notification;

public interface INotificationService {
    /**
     * Notify a user with a message
     *
     * @param userId  An id representing the user
     * @param message THe message to send to the given user
     */
    void notify(final int userId, final String message);
}
