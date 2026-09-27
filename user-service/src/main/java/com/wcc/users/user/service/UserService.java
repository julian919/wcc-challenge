package com.wcc.users.user.service;

import com.wcc.users.client.AuthClient;
import com.wcc.users.user.dto.UserDto;
import com.wcc.users.user.exception.RegistrationFailedException;
import com.wcc.users.user.exception.UserNotFoundException;
import com.wcc.users.user.model.User;
import com.wcc.users.user.repository.UserRepository;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final AuthClient authClient;
    private final UserRepository userRepository;

    public UserService(AuthClient authClient, UserRepository userRepository) {
        this.authClient = authClient;
        this.userRepository = userRepository;
    }

    public UserDto registerUser(String username, String password, String firstName, String lastName) {
        String principalId;
        try {
            principalId = authClient.createPrincipal(username, password);
        } catch (RestClientException e) {
            log.error("Creating the principal failed; if auth-service saved it anyway, it needs cleanup", e);
            throw new RegistrationFailedException();
        }

        try {
            User saved = userRepository.save(new User(null, principalId, firstName, lastName, Instant.now()));
            return UserDto.from(saved);
        } catch (RuntimeException e) {
            log.error("Saving the profile failed; deleting principal {}", principalId, e);
            try {
                authClient.deletePrincipal(principalId);
            } catch (RestClientException undoFailure) {
                log.error("Deleting principal {} failed; it needs cleanup", principalId, undoFailure);
            }
            throw new RegistrationFailedException();
        }
    }

    public UserDto getOwnProfile(String principalId) {
        return userRepository.findByPrincipalId(principalId)
                .map(UserDto::from)
                .orElseThrow(UserNotFoundException::new);
    }
}
