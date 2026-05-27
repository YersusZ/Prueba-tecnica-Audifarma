package com.pruebatecnica.audifarma.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pruebatecnica.audifarma.domain.exception.CustomerNotFoundException;
import com.pruebatecnica.audifarma.domain.model.Address;
import com.pruebatecnica.audifarma.domain.model.Customer;
import com.pruebatecnica.audifarma.domain.model.DocumentType;
import com.pruebatecnica.audifarma.domain.ports.in.AddAddressCommand;
import com.pruebatecnica.audifarma.domain.ports.out.CustomerRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ManageAddressUseCaseImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ManageAddressUseCaseImpl useCase;

    @Test
    void addAddressShouldAppendAddressToCustomer() {
        UUID customerId = UUID.randomUUID();
        Customer customer = Customer.create("Mario", "Lopez", "444", DocumentType.CC, 40);
        AddAddressCommand command = new AddAddressCommand(customerId, "Antioquia", "Medellin", "Calle 10 #20-30");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer updated = useCase.addAddress(command);

        assertEquals(1, updated.getAddresses().size());
        assertEquals("Antioquia", updated.getAddresses().getFirst().departament());
        verify(customerRepository).save(customer);
    }

    @Test
    void addAddressShouldThrowWhenCustomerNotFound() {
        UUID customerId = UUID.randomUUID();
        AddAddressCommand command = new AddAddressCommand(customerId, "Antioquia", "Medellin", "Calle 10 #20-30");

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> useCase.addAddress(command));
    }

    @Test
    void removeAddressShouldDeleteAddressFromCustomer() {
        UUID customerId = UUID.randomUUID();
        Address address = Address.create("Antioquia", "Medellin", "Calle 10 #20-30");
        Customer customer = Customer.create("Mario", "Lopez", "444", DocumentType.CC, 40);
        customer.addAddress(address);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer updated = useCase.removeAddress(customerId, address.id());

        assertEquals(0, updated.getAddresses().size());
        verify(customerRepository).save(customer);
    }
}
