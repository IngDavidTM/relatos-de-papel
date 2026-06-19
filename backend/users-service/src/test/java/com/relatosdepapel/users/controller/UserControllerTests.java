package com.relatosdepapel.users.controller;

import com.relatosdepapel.users.exception.ForbiddenOperationException;
import com.relatosdepapel.users.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserControllerTests {

    @Test
    void rejectsAccessToAnotherUsersProfile() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);

        assertThrows(ForbiddenOperationException.class,
                () -> controller.getById(2L, 1L, "ROLE_USER"));
    }

    @Test
    void allowsAdministratorsToResolveAProfile() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);

        controller.getById(2L, 1L, "ROLE_ADMIN");

        verify(userService).findById(2L);
    }
}
