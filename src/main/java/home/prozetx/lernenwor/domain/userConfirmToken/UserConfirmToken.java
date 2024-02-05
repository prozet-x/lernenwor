package home.prozetx.lernenwor.domain.userConfirmToken;

import home.prozetx.lernenwor.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserConfirmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    private User user;

    public UserConfirmToken(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
    }
}
