package nick.service.service;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long id) {
        super("Payment {" + id + "} not found");
    }
}
