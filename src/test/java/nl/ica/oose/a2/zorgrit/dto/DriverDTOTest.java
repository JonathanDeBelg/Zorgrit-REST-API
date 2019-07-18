package nl.ica.oose.a2.zorgrit.dto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class DriverDTOTest {

    @Test
    public void testGetterSetterCareInstitution() {
        // Setup
        DriverDTO driverDTO = new DriverDTO();
        CareInstitutionDTO careInstitutionDTO = mock(CareInstitutionDTO.class);

        // Test
        driverDTO.setCareInstitution(careInstitutionDTO);

        // Verify
        assertEquals(careInstitutionDTO, driverDTO.getCareInstitution());
    }

    @Test
    public void testGetterSetterVerification() {
        //Setup
        DriverDTO driverDTO = new DriverDTO();
        boolean verification = driverDTO.isVerified();

        // Test
        driverDTO.setVerification(false);

        // Verify
        assertEquals(verification, driverDTO.isVerified());
    }

    @Test
    public void testGetterSetterUtility() {
        //Setup
        DriverDTO driverDTO = new DriverDTO();
        UtilityDTO utilityDTO = mock(UtilityDTO.class);

        // Test
        driverDTO.setUtility(utilityDTO);

        // Verify
        assertEquals(utilityDTO, driverDTO.getUtility());
    }

    @Test
    public void testGetterSetterCareUtility() {
        //Setup
        DriverDTO driverDTO = new DriverDTO();
        CareInstitutionDTO careInstitutionDTO = mock(CareInstitutionDTO.class);

        //Test
        driverDTO.setCareInstitution(careInstitutionDTO);

        //Verify
        assertEquals(careInstitutionDTO, driverDTO.getCareInstitution());
    }

    @Test
    public void testGetterSetterNumberOfPassengers() {
        //Setup
        DriverDTO driverDTO = new DriverDTO();
        int numberOfPassengers = 3;

        //Test
        driverDTO.setNumberOfPassengers(numberOfPassengers);

        //Verify
        assertEquals(numberOfPassengers, driverDTO.getNumberOfPassengers());
    }

    @Test
    public void testGetterSetterPrefferedClients() {
        //Setup
        DriverDTO driverDTO = new DriverDTO();
        List<ClientDTO> preferredClients = new ArrayList<>();
        ClientDTO clientDTO = mock(ClientDTO.class);
        preferredClients.add(clientDTO);

        //Test
        driverDTO.setPreferredClients(preferredClients);

        //Verify
        assertEquals(preferredClients, driverDTO.getPreferredClients());
    }
}
