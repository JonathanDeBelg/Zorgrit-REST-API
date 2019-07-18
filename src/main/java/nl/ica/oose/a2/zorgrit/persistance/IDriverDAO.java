package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.UserDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

import java.util.ArrayList;
import java.util.List;

public interface IDriverDAO {
    /**
     * Gets all rides for a specified driver
     *
     * @param driverId An id that represents the user
     * @return A list of all rides for a specified user
     * @throws DataUnreachableException
     */
    ArrayList<RideDTO> getAllFutureRides(final int driverId) throws DataUnreachableException;

    /**
     * Adds a driver to a ride
     *
     * @param rideId   An id that represents a specific ride
     * @param driverId An id that represents a specific driver
     * @throws DataUnreachableException
     */
    void acceptRide(final int driverId, final int rideId) throws DataUnreachableException;

    /**
     * Cancels a ride by a driver
     *
     * @param driverId Represents the id of a driver
     * @param rideId   Represents the id of a ride
     * @throws DataUnreachableException
     */
    void cancelRide(final int driverId, final int rideId) throws DataUnreachableException;

    /**
     * Gets all request rides for a specified driver
     *
     * @param driverId Represents the id of a driver
     * @return A list of all request rides for a specified user
     * @throws DataUnreachableException
     */
    ArrayList<RideDTO> getAllRequestRidesForDriver(final int driverId) throws DataUnreachableException;

    /**
     * Get the profile of a driver
     *
     * @param driverId Represents the id of a driver
     * @return the profile of an user
     * @throws DataUnreachableException
     */
    UserDTO getProfile(final int driverId) throws DataUnreachableException;

    /**
     * Update the profile of a driver
     *
     * @param driverId Represents the id of a driver
     * @param driver   An object that represents the user
     * @throws DataUnreachableException
     */
    void updateProfile(final int driverId, final DriverDTO driver) throws DataUnreachableException;

    /**
     * Gets all clients
     *
     * @param driverId An id that represents the driver
     * @return A list of clients
     * @throws DataUnreachableException
     */
    List<ClientDTO> getAllClientsForDriver(final int driverId) throws DataUnreachableException;

    /**
     * Get a list of all drivers institutions a client can get a ride from
     *
     * @param clientId An id that represents the user
     * @return A list with all drivers available for client
     * @throws DataUnreachableException
     */
    ArrayList<DriverDTO> getAllDriversForClient(final int clientId) throws DataUnreachableException;

    /**
     * Get a list of all completed rides for a driver
     *
     * @param driverId An id representing the driver
     * @return A list of completed rides
     * @throws DataUnreachableException
     */
    ArrayList<RideDTO> getAllCompletedRides(final int driverId) throws DataUnreachableException;

    /**
     * Get a list of all cancelled rides for a driver
     *
     * @param driverId
     * @return A list of cancelled rides
     * @throws DataUnreachableException
     */
    ArrayList<RideDTO> getAllCancelledRides(final int driverId) throws DataUnreachableException;

    List<DriverDTO> getAllDrivers();

    DriverDTO getDriver(int driverId);

    List<DriverDTO> getAllDriversTownship(String township);

    List<DriverDTO> getAllDriversCareInstitution(int careInstitution);

    void createDriver(DriverDTO driverDTO);

    void editDriver(int driverId, DriverDTO driver);

    void deleteDriver(int driverId);

    List<RideDTO> checkIfDriverHasRides(int driverId);

    List<DriverDTO> getPreferredDriversToMatch(int clientId);
}
