package dao;

import models.User;

import java.util.List;

/**
 * Created by alec on 10/6/16.
 */
public interface UserDao extends Dao<User> {

    List<User> findByUsername(String username);
}
