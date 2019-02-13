package de.hhu.abschlussprojektverleihplattform.service.propay;

import org.springframework.web.client.RestTemplate;

public class ProPayService implements IProPayService{

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
    public int checkbalance(String username) {

        RestTemplate restTemplate

        return 0;
    }
    //TODO: implement service that makes the requests
}
