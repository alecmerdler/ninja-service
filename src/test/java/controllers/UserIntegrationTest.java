package controllers;

import com.google.inject.Injector;
import com.mashape.unirest.http.Unirest;
import dao.UserDao;
import ninja.NinjaTest;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import services.MessageService;
import utils.UnirestObjectMapper;

/**
 * Created by alec on 11/7/16.
 */
public class UserIntegrationTest extends NinjaTest {

    ObjectMapper objectMapper;
    MessageService messageService;
    UserDao userDao;
    String apiUrl;
    String userUrl;
    String initializeUrl;

    @Before
    public void beforeEach() {
        Injector injector = getInjector();
        objectMapper = new ObjectMapper();
        messageService = injector.getInstance(MessageService.class);
        userDao = injector.getInstance(UserDao.class);
        apiUrl = getServerAddress() + "api/v1";
        userUrl = apiUrl + "/users";
        initializeUrl = getServerAddress() + "/initialize";

        Unirest.setObjectMapper(new UnirestObjectMapper());
    }

    @Test
    public void testCreateUserPublishesMessage() {

    }

    @Test
    public void testUpdateUserPublishesMessage() {

    }

    @Test
    public void testDestroyUserPublishesMessage() {
        
    }
}
