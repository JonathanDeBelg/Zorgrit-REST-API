package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

import java.util.ArrayList;
import java.util.List;

public interface IDriverService {
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
     * @param driverId An id that represents the driver
     * @return A list of all requested rides for a specified user
     * @throws DataUnreachableException
     */
    ArrayList<RideDTO> getAllRequestRides(final int driverId) throws DataUnreachableException;

    /**
     * Updates the driver profile
     *
     * @param driverId An id that represents the driver
     * @param driver   A driver object with the user details
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
     * @return An ArrayList of drivers
     * @throws DataUnreachableException
     */
    ArrayList<DriverDTO> getAllDriversForClient(final int clientId) throws DataUnreachableException;

    /**
     * Get a list of all completed rides for a driver
     *
     * @param driverId An id representing the driver
     * @return A list of completed rides
     */
    ArrayList<RideDTO> getAllCompletedRides(final int driverId) throws DataUnreachableException;

    /**
     * Get a list of all cancelled rides for a driver
     *
     * @param driverId An id representing the driver
     * @return A list of cancelled rides
     */
    ArrayList<RideDTO> getAllCancelledRides(final int driverId) throws DataUnreachableException;

    List<DriverDTO> getAllDrivers();

    DriverDTO getDriver(int driverId);

    List<DriverDTO> getAllDriversTownship(String township);

    List<DriverDTO> getAllDriversCareInstitution(int careInstitution);

    void createDriver(DriverDTO driverDTO);

    void editDriver(int driverId, DriverDTO driverDTO);

    void deleteDriver(int driverId);

    List<DriverDTO> getPreferredDriversToMatch(int clientId);
}
