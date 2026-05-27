package com.pruebatecnica.audifarma.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pruebatecnica.audifarma.domain.exception.BusinessException;
import com.pruebatecnica.audifarma.domain.exception.CustomerNotFoundException;
import com.pruebatecnica.audifarma.domain.model.Customer;
import com.pruebatecnica.audifarma.domain.model.DocumentType;
import com.pruebatecnica.audifarma.domain.ports.in.UpdateCustomerCommand;
import com.pruebatecnica.audifarma.domain.ports.out.CustomerRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerUseCaseImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private UpdateCustomerUseCaseImpl useCase;

    @Test
    void updateCustomerShouldPersistChangesWhenDataIsValid() {
        UUID id = UUID.randomUUID();
        Customer customer = Customer.create("Carlos", "Ruiz", "555", DocumentType.CC, 29);
        UpdateCustomerCommand command = new UpdateCustomerCommand(
                id,
                "Carlos Andres",
                "Ruiz Gomez",
                "556",
                DocumentType.CE,
                30,
                true
        );

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByDocumentNumberAndIdNot("556", id)).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer updated = useCase.updateCustomer(command);

        assertEquals("Carlos Andres", updated.getFirstName());
        assertEquals("Ruiz Gomez", updated.getLastName());
        assertEquals("556", updated.getDocumentNumber());
        assertEquals(DocumentType.CE, updated.getDocumentType());
        assertEquals(30, updated.getAge());
        verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomerShouldThrowWhenCustomerDoesNotExist() {
        UUID id = UUID.randomUUID();
        UpdateCustomerCommand command = new UpdateCustomerCommand(
                id,
                "Carlos",
                "Ruiz",
                "555",
                DocumentType.CC,
                29,
                true
        );

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> useCase.updateCustomer(command));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomerShouldThrowWhenDocumentBelongsToAnotherCustomer() {
        UUID id = UUID.randomUUID();
        Customer customer = Customer.create("Carlos", "Ruiz", "555", DocumentType.CC, 29);
        UpdateCustomerCommand command = new UpdateCustomerCommand(
                id,
                "Carlos",
                "Ruiz",
                "999",
                DocumentType.CC,
                29,
                true
        );

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByDocumentNumberAndIdNot("999", id)).thenReturn(true);

        assertThrows(BusinessException.class, () -> useCase.updateCustomer(command));
        verify(customerRepository, never()).save(any(Customer.class));
    }
}
