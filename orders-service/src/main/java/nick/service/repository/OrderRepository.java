package nick.service.repository;


import nick.service.entities.Order;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty("order.micro.service")
public interface OrderRepository extends CrudRepository<Order, Long> {

//    static void save(Map<String, Object> updates, Long id) {
//
//    }
}
