package nl.ica.oose.a2.zorgrit.service.ridematcher;

import nl.ica.oose.a2.zorgrit.service.notification.INotificationService;
import nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache.RideMatchCache;
import nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache.RideMatchProposedDriverCache;
import nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache.RideMatchState;
import nl.ica.oose.a2.zorgrit.service.ridematcher.Strategies.DriverPreferenceStrategy;
import nl.ica.oose.a2.zorgrit.service.ridematcher.Strategies.IStrategy;
import nl.ica.oose.a2.zorgrit.service.ridematcher.Strategies.ZipCodeTwoDigitsStrategy;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.persistance.IClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.IRideDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class RideMatcher implements IRideMatcher {

    @Inject
    private IClientDAO clientDAO;

    @Inject
    private IRideDAO rideDAO;

    @Inject
    private INotificationService notificationService;

    @Inject
    private DriverPreferenceStrategy driverPreferenceStrategy;

    @Inject
    private ZipCodeTwoDigitsStrategy zipCodeTwoDigitsStrategy;

    private static final Logger LOGGER = LoggerFactory.getLogger(RideMatcher.class);

    @Override
    public void startMatching(final RideDTO ride) {
        RideMatchState newState = new RideMatchState(
            ride, driverPreferenceStrategy, calculateMaxCounter(ride, driverPreferenceStrategy)
        );

        RideMatchCache.Instance().startMatching(newState);

        newState.Execute(ride);
    }

    @Override
    public void updateMatching() {
        for(RideMatchState state : RideMatchCache.Instance().getRideMatchStates()) {
            state.setCounter(state.getCounter() + 1);

            //Next state if time has elapsed of if the number of matches is to low.
            if (state.getCounter() >= state.getMaxCounter() || RideMatchProposedDriverCache.Instance().numberOfMatches(state.getRide().getId()) < 1) {
                state.setCounter(0);
                state.setStrategy(nextStrategy(state.getStrategy()));
                state.setMaxCounter(calculateMaxCounter(state.getRide(), state.getStrategy()));

                state.Execute(state.getRide());
            }
        }
    }

    private int calculateMaxCounter(RideDTO ride, IStrategy strategy) {

        switch (strategy.getName()) {
            case "ClientDriverPreference":
                return 60;

            case "DriverPreference":
                return 120;

            case "CareInstitutionDTO":
                return 180;

            case "ZipCodeFourDigits":
                return 240;

             case "ZipCodeTwoDigits":
                 return Integer.MAX_VALUE;

            default:
                return 60;
        }
    }

    private IStrategy nextStrategy(IStrategy currentStrategy) {
        switch (currentStrategy.getName()) {
            case "ClientDriverPreference":
                return driverPreferenceStrategy;

            case "DriverPreference":
                return zipCodeTwoDigitsStrategy;

            default:
                return zipCodeTwoDigitsStrategy;
        }
    }

    public void rejectRide(final int rideId, final int driverId) {
        RideMatchProposedDriverCache.Instance().rejectRide(rideId, driverId);
    }
}
