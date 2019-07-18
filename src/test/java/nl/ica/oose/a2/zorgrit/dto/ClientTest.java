package nl.ica.oose.a2.zorgrit.dto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ClientTest {

    @Test
    public void testGetterSetterPreferredDrivers() {
        //Setup
        ClientDTO client = new ClientDTO();
        List<DriverDTO> drivers = new ArrayList<>();
        DriverDTO driverDTO = mock(DriverDTO.class);
        drivers.add(driverDTO);

        //Test
        client.setPreferredDrivers(drivers);

        //Verify
        assertEquals(drivers, client.getPreferredDrivers());
    }

    @Test
    public void testGetterSetterLimitations() {
        //Setup
        ClientDTO client = new ClientDTO();
        List<LimitationDTO> limitations = new ArrayList<>();
        LimitationDTO limitation = mock(LimitationDTO.class);
        limitations.add(limitation);

        //Test
        client.setLimitations(limitations);

        //Verify
        assertEquals(limitations, client.getLimitations());
    }

    @Test
    public void testGetterSetterCareInstitution() {
        //Setup
        ClientDTO client = new ClientDTO();
        CareInstitutionDTO careInstitution = mock(CareInstitutionDTO.class);

        //Test
        client.setCareInstitution(careInstitution);

        //Verify
        assertEquals(careInstitution, client.getCareInstitution());
    }

    @Test
    public void testGetterSetterCareUtility() {
        //Setup
        ClientDTO client = new ClientDTO();
        UtilityDTO utility = mock(UtilityDTO.class);

        //Test
        client.setUtility(utility);

        //Verify
        assertEquals(utility, client.getUtility());
    }

    @Test
    public void testGetterSetterDriverPreferenceForced() {
        //Setup
        ClientDTO client = new ClientDTO();
        boolean driverPreferenceForced = true;

        //Test
        client.setDriverPreferenceForced(driverPreferenceForced);

        //Verify
        assertEquals(driverPreferenceForced, client.isDriverPreferenceForced());
    }

    @Test
    public void testGetterSetterCompagnion() {
        //Setup
        ClientDTO client = new ClientDTO();
        String compagnion = "Henk Jansen";

        //Test
        client.setCompanion(compagnion);

        //Verify
        assertEquals(compagnion, client.getCompanion());
    }
}
