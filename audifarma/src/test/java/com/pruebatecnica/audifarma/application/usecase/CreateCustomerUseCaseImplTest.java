package com.pruebatecnica.audifarma.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pruebatecnica.audifarma.domain.exception.BusinessException;
import com.pruebatecnica.audifarma.domain.model.Customer;
import com.pruebatecnica.audifarma.domain.model.DocumentType;
import com.pruebatecnica.audifarma.domain.ports.in.CreateCustomerCommand;
import com.pruebatecnica.audifarma.domain.ports.out.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CreateCustomerUseCaseImpl useCase;

    @Test
    void createCustomerShouldPersistWhenDocumentIsUnique() {
        CreateCustomerCommand command = new CreateCustomerCommand(
                "Juan",
                "Perez",
                "123456",
                DocumentType.CC,
                30
        );

        when(customerRepository.existsByDocumentNumber(command.documentNumber())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer created = useCase.createCustomer(command);

        assertNotNull(created.getId());
        assertEquals("Juan", created.getFirstName());
        assertEquals("Perez", created.getLastName());
        assertEquals("123456", created.getDocumentNumber());
        verify(customerRepository).existsByDocumentNumber("123456");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomerShouldFailWhenDocumentAlreadyExists() {
        CreateCustomerCommand command = new CreateCustomerCommand(
                "Juan",
                "Perez",
                "123456",
                DocumentType.CC,
                30
        );

        when(customerRepository.existsByDocumentNumber(command.documentNumber())).thenReturn(true);

        assertThrows(BusinessException.class, () -> useCase.createCustomer(command));
        verify(customerRepository, never()).save(any(Customer.class));
    }
}
