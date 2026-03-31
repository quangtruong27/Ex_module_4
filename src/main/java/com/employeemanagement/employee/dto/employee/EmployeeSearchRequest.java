package com.employeemanagement.employee.dto.employee;

import com.employeemanagement.employee.model.Gender;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class EmployeeSearchRequest {
	String name;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	LocalDate dobFrom;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	LocalDate dobTo;

	Gender gender;

	String salaryRange;

	String phone;

	Integer departmentId;
}
