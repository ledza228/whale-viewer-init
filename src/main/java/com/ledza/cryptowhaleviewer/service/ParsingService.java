package com.ledza.cryptowhaleviewer.service;

import com.ledza.cryptowhaleviewer.entity.Transaction;
import org.springframework.stereotype.Service;

public interface ParsingService {

    public Transaction parse(String html);

    public boolean isExist(String html);

}
