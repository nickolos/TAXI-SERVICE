package nick.service.service;


import nick.service.model.PaymentRequest;
import nick.service.model.PaymentResponse;

public interface PaymentService {
    void create (Long id, PaymentRequest payment);
    PaymentResponse update (Long id, PaymentRequest payment);
    void to_pay(Long id, PaymentRequest payment);
    void delete(Long id);
}
