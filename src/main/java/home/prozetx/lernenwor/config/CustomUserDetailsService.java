package home.prozetx.lernenwor.config;

import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with name %s does not exist", username));
        }
        return user.get();
    }
}
