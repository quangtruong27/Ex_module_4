package com.employeemanagement.employee.service;

import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
	Page<Employee> findByAttributes(EmployeeSearchRequest request,  Pageable pageable);

	Optional<Employee> findById(Integer id);

	Employee createEmployee(Employee employee);

	Employee updateEmployee(Integer id, Employee updatedEmployee);

	void deleteEmployee(Integer id);

	Employee updateAvatarCloud(Integer Id, MultipartFile file);

	Employee updateAvatar(Integer Id, MultipartFile file);

	Employee updateAvatarLS(Integer id, MultipartFile file);

}