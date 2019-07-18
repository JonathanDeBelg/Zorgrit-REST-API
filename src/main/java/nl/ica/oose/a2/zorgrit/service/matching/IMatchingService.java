package nl.ica.oose.a2.zorgrit.service.matching;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

import java.util.List;

public interface IMatchingService {
    /**
     * Calls all checks and matches a list of drivers with the requirements
     *
     * @param availableDrivers A list that contains Drivers
     * @param client           An object of the Class Client
     * @param ride             An object of the Class RideDTO
     * @return                 Returns a list with drivers that are matched
     * @throws DataUnreachableException
     */
    List<DriverDTO> requirementsCheck(final List<DriverDTO> availableDrivers, final ClientDTO client, final RideDTO ride) throws DataUnreachableException;
}
