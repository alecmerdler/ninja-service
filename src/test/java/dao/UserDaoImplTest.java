package dao;

import com.google.inject.Provider;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;

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
        doReturn(queryMock).when(entityManagerMock).createQuery("SELECT x FROM User");
        doReturn(entityManagerMock).when(providerMock).get();
        doReturn(new ArrayList<>()).when(queryMock).getResultList();
        userDao = new UserDaoImpl(providerMock);
    }

    @Test
    public void testFindAllNoUsers() {
        assertEquals(0, userDao.findAll().size());
    }

    @Test
    public void testFindByUsername() {

    }
}