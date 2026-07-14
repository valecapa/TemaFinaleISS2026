package utils.cargoservice;

public class Hold {
    public static final int NUM_SLOTS = 4;       // slot1..slot4

    public enum SlotState { FREE, RESERVED, OCCUPIED }

    private SlotState[] slots = {
        SlotState.FREE,
        SlotState.FREE,
        SlotState.FREE,
        SlotState.FREE
    };

    private boolean slot5Occupied = false;

    public boolean isSlot5Occupied() {
        return slot5Occupied;
    }

    public void setSlot5Occupied(boolean occupied) {
        slot5Occupied = occupied;
    }

    public int reserveFirstFree() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (slots[i] == SlotState.RESERVED) {
                return -1; // al più una prenotazione attiva alla volta
            }
        }
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (slots[i] == SlotState.FREE) {
                slots[i] = SlotState.RESERVED;
                return (i + 1);
            }
        }
        return -1; // piena
    }

    public void markReservedOccupied() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (slots[i] == SlotState.RESERVED) {
                slots[i] = SlotState.OCCUPIED;
                return;
            }
        }
    }

    public void releaseReserved() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (slots[i] == SlotState.RESERVED) {
                slots[i] = SlotState.FREE;
                return;
            }
        }
    }

    public boolean isFull() {
        for (SlotState s : slots) {
            if (s != SlotState.OCCUPIED) {
                return false;
            }
        }
        return true;
    }

    public String displayStatus() {
        int occupied = 0;
        for (SlotState s : slots) {
            if (s == SlotState.OCCUPIED) {
                occupied++;
            }
        }
        return occupied + "/" + NUM_SLOTS + " occupied";
    }
}