package ApiWebManga.dto.Response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class PaymentResponse implements Serializable {
    private String code;
    private String message;
    private String paymentUrl;
}