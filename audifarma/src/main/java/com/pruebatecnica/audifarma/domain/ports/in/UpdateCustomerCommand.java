package com.pruebatecnica.audifarma.domain.ports.in;
import com.pruebatecnica.audifarma.domain.model.DocumentType;
import java.util.UUID;

public record UpdateCustomerCommand(
            UUID id,
            String firstName,
            String lastName,
            String documentNumber,
            DocumentType documentType,
            Integer age,
            Boolean active
    ) {}