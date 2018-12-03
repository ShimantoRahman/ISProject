package SysteemKlasses;

import java.util.Arrays;
import java.util.Objects;

public class Toewijzingsaanvraag {
    private static final int AANTAL_VOORKEUREN = 3;
    private static int aantalAanvragen;

    private int toewijzingsaanvraagNummer;
    private Ouder ouder;
    private Student student;
    private Voorkeur[] voorkeuren;
    private boolean thuisscholingToegewezen;
    private StatusToewijzingsaanvraag statusToewijzingsaanvraag;

    public Toewijzingsaanvraag(Ouder ouder) {
        this.toewijzingsaanvraagNummer = aantalAanvragen;
        this.ouder = ouder;
        this.student = null;
        this.voorkeuren = new Voorkeur[AANTAL_VOORKEUREN];
        this.thuisscholingToegewezen = false;
        this.statusToewijzingsaanvraag = StatusToewijzingsaanvraag.Ontwerp;
        aantalAanvragen++;
    }

    public Toewijzingsaanvraag(int toewijzingsaanvraagNummer, Ouder ouder, Student student, Voorkeur[] voorkeuren,
                               boolean thuisscholingToegewezen, StatusToewijzingsaanvraag statusToewijzingsaanvraag) {
        this.toewijzingsaanvraagNummer = toewijzingsaanvraagNummer;
        this.ouder = ouder;
        this.student = student;
        this.voorkeuren = voorkeuren;
        this.thuisscholingToegewezen = thuisscholingToegewezen;
        this.statusToewijzingsaanvraag = statusToewijzingsaanvraag;
    }

    public static int getAantalVoorkeuren() {
        return AANTAL_VOORKEUREN;
    }

    public static int getAantalAanvragen() {
        return aantalAanvragen;
    }

    public static void setAantalAanvragen(int aantalAanvragen) {
        Toewijzingsaanvraag.aantalAanvragen = aantalAanvragen;
    }

    public int getToewijzingsaanvraagNummer() {
        return toewijzingsaanvraagNummer;
    }

    public void setToewijzingsaanvraagNummer(int toewijzingsaanvraagNummer) {
        this.toewijzingsaanvraagNummer = toewijzingsaanvraagNummer;
    }

    public Ouder getOuder() {
        return ouder;
    }

    public void setOuder(Ouder ouder) {
        this.ouder = ouder;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Voorkeur[] getVoorkeuren() {
        return voorkeuren;
    }

    public void setVoorkeuren(Voorkeur[] voorkeuren) {
        this.voorkeuren = voorkeuren;
    }

    public boolean isThuisscholingToegewezen() {
        return thuisscholingToegewezen;
    }

    public void setThuisscholingToegewezen(boolean thuisscholingToegewezen) {
        this.thuisscholingToegewezen = thuisscholingToegewezen;
    }

    public StatusToewijzingsaanvraag getStatusToewijzingsaanvraag() {
        return statusToewijzingsaanvraag;
    }

    public void setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag statusToewijzingsaanvraag) {
        this.statusToewijzingsaanvraag = statusToewijzingsaanvraag;
    }

    public int getVoorkeurIndex(School school) {
        for (int i = 0; i < voorkeuren.length; i++) {
            if(voorkeuren[i].getSchool().equals(school))
                return i;
        } return -1;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(toewijzingsaanvraagNummer, student, statusToewijzingsaanvraag);
        result = 31 * result + Arrays.hashCode(voorkeuren);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof Toewijzingsaanvraag)
            return this.toewijzingsaanvraagNummer == ((Toewijzingsaanvraag) o).toewijzingsaanvraagNummer;
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s", student.getNaam(), student.getVoornaam());
    }
}
