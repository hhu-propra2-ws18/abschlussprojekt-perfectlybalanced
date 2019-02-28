package de.hhu.abschlussprojektverleihplattform.service.propay.adapter;

import de.hhu.abschlussprojektverleihplattform.service.propay.exceptions.ProPayAccountNotExistException;
import de.hhu.abschlussprojektverleihplattform.service.propay.interfaces.IProPayAdapter;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static de.hhu.abschlussprojektverleihplattform.service.propay.ProPayUtil.makeHttpRequestWithParameter;

@Component
public class ProPayAdapter implements IProPayAdapter {

    @Value("${propaybaseurl}")
    private String baseurl;

    @Override
    public Account getAccount(String username)
            throws HttpClientErrorException,ProPayAccountNotExistException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = baseurl + "account/" + username;

            return restTemplate.getForObject(url, Account.class);
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().is4xxClientError()){
                throw new ProPayAccountNotExistException();
            } else {
                throw ex;
            }
        }
    }

    @Override
    public Account createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(String username, long amount)
            throws HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity request = makeHttpRequestWithParameter("amount",amount+"");

        String url = baseurl + "account/"+username;
        ResponseEntity<Account> response = restTemplate.postForEntity(url, request, Account.class);
        return response.getBody();
    }

    @Override
    public void makePayment(String sourceUsername, String destinationUsername, long amount)
            throws HttpClientErrorException {

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity request = makeHttpRequestWithParameter("amount",""+amount);

        String url = baseurl + "account/" + sourceUsername+"/transfer/"+destinationUsername;
        ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);

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
        RestTemplate restTemplate = new RestTemplate();

        String method_url = "reservation/release/"+sourceUsername;
        String url = baseurl + method_url;

        HttpEntity request=makeHttpRequestWithParameter("reservationId",""+reservationId);

        ResponseEntity<Account> response
                = restTemplate.postForEntity(
                URI.create(url),request,Account.class
        );

        if(response.getStatusCode().is4xxClientError()){
            throw new Exception("cannot make reservation");
        }
        return response.getBody();
    }

    @Override
    public Account punishReservation(
            String sourceUsername,
            long reservationId)
        throws Exception {

        RestTemplate restTemplate = new RestTemplate();

        String method_url = "reservation/punish/"+sourceUsername;
        String url = baseurl + method_url;

        System.out.println("url:"+url);

        HttpEntity request=
                makeHttpRequestWithParameter("reservationId",""+reservationId);

        ResponseEntity<Account> response =
                restTemplate.postForEntity(URI.create(url),request,Account.class);

        return response.getBody();
    }

}
