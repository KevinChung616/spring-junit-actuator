package com.example.springboot3unittestactuator.repository;

import com.example.springboot3unittestactuator.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
