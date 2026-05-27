
package com.pruebatecnica.audifarma.application.usecase;
import com.pruebatecnica.audifarma.domain.model.Address;
import com.pruebatecnica.audifarma.domain.model.Customer;
import com.pruebatecnica.audifarma.domain.ports.out.CustomerRepository;
import com.pruebatecnica.audifarma.domain.ports.in.AddAddressCommand;
import com.pruebatecnica.audifarma.domain.ports.in.ManageAddressUseCase;
import com.pruebatecnica.audifarma.domain.exception.CustomerNotFoundException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManageAddressUseCaseImpl implements ManageAddressUseCase {


    private final CustomerRepository customerRepository;

    public ManageAddressUseCaseImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer addAddress(AddAddressCommand command) {
        Customer customer = findOrThrow(command.customerId());

        Address address = Address.create(
                command.departament(),
                command.city(),
                command.fullAddress()
        );

        customer.addAddress(address);
        return customerRepository.save(customer);
    }

    @Override
    public Customer removeAddress(UUID customerId, UUID addressId) {
        Customer customer = findOrThrow(customerId);
        customer.removeAddress(addressId);
        return customerRepository.save(customer);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private Customer findOrThrow(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

}