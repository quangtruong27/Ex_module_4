package com.employeemanagement.employee.repository;

import com.employeemanagement.employee.model.Department;

import java.util.List;

public interface IDepartmentRepository {
	List<Department> findAllDepartments();
	Department findDepartmentById(Integer id);
	Department createDepartment(Department department);
	Department updateDepartment(Integer id, Department updateDepartment);
	Department deleteDepartment(Integer id);
}
