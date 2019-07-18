package nl.ica.oose.a2.zorgrit.service.ridematcher.Strategies;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.IDriverDAO;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class Filters {

    public Filters() {
    }

    public static boolean checkIfRoomForCompanion(DriverDTO driverDTO, ClientDTO client) {
        if (client.getCompanion() == null) {
            return driverDTO.getNumberOfPassengers() >= 1;
        } else {
            return driverDTO.getNumberOfPassengers() >= 2;
        }
    }

    public static boolean checkIfRoomForPassengers(DriverDTO driverDTO, RideDTO ride) {
        return driverDTO.getNumberOfPassengers() >= ride.getNumberOfCompanions();
    }

    public static boolean checkIfDriverPrefersClient(DriverDTO driverDTO, ClientDTO client) {
        for (ClientDTO c : driverDTO.getPreferredClients()) {
            if (c.getId() == client.getId()) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkIfDriverIsAvailable(DriverDTO driverDTO, RideDTO ride, IDriverDAO driverDAO) {
        List<RideDTO> futureRides;
        try {
            futureRides = driverDAO.getAllFutureRides(driverDTO.getId());
        } catch (DataUnreachableException e) {
            LoggerFactory.getLogger(ClientDriverPreferenceStrategy.class).error("DriverPreferenceStrategy: " + e);
            return false;
        }

        //Get start and end time of ride.
        Timestamp start = Timestamp.valueOf(ride.getPickUpDateTime());
        Timestamp end = addTime(ride.getDuration(), start);

        for (RideDTO r : futureRides) {
            Timestamp rideStart = Timestamp.valueOf(r.getPickUpDateTime());
            Timestamp rideEnd = addTime(ride.getDuration(), start);
            if (start.after(rideEnd) || end.before(rideStart)) {
                //Return false if driverDTO has another ride.
                return false;
            }
        }

        //If no overlapping rides are found, return true.
        return true;
    }

    public static boolean checkUtility(DriverDTO driverDTO, ClientDTO client) {
        if (driverDTO.getUtility() == null || driverDTO.getUtility().getRating() == 0 || client.getUtility() == null || client.getUtility().getRating() == 0) {
            return false;
        }
        return client.getUtility().getRating() <= driverDTO.getUtility().getRating();
    }

    private static Timestamp addTime(final int minutes, Timestamp original) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(original);
        cal.add(Calendar.MINUTE, minutes);
        return new Timestamp(cal.getTime().getTime());
    }
}
