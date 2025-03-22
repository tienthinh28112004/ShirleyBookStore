package ApiWebManga.dto.Response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class PaymentResponse implements Serializable {
    private String status;
    private String message;
    private String URL;
}