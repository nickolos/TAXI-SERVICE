package nick.service.repository;


import nick.service.entities.DetailHistoryOrder;
import nick.service.entities.HistoryUser;
import nick.service.model.DetailHistoryResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ConditionalOnProperty("history.micro.service")
public interface DetailHistoryOrderRepository extends JpaRepository<DetailHistoryOrder, Long>  {

   List<DetailHistoryResponse> findAllByHistoryUser(HistoryUser user, Pageable pageable);


}
