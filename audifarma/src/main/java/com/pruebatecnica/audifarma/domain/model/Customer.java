
package com.pruebatecnica.audifarma.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Customer{
    private UUID id;
    private String firstName;
    private String lastName;
    private String DocumentNumber;
    private DocumentType DocumentType;
    private Integer age;
    private Boolean active;
    private List<Address> addresses;

    // Constructor completo para rehidratar desde la persistencia
    public Customer(UUID id, String firstName, String lastName, String documentNumber, DocumentType documentType, Integer age, Boolean active, List<Address> addresses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.DocumentNumber = documentNumber;
        this.DocumentType = documentType;
        this.age = age;
        this.active = active;
        this.addresses = addresses;
    }

    //Constructor vacío para frameworks de persistencia
    public static Customer create(String firstName, String lastName, String documentNumber, DocumentType documentType, Integer age) {
        validate(firstName, lastName, documentNumber, documentType);
        return new Customer(UUID.randomUUID(), firstName, lastName, documentNumber, documentType, age, true, new ArrayList<>());
    }

    public void update(String firstName, String lastName, DocumentType documentType, String documentNumber, Integer age, Boolean active) {
        validate(firstName, lastName, documentNumber, documentType);
        this.firstName = firstName;
        this.lastName = lastName;
        this.DocumentNumber = documentNumber;
        this.DocumentType = documentType;
        this.age = age;
        this.active = active;
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    public void removeAddress(UUID addressId) {
        this.addresses.removeIf(addr -> addr.id().equals(addressId));
    }

 
    // Validación de reglas de negocio
    private static void validate(String firstName, String lastName, String documentNumber, DocumentType documentType) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de documento no puede estar vacío");
        }
        if (documentType == null) {
            throw new IllegalArgumentException("El tipo de documento no puede ser nulo");
        }
    }

    //Getters (sin setters para mantener la inmutabilidad)
    public UUID getId() {return id;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getDocumentNumber() {return DocumentNumber;}
    public DocumentType getDocumentType() {return DocumentType;}
    public Integer getAge() {return age;}
    public Boolean getActive() {return active;}
    public List<Address> getAddresses()
    {
        return Collections.unmodifiableList(addresses);
    }

}