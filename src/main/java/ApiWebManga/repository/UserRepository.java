package ApiWebManga.repository;

import ApiWebManga.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "select u from User u where u.isActive=true " +
            "and (lower(u.fullName) like :keyword " +
            "or lower(u.email) like :keyword)")
    Page<User> searchByKeyword(String keyword, Pageable pageable);
    boolean existsByFullName(String fullName);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.isActive =true AND u.emailVerifiedAt IS NOT NULL")
    Page<User> findAllActiveUsers(Pageable pageable);
}
