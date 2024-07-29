package home.prozetx.lernenwor.service.mapper;

import home.prozetx.lernenwor.config.CustomUserDetails;
import home.prozetx.lernenwor.domain.auth.SignUp;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.domain.user.UserCreation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User signUpToUser(SignUp userCreation);
    CustomUserDetails userToCustomUserDetails(User user);
}
