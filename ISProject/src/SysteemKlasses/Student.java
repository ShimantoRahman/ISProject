package SysteemKlasses;

//import java.util.HashMap;

public class Student extends Persoon {
    private String phoneNumber;
    private Ouder ouder;
    private School toegewezenSchool;
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

    public Student(String rijksregisterNummer, String naam, String voornaam, String phoneNumber, Ouder ouder,
                   School toegewezenSchool) {
        super(rijksregisterNummer, naam, voornaam);
        this.phoneNumber = phoneNumber;
        this.ouder = ouder;
        this.toegewezenSchool = toegewezenSchool;
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

    public School getToegewezenSchool() {
        return toegewezenSchool;
    }

    public void setToegewezenSchool(School toegewezenSchool) {
        this.toegewezenSchool = toegewezenSchool;
    }

    public String toString() {
        return super.toString() + String.format("\nTelefoonnummer: %s", this.phoneNumber);
    }
}
