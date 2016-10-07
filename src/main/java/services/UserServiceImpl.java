package services;

import com.google.inject.Inject;
import dao.UserDao;
import models.User;
import org.hibernate.service.spi.ServiceException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

/**
 * Created by alec on 10/6/16.
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Inject
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> listAllUsers() {
        return userDao.findAll();
    }

    public User createUser(User user) throws ServiceException {
        if (user == null) {
            throw new ServiceException("User should not be null");
        }
        try {
            return (User) userDao.create(user);
        } catch (PersistenceException pe) {
            throw new ServiceException("Could not persist given User");
        }
    }

    public Optional<User> retrieveUserById(int id) {
        return empty();
    }

    public Optional<User> retrieveUserByUsername(String username) {
        List<User> usersWithUsername = userDao.findByUsername(username);
        User user = null;
        if (usersWithUsername.size() > 0) {
            user = usersWithUsername.get(0);
        }

        return ofNullable(user);
    }
}
