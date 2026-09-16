package utils.cargoservice;

public interface ISlot {
    int getID();
    boolean isOccupied();
    void setOccupied(boolean value);
}
