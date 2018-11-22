package SysteemKlasses;

import GUI.ControllerInlogScherm;
import Logic.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    private static Stage primaryStage;
    private static HashMap<String, Student> studenten;
    private static HashMap<String, Ouder> ouders;
    private static HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen;
    private static ArrayList<School> scholen;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Main.primaryStage = primaryStage;
    }

    public static HashMap<String, Student> getStudenten() {
        return studenten;
    }

    public static void setStudenten(HashMap<String, Student> studenten) {
        Main.studenten = studenten;
    }

    public static HashMap<String, Ouder> getOuders() {
        return ouders;
    }

    public static void setOuders(HashMap<String, Ouder> ouders) {
        Main.ouders = ouders;
    }

    public static HashMap<Integer, Toewijzingsaanvraag> getToewijzingsaanvragen() {
        return toewijzingsaanvragen;
    }

    public static void setToewijzingsaanvragen(HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen) {
        Main.toewijzingsaanvragen = toewijzingsaanvragen;
    }

    public static ArrayList<School> getScholen() {
        return scholen;
    }

    public static void setScholen(ArrayList<School> scholen) {
        Main.scholen = scholen;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO lees alle data in
        //setStudenten(Data.getStudenten());
        //setOuders(Data.getOuders());
        //setToewijzingsaanvragen(Data.getToewijzingsaanvragen());
        //setScholen(Data.getScholen());

        // voorlopig gewoon instantieren
        studenten = new HashMap<>();
        ouders = new HashMap<>();
        toewijzingsaanvragen = new HashMap<>();
        scholen = new ArrayList<>();

        //testScenario();

        // start GUI
        this.primaryStage = primaryStage;
        //Parent root = FXMLLoader.load(getClass().getResource("InlogScherm.fxml"));
        primaryStage.setTitle("Toewijzingsaanvraag");
        primaryStage.setResizable(false);
        //primaryStage.setScene(new Scene(root));
        primaryStage.setScene(ControllerInlogScherm.getScene());
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private double berekenAfstand(Ouder ouder, School school) {
        double breedtegraadOuder =  ouder.getAdres().getGemeente().getBreedtegraad();
        double lengtegraadOuder =  ouder.getAdres().getGemeente().getLengtegraad();
        double breedtegraadSchool =  school.getAdres().getGemeente().getBreedtegraad();
        double lengtegraadSchool =  school.getAdres().getGemeente().getLengtegraad();
        HaversinFormule formule = new HaversinFormule();
        formule.setPunten(breedtegraadOuder, lengtegraadOuder, breedtegraadSchool, lengtegraadSchool);
        return formule.getAfstand();
    }

    private void testScenario() {
        // testdata
        testdata();

        // algoritme
        ISortingAlgoritm sortingAlgoritm = new SelectionSort();
        IAfstandBerekeningFormule afstandBerekeningFormule = new HaversinFormule();
        IIndividueleProcedure individueleProcedure = new BroerZusAfstandLotingIndividueleProcedure(ouders, studenten, toewijzingsaanvragen, scholen, sortingAlgoritm, afstandBerekeningFormule);
        IToewijzingsAlgoritme toewijzingsAlgoritme = new StudentProposingIToewijzingsAlgoritme(ouders, studenten, toewijzingsaanvragen, scholen, individueleProcedure);
        toewijzingsAlgoritme.startToewijzingsProcedure();

        System.out.println("");

        for (Toewijzingsaanvraag ta: toewijzingsaanvragen.values()) {
            System.out.println(String.format("toewijzingsaanvraagnummer: %d\nOuder naam: %s %s\n" +
                            "Student naam: %s %s\nToegewezen school: %s\nThuisonderwijs: %s\n",
                    ta.getToewijzingsaanvraagNummer(), ta.getOuder().getVoornaam(), ta.getOuder().getNaam(),
                    ta.getStudent().getVoornaam(), ta.getStudent().getNaam(), ta.getToegewezenSchool(),
                    ta.isThuisscholingToegewezen()));
        }

        // programma afsluiten
        System.exit(0);
    }

    private void testdata() {
        Student shimanto = new Student("1", "Rahman", "Shimanto", "123");
        Student jana = new Student("2", "Surmont", "Jana", "234");
        Student jordy = new Student("3", "Cousaert", "Jordy", "345");
        Student yari = new Student("4", "De Langhe", "Yari", "456");
        Student jeroen = new Student("5", "Van Gils", "Jeroen", "567");
        Student arthur = new Student("6", "Verstaete", "Arthur", "678");
        Student celine = new Student("7", "Vandenbroucke", "Céline", "789");

        System.out.println("Studenten gemaakt");

        studenten.put(shimanto.getRijksregisterNummer(), shimanto);
        studenten.put(jana.getRijksregisterNummer(), jana);
        studenten.put(jordy.getRijksregisterNummer(), jordy);
        studenten.put(yari.getRijksregisterNummer(), yari);
        studenten.put(jeroen.getRijksregisterNummer(), jeroen);
        studenten.put(arthur.getRijksregisterNummer(), arthur);
        studenten.put(celine.getRijksregisterNummer(), celine);

        System.out.println("Studenten toegevoegd");

        Gemeente gent = new Gemeente("Gent", 9000, 3.7216960000, 51.0540100000);
        Gemeente brugge = new Gemeente("Brugge", 8000, 3.2252300000, 51.2094110000);
        Gemeente antwerpen = new Gemeente("Antwerpen", 2040, 4.3997220000, 51.2205810000);
        Gemeente berendrecht = new Gemeente("Berendrecht", 2040, 4.3144480000, 51.3401140000);
        Gemeente hasselt = new Gemeente("Hasselt", 3500, 5.3382270000, 50.9297330000);
        Gemeente brussel = new Gemeente("Brussel", 1000, 4.3299990000, 50.8300010000);

        System.out.println("Gemeentes gemaakt");

        Adres adres1 = new Adres("Veldstraat", "1", gent);
        Adres adres2 = new Adres("Veldstraat", "2", gent);
        Adres adres3 = new Adres("Veldstraat", "3", gent);
        Adres adres4 = new Adres("Moerstraat", "1", brugge);
        Adres adres5 = new Adres("Moerstraat", "2", brugge);
        Adres adres6 = new Adres("Moerstraat", "3", brugge);
        Adres adres7 = new Adres("Vrijheidstraat", "1", antwerpen);
        Adres adres8 = new Adres("Vrijheidstraat", "2", antwerpen);
        Adres adres9 = new Adres("Vrijheidstraat", "3", antwerpen);
        Adres adres10 = new Adres("Oude Papenstraat", "1", berendrecht);
        Adres adres11 = new Adres("Oude Papenstraat", "2", berendrecht);
        Adres adres12 = new Adres("Oude Papenstraat", "3", berendrecht);
        Adres adres13 = new Adres("Rozenstraat", "1", hasselt);
        Adres adres14 = new Adres("Rozenstraat", "2", hasselt);
        Adres adres15 = new Adres("Rozenstraat", "3", hasselt);
        Adres adres16 = new Adres("Bisschopstraat", "1", brussel);
        Adres adres17 = new Adres("Bisschopstraat", "2", brussel);
        Adres adres18 = new Adres("Bisschopstraat", "3", brussel);

        System.out.println("Adressen gemaakt");

        Ouder ouder1 = new Ouder("100","Marilyn","Monroe","",adres1);
        Ouder ouder2 = new Ouder("101","Lincoln","Abraham","",adres2);
        Ouder ouder3 = new Ouder("102","Teresa","Moeder","",adres4);
        Ouder ouder4 = new Ouder("103","F. Kennedy","John","",adres5);
        Ouder ouder5 = new Ouder("104","Luther King","Martin","",adres7);

        System.out.println("Ouders gemaakt");

        ouders.put(ouder1.getRijksregisterNummer(), ouder1);
        ouders.put(ouder2.getRijksregisterNummer(), ouder2);
        ouders.put(ouder3.getRijksregisterNummer(), ouder3);
        ouders.put(ouder4.getRijksregisterNummer(), ouder4);
        ouders.put(ouder5.getRijksregisterNummer(), ouder5);

        System.out.println("Ouders toegevoegd");

        School voskenslaan = new School("Voskenslaan", adres3,2);
        School sintJozefsinstituut = new School("Sint-Jozefsinstituut", adres6,2);
        School koninklijkAtheneumAntwerpen = new School("Koninklijk Atheneum Antwerpen", adres9, 2);

        System.out.println("Scholen gemaakt");

        scholen.add(voskenslaan);
        scholen.add(sintJozefsinstituut);
        scholen.add(koninklijkAtheneumAntwerpen);

        System.out.println("Scholen toegevoegd");

        Voorkeur[] voorkeurenShimanto = new Voorkeur[3];
        Voorkeur[] voorkeurenJana = new Voorkeur[3];
        Voorkeur[] voorkeurenJordy = new Voorkeur[3];
        Voorkeur[] voorkeurenYari = new Voorkeur[3];
        Voorkeur[] voorkeurenJeroen = new Voorkeur[3];
        Voorkeur[] voorkeurenArthur = new Voorkeur[3];
        Voorkeur[] voorkeurenCeline = new Voorkeur[3];

        System.out.println("Voorkeuren geinstantieerd");

        voorkeurenShimanto[0] = new Voorkeur(voskenslaan, berekenAfstand(ouder1, voskenslaan), true);
        voorkeurenShimanto[1] = new Voorkeur(sintJozefsinstituut, berekenAfstand(ouder1, sintJozefsinstituut), true);
        voorkeurenShimanto[2] = new Voorkeur(koninklijkAtheneumAntwerpen, berekenAfstand(ouder1, koninklijkAtheneumAntwerpen), true);

        voorkeurenJana[0] = new Voorkeur(sintJozefsinstituut, berekenAfstand(ouder1, sintJozefsinstituut), true);
        voorkeurenJana[1] = new Voorkeur(voskenslaan, berekenAfstand(ouder1, voskenslaan), true);
        voorkeurenJana[2] = new Voorkeur(koninklijkAtheneumAntwerpen, berekenAfstand(ouder1, koninklijkAtheneumAntwerpen), true);

        voorkeurenJordy[0] = new Voorkeur(koninklijkAtheneumAntwerpen, berekenAfstand(ouder2, koninklijkAtheneumAntwerpen), true);
        voorkeurenJordy[1] = new Voorkeur(sintJozefsinstituut, berekenAfstand(ouder2, sintJozefsinstituut), true);
        voorkeurenJordy[2] = new Voorkeur(voskenslaan, berekenAfstand(ouder2, voskenslaan), true);

        voorkeurenYari[0] = new Voorkeur(sintJozefsinstituut, berekenAfstand(ouder2, sintJozefsinstituut), true);
        voorkeurenYari[1] = new Voorkeur(koninklijkAtheneumAntwerpen, berekenAfstand(ouder2, koninklijkAtheneumAntwerpen), true);
        voorkeurenYari[2] = new Voorkeur(voskenslaan, berekenAfstand(ouder2, voskenslaan), true);

        voorkeurenJeroen[0] = new Voorkeur(koninklijkAtheneumAntwerpen, berekenAfstand(ouder3, koninklijkAtheneumAntwerpen), true);
        voorkeurenJeroen[1] = new Voorkeur(voskenslaan, berekenAfstand(ouder3, voskenslaan), true);
        voorkeurenJeroen[2] = new Voorkeur(sintJozefsinstituut, berekenAfstand(ouder3, sintJozefsinstituut), true);

        voorkeurenArthur[0] = new Voorkeur(voskenslaan, berekenAfstand(ouder4, voskenslaan), false);
        voorkeurenArthur[1] = new Voorkeur(sintJozefsinstituut, berekenAfstand(ouder4, sintJozefsinstituut), false);
        voorkeurenArthur[2] = new Voorkeur(koninklijkAtheneumAntwerpen, berekenAfstand(ouder4, koninklijkAtheneumAntwerpen), false);

        voorkeurenCeline[0] = new Voorkeur(voskenslaan, berekenAfstand(ouder5, voskenslaan), false);
        voorkeurenCeline[1] = new Voorkeur(sintJozefsinstituut, berekenAfstand(ouder5, sintJozefsinstituut), false);
        voorkeurenCeline[2] = new Voorkeur(koninklijkAtheneumAntwerpen, berekenAfstand(ouder5, koninklijkAtheneumAntwerpen), false);

        System.out.println("Voorkeur gemaakt");

        Toewijzingsaanvraag aanvraag1 = new Toewijzingsaanvraag(ouder1);
        aanvraag1.setStudent(shimanto);
        aanvraag1.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Ingediend);
        aanvraag1.setVoorkeuren(voorkeurenShimanto);
        toewijzingsaanvragen.put(aanvraag1.getToewijzingsaanvraagNummer(), aanvraag1);

        Toewijzingsaanvraag aanvraag2 = new Toewijzingsaanvraag(ouder1);
        aanvraag2.setStudent(jana);
        aanvraag2.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Ingediend);
        aanvraag2.setVoorkeuren(voorkeurenJana);
        toewijzingsaanvragen.put(aanvraag2.getToewijzingsaanvraagNummer(), aanvraag2);

        Toewijzingsaanvraag aanvraag3 = new Toewijzingsaanvraag(ouder2);
        aanvraag3.setStudent(jordy);
        aanvraag3.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Ingediend);
        aanvraag3.setVoorkeuren(voorkeurenJordy);
        toewijzingsaanvragen.put(aanvraag3.getToewijzingsaanvraagNummer(), aanvraag3);

        Toewijzingsaanvraag aanvraag4 = new Toewijzingsaanvraag(ouder2);
        aanvraag4.setStudent(yari);
        aanvraag4.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Ingediend);
        aanvraag4.setVoorkeuren(voorkeurenYari);
        toewijzingsaanvragen.put(aanvraag4.getToewijzingsaanvraagNummer(), aanvraag4);

        Toewijzingsaanvraag aanvraag5 = new Toewijzingsaanvraag(ouder3);
        aanvraag5.setStudent(jeroen);
        aanvraag5.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Ingediend);
        aanvraag5.setVoorkeuren(voorkeurenJeroen);
        toewijzingsaanvragen.put(aanvraag5.getToewijzingsaanvraagNummer(), aanvraag5);

        Toewijzingsaanvraag aanvraag6 = new Toewijzingsaanvraag(ouder4);
        aanvraag6.setStudent(arthur);
        aanvraag6.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Ingediend);
        aanvraag6.setVoorkeuren(voorkeurenArthur);
        toewijzingsaanvragen.put(aanvraag6.getToewijzingsaanvraagNummer(), aanvraag6);

        Toewijzingsaanvraag aanvraag7 = new Toewijzingsaanvraag(ouder5);
        aanvraag7.setStudent(celine);
        aanvraag7.setStatusToewijzingsaanvraag(StatusToewijzingsaanvraag.Ingediend);
        aanvraag7.setVoorkeuren(voorkeurenCeline);
        toewijzingsaanvragen.put(aanvraag7.getToewijzingsaanvraagNummer(), aanvraag7);

        System.out.println("Aanvragen gemaakt en toegevoegd");
    }
}
