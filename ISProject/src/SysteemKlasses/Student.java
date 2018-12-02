package SysteemKlasses;

public class Student extends Persoon {
    private String phoneNumber;
    private Ouder ouder;
    private School toegewezenSchool;

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
