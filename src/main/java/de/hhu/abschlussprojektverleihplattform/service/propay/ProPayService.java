package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.logic.IPayment;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProPayService implements IProPayService, IPayment {

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
        Account account=null;
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = baseurl + "account/" + username;
            account = restTemplate.getForObject(url, Account.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("user not exists?");
        }

        return account.amount;
    }

    @Override
    public boolean accountExists(String username) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = baseurl + "account/" + username;
            Account account = restTemplate.getForObject(url, Account.class);

            return true;
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().is4xxClientError()){
                return false;
            }
        }
        return false;
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
    public boolean changeUserBalanceBy(String username, long delta){

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("amount", "" + delta);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            String url = baseurl + "account/"+username;
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reservation makeReservationFromSourceUserToTargetUser(String userSource, String userTarget, long amount) throws Exception {

        //TODO: implement
        return null;
    }


    //------------------- implement methods from Johannes Logic Interfaces ---------------

    @Override
    public boolean UserHasAmount(UserEntity User, int amount) {
        try{
            return getBalance(User.getUsername())>=amount;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean reservateAmount(UserEntity user, int amount) {
        //TODO: implement
        return false;
    }

    @Override
    public void tranferReservatedMoney(UserEntity payingUser, UserEntity recivingUser, int amount) {
        //TODO: implement
    }

    @Override
    public void returnReservatedMoney(UserEntity userEntity, int amount) {
        //TODO: implement
    }
}
