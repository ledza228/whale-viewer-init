package com.ledza.cryptowhaleviewer.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TelegramRequestUtil {

    private static String startUrl;
    private static String endUrl;

    private static RestTemplate restTemplate;

    public static String getPostText(Long id){
        String url = createUrl(id);
        return restTemplate.getForObject(url,String.class);
    }

    static private String createUrl(Long id){
        return startUrl + id + endUrl;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        TelegramRequestUtil.restTemplate = restTemplate;
    }

    @Value("${url.telegram.post.start}")
    public void setStartUrl(String startUrl) {
        TelegramRequestUtil.startUrl = startUrl;
    }

    @Value("${url.telegram.post.end}")
    public void setEndUrl(String endUrl) {
        TelegramRequestUtil.endUrl = endUrl;
    }
}
