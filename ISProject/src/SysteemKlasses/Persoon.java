package SysteemKlasses;

import java.util.Objects;

public abstract class Persoon {
    private final String rijksregisterNummer;
    private final String naam;
    private final String voornaam;

    public Persoon(String rijksregisterNummer, String naam, String voornaam) {
        this.rijksregisterNummer = rijksregisterNummer;
        this.naam = naam;
        this.voornaam = voornaam;
    }

    public String getRijksregisterNummer() {
        return rijksregisterNummer;
    }

    public String getNaam() {
        return naam;
    }

    public String getVoornaam() {
        return voornaam;
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
