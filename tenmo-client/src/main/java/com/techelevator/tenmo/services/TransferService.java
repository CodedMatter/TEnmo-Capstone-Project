package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

    public static final String API_BASE_URL = "http://localhost:8080/api/transfer/";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {this.authToken = authToken;}

    public Transfer[] getAllTransfers() {
        Transfer[] allTransfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            allTransfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        } return allTransfers;
    }

    public Transfer createTransfer(Transfer transfer){
        Transfer transferCreated = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL, HttpMethod.POST, makeTransferEntity(transfer), Transfer.class);
            transferCreated = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferCreated;
//        Transfer returnedTransfer = null;
//        returnedTransfer = restTemplate.postForObject(API_BASE_URL,
//                makeTransferEntity(transfer),Transfer.class);
//        return returnedTransfer;
    }

    public Transfer[] getPendingTransfers(Long id){
        Transfer[] pendingTransfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + "/pending/" + id, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            pendingTransfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        } return pendingTransfers;
    }


    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }


}
