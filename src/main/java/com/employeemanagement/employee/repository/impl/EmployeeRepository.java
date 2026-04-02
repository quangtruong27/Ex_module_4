package com.employeemanagement.employee.repository.impl;

import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.exception.AppException;
import com.employeemanagement.employee.exception.ErrorCode;
import com.employeemanagement.employee.model.Employee;
import com.employeemanagement.employee.model.Gender;
import com.employeemanagement.employee.repository.IEmployeeRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeRepository implements IEmployeeRepository {

	@Override
	public List<Employee> findAll(EmployeeSearchRequest employeeSearchRequest) {
		List<Employee> employeeList = new ArrayList<>();

		try {
			String sql = "SELECT id, name, dob, gender, salary, phone, department_id" + " FROM employee WHERE 1=1";

			List<Object> parameters = new ArrayList<>();
			if (employeeSearchRequest.getName() != null) {
				sql += " AND LOWER(name) LIKE ?";
				parameters.add("%" + employeeSearchRequest.getName().toLowerCase() + "%");
			}
			if (employeeSearchRequest.getDobFrom() != null) {
				sql += " AND dob >= ?";
				parameters.add(employeeSearchRequest.getDobFrom());
			}
			if (employeeSearchRequest.getDobTo() != null) {
				sql += " AND dob <= ?";
				parameters.add(employeeSearchRequest.getDobTo());
			}
			if (employeeSearchRequest.getGender() != null) {
				sql += " AND gender = ?";
				parameters.add(employeeSearchRequest.getGender().toString());
			}
			if (employeeSearchRequest.getPhone() != null) {
				sql += " AND phone LIKE ?";
				parameters.add("%" + employeeSearchRequest.getPhone() + "%");
			}
			if (employeeSearchRequest.getDepartmentId() != null) {
				sql += " AND department_id = ?";
				parameters.add(employeeSearchRequest.getDepartmentId());
			}
			if (employeeSearchRequest.getSalaryRange() != null) {
				switch (employeeSearchRequest.getSalaryRange()) {
					case "lt5":
						sql += " AND salary < 5000000";
						break;
					case "5-10":
						sql += " AND salary >= 5000000 AND salary <= 10000000";
						break;
					case "10-20":
						sql += " AND salary >= 10000000 AND salary <= 20000000";
						break;
					case "gt20":
						sql += "AND salary > 20000000";
						break;
				}
			}

			PreparedStatement preparedStatement = BaseRepository.getConnection()
					.prepareStatement(sql);

			for (int i = 0; i < parameters.size(); i++) {
				if (parameters.get(i) instanceof LocalDate) {
					preparedStatement.setDate(i + 1, Date.valueOf((LocalDate) parameters.get(i)));
				} else if (parameters.get(i) instanceof Integer) {
					preparedStatement.setInt(i + 1, (Integer) parameters.get(i));
				} else {
					preparedStatement.setObject(i + 1, parameters.get(i));
				}
			}

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Employee employee = new Employee(
						resultSet.getInt("id"),
						resultSet.getString("name"),
						resultSet.getDate("dob").toLocalDate(),
						Gender.valueOf(resultSet.getString("gender")),
						resultSet.getDouble("salary"),
						resultSet.getString("phone"),
						resultSet.getInt("department_id")
				);
				employeeList.add(employee);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return employeeList;
	}

	@Override
	public Optional<Employee> findById(Integer id) {
		Optional<Employee> employeeOptional = Optional.empty();
		try {
			String sql = "SELECT id, name, dob, gender, salary, phone, department_id" +
					" FROM employee WHERE id = ?";

			PreparedStatement preparedStatement = BaseRepository.getConnection()
					.prepareStatement(sql);
			preparedStatement.setInt(1, id);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				employeeOptional = Optional.of(Employee.builder()
						.id(resultSet.getInt("id"))
						.name(resultSet.getString("name"))
						.dob(resultSet.getDate("dob").toLocalDate())
						.gender(Gender.valueOf(resultSet.getString("gender")))
						.salary(resultSet.getDouble("salary"))
						.phone(resultSet.getString("phone"))
						.departmentId(resultSet.getInt("department_id"))
						.build());
			}
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return employeeOptional;
	}

	@Override
	public Employee createEmployee(Employee employee) {
		String updateQuery = "UPDATE employee SET name = ?, dob = ?, gender = ?, salary = ?," +
				" phone = ?, department_id = ? WHERE id = ?";
		String insertQuery = "INSERT INTO employee (id, name, dob, gender, salary, phone, " +
				"department_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try {
			// Kiểm tra xem Employee đã tồn tại trong DB hay chưa
			Optional<Employee> existingEmployee = findById(employee.getId());

			if (existingEmployee.isPresent()) {
				// Nếu đã tồn tại, thực hiện cập nhật (UPDATE)
				try (PreparedStatement preparedStatement = BaseRepository.getConnection()
						.prepareStatement(updateQuery, Statement.RETURN_GENERATED_KEYS)) {
					preparedStatement.setString(1, employee.getName());
					preparedStatement.setDate(2, Date.valueOf(employee.getDob()));
					preparedStatement.setString(3, employee.getGender().toString());
					preparedStatement.setDouble(4, employee.getSalary());
					preparedStatement.setString(5, employee.getPhone());
					preparedStatement.setInt(6, employee.getDepartmentId());
					preparedStatement.setString(7, employee.getId().toString()); // Tương tự với các tham số khác
					preparedStatement.executeUpdate();
				}
			} else {

				try (PreparedStatement preparedStatement = BaseRepository.getConnection()
						.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
					preparedStatement.setString(1, employee.getId().toString());
					preparedStatement.setString(2, employee.getName());
					preparedStatement.setDate(3, Date.valueOf(employee.getDob()));
					preparedStatement.setString(4, employee.getGender().toString());
					preparedStatement.setDouble(5, employee.getSalary());
					preparedStatement.setString(6, employee.getPhone());
					preparedStatement.setInt(7, employee.getDepartmentId()); // Tương tự với các tham số khác
					preparedStatement.executeUpdate();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return employee;
	}

	@Override
	public Employee updateEmployee(Integer id, Employee updatedEmployee) {
		String sql = "UPDATE employee SET name = ?, dob = ?, gender = ?, salary = ?, phone = ?, department_id = ? " +
				"WHERE id = ?";

		try (Connection conn = BaseRepository.getConnection();
			 PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

			preparedStatement.setString(1, updatedEmployee.getName());
			preparedStatement.setDate(2, Date.valueOf(updatedEmployee.getDob()));
			preparedStatement.setString(3, updatedEmployee.getGender().name());
			preparedStatement.setDouble(4, updatedEmployee.getSalary());
			preparedStatement.setString(5, updatedEmployee.getPhone());
			preparedStatement.setInt(6, updatedEmployee.getDepartmentId());
			preparedStatement.setInt(7, id); // Điều kiện WHERE id = ?

			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected == 0) {
				return null; // Không tìm thấy để update
			}

			updatedEmployee.setId(id); // Đảm bảo trả về object có ID chuẩn

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return updatedEmployee;
	}

	@Override
	public Employee deleteEmployee(Integer id) {

		Employee employeeToDelete = findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

		String sql = "DELETE FROM employee WHERE id = ?";

		try (PreparedStatement preparedStatement = BaseRepository.getConnection()
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return  employeeToDelete;
	}

}
