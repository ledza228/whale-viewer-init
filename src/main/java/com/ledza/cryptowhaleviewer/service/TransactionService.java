package com.ledza.cryptowhaleviewer.service;

import com.ledza.cryptowhaleviewer.entity.Coin;
import com.ledza.cryptowhaleviewer.entity.OperationType;
import com.ledza.cryptowhaleviewer.entity.Transaction;
import com.ledza.cryptowhaleviewer.entity.TransactionRoute;
import com.ledza.cryptowhaleviewer.repository.CoinRepository;
import com.ledza.cryptowhaleviewer.repository.OperationTypeRepository;
import com.ledza.cryptowhaleviewer.repository.TransactionRepository;
import com.ledza.cryptowhaleviewer.repository.TransactionRouteRepository;
import com.ledza.cryptowhaleviewer.utils.TelegramRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private Long currentPostId;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionRouteRepository transactionRouteRepository;

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    @Autowired
    private ParsingService parsingService;

    public void addTransactions(List<Transaction> transactions){
        List<Coin> coins = coinRepository.findAll();
        List<OperationType> operationTypes = operationTypeRepository.findAll();
        List<TransactionRoute> transactionRoutes = transactionRouteRepository.findAll();


        System.out.println("Trying save in bd");

        for (Transaction transaction : transactions){
            Coin transCoin = transaction.getCoin();
            OperationType transType = transaction.getOperation();
            TransactionRoute transRoute = transaction.getRoute();

            int pos = coins.indexOf(transCoin);
            if (pos != -1){
                transCoin.setId(coins.get(pos).getId());
            }
            else {
                long len = coins.size();
                transCoin.setId(len+1);
                coins.add(transCoin);
            }
            pos = operationTypes.indexOf(transType);
            if (pos != -1){
                transType.setId(operationTypes.get(pos).getId());
            }
            else{
                long len = operationTypes.size();
                transType.setId(len+1);
                operationTypes.add(transType);
            }
            pos = transactionRoutes.indexOf(transRoute);
            if (pos != -1){
                transRoute.setId(transactionRoutes.get(pos).getId());
            }
            else {
                long len = transactionRoutes.size();
                transRoute.setId(len+1);
                transactionRoutes.add(transRoute);
            }
        }

        coinRepository.saveAll(transactions.stream().map(Transaction::getCoin).collect(Collectors.toList()));
        operationTypeRepository.saveAll(transactions.stream().map(Transaction::getOperation).collect(Collectors.toList()));
        transactionRouteRepository.saveAll(transactions.stream().map(Transaction::getRoute).collect(Collectors.toList()));

        transactionRepository.saveAll(transactions);
        System.out.println("Was saved in db");
    }

    @Scheduled(fixedDelay = 1000*60*20)
    public void findingTransactions() throws InterruptedException {
        initPostId();
        List<Transaction> transactions = new ArrayList<>();
        String post = nextPost();
        while (true) {
            Thread.sleep(100);
            try {
                if (parsingService.isExist(post)) {
                    Transaction transaction = parsingService.parse(post);
                    if (transaction == null) {
                        post = nextPost();
                        continue;
                    }
                    transaction.setId(currentPostId);
                    transactions.add(transaction);
                    if (transactions.size() > 30) {
                        addTransactions(transactions);
                        transactions.clear();
                    }
                    System.out.println(transaction);
                } else {
                    System.out.println("THERE IS NO SUCH POST YET");
                    currentPostId--;
                    break;
                }
            }
            catch (Exception e){
                System.out.println("WAS NULL POINTER EXCEPTION");
                Thread.sleep(10000);
                post = currentPost();
                continue;
            }
            post = nextPost();
        }

        addTransactions(transactions);
    }

    private String nextPost(){
        return TelegramRequestUtil.getPostText(++currentPostId);
    }

    private String currentPost(){
        return TelegramRequestUtil.getPostText(currentPostId);
    }

    public void initPostId(){
        Long maxId = transactionRepository.findMaxId();
        System.out.println("LAST TRANS: " + maxId);
        if (maxId != null) {
            this.currentPostId = maxId;
        }
    }


    @Value("${telegram.first.post}")
    public void setCurrentPostId(Long currentPostId) {
        this.currentPostId = currentPostId;
    }
}
