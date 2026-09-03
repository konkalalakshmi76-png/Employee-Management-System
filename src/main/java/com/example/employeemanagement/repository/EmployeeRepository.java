package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Override
    @EntityGraph(attributePaths = {"department", "address"})
    java.util.Optional<Employee> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"department", "address"})
    Page<Employee> findAll(Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByEmployeeCode(String employeeCode);

    @Query("SELECT e FROM Employee e WHERE e.department = :department")
    List<Employee> findByDepartment(@Param("department") Department department);

    @EntityGraph(attributePaths = {"department", "address"})
    Page<Employee> findByDepartment(Department department, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    @EntityGraph(attributePaths = {"department", "address"})
    Page<Employee> searchByName(@Param("name") String name, Pageable pageable);

    @EntityGraph(attributePaths = {"department", "address"})
    Page<Employee> findByDesignationContainingIgnoreCase(String designation, Pageable pageable);

    List<Employee> findByDepartmentId(Long departmentId);
}
