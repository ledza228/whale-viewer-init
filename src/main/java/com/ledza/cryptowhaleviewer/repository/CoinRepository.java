package com.ledza.cryptowhaleviewer.repository;

import com.ledza.cryptowhaleviewer.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {
}
