package home.prozetx.lernenwor.service.mapper;

import home.prozetx.lernenwor.domain.auth.SignUp;
import home.prozetx.lernenwor.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User signUpToUser(SignUp signUp);
}
