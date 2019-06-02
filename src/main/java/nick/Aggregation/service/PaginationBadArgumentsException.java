package nick.Aggregation.service;

public class PaginationBadArgumentsException extends RuntimeException {
    PaginationBadArgumentsException() {
        super("Pagination page should be positive or null, size should be positive");
    }
}
