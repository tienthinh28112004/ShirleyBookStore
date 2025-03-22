package ApiWebManga.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetail extends AbstractEntity<Long>{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id",nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id",nullable = false)
    private Book book;

    @Column(name="quantity",nullable = false)
    private Long quantity;

    @Column(name="price",nullable = false)
    private Long price;

    @Column(name="total_money",nullable = false)
    private Long totalMoneyBook;//book.price * quantity(tổng tiền của sách ấy nhân với số luọng

}
