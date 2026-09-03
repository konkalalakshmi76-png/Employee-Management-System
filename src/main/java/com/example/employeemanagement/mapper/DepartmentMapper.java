package com.example.employeemanagement.mapper;

import com.example.employeemanagement.dto.DepartmentRequestDTO;
import com.example.employeemanagement.dto.DepartmentResponseDTO;
import com.example.employeemanagement.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public Department toEntity(DepartmentRequestDTO dto) {
        Department department = new Department();
        department.setDepartmentName(dto.getDepartmentName());
        department.setDescription(dto.getDescription());
        department.setLocation(dto.getLocation());
        return department;
    }

    public void updateEntity(Department department, DepartmentRequestDTO dto) {
        department.setDepartmentName(dto.getDepartmentName());
        department.setDescription(dto.getDescription());
        department.setLocation(dto.getLocation());
    }

    public DepartmentResponseDTO toResponse(Department department) {
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(department.getId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setDescription(department.getDescription());
        dto.setLocation(department.getLocation());
        return dto;
    }
}
