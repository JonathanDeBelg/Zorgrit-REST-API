package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RideDAOTest extends DatabaseConnection {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRideThrowIllegalArgumentException() throws DataUnreachableException {
        //Setup
        int rideId = 9999999;
        RideDAO rideDAO = new RideDAO();

        //Test
        rideDAO.getRide(rideId);
    }

    @Test
    public void testGetRide() throws DataUnreachableException {
        int rideId = 1;
        RideDAO rideDAO = new RideDAO();
        RideDTO ride = rideDAO.getRide(rideId);
        assertTrue(ride instanceof RideDTO);
    }

    @Test
    public void testUpdateRide() throws SQLException, DataUnreachableException {
        //Setup
        IRideDAO rideDAO = new RideDAO();
        RideDTO ride = new RideDTO();
        Connection connection = openConnection();

        int rideId = 1;
        int clientId = 2;
        int preferedCareInstitution = 1;
        int preferedDriver = 1;
        String pickUpDateTime = "2025-06-06 23:00:00";
        String pickUpLocation = "Dorpstraat 4";
        String dropOffLocation = "Dorplaan 35";
        int numberOfCompagnions = 2;
        int numberOffLuggage = 2;
        boolean returnRide = false;
        boolean callService = false;
        String utility = "rollator";
        int repeatingRideId = -1;
        boolean cancelledByClient = false;
        boolean executed = false;

        ride.setId(rideId);
        ride.setClientId(clientId);
        ride.setPreferedCareInstitution(preferedCareInstitution);
        ride.setPreferedDriver(preferedDriver);
        ride.setPickUpDateTime(pickUpDateTime);
        ride.setPickUpLocation(pickUpLocation);
        ride.setDropOffLocation(dropOffLocation);
        ride.setNumberOfCompanions(numberOfCompagnions);
        ride.setNumberOfLuggage(numberOffLuggage);
        ride.setReturnRide(returnRide);
        ride.setCallService(callService);
        ride.setUtility(utility);
        ride.setRepeatingRideId(repeatingRideId);
        ride.setCancelledByClient(cancelledByClient);
        ride.setExecuted(executed);

        //Test
        rideDAO.updateRide(ride);

        //UserDTO table
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ride WHERE id = ?");
        preparedStatement.setInt(1, ride.getId());
        ResultSet resultset = preparedStatement.executeQuery();
        resultset.next();

        //Verify
        assertEquals(clientId, resultset.getInt("clientId"));
        assertEquals(preferedCareInstitution, resultset.getInt("preferedCareInstitution"));
        assertEquals(preferedDriver, resultset.getInt("preferedDriver"));
        assertEquals(pickUpLocation, resultset.getString("pickUpLocation"));
        assertEquals(dropOffLocation, resultset.getString("dropOffLocation"));
        assertEquals(numberOfCompagnions, resultset.getInt("numberOfcompanions"));
        assertEquals(numberOffLuggage, resultset.getInt("numberOfLuggage"));
        assertEquals(returnRide, resultset.getBoolean("returnRide"));
        assertEquals(callService, resultset.getBoolean("callService"));
        assertEquals(utility, resultset.getString("utility"));
        assertEquals(repeatingRideId, resultset.getInt("repeatingRideId"));
        assertEquals(returnRide, resultset.getBoolean("returnRide"));
        assertEquals(cancelledByClient, resultset.getBoolean("cancelledByClient"));
        assertEquals(executed, resultset.getBoolean("executed"));
    }
}
