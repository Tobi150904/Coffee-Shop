package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.LoyaltyTransactionDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import java.util.List;

public interface LoyaltyTransactionService {
    LoyaltyTransactionDTO createLoyaltyTransaction(LoyaltyTransactionDTO dto);
    List<LoyaltyTransactionDTO> getAllLoyaltyTransactions();
    LoyaltyTransactionDTO getLoyaltyTransactionById(Integer id) throws ResourceNotFoundException;
    void deleteLoyaltyTransaction(Integer id) throws ResourceNotFoundException;
}
