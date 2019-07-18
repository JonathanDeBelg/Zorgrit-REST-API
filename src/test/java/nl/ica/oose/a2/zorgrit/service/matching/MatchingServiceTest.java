package nl.ica.oose.a2.zorgrit.service.matching;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.UtilityDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.IDriverDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class MatchingServiceTest {

    @Mock
    private IDriverDAO driverDAO;

    @InjectMocks
    private MatchingService matchingService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRequirementsCheckIsTrue() throws DataUnreachableException {
        // Setup
        List<DriverDTO> driverDTOS = new ArrayList<>();
        DriverDTO driverDTO = new DriverDTO();
        ClientDTO clientDTO = new ClientDTO();
        RideDTO driverRide = new RideDTO();
        RideDTO clientRide = new RideDTO();
        List<ClientDTO> preferredClients = new ArrayList<>();
        UtilityDTO utilityDriver = new UtilityDTO();
        UtilityDTO utilityClient = new UtilityDTO();
        ArrayList<RideDTO> rides = new ArrayList<>();

        // Test
        clientDTO.setId(122);
        driverDTO.setId(5);

        driverDTO.setUtility(utilityDriver);
        driverDTO.getUtility().setName("Mobility Scooter");
        clientDTO.setUtility(utilityClient);
        clientDTO.getUtility().setName("Wheelchair");
        clientDTO.getUtility().setRating(1);
        driverDTO.getUtility().setRating(3);

        driverDTO.setNumberOfPassengers(2);
        clientDTO.setCompanion("Henk");

        preferredClients.add(clientDTO);
        driverDTO.setPreferredClients(preferredClients);

        clientRide.setId(12);
        clientRide.setClientId(clientDTO.getId());
        clientRide.setPickUpDateTime("2018-01-03 11:00:00");
        clientRide.setDuration(10);

        driverRide.setId(25);
        driverRide.setDriverId(driverDTO.getId());
        driverRide.setPickUpDateTime("2018-01-03 12:00:00");
        driverRide.setDuration(10);

        driverDTOS.add(driverDTO);
        rides.add(driverRide);

        when(driverDAO.getAllFutureRides(driverDTO.getId())).thenReturn(rides);

        // Verify
        assertEquals(driverDTOS, matchingService.requirementsCheck(driverDTOS, clientDTO, clientRide));
    }

    @Test
    public void testRequirementsCheckIsFalse() throws DataUnreachableException {
        // Setup
        List<DriverDTO> drivers = new ArrayList<>();
        DriverDTO driverDTO = new DriverDTO();
        ClientDTO clientDTO = new ClientDTO();
        RideDTO ride = new RideDTO();
        List<ClientDTO> preferredClients = new ArrayList<>();
        UtilityDTO utilityDriver = new UtilityDTO();
        UtilityDTO utilityClient = new UtilityDTO();
        ArrayList<RideDTO> rides = new ArrayList<>();

        // Test
        driverDTO.setUtility(utilityDriver);
        driverDTO.getUtility().setName("Mobility Scooter");
        clientDTO.setUtility(utilityClient);
        clientDTO.getUtility().setName("Wheelchair");
        clientDTO.setId(12);
        driverDTO.setId(4);
        clientDTO.getUtility().setRating(1);
        driverDTO.getUtility().setRating(3);
        driverDTO.setNumberOfPassengers(2);
        clientDTO.setCompanion("Henk");
        preferredClients.add(clientDTO);
        driverDTO.setPreferredClients(preferredClients);
        ride.setId(3);
        ride.setClientId(clientDTO.getId());
        ride.setPickUpDateTime("2018-01-03 12:00:00");
        ride.setDuration(20);
        ride.setDistance(1500);
        drivers.add(driverDTO);
        rides.add(ride);

        when(driverDAO.getAllFutureRides(driverDTO.getId())).thenReturn(rides);

        // Verify
        assertEquals(drivers, matchingService.requirementsCheck(drivers, clientDTO, ride));
    }
}
