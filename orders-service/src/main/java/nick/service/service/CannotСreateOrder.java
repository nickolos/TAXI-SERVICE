package nick.service.service;

public class CannotСreateOrder extends RuntimeException {
    public CannotСreateOrder() {
        super("No free taxis");
    }
}
