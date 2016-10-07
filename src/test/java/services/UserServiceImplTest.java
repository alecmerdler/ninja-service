package services;

import dao.UserDao;
import models.User;
import org.hibernate.service.spi.ServiceException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by alec on 10/6/16.
 */
public class UserServiceImplTest {
    UserServiceImpl userService;

    // Mocks
    UserDao userDaoMock;

    @Before
    public void beforeEach() {
        userDaoMock = mock(UserDao.class);
    }

    @Test
    public void testListAllUsersNoUsers() {
        doReturn(new ArrayList<>()).when(userDaoMock).findAll();
        userService = new UserServiceImpl(userDaoMock);

        assertEquals(0, userService.listAllUsers().size());
        verify(userDaoMock).findAll();
    }

    @Test
    public void testListAllUsersSomeUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        doReturn(users).when(userDaoMock).findAll();
        userService = new UserServiceImpl(userDaoMock);

        assertEquals(users.size(), userService.listAllUsers().size());
        verify(userDaoMock).findAll();
    }

    @Test
    public void testCreateUserValid() {
        User user = new User();
        doReturn(user).when(userDaoMock).create(user);
        userService = new UserServiceImpl(userDaoMock);

        assertEquals(user, userService.createUser(user));
        verify(userDaoMock).create(user);
    }

    @Test
    public void testCreateUserInvalidThrowsServiceException() {
        userService = new UserServiceImpl(userDaoMock);
        try {
            userService.createUser(null);
            fail("Should throw exception");
        } catch (Exception e) {
            assertTrue(e instanceof ServiceException);
        }
    }

    @Test
    public void testRetrieveUserByUsernameDoesNotExist() {
        String username = "bob";
        doReturn(new ArrayList<>()).when(userDaoMock).findByUsername(username);
        userService = new UserServiceImpl(userDaoMock);

        assertFalse(userService.retrieveUserByUsername(username).isPresent());
        verify(userDaoMock).findByUsername(username);
    }

    @Test
    public void testRetrieveUserByUsernameExists() {

    }
}