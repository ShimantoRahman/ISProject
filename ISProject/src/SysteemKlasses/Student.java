package SysteemKlasses;

//import java.util.HashMap;

public class Student extends Persoon {
    private String phoneNumber;
    private Ouder ouder;
    //hashmap/map gebruiken voor voorkeursscholen te linken met boolean broer of zus aanwezig op die school?
    //private HashMap<School, Boolean> broersEnZussenOpScholen;
    //private HashMap<School, Double> afstandTotScholen;

    public Student() {
        this("","","","");
    }

    public Student(String rijksregisterNummer, String naam, String voornaam, String phoneNumber) {
        super(rijksregisterNummer, naam, voornaam);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Ouder getOuder() {
        return ouder;
    }

    public void setOuder(Ouder ouder) {
        this.ouder = ouder;
    }

    public String toString() {
        return super.toString() + String.format("\nTelefoonnummer: %s", this.phoneNumber);
    }
}
