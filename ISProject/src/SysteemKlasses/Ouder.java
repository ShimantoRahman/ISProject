package SysteemKlasses;

public class Ouder extends Persoon {
    private String emailAdres;
    private Adres adres;

    public Ouder() {
        this("","","","",null);
    }

    public Ouder(String rijksregisterNummer, String naam, String voornaam, String emailAdres, Adres adres) {
        super(rijksregisterNummer, naam, voornaam);
        this.emailAdres = emailAdres;
        this.adres = adres;
    }

    public String getEmailAdres() {
        return emailAdres;
    }

    public void setEmailAdres(String emailAdres) {
        this.emailAdres = emailAdres;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public String toString() {
        return super.toString() + String.format("\nEmail adres: %s\nAdres: %s", this.emailAdres, this.adres);
    }
}
