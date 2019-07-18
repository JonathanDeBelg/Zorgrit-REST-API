package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.service.ICareInstitutionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

public class CareInstitutionControllerTest {

    @InjectMocks
    private CareInstitutionController careInstitutionController;

    @Mock
    private ICareInstitutionService careInstitutionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void getAllCareInstitutions() {
        //Test
        careInstitutionController.getAllCareInstitutions();

        //Verify
        verify(careInstitutionService).getAllCareInstitutions();
    }

    @Test
    public void getCareInstitution() {
        //Test
        careInstitutionController.getCareInstitution(0);

        //Verify
        verify(careInstitutionService).getCareInstitution(0);

    }
}
