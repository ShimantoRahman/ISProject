package SysteemKlasses;

public class Adres {
    private String straat;
    //straatnummer kan ook bijvoorbeeld: 34-5 zijn dus int volstaat niet
    private String straatnummer;
    private Gemeente gemeente;

    public Adres(String straat, String straatnummer, Gemeente gemeente) {
        this.straat = straat;
        this.straatnummer = straatnummer;
        this.gemeente = gemeente;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getStraatnummer() {
        return straatnummer;
    }

    public void setStraatnummer(String straatnummer) {
        this.straatnummer = straatnummer;
    }

    public Gemeente getGemeente() {
        return gemeente;
    }

    public void setGemeente(Gemeente gemeente) {
        this.gemeente = gemeente;
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof Adres)
            return this.straat.equals(((Adres) o).straat)
                    && this.straatnummer.equals(((Adres) o).straatnummer)
                    && this.gemeente.equals(((Adres) o).gemeente);
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", straat, straatnummer, gemeente);
    }
}
