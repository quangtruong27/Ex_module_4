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

	@Override
	public List<Employee> findByAttributes(EmployeeSearchRequest request) {
		return employeeRepository.findByAttributes(request);
	}

	@Override
	public Optional<Employee> findById(Integer id) {
		return employeeRepository.findById(id);
	}

	@Override
	public Employee createEmployee(Employee employee) {
		// Sử dụng hàm save() thần thánh của Spring Data JPA
		return employeeRepository.save(employee);
	}

	@Override
	public Employee updateEmployee(Integer id, Employee updatedEmployee) {
		// Gán ID vào để JPA biết đây là "Cập nhật" chứ không phải "Thêm mới"
		updatedEmployee.setId(id);
		return employeeRepository.save(updatedEmployee);
	}

	@Override
	public void deleteEmployee(Integer id) {
		// Sử dụng hàm deleteById() của JPA và không return gì cả (void)
		employeeRepository.deleteById(id);
	}
}