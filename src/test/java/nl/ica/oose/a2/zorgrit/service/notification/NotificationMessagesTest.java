package nl.ica.oose.a2.zorgrit.service.notification;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class NotificationMessagesTest {

    @Test
    public void testGetInstance() {
        assertTrue(NotificationMessages.getInstance() instanceof NotificationMessages);
    }

    @Test
    public void testGetQuery() {
        //Setup
        NotificationMessages notificationMessages = NotificationMessages.getInstance();
        Properties properties = mock(Properties.class);
        String message = "U heeft een nieuwe rit klaarstaan.";

        //Test
        assertEquals(message, notificationMessages.getQuery(MessageType.DRIVER_NEW_PROPOSED_RIDE));
    }
}
