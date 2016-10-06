package dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import models.User;

import javax.persistence.EntityManager;

/**
 * Created by alec on 10/4/16.
 */
public class UserDao extends BaseDao<User> {

//    @Inject
//    private Provider<EntityManager> entityManagerProvider;

//    public UserDao() {
//        super();
//    }
//
//    @Inject
//    public UserDao(Provider<EntityManager> entityManagerProvider) {
//        this.entityManagerProvider = entityManagerProvider;
//    }

    @Inject
    public UserDao(Provider<EntityManager> entityManagerProvider) {
        super(entityManagerProvider);
        this.modelName = User.class.getSimpleName();
    }

//    @UnitOfWork
//    public List<Model> findAll() {
//        EntityManager entityManager = entityManagerProvider.get();
//
//        return entityManager.createQuery("select user from User as user").getResultList();
//    }
//
//    @UnitOfWork
//    public List<User> findByUsername(String username) {
//        EntityManager entityManager = entityManagerProvider.get();
//
//        Query query = entityManager.createQuery("select user from User as user where user.username = :username")
//            .setParameter("username", username);
//        return query.getResultList();
//    }
//
//    @Transactional
//    public User create(User user) {
//        EntityManager entityManager = entityManagerProvider.get();
//        entityManager.persist(user);
//
//        return user;
//    }
}
