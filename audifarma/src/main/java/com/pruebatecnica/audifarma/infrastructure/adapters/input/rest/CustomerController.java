package com.pruebatecnica.audifarma.infrastructure.adapters.input.rest;
import com.pruebatecnica.audifarma.domain.model.Customer;
import com.pruebatecnica.audifarma.domain.model.DocumentType;
import com.pruebatecnica.audifarma.domain.ports.in.AddAddressCommand;
import com.pruebatecnica.audifarma.domain.ports.in.CreateCustomerCommand;
import com.pruebatecnica.audifarma.domain.ports.in.CreateCustomerUseCase;
import com.pruebatecnica.audifarma.domain.ports.in.GetCustomerUseCase;
import com.pruebatecnica.audifarma.domain.ports.in.ManageAddressUseCase;
import com.pruebatecnica.audifarma.domain.ports.in.UpdateCustomerCommand;
import com.pruebatecnica.audifarma.domain.ports.in.UpdateCustomerUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final ManageAddressUseCase manageAddressUseCase;

    public CustomerController(
            CreateCustomerUseCase createCustomerUseCase,
            UpdateCustomerUseCase updateCustomerUseCase,
            GetCustomerUseCase getCustomerUseCase,
            ManageAddressUseCase manageAddressUseCase
    ) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.updateCustomerUseCase = updateCustomerUseCase;
        this.getCustomerUseCase = getCustomerUseCase;
        this.manageAddressUseCase = manageAddressUseCase;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        Customer created = createCustomerUseCase.createCustomer(new CreateCustomerCommand(
                request.firstName(),
                request.lastName(),
                request.documentNumber(),
                request.documentType(),
                request.age()
        ));

        return ResponseEntity
                .created(URI.create("/api/customers/" + created.getId()))
                .body(toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {
        Customer updated = updateCustomerUseCase.updateCustomer(new UpdateCustomerCommand(
                id,
                request.firstName(),
                request.lastName(),
                request.documentNumber(),
                request.documentType(),
                request.age(),
                request.active()
        ));

        return ResponseEntity.ok(toResponse(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(getCustomerUseCase.getCustomerById(id)));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(getCustomerUseCase.getAllCustomers().stream().map(this::toResponse).toList());
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<CustomerResponse> addAddress(
            @PathVariable UUID id,
            @Valid @RequestBody AddAddressRequest request
    ) {
        Customer updated = manageAddressUseCase.addAddress(new AddAddressCommand(
                id,
                request.departament(),
                request.city(),
                request.fullAddress()
        ));

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<CustomerResponse> removeAddress(@PathVariable UUID id, @PathVariable UUID addressId) {
        return ResponseEntity.ok(toResponse(manageAddressUseCase.removeAddress(id, addressId)));
    }

    private CustomerResponse toResponse(Customer customer) {
        List<AddressResponse> addresses = customer.getAddresses().stream()
                .map(address -> new AddressResponse(address.id(), address.departament(), address.city(), address.fullAddress()))
                .toList();

        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getDocumentNumber(),
                customer.getDocumentType(),
                customer.getAge(),
                customer.getActive(),
                addresses
        );
    }

    public record CreateCustomerRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @NotBlank String documentNumber,
            @NotNull DocumentType documentType,
            @NotNull @Positive Integer age
    ) {}

    public record UpdateCustomerRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @NotBlank String documentNumber,
            @NotNull DocumentType documentType,
            @NotNull @Positive Integer age,
            @NotNull Boolean active
    ) {}

    public record AddAddressRequest(
            @NotBlank String departament,
            @NotBlank String city,
            @NotBlank String fullAddress
    ) {}

    public record CustomerResponse(
            UUID id,
            String firstName,
            String lastName,
            String documentNumber,
            DocumentType documentType,
            Integer age,
            Boolean active,
            List<AddressResponse> addresses
    ) {}

    public record AddressResponse(
            UUID id,
            String departament,
            String city,
            String fullAddress
    ) {}
}
