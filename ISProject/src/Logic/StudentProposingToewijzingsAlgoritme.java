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
        try {
            controleerInstantieVariabelen();
            voorlopigeToewijzingVanAlleKinderenAanEersteVoorkeur();
            toewijzingsProcedureVoorElkKind();
            toewijzingsProcedureVoorNietToegewezenKinderen();
            definitieveToewijzingVanAlleKinderen();
        } catch (ToewijzingsaanvraagException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

    }

    private void controleerInstantieVariabelen() throws ToewijzingsaanvraagException {
        if(studenten == null)
            throw new ToewijzingsaanvraagException("Hashmap studenten is null");
        if(ouders == null)
            throw new ToewijzingsaanvraagException("Hashmap ouders is null");
        if(scholen == null)
            throw new ToewijzingsaanvraagException("Arraylist scholen is null");
        if(toewijzingsaanvragen == null)
            throw new ToewijzingsaanvraagException("Hashmap toewijzingsaanvragen is null");
        if(individueleProcedure == null)
            throw new ToewijzingsaanvraagException("Individueleprocedure is null");
    }

    // private hulpmethoden

    // Zet de status van alle toewijzingsaanvragen op voorlopig
    // Zet de toegewezenschool van elke student op de eerste voorkeurrschool
    private void voorlopigeToewijzingVanAlleKinderenAanEersteVoorkeur() throws  ToewijzingsaanvraagException{
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values()) {
            controleerToewijzingsaanvraag(toewijzingsaanvraag);
            toewijzingsaanvraag.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Voorlopig);
            Voorkeur eersteVoorkeur = toewijzingsaanvraag.getVoorkeuren()[0];
            toewijzingsaanvraag.getStudent().setToegewezenSchool(eersteVoorkeur.getSchool());
            eersteVoorkeur.setStatus(StatusVoorkeur.Toegewezen);
            eersteVoorkeur.getSchool().getStudenten()
                    .put(toewijzingsaanvraag.getStudent().getRijksregisterNummer(), toewijzingsaanvraag.getStudent());
        }
    }

    private void controleerToewijzingsaanvraag(Toewijzingsaanvraag toewijzingsaanvraag)
            throws ToewijzingsaanvraagException {
        if(toewijzingsaanvraag == null)
            throw new ToewijzingsaanvraagException("Toewijzingsaanvraag is null");
        if(toewijzingsaanvraag.getStudent() == null)
            throw new ToewijzingsaanvraagException("Student van toewijzingsaanvraag is null");
        if(toewijzingsaanvraag.getOuder() == null)
            throw new ToewijzingsaanvraagException("Ouder van toewijzingsaanvraag is null");
        if(toewijzingsaanvraag.getVoorkeuren() == null)
            throw new ToewijzingsaanvraagException("Voorkeuren van toewijzingsaanvraag is null");
        for (Voorkeur voorkeur: toewijzingsaanvraag.getVoorkeuren()) {
            if(voorkeur == null)
                throw new ToewijzingsaanvraagException("voorkeur van voorkeuren is null");
            if(voorkeur.getSchool() == null)
                throw new ToewijzingsaanvraagException("voorkeurrschool is null");
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
