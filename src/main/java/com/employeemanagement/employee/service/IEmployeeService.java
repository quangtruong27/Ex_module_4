package com.employeemanagement.employee.service;

import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.model.Employee;

import java.util.List;

public interface IEmployeeService {
	List<Employee> findAll(EmployeeSearchRequest request);

	Employee findById(Integer id);

	Employee createEmployee(Employee employee);

	Employee updateEmployee(Integer id, Employee updatedEmployee);

	Employee deleteEmployee(Integer id);
}