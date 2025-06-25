package com.coffeeshop.management.service.impl;

import com.coffeeshop.management.dto.LoyaltyTransactionDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import com.coffeeshop.management.model.LoyaltyTransaction;
import com.coffeeshop.management.repository.LoyaltyTransactionRepository;
import com.coffeeshop.management.service.LoyaltyTransactionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoyaltyTransactionServiceImpl implements LoyaltyTransactionService {
    @Autowired
    private LoyaltyTransactionRepository loyaltyTransactionRepository;

    @Override
    public LoyaltyTransactionDTO createLoyaltyTransaction(LoyaltyTransactionDTO dto) {
        LoyaltyTransaction entity = new LoyaltyTransaction();
        BeanUtils.copyProperties(dto, entity);
        LoyaltyTransaction saved = loyaltyTransactionRepository.save(entity);
        return convertToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoyaltyTransactionDTO> getAllLoyaltyTransactions() {
        return loyaltyTransactionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LoyaltyTransactionDTO getLoyaltyTransactionById(Integer id) throws ResourceNotFoundException {
        return loyaltyTransactionRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyTransaction not found with id: " + id));
    }

    @Override
    public void deleteLoyaltyTransaction(Integer id) throws ResourceNotFoundException {
        if (!loyaltyTransactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("LoyaltyTransaction not found with id: " + id);
        }
        loyaltyTransactionRepository.deleteById(id);
    }

    private LoyaltyTransactionDTO convertToDto(LoyaltyTransaction entity) {
        LoyaltyTransactionDTO dto = new LoyaltyTransactionDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
