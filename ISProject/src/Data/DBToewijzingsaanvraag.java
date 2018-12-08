package Data;

import SysteemKlasses.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author jivgils
 */
public class DBToewijzingsaanvraag {

    public static HashMap<Integer, Toewijzingsaanvraag> getToewijzingsaanvragen() throws DBException {
        Connection con = null;
        try {
            HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen = new HashMap<>();
            HashMap<String, Student> studenten = DBStudent.getStudenten();
            HashMap<String, Ouder> ouders = DBOuder.getOuders();
            Voorkeur[] voorkeuren;

            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT * "
                    + "FROM Toewijzingsaanvraag";
            ResultSet srs = stmt.executeQuery(sql);

            String rijksregisternummerStudent, rijksregisternummerOuder;
            int toewijzingsaanvraagnummer;
            boolean thuisscholingToegewezen;
            StatusToewijzingsaanvraag status;

            while (srs.next()) {
                toewijzingsaanvraagnummer = srs.getInt("Toewijzingsaanvraagnummer");
                rijksregisternummerStudent = srs.getString("RijksregisternummerStudent");
                rijksregisternummerOuder = srs.getString("RijksregisternummerOuder");
                //TODO thuisscholing een boolean maken in database
                thuisscholingToegewezen = srs.getBoolean("ThuisscholingToegewezen");
                //TODO string van maken in database
                status = StatusToewijzingsaanvraag.valueOf(srs.getString("Status_toewijzingsaanvraag"));

                Ouder ouder = ouders.get(rijksregisternummerOuder);
                Student student = studenten.get(rijksregisternummerStudent);

                voorkeuren = getVoorkeuren(toewijzingsaanvraagnummer);

                Toewijzingsaanvraag toewijzingsaanvraag = new Toewijzingsaanvraag(toewijzingsaanvraagnummer,
                        ouder, student, voorkeuren, thuisscholingToegewezen, status);
                toewijzingsaanvragen.put(toewijzingsaanvraagnummer, toewijzingsaanvraag);
            }

            DBConnector.closeConnection(con);
            return toewijzingsaanvragen;
        } catch (DBException dbe) {
            dbe.printStackTrace();
            DBConnector.closeConnection(con);
            throw dbe;
        } catch (Exception ex) {
            ex.printStackTrace();
            DBConnector.closeConnection(con);
            throw new DBException(ex);
        }
    }

    private static Voorkeur[] getVoorkeuren(int toewijzingsaanvraagnummer) throws DBException {
        Voorkeur[] voorkeuren = new Voorkeur[3];
        voorkeuren[0] = getVoorkeur(toewijzingsaanvraagnummer, 1);
        voorkeuren[1] = getVoorkeur(toewijzingsaanvraagnummer, 2);
        voorkeuren[2] = getVoorkeur(toewijzingsaanvraagnummer, 3);
        return voorkeuren;
    }

    private static Voorkeur getVoorkeur(int toewijzingsaanvraagnummer, int rang) throws DBException {
        Connection con = null;
        try {
            HashMap<Integer, School> scholen = DBSchool.getScholenMap();

            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT * "
                    + "FROM BestaatUitVoorkeur " +
                    "WHERE Toewijzingsaanvraagnummer = " + toewijzingsaanvraagnummer + " " +
                    "AND Rang = " + rang;
            ResultSet srs = stmt.executeQuery(sql);

            StatusVoorkeur status;
            int schoolnummer;
            double afstand;
            boolean broerOfZusAanwezig;
            School school;

            if(srs.next()) {
                afstand = srs.getDouble("Afstand");
                //TODO boolean van maken in database
                broerOfZusAanwezig = srs.getBoolean("Broer_of_zus_aanwezig");
                //TODO string van maken
                status = StatusVoorkeur.valueOf(srs.getString("StatusVoorkeur"));
                schoolnummer = srs.getInt("Schoolnummer");
                school = scholen.get(schoolnummer);

                return new Voorkeur(school, afstand, broerOfZusAanwezig, status);
            } else {
                DBConnector.closeConnection(con);
                return null;
            }

        } catch (DBException dbe) {
            dbe.printStackTrace();
            DBConnector.closeConnection(con);
            throw dbe;
        } catch (Exception ex) {
            ex.printStackTrace();
            DBConnector.closeConnection(con);
            throw new DBException(ex);
        }
    }

