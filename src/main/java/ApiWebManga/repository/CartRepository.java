package ApiWebManga.repository;

import ApiWebManga.Entity.Cart;
import ApiWebManga.dto.Response.CartItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Long> {
//    @Query("select count(*) from Cart c where c.user.id = :id")
//    Long countByUserId(@Param("id") Long id);

//    @Query("select new ApiWebManga.dto.Response.CartItemResponse"+
//            "(c.cartDetails.book.id,c.cartDetails.book.title,c.cartDetails.book.price,c.cartDetails.book.thumbnail,c.cartDetails.quantity,c.totalMoney)"+
//            "from Cart c "+
//            "where c.user.id = :id")
//    List<CartItemResponse> findAllByUserId(@Param("id") Long id);

}
