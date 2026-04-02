package com.employeemanagement.employee.repository.impl;

import com.employeemanagement.employee.model.Department;
import com.employeemanagement.employee.repository.IDepartmentRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentRepository implements IDepartmentRepository {

	@Override
	public List<Department> findAllDepartments() {
		List<Department> departments = new ArrayList<>();
		String sql = "SELECT id, name FROM department";

		try (PreparedStatement preparedStatement = BaseRepository.getConnection()
				.prepareStatement(sql);
			 ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				Department department = Department.builder()
						.id(resultSet.getInt("id"))
						.name(resultSet.getString("name"))
						.build();
				departments.add(department);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return departments;
	}

	@Override
	public Department findDepartmentById(Integer id) {
		String sql = "SELECT id, name FROM department WHERE id = ?";

		try (PreparedStatement preparedStatement = BaseRepository.getConnection()
				.prepareStatement(sql)
		) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
					return Department.builder()
							.id(resultSet.getInt("id"))
							.name(resultSet.getString("name"))
							.build();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null; // Không tìm thấy trả về null
	}

	@Override
	public Department createDepartment(Department department) {

		String sql = "INSERT INTO department (name) VALUES (?)";

		try (PreparedStatement preparedStatement = BaseRepository.getConnection()
				.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
		) {

			preparedStatement.setString(1, department.getName());
			preparedStatement.executeUpdate();

			// Lấy ID vừa được tạo ra gán lại cho object
			try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
				if (rs.next()) {
					department.setId(rs.getInt(1));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return department;
	}

	@Override
	public Department updateDepartment(Integer id, Department updateDepartment) {
		String sql = "UPDATE department SET name = ? WHERE id = ?";

		try (PreparedStatement preparedStatement = BaseRepository.getConnection()
				.prepareStatement(sql)) {

			preparedStatement.setString(1, updateDepartment.getName());
			preparedStatement.setInt(2, id);

			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected > 0) {
				updateDepartment.setId(id); // Đảm bảo ID được giữ nguyên
				return updateDepartment;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null; // Không tìm thấy phòng ban để update
	}

	@Override
	public Department deleteDepartment(Integer id) {
		// Moi thông tin phòng ban lên trước khi xóa để return
		Department departmentToDelete = findDepartmentById(id);

		if (departmentToDelete == null) {
			return null; // ID không tồn tại
		}

		String sql = "DELETE FROM department WHERE id = ?";

		try (PreparedStatement preparedStatement = BaseRepository.getConnection()
				.prepareStatement(sql)) {

			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return departmentToDelete;
	}
}