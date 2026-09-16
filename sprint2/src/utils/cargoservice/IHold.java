package utils.cargoservice;

import java.util.List;
import kotlin.Pair;

public interface IHold {

    IPosition getIOPortPosition();
    IPosition getHomePosition();
    IPosition getSlot5Position();
    List<Pair<IPosition, ISlot>> getSlots();

    int slotX(int slotId);
    int slotY(int slotId);

    int reserveFirstFree();

    void releaseReserved();

    String displayStatus();
    
    boolean setReservedSlotOccupied();
    
    boolean setAllSlotsFree();
}
