package com.pruebatecnica.audifarma.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pruebatecnica.audifarma.domain.exception.CustomerNotFoundException;
import com.pruebatecnica.audifarma.domain.model.Customer;
import com.pruebatecnica.audifarma.domain.model.DocumentType;
import com.pruebatecnica.audifarma.domain.ports.out.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetCustomerUseCaseImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private GetCustomerUseCaseImpl useCase;

    @Test
    void getCustomerByIdShouldReturnCustomerWhenExists() {
        UUID id = UUID.randomUUID();
        Customer customer = Customer.create("Ana", "Diaz", "222", DocumentType.CC, 28);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        Customer result = useCase.getCustomerById(id);

        assertEquals(customer, result);
        verify(customerRepository).findById(id);
    }

    @Test
    void getCustomerByIdShouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> useCase.getCustomerById(id));
    }

    @Test
    void getAllCustomersShouldReturnRepositoryList() {
        List<Customer> expected = List.of(
                Customer.create("Ana", "Diaz", "222", DocumentType.CC, 28),
                Customer.create("Luis", "Torres", "333", DocumentType.CE, 35)
        );

        when(customerRepository.findAll()).thenReturn(expected);

        List<Customer> result = useCase.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals(expected, result);
        verify(customerRepository).findAll();
    }
}
