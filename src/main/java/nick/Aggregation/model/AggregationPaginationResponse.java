package nick.Aggregation.model;

import nick.service.model.PaginationResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AggregationPaginationResponse {
    private long total;
    private int pages;
    private int current;

    public AggregationPaginationResponse(PaginationResponse pagination) {
        total = pagination.getTotal();
        pages = pagination.getPages();
        current = pagination.getCurrent();
    }
}
