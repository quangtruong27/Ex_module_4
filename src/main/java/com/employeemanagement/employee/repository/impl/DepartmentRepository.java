package com.employeemanagement.employee.repository.impl;

import com.employeemanagement.employee.model.Department;
import com.employeemanagement.employee.repository.IDepartmentRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentRepository implements IDepartmentRepository {
	final List<Department> departments = new ArrayList<>(
			Arrays.asList(
					new Department(1, "Quản Lý"),
					new Department(2, "Nhân Sự"),
					new Department(3, "Kế Toán"),
					new Department(4, "Sản Xuất"),
					new Department(5, "Sale")
			)
	);

	public List<Department> findAllDepartments() {
		return departments;
	}

	public Department findDepartmentById(Integer id) {
		for(Department department : departments) {
			if (department.getId() == id) {
				return department;
			}
		}
		return null;
	}

	public Department createDepartment(Department department) {
		department.setId((int)(Math.random() * 100000000));
		departments.add(department);
		return department;
	}

	public Department updateDepartment(Integer id, Department updateDepartment) {
		for (Department department : departments) {
			if (department.getId().equals(id)) {
				department.setName(updateDepartment.getName());
				return department;
			}
		}
		return null;
	}

	public Department deleteDepartment(Integer id) {
		for (Department department : departments) {
			if (department.getId() == id) {
				departments.remove(department);
				return department;
			}
		}
		return null;
	}
}
