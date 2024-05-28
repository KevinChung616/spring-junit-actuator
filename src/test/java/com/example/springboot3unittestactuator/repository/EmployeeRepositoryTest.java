package com.example.springboot3unittestactuator.repository;

import com.example.springboot3unittestactuator.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;


    @Test
    public void testSaveAndFindById() {
        // Create a new Employee
        Employee employee = new Employee();
        employee.setName("John Doe");

        // Save the Employee
        Employee savedEmployee = employeeRepository.save(employee);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isNotNull();

        // Find the Employee by ID
        Optional<Employee> foundEmployee = employeeRepository.findById(savedEmployee.getId());
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getName()).isEqualTo("John Doe");
    }

    @Test
    public void testFindAll() {
        // Create and save some employees
        Employee employee1 = new Employee();
        employee1.setName("John Doe");
        employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setName("Jane Doe");
        employeeRepository.save(employee2);

        // Find all employees
        Iterable<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(2);
    }

    @Test
    public void testDelete() {
        // Create a new Employee
        Employee employee = new Employee();
        employee.setName("John Doe");
        Employee savedEmployee = employeeRepository.save(employee);

        // Delete the Employee
        employeeRepository.delete(savedEmployee);

        // Verify the Employee is deleted
        Optional<Employee> foundEmployee = employeeRepository.findById(savedEmployee.getId());
        assertThat(foundEmployee).isNotPresent();
    }

}