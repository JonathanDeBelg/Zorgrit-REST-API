package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;

import java.util.ArrayList;

/**
 * RideDTO Service
 *
 * Gets and sets ride data
 */
public interface IRideService {
    /**
     * @param rideId An id that represents a specific ride
     * @return All ride data
     * @throws DataUnreachableException
     */
    RideDTO getRide(final int rideId) throws DataUnreachableException;

    /**
     * @param clientId
     * @return
     * @throws DataUnreachableException
     */
    RideDTO getCurrentRide(final int clientId) throws DataUnreachableException;

    /**
     *
     * @param rideId
     * @throws DataUnreachableException
     */
    void startRide(final int rideId) throws DataUnreachableException;

    /**
     * Updates the ride
     *
     * @param ride a RideDTO object
     * @throws InvalidDateFormatException
     * @throws DataUnreachableException
     */
    void updateRide(RideDTO ride) throws DataUnreachableException, InvalidDateFormatException;

    /**
     *
     * @param rideId
     * @return All ride rating data
     * @throws DataUnreachableException
     */
    RideRating getRideRating(final int rideId) throws DataUnreachableException;

    /**
     *
     * @param rideId id of the ride
     * @param rating a RideRating object
     * @throws DataUnreachableException
     */
    void addRideRating(final int rideId, final RideRating rating) throws DataUnreachableException;

    /**
     *
     * @param driverId
     * @return list of all RideRatings.
     * @throws DataUnreachableException
     */
    ArrayList<RideRating> getAllRatingsByDriver(final int driverId) throws DataUnreachableException;

    /**
     *
     * @param rideId
     * @param rating
     * @throws DataUnreachableException
     */
    void putRideRating(final int rideId, final RideRating rating) throws DataUnreachableException;

    /**
     *
     * @param rideId
     * @throws DataUnreachableException
     */
    void deleteRideRating(final int rideId) throws DataUnreachableException;

    RidesDTO getAllRides();

    RidesDTO getAllRidesTownship(String township);

    RidesDTO getAllRidesCareInstitution(int careInstitutionId);

    RidesInformationDTO getAllNotMatchedRides(int careInstitutionId);

    RidesInformationDTO getAllMatchedRides(int careInstitutionId);

    RidesInformationDTO getAllRidesInfo(int careInstitutionId);

    void matchRideToDriver(int rideId, int driverId);

    RideInformationDTO getRideToMatch(int rideId);
}

