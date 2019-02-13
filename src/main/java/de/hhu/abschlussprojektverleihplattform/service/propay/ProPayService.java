package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ProPayService implements IProPayService{

    public static final String baseurl = "propra-propay.herokuapp.com/";

    private static ProPayService instance=null;

    public synchronized static ProPayService getInstance(){
        if(instance==null){
            instance=new ProPayService();
        }
        return instance;
    }

    private ProPayService(){

    }

    @Override
    public long checkbalance(String username) {

        RestTemplate restTemplate = new RestTemplate();

        String url = baseurl+"account/"+username;
        Account account = restTemplate.getForObject(url,Account.class);

        return account.amount;
    }

    @Override
    public boolean accountExists(String username) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = baseurl + "account/" + username;
            Account account = restTemplate.getForObject(url, Account.class);
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().is4xxClientError()){

            }
        }
        return false;
    }

    @Override
    public boolean makePayment(String sourceAccount, String targetAccount, long amount) {
        return false;
    }
    //TODO: implement service that makes the requests
}
