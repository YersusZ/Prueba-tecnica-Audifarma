

package com.pruebatecnica.audifarma.domain.model;
import java.util.UUID;

public class Address{
    private UUID id;
    private String departament;
    private String city;
    private String fullAddress;

    public UUID getId() {
        return id;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}