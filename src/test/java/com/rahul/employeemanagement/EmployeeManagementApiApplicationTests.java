
package com.rahul.employeemanagement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.employeemanagement.employee.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeManagementApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void cleanDatabase() {
        employeeRepository.deleteAll();
    }

    private String employeeJson(String email, String department)
            throws Exception {
        return objectMapper.writeValueAsString(
                new EmployeePayload(
                        "Rahul",
                        "Asapu",
                        email,
                        department,
                        "Java Developer"
                )
        );
    }

    record EmployeePayload(
            String firstName,
            String lastName,
            String email,
            String department,
            String jobTitle
    ) {}

    @Test
    void createEmployeeReturnsCreated() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson(
                                "rahul.test@example.com", "Engineering")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value("Rahul"))
                .andExpect(jsonPath("$.email")
                        .value("rahul.test@example.com"));
    }

    @Test
    void getEmployeesReturnsCreatedEmployee() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson(
                                "rahul.list@example.com", "Engineering")))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email")
                        .value("rahul.list@example.com"));
    }

    @Test
    void updateEmployeeChangesDepartment() throws Exception {
        String response = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson(
                                "rahul.update@example.com", "Engineering")))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode employee = objectMapper.readTree(response);
        long id = employee.get("id").asLong();

        mockMvc.perform(put("/api/employees/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson(
                                "rahul.update@example.com",
                                "Platform Engineering")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department")
                        .value("Platform Engineering"));
    }

    @Test
    void deleteEmployeeRemovesEmployee() throws Exception {
        String response = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson(
                                "rahul.delete@example.com", "Engineering")))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/employees/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/employees/" + id))
                .andExpect(status().isNotFound());
    }

    
@Test
void createEmployeeRejectsBlankFirstName() throws Exception {
    String request = objectMapper.writeValueAsString(
            new EmployeePayload(
                    " ",
                    "Asapu",
                    "rahul.blank@example.com",
                    "Engineering",
                    "Java Developer"
            )
    );

    mockMvc.perform(post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").exists());
}

@Test
void createEmployeeRejectsInvalidEmail() throws Exception {
    mockMvc.perform(post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(employeeJson(
                            "not-an-email",
                            "Engineering"
                    )))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").exists());
}

}
