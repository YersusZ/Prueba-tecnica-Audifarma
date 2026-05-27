package com.pruebatecnica.audifarma.domain.ports.in;
import java.util.UUID;


public record AddAddressCommand(
            UUID customerId,
            String departament,
            String city,
            String fullAddress
    ) {}