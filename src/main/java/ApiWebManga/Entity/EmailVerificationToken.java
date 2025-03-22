package ApiWebManga.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationToken{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Column(name ="token",unique = true,nullable = false)
    private String token;

    @Column(name = "expiration_date",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
}
