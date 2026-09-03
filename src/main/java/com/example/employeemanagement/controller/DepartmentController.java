package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.DepartmentRequestDTO;
import com.example.employeemanagement.dto.DepartmentResponseDTO;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.mapper.DepartmentMapper;
import com.example.employeemanagement.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    public DepartmentController(DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @PostMapping("/departments")
    public ResponseEntity<Map<String, Object>> createDepartment(@Valid @RequestBody DepartmentRequestDTO request) {
        Department department = departmentService.createDepartment(request);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Department created successfully");
        response.put("data", departmentMapper.toResponse(department));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/departments")
    public ResponseEntity<Map<String, Object>> getAllDepartments() {
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments().stream()
                .map(departmentMapper::toResponse)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Departments retrieved successfully");
        response.put("data", departments);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<Map<String, Object>> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Department retrieved successfully");
        response.put("data", departmentMapper.toResponse(department));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<Map<String, Object>> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentRequestDTO request) {
        Department department = departmentService.updateDepartment(id, request);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Department updated successfully");
        response.put("data", departmentMapper.toResponse(department));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Map<String, Object>> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Department deleted successfully");
        return ResponseEntity.ok(response);
    }
}
