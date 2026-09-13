package utils.cargoservice;

import java.util.List;
import kotlin.Pair;

public interface IHold {

    // --- layout fisso della stiva (letto da config, non cambia mai a runtime) ---
    IPosition getIOPortPosition();
    IPosition getHomePosition();
    IPosition getSlot5Position();
    List<Pair<IPosition, ISlot>> getSlots();

    /** Coordinate dello slot con id dato. Lancia IllegalArgumentException se lo slot non esiste. */
    int slotX(int slotId);
    int slotY(int slotId);

    // --- stato mutabile delle riserve (usato solo dall'istanza di cargoservice) ---

    /** Riserva il primo slot libero e lo marca occupato. Ritorna l'id dello slot riservato, o -1 se la stiva è piena. */
    int reserveFirstFree();

    /** Libera lo slot riservato dall'ultima reserveFirstFree() (usato su timeout/disengaged o trasporto fallito). */
    void releaseReserved();

    /** Stringa sintetica dello stato corrente della stiva, da mostrare sul display IOPort. */
    String displayStatus();
}
