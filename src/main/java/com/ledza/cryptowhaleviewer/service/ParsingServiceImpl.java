package com.ledza.cryptowhaleviewer.service;

import com.ledza.cryptowhaleviewer.entity.Coin;
import com.ledza.cryptowhaleviewer.entity.OperationType;
import com.ledza.cryptowhaleviewer.entity.Transaction;
import com.ledza.cryptowhaleviewer.entity.TransactionRoute;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParsingServiceImpl implements ParsingService{

    @Override
    public Transaction parse(String htmlText) {
        Document doc = Jsoup.parse(htmlText);
        Element textElement = doc.select("div.tgme_widget_message_text").first();
        Element dateElement = doc.select("time").first();

        if (!textElement.text().contains("TX - link")){
            return null;
        }
        String htmlMainText = textElement.select("pre").html();
        int mainTextStart = htmlMainText.lastIndexOf("</i>");
        mainTextStart = mainTextStart == -1 ? 0 : mainTextStart + 5;
        int mainTextEnd = htmlMainText.indexOf("<br");
        String text = mainTextEnd == -1 ?
                htmlMainText.substring(mainTextStart):
                htmlMainText.substring(mainTextStart,mainTextEnd);

        Transaction transaction = parseCleanTransaction(text);
        transaction.setDate(parseDate(dateElement.attr("datetime")));

        return transaction;
    }

    /*
    Example of string:
        10,156 BTC ($374,852,300) transfered from Unknown to Unknown
     */
    private Transaction parseCleanTransaction(String text){
        System.out.println(text);

        int firstSpacePos = text.indexOf(" ");
        int openBracketPos = text.indexOf("($");
        int closeBracketPos = text.indexOf(")");
        int fromPos = text.indexOf("from");
        int toPos = text.indexOf("to");

        String amntCoinsStr = text.substring(0,firstSpacePos).strip();
        String coinType = text.substring(firstSpacePos,openBracketPos).strip();
        String priceUSDStr = text.substring(openBracketPos+2,closeBracketPos);
        String operation = text.substring(closeBracketPos+1,fromPos).strip();
        String fromPlace = text.substring(fromPos+4,toPos).strip();
        String toPlace = text.substring(toPos+2).strip();

        Long amnCoins = strToLong(amntCoinsStr);
        Long priceUSD = strToLong(priceUSDStr);

        Transaction transaction = new Transaction();
        transaction.setAmountCoins(amnCoins);
        transaction.setPriceUSD(priceUSD);
        transaction.setOperation(new OperationType(operation));
        transaction.setCoin(new Coin(coinType));
        transaction.setRoute(new TransactionRoute(toPlace,fromPlace));

        return transaction;
    }

    private Long strToLong(String str){
        return Long.parseLong(str.replace(",",""));
    }

    private LocalDateTime parseDate(String timeStr){
        return LocalDateTime.parse(timeStr.substring(0,timeStr.indexOf("+")));
    }

    @Override
    public boolean isExist(String html) {return !html.contains("Post not found");}

}
