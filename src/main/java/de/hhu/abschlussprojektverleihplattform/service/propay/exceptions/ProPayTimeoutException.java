package de.hhu.abschlussprojektverleihplattform.service.propay.exceptions;

public class ProPayTimeoutException extends Exception{

    public ProPayTimeoutException(String msg){
        super(msg);
    }
}
