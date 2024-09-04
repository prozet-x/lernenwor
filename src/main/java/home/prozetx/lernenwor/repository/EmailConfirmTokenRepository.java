package home.prozetx.lernenwor.repository;

import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.domain.EmailConfirmToken.EmailConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfirmTokenRepository extends JpaRepository<EmailConfirmToken, Long> {
    EmailConfirmToken findByToken(String token);
    void deleteByUser(User user);
}
