package dao;

import com.google.inject.Provider;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by alec on 10/6/16.
 */
public class BaseDaoTest {

    Dao baseDao;

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
    public void testFindAllNoModels() {

    }

    @Test
    public void testFindAllSomeModels() {

    }

    @Test
    public void testCreateValid() {

    }

    @Test
    public void testCreateInvalid() {

    }

}