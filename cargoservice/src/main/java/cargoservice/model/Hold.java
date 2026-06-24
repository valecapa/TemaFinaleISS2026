package cargoservice.model;

/**
 * 
 * POJO passivo: gestito internamente dal cargoservice.
 *
 */
public class Hold {

    public static final int NUM_SLOTS = 4;   // slot1..slot4

    public enum SlotState { FREE, RESERVED, OCCUPIED }

    private final SlotState[] slots = {
        SlotState.FREE,
        SlotState.FREE,
        SlotState.FREE,
        SlotState.FREE
    };

    private int reservedIndex = -1;          // indice dello slot attualmente RESERVED (-1 = nessuno)
    private boolean slot5Occupied = false;

    // ── query ────────────────────────────────────────────────

    /** Restituisce true se tutti gli slot1-4 sono OCCUPIED. */
    public boolean isFull() {
        for (SlotState s : slots) {
            if (s != SlotState.OCCUPIED) return false;
        }
        return true;
    }

    /** Restituisce true se c'è già uno slot RESERVED (sistema già engaged). */
    public boolean hasReserved() {
        return reservedIndex != -1;
    }

    /** Restituisce il nome dello slot riservato (es. "slot2"), o null se nessuno. */
    public String getReservedSlotName() {
        if (reservedIndex == -1) return null;
        return "slot" + (reservedIndex + 1);
    }

    public SlotState getSlotState(int index) {
        return slots[index];
    }

    public boolean isSlot5Occupied() {
        return slot5Occupied;
    }

    // ── comandi ──────────────────────────────────────────────

    /**
     * Strategia first-free: riserva il primo slot libero.
     * @return nome del slot riservato (es. "slot1"), o null se la hold è piena.
     */
    public String reserveFirstFree() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (slots[i] == SlotState.FREE) {
                slots[i] = SlotState.RESERVED;
                reservedIndex = i;
                return "slot" + (i + 1);
            }
        }
        return null;   // hold piena
    }

    /**
     * Rilascia lo slot correntemente RESERVED, tornandolo a FREE.
     * Chiamato in caso di timeout (disengaged) o annullamento.
     */
    public void releaseReserved() {
        if (reservedIndex != -1) {
            slots[reservedIndex] = SlotState.FREE;
            reservedIndex = -1;
        }
    }

    /**
     * Marca lo slot riservato come OCCUPIED.
     * Chiamato quando il robot ha depositato il container nello slot definitivo.
     */
    public void markReservedOccupied() {
        if (reservedIndex != -1) {
            slots[reservedIndex] = SlotState.OCCUPIED;
            reservedIndex = -1;
        }
    }

    public void setSlot5Occupied(boolean occupied) {
        this.slot5Occupied = occupied;
    }

    // ── rappresentazione ─────────────────────────────────────

    /** Stringa di stato per il display IOPort (es. "2/4 occupied"). */
    public String displayStatus() {
        int count = 0;
        for (SlotState s : slots) {
            if (s == SlotState.OCCUPIED) count++;
        }
        return count + "/" + NUM_SLOTS + " occupied";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Hold[");
        for (int i = 0; i < NUM_SLOTS; i++) {
            sb.append("slot").append(i + 1).append("=").append(slots[i]);
            if (i < NUM_SLOTS - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
