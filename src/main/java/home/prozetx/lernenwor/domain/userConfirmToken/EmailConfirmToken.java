package home.prozetx.lernenwor.domain.userConfirmToken;

import home.prozetx.lernenwor.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class EmailConfirmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ToString.Exclude
    private String token;

    @ManyToOne
    private User user;

    public EmailConfirmToken(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
    }
}
