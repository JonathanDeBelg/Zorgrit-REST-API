package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.UserDTO;
import nl.ica.oose.a2.zorgrit.dto.UserPreference;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

import java.util.ArrayList;
import java.util.List;

public interface IUserService {

    /**
     * Gets the user profile
     *
     * @param userId An id that represents the user
     * @return An object of the UserDTO class
     * @throws DataUnreachableException
     */
    UserDTO getProfile(final int userId) throws DataUnreachableException;

    ArrayList<UserPreference> getUserPreferenceByUserId(final int userId) throws DataUnreachableException;

    void setUserPreferenceByUserIdAndKey(final int userId, final String key, final String value) throws DataUnreachableException;

    void setUserPreferencesByUserId(final int userId, final List<UserPreference> list) throws DataUnreachableException;
}
