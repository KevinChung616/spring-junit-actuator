package com.example.springboot3unittestactuator.service;

import com.example.springboot3unittestactuator.Exception.EmployeeNotFoundException;
import com.example.springboot3unittestactuator.entity.Employee;
import com.example.springboot3unittestactuator.repository.EmployeeRepository;
import com.example.springboot3unittestactuator.repository.KafkaProducer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final KafkaProducer kafkaProducer;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, KafkaProducer kafkaProducer) {
        this.employeeRepository = employeeRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public List<Employee> listEmployees() {
        return employeeRepository.findAll();
    }

    public Employee findEmployeeById(Integer id) {
        return employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with Id: " +  id));
    }

    public Employee createEmployee(Employee employee) {
        Employee persistedEmployee = employeeRepository.save(employee);
        kafkaProducer.sendPayload(employee.toString());
        return persistedEmployee;
    }

    public Employee updateEmployee(Employee employee) {
        Employee updatedEmployee = employeeRepository.save(employee);
        kafkaProducer.sendPayload(employee.toString());
        return updatedEmployee;
    }

    public Boolean deleteEmployeeById(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("employee wit id " + id  + " not exists");
        }
        employeeRepository.deleteById(id);
        return true;
    }
}
