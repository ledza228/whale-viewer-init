package com.ledza.cryptowhaleviewer.repository;

import com.ledza.cryptowhaleviewer.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT MAX(id) FROM transaction",nativeQuery = true)
    Long findMaxId();

}
