package dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import models.User;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by alec on 10/4/16.
 */
public class UserDao implements Dao<User> {
    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    public UserDao(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @UnitOfWork
    public List<User> findAll() {
        EntityManager entityManager = entityManagerProvider.get();

        return entityManager.createQuery("select user from User as user").getResultList();
    }

    @UnitOfWork
    public List<User> findByUsername(String username) {
        EntityManager entityManager = entityManagerProvider.get();

        Query query = entityManager.createQuery("select user from User as user where user.username = :username")
            .setParameter("username", username);
        return query.getResultList();
    }

    @Transactional
    public User create(User user) {
        EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(user);

        return user;
    }
}
