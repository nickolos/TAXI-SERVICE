package nick.Aggregation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nick.service.model.HistoryListResponse;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryLabelListPageResponse {
    private AggregationPaginationResponse pagination;
    private List<HistoryLabelResponse> history_trip;

    public HistoryLabelListPageResponse(HistoryListResponse response) {
        pagination = new AggregationPaginationResponse(response.getPagination());
        history_trip = response.getHistory_trip().stream().map(HistoryLabelResponse::new).collect(Collectors.toList());
    }

}
