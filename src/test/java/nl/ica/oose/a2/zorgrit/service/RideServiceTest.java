package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;
import nl.ica.oose.a2.zorgrit.exceptions.NoRecordsFoundRuntimeException;
import nl.ica.oose.a2.zorgrit.persistance.ClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.IRideDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RideServiceTest {
    private static final String NO_RECORDS_WHERE_FOUND = "No records where found.";

    @Mock
    private IRideDAO rideDAO;

    @Mock
    private ClientDAO clientDAO;

    @InjectMocks
    private RideService sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllRidesThrowsDataUnreachableException() {
        RidesDTO ridesDTO = new RidesDTO();

        when(rideDAO.getAllRides()).thenReturn(ridesDTO);

        Exception actualResult = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.getAllRides());
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, actualResult.getMessage());
    }

    @Test
    public void getAllRidesInfoThrowsDataUnreachableException() {
        RidesInformationDTO ridesInformationDTO = new RidesInformationDTO();

        when(rideDAO.getAllRidesInfo(1)).thenReturn(ridesInformationDTO);

        Exception actualResult = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.getAllRidesInfo(1));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, actualResult.getMessage());
    }

    @Test
    public void getAllNotMatchedRidesThrowsDataUnreachableException() {
        RidesInformationDTO ridesInformationDTO = new RidesInformationDTO();

        when(rideDAO.getAllNotMatchedRides(1)).thenReturn(ridesInformationDTO);

        Exception actualResult = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.getAllNotMatchedRides(1));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, actualResult.getMessage());
    }

    @Test
    public void getAllMatchedRidesThrowsDataUnreachableException() {
        RidesInformationDTO ridesInformationDTO = new RidesInformationDTO();

        when(rideDAO.getAllMatchedRides(1)).thenReturn(ridesInformationDTO);

        Exception actualResult = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.getAllMatchedRides(1));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, actualResult.getMessage());
    }

    @Test
    public void getAllRidesCareInstitutionThrowsDataUnreachableException() {
        RidesDTO ridesDTO = new RidesDTO();

        when(rideDAO.getAllRidesCareInstitution(1)).thenReturn(ridesDTO);

        Exception actualResult = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.getAllRidesCareInstitution(1));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, actualResult.getMessage());
    }

    @Test
    public void getAllRidesTownshipThrowsDataUnreachableException() {
        RidesDTO ridesDTO = new RidesDTO();

        when(rideDAO.getAllRidesTownship("test")).thenReturn(ridesDTO);

        Exception actualResult = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.getAllRidesTownship("test"));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, actualResult.getMessage());
    }

    @Test
    public void getAllRidesInfoCallsDAO() {
        RidesInformationDTO ridesInformationDTO = new RidesInformationDTO();
        RideInformationDTO rideInformationDTO = new RideInformationDTO();
        ridesInformationDTO.addRide(rideInformationDTO);

        when(rideDAO.getAllRidesInfo(1)).thenReturn(ridesInformationDTO);
        // Test
        sut.getAllRidesInfo(1);

        // Verify
        verify(rideDAO).getAllRidesInfo(1);
    }

    @Test
    public void getAllMatchedRidesCallsDAO() {
        RidesInformationDTO ridesInformationDTO = new RidesInformationDTO();
        RideInformationDTO rideInformationDTO = new RideInformationDTO();
        ridesInformationDTO.addRide(rideInformationDTO);

        when(rideDAO.getAllMatchedRides(1)).thenReturn(ridesInformationDTO);
        // Test
        sut.getAllMatchedRides(1);

        // Verify
        verify(rideDAO).getAllMatchedRides(1);
    }

    @Test
    public void getAllNotMatchedRidesCallsDAO() {
        RidesInformationDTO ridesInformationDTO = new RidesInformationDTO();
        RideInformationDTO rideInformationDTO = new RideInformationDTO();
        ridesInformationDTO.addRide(rideInformationDTO);

        when(rideDAO.getAllNotMatchedRides(1)).thenReturn(ridesInformationDTO);
        // Test
        sut.getAllNotMatchedRides(1);

        // Verify
        verify(rideDAO).getAllNotMatchedRides(1);
    }

    @Test
    public void deleteRideRatingCallsDAO() throws DataUnreachableException {
        int userId = 1;

        // Test
        sut.deleteRideRating(userId);

        // Verify
        verify(rideDAO).deleteRideRating(userId);
    }

    @Test
    public void putRideRatingCallsDAO() throws DataUnreachableException {
        int userId = 1;
        RideRating rideRating = new RideRating();

        // Test
        sut.putRideRating(userId , rideRating);

        // Verify
        verify(rideDAO).putRideRating(userId, rideRating);
    }

    @Test
    public void getAllRatingsByDriverCallsDAO() throws DataUnreachableException {
        int userId = 1;

        // Test
        sut.getAllRatingsByDriver(userId);

        // Verify
        verify(rideDAO).getAllRatingsByDriver(userId);
    }

    @Test
    public void addRideRatingCallsDAO() throws DataUnreachableException {
        int userId = 1;
        RideRating rideRating = new RideRating();

        // Test
        sut.addRideRating(userId , rideRating);

        // Verify
        verify(rideDAO).addRideRating(userId, rideRating);
    }

    @Test
    public void getRideRatingCallsDAO() throws DataUnreachableException {
        int userId = 1;

        // Test
        sut.getRideRating(userId);

        // Verify
        verify(rideDAO).getRideRating(userId);
    }

    @Test
    public void startRideCallsDAO() throws DataUnreachableException {
        int userId = 1;

        // Test
        sut.startRide(userId);

        // Verify
        verify(rideDAO).startRide(userId);
    }

    @Test
    public void getCurrentRideCallsDAO() throws DataUnreachableException {
        int userId = 1;

        // Test
        sut.getCurrentRide(userId);

        // Verify
        verify(rideDAO).getCurrentRide(userId);
    }

    @Test
    public void testGetRide_RideExcists_True() throws DataUnreachableException {
        // Test
        sut.getRide(2);

        // Verify
        verify(rideDAO).getRide(2);
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetRideDataUnreachableException() throws DataUnreachableException {
        //Setup
        int rideId = -15;
        when(rideDAO.getRide(rideId)).thenThrow(DataUnreachableException.class);

        //Test
        sut.getRide(rideId);
    }

    @Test
    public void testUpdateRide() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        RideDTO ride = new RideDTO();

        //Test
        sut.updateRide(ride);

        //Verify
        verify(rideDAO).updateRide(ride);
    }

    @Test
    public void testUpdateRepeatingRide() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        RideDTO ride = new RideDTO();
        ride.setExecuted(true);
        ride.setRepeatingRideId(2);
        ride.setPickUpDateTime("2018-03-03 12:00:00");

        Mockito.doReturn(1).when(clientDAO).addRide(ride);

        //Test
        sut.updateRide(ride);

        //Verify
        verify(rideDAO).updateRide(ride);
    }

    @Test(expected = DataUnreachableException.class)
    public void testUpdateRideDataUnreachableException() throws DataUnreachableException, InvalidDateFormatException {
        //Setup
        RideDTO ride = new RideDTO();
        doThrow(DataUnreachableException.class).when(rideDAO).updateRide(ride);

        //Test
        sut.updateRide(ride);
    }

