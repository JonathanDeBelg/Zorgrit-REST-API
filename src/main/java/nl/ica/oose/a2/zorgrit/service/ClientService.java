package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;
import nl.ica.oose.a2.zorgrit.exceptions.NoRecordsFoundRuntimeException;
import nl.ica.oose.a2.zorgrit.exceptions.RideNotPayedException;
import nl.ica.oose.a2.zorgrit.persistance.IClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.IRideDAO;
import nl.ica.oose.a2.zorgrit.persistance.IUserDAO;
import nl.ica.oose.a2.zorgrit.service.ridematcher.IRideMatcher;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ClientService implements IClientService {
    private static final String NO_RECORDS_WHERE_FOUND = "No records where found.";

    @Inject
    private IClientDAO clientDAO;

    @Inject
    private IRideDAO rideDAO;

    @Inject
    private IUserDAO userDAO;

    @Inject
    private IRideMatcher rideMatcher;

    public ArrayList<RideDTO> getAllFutureRides(final int clientId) throws DataUnreachableException {
        return clientDAO.getAllFutureRides(clientId);
    }

    public void cancelRide(final int rideId) throws DataUnreachableException {
        clientDAO.cancelRide(rideId);
    }

    public ArrayList<CareInstitutionDTO> getAllCareInstitutions(final int clientId) throws DataUnreachableException {
        return clientDAO.getAllCareInstitutions(clientId);
    }

    public UserDTO getProfile(final int clientId) throws DataUnreachableException {
        return clientDAO.getProfile(clientId);
    }

    public void updateProfile(final int clientId, final ClientDTO client) throws DataUnreachableException {
        clientDAO.updateProfile(clientId, client);
    }

    public void addRide(final RideDTO ride) throws DataUnreachableException, InvalidDateFormatException {
        ride.setId(clientDAO.addRide(ride));
        rideMatcher.startMatching(ride);
    }

    public void cancelRepeatingRides(final int repeatingRideId) throws DataUnreachableException, InvalidDateFormatException {
        clientDAO.cancelRepeatingRides(repeatingRideId);
    }

    public ArrayList<RideDTO> getClientAllCancelledRides(final int clientId) throws DataUnreachableException {
        return clientDAO.getAllCancelledRides(clientId);
    }

    public ArrayList<RideDTO> getClientAllCompletedRides(final int clientId) throws DataUnreachableException {
        return clientDAO.getAllCompletedRides(clientId);
    }

    @Override
    public List<ClientDTO> getAllClientsCareInstitution(int careInstitution) {
        List<ClientDTO> clients = clientDAO.getAllClientsCareInstitution(careInstitution);

        if (!clients.isEmpty()) {
            return clients;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public ClientDTO getClient(int clientId) {
        ClientDTO client = clientDAO.getClient(clientId);

        if (client.getId() != 0) {
            return client;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public void editClient(int userId, ClientDTO client) {
        UserDTO user = clientDAO.getClient(userId);

        if (user.getId() != 0) {
            clientDAO.editClient(userId, client);
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public void createClient(ClientDTO client) {
        clientDAO.addClient(client);
    }

    @Override
    public List<LimitationDTO> getAllLimitations() {
        List<LimitationDTO> limitations = clientDAO.getLimitations();

        if (!limitations.isEmpty()) {
            return limitations;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public List<UtilityDTO> getClientUtilities(int userId) {
        UserDTO user = clientDAO.getClient(userId);
        List<UtilityDTO> utilities = clientDAO.getClientUtilities(userId);

        if (user.getId() != 0 && !utilities.isEmpty()) {
            return utilities;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public List<UtilityDTO> getAllUtilities() {
        List<UtilityDTO> utilities = clientDAO.getUtilities();

        if (!utilities.isEmpty()) {
            return utilities;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    //kijkt eerst of de client bestaande ritten heeft, zo ja dan wordt de invisableSince van de client ingevuld, zo nee dan wordt deze direct verwijderd uit de database.
    @Override
    public void deleteClient(int clientId) {
        if (!rideDAO.checkIfClientHasUnPayedRides(clientId)) {
            List<RideDTO> ridesDTO = clientDAO.checkIfClientHasRides(clientId);
            if (ridesDTO != null) {
                userDAO.setUserInActiveSince(clientId);
            } else {
                clientDAO.deleteAllTablesForClient(clientId);
            }
        } else {
            throw new RideNotPayedException("Er zijn nog niet betaalde ritten, zolang deze open staan kan de client niet verwijderd worden");
        }
    }
}
