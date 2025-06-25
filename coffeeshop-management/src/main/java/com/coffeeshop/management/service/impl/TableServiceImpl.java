package com.coffeeshop.management.service.impl;

import com.coffeeshop.management.dto.TableDTO;
import com.coffeeshop.management.model.TableEntity;
import com.coffeeshop.management.repository.TableRepository;
import com.coffeeshop.management.service.TableService;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;

    @Autowired
    public TableServiceImpl(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public TableDTO createTable(TableDTO tableDTO) {
        validateTableDTO(tableDTO);
        
        TableEntity table = new TableEntity();
        BeanUtils.copyProperties(tableDTO, table);
        table.setStatus(TableEntity.TableStatus.valueOf(tableDTO.getStatus().toUpperCase()));
        
        TableEntity savedTable = tableRepository.save(table);
        return convertToDto(savedTable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TableDTO> getAllTables() {
        return tableRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TableDTO getTableById(Integer id) throws ResourceNotFoundException {
        return tableRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
    }

    @Override
    public TableDTO updateTable(Integer id, TableDTO tableDTO) throws ResourceNotFoundException {
        validateTableDTO(tableDTO);
        
        return tableRepository.findById(id).map(existingTable -> {
            BeanUtils.copyProperties(tableDTO, existingTable, "tableId");
            existingTable.setStatus(TableEntity.TableStatus.valueOf(tableDTO.getStatus().toUpperCase()));
            TableEntity updatedTable = tableRepository.save(existingTable);
            return convertToDto(updatedTable);
        }).orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
    }

    @Override
    public void deleteTable(Integer id) throws ResourceNotFoundException {
        if (!tableRepository.existsById(id)) {
            throw new ResourceNotFoundException("Table not found with id: " + id);
        }
        tableRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public TableDTO getTableByTableNumber(String tableNumber) throws ResourceNotFoundException {
        return tableRepository.findByTableNumber(tableNumber)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with number: " + tableNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TableDTO> getTablesByStatus(String status) {
        return tableRepository.findByStatus(TableEntity.TableStatus.valueOf(status.toUpperCase())).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TableDTO convertToDto(TableEntity table) {
        TableDTO tableDTO = new TableDTO();
        BeanUtils.copyProperties(table, tableDTO);
        tableDTO.setStatus(table.getStatus().name());
        return tableDTO;
    }

    private void validateTableDTO(TableDTO tableDTO) {
        if (tableDTO.getTableNumber() == null || tableDTO.getTableNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Table number cannot be empty");
        }
        if (tableDTO.getStatus() == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
    }
}