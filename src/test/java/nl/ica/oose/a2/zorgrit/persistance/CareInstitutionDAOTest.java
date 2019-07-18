package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.CareInstitutionDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CareInstitutionDAOTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllDrivers() throws DataUnreachableException {
        //Setup
        CareInstitutionDAO careInstitutionDAO = new CareInstitutionDAO();
        int careInstitutionId =1;

        //Verify
        assertEquals(careInstitutionDAO.getAllDrivers(careInstitutionId).size(), 2);
    }

    @Test
    public void getCareInstitutionReturnsCorrectCareInstitution() throws DataUnreachableException {
        //Setup
        CareInstitutionDAO careInstitutionDAO = new CareInstitutionDAO();
        int careInstitutionId =1;

        CareInstitutionDTO careInstitution = careInstitutionDAO.getCareInstitution(careInstitutionId);
        String actualName = "Reinearde";
        //Verify
        assertEquals(actualName, careInstitution.getName());
    }

    @Test
    public void getAllCareInstitutionsReturnsCorrectNumberOfCareInstitutions() throws DataUnreachableException {
        //Setup
        CareInstitutionDAO careInstitutionDAO = new CareInstitutionDAO();

        List<CareInstitutionDTO> careInstitutions = careInstitutionDAO.getAllCareInstitutions();

        //Verify
        assertEquals(5, careInstitutions.size());
    }

    @Test(expected = DataUnreachableException.class)
    public void testGetAllDriversDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        CareInstitutionDAO careInstitutionDAO = Mockito.spy(new CareInstitutionDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)careInstitutionDAO).openConnection();
        int careInstitutionId = 10;

        //Test
        careInstitutionDAO.getAllDrivers(careInstitutionId);
    }

    @Test(expected = RuntimeException.class)
    public void getAllCareInstitutionsReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        CareInstitutionDAO careInstitutionDAO = Mockito.spy(new CareInstitutionDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)careInstitutionDAO).openConnection();

        //Test
        careInstitutionDAO.getAllCareInstitutions();
    }

    @Test(expected = RuntimeException.class)
    public void getCareInstitutionReturnsDataUnreachableException() throws DataUnreachableException, SQLException {
        //Setup
        CareInstitutionDAO careInstitutionDAO = Mockito.spy(new CareInstitutionDAO());
        Mockito.doThrow(SQLException.class).when((DatabaseConnection)careInstitutionDAO).openConnection();
        int careInstitutionId = 1;

        //Test
        careInstitutionDAO.getCareInstitution(careInstitutionId);
    }
}
