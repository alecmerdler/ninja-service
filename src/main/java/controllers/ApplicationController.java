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
import rx.schedulers.Schedulers;
import services.MessageService;
import services.UserService;

import java.util.*;

import static ninja.Results.json;


@Singleton
public class ApplicationController {

    private final UserService userService;
    private final MessageService messageService;

    @Inject
    public ApplicationController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    public Result listMessages() {
        final List<Map<String, Object>> messages = new ArrayList<>();
        try {
            messageService.getMessages()
                    .subscribeOn(Schedulers.computation())
                    .subscribe((List<Map<String, Object>> newMessages) -> {
                        for (Map<String, Object> message : newMessages) {
                            messages.add(message);
                        }
                    });
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        return json().render(messages);
    }

    public Result sendMessage() {
        String topic = "test";
        Map<String, Object> message = new HashMap<>();
        message.put("from", "service-2");
        try {
            messageService.sendMessage(topic, message);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "message sent");

        return json().render(response);
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
