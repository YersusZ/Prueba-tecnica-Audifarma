package com.pruebatecnica.audifarma.domain.ports.in;
import com.pruebatecnica.audifarma.domain.model.Customer;
import java.util.List;
import java.util.UUID;

public interface GetCustomerUseCase {
    Customer getCustomerById(UUID id);
    List<Customer> getAllCustomers();
}