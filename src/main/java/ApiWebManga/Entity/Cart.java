package ApiWebManga.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")
public class Cart extends AbstractEntity<Long>{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Column(name="total_money",nullable = false)
    private Long totalMoney;//price * quantity

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)//nếu có thay đổi nó sẽ tự động câập nhật trong csdl và trong cả orderdetail
    private List<CartDetail> cartDetails;
}
