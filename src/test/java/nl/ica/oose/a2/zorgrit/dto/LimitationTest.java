package nl.ica.oose.a2.zorgrit.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LimitationTest {

    @Test
    public void testGetterSetterName() {
        //Setup
        LimitationDTO limitation = new LimitationDTO();
        String limitationName = "test";

        //Test
        limitation.setName(limitationName);

        //Verify
        assertEquals(limitationName, limitation.getName());
    }
}
