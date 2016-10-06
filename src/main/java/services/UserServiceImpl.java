package services;

import com.google.inject.Inject;
import dao.Dao;
import models.User;
import org.hibernate.service.spi.ServiceException;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by alec on 10/6/16.
 */
public class UserServiceImpl implements UserService {

    private final Dao userDao;

    @Inject
    public UserServiceImpl(Dao userDao) {
        this.userDao = userDao;
    }

    public List<User> listAllUsers() {
        return userDao.findAll();
    }

    public User createUser(User user) {
        if (user == null) {
            throw new ServiceException("User should not be null");
        }

        try {
            return (User) userDao.create(user);
        } catch (PersistenceException pe) {
            throw new ServiceException("Could not persist given User");
        }
    }
}
