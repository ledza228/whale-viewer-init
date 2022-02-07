package com.ledza.cryptowhaleviewer.entity;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class TransactionRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String from_place;

    @NonNull
    private String to_place;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionRoute that = (TransactionRoute) o;

        if (!from_place.equals(that.from_place)) return false;
        return to_place.equals(that.to_place);
    }

    @Override
    public int hashCode() {
        int result = from_place.hashCode();
        result = 31 * result + to_place.hashCode();
        return result;
    }
}
