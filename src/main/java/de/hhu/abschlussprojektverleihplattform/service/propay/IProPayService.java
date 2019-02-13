package de.hhu.abschlussprojektverleihplattform.service.propay;

public interface IProPayService {

    public long checkbalance(String username);

    public boolean accountExists(String username);

    public boolean makePayment(String sourceAccount, String targetAccount, long amount);

    
}
