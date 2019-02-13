package de.hhu.abschlussprojektverleihplattform.service.propay;

public interface IProPayService {

    public boolean createAccountIfNotExists(String username) throws Exception;

    public long getBalance(String username) throws Exception;

    public boolean accountExists(String username);

    public boolean makePayment(String sourceAccount, String targetAccount, long amount);

    public boolean changeUserBalanceBy(String username,long delta);
}
