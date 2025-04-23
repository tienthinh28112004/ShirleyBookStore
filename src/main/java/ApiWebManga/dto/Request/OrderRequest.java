package ApiWebManga.dto.Request;

import ApiWebManga.Enums.PaymentExpression;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private PaymentExpression paymentExpression;

    private List<OrderDetailRequest> detailRequests;
}
