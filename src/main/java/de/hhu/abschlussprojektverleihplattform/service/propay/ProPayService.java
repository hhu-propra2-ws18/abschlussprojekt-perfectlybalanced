package de.hhu.abschlussprojektverleihplattform.service.propay;

import static de.hhu.abschlussprojektverleihplattform.service.propay.ProPayUtils.make_new_user;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.propay.exceptions.ProPayAccountNotExistException;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;
import java.net.URI;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProPayService implements IProPayService, IPaymentService {

    public static final String baseurl = "http://propra-propay.herokuapp.com/";

    private static ProPayService instance=null;

    //jens said we should use dependency injection
    /*
    public synchronized static ProPayService getInstance(){
        if(instance==null){
            instance=new ProPayService();
        }
        return instance;
    }
    */

    private ProPayService(){}

    // ---- implement circumvent() to circumvent inconvenience in live propay api ------

    public void circumventForAccount(String username){
        try {
            //give our account 1 Euro
            changeUserBalanceBy(username, 1);

            //put that amount away to throwaway user
            String user1 = make_new_user();
            createAccountIfNotExists(user1);
            makePayment(username,user1,1);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    // ------------- implement propay interface methods ------------------

    @Override
    public boolean createAccountIfNotExists(String username) throws Exception {
        //IMPORTANT:
        //the propay api automatically creates an account if we check the balance of that account

        try{
            getBalance(username);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public long getBalance(String username) throws Exception{
        return this.getAccount(username).amount;
    }

    @Override
    public Account accountExists(String username)
        throws ProPayAccountNotExistException, HttpClientErrorException{
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = baseurl + "account/" + username;
            Account account = restTemplate.getForObject(url, Account.class);

            return account;
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().is4xxClientError()){
                throw new ProPayAccountNotExistException();
            } else {
                throw ex;
            }
        }
    }

    @Override
    public boolean makePayment(String sourceAccount, String targetAccount, long amount) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("amount", ""+amount);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            String url = baseurl + "account/" + sourceAccount+"/transfer/"+targetAccount;
            ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);

            return response.getStatusCode().is2xxSuccessful();
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().is4xxClientError()){
                return false;
            }
        }
        return false;
    }

    @Override
    public void changeUserBalanceBy(String username, long delta) throws Exception{

        System.out.println("attempt to change balance of "+username+" by "+delta);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("amount", "" + delta);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String url = baseurl + "account/"+username;
        restTemplate.postForEntity(url, request, Account.class);
    }

    @Override
    public Reservation makeReservationFromSourceUserToTargetUser(
                    String userSource, String userTarget, long amount
    ) throws Exception {

        System.out.println(
	    "attempting to make reservation from "
	    +userSource
	    +" to "
	    +userTarget
	    +" for "
	    +amount
	    +" Euro"
	);

        //temporary, until the api gets fixed
        circumventForAccount(userSource);
        circumventForAccount(userTarget);

        RestTemplate restTemplate = new RestTemplate();

        String method_url = "reservation/reserve/"+userSource+"/"+userTarget;
        String url = baseurl + method_url;

        System.out.println("url:"+url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("amount", "" + amount);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);


        ResponseEntity<Reservation> reservation 
            = restTemplate.postForEntity(
                URI.create(url),request,Reservation.class
        );

        if(reservation.getStatusCode().is4xxClientError()){
            System.err.println("could not make reservation");
            throw new Exception("cannot make reservation");
        }
        return reservation.getBody();
    }

    @Override
    public Account getAccount(String username) throws Exception {
        Account account=null;
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = baseurl + "account/" + username;
            account = restTemplate.getForObject(url, Account.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("user not exists?");
        }

        return account;
    }

    @Override
    public void returnReservedAmount(String username,Long reservationId) throws Exception {

        System.out.println(
            "attempting to return reserved money to "
            +username
            +" with reservationId="
            +reservationId);

        RestTemplate restTemplate = new RestTemplate();

        String method_url = "reservation/release/"+username;
        String url = baseurl + method_url;

        System.out.println("url:"+url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("reservationId", "" + reservationId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Account> reservation 
            = restTemplate.postForEntity(
            URI.create(url),request,Account.class
        );

        if(reservation.getStatusCode().is4xxClientError()){
            throw new Exception("cannot make reservation");
        }
    }

    @Override
    public void punishReservedAmount(String sourceUsername, Long reservationId) throws Exception {
        System.out.println(
            "attempting to punish reserved money from "
            +sourceUsername
            +" with reservationId="
            +reservationId
        );

        RestTemplate restTemplate = new RestTemplate();

        String method_url = "reservation/punish/"+sourceUsername;
        String url = baseurl + method_url;

        System.out.println("url:"+url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("reservationId", "" + reservationId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        restTemplate.postForEntity(URI.create(url),request,Account.class);
    }

    //------------------- implement methods from Johannes LendingService Interfaces ---------------

    @Override
    public Long reservateAmount(String payingUser, String recivingUser, int amount) throws Exception {
        Reservation reservation = makeReservationFromSourceUserToTargetUser(payingUser, recivingUser, amount);
        return reservation.id;
    }

    @Override
    public void tranferReservatedMoney(String username,Long id) throws Exception {
        punishReservedAmount(username,id);
    }

    @Override
    public void returnReservatedMoney(String username,Long id) throws Exception {
        returnReservedAmount(username,id);
    }

    @Override
    public Long usersCurrentBalance(String username) throws Exception {
        return getBalance(username);
    }
}
