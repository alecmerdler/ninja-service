/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;


import models.User;
import ninja.NinjaTest;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ApplicationControllerIntegrationTest extends NinjaTest {

    ObjectMapper objectMapper;
    String apiUrl;
    String usersUrl;

    @Before
    public void beforeEach() {
        objectMapper = new ObjectMapper();
        apiUrl = getServerAddress() + "api/v1";
        usersUrl = apiUrl + "/users";
    }

    @Test
    public void testRootGET() {
        String response = ninjaTestBrowser.makeJsonRequest(getServerAddress());
    }

    @Test
    public void testListUsers() {
        String response = ninjaTestBrowser.makeJsonRequest(usersUrl);
        try {
            List<User> users = objectMapper.readValue(response, List.class);
            assertEquals(0, users.size());
        } catch (IOException ioe) {
            fail(ioe.getMessage());
        }
    }

    @Test
    public void testCreateUser() {
        User user = new User("bob", "bob@bob.com");
        String response = ninjaTestBrowser.postJson(usersUrl, user);
        try {
            User createdUser = objectMapper.readValue(response, User.class);
            assertEquals(user.getUsername(), createdUser.getUsername());
        } catch (IOException ioe) {
            fail(ioe.getMessage());
        }
    }

    @Test
    public void testRetrieveUser() {

    }

    @Test
    public void testUpdateUser() {

    }

    @Test
    public void testDestroyUser() {

    }
}
