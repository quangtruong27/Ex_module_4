package com.employeemanagement.employee.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
	 Integer id;
	 String name;
	 LocalDate dob;
	 Gender gender;
	 Double salary;
	 String phone;

}
