package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    public static final String API_BASE_URL = "http://localhost:8080/api/account/";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {this.authToken = authToken;}

    public BigDecimal getBalance(long userId){
        BigDecimal balance = null;
        try{
            ResponseEntity<BigDecimal> response = restTemplate.exchange(
                    API_BASE_URL + "user/" + userId + "/balance" ,
                    HttpMethod.GET, makeAuthEntity(), BigDecimal.class);

            balance = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public Account getAccountByUserId(long userId){

        Account account = null;
        try{
            ResponseEntity<Account> response = restTemplate.exchange(
                    API_BASE_URL + "user/" + userId,
                    HttpMethod.GET, makeAuthEntity(), Account.class);

            account = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;

    }

    public void sendTeBucks(long senderId, long receiverId, BigDecimal amount){
        try{
            ResponseEntity<Void> response = restTemplate.exchange(
                    API_BASE_URL + "send/" + senderId + "/" + receiverId + "/" + amount ,
                    HttpMethod.POST, makeAuthEntity(), Void.class);
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
