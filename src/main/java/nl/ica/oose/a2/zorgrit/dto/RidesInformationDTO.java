package nl.ica.oose.a2.zorgrit.dto;

import java.util.ArrayList;
import java.util.List;

public class RidesInformationDTO {
    private List<RideInformationDTO> ridesInformation;

    public RidesInformationDTO() {
        ridesInformation = new ArrayList<>();
    }

    public List<RideInformationDTO> getRidesInformation() {
        return ridesInformation;
    }

    public void setRidesInformation(List<RideInformationDTO> ridesInformation) {
        this.ridesInformation = ridesInformation;
    }

    public void addRide(RideInformationDTO rideInformationDTO) {
        ridesInformation.add(rideInformationDTO);
    }
}
