package nl.ica.oose.a2.zorgrit.dto;

import java.util.ArrayList;

public class RidesDTO {
    private ArrayList<RideDTO> rides;

    public RidesDTO(ArrayList<RideDTO> rides) {
        this.rides = rides;
    }

    public RidesDTO() {
        this.rides = new ArrayList<>();
    }

    public ArrayList<RideDTO> getRides() {
        return rides;
    }

    public void setRides(ArrayList<RideDTO> rides) {
        this.rides = rides;
    }

    public void addRide(RideDTO rideDTO) {
        rides.add(rideDTO);
    }
}
