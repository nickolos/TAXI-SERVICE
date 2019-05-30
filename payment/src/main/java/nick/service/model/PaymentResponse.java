package nick.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nick.service.entities.Payment;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private float cost;
    private boolean status_payment;

    public PaymentResponse(Payment payment) {
        id = payment.getId();
        cost = payment.getCost();
        status_payment = payment.isStatus_payment();
    }
}
