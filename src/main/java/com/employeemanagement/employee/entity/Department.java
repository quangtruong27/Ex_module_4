package com.employeemanagement.employee.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String name;
}
