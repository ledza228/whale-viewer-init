package com.ledza.cryptowhaleviewer.repository;

import com.ledza.cryptowhaleviewer.entity.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {
}
