package com.employeemanagement.employee.service;

import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
	List<Employee> findByAttributes(EmployeeSearchRequest request);

	Optional<Employee> findById(Integer id);

	Employee createEmployee(Employee employee);

	Employee updateEmployee(Integer id, Employee updatedEmployee);

	void deleteEmployee(Integer id);
}