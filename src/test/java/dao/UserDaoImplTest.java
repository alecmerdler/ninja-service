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
import static org.mockito.Mockito.verify;

/**
 * Created by alec on 10/4/16.
 */
public class UserDaoImplTest {
    UserDao userDao;

    // Mocks
    Provider<EntityManager> providerMock;
    EntityManager entityManagerMock;
    Query queryMock;

    public class FindAll {

    }

    @Before
    public void beforeEach() {
        entityManagerMock = mock(EntityManager.class);
        providerMock = mock(Provider.class);
        queryMock = mock(Query.class);
        doReturn(entityManagerMock).when(providerMock).get();
    }

    @Test
    public void testFindAllNoUsers() {
        doReturn(queryMock).when(entityManagerMock).createQuery("select user from User as user");
        doReturn(new ArrayList<>()).when(queryMock).getResultList();
        userDao = new UserDaoImpl(providerMock);

        assertEquals(0, userDao.findAll().size());
    }

    @Test
    public void testFindAllSomeUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        doReturn(queryMock).when(entityManagerMock).createQuery("select user from User as user");
        doReturn(users).when(queryMock).getResultList();
        userDao = new UserDaoImpl(providerMock);

        assertEquals(users.size(), userDao.findAll().size());
    }

    @Test
    public void testFindByUsernameNoUsers() {
        User user = new User();
        String username = "bob";
        doReturn(queryMock).when(entityManagerMock).createQuery("select user from User as user where user.username = :username");
        doReturn(queryMock).when(queryMock).setParameter("username", username);
        doReturn(new ArrayList<>()).when(queryMock).getResultList();
        userDao = new UserDaoImpl(providerMock);

        assertEquals(0, userDao.findByUsername(username).size());
        verify(entityManagerMock).createQuery("select user from User as user where user.username = :username");
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        List<User> users = new ArrayList<>();
        users.add(user);
        doReturn(queryMock).when(entityManagerMock).createQuery("select user from User as user where user.username = :username");
        doReturn(queryMock).when(queryMock).setParameter("username", user.getUsername());
        doReturn(users).when(queryMock).getResultList();
        userDao = new UserDaoImpl(providerMock);
        List<User> userList = userDao.findByUsername(user.getUsername());

        assertEquals(1, userList.size());
        assertEquals(user, userList.get(0));
    }

    @Test
    public void testCreateValid() {

    }
}