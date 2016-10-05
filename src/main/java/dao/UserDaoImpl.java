package dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import models.User;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alec on 10/4/16.
 */
public class UserDaoImpl implements UserDao {
    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    public UserDaoImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @UnitOfWork
    public List<User> findAll() {
        EntityManager entityManager = entityManagerProvider.get();

        return entityManager.createQuery("SELECT x FROM User").getResultList();
    }

    @UnitOfWork
    public List<User> findByUsername(String username) {
        return new ArrayList<>();
    }
}
