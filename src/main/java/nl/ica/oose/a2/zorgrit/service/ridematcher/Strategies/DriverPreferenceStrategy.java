package nl.ica.oose.a2.zorgrit.service.ridematcher.Strategies;

import nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache.RideMatchProposedDriverCache;
import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.IClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.IDriverDAO;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class DriverPreferenceStrategy implements IStrategy {

    @Inject
    private IClientDAO clientDAO;

    @Inject
    private IDriverDAO driverDAO;

    @Override
    public String getName() {
        return "DriverPreference";
    }

    @Override
    public void execute(RideDTO ride) {
        //Get all preferred driver from the client
        try {
            ClientDTO client = clientDAO.getProfile(ride.getClientId());
            List<DriverDTO> drivers = client.getPreferredDrivers();

            //Filter the driverDTOS
            drivers = filterDrivers(drivers, client, ride);

            //Propose driverDTOS
            RideMatchProposedDriverCache.Instance().proposeDrivers(ride.getId(), drivers);

        } catch (DataUnreachableException e) {
            LoggerFactory.getLogger(ClientDriverPreferenceStrategy.class).error("DriverPreferenceStrategy: " + e);
        }
    }

    private ArrayList<DriverDTO> filterDrivers(List<DriverDTO> driverDTOS, ClientDTO client, RideDTO ride) {
        ArrayList<DriverDTO> filteredDrivers = new ArrayList<>(driverDTOS);

        //Loop over all driverDTOS and remove them when they do not meet requirements.
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
}
