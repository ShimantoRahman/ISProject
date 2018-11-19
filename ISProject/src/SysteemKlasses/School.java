package SysteemKlasses;

import java.util.HashMap;
import java.util.Objects;

public class School {
    private String naam;
    private Adres adres;
    private int aantalPlaatsen;
    private HashMap<String, Student> studenten;

    public School(String naam, Adres adres, int aantalPlaatsen) {
        this.naam = naam;
        this.adres = adres;
        this.aantalPlaatsen = aantalPlaatsen;
        this.studenten = new HashMap<>();
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public int getAantalPlaatsen() {
        return aantalPlaatsen;
    }

    public void setAantalPlaatsen(int aantalPlaatsen) {
        this.aantalPlaatsen = aantalPlaatsen;
    }

    public HashMap<String, Student> getStudenten() {
        return studenten;
    }

    public void setStudenten(HashMap<String, Student> studenten) {
        this.studenten = studenten;
    }

    @Override
    public int hashCode() {
        return Objects.hash(naam, adres, aantalPlaatsen, studenten);
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof School)
            return this.naam.equals(((School) o).naam) && this.adres.equals(((School) o).adres);
        return false;
    }
}
