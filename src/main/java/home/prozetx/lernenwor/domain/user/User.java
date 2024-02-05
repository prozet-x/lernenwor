package home.prozetx.lernenwor.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(unique = true)
    private String name;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    private boolean emailConfirmed = false;

    @NotNull
    @ToString.Exclude
    private String password;

//    public String toSafeString() {
//        return "User {" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", email='" + email + '\'' +
//                '}';
//    }
}
