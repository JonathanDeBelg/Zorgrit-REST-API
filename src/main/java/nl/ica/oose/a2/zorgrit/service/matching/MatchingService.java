package nl.ica.oose.a2.zorgrit.service.matching;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.IDriverDAO;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MatchingService implements IMatchingService {

    @Inject
    private IDriverDAO driverDAO;

    public List<DriverDTO> requirementsCheck(final List<DriverDTO> availableDrivers, final ClientDTO client, final RideDTO ride) throws DataUnreachableException {
        List<DriverDTO> drivers = new ArrayList<>();

        for (DriverDTO driverDTO : availableDrivers) {
            if (utilityCheck(driverDTO, client)
                    && (driverPrefersClientCheck(driverDTO, client))
                    && (isAvailable(driverDTO.getId(), ride))
                    && (roomForCompanionCheck(driverDTO, client))
                    && (roomForAditionalPassengersCheck(driverDTO, ride))) {
                drivers.add(driverDTO);
            }
        }
        return drivers;
    }

    private boolean utilityCheck(final DriverDTO driver, final ClientDTO client) {
        return client.getUtility().getRating() <= driver.getUtility().getRating();
    }

    private boolean driverPrefersClientCheck(final DriverDTO driver, final ClientDTO client) {
        if (driver.getPreferredClients() != null) {
            List<ClientDTO> preferredClients = driver.getPreferredClients();
            for (ClientDTO preferredClient : preferredClients) {
                if (client.getId() == preferredClient.getId()) {
                    return true;
                }
            }
            return false;
        }
        // NO PREFERENCE SO GO TROUGH WITH IT
        return true;
    }

    private boolean roomForCompanionCheck(final DriverDTO driverDTO, final ClientDTO client) {
        if (client.getCompanion() == null) {
            return driverDTO.getNumberOfPassengers() >= (1);
        } else {
            return driverDTO.getNumberOfPassengers() >= (2);
        }
    }

    private boolean roomForAditionalPassengersCheck(final DriverDTO driver, final RideDTO ride) {
        return driver.getNumberOfPassengers() >= ride.getNumberOfCompanions();
    }

    private boolean isAvailable(final int driverId, final RideDTO clientRide) throws DataUnreachableException {
        List<RideDTO> driverRides = driverDAO.getAllFutureRides(driverId);
        Timestamp clientStart = Timestamp.valueOf(clientRide.getPickUpDateTime());
        Timestamp clientFinish = addTime(clientRide.getDuration(), clientStart);

        for (RideDTO r : driverRides) {
            Timestamp driverStart = Timestamp.valueOf(r.getPickUpDateTime());
            Timestamp driverFinish = addTime(r.getDuration(), driverStart);

            return checkDriverTimeAvailable(driverStart, driverFinish, clientStart, clientFinish);
        }
        return true;
    }

    private boolean checkDriverTimeAvailable(Timestamp driverStart, Timestamp driverFinish, Timestamp clientStart, Timestamp clientFinish) {
        return !
                // DriverRide starts in clientRide
                (driverStart.after(clientStart) && driverStart.before(clientFinish)) ||
                // DriverRide ends in clientRide
                (driverFinish.after(clientStart) && driverFinish.before(clientFinish)) ||
                // DriverRide starts before and end after clientRide
                (driverStart.before(clientStart) && driverFinish.after(clientFinish)) ||
                // DriverRide starts on clientRideStart or clientRideEnd
                driverStart.equals(clientStart) || driverStart.equals(clientFinish) ||
                // DriverRide ends on clientRideStart or clientRideEnd
                driverFinish.equals(clientStart) || driverFinish.equals(clientFinish);
    }

    private Timestamp addTime(final int minutes, final Timestamp timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        cal.add(Calendar.MINUTE, minutes);
        return new Timestamp(cal.getTime().getTime());
    }
}
