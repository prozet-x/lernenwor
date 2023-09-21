package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.dto.UserDto;
import home.prozetx.lernenwor.model.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    @PostMapping
    public User createNew(@RequestBody @Valid UserDto userDto) {
        return null;
    }
}
