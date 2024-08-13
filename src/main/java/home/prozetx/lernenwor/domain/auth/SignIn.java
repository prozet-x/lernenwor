package home.prozetx.lernenwor.domain.auth;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SignIn {
    private String name;
    private String password;
}
