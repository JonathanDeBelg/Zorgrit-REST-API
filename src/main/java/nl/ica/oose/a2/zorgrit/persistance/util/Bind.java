package nl.ica.oose.a2.zorgrit.persistance.util;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.OAuthClient;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.payment.Transaction;
import nl.ica.oose.a2.zorgrit.dto.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Bind {
    public RideDTO ride(ResultSet resultSet) throws SQLException {
        RideDTO ride = new RideDTO();
        ride.setId(resultSet.getInt("Id"));
        ride.setClientId(resultSet.getInt("ClientId"));
        ride.setPickUpDateTime(new Formatter().formatSQLTimestampToStringDateWithoutMilliseconds(resultSet.getTimestamp("PickUpDateTime")));
        ride.setPickUpLocation(resultSet.getString("PickUpLocation"));
        ride.setDropOffLocation(resultSet.getString("DropOffLocation"));
        ride.setNumberOfCompanions(resultSet.getInt("NumberOfCompanions"));
        ride.setNumberOfLuggage(resultSet.getInt("NumberOfLuggage"));
        ride.setReturnRide(resultSet.getBoolean("ReturnRide"));
        ride.setCallService(resultSet.getBoolean("CallService"));
        ride.setUtility(resultSet.getString("utility"));
        ride.setRepeatingRideId(resultSet.getInt("repeatingRideId"));
        ride.setDriverId(resultSet.getInt("driverId"));
        ride.setCancelledByClient(resultSet.getBoolean("cancelledByClient"));
        ride.setExecuted(resultSet.getBoolean("executed"));
        ride.setDistance(resultSet.getInt("distance"));
        ride.setDuration(resultSet.getInt("duration"));

        return ride;
    }

    public DriverDTO driver(ResultSet resultSet) throws SQLException {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setId(resultSet.getInt("id"));
        driverDTO.setFirstName(resultSet.getString("firstName"));
        driverDTO.setLastName(resultSet.getString("lastName"));
        driverDTO.setEmail(resultSet.getString("email"));
        driverDTO.setPhoneNumber(resultSet.getString("phoneNumber"));
        driverDTO.setStreet(resultSet.getString("street"));
        driverDTO.setHouseNumber(resultSet.getString("houseNumber"));
        driverDTO.setZipCode(resultSet.getString("zipCode"));
        driverDTO.setResidence(resultSet.getString("residence"));
        driverDTO.setDateOfBirth(resultSet.getString("dateOfBirth"));

        return driverDTO;
    }

    public ClientDTO client(ResultSet resultSet) throws SQLException {
        ClientDTO client = new ClientDTO();
        client.setId(resultSet.getInt("id"));
        client.setFirstName(resultSet.getString("firstName"));
        client.setLastName(resultSet.getString("lastName"));
        client.setEmail(resultSet.getString("email"));
        client.setPhoneNumber(resultSet.getString("phoneNumber"));
        client.setStreet(resultSet.getString("street"));
        client.setHouseNumber(resultSet.getString("houseNumber"));
        client.setZipCode(resultSet.getString("zipCode"));
        client.setResidence(resultSet.getString("residence"));
        client.setDateOfBirth(resultSet.getString("dateOfBirth"));
        client.setDriverPreferenceForced(resultSet.getBoolean("driverPreferenceForced"));
        client.setCompanion(resultSet.getString("companion"));
        client.setFirstTimeProfileCheck(resultSet.getBoolean("firstTimeProfileCheck"));
        return client;
    }

    public OAuthClient oAuthClient(ResultSet resultSet) throws  SQLException {
        OAuthClient client= new OAuthClient();
        client.setToken(resultSet.getString("token"));
        client.setUserId(resultSet.getInt("userId"));

        return client;
    }

    public Transaction transaction(ResultSet set) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(set.getString(2));
        transaction.setClientId(set.getInt(3));
        transaction.setDriverId(set.getInt(4));
        transaction.setRideId(set.getInt(5));
        transaction.setAmount(set.getDouble(6));
        transaction.setCreatedAt(set.getTimestamp(7));
        transaction.setExpiresAt(set.getTimestamp(8));
        transaction.setStatus(set.getInt(9));
        transaction.setCheckoutUrl(set.getString(10));

        return transaction;
    }
    public RideRating rideRating(ResultSet resultSet) throws SQLException {
        RideRating rating = new RideRating();

        rating.setRideId(resultSet.getInt("rideId"));
        rating.setRating(resultSet.getInt("rating"));
        rating.setMessage(resultSet.getString("message"));
        rating.setTimestamp(resultSet.getString("timestamp"));

        return rating;
    }

    public UserPreference userPreference(ResultSet resultSet) throws SQLException{
        UserPreference userPreference = new UserPreference();
        userPreference.setPreferenceKey(resultSet.getString("settingKey"));
        userPreference.setPreferenceValue(resultSet.getString("settingValue"));
        return userPreference;
    }
}
