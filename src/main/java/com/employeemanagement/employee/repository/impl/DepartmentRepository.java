package com.employeemanagement.employee.repository.impl;

import com.employeemanagement.employee.entity.Department;
import com.employeemanagement.employee.repository.IDepartmentRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentRepository implements IDepartmentRepository {

	@Override
	public List<Department> findAllDepartments() {
		Session session = ConnectionUtil.sessionFactory.openSession();
		List<Department> departments = session.createQuery("FROM Department").getResultList();
		session.close();
		return departments;
	}

	@Override
	public Department findDepartmentById(Integer id) {
		Session session = ConnectionUtil.sessionFactory.openSession();
		Department department = (Department) session.createQuery("FROM Department WHERE id = :id").
				setParameter("id", id)
				.getSingleResult();
		session.close();
		return department;
	}

	@Override
	public Department createDepartment(Department department) {
		Session session = ConnectionUtil.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(department);

		transaction.commit();
		session.close();

		return department;
	}

	@Override
	public Department updateDepartment(Integer id, Department updateDepartment) {
		Session session = ConnectionUtil.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		// Tìm phòng ban cần sửa
		Department existingDept = session.get(Department.class, id);

		// Nếu tìm thấy thì cập nhật tên và lưu lại
		if (existingDept != null) {
			existingDept.setName(updateDepartment.getName());

			session.update(existingDept);
			transaction.commit();

			session.close();
			return existingDept;
		}

		session.close();
		return null; // Không tìm thấy để sửa
	}
	@Override
	public Department deleteDepartment(Integer id) {
		Session session = ConnectionUtil.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Department department = session.get(Department.class, id);

		if(department != null) {
			session.delete(department);
			transaction.commit();

			session.close();
			return department;
		}
		session.close();
		return null;
	}
}