package SysteemKlasses;

public class Gemeente {
    private String naam;
    private int postcode;

    public Gemeente(String naam, int postcode) {
        this.naam = naam;
        this.postcode = postcode;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof Gemeente)
            return this.naam.equals(((Gemeente) o).naam) && this.postcode == ((Gemeente) o).postcode;
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %d", naam, postcode);
    }
}
