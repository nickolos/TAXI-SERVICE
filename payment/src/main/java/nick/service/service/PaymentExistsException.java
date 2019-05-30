package nick.service.service;

public class PaymentExistsException extends RuntimeException {
    public PaymentExistsException(Long id) {
        super("Payment {" + id + "} already exists");
    }
}
