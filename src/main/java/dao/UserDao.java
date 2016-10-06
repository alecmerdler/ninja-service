package dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import models.User;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by alec on 10/4/16.
 */
public class UserDao extends BaseDao<User> {

    @Inject
    public UserDao(Provider<EntityManager> entityManagerProvider) {
        super(entityManagerProvider);
        this.modelName = User.class.getSimpleName();
    }

    @UnitOfWork
    public List<User> findByUsername(String username) {
        EntityManager entityManager = entityManagerProvider.get();

        return entityManager.createQuery("select user from User as user where user.username = :username")
                .setParameter("username", username)
                .getResultList();
    }
}
