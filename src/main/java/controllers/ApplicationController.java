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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.User;
import ninja.Context;
import ninja.Result;
import ninja.exceptions.BadRequestException;
import ninja.params.Param;
import ninja.params.PathParam;
import org.hibernate.service.spi.ServiceException;
import services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ninja.Results.json;


@Singleton
public class ApplicationController {

    private final UserService userService;

    @Inject
    public ApplicationController(UserService userService) {
        this.userService = userService;
    }

    public Result listUsers(@Param("username") String username) {
        List<User> users = new ArrayList<>();

        if (username != null) {
            Optional<User> userOptional = userService.retrieveUserByUsername(username);
            if (userOptional.isPresent()) {
                users.add(userOptional.get());
            }
        }
        else {
            users = userService.listAllUsers();
        }

        return json().render(users);
    }

    public Result createUser(Context context, User user) {
        try {
            userService.createUser(user);
        } catch (ServiceException se) {
            throw new BadRequestException(se.getMessage());
        }


        return json().render(user);
    }

    public Result retrieveUser(@PathParam("id") Long id) {
        User user;
        Optional<User> userOptional = userService.retrieveUserById(id);
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }
        else {
            throw new BadRequestException("User with given ID does not exist");
        }

        return json()
                .render(user);
    }

    public Result updateUser(@PathParam("id") Long id, Context context, User user) {
        Optional<User> userOptional = userService.retrieveUserById(id);
        if (userOptional.isPresent()) {
            try {
                userService.updateUser(user);
            } catch (ServiceException se) {
                throw new BadRequestException(se.getMessage());
            }
        }
        else {
            throw new BadRequestException("User with given ID does not exist");
        }

        return json()
                .status(200)
                .render(user);
    }

    public Result destroyUser(@PathParam("id") Long id) {
        Optional<User> userOptional = userService.retrieveUserById(id);
        if (userOptional.isPresent()) {
            try {
                User user = userOptional.get();
                userService.destroyUser(user);
            } catch (ServiceException se) {
                throw new BadRequestException(se.getMessage());
            }
        }
        else {
            throw new BadRequestException("User with given username does not exist");
        }

        return json()
                .status(204);
    }
}
