package nick.service.service;

public class NoFreeTaxisException extends RuntimeException {
    public NoFreeTaxisException() {
        super("No free taxi ");
    }
}
