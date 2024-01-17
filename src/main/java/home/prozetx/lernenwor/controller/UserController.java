package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.domain.user.UserCreation;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;
    @GetMapping
    public String get() {
        User user = new User("name1");
        userRepository.save(user);


        return Long.toString(userRepository.count());
    }
    @PostMapping
    public String createNew(@RequestBody @Valid UserCreation userCreation, BindingResult bindingResult) {
        //userRepository.save(user);
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("\n"));
        }

        return "Cool!";
    }
}
