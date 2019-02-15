package de.hhu.abschlussprojektverleihplattform.model;

import java.util.HashMap;
import java.util.Map;

public enum Lendingstatus {
    requested(0),
    confirmt(1),
    returned(2),
    conflict(3),
    done(4),
    denied(5);

    private final int value;
    private static Map map = new HashMap<>();


    private Lendingstatus(int value) {
        this.value = value;
    }

    public static int getLemdingStatusValueFrom(Lendingstatus lendingstatus){
        return lendingstatus.value;
    }

    static {
        for (Lendingstatus lendingstatus : Lendingstatus.values()){
            map.put(lendingstatus.value, lendingstatus);
        }
    }

    public static Lendingstatus valueOf(int lendingStatus){
        return (Lendingstatus) map.get(lendingStatus);
    }

    public int getValue(){
        return value;
    }
}




