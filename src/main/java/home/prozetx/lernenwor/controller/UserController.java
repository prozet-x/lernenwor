package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.dto.UserCreation;
import home.prozetx.lernenwor.dto.UserDto;
import home.prozetx.lernenwor.model.User;
import home.prozetx.lernenwor.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;
    @GetMapping
    public String get() {
        User user = new User("name1");


        return Long.toString(userRepository.count());
    }
    @PostMapping
    public User createNew(@RequestBody @Valid UserCreation userCreation) {
        //userRepository.save(user);

        return null;
    }
}
