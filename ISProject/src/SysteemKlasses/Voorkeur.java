package SysteemKlasses;

import java.util.Objects;

public class Voorkeur {
    private School school;
    private double afstand;
    private boolean broerOfZusAanwezig;
    private StatusVoorkeur status;


    public Voorkeur(School school, double afstand, boolean broerOfZusAanwezig) {
        this.school = school;
        this.afstand = afstand;
        this.broerOfZusAanwezig = broerOfZusAanwezig;
        this.status = StatusVoorkeur.Undefined;
    }

    public Voorkeur(School school, double afstand, boolean broerOfZusAanwezig, StatusVoorkeur status) {
        this.school = school;
        this.afstand = afstand;
        this.broerOfZusAanwezig = broerOfZusAanwezig;
        this.status = status;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public double getAfstand() {
        return afstand;
    }

    public void setAfstand(double afstand) {
        this.afstand = afstand;
    }

    public boolean isBroerOfZusAanwezig() {
        return broerOfZusAanwezig;
    }

    public void setBroerOfZusAanwezig(boolean broerOfZusAanwezig) {
        this.broerOfZusAanwezig = broerOfZusAanwezig;
    }

    public StatusVoorkeur getStatus() {
        return status;
    }

    public void setStatus(StatusVoorkeur status) {
        this.status = status;
    }

    public static boolean beideBroerOfZusAanwezig(Voorkeur voorkeur1, Voorkeur voorkeur2) {
        return voorkeur1.isBroerOfZusAanwezig() && voorkeur2.isBroerOfZusAanwezig();
    }

    public static boolean beideBroerOfZusNietAanwezig(Voorkeur voorkeur1, Voorkeur voorkeur2) {
        return !voorkeur1.isBroerOfZusAanwezig() && !voorkeur2.isBroerOfZusAanwezig();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voorkeur voorkeur = (Voorkeur) o;
        return status == voorkeur.status &&
                afstand == voorkeur.afstand &&
                broerOfZusAanwezig == voorkeur.broerOfZusAanwezig &&
                Objects.equals(school, voorkeur.school);
    }

    @Override
    public int hashCode() {
        return Objects.hash(school, afstand, broerOfZusAanwezig, status);
    }
}
