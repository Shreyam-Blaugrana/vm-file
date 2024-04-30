package com.shreyam.upload.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shreyam.upload.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	

}
