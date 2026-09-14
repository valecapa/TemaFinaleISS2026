package utils.cargoservice;

import java.util.ArrayList;
import java.util.List;
import kotlin.Pair;
import utils.cargoservice.IPosition;

public class Hold implements IHold {

    private final IPosition ioPosition;
    private final IPosition homePosition;
    private final IPosition slot5Position;

    private final List<Pair<IPosition, ISlot>> slotList = new ArrayList<>();

    // unico stato mutabile: quale slot è attualmente riservato (-1 = nessuno)
    private int reservedSlotId = -1;

    public Hold() {
        ioPosition = new Position(4, 0);
        homePosition = new Position(0, 0);
        slot5Position = new Position(2, 5);

        slotList.add(new Pair<>(new Position(1, 1), new Slot(1)));
        slotList.add(new Pair<>(new Position(1, 4), new Slot(2)));
        slotList.add(new Pair<>(new Position(3, 1), new Slot(3)));
        slotList.add(new Pair<>(new Position(3, 4), new Slot(4)));
    }

    @Override
    public IPosition getIOPortPosition() {
        return ioPosition;
    }

    @Override
    public IPosition getHomePosition() {
        return homePosition;
    }

    @Override
    public IPosition getSlot5Position() {
        return slot5Position;
    }

    @Override
    public List<Pair<IPosition, ISlot>> getSlots() {
        return slotList;
    }

    private Pair<IPosition, ISlot> findById(int slotId) {
        for (Pair<IPosition, ISlot> p : slotList) {
            if (p.getSecond().getID() == slotId) {
                return p;
            }
        }
        throw new IllegalArgumentException("Slot inesistente: " + slotId);
    }

    @Override
    public int slotX(int slotId) {
        return findById(slotId).getFirst().getX();
    }

    @Override
    public int slotY(int slotId) {
        return findById(slotId).getFirst().getY();
    }

    @Override
    public int reserveFirstFree() {
        for (Pair<IPosition, ISlot> p : slotList) {
            ISlot slot = p.getSecond();
            if (!slot.isOccupied()) {
                slot.setOccupied(true);
                reservedSlotId = slot.getID();
                return reservedSlotId;
            }
        }
        return -1; // stiva piena
    }

    @Override
    public void releaseReserved() {
        if (reservedSlotId == -1) {
            return; // niente da liberare
        }
        findById(reservedSlotId).getSecond().setOccupied(false);
        reservedSlotId = -1;
    }

    @Override
    public String displayStatus() {
        StringBuilder sb = new StringBuilder();
        for (Pair<IPosition, ISlot> p : slotList) {
            ISlot slot = p.getSecond();
            if (sb.length() > 0) sb.append(",");
            sb.append(slot.getID()).append(":");
            if (slot.isOccupied()) {
            	sb.append("OCC");
            } else if (slot.getID() == reservedSlotId) {
            	sb.append("RES");
            } else {
            	sb.append("FREE");
            }
        }
        return sb.toString();
    }

	@Override
	public boolean setReservedSlotOccupied() {
		for (Pair<IPosition, ISlot> p : slotList) {
			if (p.getSecond().getID() == reservedSlotId) {
				ISlot slot = p.getSecond();
				slot.setOccupied(true);
				reservedSlotId = -1;
				return true;
			}
 
        }
		
		return false;
	}

	@Override
	public boolean setAllSlotsFree() {
		for (Pair<IPosition, ISlot> p : slotList) {
			p.getSecond().setOccupied(false);
		}
		return true;
	}
    
    
}