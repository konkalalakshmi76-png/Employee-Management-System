package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequestDTO;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private com.example.employeemanagement.mapper.EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setDepartmentName("IT");
    }

    @Test
    void createEmployee_shouldReturnSavedEmployee() {
        EmployeeRequestDTO request = new EmployeeRequestDTO();
        request.setEmployeeCode("EMP-1001");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPhone("9876543210");
        request.setDesignation("Developer");
        request.setSalary(new BigDecimal("75000.00"));
        request.setJoiningDate(LocalDate.of(2024, 1, 10));
        request.setStatus("ACTIVE");
        request.setDepartmentId(1L);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(employeeRepository.existsByEmployeeCode(request.getEmployeeCode())).thenReturn(false);
        when(employeeMapper.toEntity(any(), any())).thenAnswer(invocation -> {
            Employee employee = new Employee();
            employee.setEmployeeCode(request.getEmployeeCode());
            employee.setFirstName(request.getFirstName());
            employee.setLastName(request.getLastName());
            employee.setEmail(request.getEmail());
            employee.setPhone(request.getPhone());
            employee.setDesignation(request.getDesignation());
            employee.setSalary(request.getSalary());
            employee.setJoiningDate(request.getJoiningDate());
            employee.setStatus(request.getStatus());
            employee.setDepartment(department);
            return employee;
        });
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee saved = employeeService.createEmployee(request);

        assertThat(saved).isNotNull();
        assertThat(saved.getEmployeeCode()).isEqualTo("EMP-1001");
        assertThat(saved.getDepartment()).isEqualTo(department);
    }
}
