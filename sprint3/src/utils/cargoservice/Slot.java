package utils.cargoservice;

public class Slot implements ISlot {
    private final int id;
    private boolean occupied;

    public Slot(int id) {
        this.id = id;
        this.occupied = false;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public boolean isOccupied() {
        return this.occupied;
    }

    @Override
    public void setOccupied(boolean value) {
        this.occupied = value;
    }
}
