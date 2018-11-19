package SysteemKlasses;

public class ToewijzingsaanvraagException extends Exception {
    public ToewijzingsaanvraagException(String message) {
        super(String.format("[ToewijzingsaanvraagException]: %s", message));
    }
}
