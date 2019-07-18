package nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache;


import java.util.*;

public class RideMatchCache {
    private static RideMatchCache instance;
    private LinkedList<RideMatchState> rideMatchStates;

    private RideMatchCache() {
        rideMatchStates = new LinkedList<>();
    }

    public static RideMatchCache Instance() {
        if (instance == null) { instance = new RideMatchCache(); }
        return instance;
    }

    public LinkedList<RideMatchState> getRideMatchStates() {
        return this.rideMatchStates;
    }

    public void startMatching(RideMatchState state) {
        this.rideMatchStates.add(state);
    }

    public void stopMatching(int rideId) {
        //Remove proposed drivers.
        RideMatchProposedDriverCache.Instance().finishMatching(rideId);

        RideMatchState toRemove = null;
        for (RideMatchState state: rideMatchStates) {
            if (state.getRide().getId() == 1) {
                toRemove = state;
                break;
            }
        }

        if (toRemove != null) {
            rideMatchStates.remove(toRemove);
        }
    }
}
