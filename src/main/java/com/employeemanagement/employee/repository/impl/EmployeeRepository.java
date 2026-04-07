package com.employeemanagement.employee.repository.impl;

import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.entity.Department;
import com.employeemanagement.employee.entity.Employee;
import com.employeemanagement.employee.repository.IEmployeeRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.*;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeRepository implements IEmployeeRepository {

	@Override
	public List<Employee> findAll(EmployeeSearchRequest employeeSearchRequest) {
		Session session = ConnectionUtil.sessionFactory.openSession();

		String hql = "FROM Employee e LEFT JOIN FETCH e.department WHERE "
				+ "(:name IS NULL OR lower(e.name) LIKE CONCAT('%', :name, '%')) " // // Nếu name = null thì bỏ qua, ngược lại tìm LIKE
				+ " AND (:dobFrom IS NULL OR e.dob >= :dobFrom) "
				+ " AND (:dobTo IS NULL OR e.dob <= :dobTo) "
				+ " AND (:gender IS NULL OR e.gender = :gender) "
				+ " AND (:phone IS NULL OR e.phone LIKE CONCAT('%', :phone, '%')) "
				+ " AND (:departmentId IS NULL OR e.department.id = :departmentId)";

		if (employeeSearchRequest.getSalaryRange() != null) {
			hql += " AND ("; // Mo dau dk salary range
			switch (employeeSearchRequest.getSalaryRange()) {
				case "lt5":
					hql += "e.salary < 5000000";
					break;
				case "5-10":
					hql += "e.salary => 5000000 AND e.salary < 10000000";
					break;
				case "10-20":
					hql += "e.salary => 10000000 AND e.salary < 20000000";
					break;
				case "gt20":
					hql += "e.salary > 20000000";
					break;
			}
			hql += ")"; //close dk salary range
		}
		Query<Employee> query = session.createQuery(hql, Employee.class);  // Tạo query từ HQL

		// Gán parameter từ request vào query
		query.setParameter("name", employeeSearchRequest.getName());
		query.setParameter("dobFrom", employeeSearchRequest.getDobFrom());
		query.setParameter("dobTo", employeeSearchRequest.getDobTo());
		query.setParameter("gender", employeeSearchRequest.getGender());
		query.setParameter("phone", employeeSearchRequest.getPhone());
		query.setParameter("departmentId", employeeSearchRequest.getDepartmentId());

		// Thực thi query và trả về list
		return query.getResultList();
	}

	@Override
	public Optional<Employee> findById(Integer id) {
		Session session = ConnectionUtil.sessionFactory.openSession();
		Employee employee = (Employee) session.createQuery("FROM Employee WHERE id = :id").
				setParameter("id", id)
				.uniqueResult();
		session.close();
		return Optional.ofNullable(employee);
	}

	@Override
	public Employee createEmployee(Employee employee) {
		try (Session session = ConnectionUtil.sessionFactory.openSession()) {
			Transaction transaction = session.beginTransaction();

			try {

				if (employee.getDepartment() != null && employee.getDepartment().getId() != null) { // Nếu có department id thì lấy department từ DB
					Department department = session.find(Department.class, employee.getDepartment().getId());
					employee.setDepartment(department); // gán department đầy đủ cho employy
				}

				session.saveOrUpdate(employee); // Lưu hoặc update
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback(); // rollback neu co loi
				}
				throw new RuntimeException(e);
			}
		}
		return employee;
	}

	@Override
	public Employee updateEmployee(Integer id, Employee updatedEmployee) {
		try (Session session = ConnectionUtil.sessionFactory.openSession()) {
			Transaction transaction = session.beginTransaction();

			Employee existingEmployee = session.get(Employee.class, id); // Tìm nhân viên cũ trong DB
			if (existingEmployee == null) {
				return null;
			}

			//Cập nhật các trường dữ liệu
			existingEmployee.setName(updatedEmployee.getName());
			existingEmployee.setDob(updatedEmployee.getDob());
			existingEmployee.setGender(updatedEmployee.getGender());
			existingEmployee.setSalary(updatedEmployee.getSalary());
			existingEmployee.setPhone(updatedEmployee.getPhone());

			if (updatedEmployee.getDepartment() != null && updatedEmployee.getDepartment().getId() != null) {
				Department department = session.get(Department.class, updatedEmployee.getDepartment().getId());
				existingEmployee.setDepartment(department);
			}

			// Lưu
			session.update(existingEmployee);
			transaction.commit();

			return existingEmployee;
		}
	}

	@Override
	public Employee deleteEmployee(Integer id) {
		Session session = ConnectionUtil.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Employee employee = session.get(Employee.class, id); //Tìm nhân viên xem có tồn tại không

		if (employee != null) { // 	Nếu tìm thấy thì tiến hành xóa

			session.delete(employee);
			transaction.commit(); // Xác nhận lưu thay đổi xuống Database

			session.close();
			return employee;
		}
		session.close();
		return null;
	}
}