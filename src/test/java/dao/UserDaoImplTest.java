package dao;

import com.google.inject.Provider;
import models.User;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by alec on 10/4/16.
 */
public class UserDaoImplTest {
    Dao userDao;

    // Mocks
    Provider<EntityManager> providerMock;
    EntityManager entityManagerMock;
    Query queryMock;


    @Before
    public void beforeEach() {
        entityManagerMock = mock(EntityManager.class);
        providerMock = mock(Provider.class);
        queryMock = mock(Query.class);
        doReturn(entityManagerMock).when(providerMock).get();
    }

    @Test
    public void testFindAllNoUsers() {
        doReturn(queryMock).when(entityManagerMock).createQuery("select t from User as t");
        doReturn(new ArrayList<>()).when(queryMock).getResultList();
        userDao = new UserDao(providerMock);

        assertEquals(0, userDao.findAll().size());
    }

    @Test
    public void testFindAllSomeUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        doReturn(queryMock).when(entityManagerMock).createQuery("select t from User as t");
        doReturn(users).when(queryMock).getResultList();
        userDao = new UserDao(providerMock);

        assertEquals(users.size(), userDao.findAll().size());
    }

    @Test
    public void testCreateValid() {

    }
}