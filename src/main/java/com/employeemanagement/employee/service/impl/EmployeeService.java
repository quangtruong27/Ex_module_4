package com.employeemanagement.employee.service.impl;

import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.entity.Employee;
import com.employeemanagement.employee.repository.IEmployeeRepository;
import com.employeemanagement.employee.service.IEmployeeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeService implements IEmployeeService {
	IEmployeeRepository employeeRepository;


	public List<Employee> findAll(EmployeeSearchRequest request) {
		return employeeRepository.findAll(request);
	}

	public Optional<Employee> findById(Integer id) {
		return employeeRepository.findById(id);
	}

	public Employee createEmployee(Employee employee) {
		return employeeRepository.createEmployee(employee);
	}

	public Employee updateEmployee(Integer id ,Employee updateEmployee) {
		return employeeRepository.updateEmployee(id, updateEmployee);
	}

	public Employee deleteEmployee(Integer id) {
		return employeeRepository.deleteEmployee(id);
	}
}
