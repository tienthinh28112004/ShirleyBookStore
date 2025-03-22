package ApiWebManga.dto.Request;

import ApiWebManga.Entity.OrderDetail;
import ApiWebManga.Entity.User;
import ApiWebManga.Enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private String fullName;

    private String phoneNumber;

    private String address;

    private String note;

    private List<OrderDetailRequest> detailRequests;
}
