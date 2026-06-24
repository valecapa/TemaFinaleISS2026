package cargoservice;

import cargoservice.model.Hold;
import cargoservice.model.Hold.SlotState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test T1 — Gestione della Hold (logica interna, nessun robot né sonar).
 * Questi test verificano il POJO Hold in isolamento.
 */
class HoldTest {

    private Hold hold;

    @BeforeEach
    void setUp() {
        hold = new Hold();
    }

    // T1.1 — hold vuota: reserveFirstFree restituisce slot1
    @Test
    void T1_1_reserveFirstFreeOnEmptyHold() {
        String slot = hold.reserveFirstFree();
        assertEquals("slot1", slot);
        assertEquals(SlotState.RESERVED, hold.getSlotState(0));
        assertTrue(hold.hasReserved());
    }

    // T1.2 — hold già con uno slot RESERVED: nessun altro slot va in RESERVED
    // (la gestione del retrylater è nel cargoservice, ma la hold non deve
    //  permettere una doppia reservation)
    @Test
    void T1_2_noDoubleReservation() {
        hold.reserveFirstFree();          // slot1 RESERVED
        // un secondo reserveFirstFree non dovrebbe essere chiamato dal
        // cargoservice in stato engaged, ma verifichiamo che se chiamato
        // prenda slot2 (non duplica la reservation su slot1)
        String slot = hold.reserveFirstFree();
        assertEquals("slot2", slot);
    }

    // T1.3 — hold piena: reserveFirstFree restituisce null
    @Test
    void T1_3_fullHoldReturnsNull() {
        // occupa tutti e 4 gli slot
        hold.reserveFirstFree();
        hold.markReservedOccupied();
        hold.reserveFirstFree();
        hold.markReservedOccupied();
        hold.reserveFirstFree();
        hold.markReservedOccupied();
        hold.reserveFirstFree();
        hold.markReservedOccupied();

        assertTrue(hold.isFull());
        assertNull(hold.reserveFirstFree());
    }

    // T1.4 — timeout scaduto: releaseReserved riporta lo slot a FREE
    @Test
    void T1_4_releaseReservedOnTimeout() {
        hold.reserveFirstFree();           // slot1 RESERVED
        assertTrue(hold.hasReserved());

        hold.releaseReserved();            // simula disengaged

        assertFalse(hold.hasReserved());
        assertEquals(SlotState.FREE, hold.getSlotState(0));
        assertFalse(hold.isFull());
    }

    // T1.5 — 3 slot OCCUPIED, 1 libero: viene assegnato quello libero
    @Test
    void T1_5_firstFreeWithThreeOccupied() {
        // occupa slot1, slot2, slot3
        hold.reserveFirstFree(); hold.markReservedOccupied();
        hold.reserveFirstFree(); hold.markReservedOccupied();
        hold.reserveFirstFree(); hold.markReservedOccupied();

        // slot4 è ancora FREE
        String slot = hold.reserveFirstFree();
        assertEquals("slot4", slot);
        assertFalse(hold.isFull());
    }

    // Verifica displayStatus
    @Test
    void displayStatusReflectsOccupied() {
        hold.reserveFirstFree();
        hold.markReservedOccupied();
        assertEquals("1/4 occupied", hold.displayStatus());
    }
}
