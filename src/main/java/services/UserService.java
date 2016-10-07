package services;

import models.User;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

/**
 * Created by alec on 10/6/16.
 */
public interface UserService {

    List<User> listAllUsers();

    User createUser(User user) throws ServiceException;

    User retrieveUserById(int id) throws ServiceException;

    User retrieveUserByUsername(String username) throws ServiceException;
}
