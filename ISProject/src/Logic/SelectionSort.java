package Logic;

import SysteemKlasses.*;

public class SelectionSort implements ISortingAlgoritm {
    // instantie variabelen
    private IAfstandBerekeningFormule afstandBerekeningFormule;

    //constructors
    public SelectionSort(IAfstandBerekeningFormule afstandBerekeningFormule) {
        this.afstandBerekeningFormule = afstandBerekeningFormule;
    }

    // public methoden

    // sorteert een array van toewijzingsaanvragen waarbij de eerste aanvraag de meest gunstig is
    // en de laatste de minst gunstig
    public Toewijzingsaanvraag[] sort(Toewijzingsaanvraag[] aanvragen, School school)
            throws ToewijzingsaanvraagException{
        for (int i = 0; i < aanvragen.length - 1; i++) {

            int index = i;
            Voorkeur besteVoorkeur = getToegewezenVoorkeur(aanvragen[index], school);

            for (int j = i + 1; j < aanvragen.length; j++) {
                Voorkeur voorkeur = getToegewezenVoorkeur(aanvragen[j], school);

                // beide hebben broer of zus aanwezig of beide hebben geen broer of zus aanwezig
                // kijk naar afstand van school
                if(Voorkeur.beideBroerOfZusAanwezig(besteVoorkeur, voorkeur) ||
                        Voorkeur.beideBroerOfZusNietAanwezig(besteVoorkeur, voorkeur)) {

                    // als de geteste voorkeur dichter bij de school ligt
                    // maak geteste voorkeur de nieuwe beste voorkeur
                    if(besteVoorkeur.getAfstand() > voorkeur.getAfstand()) {
                        index = j;
                        besteVoorkeur = voorkeur;
                    }

                    // als beide afstanden gelijk zijn
                    // loting voor te zien wie de beste voorkeur wordt
                    else if (besteVoorkeur.getAfstand() == voorkeur.getAfstand()) {
                        double randomGetal = Math.random();
                        if(randomGetal > 0.5)
                            besteVoorkeur = voorkeur;
                    }

                    // als de beste voorkeur dichter bij de school ligt
                    // doe niets
                }

                // broer of zus is niet aanwezig bij beste voorkeur maar wel bij geteste voorkeur
                // maak geteste voorkeur de nieuwe beste voorkeur
                else if(!besteVoorkeur.isBroerOfZusAanwezig() && voorkeur.isBroerOfZusAanwezig()) {
                    index = j;
                    besteVoorkeur = voorkeur;
                }

                // broer of zus is aanwezig bij temporary voorkeur maar niet bij geteste voorkeur
                // doe niets
            }

            //swap waarden
            Toewijzingsaanvraag tempAanvraag = aanvragen[i];
            aanvragen[i] = aanvragen[index];
            aanvragen[index] = tempAanvraag;

        } return aanvragen;
    }

    // private hulpmethoden

    // retourneert de voorkeur waaraan de student voorlopig is toegewezen
    // zou normaal gezien altijd een waarde moeten retourneren anders gooit de method een exception
    private Voorkeur getToegewezenVoorkeur(Toewijzingsaanvraag aanvraag, School school) throws ToewijzingsaanvraagException {
        for (Voorkeur voorkeur: aanvraag.getVoorkeuren()) {
            if(voorkeur.getStatus() == StatusVoorkeur.Toegewezen)
                return voorkeur;
        }
        if(aanvraag.getStudent().getToegewezenSchool().equals(school)) {
            double afstand = berekenAfstandTussenOuderEnSchool(aanvraag.getOuder(), school);
            // maak dummy voorkeur aan
            return new Voorkeur(school, afstand, false);
        }
        throw new ToewijzingsaanvraagException("In methode 'getToegewezenVoorkeur' is een aanvraag meegegeven " +
                "zonder voorlopig toegewezen school");
    }

    private double berekenAfstandTussenOuderEnSchool(Ouder ouder, School school) {
        double breedtegraadOuder =  ouder.getAdres().getGemeente().getBreedtegraad();
        double lengtegraadOuder =  ouder.getAdres().getGemeente().getLengtegraad();
        double breedtegraadSchool =  school.getAdres().getGemeente().getBreedtegraad();
        double lengtegraadSchool =  school.getAdres().getGemeente().getLengtegraad();
        afstandBerekeningFormule.setPunten(breedtegraadOuder, lengtegraadOuder, breedtegraadSchool, lengtegraadSchool);
        return afstandBerekeningFormule.getAfstand();
    }
}
