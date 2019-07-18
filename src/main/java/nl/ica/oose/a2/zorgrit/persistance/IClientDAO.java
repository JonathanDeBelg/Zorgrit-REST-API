package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;

import java.util.ArrayList;
import java.util.List;

public interface IClientDAO {
    /**
     * Adds a ride.
     *
     * @param ride A object of the Class Ride
     * @throws DataUnreachableException
     * @throws InvalidDateFormatException
     */
    int addRide(final RideDTO ride) throws DataUnreachableException, InvalidDateFormatException;

    /**
     * Cancels one ride
     *
     * @param rideId Represents the id of a ride
     * @throws DataUnreachableException
     */
    void cancelRide(final int rideId) throws DataUnreachableException;

    /**
     * Gets all rides for a specified user
     *
     * @param clientId A id that represents the user
     * @return A list of all rides for a specified user
     * @throws DataUnreachableException
     */
    ArrayList<RideDTO> getAllFutureRides(final int clientId) throws DataUnreachableException;

    /**
     * Get a list of all care institutions a client can get a ride from
     *
     * @param clientId An id that represents the user
     * @return A list of all careInstitutions a client can get a ride from
     * @throws DataUnreachableException
     */
    ArrayList<CareInstitutionDTO> getAllCareInstitutions(final int clientId) throws DataUnreachableException;

    /**
     * A list of preferred drivers for a client
     *
     * @param clientId An id that represents the client
     * @return A list with all drivers available for the client
     * @throws DataUnreachableException
     */
    ArrayList<DriverDTO> getPreferredDrivers(final int clientId) throws DataUnreachableException;

    /**
     * Get a list of all rides that are cancelled by user
     *
     * @param clientId An id that represents the user
     * @return A list with all rides that are cancelled by user
     * @throws DataUnreachableException
     */
    ArrayList<RideDTO> getAllCompletedRides(final int clientId) throws DataUnreachableException;

    /**
     * Get a list of all rides that are cancelled by user
     *
     * @param clientId An id that represents the user
     * @return A list with all rides that are cancelled by user
     * @throws DataUnreachableException
     */
    ArrayList<RideDTO> getAllCancelledRides(final int clientId) throws DataUnreachableException;

    /**
     * Cancels one or more repeating rides
     *
     * @param repeatRideId Represents one or more repeating rides
     * @throws DataUnreachableException
     * @throws InvalidDateFormatException
     */
    void cancelRepeatingRides(final int repeatRideId) throws DataUnreachableException, InvalidDateFormatException;

    /**
     * Gets the profile of the client
     *
     * @param clientId An id that represents the user
     * @return the profile of a specified user
     * @throws DataUnreachableException
     */
    ClientDTO getProfile(final int clientId) throws DataUnreachableException;

    /**
     * Update the profile of the client
     *
     * @param clientId An id that represents the user
     * @param client   An object that represents the user
     * @throws DataUnreachableException
     */
    void updateProfile(final int clientId, final ClientDTO client) throws DataUnreachableException;

    List<ClientDTO> getAllClientsCareInstitution(int careInstitution);

    ClientDTO getClient(int clientId);

    void editClient(int userId, ClientDTO client);

    List<LimitationDTO> getLimitations();

    List<UtilityDTO> getUtilities();

    void addClient(ClientDTO client);

    List<UtilityDTO> getClientUtilities(int userId);

    void deleteAllTablesForClient(int clientId);

    List<RideDTO> checkIfClientHasRides(int clientId);



}
