package Data;

import SysteemKlasses.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DBSchool {
    private static HashMap<Integer, School> scholenMap = null;

    public static HashMap<Integer, School> getScholenMap() throws DBException {
        if(scholenMap == null)
            getScholen();
        return scholenMap;
    }

    public static ArrayList<School> getScholen() throws DBException {
        scholenMap = new HashMap<>();
        ArrayList<School> scholen = new ArrayList<>();
        HashMap<Integer, Gemeente> gemeenten = DBGemeente.getGemeenten();

        Connection con = null;

        try {
            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String naam, straatNaam, huisnummer;
            int gemeentenummer, aantalPlaatsen, schoolnummer;

            String sql = " SELECT *"
                    + " FROM School ";

            ResultSet srs = stmt.executeQuery(sql);

            while (srs.next()) {
                naam = srs.getString("Schoolnaam");
                straatNaam = srs.getString("Straatnaam");
                huisnummer = srs.getString("Huisnummer");
                aantalPlaatsen = srs.getInt("Aantal_Plaatsen");
                gemeentenummer = srs.getInt("Gemeentenummer");
                schoolnummer = srs.getInt("Schoolnummer");
                Gemeente gemeente =  gemeenten.get(gemeentenummer);
                Adres adres = new Adres(straatNaam, huisnummer, gemeente);
                School school = new School(naam, adres, aantalPlaatsen);
                scholen.add(school);
                scholenMap.put(schoolnummer, school);
            }

            DBConnector.closeConnection(con);
            return scholen;
        }

        catch (Exception ex) {
            ex.printStackTrace();
            DBConnector.closeConnection(con);
            throw new DBException(ex);
        }
    }

    public static int getSchoolnummer(School school) throws DBException {
        Connection con = null;
        try {
            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql;
            ResultSet srs;

            String gemeenteNaam = school.getAdres().getGemeente().getNaam();
            int postcode = school.getAdres().getGemeente().getPostcode();
            String straatnaam = school.getAdres().getStraat();
            String huisnummer = school.getAdres().getStraatnummer();
            int gemeentenummer, schoolnummer;

            sql = "SELECT Gemeentenummer " +
                    "FROM Gemeente " +
                    "WHERE Postcode = " + postcode +
                    "AND Gemeente = " + gemeenteNaam;

            srs = stmt.executeQuery(sql);

            if(srs.next()) {
                gemeentenummer = srs.getInt("Gemeentenummer");
            } else {
                DBConnector.closeConnection(con);
                throw new DBException("Geen gemeentenummer gevonden voor de gemeente");
            }

            sql = "SELECT Schoolnummer " +
                    "FROM School " +
                    "WHERE Schoolnaam = " + school.getNaam() +
                    " AND Straatnaam = " + straatnaam +
                    " AND Huisnummer = " + huisnummer +
                    " AND Gemeentenummer = " + gemeentenummer;

            srs = stmt.executeQuery(sql);

            if(srs.next()) {
                schoolnummer = srs.getInt("Schoolnummer");
                return schoolnummer;
            } else {
                DBConnector.closeConnection(con);
                throw new DBException("Geen schoolnummer gevonden voor de school");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            DBConnector.closeConnection(con);
            throw new DBException(ex);
        }
    }
}
