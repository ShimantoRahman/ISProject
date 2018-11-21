package Data;

import SysteemKlasses.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;


public class DB {

    /*
    public static HashMap<String, Ouder> getOuders() {
        Connection con = null;
        try {
            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT Persoon.Rijksregisternummer, Persoon.Naam, Persoon.Voornaam, Ouder.E-mailadres," +
                    " Ouder.Straatnaam, Ouder.Huisnummer, Ouder.Postcode, Ouder.Gemeente "
                    + "FROM Ouder JOIN ON Persoon.Rijksregisternummer = Ouder.Rijksregisternummer;";

            ResultSet srs = stmt.executeQuery(sql);

            HashMap<String, Ouder> ouders = new HashMap<>();
            String rijksregisternummerOuder, emailadres, straatnaam, huisnummer, gemeenteNaam, naam, voornaam;
            int postcode;

            if (srs.next()) {
                rijksregisternummerOuder = srs.getString("Rijksregisternummer");
                naam = srs.getString("Naam");
                voornaam = srs.getString("Voornaam");
                emailadres = srs.getString("E-mailadres");
                straatnaam = srs.getString("Straatnaam");
                huisnummer = srs.getString("Huisnummer");
                postcode = srs.getInt("Postcode");
                gemeenteNaam = srs.getString("Gemeente");

                Gemeente gemeente = new Gemeente(gemeenteNaam, postcode);
                Adres adres = new Adres(straatnaam, huisnummer, gemeente);
                Ouder ouder = new Ouder(rijksregisternummerOuder, naam, voornaam, emailadres, adres);
                ouders.put(ouder.getRijksregisterNummer(), ouder);

            } else {
                DBConnector.closeConnection(con);
                return null;
            }

            while(srs.next()) {
                rijksregisternummerOuder = srs.getString("Rijksregisternummer");
                naam = srs.getString("Naam");
                voornaam = srs.getString("Voornaam");
                emailadres = srs.getString("E-mailadres");
                straatnaam = srs.getString("Straatnaam");
                huisnummer = srs.getString("Huisnummer");
                postcode = srs.getInt("Postcode");
                gemeenteNaam = srs.getString("Gemeente");

                Gemeente gemeente = new Gemeente(gemeenteNaam, postcode);
                Adres adres = new Adres(straatnaam, huisnummer, gemeente);
                Ouder ouder = new Ouder(rijksregisternummerOuder, naam, voornaam, emailadres, adres);
                ouders.put(ouder.getRijksregisterNummer(), ouder);
            }

            DBConnector.closeConnection(con);
            return ouders;
        } catch (Exception ex) {
            ex.printStackTrace();
            DBConnector.closeConnection(con);
            throw new DBException(ex);
        }
    }
    */
}

