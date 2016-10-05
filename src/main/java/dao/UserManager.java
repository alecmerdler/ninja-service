package dao;

import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

/**
 * Created by alec on 10/4/16.
 */
public class UserManager {
    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    public UserManager(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }
}
