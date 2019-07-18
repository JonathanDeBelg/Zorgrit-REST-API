package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;
import nl.ica.oose.a2.zorgrit.exceptions.NoRecordsFoundRuntimeException;
import nl.ica.oose.a2.zorgrit.persistance.IClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.IRideDAO;
import nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache.RideMatchCache;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RideService implements IRideService {
    private static final String NO_RECORDS_WHERE_FOUND = "No records where found.";

    @Inject
    private IRideDAO rideDAO;

    @Inject
    private IClientDAO clientDAO;

    public RideDTO getRide(final int rideId) throws DataUnreachableException {
        return rideDAO.getRide(rideId);
    }

    @Override
    public RideDTO getCurrentRide(final int clientId) throws DataUnreachableException {
        return rideDAO.getCurrentRide(clientId);
    }

    @Override
    public void startRide(int rideId) throws DataUnreachableException {
        rideDAO.startRide(rideId);
    }

    public void updateRide(RideDTO ride) throws DataUnreachableException, InvalidDateFormatException {
        rideDAO.updateRide(ride);
        if (ride.isExecuted()) {
            RideMatchCache.Instance().stopMatching(ride.getId());

            if (ride.getRepeatingRideId() > 0) {
                ride.setPickUpDateTime(addWeekToDateString(ride.getPickUpDateTime()));
                clientDAO.addRide(ride);
            }
        }
    }

    public RideRating getRideRating(final int rideId) throws DataUnreachableException {
        return rideDAO.getRideRating(rideId);
    }

    public void addRideRating(final int rideId, final RideRating rating) throws DataUnreachableException {
        rideDAO.addRideRating(rideId, rating);
    }

    public ArrayList<RideRating> getAllRatingsByDriver(final int driverId) throws DataUnreachableException {
        return rideDAO.getAllRatingsByDriver(driverId);
    }

    public void putRideRating(final int rideId, final RideRating rating) throws DataUnreachableException {
        rideDAO.putRideRating(rideId, rating);
    }

    public void deleteRideRating(final int rideId) throws DataUnreachableException {
        rideDAO.deleteRideRating(rideId);
    }

    private String addWeekToDateString(String dateTime) {
        Timestamp ts = Timestamp.valueOf(dateTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.DAY_OF_WEEK, 7);
        ts.setTime(cal.getTime().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(ts);
    }

    @Override
    public RidesDTO getAllRides() {
        RidesDTO ridesDTO = rideDAO.getAllRides();
        if (!ridesDTO.getRides().isEmpty()) {
            return ridesDTO;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public RidesDTO getAllRidesTownship(String township) {
        RidesDTO ridesDTO = rideDAO.getAllRidesTownship(township);
        if (!ridesDTO.getRides().isEmpty()) {
            return ridesDTO;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public RidesDTO getAllRidesCareInstitution(int careInstitutionId) {
        RidesDTO ridesDTO = rideDAO.getAllRidesCareInstitution(careInstitutionId);
        if (!ridesDTO.getRides().isEmpty()) {
            return ridesDTO;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public RidesInformationDTO getAllNotMatchedRides(int careInstitutionId) {
        RidesInformationDTO ridesInformationDTO = rideDAO.getAllNotMatchedRides(careInstitutionId);
        if (!ridesInformationDTO.getRidesInformation().isEmpty()) {
            return ridesInformationDTO;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public RidesInformationDTO getAllMatchedRides(int careInstitutionId) {
        RidesInformationDTO ridesInformationDTO = rideDAO.getAllMatchedRides(careInstitutionId);
        if (!ridesInformationDTO.getRidesInformation().isEmpty()) {
            return ridesInformationDTO;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public RidesInformationDTO getAllRidesInfo(int careInstitutionId) {
        RidesInformationDTO ridesInformationDTO = rideDAO.getAllRidesInfo(careInstitutionId);
        if (!ridesInformationDTO.getRidesInformation().isEmpty()) {
            return ridesInformationDTO;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public RideInformationDTO getRideToMatch(int rideId){
        RideInformationDTO rideDTO = rideDAO.getRideToMatch(rideId);
        if (rideDTO.getId() != 0) {
            return rideDTO;
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }

    @Override
    public void matchRideToDriver(int rideId, int driverId) {
        RideDTO matchedRide = rideDAO.getRideToMatch(rideId);
        if (matchedRide.getId() != 0) {
            rideDAO.matchRideToDriver(rideId, driverId);
        } else {
            throw new NoRecordsFoundRuntimeException(NO_RECORDS_WHERE_FOUND);
        }
    }
}
