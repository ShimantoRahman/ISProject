package Logic;

import SysteemKlasses.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BroerZusAfstandLotingIndividueleProcedure implements IIndividueleProcedure{
    // <Rijksregisternummer, Ouder object>
    private HashMap<String, Ouder> ouders;
    // <Rijksregisternummer, Student object>
    private HashMap<String, Student> studenten;
    // <Toewijzingsaanvraagnummer, Toewijzingsaanvraag object>
    private HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen;
    private HashSet<School> scholen;
    private ISortingAlgoritm sortingAlgoritm;
    private IAfstandBerekeningFormule afstandBerekeningFormule;

    public BroerZusAfstandLotingIndividueleProcedure(HashMap<String, Ouder> ouders, HashMap<String, Student> studenten,
                                                     HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen,
                                                     HashSet<School> scholen, ISortingAlgoritm sortingAlgoritm,
                                                     IAfstandBerekeningFormule afstandBerekeningFormule) {
        this.ouders = ouders;
        this.studenten = studenten;
        this.toewijzingsaanvragen = toewijzingsaanvragen;
        this.scholen = scholen;
        this.sortingAlgoritm = sortingAlgoritm;
        this.afstandBerekeningFormule = afstandBerekeningFormule;
    }

    // voert de individuele toewijzingsprocedure uit
    @Override
    public void startIndividueleProcedure(Toewijzingsaanvraag toewijzingsaanvraag) {
        Student student = toewijzingsaanvraag.getStudent();
        Voorkeur voorkeur = volgendeVoorkeursschool(toewijzingsaanvraag);
        School school = voorkeur.getSchool();

        // student heeft nog een voorkeursschool die nog niet geweigerd is
        if(voorkeur != null) {
            // kijkt of er genoeg of te weinig plaatsen zijn op de voorkeursschool en voert toepasselijke code toe
            analyzePlaatsenSchool(toewijzingsaanvraag, voorkeur, school, student);
        }

        // student heeft geen voorkeurrschool meer die nog niet geweigerd is
        else {
            // kijkt of er nog vrije scholen zijn en voert toepasselijke code toe
            analyzeVrijeScholen(toewijzingsaanvraag, student);
        }
    }

    private void analyzePlaatsenSchool(Toewijzingsaanvraag toewijzingsaanvraag, Voorkeur voorkeur, School school, Student student) {
        // zet school als voorlopig toegewezen school bij student
        toewijzingsaanvraag.setToegewezenSchool(school);
        // zet student bij de voorlopig toegewezen studenten van die school
        school.getStudenten().put(student.getRijksregisterNummer(), student);

        // indien de school niet genoeg plaatsen heeft
        if(!heeftGenoegPlaatsen(school)) {
            rangschikkingEnToewijzing(voorkeur, school);
        }

        // indien de school genoeg plaatsen heeft
        // zet de statusVoorkeur op toegewezen
        // zet de toegewezen school op deze school
        // zet de student bij de haspmap toegewezen studenten bij de school
        else {
            voorkeur.setStatus(StatusVoorkeur.Toegewezen);
            toewijzingsaanvraag.setToegewezenSchool(school);
            school.getStudenten().put(student.getRijksregisterNummer(), student);
        }
    }

    private void rangschikkingEnToewijzing(Voorkeur voorkeur, School school) {
        try {
            // zoek alle toewijzingsaanvragen die de huidige school als toegewezen school hebben
            Toewijzingsaanvraag[] aanvragen = getToewijzingsaanvragenAanSchool(school);

            // rangschik alle studenten
            sortingAlgoritm.sort(aanvragen);

            // zet de statusVoorkeur van alle studenten die gunstig gerangschikt zijn op toegewezen
            // zet hun toegewezen school op deze school
            // zet de student bij de haspmap toegewezen studenten bij de school
            for (int i = 0; i < voorkeur.getSchool().getAantalPlaatsen(); i++) {
                int index = aanvragen[i].getVoorkeurIndex(school);
                if(index < 0)
                    throw new ToewijzingsaanvraagException("Voorkeursschool niet gevonden in de voorkeuren");
                aanvragen[i].getVoorkeuren()[index].setStatus(StatusVoorkeur.Toegewezen);
                aanvragen[i].setToegewezenSchool(school);
                Student tempStudent = aanvragen[i].getStudent();
                school.getStudenten().put(tempStudent.getRijksregisterNummer(), tempStudent);
            }

            // zet de statusVoorkeur van alle studenten die ongunstig gerangschikt zijn op geweigerd
            // zet hun voorlopig toegewezen school op null
            // verwijdert de student van de hashmap toegewezen studenten bij de school
            for (int i = voorkeur.getSchool().getAantalPlaatsen(); i < aanvragen.length; i++) {
                int index = aanvragen[i].getVoorkeurIndex(school);
                if(index < 0)
                    throw new ToewijzingsaanvraagException("Voorkeursschool niet gevonden in de voorkeuren");
                aanvragen[i].getVoorkeuren()[index].setStatus(StatusVoorkeur.Geweigerd);
                aanvragen[i].setToegewezenSchool(null);
                school.getStudenten().remove(aanvragen[i].getStudent().getRijksregisterNummer());
            }

        } catch (ToewijzingsaanvraagException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private void analyzeVrijeScholen(Toewijzingsaanvraag toewijzingsaanvraag, Student student) {
        // ga door alle scholen om te zien of er nog scholen zijn met vrije plaatsen
        // plaats deze scholen in een arraylist
        ArrayList<School> vrijeScholen = getVrijeScholen();

        //indien er nog vrije scholen zijn
        if(vrijeScholen.size() > 0) {
            // zoek in deze arraylist die de kleinste afstand heeft tot de student
            School dichtsteSchool = vindDichtsteSchool(vrijeScholen, toewijzingsaanvraag);

            // wijs de student toe tot deze school
            toewijzingsaanvraag.setToegewezenSchool(dichtsteSchool);
            dichtsteSchool.getStudenten().put(student.getRijksregisterNummer(), student);
        }

        //indien er geen vrije scholen meer zijn
        else {
            // thuisonderwijs
            toewijzingsaanvraag.setThuisonderwijs(true);
            toewijzingsaanvraag.setToegewezenSchool(null);
        }
    }

    // retourneert de volgende voorkeursschool die nog niet is geweigerd
    // als alle voorkeursscholen geweigerd zijn, dan retourneert de methode null
    private Voorkeur volgendeVoorkeursschool(Toewijzingsaanvraag toewijzingsaanvraag) {
        for (Voorkeur voorkeur: toewijzingsaanvraag.getVoorkeuren()) {
            if(voorkeur.getStatus() == StatusVoorkeur.Toegewezen || voorkeur.getStatus() == StatusVoorkeur.Undefined)
                return voorkeur;
        } return null;
    }

    // retourneert of een school genoeg plaatsen heeft
    private boolean heeftGenoegPlaatsen(School school) {
        return school.getAantalPlaatsen() >= getStudentenOpSchool(school).size();
    }

    // retourneert een arraylist met alle studenten op die school
    private ArrayList<Student> getStudentenOpSchool(School school) {
        ArrayList<Student> studenten = new ArrayList<>();
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values()) {
            if(toewijzingsaanvraag.getToegewezenSchool().equals(school))
                studenten.add(toewijzingsaanvraag.getStudent());
        } return studenten;
    }

    // retourneert een arraylist met alle toewijzingsaanvragen die toegewezen zijn aan die school
    private Toewijzingsaanvraag[] getToewijzingsaanvragenAanSchool(School school) {
        ArrayList<Toewijzingsaanvraag> aanvragen = new ArrayList<>();
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values()) {
            if(toewijzingsaanvraag.getToegewezenSchool().equals(school))
                aanvragen.add(toewijzingsaanvraag);
        } return aanvragen.toArray(new Toewijzingsaanvraag[aanvragen.size()]);
    }

    // retourneert alle scholen die nog vrije plaatsen hebben in een arraylist
    private ArrayList<School> getVrijeScholen() {
        ArrayList<School> vrijeScholen = new ArrayList<>();
        for (School school: scholen) {
            if(school.getAantalPlaatsen() > school.getStudenten().size())
                vrijeScholen.add(school);
        } return vrijeScholen;
    }

    private School vindDichtsteSchool(ArrayList<School> scholen, Toewijzingsaanvraag toewijzingsaanvraag) {
        Adres adresOuder = toewijzingsaanvraag.getOuder().getAdres();
        double breedtegraadOuder = adresOuder.getGemeente().getBreedtegraad();
        double lengtegraadOuder = adresOuder.getGemeente().getLengtegraad();

        // zet dichtste school als de eerste school in de lijst
        School dichtsteSchool = scholen.get(0);

        // zet de kleinsteAfstand op de afstand tussen de ouder en de eerste school in de lijst
        double breedtegraadSchool = scholen.get(0).getAdres().getGemeente().getBreedtegraad();
        double lengtegraadSchool = scholen.get(0).getAdres().getGemeente().getLengtegraad();
        afstandBerekeningFormule.setPunten(breedtegraadOuder, lengtegraadOuder, breedtegraadSchool, lengtegraadSchool);
        double kleinsteAfstand = afstandBerekeningFormule.getAfstand();

        for (int i = 1; i <scholen.size(); i++) {
            School school = scholen.get(i);
            Adres adresSchool = school.getAdres();

            // vind lengte-en breedtegraden van de adressen
            breedtegraadSchool = school.getAdres().getGemeente().getBreedtegraad();
            lengtegraadSchool = school.getAdres().getGemeente().getLengtegraad();
            afstandBerekeningFormule.setPunten(breedtegraadOuder, lengtegraadOuder,
                    breedtegraadSchool, lengtegraadSchool);

            // bereken afstand tussen de ouder en de school
            double afstand = afstandBerekeningFormule.getAfstand();
            if(afstand < kleinsteAfstand) {
                kleinsteAfstand = afstand;
                dichtsteSchool = school;
            }

        } return dichtsteSchool;
    }
}
