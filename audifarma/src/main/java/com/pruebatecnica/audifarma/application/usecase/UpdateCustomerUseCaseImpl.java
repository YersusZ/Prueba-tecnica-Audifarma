
package com.pruebatecnica.audifarma.application.usecase;
import com.pruebatecnica.audifarma.domain.model.Customer;
import com.pruebatecnica.audifarma.domain.exception.CustomerNotFoundException;
import com.pruebatecnica.audifarma.domain.ports.out.CustomerRepository;
import com.pruebatecnica.audifarma.domain.ports.in.UpdateCustomerUseCase;
import com.pruebatecnica.audifarma.domain.ports.in.UpdateCustomerCommand;
import com.pruebatecnica.audifarma.domain.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UpdateCustomerUseCaseImpl implements UpdateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public UpdateCustomerUseCaseImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer updateCustomer(UpdateCustomerCommand command) {
        Customer customer = customerRepository.findById(command.id())
                .orElseThrow(() -> new CustomerNotFoundException(command.id()));

        if (customerRepository.existsByDocumentNumberAndIdNot(command.documentNumber(), command.id())) {
            throw new BusinessException("Document number already registered: " + command.documentNumber());
        }

        customer.update(
                command.firstName(),
                command.lastName(),
                command.documentType(),
                command.documentNumber(),
                command.age(),
                command.active()
        );

        return customerRepository.save(customer);
    }


}