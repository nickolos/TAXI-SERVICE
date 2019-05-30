package nick.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryListResponse {
    private PaginationResponse pagination;
    private List<DetailHistoryResponse> history_trip;


}
