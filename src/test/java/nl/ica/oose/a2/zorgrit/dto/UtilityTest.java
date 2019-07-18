package nl.ica.oose.a2.zorgrit.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilityTest {

    @Test
    public void testGetterSetterName() {
        //Setup
        UtilityDTO utility = new UtilityDTO();
        String utilityName = "test";

        //Test
        utility.setName(utilityName);

        //Verify
        assertEquals(utilityName, utility.getName());
    }

    @Test
    public void testGetterSetterUtility() {
        //Setup
        UtilityDTO utility = new UtilityDTO();
        String utilityName = "test";

        //Test
        utility.setName(utilityName);

        //Verify
        assertEquals(utilityName, utility.getName());
    }

    @Test
    public void testGetterSetterRating() {
        //Setup
        UtilityDTO utility = new UtilityDTO();
        int rating = 1;

        //Test
        utility.setRating(rating);

        //Verify
        assertEquals(rating, utility.getRating());
    }
}
