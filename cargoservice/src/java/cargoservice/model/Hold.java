package java.cargoservice.model;

/**
 *
 * POJO passivo: gestito internamente dal cargoservice.
 *
 */
public class Hold {
    public static final int NUM_SLOTS = 4;       // slot1..slot4

    public enum SlotState { FREE, RESERVED, OCCUPIED }
    // RESERVED: slot assegnato a una richiesta accettata, robot non ancora arrivato.
    // Finché uno slot è RESERVED il sistema è 'engaged' e non accetta nuove richieste.

    private SlotState[] slots = { // slot1..slot4
        SlotState.FREE,
        SlotState.FREE, 
        SlotState.FREE,
        SlotState.FREE
    };

    // slot5 è libero o occupato dal marker
    //private boolean slot5Occupied = false;

    // Riserva il primo slot libero; restituisce il suo nome (es. "slot1") o null se piena
    //public String reserveFirstFree() { ... }

    // Segna lo slot attualmente RESERVED come OCCUPIED
   //public void markReservedOccupied() { ... }

    // True se tutti e 4 gli slot sono OCCUPIED
    //public boolean isFull() { ... }

    // Stringa per il display (es. "2/4 occupied")
    //public String displayStatus() { ... }
}
