package com.pruebatecnica.audifarma.domain.ports.in;
import com.pruebatecnica.audifarma.domain.model.Customer;

public interface CreateCustomerUseCase {
    Customer createCustomer(CreateCustomerCommand command);
}
