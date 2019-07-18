package nl.ica.oose.a2.zorgrit.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class UserDTOTest {

    @Test
    public void ShouldPassAllPojoTests() {
        // Setup
        final Class<?> classUnderTest = UserDTO.class;

        // Verify
        assertPojoMethodsFor(classUnderTest).quickly();
    }

    @Test
    public void testGetterSetterId() {
        //Setup
        UserDTO userDTO = new ClientDTO();
        int id = 1;

        //Test
        userDTO.setId(id);

        //Verify
        assertEquals(id, userDTO.getId());
    }

    @Test
    public void testGetterSetterType() {
        //Setup
        UserDTO userDTO = new ClientDTO();
        String type = "client";

        //Test
        userDTO.setType(type);

        //Verify
        assertEquals(type, userDTO.getType());
    }

    @Test
    public void testGetterSetterFirstName() {
        //Setup
        UserDTO userDTO = new ClientDTO();
        String firstName = "Henk";

        //Test
        userDTO.setFirstName(firstName);

        //Verify
        assertEquals(firstName, userDTO.getFirstName());
    }

    @Test
    public void testGetterSetterLastName() {
        //Setup
        UserDTO userDTO = new ClientDTO();
        String lastName = "Jansen";

        //Test
        userDTO.setLastName(lastName);

        //Verify
        assertEquals(lastName, userDTO.getLastName());
    }

    @Test
    public void testGetterSetterEmail() {
        //Setup
        UserDTO userDTO = new ClientDTO();
        String email = "Jansen@gmail.com";

        //Test
        userDTO.setEmail(email);

        //Verify
        assertEquals(email, userDTO.getEmail());
    }

    @Test
    public void testGetterSetterPhoneNumber() {
        //Setup
        //TODO phonenumber cannot be a int
        UserDTO userDTO = new ClientDTO();
        String phoneNumber = "0612345678";

        //Test
        userDTO.setPhoneNumber(phoneNumber);

        //Verify
        assertEquals(phoneNumber, userDTO.getPhoneNumber());
    }

    @Test
    public void testGetterSetterStreet() {
        //Setup
        UserDTO userDTO = new ClientDTO();
        String street = "Ruitenberglaan";

        //Test
        userDTO.setStreet(street);

        //Verify
        assertEquals(street, userDTO.getStreet());
    }

    @Test
    public void testGetterSetterHouseNumber() {
        //Setup
        UserDTO userDTO = new ClientDTO();
        String houseNumber = "23A";

        //Test
        userDTO.setHouseNumber(houseNumber);

        //Verify
        assertEquals(houseNumber, userDTO.getHouseNumber());
    }

    @Test
    public void testGetterSetterZipCode() {
        //Setup
        UserDTO userDTO = new ClientDTO();
        String zipCode = "1298KT";

        //Test
        userDTO.setZipCode(zipCode);

        //Verify
        assertEquals(zipCode, userDTO.getZipCode());
    }

    @Test
    public void testGetterSetterResidence() {
        //Setup
        UserDTO userDTO = new ClientDTO();
        String residence = "Arnhem";

        //Test
        userDTO.setResidence(residence);

        //Verify
        assertEquals(residence, userDTO.getResidence());
    }

    @Test
    public void testGetterSetterDateOfBirth() {
        //Setup
        UserDTO userDTO = new ClientDTO();
        String dateOfBirth = "2017-04-03";

        //Test
        userDTO.setDateOfBirth(dateOfBirth);

        //Verify
        assertEquals(dateOfBirth, userDTO.getDateOfBirth());
    }

    @Test
    public void testGetterSetterFirstTimeProfileCheck() {
        //Setup
        UserDTO userDTO = new ClientDTO();

        //Test
        userDTO.setFirstTimeProfileCheck(true);

        //Verify
        assertEquals(userDTO.isFirstTimeProfileCheck(), true);
    }
}
