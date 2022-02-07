package com.ledza.cryptowhaleviewer.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Transaction {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "price_usd")
    private Long priceUSD;

    @Column(name = "coin_amount")
    private Long amountCoins;

    @Column(name = "date_time")
    private LocalDateTime date;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "operation_id")
    private OperationType operation;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id")
    private TransactionRoute route;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "coin_id")
    private Coin coin;


}
