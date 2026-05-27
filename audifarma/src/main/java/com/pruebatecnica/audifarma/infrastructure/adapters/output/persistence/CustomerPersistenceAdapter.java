package com.pruebatecnica.audifarma.infrastructure.adapters.output.persistence;

import com.pruebatecnica.audifarma.domain.model.Address;
import com.pruebatecnica.audifarma.domain.model.Customer;
import com.pruebatecnica.audifarma.domain.ports.out.CustomerRepository;
import com.pruebatecnica.audifarma.infrastructure.entity.AddressEntity;
import com.pruebatecnica.audifarma.infrastructure.entity.CustomerEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceAdapter implements CustomerRepository {

    private final SpringDataCustomerRepository repository;

    public CustomerPersistenceAdapter(SpringDataCustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = toEntity(customer);
        CustomerEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return repository.existsByDocumentNumber(documentNumber);
    }

    @Override
    public boolean existsByDocumentNumberAndIdNot(String documentNumber, UUID id) {
        return repository.existsByDocumentNumberAndIdNot(documentNumber, id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    private CustomerEntity toEntity(Customer customer) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customer.getId());
        entity.setFirstName(customer.getFirstName());
        entity.setLastName(customer.getLastName());
        entity.setDocumentNumber(customer.getDocumentNumber());
        entity.setDocumentType(customer.getDocumentType());
        entity.setAge(customer.getAge());
        entity.setActive(customer.getActive());

        List<AddressEntity> addresses = customer.getAddresses().stream().map(address -> {
            AddressEntity addressEntity = new AddressEntity();
            addressEntity.setId(address.id());
            addressEntity.setDepartament(address.departament());
            addressEntity.setCity(address.city());
            addressEntity.setFullAddress(address.fullAddress());
            addressEntity.setCustomer(entity);
            return addressEntity;
        }).toList();

        entity.setAddresses(new ArrayList<>(addresses));
        return entity;
    }

    private Customer toDomain(CustomerEntity entity) {
        List<Address> addresses = entity.getAddresses().stream()
                .map(address -> new Address(address.getId(), address.getDepartament(), address.getCity(), address.getFullAddress()))
                .toList();

        return new Customer(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDocumentNumber(),
                entity.getDocumentType(),
                entity.getAge(),
                entity.getActive(),
                new ArrayList<>(addresses)
        );
    }
}
