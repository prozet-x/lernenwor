package home.prozetx.lernenwor.unit;

import home.prozetx.lernenwor.controller.UserController;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

}
