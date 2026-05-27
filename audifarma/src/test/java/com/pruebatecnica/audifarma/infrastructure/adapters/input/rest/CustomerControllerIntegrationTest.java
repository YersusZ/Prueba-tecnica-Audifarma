package com.pruebatecnica.audifarma.infrastructure.adapters.input.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebatecnica.audifarma.infrastructure.adapters.output.persistence.SpringDataCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpringDataCustomerRepository springDataCustomerRepository;

    @BeforeEach
    void cleanDatabase() {
        springDataCustomerRepository.deleteAll();
    }

    @Test
    void createAndGetCustomerFlowShouldWork() throws Exception {
        String createBody = """
                {
                  "firstName": "Laura",
                  "lastName": "Ramirez",
                  "documentNumber": "9001",
                  "documentType": "CC",
                  "age": 27
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.firstName", is("Laura")))
                .andExpect(jsonPath("$.lastName", is("Ramirez")))
                .andExpect(jsonPath("$.documentNumber", is("9001")))
                .andReturn();

        JsonNode created = objectMapper.readTree(createResult.getResponse().getContentAsString());
        String id = created.get("id").asText();

        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is("Laura")))
                .andExpect(jsonPath("$.addresses", hasSize(0)));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void createCustomerShouldReturnConflictWhenDocumentExists() throws Exception {
        String body = """
                {
                  "firstName": "Ana",
                  "lastName": "Lopez",
                  "documentNumber": "777",
                  "documentType": "CC",
                  "age": 30
                }
                """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)));
    }

    @Test
    void updateCustomerShouldModifyExistingCustomer() throws Exception {
        String createBody = """
                {
                  "firstName": "Carlos",
                  "lastName": "Mora",
                  "documentNumber": "5555",
                  "documentType": "CC",
                  "age": 32
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn();

        String id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        String updateBody = """
                {
                  "firstName": "Carlos Andres",
                  "lastName": "Mora Gomez",
                  "documentNumber": "5556",
                  "documentType": "CE",
                  "age": 33,
                  "active": true
                }
                """;

        mockMvc.perform(put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Carlos Andres")))
                .andExpect(jsonPath("$.documentNumber", is("5556")))
                .andExpect(jsonPath("$.documentType", is("CE")));
    }

    @Test
    void addAndRemoveAddressShouldUpdateCustomerAddresses() throws Exception {
        String createBody = """
                {
                  "firstName": "Miguel",
                  "lastName": "Suarez",
                  "documentNumber": "8100",
                  "documentType": "CC",
                  "age": 41
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn();

        String customerId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        String addAddressBody = """
                {
                  "departament": "Antioquia",
                  "city": "Medellin",
                  "fullAddress": "Calle 10 #20-30"
                }
                """;

        MvcResult addResult = mockMvc.perform(post("/api/customers/{id}/addresses", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addAddressBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addresses", hasSize(1)))
                .andExpect(jsonPath("$.addresses[0].city", is("Medellin")))
                .andReturn();

        String addressId = objectMapper.readTree(addResult.getResponse().getContentAsString())
                .get("addresses").get(0).get("id").asText();

        mockMvc.perform(delete("/api/customers/{id}/addresses/{addressId}", customerId, addressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addresses", hasSize(0)));
    }

    @Test
    void getCustomerShouldReturnNotFoundWhenMissing() throws Exception {
        mockMvc.perform(get("/api/customers/00000000-0000-0000-0000-000000000001"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }
}
