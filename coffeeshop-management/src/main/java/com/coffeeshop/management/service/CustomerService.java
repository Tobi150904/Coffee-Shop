package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.CustomerDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(Integer id) throws ResourceNotFoundException;
    CustomerDTO updateCustomer(Integer id, CustomerDTO customerDTO) throws ResourceNotFoundException;
    void deleteCustomer(Integer id) throws ResourceNotFoundException;
    CustomerDTO getCustomerByPhoneNumber(String phoneNumber) throws ResourceNotFoundException;
}
