package ApiWebManga.repository;

import ApiWebManga.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByFullName(String fullName);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:keyword% OR u.email LIKE %:keyword%")
    Page<User> findAllByKeyword(Pageable pageable,@Param("keyword") String keyword);
}
