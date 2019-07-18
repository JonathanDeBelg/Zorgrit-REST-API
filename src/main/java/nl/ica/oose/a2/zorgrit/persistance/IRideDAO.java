package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

import java.util.ArrayList;

/**
 * RideDTO DAO
 *
 * gets, adds, sets and deletes ride data
 */
public interface IRideDAO {
    /**
     * @param rideId An id that represents a specific ride
     * @return All ride data
     * @throws DataUnreachableException
     * @throws IllegalArgumentException
     */
    RideDTO getRide(int rideId) throws DataUnreachableException, IllegalArgumentException;

    /**
     * @param clientId Id of the client
     * @return RideDTO with ride rating.
     * @throws DataUnreachableException
     * @throws IllegalArgumentException
     */
    RideDTO getCurrentRide(int clientId) throws DataUnreachableException;

    /**
     *
     * @param rideId
     * @throws DataUnreachableException
     */
    void startRide (int rideId) throws DataUnreachableException;

    /**
     * Updates the RideDTO
     *
     * @param ride RideDTO object
     * @throws DataUnreachableException
     */
    void updateRide(RideDTO ride) throws DataUnreachableException;

    /**
     *
     * @param rideId
     * @return
     * @throws DataUnreachableException
     */
    RideRating getRideRating(int rideId) throws DataUnreachableException;

    /**
     *
     * @param rideId
     * @param rating
     * @throws DataUnreachableException
     */
    void addRideRating(int rideId, RideRating rating) throws DataUnreachableException;

    /**
     *
     * @param driverId
     * @return
     * @throws DataUnreachableException
     */
    ArrayList<RideRating> getAllRatingsByDriver(int driverId) throws DataUnreachableException;

    /**
     *
     * @param rideId
     * @param rating
     * @throws DataUnreachableException
     */
    void putRideRating(int rideId, RideRating rating) throws DataUnreachableException;

    /**
     *
     * @param rideId
     * @throws DataUnreachableException
     */
    void deleteRideRating(int rideId) throws DataUnreachableException;

    RidesDTO getAllRides();

    RidesDTO getAllRidesTownship(String township);

    RidesDTO getAllRidesCareInstitution(int careInstitutionId);

    boolean checkIfClientHasUnPayedRides(int userId);

    RidesInformationDTO getAllNotMatchedRides(int careInstitutionId);

    RidesInformationDTO getAllMatchedRides(int careInstitutionId);

    RidesInformationDTO getAllRidesInfo(int careInstitutionId);

    RideInformationDTO getRideToMatch(int rideId);

    void matchRideToDriver(int rideId, int driverId);
}
