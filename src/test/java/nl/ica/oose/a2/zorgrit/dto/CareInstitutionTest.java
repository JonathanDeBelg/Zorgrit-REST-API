package nl.ica.oose.a2.zorgrit.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class CareInstitutionTest {

    @Test
    public void ShouldPassAllPojoTests() {
        // Setup
        final Class<?> classUnderTest = CareInstitutionDTO.class;

        // Verify
        assertPojoMethodsFor(classUnderTest).quickly();
    }

    @Test
    public void testGetterSetterId() {
        //Setup
        CareInstitutionDTO careInstitution = new CareInstitutionDTO();
        int id = 1;

        //Test
        careInstitution.setId(id);

        //Verify
        assertEquals(id, careInstitution.getId());
    }

    @Test
    public void testGetterSetterName() {
        //Setup
        CareInstitutionDTO careInstitution = new CareInstitutionDTO();
        String name = "Reinearde";

        //Test
        careInstitution.setName(name);

        //Verify
        assertEquals(name, careInstitution.getName());
    }
}
