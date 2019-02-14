package de.hhu.abschlussprojektverleihplattform.model;

public enum Lendingstatus {
    requested(0),
    confirmt(1),
    returned(2),
    conflict(3),
    done(4);

    private final int value;

    private Lendingstatus(int value) {
        this.value = value;
    }

    public static int getLemdingStatusValueFrom(Lendingstatus lendingstatus){
        return lendingstatus.value;
    }
}




