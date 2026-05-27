package com.pruebatecnica.audifarma.domain.ports.in;    
import com.pruebatecnica.audifarma.domain.model.DocumentType;

public record CreateCustomerCommand(
        String firstName,
        String lastName,
        String documentNumber,
        DocumentType documentType,
        Integer age
) {}