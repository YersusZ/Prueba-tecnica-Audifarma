package com.pruebatecnica.audifarma.domain.ports.in;
import com.pruebatecnica.audifarma.domain.model.Customer;

public interface UpdateCustomerUseCase {
    Customer updateCustomer(UpdateCustomerCommand command);
}