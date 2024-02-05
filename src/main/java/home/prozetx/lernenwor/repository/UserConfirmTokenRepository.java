package home.prozetx.lernenwor.repository;

import home.prozetx.lernenwor.domain.userConfirmToken.UserConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConfirmTokenRepository extends JpaRepository<UserConfirmToken, Long> {
}
