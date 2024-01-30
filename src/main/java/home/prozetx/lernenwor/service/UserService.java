package home.prozetx.lernenwor.service;

import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.domain.user.UserCreation;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    @Transactional
    public User saveUser(UserCreation userCreation) {
        try {
            return userRepository.save(UserMapper.INSTANCE.userCreationToUser(userCreation));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
