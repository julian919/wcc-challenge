package com.wcc.users.user.service;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wcc.users.client.AuthClient;
import com.wcc.users.user.dto.UserDto;
import com.wcc.users.user.exception.RegistrationFailedException;
import com.wcc.users.user.model.User;
import com.wcc.users.user.repository.UserRepository;

class UserServiceTest {

    private final AuthClient authClient = mock(AuthClient.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService service = new UserService(authClient, userRepository);

    @Test
    void registerUserSavesTheProfileForTheNewPrincipal() {
        String userId = "user-1";
        String username = "julian-test";
        String password = "password-test";
        String firstName = "Julian";
        String lastName = "Lieow";
        String principalId = "principal-1";
        Instant now = Instant.now();

        User savedUser = new User(userId, principalId, firstName, lastName, now);

        when(authClient.createPrincipal(username, password)).thenReturn(principalId);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto user = service.registerUser(username, password, firstName, lastName);

        assertEquals(userId, user.id());
        assertEquals(principalId, user.principalId());
        assertEquals(firstName, user.firstName());
        assertEquals(lastName, user.lastName());
        assertEquals(now, user.createdAt());
        verify(authClient, never()).deletePrincipal(any());
    }

    @Test
    void registerUserDeletesThePrincipalWhenTheProfileSaveFails() {
        String username = "julian-test";
        String password = "password-test";
        String principalId = "principal-2";
        String firstName = "julien";
        String lastName = "wcc";

        when(authClient.createPrincipal(username, password)).thenReturn(principalId);
        when(userRepository.save(any(User.class))).thenThrow(new RegistrationFailedException());

        RegistrationFailedException exception = assertThrows(RegistrationFailedException.class,
                () -> service.registerUser(username, password, firstName, lastName));
        assertEquals("REGISTRATION_FAILED", exception.getErrorCode());

        verify(authClient).deletePrincipal(principalId);
    }

    @Test
    void registerUserSavesNoProfileWhenCreatingThePrincipalFails() {
        String username = "julian-test";
        String password = "password-test";
        String firstName = "julien";
        String lastName = "wcc";

        when(authClient.createPrincipal(username, password))
                .thenThrow(new RegistrationFailedException());

        RegistrationFailedException exception = assertThrows(RegistrationFailedException.class,
                () -> service.registerUser(username, password, firstName, lastName));

        assertEquals("REGISTRATION_FAILED", exception.getErrorCode());

        verify(userRepository, never()).save(any());
    }
}
