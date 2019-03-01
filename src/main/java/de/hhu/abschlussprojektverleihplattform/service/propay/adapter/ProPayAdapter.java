package de.hhu.abschlussprojektverleihplattform.service.propay.adapter;

import de.hhu.abschlussprojektverleihplattform.model.TransactionEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.TransactionRepository;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.service.propay.exceptions.ProPayAccountNotExistException;
import de.hhu.abschlussprojektverleihplattform.service.propay.exceptions.ProPayTimeoutException;
import de.hhu.abschlussprojektverleihplattform.service.propay.interfaces.IProPayAdapter;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;

import static de.hhu.abschlussprojektverleihplattform.service.propay.ProPayUtil.makeHttpRequestWithParameter;

@Component
public class ProPayAdapter implements IProPayAdapter {

    @Value("${propaybaseurl}")
    public String baseurl;

    private final UserService userService;
    private final TransactionRepository transactionRepository;

    public ProPayAdapter(UserService userService, TransactionRepository transactionRepository) {
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }

    private void handleHttClientErrorException(HttpClientErrorException ex) throws Exception{

        if(ex.getStatusCode().value()==500
                || ex.getStatusCode().value()==503
                || ex.getStatusCode().value()==598
                || ex.getStatusCode().value()==504
                || ex.getStatusCode().value()==599){
            throw new ProPayTimeoutException("Propay is not available at this time");
        }
        throw ex;
    }

    @Override
    public Account getAccount(String username)
            throws Exception{
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = baseurl + "account/" + username;

            return restTemplate.getForObject(url, Account.class);
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().value()==404){
                throw new ProPayAccountNotExistException("The requested accound does not exist");
            }else {
                handleHttClientErrorException(ex);
                return null;
            }
        }
    }

    @Override
    public Account createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(String username, long amount)
            throws Exception {
        UserEntity user = userService.findByUsername(username);

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpEntity request = makeHttpRequestWithParameter("amount", amount + "");

            String url = baseurl + "account/" + username;
            ResponseEntity<Account> response =
                    restTemplate.postForEntity(url, request, Account.class);

            if (amount != 0){
                TransactionEntity transaction = new TransactionEntity(user,
                        user,
                        (int)amount,
                        new Timestamp(System.currentTimeMillis()));
                transactionRepository.addTransaction(transaction);
            }

            return response.getBody();
        }catch (HttpClientErrorException ex){
            handleHttClientErrorException(ex);
            return null;
        }
    }

    @Override
    public void makePayment(String sourceUsername, String destinationUsername, long amount)
            throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpEntity request =
                    makeHttpRequestWithParameter("amount", "" + amount);

            String url = baseurl
                    + "account/"
                    + sourceUsername
                    + "/transfer/"
                    + destinationUsername;
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);
            System.out.println(response);
        }catch (HttpClientErrorException ex){
            handleHttClientErrorException(ex);
        }
    }

    @Override
    public Reservation makeReservation(
            String sourceUsername,
            String destinationUsername,
            long amount)
            throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String method_url = "reservation/reserve/"+sourceUsername+"/"+destinationUsername;
        String url = baseurl + method_url;

        HttpEntity request = makeHttpRequestWithParameter("amount",""+amount);

        ResponseEntity<Reservation> reservation
                = restTemplate.postForEntity(
                URI.create(url),request,Reservation.class
        );

        if(reservation.getStatusCode().is4xxClientError()){
            throw new Exception("cannot make reservation");
        }
        return reservation.getBody();
    }

    @Override
    public Account releaseReservation(String sourceUsername, long reservationId) throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String method_url = "reservation/release/" + sourceUsername;
            String url = baseurl + method_url;

            HttpEntity request = makeHttpRequestWithParameter("reservationId", "" + reservationId);

            ResponseEntity<Account> response
                    = restTemplate.postForEntity(
                    URI.create(url), request, Account.class
            );


            return response.getBody();
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().is4xxClientError()){
                throw new Exception("cannot make reservation");
            }else {
                handleHttClientErrorException(ex);
                return null;
            }
        }


    }

    @Override
    public Account punishReservation(
            String sourceUsername,
            long reservationId)
        throws Exception {
        try {

            RestTemplate restTemplate = new RestTemplate();

            String method_url = "reservation/punish/" + sourceUsername;
            String url = baseurl + method_url;

            System.out.println("url:" + url);

            HttpEntity request =
                    makeHttpRequestWithParameter("reservationId", "" + reservationId);

            ResponseEntity<Account> response =
                    restTemplate.postForEntity(URI.create(url), request, Account.class);

            return response.getBody();
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().is4xxClientError()){
                throw new Exception("cannot make reservation");
            }else {
                handleHttClientErrorException(ex);
                return null;
            }
        }
    }

}
