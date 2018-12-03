package Logic;

import SysteemKlasses.*;

import java.util.ArrayList;
import java.util.HashMap;

// TODO test scenario's schrijven en debuggen
// TODO ToewijzingsaanvraagExceptions gooien bij (in theorie) onmogelijke situaties voor debuggen makkelijker te maken

public class StudentProposingToewijzingsAlgoritme implements IToewijzingsAlgoritme {

    // instantie variabelen

    // <Rijksregisternummer, Ouder object>
    private HashMap<String, Ouder> ouders;
    // <Rijksregisternummer, Student object>
    private HashMap<String, Student> studenten;
    // <Toewijzingsaanvraagnummer, Toewijzingsaanvraag object>
    private HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen;
    private ArrayList<School> scholen;
    private IIndividueleProcedure individueleProcedure;


    // constructors
    public StudentProposingToewijzingsAlgoritme(HashMap<String, Ouder> ouders, HashMap<String, Student> studenten,
                                                HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen,
                                                ArrayList<School> scholen, IIndividueleProcedure individueleProcedure) {
        this.ouders = ouders;
        this.studenten = studenten;
        this.toewijzingsaanvragen = toewijzingsaanvragen;
        this.scholen = scholen;
        this.individueleProcedure = individueleProcedure;
    }

    // public methoden
    @Override
    public void startToewijzingsProcedure() {
        voorlopigeToewijzingVanAlleKinderenAanEersteVoorkeur();
        toewijzingsProcedureVoorElkKind();
        toewijzingsProcedureVoorNietToegewezenKinderen();
        definitieveToewijzingVanAlleKinderen();
    }

    // private hulpmethoden

    // Zet de status van alle toewijzingsaanvragen op voorlopig
    // Zet de toegewezenschool van elke student op de eerste voorkeurrschool
    private void voorlopigeToewijzingVanAlleKinderenAanEersteVoorkeur() {
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values()) {
            toewijzingsaanvraag.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Voorlopig);
            Voorkeur eersteVoorkeur = toewijzingsaanvraag.getVoorkeuren()[0];
            toewijzingsaanvraag.getStudent().setToegewezenSchool(eersteVoorkeur.getSchool());
            eersteVoorkeur.setStatus(StatusVoorkeur.Toegewezen);
            eersteVoorkeur.getSchool().getStudenten()
                    .put(toewijzingsaanvraag.getStudent().getRijksregisterNummer(), toewijzingsaanvraag.getStudent());
        }
    }

    private void toewijzingsProcedureVoorElkKind() {
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values())
            individueleProcedure.startIndividueleProcedure(toewijzingsaanvraag);
    }

    private void toewijzingsProcedureVoorNietToegewezenKinderen() {
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
    private HashMap<Integer, Toewijzingsaanvraag> getNietToegewezenAanvragen
    (HashMap<Integer, Toewijzingsaanvraag> aanvragen) {
        HashMap<Integer, Toewijzingsaanvraag> nietToegewezenAanvragen = new HashMap<>();
        for (Toewijzingsaanvraag toewijzingsaanvraag: aanvragen.values()) {
            if(!toewijzingsaanvraag.isThuisscholingToegewezen()
                    && toewijzingsaanvraag.getStudent().getToegewezenSchool() == null)
                nietToegewezenAanvragen.put(toewijzingsaanvraag.getToewijzingsaanvraagNummer(), toewijzingsaanvraag);
        } return nietToegewezenAanvragen;
    }

    private void definitieveToewijzingVanAlleKinderen() {
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values())
            toewijzingsaanvraag.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Definitief);
    }
}
