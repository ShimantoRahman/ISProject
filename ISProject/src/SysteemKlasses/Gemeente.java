package SysteemKlasses;

public class Gemeente {
    private String naam;
    private int postcode;
    private double breedtegraad;
    private double lengtegraad;

    public Gemeente(String naam, int postcode, double breedtegraad, double lengtegraad) {
        this.naam = naam;
        this.postcode = postcode;
        this.breedtegraad = breedtegraad;
        this.lengtegraad = lengtegraad;
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

    public double getBreedtegraad() {
        return breedtegraad;
    }

    public void setBreedtegraad(double breedtegraad) {
        this.breedtegraad = breedtegraad;
    }

    public double getLengtegraad() {
        return lengtegraad;
    }

    public void setLengtegraad(double lengtegraad) {
        this.lengtegraad = lengtegraad;
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
