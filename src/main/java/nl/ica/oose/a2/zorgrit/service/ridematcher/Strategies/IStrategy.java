package nl.ica.oose.a2.zorgrit.service.ridematcher.Strategies;

import nl.ica.oose.a2.zorgrit.dto.RideDTO;

public interface IStrategy {
    String getName();
    void execute(RideDTO ride);
}
