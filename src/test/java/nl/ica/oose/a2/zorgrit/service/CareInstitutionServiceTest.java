package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.CareInstitutionDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableRuntimeException;
import nl.ica.oose.a2.zorgrit.exceptions.RideNotPayedException;
import nl.ica.oose.a2.zorgrit.persistance.ICareInstitutionDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CareInstitutionServiceTest {

    @Mock
    private ICareInstitutionDAO careinstitutionDAO;

    @InjectMocks
    private ICareInstitutionService sut;
    private CareInstitutionDTO testCareInstitution;

    @BeforeEach
    void setup() {
        this.sut = new CareInstitutionService();
        this.testCareInstitution = new CareInstitutionDTO();
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void getAllCareInstitutions() {
        ArrayList<CareInstitutionDTO> careInstitutionList = new ArrayList<>();
        careInstitutionList.add(testCareInstitution);

        when(careinstitutionDAO.getAllCareInstitutions()).thenReturn(careInstitutionList);

        assertEquals(careInstitutionList, sut.getAllCareInstitutions());
    }

    @Test
    void getCareInstitution() {
        when(careinstitutionDAO.getCareInstitution(1)).thenReturn(testCareInstitution);

        assertEquals(testCareInstitution, sut.getCareInstitution(1));

    }

    @Test
    void getCareInstitutionThrowsDataUnreachableRuntimeException() {
        int careInstitutionId = 1;

        when(careinstitutionDAO.getCareInstitution(careInstitutionId)).thenReturn(null);

        assertThrows(DataUnreachableRuntimeException.class, () -> {
            sut.getCareInstitution(careInstitutionId);
        });
    }

    @Test
    void getAllCareInstitutionsThrowsDataUnreachableRuntimeException() {
        when(careinstitutionDAO.getAllCareInstitutions()).thenReturn(null);

        assertThrows(DataUnreachableRuntimeException.class, () -> {
            sut.getAllCareInstitutions();
        });
    }
}

