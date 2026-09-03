package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.AddressRequestDTO;
import com.example.employeemanagement.dto.AddressResponseDTO;
import com.example.employeemanagement.entity.Address;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.AddressRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AddressRepository addressRepository;
    private final EmployeeMapper employeeMapper;

    public AddressController(AddressRepository addressRepository, EmployeeMapper employeeMapper) {
        this.addressRepository = addressRepository;
        this.employeeMapper = employeeMapper;
    }

    @PostMapping("/addresses")
    public ResponseEntity<Map<String, Object>> createAddress(@Valid @RequestBody AddressRequestDTO request) {
        Address address = employeeMapper.toEntity(request);
        Address savedAddress = addressRepository.save(address);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Address created successfully");
        response.put("data", employeeMapper.toResponse(savedAddress));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/addresses/{id}")
    public ResponseEntity<Map<String, Object>> getAddressById(@PathVariable Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Address retrieved successfully");
        response.put("data", employeeMapper.toResponse(address));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<Map<String, Object>> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequestDTO request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
        employeeMapper.updateEntity(address, request);
        Address updatedAddress = addressRepository.save(address);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Address updated successfully");
        response.put("data", employeeMapper.toResponse(updatedAddress));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Map<String, Object>> deleteAddress(@PathVariable Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
        addressRepository.delete(address);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Address deleted successfully");
        return ResponseEntity.ok(response);
    }
}
