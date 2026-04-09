package com.employeemanagement.employee.service;

import com.employeemanagement.employee.entity.Department;
import java.util.List;

public interface IDepartmentService {
	List<Department> findAllDepartments();
	Department findDepartmentById(Integer id);
	Department createDepartment(Department department);
	Department updateDepartment(Integer id, Department updateDepartment);
	void deleteDepartment(Integer id);
}