package com.coffeeshop.management.service.impl;

import com.coffeeshop.management.dto.CustomerDTO;
import com.coffeeshop.management.model.Customer;
import com.coffeeshop.management.repository.CustomerRepository;
import com.coffeeshop.management.service.CustomerService;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        validateCustomerDTO(customerDTO);
        
        if (customerRepository.existsByPhoneNumber(customerDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Integer id) throws ResourceNotFoundException {
        return customerRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public CustomerDTO updateCustomer(Integer id, CustomerDTO customerDTO) throws ResourceNotFoundException {
        validateCustomerDTO(customerDTO);
        
        return customerRepository.findById(id).map(existingCustomer -> {
            if (!existingCustomer.getPhoneNumber().equals(customerDTO.getPhoneNumber()) &&
                customerRepository.existsByPhoneNumber(customerDTO.getPhoneNumber())) {
                throw new IllegalArgumentException("Phone number already exists");
            }
            
            BeanUtils.copyProperties(customerDTO, existingCustomer, "customerId", "createdAt");
            Customer updatedCustomer = customerRepository.save(existingCustomer);
            return convertToDto(updatedCustomer);
        }).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public void deleteCustomer(Integer id) throws ResourceNotFoundException {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerByPhoneNumber(String phoneNumber) throws ResourceNotFoundException {
        return customerRepository.findByPhoneNumber(phoneNumber)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with phone number: " + phoneNumber));
    }

    private CustomerDTO convertToDto(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    private void validateCustomerDTO(CustomerDTO customerDTO) {
        if (customerDTO.getFullName() == null || customerDTO.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        if (customerDTO.getPhoneNumber() == null || customerDTO.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
    }
}
