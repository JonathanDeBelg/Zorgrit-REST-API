package nl.ica.oose.a2.zorgrit.dto;

public class RideInformationDTO extends RideDTO {
    DriverDTO driverDTO;
    ClientDTO clientDTO;

    public RideInformationDTO() {
        driverDTO = new DriverDTO();
        clientDTO = new ClientDTO();
    }

    public DriverDTO getDriverDTO() {
        return driverDTO;
    }

    public void setDriverDTO(DriverDTO driverDTO) {
        this.driverDTO = driverDTO;
    }

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
    }
}
