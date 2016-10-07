package services;

import models.User;
import org.hibernate.service.spi.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * Created by alec on 10/6/16.
 */
public interface UserService {

    List<User> listAllUsers();

    User createUser(User user) throws ServiceException;

    Optional<User> retrieveUserById(int id) throws ServiceException;

    Optional<User> retrieveUserByUsername(String username) throws ServiceException;
}
