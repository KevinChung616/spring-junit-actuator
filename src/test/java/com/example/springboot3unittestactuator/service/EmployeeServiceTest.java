package com.example.springboot3unittestactuator.service;

import com.example.springboot3unittestactuator.Exception.EmployeeNotFoundException;
import com.example.springboot3unittestactuator.entity.Employee;
import com.example.springboot3unittestactuator.repository.EmployeeRepository;
import com.example.springboot3unittestactuator.repository.KafkaProducer;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        /**
         *  Initialize the mocks and inject them into the test class.
         *  Ensure that the mock objects and their behaviors are properly created and associated
         *  with the fields annotated with @Mock, @Spy, or @InjectMocks.
         */
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listEmployees() {
        Employee employee1 = new Employee();
        employee1.setName("John Doe");

        Employee employee2 = new Employee();
        employee2.setName("Jane Doe");

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> employees = employeeService.listEmployees();

        assertThat(employees).hasSize(2);
        assertThat(employees).contains(employee1, employee2);
    }

    @Test
    void findEmployeeById_whenEmployeeExists() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.findEmployeeById(1);

        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.getId()).isEqualTo(1);
        assertThat(foundEmployee.getName()).isEqualTo("John Doe");
    }

    @Test
    void findEmployeeById_whenEmployeeDoesNotExist() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.findEmployeeById(1))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with Id: 1");
    }

    @Test
    void createEmployee() {
        Employee employee = new Employee();
        employee.setName("John Doe");

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee createdEmployee = employeeService.createEmployee(employee);

        assertThat(createdEmployee).isNotNull();
        assertThat(createdEmployee.getName()).isEqualTo("John Doe");

        ArgumentCaptor<String> kafkaMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaProducer).sendPayload(kafkaMessageCaptor.capture());
        assertThat(kafkaMessageCaptor.getValue()).isEqualTo(employee.toString());
    }

    @Test
    void updateEmployee() {
        Employee employee = new Employee();
        employee.setName("John Doe");

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee updatedEmployee = employeeService.updateEmployee(employee);

        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getName()).isEqualTo("John Doe");

        ArgumentCaptor<String> kafkaMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaProducer).sendPayload(kafkaMessageCaptor.capture());
        assertThat(kafkaMessageCaptor.getValue()).isEqualTo(employee.toString());
    }

    @Test
    void deleteEmployeeById_whenEmployeeExists() {
        when(employeeRepository.existsById(anyInt())).thenReturn(true);

        Boolean result = employeeService.deleteEmployeeById(1);

        assertThat(result).isTrue();
        verify(employeeRepository).deleteById(1);
    }

    @Test
    void deleteEmployeeById_whenEmployeeDoesNotExist() {
        when(employeeRepository.existsById(anyInt())).thenReturn(false);

        assertThatThrownBy(() -> employeeService.deleteEmployeeById(1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("employee wit id 1 not exists");
    }
}
