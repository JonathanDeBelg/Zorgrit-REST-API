package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;

import java.util.ArrayList;
import java.util.List;

public interface IClientService {
    /**
     * @param ride An object of the Class Ride
     * @throws DataUnreachableException
     * @throws InvalidDateFormatException
     */
    void addRide(final RideDTO ride) throws DataUnreachableException, InvalidDateFormatException;

    /**
     * Cancels one ride
     *
     * @param rideId Represents the id of a ride
     * @throws DataUnreachableException
     */
    void cancelRide(final int rideId) throws DataUnreachableException;

    /**
     * Cancels one or more repeating rides
     *
     * @param repeatingRideId Represents one or more repeating rides
     * @throws DataUnreachableException
     * @throws InvalidDateFormatException
     */
    void cancelRepeatingRides(final int repeatingRideId) throws DataUnreachableException, InvalidDateFormatException;

    /**
     * Gets all cancelled rides for a specified user
     *
     * @param clientId An id that represents the user
     * @return A list of all rides for a specified user
     * @throws DataUnreachableException
     */
    ArrayList<RideDTO> getClientAllCancelledRides(final int clientId) throws DataUnreachableException;

    /**
     * Gets all completed rides for a specified user
     *
     * @param clientId An id that represents the user
     * @return A list of all rides for a specified user
     * @throws DataUnreachableException
     */
    ArrayList<RideDTO> getClientAllCompletedRides(final int clientId) throws DataUnreachableException;

    /**
     * Gets all rides for a specified user that
     *
     * @param clientId An id that represents the user
     * @return A list of all rides for a specified user
     * @throws DataUnreachableException
     */
    ArrayList<RideDTO> getAllFutureRides(final int clientId) throws DataUnreachableException;

    /**
     * Gets the profile of the client
     *
     * @param clientId An id that represents the user
     * @return An object of the client class
     * @throws DataUnreachableException
     */
    UserDTO getProfile(final int clientId) throws DataUnreachableException;

    /**
     * Updates the profile of the client
     *
     * @param clientId An id that represents the user
     * @param client   A client object with the user details
     * @throws DataUnreachableException
     */
    void updateProfile(final int clientId, final ClientDTO client) throws DataUnreachableException;

    /**
     * Get a list of all care institutions a client can get a ride from
     *
     * @param clientId An id that represents the user
     * @return An ArrayList of careInstitutions available for the specified user
     * @throws DataUnreachableException
     */
    ArrayList<CareInstitutionDTO> getAllCareInstitutions(final int clientId) throws DataUnreachableException;

    List<ClientDTO> getAllClientsCareInstitution(int careInstitution);

    ClientDTO getClient(int clientId);

    void editClient(int userId, ClientDTO client);

    void createClient(ClientDTO client);

    List<LimitationDTO> getAllLimitations();

    List<UtilityDTO> getClientUtilities(int userId);

    List<UtilityDTO> getAllUtilities();

    void deleteClient(int userId);
}