//    @Test(expected = InvalidDateFormatException.class)
//    public void testUpdateRideInvalidDateFormatException() throws DataUnreachableException, InvalidDateFormatException {
//        //Setup
//        RideDTO ride = new RideDTO();
//        doThrow(InvalidDateFormatException.class).when(rideDAO).updateRide(ride);
//
//        //Test
//        sut.updateRide(ride);
//    }

    @Test
    public void getAllRidesReturnsRides() {
        RideDTO testRide = new RideDTO();
        ArrayList<RideDTO> rideList = new ArrayList<>();

        rideList.add(testRide);

        RidesDTO rides = new RidesDTO();
        rides.setRides(rideList);

        when(rideDAO.getAllRides())
                .thenReturn(rides);

        assertEquals(rides, sut.getAllRides());
    }



    @Test
    public void getAllRidesTownshipReturnsRides() {
        RideDTO testDriver = new RideDTO();
        ArrayList<RideDTO> rideList = new ArrayList<>();

        rideList.add(testDriver);

        RidesDTO rides = new RidesDTO();
        rides.setRides(rideList);

        when(rideDAO.getAllRidesTownship("Test"))
                .thenReturn(rides);

        assertEquals(rides, sut.getAllRidesTownship("Test"));
    }


    @Test
    public void getAllRidesCareInstitutionReturnsRides() {
        RideDTO rideDTO = new RideDTO();
        RidesDTO rides = new RidesDTO();
        rides.addRide(rideDTO);

        when(rideDAO.getAllRidesCareInstitution(1))
                .thenReturn(rides);

        RidesDTO actualResult = sut.getAllRidesCareInstitution(1);

        assertEquals(rides, actualResult);
    }

    @Test
    public void getRideToMatchReturnsRides() {
        RideInformationDTO ride = new RideInformationDTO();
        ride.setId(1);

        when(rideDAO.getRideToMatch(1))
                .thenReturn(ride);

        sut.getRideToMatch(1);

        verify(rideDAO).getRideToMatch(ride.getId());
    }

    @Test
    public void matchRideToDriverCallsDAO() {
        int rideId = 1;
        int driverId = 1;
        RideInformationDTO ride = new RideInformationDTO();
        ride.setId(rideId);

        when(rideDAO.getRideToMatch(1))
                .thenReturn(ride);

        sut.matchRideToDriver(rideId, driverId);
        verify(rideDAO).matchRideToDriver(ride.getId(), driverId);
    }

    @Test
    public void getRideToMatchThrowsDataUnreachableException() {
        int rideId = 1;
        RideInformationDTO ride = new RideInformationDTO();
        when(rideDAO.getRideToMatch(rideId))
                .thenReturn(ride);

        Exception exception = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.getRideToMatch(rideId));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, exception.getMessage());
    }

    @Test
    public void matchRideToDriverThrowsDataUnreachableException() {
        int rideId = 1;
        int driverId = 1;
        RideInformationDTO ride = new RideInformationDTO();
        when(rideDAO.getRideToMatch(rideId))
                .thenReturn(ride);

        Exception exception = assertThrows(NoRecordsFoundRuntimeException.class, () -> sut.matchRideToDriver(rideId, driverId));
        Assertions.assertEquals(NO_RECORDS_WHERE_FOUND, exception.getMessage());
    }
}
