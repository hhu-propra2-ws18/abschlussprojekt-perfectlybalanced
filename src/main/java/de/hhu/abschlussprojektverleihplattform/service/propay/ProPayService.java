package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
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
    //TODO: implement service that makes the requests
}
