package nl.ica.oose.a2.zorgrit.service.ridematcher;

import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

public interface IRideMatcher {

    /**
     * Starts the matching process.
     *
     * @param ride
     * @throws DataUnreachableException
     */
    void startMatching(final RideDTO ride);

    /**
     * Update ride matching for all rides
     *
     * @throws DataUnreachableException
     */
    void updateMatching();

    /**
     * Reject a ride for a driver
     *
     * @param rideId   The ride to reject
     * @param driverId The driver who rejects the ride
     * @throws DataUnreachableException
     */
    void rejectRide(final int rideId, final int driverId) throws DataUnreachableException;
}
