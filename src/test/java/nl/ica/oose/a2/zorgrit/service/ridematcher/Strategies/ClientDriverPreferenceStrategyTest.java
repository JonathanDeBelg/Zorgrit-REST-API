package nl.ica.oose.a2.zorgrit.service.ridematcher.Strategies;

import nl.ica.oose.a2.zorgrit.persistance.ClientDAO;
import nl.ica.oose.a2.zorgrit.persistance.DriverDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientDriverPreferenceStrategyTest {

    @Mock
    ClientDAO clientDAO;

    @Mock
    DriverDAO driverDAO;

    @InjectMocks
    private ClientDriverPreferenceStrategy sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void execute(){
//        RideDTO rideDTO = new RideDTO();
//
//        sut.
//    }

    @Test
    public void getNameReturnName() {
        String actualResult = sut.getName();

        assertEquals(actualResult, "ClientDriverPreference");
    }

}
