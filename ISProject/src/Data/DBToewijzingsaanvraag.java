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
    /*

    public static Toewijzingsaanvraag getToewijzingsaanvraag(int toewijzingsaanvraagNummer) throws DBException {
        Connection con = null;
        try{
            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT toewijzingsaanvraag nummer, Status toewijzingsaanvraag, Rijksregisternummer Ouder, Rijksregisternummer Kind "
                    + "FROM Toewijzingsaanvraag "
                    + "WHERE toewijzingsaanvraagNummer = " + toewijzingsaanvraagNummer;
            // let op de spatie na 'summary' en 'Students' in voorgaande SQL
            ResultSet srs = stmt.executeQuery(sql);
            String statusToewijzingsaanvraag, rijksregisternummerOuder, rijksregisternummerKind;
            int toewijzingsaanvraagnummer;


            if (srs.next()) {
                toewijzingsaanvraagnummer = srs.getInt("toewijzingsaanvraag nummer");
                statusToewijzingsaanvraag = srs.getString("Status toewijzingsaanvraag");
                rijksregisternummerOuder = srs.getString("rijksregisternummerOuder");
                rijksregisternummerKind= srs.getString("rijksregisternummerKind");


            } else {// we verwachten slechts 1 rij...
                DBConnector.closeConnection(con);
                return null;
            }

            Toewijzingsaanvraag toewijzingsaanvraag = new Toewijzingsaanvraag(toewijzingsaanvraagnummer,statusToewijzingsaanvraag,rijksregisternummerOuder,rijksregisternummerKind);
            ArrayList<String> majors = new ArrayList<String>();


            DBConnector.closeConnection(con);
            return toewijzingsaanvraag;
        } catch (Exception ex) {
            ex.printStackTrace();
            DBConnector.closeConnection(con);
            throw new DBException(ex);
        }

    }

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

    public static ArrayList<Toewijzingsaanvraag> getToewijzingsaanvragen() throws DBException {
        Connection con = null;
        try {
            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT Toewijzingsaanvraag nummer "
                    + "FROM Toewijzingsaanvraag";
            ResultSet srs = stmt.executeQuery(sql);

            ArrayList<Toewijzingsaanvraag> toewijzingsaanvragen = new ArrayList<Toewijzingsaanvraag>();
            while (srs.next())
                toewijzingsaanvragen.add(getToewijzingsaanvraag(srs.getInt("Toewijzingsaanvraag nummer")));

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
    */
}

