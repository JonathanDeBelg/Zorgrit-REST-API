package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.CareInstitutionDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

import java.util.ArrayList;
import java.util.List;

public interface ICareInstitutionDAO {

    /**
     * Get all drivers for a care institution
     *
     * @param careInstitutionId The care institution to get the drivers for
     * @return A list of drivers for a care institution
     * @throws DataUnreachableException
     */
    ArrayList<DriverDTO> getAllDrivers(final int careInstitutionId) throws DataUnreachableException;

    CareInstitutionDTO getCareInstitution(int careInstitutionId);

    List<CareInstitutionDTO> getAllCareInstitutions();
}
