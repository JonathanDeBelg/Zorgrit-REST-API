package nl.ica.oose.a2.zorgrit.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class RideTest {

    @Test
    public void ShouldPassAllPojoTests() {
        // Setup
        final Class<?> classUnderTest = RideDTO.class;

        // Verify
        assertPojoMethodsFor(classUnderTest).quickly();
    }

    @Test
    public void testGetterSetterId() {
        //Setup
        RideDTO ride = new RideDTO();
        int id = 1;

        //Test
        ride.setId(id);

        //Verify
        assertEquals(id, ride.getId());
    }

    @Test
    public void testGetterSetterClientId() {
        //Setup
        RideDTO ride = new RideDTO();
        int clientId = 1;

        //Test
        ride.setClientId(clientId);

        //Verify
        assertEquals(clientId, ride.getClientId());
    }

    @Test
    public void testGetterSetterClientDriverId() {
        //Setup
        RideDTO ride = new RideDTO();
        int driverId = 1;

        //Test
        ride.setDriverId(driverId);

        //Verify
        assertEquals(driverId, ride.getDriverId());
    }


    @Test
    public void testGetterSetterPreferredDriver() {
        //Setup
        RideDTO ride = new RideDTO();
        int driverId = 1;

        //Test
        ride.setPreferedDriver(driverId);

        //Verify
        assertEquals(driverId, ride.getPreferedDriver());
    }

    @Test
    public void testGetterSetterPreferredCareInstitution() {
        //Setup
        RideDTO ride = new RideDTO();
        int careInstitution = 25;

        //Test
        ride.setPreferedCareInstitution(careInstitution);

        //Verify
        assertEquals(careInstitution, ride.getPreferedCareInstitution());
    }

    @Test
    public void testGetterSetterNumberOfCompagnions() {
        //Setup
        RideDTO ride = new RideDTO();
        int compagnions = 2;

        //Test
        ride.setNumberOfCompanions(compagnions);

        //Verify
        assertEquals(compagnions, ride.getNumberOfCompanions());
    }

    @Test
    public void testGetterSetterNumberOfLuggage() {
        //Setup
        RideDTO ride = new RideDTO();
        int luggage = 2;

        //Test
        ride.setNumberOfLuggage(luggage);

        //Verify
        assertEquals(luggage, ride.getNumberOfLuggage());
    }

    @Test
    public void testGetterSetterPickUpDateTime() {
        //Setup
        RideDTO ride = new RideDTO();
        String pickUpDateTime = "2017-12-08 23:20:05";

        //Test
        ride.setPickUpDateTime(pickUpDateTime);

        //Verify
        assertEquals(pickUpDateTime, ride.getPickUpDateTime());
    }

    @Test
    public void testGetterSetterPickUpLocation() {
        //Setup
        RideDTO ride = new RideDTO();
        String pickUpLocation = "Arnhem HAN";

        //Test
        ride.setPickUpLocation(pickUpLocation);

        //Verify
        assertEquals(pickUpLocation, ride.getPickUpLocation());
    }

    @Test
    public void testGetterSetterDropOffLocation() {
        //Setup
        RideDTO ride = new RideDTO();
        String dropOffLocation = "Arnhem station";

        //Test
        ride.setDropOffLocation(dropOffLocation);

        //Verify
        assertEquals(dropOffLocation, ride.getDropOffLocation());
    }

    @Test
    public void testGetterSetterReturnRide() {
        //Setup
        RideDTO ride = new RideDTO();
        boolean returnRide = false;

        //Test
        ride.setReturnRide(returnRide);

        //Verify
        assertEquals(returnRide, ride.isReturnRide());
    }

    @Test
    public void testGetterSetterCallService() {
        //Setup
        RideDTO ride = new RideDTO();
        boolean callService = true;

        //Test
        ride.setCallService(callService);

        //Verify
        assertEquals(callService, ride.isCallService());
    }

    @Test
    public void testGetterSetterUtility() {
        //Setup
        RideDTO ride = new RideDTO();
        String utility = "test";

        //Test
        ride.setUtility(utility);

        //Verify
        assertEquals(utility, ride.getUtility());
    }

    @Test
    public void testGetterSetterRepeatingRideId() {
        //Setup
        RideDTO ride = new RideDTO();
        int repeatingRideId = 23;

        //Test
        ride.setRepeatingRideId(repeatingRideId);

        //Verify
        assertEquals(repeatingRideId, ride.getRepeatingRideId());
    }

    @Test
    public void testGetterSetterCancelledByClient() {
        //Setup
        RideDTO ride = new RideDTO();
        boolean cancelledByClient = false;

        //Test
        ride.setCancelledByClient(cancelledByClient);

        //Verify
        assertEquals(cancelledByClient, ride.isCancelledByClient());
    }

    @Test
    public void testGetterSetterExecutedRide() {
        //Setup
        RideDTO ride = new RideDTO();
        boolean executedRide = false;

        //Test
        ride.setExecuted(executedRide);

        //Verify
        assertEquals(executedRide, ride.isExecuted());
    }

    @Test
    public void testGetterSetterDuration() {
        //Setup
        RideDTO ride = new RideDTO();
        int duration = 25;

        //Test
        ride.setDuration(duration);

        //Verify
        assertEquals(duration, ride.getDuration());
    }
}
