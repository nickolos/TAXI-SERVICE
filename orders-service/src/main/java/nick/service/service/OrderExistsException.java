package nick.service.service;

public class OrderExistsException extends RuntimeException{
    public  OrderExistsException(Long id) {
        super("There is the same order!");
    }
}
