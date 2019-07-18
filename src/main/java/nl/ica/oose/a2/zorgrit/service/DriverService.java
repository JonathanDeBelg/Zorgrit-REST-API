package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.UserDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.NoRecordsFoundRuntimeException;
import nl.ica.oose.a2.zorgrit.persistance.IClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.IDriverDAO;
import nl.ica.oose.a2.zorgrit.persistance.IUserDAO;
import nl.ica.oose.a2.zorgrit.service.ridematcher.IRideMatcher;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class DriverService implements IDriverService {
    private static final String NO_RECORDS_WHERE_FOUND = "No records where found.";

    @Inject
    private IDriverDAO driverDAO;

    @Inject
    private IClientDAO clientDAO;

    @Inject
    private IRideMatcher rideMatcher;

    @Inject
    private IUserDAO userDAO;

    public ArrayList<RideDTO> getAllFutureRides(final int driverId) throws DataUnreachableException {
        return driverDAO.getAllFutureRides(driverId);
    }

    public void acceptRide(final int driverId, final int rideId) throws DataUnreachableException {
        driverDAO.acceptRide(driverId, rideId);
    }

    public void cancelRide(final int driverId, final int rideId) throws DataUnreachableException {
        driverDAO.cancelRide(driverId, rideId);
        rideMatcher.rejectRide(rideId, driverId);
    }

    public ArrayList<RideDTO> getAllRequestRides(final int driverId) throws DataUnreachableException {
        return driverDAO.getAllRequestRidesForDriver(driverId);
    }

    public void updateProfile(final int driverId, final DriverDTO driver) throws DataUnreachableException {
        driverDAO.updateProfile(driverId, driver);
    }

    public List<ClientDTO> getAllClientsForDriver(final int driverId) throws DataUnreachableException {
        return driverDAO.getAllClientsForDriver(driverId);
    }

    public ArrayList<DriverDTO> getAllDriversForClient(final int clientId) throws DataUnreachableException {
        return driverDAO.getAllDriversForClient(clientId);
    }

    public ArrayList<RideDTO> getAllCompletedRides(final int driverId) throws DataUnreachableException {
        return driverDAO.getAllCompletedRides(driverId);
    }

    public ArrayList<RideDTO> getAllCancelledRides(final int driverId) throws DataUnreachableException {
        return driverDAO.getAllCancelledRides(driverId);
    }

    @Override
    public List<DriverDTO> getAllDrivers() {
        List<DriverDTO> drivers = driverDAO.getAllDrivers();
        if (!drivers.isEmpty()) {
            return drivers;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public DriverDTO getDriver(int driverId) {
        DriverDTO driverDTO = driverDAO.getDriver(driverId);
        if (driverDTO.getId() != 0) {
            return driverDTO;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public List<DriverDTO> getAllDriversTownship(String township) {
        List<DriverDTO> drivers = driverDAO.getAllDriversTownship(township);
        if (!drivers.isEmpty()) {
            return drivers;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public List<DriverDTO> getAllDriversCareInstitution(int careInstitution) {
        List<DriverDTO> drivers = driverDAO.getAllDriversCareInstitution(careInstitution);
        if (!drivers.isEmpty()) {
            return drivers;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public void createDriver(DriverDTO driverDTO) {
        this.userDAO.createUser(driverDTO);
        this.driverDAO.createDriver(driverDTO);
    }

    @Override
    public void editDriver(int driverId, DriverDTO driverDTO) {
        DriverDTO user = driverDAO.getDriver(driverId);

        if (user.getId() != 0) {
            driverDAO.editDriver(driverId, driverDTO);
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public void deleteDriver(int driverId) {
        List<RideDTO> ridesDTO = driverDAO.checkIfDriverHasRides(driverId);
        if (!ridesDTO.isEmpty()) {
            userDAO.setUserInActiveSince(driverId);
        } else {
            this.driverDAO.deleteDriver(driverId);
        }
    }
    @Override
    public List<DriverDTO> getPreferredDriversToMatch(int clientId) {
        UserDTO user = clientDAO.getClient(clientId);
        if (user.getId() != 0) {
            return driverDAO.getPreferredDriversToMatch(clientId);
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }


    }
}
