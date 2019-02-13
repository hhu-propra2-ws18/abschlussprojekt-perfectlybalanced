package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.logic.IPayment;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ProPayService implements IProPayService, IPayment {

    public static final String baseurl = "http://propra-propay.herokuapp.com/";

    private static ProPayService instance=null;

    public synchronized static ProPayService getInstance(){
        if(instance==null){
            instance=new ProPayService();
        }
        return instance;
    }

    private ProPayService(){}

    @Override
    public long checkbalance(String username) throws Exception{
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
        return false;
    }

    @Override
    public boolean UserHasAmount(UserEntity User, int amount) {
        return checkbalance(User.getUsername())>=amount;
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
