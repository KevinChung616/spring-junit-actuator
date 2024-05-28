package com.example.springboot3unittestactuator.controller;

import com.example.springboot3unittestactuator.entity.Employee;
import com.example.springboot3unittestactuator.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private Employee employee1;
    private Employee employee2;
    private List<Employee> employees;

    @BeforeEach
    void setUp() {
        employee1 = new Employee();
        employee1.setId(1);
        employee1.setName("John Doe");

        employee2 = new Employee();
        employee2.setId(2);
        employee2.setName("Jane Doe");

        employees = Arrays.asList(employee1, employee2);
    }

    @Test
    void testListEmployees() throws Exception {
        when(employeeService.listEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(employee1.getId()))
                .andExpect(jsonPath("$[0].name").value(employee1.getName()))
                .andExpect(jsonPath("$[1].id").value(employee2.getId()))
                .andExpect(jsonPath("$[1].name").value(employee2.getName()));
    }

    @Test
    void testFindEmployeeById() throws Exception {
        when(employeeService.findEmployeeById(1)).thenReturn(employee1);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(employee1.getId()))
                .andExpect(jsonPath("$.name").value(employee1.getName()));
    }

    @Test
    void testCreateEmployee() throws Exception {
        when(employeeService.createEmployee(employee1)).thenReturn(employee1);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(employee1.getId()))
                .andExpect(jsonPath("$.name").value(employee1.getName()));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        when(employeeService.updateEmployee(employee1)).thenReturn(employee1);

        mockMvc.perform(put("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(employee1.getId()))
                .andExpect(jsonPath("$.name").value(employee1.getName()));
    }

    @Test
    void testDeleteEmployeeById() throws Exception {
        when(employeeService.deleteEmployeeById(1)).thenReturn(true);

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));
    }
}
