package ApiWebManga.repository;

import ApiWebManga.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT n FROM Order n WHERE n.user.id= :userId")
    List<Order> findOrderByUserId(@Param("userId") Long userId);
}
