package services;

import models.User;

import java.util.List;

/**
 * Created by alec on 10/6/16.
 */
public interface UserService {

    List<User> listAllUsers();

    User createUser(User user);
}
