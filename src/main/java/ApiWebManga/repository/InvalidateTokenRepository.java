package ApiWebManga.repository;

import ApiWebManga.Entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface InvalidateTokenRepository extends JpaRepository<InvalidatedToken,String> {
    boolean existsById(String id);

     //findByToken(String token);
}
