package ApiWebManga.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "UserHasRole")
@Table(name = "user_has_role")
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
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;
}
