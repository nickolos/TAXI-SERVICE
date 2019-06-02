package nick.service.repository;


import nick.service.entities.HistoryUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty("history.micro.service")
public interface HistoryRepository extends JpaRepository<HistoryUser, Long> {

  //  List<DetailHistoryOrder> findAll(HistoryUser user, PageRequest pageRequest);

    //List<DetailHistoryResponse> findOrdersOneUser(Set<DetailHistoryResponse> list, PageRequest pageRequest);

    //List<DetailHistoryResponse> findAll(R collect, PageRequest pageRequest);
    ///List<DetailHistoryResponse> findAll(Set<DetailHistoryResponse> list, PageRequest pageRequest);
}
