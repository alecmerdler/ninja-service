package dao;

import com.google.inject.Provider;
import models.User;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by alec on 10/4/16.
 */
public class UserDaoImplTest {
    UserDao userDao;

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
        userDao = new UserDao(providerMock);
    }

    @Test
    public void testFindAllNoUsers() {
        doReturn(queryMock).when(entityManagerMock).createQuery("select t from User as t");
        doReturn(new ArrayList<>()).when(queryMock).getResultList();

        assertEquals(0, userDao.findAll().size());
    }

    @Test
    public void testFindAllSomeUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        doReturn(queryMock).when(entityManagerMock).createQuery("select t from User as t");
        doReturn(users).when(queryMock).getResultList();

        assertEquals(users.size(), userDao.findAll().size());
    }

    @Test
    public void testFindByUsernameNoUsers() {
        String username = "bob";
        doReturn(queryMock).when(entityManagerMock).createQuery("select user from User as user where user.username = :username");
        doReturn(queryMock).when(queryMock).setParameter("username", username);
        doReturn(new ArrayList<>()).when(queryMock).getResultList();

        assertEquals(0, userDao.findByUsername(username).size());
    }

    @Test
    public void testFindByUsernameManyUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User("sally", "sally@gmail.com"));
        doReturn(queryMock).when(entityManagerMock).createQuery("select user from User as user where user.username = :username");
        doReturn(queryMock).when(queryMock).setParameter("username", users.get(0).getUsername());
        doReturn(users).when(queryMock).getResultList();

        assertEquals(users.size(), userDao.findByUsername(users.get(0).getUsername()).size());
    }

    @Test
    public void findByUsernameOneUser() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        doReturn(queryMock).when(entityManagerMock).createQuery("select user from User as user where user.username = :username");
        doReturn(queryMock).when(queryMock).setParameter("username", users.get(0).getUsername());
        doReturn(users).when(queryMock).getResultList();

        assertEquals(users.size(), userDao.findByUsername(users.get(0).getUsername()).size());
    }

    @Test
    public void testCreateValid() {
        User user = new User("bob", "bob@gmail.com");
        doNothing().when(entityManagerMock).persist(user);
        doReturn(new ArrayList<>()).when(queryMock).getResultList();
        doReturn(queryMock).when(entityManagerMock).createQuery("select user from User as user where user.username = :username");
        doReturn(queryMock).when(queryMock).setParameter("username", user.getUsername());

        assertEquals(user, userDao.create(user));
        verify(entityManagerMock).persist(user);
    }

    @Test
    public void testCreateNull() {
        try {
            userDao.create(null);
            fail("Should throw exception");
        } catch (Exception e) {
            assertTrue(e instanceof PersistenceException);
        }
    }

    @Test
    public void testCreateUsernameAlreadyExists() {
        List<User> usersWithSameUsername = new ArrayList<>();
        String username = "bob";
        User firstUser = new User(username, "bob@bob.com");
        User secondUser = new User(username, "bob@gmail.com");
        usersWithSameUsername.add(secondUser);
        doReturn(new ArrayList<>()).doReturn(usersWithSameUsername).when(queryMock).getResultList();
        doReturn(queryMock).when(entityManagerMock).createQuery("select user from User as user where user.username = :username");
        doReturn(queryMock).when(queryMock).setParameter("username", username);

        try {
            userDao.create(firstUser);
            userDao.create(secondUser);
            fail("Should throw exception");
        } catch (Exception e) {
            assertTrue(e instanceof PersistenceException);
        }
    }
}