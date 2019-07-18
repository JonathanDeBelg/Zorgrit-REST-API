package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.CareInstitutionDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableRuntimeException;
import nl.ica.oose.a2.zorgrit.persistance.ICareInstitutionDAO;

import javax.inject.Inject;
import java.util.List;

public class CareInstitutionService implements ICareInstitutionService {
    private static final String DATAUNREACHABLEMESSAGE = "Data is unreachable.";

    @Inject
    private ICareInstitutionDAO careinstitutionDAO;

    @Override
    public List<CareInstitutionDTO> getAllCareInstitutions() {
        List<CareInstitutionDTO> careInstitutions = careinstitutionDAO.getAllCareInstitutions();
        if (careInstitutions != null) {
            return careInstitutions;
        } else {
            throw new DataUnreachableRuntimeException(DATAUNREACHABLEMESSAGE);
        }
    }

    @Override

    public CareInstitutionDTO getCareInstitution(int careInstitutionId) {
        CareInstitutionDTO careInstitution = careinstitutionDAO.getCareInstitution(careInstitutionId);
        if (careInstitution != null) {
            return careInstitution;
        } else {
            throw new DataUnreachableRuntimeException(DATAUNREACHABLEMESSAGE);
        }
    }
}
