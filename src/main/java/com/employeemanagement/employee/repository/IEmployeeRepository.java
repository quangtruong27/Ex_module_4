package com.employeemanagement.employee.repository;

import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {

	@Query("""
      SELECT e FROM Employee e LEFT JOIN e.department d WHERE 
               (:#{#request.name} IS NULL OR LOWER(e.name) LIKE CONCAT('%', LOWER(:#{#request.name}), '%'))
               AND (:#{#request.dobFrom} IS NULL OR e.dob >= :#{#request.dobFrom})
               AND (:#{#request.dobTo} IS NULL OR e.dob <= :#{#request.dobTo})
               AND (:#{#request.gender} IS NULL OR e.gender = :#{#request.gender})
               AND (:#{#request.phone} IS NULL OR e.phone LIKE CONCAT('%', :#{#request.phone}, '%'))
               AND (:#{#request.departmentId} IS NULL OR e.department.id = :#{#request.departmentId})
               AND (
                     :#{#request.salaryRange} IS NULL OR
                     (
                           (:#{#request.salaryRange} = 'lt5' AND e.salary < 5000000)
                        OR (:#{#request.salaryRange} = '5-10' AND e.salary >= 5000000 AND e.salary < 10000000)   
                        OR (:#{#request.salaryRange} = '10-20' AND e.salary >= 10000000 AND e.salary < 20000000)
                        OR (:#{#request.salaryRange} = 'gt20' AND e.salary >= 20000000)
                     )
                  )
      """)
	Page<Employee> findByAttributes(@Param("request") EmployeeSearchRequest request, Pageable pageable);

}