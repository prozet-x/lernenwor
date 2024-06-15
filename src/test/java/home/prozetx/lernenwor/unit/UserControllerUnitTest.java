package home.prozetx.lernenwor.unit;

import home.prozetx.lernenwor.controller.UserController;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.domain.user.UserCreation;
import home.prozetx.lernenwor.domain.userConfirmToken.EmailConfirmToken;
import home.prozetx.lernenwor.repository.EmailConfirmTokenRepository;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;

    @Mock
    EmailConfirmTokenRepository emailConfirmTokenRepository;

    @InjectMocks
    UserController userController;

    @Test
    void testGetAll() {
        //given
        List<User> users = List.of(
                new User(1L, "name1", "email1@mail.ru", "password1", false),
                new User(2L, "name2", "email2@mail.ru", "password2", true)
        );
        List<EmailConfirmToken> tokens = List.of(
                new EmailConfirmToken(1L, "token1", users.get(0)),
                new EmailConfirmToken(2L, "token2", users.get(1))
        );

        //when
        doReturn(users).when(userRepository).findAll();
        doReturn(tokens).when(emailConfirmTokenRepository).findAll();

        //then
        var response = userController.get();
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();

        assertNotNull(response);
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, responseBody.get("users"));
        assertEquals(tokens, responseBody.get("tokens"));
    }
}
