
package com.pruebatecnica.audifarma.domain.model;

import java.util.List;
import java.util.UUID;

public class Client{
    private UUID id;
    private String name;
    private String lastName;
    private String DocumentNumber;
    private String DocumentType;
    private Integer age;
    private Boolean active;
    private List<Address> addresses;

    public Client(String name, String lastName, String documentNumber, String documentType, Integer age, Boolean active, List<Address> address) {
        this.name = name;
        this.lastName = lastName;
        this.DocumentNumber = documentNumber;
        this.DocumentType = documentType;
        this.age = age;
        this.active = active;
        this.addresses = address;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.DocumentNumber = documentNumber;
    }

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        this.DocumentType = documentType;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void removeAddress(UUID addressId){
        this.addresses.removeIf(a -> a.getId().equals(addressId));
    }
}