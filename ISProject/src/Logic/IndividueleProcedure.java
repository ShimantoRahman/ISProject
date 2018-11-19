package Logic;

import SysteemKlasses.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class IndividueleProcedure {
    // <Rijksregisternummer, Ouder object>
    private static HashMap<String, Ouder> ouders;
    // <Rijksregisternummer, Student object>
    private static HashMap<String, Student> studenten;
    // <Toewijzingsaanvraagnummer, Toewijzingsaanvraag object>
    private static HashMap<String, Toewijzingsaanvraag> toewijzingsaanvragen;
    private static HashSet<School> scholen;

    public static void setOuders(HashMap<String, Ouder> ouders) {
        IndividueleProcedure.ouders = ouders;
    }

    public static void setStudenten(HashMap<String, Student> studenten) {
        IndividueleProcedure.studenten = studenten;
    }

    public static void setToewijzingsaanvragen(HashMap<String, Toewijzingsaanvraag> toewijzingsaanvragen) {
        IndividueleProcedure.toewijzingsaanvragen = toewijzingsaanvragen;
    }

    public static void setScholen(HashSet<School> scholen) {
        IndividueleProcedure.scholen = scholen;
    }

    // voert de individuele toewijzingsprocedure uit
    public static void individueleProcedure(Toewijzingsaanvraag toewijzingsaanvraag) {
        Student student = toewijzingsaanvraag.getStudent();
        Voorkeur voorkeur = volgendeVoorkeursschool(toewijzingsaanvraag);
        School school = voorkeur.getSchool();

        // student heeft nog een voorkeursschool die nog niet geweigerd is
        if(voorkeur != null) {
            // kijkt of er genoeg of te weinig plaatsen zijn op de voorkeursschool en voert toepasselijke code toe
            analyzePlaatsenSchool(toewijzingsaanvraag, voorkeur, school, student);
        }

        // TODO student heeft geen voorkeurrschool meer die nog niet geweigerd is
        else {
            // kijkt of er nog vrije scholen zijn en voert toepasselijke code toe
            analyzeVrijeScholen(toewijzingsaanvraag, student);
        }
    }

    private static void analyzePlaatsenSchool(Toewijzingsaanvraag toewijzingsaanvraag, Voorkeur voorkeur, School school, Student student) {
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

    private static void rangschikkingEnToewijzing(Voorkeur voorkeur, School school) {
        try {
            // zoek alle toewijzingsaanvragen die de huidige school als toegewezen school hebben
            Toewijzingsaanvraag[] aanvragen = getToewijzingsaanvragenAanSchool(school);

            // rangschik alle studenten
            SelectionSort.sorteer(aanvragen);

            // zet de statusVoorkeur van alle studenten die gunstig gerangschikt zijn op toegewezen
            // zet hun toegewezen school op deze school
            // zet de student bij de haspmap toegewezen studenten bij de school
            // TODO is deze stap nodig?
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

    private static void analyzeVrijeScholen(Toewijzingsaanvraag toewijzingsaanvraag, Student student) {
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
            // TODO thuisonderwijs waarde null geven of andere oplossing?
            toewijzingsaanvraag.setToegewezenSchool(null);
        }
    }

    // retourneert de volgende voorkeursschool die nog niet is geweigerd
    // als alle voorkeursscholen geweigerd zijn, dan retourneert de methode null
    private static Voorkeur volgendeVoorkeursschool(Toewijzingsaanvraag toewijzingsaanvraag) {
        for (Voorkeur voorkeur: toewijzingsaanvraag.getVoorkeuren()) {
            if(voorkeur.getStatus() == StatusVoorkeur.Toegewezen || voorkeur.getStatus() == StatusVoorkeur.Undefined)
                return voorkeur;
        } return null;
    }

    // retourneert of een school genoeg plaatsen heeft
    private static boolean heeftGenoegPlaatsen(School school) {
        return school.getAantalPlaatsen() >= getStudentenOpSchool(school).size();
    }

    // retourneert een arraylist met alle studenten op die school
    private static ArrayList<Student> getStudentenOpSchool(School school) {
        ArrayList<Student> studenten = new ArrayList<>();
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values()) {
            if(toewijzingsaanvraag.getToegewezenSchool().equals(school))
                studenten.add(toewijzingsaanvraag.getStudent());
        } return studenten;
    }

    // retourneert een arraylist met alle toewijzingsaanvragen die toegewezen zijn aan die school
    private static Toewijzingsaanvraag[] getToewijzingsaanvragenAanSchool(School school) {
        ArrayList<Toewijzingsaanvraag> aanvragen = new ArrayList<>();
        for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values()) {
            if(toewijzingsaanvraag.getToegewezenSchool().equals(school))
                aanvragen.add(toewijzingsaanvraag);
        } return aanvragen.toArray(new Toewijzingsaanvraag[aanvragen.size()]);
    }

    // retourneert alle scholen die nog vrije plaatsen hebben in een arraylist
    private static ArrayList<School> getVrijeScholen() {
        ArrayList<School> vrijeScholen = new ArrayList<>();
        for (School school: scholen) {
            if(school.getAantalPlaatsen() > school.getStudenten().size())
                vrijeScholen.add(school);
        } return vrijeScholen;
    }

    private static School vindDichtsteSchool(ArrayList<School> scholen, Toewijzingsaanvraag toewijzingsaanvraag) {
        Adres adresOuder = toewijzingsaanvraag.getOuder().getAdres();

        // TODO zet de kleinsteAfstand op de afstand tussen de ouder en de eerste school in de lijst
        // zet dichtste school als de eerste school in de lijst
        School dichtsteSchool = scholen.get(0);
        double kleinsteAfstand = 0;

        for (School school: scholen) {
            Adres adresSchool = school.getAdres();

            // TODO vind lengte-en breedtegraden van de adressen


            // TODO gebruik haversin formule om afstand te berekenen
            double afstand = 0;
            if(afstand < kleinsteAfstand) {
                kleinsteAfstand = afstand;
                dichtsteSchool = school;
            }

        } return dichtsteSchool;
    }
}
