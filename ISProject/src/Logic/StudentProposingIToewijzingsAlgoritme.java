package Logic;

import SysteemKlasses.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// TODO test scenario's schrijven en debuggen
// TODO ToewijzingsaanvraagExceptions gooien bij (in theorie) onmogelijke situaties voor debuggen makkelijker te maken
// TODO refactoring

public class StudentProposingIToewijzingsAlgoritme implements IToewijzingsAlgoritme {
    // <Rijksregisternummer, Ouder object>
    private HashMap<String, Ouder> ouders;
    // <Rijksregisternummer, Student object>
    private HashMap<String, Student> studenten;
    // <Toewijzingsaanvraagnummer, Toewijzingsaanvraag object>
    private HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen;
    private ArrayList<School> scholen;
    private IIndividueleProcedure individueleProcedure;

    public StudentProposingIToewijzingsAlgoritme(HashMap<String, Ouder> ouders, HashMap<String, Student> studenten,
                                                 HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen,
                                                 ArrayList<School> scholen, IIndividueleProcedure individueleProcedure) {
        this.ouders = ouders;
        this.studenten = studenten;
        this.toewijzingsaanvragen = toewijzingsaanvragen;
        this.scholen = scholen;
        this.individueleProcedure = individueleProcedure;
    }

    public HashMap<String, Ouder> getOuders() {
        return ouders;
    }

    public HashMap<String, Student> getStudenten() {
        return studenten;
    }

    public HashMap<Integer, Toewijzingsaanvraag> getToewijzingsaanvragen() {
        return toewijzingsaanvragen;
    }

    public ArrayList<School> getScholen() {
        return scholen;
    }

    @Override
    public void startToewijzingsProcedure() {
        voorlopigeToewijzingAlleAanvragen();
        toewijzingsProcedureVoorElkKind();
        toewijzingsProcedureVoorResterendeKinderen();


        // toewijzingsaanvragen definitief maken
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values())
            toewijzingsaanvraag.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Definitief);

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
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values())
            individueleProcedure.startIndividueleProcedure(toewijzingsaanvraag);
    }

    private void toewijzingsProcedureVoorResterendeKinderen() {
        // kijken of er nog kinderen zijn die nog niet zijn toegewezen en indien nodig nog eens door de procedure te gaan
        HashMap<Integer, Toewijzingsaanvraag> nietToegewezenAanvragen;
        while(true) {
            nietToegewezenAanvragen = getNietToegewezenAanvragen(toewijzingsaanvragen);
            if(nietToegewezenAanvragen.size() == 0)
                break;
            else {
                for (Toewijzingsaanvraag toewijzingsaanvraag: nietToegewezenAanvragen.values())
                    individueleProcedure.startIndividueleProcedure(toewijzingsaanvraag);
            }
        }
    }

    // retourtneert een hashmap met alle toewijzingsaanvragen die nog geen school/thuisscholing zijn toegewezen
    private HashMap<Integer, Toewijzingsaanvraag> getNietToegewezenAanvragen(HashMap<Integer, Toewijzingsaanvraag> aanvragen) {
        HashMap<Integer, Toewijzingsaanvraag> nietToegewezenAanvragen = new HashMap<>();
        for (Toewijzingsaanvraag toewijzingsaanvraag: aanvragen.values()) {
            if(!toewijzingsaanvraag.isThuisonderwijs() && toewijzingsaanvraag.getToegewezenSchool() == null)
                nietToegewezenAanvragen.put(toewijzingsaanvraag.getToewijzingsaanvraagNummer(), toewijzingsaanvraag);
        } return nietToegewezenAanvragen;
    }
}
