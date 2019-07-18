package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.UserDTO;
import nl.ica.oose.a2.zorgrit.dto.UserPreference;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.IUserDAO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IUserService {

    @Inject
    private IUserDAO userDAO;

    public UserDTO getProfile(final int userId) throws DataUnreachableException {
        return userDAO.getProfile(userId);
    }

    public ArrayList<UserPreference> getUserPreferenceByUserId(final int userId) throws DataUnreachableException{
        return userDAO.getUserPreferencesByUserId(userId);
    }

    public void setUserPreferenceByUserIdAndKey(final int userId, final String key, final String value) throws DataUnreachableException{
        userDAO.setUserPreferenceByUserIdAndKey(userId, key, value);
    }
    public void setUserPreferencesByUserId(final int userId, final List<UserPreference> list) throws DataUnreachableException{
        userDAO.setUserPreferencesByUserId(userId, list);
    }
}
