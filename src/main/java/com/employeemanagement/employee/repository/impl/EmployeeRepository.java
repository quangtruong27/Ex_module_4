package com.employeemanagement.employee.repository.impl;

import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.model.Employee;
import com.employeemanagement.employee.model.Gender;
import com.employeemanagement.employee.repository.IEmployeeRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeRepository implements IEmployeeRepository {
	final List<Employee> employees = new ArrayList<>(
			Arrays.asList(
					new Employee(1, "Hoàng Văn Hải", LocalDate.of(1990, 1, 15), Gender.MALE, 15000000.00, "0975123542", 1),
					new Employee(2, "Trần Thị Hoài", LocalDate.of(1985, 5, 20), Gender.FEMALE, 14500000.00, "0967869868", 2),
					new Employee(3, "Lê Văn Sỹ", LocalDate.of(1992, 3, 10), Gender.MALE, 15500000.00, "0988881110", 3),
					new Employee(4, "Phạm Duy Khánh", LocalDate.of(1988, 7, 5), Gender.FEMALE, 14800000.00, "0986555333", 4),
					new Employee(5, "Hoàng Văn Quý", LocalDate.of(1995, 9, 25), Gender.MALE, 15200000.00, "0973388668", 5)
			)
	);

	public List<Employee> findAll(EmployeeSearchRequest request) {

		return employees.stream()
				.filter(e -> request.getName() == null || e.getName().toLowerCase().contains(request.getName().toLowerCase()))
				.filter(e -> request.getDobFrom() == null || !e.getDob().isBefore(request.getDobFrom()))
				.filter(e -> request.getDobTo() == null || !e.getDob().isAfter(request.getDobTo()))
				.filter(e -> request.getGender() == null || e.getGender() == request.getGender())
				.filter(e -> request.getPhone() == null || e.getPhone().contains(request.getPhone()))
				.filter(e -> request.getDepartmentId() == null || Objects.equals(e.getDepartmentId(), request.getDepartmentId()))
				.filter(e -> {
					if (request.getSalaryRange() == null) return true;

					return switch (request.getSalaryRange()) {
						case "lt5" -> e.getSalary() < 5000000;
						case "5-10" -> e.getSalary() >= 5000000 && e.getSalary() < 10000000;
						case "10-20" -> e.getSalary() >= 10000000 && e.getSalary() <= 20000000;
						case "gt20" -> e.getSalary() > 20000000;
						default -> true;
					};
				})
				.collect(Collectors.toList());
	}

	public Employee findById(Integer id) {
		for (Employee employee : employees) {
			if (employee.getId() == id) {
				return employee;
			}
		}
		return null;
	}

	public Employee createEmployee(Employee employee) {
		employee.setId((int) (Math.random() * 100000000));
		employees.add(employee);
		return employee;
	}

	public Employee updateEmployee (Integer id, Employee updatedEmployee) {
		for (Employee employee : employees) {
			if (employee.getId().equals(id)) {
				employee.setName(updatedEmployee.getName());
				employee.setDob(updatedEmployee.getDob());
				employee.setGender(updatedEmployee.getGender());
				employee.setSalary(updatedEmployee.getSalary());
				employee.setPhone(updatedEmployee.getPhone());
				employee.setDepartmentId(updatedEmployee.getDepartmentId());
				return employee;
			}
		}
		return null;
	}

	public Employee deleteEmployee (Integer id) {
		for (Employee employee : employees) {
			if (employee.getId() == id) {
				employees.remove(employee);
				return employee;
			}
		}
		return null;
	}

}
