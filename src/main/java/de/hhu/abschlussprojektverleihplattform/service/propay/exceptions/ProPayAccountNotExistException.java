package de.hhu.abschlussprojektverleihplattform.service.propay.exceptions;

public class ProPayAccountNotExistException extends Exception{

    public ProPayAccountNotExistException(String msg){
        super(msg);
    }
}
