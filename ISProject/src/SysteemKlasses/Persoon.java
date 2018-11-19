package SysteemKlasses;

import java.util.Objects;

public abstract class Persoon {
    private String rijksregisterNummer;
    private String naam;
    private String voornaam;

    public Persoon() {
        this("","","");
    }

    public Persoon(String rijksregisterNummer, String naam, String voornaam) {
        this.rijksregisterNummer = rijksregisterNummer;
        this.naam = naam;
        this.voornaam = voornaam;
    }

    public String getRijksregisterNummer() {
        return rijksregisterNummer;
    }

    public void setRijksregisterNummer(String rijksregisterNummer) {
        this.rijksregisterNummer = rijksregisterNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rijksregisterNummer, naam, voornaam);
    }

    public boolean equals(Object o) {
        if(o != null && o instanceof Persoon)
            return this.rijksregisterNummer.equals(((Persoon) o).getRijksregisterNummer());
        return false;
    }

    public String toString() {
        return String.format("Rijksregisternummer: %s\nNaam: %s\nVoornaam: %s",
                this.rijksregisterNummer, this.naam, this.voornaam);
    }
}
