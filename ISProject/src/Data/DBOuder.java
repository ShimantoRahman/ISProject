package Data;

import SysteemKlasses.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;


public class DBOuder {
    private static HashMap<String, Ouder> ouders = null;

    public static HashMap<String, Ouder> getOuders() throws DBException {
        if (ouders == null) {
            ouders = new HashMap<>();
            HashMap<Integer, Gemeente> gemeenten = DBGemeente.getGemeenten();

            Connection con = null;

            try {
                con = DBConnector.getConnection();
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

                String rijksregisternummer, naam, voornaam, straatNaam, huisnummer, emailadres;
                int gemeentenummer;

                String sql = "SELECT Persoon.Rijksregisternummer, Persoon.Voornaam, Persoon.Naam," +
                        " Ouder.Emailadres, Ouder.Straatnaam, Ouder.Huisnummer, Ouder.Gemeentenummer"
                        + " FROM Persoon JOIN Ouder ON Persoon.Rijksregisternummer = Ouder.Rijksregisternummer";

                ResultSet srs = stmt.executeQuery(sql);

                while (srs.next()) {
                    rijksregisternummer = srs.getString("Persoon.Rijksregisternummer");
                    naam = srs.getString("Persoon.Naam");
                    voornaam = srs.getString("Persoon.Voornaam");
                    emailadres = srs.getString("Ouder.Emailadres");
                    straatNaam = srs.getString("Ouder.Straatnaam");
                    huisnummer = srs.getString("Ouder.Huisnummer");
                    gemeentenummer = srs.getInt("Ouder.Gemeentenummer");
                    Gemeente gemeente =  gemeenten.get(gemeentenummer);
                    Adres adres = new Adres(straatNaam, huisnummer, gemeente);
                    Ouder ouder = new Ouder(rijksregisternummer, naam, voornaam, emailadres, adres);
                    ouders.put(ouder.getRijksregisterNummer(), ouder);
                }

                DBConnector.closeConnection(con);
            }

            catch (Exception ex) {
                ex.printStackTrace();
                DBConnector.closeConnection(con);
                throw new DBException(ex);
            }
        }
        return ouders;
    }
}
