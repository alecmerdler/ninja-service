package dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import models.Model;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by alec on 10/5/16.
 */
public class BaseDao<T extends Model> implements Dao<T> {

    protected Provider<EntityManager> entityManagerProvider;
    protected String modelName;

    @Inject
    public BaseDao(Provider<EntityManager> entityManagerProvider, String modelName) {
        this.entityManagerProvider = entityManagerProvider;
        this.modelName = modelName;
    }

    @UnitOfWork
    public List<T> findAll() {
        EntityManager entityManager = entityManagerProvider.get();
        String queryString = "select t from " + modelName + " as t";

        return entityManager.createQuery(queryString).getResultList();
    }

    @Transactional
    public T create(T model) throws PersistenceException {
        if (model == null) {
            throw new PersistenceException("Model should not be null");
        }
        EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(model);

        return model;
    }
}
