package com.coffeeshop.management.repository;

import com.coffeeshop.management.model.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {
    Optional<TableEntity> findByTableNumber(String tableNumber);
    List<TableEntity> findByStatus(TableEntity.TableStatus status);
}