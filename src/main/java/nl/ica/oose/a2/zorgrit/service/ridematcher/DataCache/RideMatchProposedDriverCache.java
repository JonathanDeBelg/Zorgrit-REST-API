package nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache;

import nl.ica.oose.a2.zorgrit.dto.DriverDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RideMatchProposedDriverCache {
    private static RideMatchProposedDriverCache instance;
    //              RideId              DriverId
    private HashMap<Integer, LinkedList<Integer>> proposedDrivers;

    private RideMatchProposedDriverCache() {
        proposedDrivers = new HashMap<>();
    }

    public static RideMatchProposedDriverCache Instance() {
        if (instance == null) {
            instance = new RideMatchProposedDriverCache();
        }

        return instance;
    }

    /**
     * Add a list of driver ids to the proposedDrivers.
     *
     * @param rideId
     * @param driverDTOS
     */
    public void proposeDrivers(int rideId, List<DriverDTO> driverDTOS) {
        LinkedList<Integer> list;

        if (proposedDrivers.containsKey(rideId)) {
            list = proposedDrivers.get(rideId);
        }
        else {
            list = new LinkedList<Integer>();
        }

        for (DriverDTO d: driverDTOS) {
            list.add(d.getId());
        }

        proposedDrivers.put(rideId, list);
    }

    /**
     * Add one driverDTO to the proposedDrivers.
     *
     * @param rideId
     * @param driverDTO
     */
    public void proposeDriver(int rideId, DriverDTO driverDTO) {
        if (proposedDrivers.containsKey(rideId)) {
            proposedDrivers.get(rideId).add(driverDTO.getId());
        }
        else {
            LinkedList<Integer> newList = new LinkedList<>();
            newList.add(driverDTO.getId());
            proposedDrivers.put(rideId, newList);
        }
    }

    public List<Integer> getRidesProposedForDriver(int driverId) {
        ArrayList<Integer> rideIds = new ArrayList<>();
        proposedDrivers.forEach((k, v) -> {
            if (v.contains(driverId)) {
                rideIds.add(k);
            }
        });

        return rideIds;
    }

    /**
     * Remove driverDTO from the proposed drivers.
     *
     * @param rideId
     * @param driverDTO
     */
    public void rejectRide(int rideId, DriverDTO driverDTO) {
        this.rejectRide(rideId, driverDTO.getId());
    }

    public void rejectRide(int rideId, int driverId) {
        if (proposedDrivers.containsKey(rideId)) {
            proposedDrivers.get(rideId).remove(driverId);
        }
    }

    /**
     * Remove entry when matching is done
     *
     * @param rideId
     */
    public void finishMatching(int rideId){
        if (proposedDrivers.containsKey(rideId)) {
            proposedDrivers.remove(rideId);
        }
    }

    public int numberOfMatches(int rideId) {
        if (proposedDrivers.containsKey(rideId)) {
            return proposedDrivers.get(rideId).size();
        }
        return 0;
    }
}
