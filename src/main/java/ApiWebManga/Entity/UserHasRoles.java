package ApiWebManga.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "UserHasRole")
@Table(name = "users_has_roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder//1 admin,2user
public class UserHasRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;
}
