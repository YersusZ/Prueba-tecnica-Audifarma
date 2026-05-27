package com.pruebatecnica.audifarma.domain.ports.in;
import com.pruebatecnica.audifarma.domain.model.Customer;
import java.util.UUID;


public interface ManageAddressUseCase {
    Customer addAddress(AddAddressCommand command);
    Customer removeAddress(UUID customerId, UUID addressId);
}