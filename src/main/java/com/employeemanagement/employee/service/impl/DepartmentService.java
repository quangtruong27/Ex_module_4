package com.employeemanagement.employee.service.impl;

import com.employeemanagement.employee.entity.Department;
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

	@Override
	public List<Department> findAllDepartments() {
		return departmentRepository.findAll();
	}

	@Override
	public Department findDepartmentById(Integer id) {
		return departmentRepository.findById(id).orElse(null);
	}

	@Override
	public Department createDepartment(Department department) {
		return departmentRepository.save(department);
	}

	@Override
	public Department updateDepartment(Integer id, Department updatedepartment) {
		updatedepartment.setId(id);
		return departmentRepository.save(updatedepartment);
	}

	@Override
	public void deleteDepartment(Integer id) {
		departmentRepository.deleteById(id);
	}
}