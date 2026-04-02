package com.employeemanagement.employee.repository;

import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeRepository {
	List<Employee> findAll(EmployeeSearchRequest request);

	Optional<Employee> findById(Integer id);

	Employee createEmployee(Employee employee);

	Employee updateEmployee(Integer id, Employee updatedEmployee);

	Employee deleteEmployee(Integer id);
}
