package com.ledza.cryptowhaleviewer.repository;

import com.ledza.cryptowhaleviewer.entity.TransactionRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRouteRepository extends JpaRepository<TransactionRoute, Long> {
}
