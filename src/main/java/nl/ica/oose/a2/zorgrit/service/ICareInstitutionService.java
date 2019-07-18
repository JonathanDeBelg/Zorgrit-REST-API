package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.CareInstitutionDTO;

import java.util.List;

public interface ICareInstitutionService {

    List<CareInstitutionDTO> getAllCareInstitutions();

    CareInstitutionDTO getCareInstitution(int careInstitutionId);
}
