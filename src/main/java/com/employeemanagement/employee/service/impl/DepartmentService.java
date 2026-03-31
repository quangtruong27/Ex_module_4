package com.employeemanagement.employee.service.impl;

import com.employeemanagement.employee.model.Department;
import com.employeemanagement.employee.repository.IDepartmentRepository;
import com.employeemanagement.employee.service.IDepartmentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService implements IDepartmentService {
	IDepartmentRepository departmentRepository;

	public List<Department> findAllDepartments() {
		return departmentRepository.findAllDepartments();
	}

	public Department findDepartmentById(Integer id) {
		return departmentRepository.findDepartmentById(id);
	}

	public Department createDepartment(Department department) {
		return departmentRepository.createDepartment(department);
	}

	public Department updateDepartment(Integer id, Department updatedepartment) {
		return departmentRepository.updateDepartment(id, updatedepartment);
	}

	public Department deleteDepartment(Integer id) {
		return departmentRepository.deleteDepartment(id);
	}
}
