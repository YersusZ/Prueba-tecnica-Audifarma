
package com.pruebatecnica.audifarma.application.usecase;
import com.pruebatecnica.audifarma.domain.model.Customer;
import com.pruebatecnica.audifarma.domain.ports.out.CustomerRepository;
import com.pruebatecnica.audifarma.domain.ports.in.GetCustomerUseCase;
import com.pruebatecnica.audifarma.domain.exception.CustomerNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GetCustomerUseCaseImpl implements GetCustomerUseCase {

    private final CustomerRepository customerRepository;

    public GetCustomerUseCaseImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}