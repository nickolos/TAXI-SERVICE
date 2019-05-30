package nick.service.service;


import nick.service.entities.Payment;
import nick.service.model.PaymentRequest;
import nick.service.model.PaymentResponse;
import nick.service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("localPaymentService")
@ConditionalOnProperty("payment.micro.service")
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private Payment findChecked(Long id) {
        Payment entity = paymentRepository.findById(id).get();
        if(entity == null) {
            throw new PaymentNotFoundException(id);
        }
        return entity;
    }



    @Override
    @Transactional
    public void create(Long id, PaymentRequest payment) {
        if(paymentRepository.existsById(id)) {
            throw new PaymentExistsException(id);
        }
        Payment entity = new Payment();
        entity.setId(id);
        entity.setCost(payment.getCost());
        entity.setStatus_payment(false);
        paymentRepository.save(entity);
    }

    @Override
    public PaymentResponse update(Long id, PaymentRequest payment) {

        Payment entity = findChecked(id);
        entity.setCost(payment.getCost());
        paymentRepository.save(entity);
        return new PaymentResponse(entity);
    }

    @Override
    @Transactional
    public void to_pay(Long id, PaymentRequest payment) {
        Payment entity = findChecked(id);
        entity.setStatus_payment(true);
       paymentRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        paymentRepository.delete(findChecked(id));
    }
}
