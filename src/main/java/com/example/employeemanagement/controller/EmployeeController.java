package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeRequestDTO;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @PostMapping("/employees")
    public ResponseEntity<Map<String, Object>> createEmployee(@Valid @RequestBody EmployeeRequestDTO request) {
        Employee employee = employeeService.createEmployee(request);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Employee created successfully");
        response.put("data", employeeMapper.toResponse(employee));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/employees")
    public ResponseEntity<Map<String, Object>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long department,
            @RequestParam(required = false) String designation) {

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employeePage;

        if (name != null && !name.isBlank()) {
            employeePage = employeeService.searchEmployees(name, pageable);
        } else if (department != null) {
            employeePage = employeeService.filterByDepartment(department, pageable);
        } else if (designation != null && !designation.isBlank()) {
            employeePage = employeeService.filterByDesignation(designation, pageable);
        } else {
            employeePage = employeeService.getAllEmployees(pageable);
        }

        List<EmployeeResponseDTO> employees = employeePage.getContent().stream()
                .map(employeeMapper::toResponse)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Employees retrieved successfully");
        response.put("data", employees);
        response.put("page", employeePage.getNumber());
        response.put("size", employeePage.getSize());
        response.put("totalElements", employeePage.getTotalElements());
        response.put("totalPages", employeePage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Map<String, Object>> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Employee retrieved successfully");
        response.put("data", employeeMapper.toResponse(employee));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO request) {
        Employee employee = employeeService.updateEmployee(id, request);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Employee updated successfully");
        response.put("data", employeeMapper.toResponse(employee));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Employee deleted successfully");
        return ResponseEntity.ok(response);
    }
}
