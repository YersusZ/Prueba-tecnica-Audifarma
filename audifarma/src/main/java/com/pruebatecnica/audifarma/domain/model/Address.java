

package com.pruebatecnica.audifarma.domain.model;
import java.util.UUID;

public record Address(
    UUID id,
    String departament,
    String city,
    String fullAddress
) {


    public Address {
        if (departament == null || departament.trim().isEmpty()) {
            throw new IllegalArgumentException("El departamento no puede estar vacío");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad no puede estar vacía");
        }
        if (fullAddress == null || fullAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección completa no puede estar vacía");
        }
    }

    public static Address create(String departament, String city, String fullAddress) {
        return new Address(UUID.randomUUID(), departament, city, fullAddress);
    }

    public Address withId(UUID newId) {
        return new Address(newId, departament, city, fullAddress);
    }
}