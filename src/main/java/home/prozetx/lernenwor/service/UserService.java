package home.prozetx.lernenwor.service;

import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.domain.user.UserCreation;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Optional<User> saveUser(UserCreation userCreation) {
//        if (userRepository.existsByName(userCreation.name()) || userRepository.existsByName(userCreation.name())) {
//            System.out.println("Ffffffuuuuuucccckkkk!");
//            return Optional.empty();
//        }
//        User user = UserMapper.INSTANCE.userCreationToUser(userCreation);
//        return Optional.of(userRepository.save(user));

        try {
            return Optional.of(userRepository.save(UserMapper.INSTANCE.userCreationToUser(userCreation)));
        } catch (DataIntegrityViolationException ex) {
            System.out.println(ex.getMessage());
            System.out.println("FFFFFUUUUUCCCCCKKKKK22222222222222222222222!!!!!!");
            return Optional.empty();
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            System.out.println("FFFFFUUUUUCCCCCKKKKK3333333333333333!!!!!!");
            return Optional.empty();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("FFFFFUUUUUCCCCCKKKKK!!!!!!");
            return Optional.empty();
        }
        //return Optional.empty();



    }
}
