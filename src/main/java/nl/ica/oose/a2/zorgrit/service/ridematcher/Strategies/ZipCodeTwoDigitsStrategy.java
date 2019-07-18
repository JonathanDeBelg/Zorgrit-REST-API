package nl.ica.oose.a2.zorgrit.service.ridematcher.Strategies;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.UserPreference;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.DriverDAO;
import nl.ica.oose.a2.zorgrit.persistance.IClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.IDriverDAO;
import nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache.RideMatchProposedDriverCache;
import nl.ica.oose.a2.zorgrit.service.ridematcher.util.IExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ZipCodeTwoDigitsStrategy implements IStrategy {

    public static String ZipCodeTwoDigitsStrategyKey = "ZipCodeTwoDigitsStrategy";
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverDAO.class);

    @Inject
    IDriverDAO driverDAO;

    @Inject
    IClientDAO clientDAO;

    @Inject
    IExtractor extractor;

    @Override
    public String getName() {
        return "ZipCodeTwoDigits";
    }

    @Override
    public void execute(RideDTO ride) {
        try {
            ClientDTO client = clientDAO.getProfile(ride.getClientId());
            if(!checkPreference(client)) {
                return;
            }

            ArrayList<DriverDTO> driverDTOS = driverDAO.getAllDriversForClient(ride.getClientId());
            ArrayList<DriverDTO> filteredDrivers = filterDrivers(driverDTOS, client, ride);
            ArrayList<DriverDTO> toNotify = new ArrayList<>();
            int zipCodeRide = extractor.getFirstNDigits(extractor.zipCodeDigitsFromAddress(ride.getPickUpLocation()), 2);

            for (DriverDTO d: filteredDrivers) {
                int zipCodeDriver = extractor.getFirstNDigits(extractor.zipCodeDigits(d.getZipCode()), 2);

                if (zipCodeRide == zipCodeDriver) {
                    toNotify.add(d);
                }
            }

            RideMatchProposedDriverCache.Instance().proposeDrivers(ride.getId(), toNotify);
        }
        catch (DataUnreachableException e) {
            LOGGER.error("Database error: ", e);
        }
    }

    private ArrayList<DriverDTO> filterDrivers(ArrayList<DriverDTO> driverDTOS, ClientDTO client, RideDTO ride) {
        ArrayList<DriverDTO> filteredDrivers = new ArrayList<>(driverDTOS);

        /**
         * Loop over all driverDTOS and remove them when they do not meet requirements.
         */
        for(DriverDTO driverDTO : driverDTOS) {
            //Check if the driverDTO can move the utility
            if (!Filters.checkUtility(driverDTO, client)) {
                filteredDrivers.remove(driverDTO);
                break;
            }

            //Check if the driverDTO has enough space for the companion in the car.
            if (!Filters.checkIfRoomForCompanion(driverDTO, client)) {
                filteredDrivers.remove(driverDTO);
                break;
            }

            //Check if the driverDTO had enough space for the passengers in the car
            if (!Filters.checkIfRoomForPassengers(driverDTO, ride)) {
                filteredDrivers.remove(driverDTO);
                break;
            }

            //Check if the driverDTO is available
            if (!Filters.checkIfDriverIsAvailable(driverDTO, ride, driverDAO)) {
                filteredDrivers.remove(driverDTO);
                break;
            }
        }

        return filteredDrivers;
    }

    /**
     * Check preference of client and return value. If the preference is not found return true.
     *
     * @param c client
     * @return true or preference value.
     */
    private boolean checkPreference(ClientDTO c) {
        List<UserPreference> preferences = c.getPreferences();
        for (UserPreference pref : preferences) {
            if (pref.getPreferenceKey().equals(ZipCodeTwoDigitsStrategyKey)) {
                return Integer.parseInt(pref.getPreferenceValue()) == 1;
            }
        }

        return true;
    }
}
