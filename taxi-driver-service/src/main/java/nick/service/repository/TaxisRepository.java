package nick.service.repository;


import nick.service.entities.Taxis;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty("taxi-driver.micro.service")
public interface TaxisRepository extends JpaRepository<Taxis, Long> {
}
