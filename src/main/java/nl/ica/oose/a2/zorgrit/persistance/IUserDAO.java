package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.UserDTO;
import nl.ica.oose.a2.zorgrit.dto.UserPreference;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

import java.util.ArrayList;
import java.util.List;

/**
 * UserDTO DAO
 *
 * Gets the profile of an user
 */
public interface IUserDAO {

    /**
     * Gets the profile of the user
     *
     * @param userId An id that represents the user
     * @return the specified user
     */
    UserDTO getProfile(final int userId) throws DataUnreachableException;

    int getUserByUsernameAndPassword(final String username, final String password) throws DataUnreachableException;

    ArrayList<UserPreference> getUserPreferencesByUserId(final int userId) throws DataUnreachableException;

    void setUserPreferenceByUserIdAndKey(final int userId, final String key, final String value) throws DataUnreachableException;

    void setUserPreferencesByUserId(final int userId, final List<UserPreference> list) throws DataUnreachableException;

    void createUser(DriverDTO driverDTO);


    void setUserInActiveSince(int clientId);
}
