package Logic;

import SysteemKlasses.*;

import java.util.ArrayList;
import java.util.HashMap;

public class BroerZusAfstandLotingSchoolProcedure implements ISchoolProcedure {
    // instantie variabelen

    // <Rijksregisternummer, Ouder object>
    private HashMap<String, Ouder> ouders;
    // <Rijksregisternummer, Student object>
    private HashMap<String, Student> studenten;
    // <Toewijzingsaanvraagnummer, Toewijzingsaanvraag object>
    private HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen;
    private ArrayList<School> scholen;
    private ISortingAlgoritm sortingAlgoritm;
    private IAfstandBerekeningFormule afstandBerekeningFormule;

    // constructors

    public BroerZusAfstandLotingSchoolProcedure(HashMap<String, Ouder> ouders, HashMap<String, Student> studenten,
                                                HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen,
                                                ArrayList<School> scholen, ISortingAlgoritm sortingAlgoritm,
                                                IAfstandBerekeningFormule afstandBerekeningFormule) {
        this.ouders = ouders;
        this.studenten = studenten;
        this.toewijzingsaanvragen = toewijzingsaanvragen;
        this.scholen = scholen;
        this.sortingAlgoritm = sortingAlgoritm;
        this.afstandBerekeningFormule = afstandBerekeningFormule;
    }

    public void startProcedure(School school) {
        if(schoolHeeftGenoegPlaatsen(school)) {
            // doe niets
            // alle studenten zijn al aan de school toegewezen
        } else {
            rangschikkingEnToewijzingStudenten(school);
        }
    }

    private boolean schoolHeeftGenoegPlaatsen(School school) {
        return school.getAantalPlaatsen() >= school.getStudenten().size();
    }

    private void rangschikkingEnToewijzingStudenten(School school) {
        try {
            Toewijzingsaanvraag[] aanvragenAanSchool = getToewijzingsaanvragenAanSchool(school);

            // rangschik alle studenten
            sortingAlgoritm.sort(aanvragenAanSchool, school);

            toewijzingStudenten(aanvragenAanSchool, school);

        } catch (ToewijzingsaanvraagException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private Toewijzingsaanvraag[] getToewijzingsaanvragenAanSchool(School school) {
        ArrayList<Toewijzingsaanvraag> aanvragen = new ArrayList<>();
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values()) {
            if(toewijzingsaanvraag.getStudent().getToegewezenSchool() != null
                    && toewijzingsaanvraag.getStudent().getToegewezenSchool().equals(school))
                aanvragen.add(toewijzingsaanvraag);
        } return aanvragen.toArray(new Toewijzingsaanvraag[aanvragen.size()]);
    }

    private void toewijzingStudenten(Toewijzingsaanvraag[] aanvragenAanSchool, School school)
            throws ToewijzingsaanvraagException{

        toewijzingGunstigGerangschikteStudenten(aanvragenAanSchool, school);
        toewijzingsOngunstigGerangschikteStudenten(aanvragenAanSchool, school);
    }

    private void toewijzingGunstigGerangschikteStudenten (Toewijzingsaanvraag[] aanvragenAanSchool, School school)
            throws ToewijzingsaanvraagException {

        for (int i = 0; i < school.getAantalPlaatsen(); i++) {
            int index = aanvragenAanSchool[i].getVoorkeurIndex(school);
            if(index < 0)
                throw new ToewijzingsaanvraagException("Voorkeursschool niet gevonden in de voorkeuren");
            aanvragenAanSchool[i].getVoorkeuren()[index].setStatus(StatusVoorkeur.Toegewezen);
            wijsStudentVoorlopigToeAanSchool(aanvragenAanSchool[i].getStudent(), school);
        }
    }

    private void toewijzingsOngunstigGerangschikteStudenten (Toewijzingsaanvraag[] aanvragenAanSchool, School school)
            throws ToewijzingsaanvraagException {
        for (int i = school.getAantalPlaatsen(); i < aanvragenAanSchool.length; i++) {
            Toewijzingsaanvraag toewijzingsaanvraag = aanvragenAanSchool[i];
            int index = toewijzingsaanvraag.getVoorkeurIndex(school);
            if(index < 0)
                throw new ToewijzingsaanvraagException("Voorkeursschool niet gevonden in de voorkeuren");
            toewijzingsaanvraag.getVoorkeuren()[index].setStatus(StatusVoorkeur.Geweigerd);
            school.getStudenten().remove(toewijzingsaanvraag.getStudent().getRijksregisterNummer());
            Voorkeur volgendeNietGeweigerdeVoorkeursschool = volgendeNietGeweigerdeVoorkeursschool(toewijzingsaanvraag);

            if (volgendeNietGeweigerdeVoorkeursschool != null)
                studentHeeftNogEenVoorkeurrschool(toewijzingsaanvraag, volgendeNietGeweigerdeVoorkeursschool);
            else
                studentHeeftGeenVoorkeursschoolMeer(toewijzingsaanvraag);
        }
    }



    // retourneert de volgende voorkeursschool die nog niet is geweigerd
    // als alle voorkeursscholen geweigerd zijn, dan retourneert de methode null
    private Voorkeur volgendeNietGeweigerdeVoorkeursschool(Toewijzingsaanvraag toewijzingsaanvraag) {
        for (Voorkeur voorkeur: toewijzingsaanvraag.getVoorkeuren()) {
            if(voorkeur.getStatus() == StatusVoorkeur.Toegewezen || voorkeur.getStatus() == StatusVoorkeur.Undefined)
                return voorkeur;
        } return null;
    }

    private void studentHeeftNogEenVoorkeurrschool (Toewijzingsaanvraag toewijzingsaanvraag, Voorkeur voorkeur) {
        School school = voorkeur.getSchool();

        voorkeur.setStatus(StatusVoorkeur.Toegewezen);
        wijsStudentVoorlopigToeAanSchool(toewijzingsaanvraag.getStudent(), school);
    }

    private void studentHeeftGeenVoorkeursschoolMeer(Toewijzingsaanvraag toewijzingsaanvraag) {
        ArrayList<School> vrijeScholen = getVrijeScholen();

        if(vrijeScholen.size() > 0) {
            wijsStudentToeAanDichtsteSchool(toewijzingsaanvraag, vrijeScholen);
        } else {
            wijsStudentToeAanThuisscholing(toewijzingsaanvraag);
        }
    }

    private void wijsStudentVoorlopigToeAanSchool(Student student, School school) {
        student.setToegewezenSchool(school);
        school.getStudenten().put(student.getRijksregisterNummer(), student);
    }

    private void wijsStudentToeAanDichtsteSchool(Toewijzingsaanvraag toewijzingsaanvraag,
                                                 ArrayList<School> vrijeScholen) {
        School dichtsteSchool = vindDichtsteSchool(vrijeScholen, toewijzingsaanvraag);
        wijsStudentVoorlopigToeAanSchool(toewijzingsaanvraag.getStudent(), dichtsteSchool);
    }

    private void wijsStudentToeAanThuisscholing(Toewijzingsaanvraag toewijzingsaanvraag) {
        toewijzingsaanvraag.setThuisscholingToegewezen(true);
        toewijzingsaanvraag.getStudent().setToegewezenSchool(null);
    }

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
