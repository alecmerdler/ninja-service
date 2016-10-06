package dao;

import models.User;

import java.util.List;

/**
 * Created by alec on 10/4/16.
 */
public interface UserDao extends Dao {
    List<User> findByUsername(String username);
    User create(User user);
}
