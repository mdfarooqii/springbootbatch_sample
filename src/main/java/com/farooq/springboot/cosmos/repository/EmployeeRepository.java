package com.farooq.springboot.cosmos.repository;

import com.farooq.springboot.cosmos.domain.Employee;
import com.microsoft.azure.spring.data.cosmosdb.repository.DocumentDbRepository;
import org.springframework.stereotype.Repository;
import com.microsoft.azure.spring.data.cosmosdb.repository.DocumentDbRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository
    extends DocumentDbRepository<Employee, String>
{ }

