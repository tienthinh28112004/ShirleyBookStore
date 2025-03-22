package ApiWebManga.dto.Response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatusResponse implements Serializable {
    private String amount;
    private String bankCode;
    private String order;
    private String message;
}
