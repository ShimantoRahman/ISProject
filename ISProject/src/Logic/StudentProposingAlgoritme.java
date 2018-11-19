package Logic;

import SysteemKlasses.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// TODO test scenario's schrijven en debuggen
// TODO ToewijzingsaanvraagExceptions gooien bij (in theorie) onmogelijke situaties voor debuggen makkelijker te maken
// TODO refactoring

// TODO static klasse maken?
public class StudentProposingAlgoritme implements Algoritme {
    // <Rijksregisternummer, Ouder object>
    private static HashMap<String, Ouder> ouders;
    // <Rijksregisternummer, Student object>
    private static HashMap<String, Student> studenten;
    // <Toewijzingsaanvraagnummer, Toewijzingsaanvraag object>
    private static HashMap<String, Toewijzingsaanvraag> toewijzingsaanvragen;
    private static HashSet<School> scholen;

    public static HashMap<String, Ouder> getOuders() {
        return ouders;
    }

    public static HashMap<String, Student> getStudenten() {
        return studenten;
    }

    public static HashMap<String, Toewijzingsaanvraag> getToewijzingsaanvragen() {
        return toewijzingsaanvragen;
    }

    public static HashSet<School> getScholen() {
        return scholen;
    }

    @Override
    public void startToewijzingsProcedure() {
        voorlopigeToewijzingAlleAanvragen();
        toewijzingsProcedureVoorElkKind();
        // TODO kijken of er nog kinderen zijn die nog niet zijn toegewezen en indien nodig nog eens door de procedure te gaan
        // TODO toewijzingsaanvragen definitief maken
        // TODO schoolfiches aanmaken
    }

    // Zet de status van alle toewijzingsaanvragen op voorlopig
    // Zet de toegewezenschool van elke student op de eerste voorkeurrschool
    private void voorlopigeToewijzingAlleAanvragen() {
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values()) {
            toewijzingsaanvraag.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Voorlopig);
            // eerste voorkeursschool
            toewijzingsaanvraag.setToegewezenSchool(toewijzingsaanvraag.getVoorkeuren()[0].getSchool());
            toewijzingsaanvraag.getVoorkeuren()[0].setStatus(StatusVoorkeur.Toegewezen);
            toewijzingsaanvraag.getVoorkeuren()[0].getSchool().getStudenten()
                    .put(toewijzingsaanvraag.getStudent().getRijksregisterNummer(), toewijzingsaanvraag.getStudent());
        }
    }

    // voert de individuele toewijzingsprocedure uit voor elk kind
    private void toewijzingsProcedureVoorElkKind() {
        IndividueleProcedure.setToewijzingsaanvragen(toewijzingsaanvragen);
        IndividueleProcedure.setOuders(ouders);
        IndividueleProcedure.setStudenten(studenten);
        IndividueleProcedure.setScholen(scholen);
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values()) {
            IndividueleProcedure.individueleProcedure(toewijzingsaanvraag);
        }
    }

}
