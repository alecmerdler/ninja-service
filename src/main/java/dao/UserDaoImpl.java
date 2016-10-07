package dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import models.User;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by alec on 10/4/16.
 */
public class UserDaoImpl extends BaseDao<User> implements UserDao {

    @Inject
    public UserDaoImpl(Provider<EntityManager> entityManagerProvider) {
        super(entityManagerProvider, User.class.getSimpleName());
    }

    @UnitOfWork
    public List<User> findByUsername(String username) {
       return queryFindAllByUsername(username);
    }

    @Transactional
    public User create(User user) throws PersistenceException {
        if (user == null) {
            throw new PersistenceException("Model should not be null");
        }
        else if (queryFindAllByUsername(user.getUsername()).size() > 0) {
            throw new PersistenceException("User with given username already exists");
        }

        return super.create(user);
    }

    private List<User> queryFindAllByUsername(String username) {
        EntityManager entityManager = entityManagerProvider.get();

        return entityManager.createQuery("select user from User as user where user.username = :username")
                .setParameter("username", username)
                .getResultList();
    }
}
