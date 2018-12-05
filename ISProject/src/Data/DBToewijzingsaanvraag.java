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

    /*
    public static void save(Toewijzingsaanvraag t) throws DBException {
        Connection con = null;
        try {
            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT toewijzingsaanvraag nummer "
                    + "FROM Toewijzingsaanvraag "
                    + "WHERE toewijzingsaanvraagNummer = "
                    + t.getToewijzingsaanvraagNummer();
            ResultSet srs = stmt.executeQuery(sql);
            if (srs.next()) {
                // UPDATE
                sql = "UPDATE Toewijzingsaanvraag "
                        + "SET Status toewijzingsaanvraag  = '" + t.getStatusToewijzingsaanvraag() + "'"
                        + ", Rijksregisternummer Ouder = '" + t.getRijksregisternummerOuder() + "'"
                        + ", Rijksregisternummer Kind = '" + t.getRijksregisternummerKind() + "'"
                        + "WHERE toewijzingsaanvraag nummer = " + t.getToewijzingsaanvraagNummer()
                ;
                stmt.executeUpdate(sql);
            } else {
                // INSERT
                sql = "INSERT into Toewijzingsaanvraag "
                        + "VALUES ('"  + t.getToewijzingsaanvraagNummer()
                        + ", '" + t.getStatusToewijzingsaanvraag() + "'"
                        + ", '" + t.getRijksregisternummerOuder() + "'"
                        + ", '" + t.getRijksregisternummerKind() + "');";

                stmt.executeUpdate(sql);
            }


            DBConnector.closeConnection(con);
        } catch (Exception ex) {
            ex.printStackTrace();
            DBConnector.closeConnection(con);
            throw new DBException(ex);
        }
    }
    */
}