    public static void setToewijzingsaanvragen(HashMap<Integer, Toewijzingsaanvraag> toewijzingsaanvragen)
            throws DBException {

        Connection con = null;
        try {
            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql;
            ResultSet srs;

            for (Toewijzingsaanvraag toewijzingsaanvraag: toewijzingsaanvragen.values()) {
                int aanvraagnummer = toewijzingsaanvraag.getToewijzingsaanvraagNummer();

                // kijken of toewijzingsaanvraag al in de data base zit
                sql = "SELECT * " +
                        "FROM Toewijzingsaanvraag " +
                        "WHERE Toewijzingsaanvraagnummer = " + aanvraagnummer;

                srs = stmt.executeQuery(sql);

                if(srs.next()) { // zit in de database
                    sql = "UPDATE Toewijzingsaanvraag " +
                            "SET " +
                            "StatusToewijzingsaanvraag = "
                            + toewijzingsaanvraag.getStatusToewijzingsaanvraag().toString() + "," +
                            "ThuisscholingToegewezen = " + toewijzingsaanvraag.isThuisscholingToegewezen() + "," +
                            "RijksregisternummerOuder = "
                            + toewijzingsaanvraag.getOuder().getRijksregisterNummer() + "," +
                            "RijksregisternummerStudent = "
                            + toewijzingsaanvraag.getStudent().getRijksregisterNummer() +
                            " WHERE Toewijzingsaanvraagnummer = " + aanvraagnummer;

                    stmt.executeQuery(sql);
                } else { // zit niet in de database
                    sql = "INSERT INTO Toewijzingsaanvraag " +
                            "VALUES (" +
                            aanvraagnummer + "," +
                            toewijzingsaanvraag.getStatusToewijzingsaanvraag().toString() + "," +
                            toewijzingsaanvraag.isThuisscholingToegewezen() + "," +
                            toewijzingsaanvraag.getStudent().getRijksregisterNummer() + "," +
                            toewijzingsaanvraag.getOuder().getRijksregisterNummer() +
                            ")";

                    stmt.executeQuery(sql);
                    setVoorkeuren(toewijzingsaanvraag.getVoorkeuren(), aanvraagnummer);
                }
            }
            DBConnector.closeConnection(con);
        }

        catch (Exception ex) {
            ex.printStackTrace();
            DBConnector.closeConnection(con);
            throw new DBException(ex);
        }
    }

    private static void setVoorkeuren(Voorkeur[] voorkeuren, int aanvraagnummer) throws DBException {
        for (int i = 0; i < voorkeuren.length; i++)
            setVoorkeur(voorkeuren[i], aanvraagnummer, i+1);
    }

    private static void setVoorkeur(Voorkeur voorkeur, int aanvraagnummer, int rang) throws DBException{
        Connection con = null;
        try {
            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql;
            int schoolnummer;

            schoolnummer = DBSchool.getSchoolnummer(voorkeur.getSchool());

            sql = "INSERT INTO BestaatUitVoorkeur " +
                    "(" +
                    aanvraagnummer + "," +
                    schoolnummer + "," +
                    voorkeur.getStatus().toString() + "," +
                    voorkeur.getAfstand() + "," +
                    voorkeur.isBroerOfZusAanwezig() + "," +
                    rang +
                    ")";

            stmt.executeQuery(sql);

            DBConnector.closeConnection(con);
        }

        catch (Exception ex) {
            ex.printStackTrace();
            DBConnector.closeConnection(con);
            throw new DBException(ex);
        }
    }
}

