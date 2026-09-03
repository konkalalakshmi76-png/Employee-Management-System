package com.example.employeemanagement.mapper;

import com.example.employeemanagement.dto.AddressRequestDTO;
import com.example.employeemanagement.dto.AddressResponseDTO;
import com.example.employeemanagement.dto.EmployeeRequestDTO;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.entity.Address;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequestDTO dto, Department department) {
        Employee employee = new Employee();
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setDesignation(dto.getDesignation());
        employee.setSalary(dto.getSalary());
        employee.setJoiningDate(dto.getJoiningDate());
        employee.setStatus(dto.getStatus());
        employee.setDepartment(department);

        if (dto.getAddress() != null) {
            employee.setAddress(toEntity(dto.getAddress()));
        }

        return employee;
    }

    public void updateEntity(Employee employee, EmployeeRequestDTO dto, Department department) {
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setDesignation(dto.getDesignation());
        employee.setSalary(dto.getSalary());
        employee.setJoiningDate(dto.getJoiningDate());
        employee.setStatus(dto.getStatus());
        employee.setDepartment(department);

        if (dto.getAddress() != null) {
            if (employee.getAddress() == null) {
                employee.setAddress(toEntity(dto.getAddress()));
            } else {
                updateEntity(employee.getAddress(), dto.getAddress());
            }
        }
    }

    public EmployeeResponseDTO toResponse(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setEmployeeCode(employee.getEmployeeCode());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDesignation(employee.getDesignation());
        dto.setSalary(employee.getSalary());
        dto.setJoiningDate(employee.getJoiningDate());
        dto.setStatus(employee.getStatus());

        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getDepartmentName());
        }

        if (employee.getAddress() != null) {
            dto.setAddress(toResponse(employee.getAddress()));
        }

        return dto;
    }

    public Address toEntity(AddressRequestDTO dto) {
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        address.setCountry(dto.getCountry());
        return address;
    }

    public void updateEntity(Address address, AddressRequestDTO dto) {
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        address.setCountry(dto.getCountry());
    }

    public AddressResponseDTO toResponse(Address address) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPincode(address.getPincode());
        dto.setCountry(address.getCountry());
        return dto;
    }
}
