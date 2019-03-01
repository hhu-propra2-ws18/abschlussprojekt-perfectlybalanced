package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.repository.TransactionRepository;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import de.hhu.abschlussprojektverleihplattform.service.propay.adapter.ProPayAdapter;
import de.hhu.abschlussprojektverleihplattform.service.propay.interfaces.IPaymentService;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStream.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Component
public class ProPayService implements IPaymentService {

    //represents the api as needed by the rest of the application

    //keep track of the accounts already created with propay.
    //creating an account that already exists should fail
    private static Set<String> accounts_created=new HashSet<>();

    @Autowired
    ProPayAdapter proPayAdapter;

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private ProPayService(TransactionRepository transactionRepository,
                          UserRepository userRepository){
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    private void create_account_if_not_exists(String username) throws Exception{
        //we can call this method before others to make sure propay knows of
        //the accounts we are doing transactions on
        //TODO
        if(!accounts_created.contains(username)){
            proPayAdapter.createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(username,0);
            accounts_created.add(username);
        }
    }

    private void create_account_if_not_exists(String... usernames) throws Exception{
        for(String username : usernames){
            create_account_if_not_exists(username);
        }
    }

    private long interval_to_check = 10000;

    private boolean was_available_recently = false;
    private long last_checked_availability_milliseconds
            =System.currentTimeMillis()-(interval_to_check*2);

    public void isAvailable() throws Exception {

        long current_time = System.currentTimeMillis();



        if((current_time-last_checked_availability_milliseconds) > interval_to_check) {

            last_checked_availability_milliseconds=current_time;

            check_available();

            was_available_recently=true;
        }else{
            if(!was_available_recently){
                throw new Exception("ProPay not available");
            }
        }
    }

    private void check_available() throws Exception{
        System.out.println("checking for propay availability");

        URL u = new URL(proPayAdapter.baseurl);
        InputStream in = u.openStream();
        in.close();
    }

    //------------------- implement methods from Johannes LendingService Interfaces ---------------

    @Override
    public Long reservateAmount(
        String payingUser,
        String recivingUser,
        int amount
    ) throws Exception {
        create_account_if_not_exists(payingUser,recivingUser);
        Reservation reservation = proPayAdapter.makeReservation(
            payingUser,
            recivingUser,
            amount
        );
        return reservation.id;
    }

    @Override
    public void tranferReservatedMoney(String username,Long id) throws Exception {
        create_account_if_not_exists(username);
        proPayAdapter.punishReservation(username,id);
    }

    @Override
    public void returnReservatedMoney(String username,Long id) throws Exception {
        create_account_if_not_exists(username);
        proPayAdapter.releaseReservation(username,id);
    }

    @Override
    public Long usersCurrentBalance(String username) throws Exception {
        create_account_if_not_exists(username);
        return proPayAdapter.getAccount(username).amount;
    }
}
