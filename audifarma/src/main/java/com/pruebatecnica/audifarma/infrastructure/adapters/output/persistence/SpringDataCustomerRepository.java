package com.pruebatecnica.audifarma.infrastructure.adapters.output.persistence;

import com.pruebatecnica.audifarma.infrastructure.entity.CustomerEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    boolean existsByDocumentNumber(String documentNumber);

    boolean existsByDocumentNumberAndIdNot(String documentNumber, UUID id);
}
