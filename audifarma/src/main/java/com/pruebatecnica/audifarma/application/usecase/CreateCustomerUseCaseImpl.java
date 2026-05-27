
package com.pruebatecnica.audifarma.application.usecase;
import com.pruebatecnica.audifarma.domain.model.Customer;
import com.pruebatecnica.audifarma.domain.ports.out.CustomerRepository;
import com.pruebatecnica.audifarma.domain.ports.in.CreateCustomerCommand;
import com.pruebatecnica.audifarma.domain.ports.in.CreateCustomerUseCase;
import com.pruebatecnica.audifarma.domain.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateCustomerUseCaseImpl implements CreateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public CreateCustomerUseCaseImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(CreateCustomerCommand command) {
        if (customerRepository.existsByDocumentNumber(command.documentNumber())) {
            throw new BusinessException("Document number already registered: " + command.documentNumber());
        }

        Customer customer = Customer.create(
                command.firstName(),
                command.lastName(),
                command.documentNumber(),
                command.documentType(),
                command.age()
        );

        return customerRepository.save(customer);
    }

}