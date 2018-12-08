package Data;

import SysteemKlasses.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class DBStudent {
    private static HashMap<String, Student> studenten = null;

    public static HashMap<String, Student> getStudenten() throws DBException {
        if (studenten == null) {
            studenten = new HashMap<>();
            HashMap<String, Ouder> ouders = DBOuder.getOuders();
            HashMap<Integer, School> scholen = DBSchool.getScholenMap();
            Connection con = null;
            try {
                con = DBConnector.getConnection();
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

                String sql = "SELECT Persoon.Rijksregisternummer, Persoon.Voornaam, Persoon.Naam," +
                        " Student.Telefoonnummer, Student.RijksregisternummerOuder, Student.Schoolnummer"
                        + "FROM Persoon JOIN Student ON Persoon.Rijksregisternummer = Student.Rijksregisternummer";

                ResultSet srs = stmt.executeQuery(sql);

                String rijksregisternummer, naam, voornaam, telefoonnummer, rijksregisternummerOuder;
                int schoolnummer;

                while (srs.next()) {
                    rijksregisternummer = srs.getString("Persoon.Rijksregisternummer");
                    naam = srs.getString("Persoon.Naam");
                    voornaam = srs.getString("Persoon.Voornaam");
                    telefoonnummer = srs.getString("Student.Telefoonnummer");
                    rijksregisternummerOuder = srs.getString("Student.RijksregisternummerOuder");
                    schoolnummer = srs.getInt("Student.Schoolnummer");

                    School school =  scholen.get(schoolnummer);
                    Ouder ouder = ouders.get(rijksregisternummerOuder);
                    Student student = new Student(rijksregisternummer, naam, voornaam, telefoonnummer, ouder, school);
                    studenten.put(rijksregisternummer, student);
                }

                DBConnector.closeConnection(con);

            }

            catch (Exception ex)
            {
                ex.printStackTrace();
                DBConnector.closeConnection(con);
                throw new DBException(ex);
            }
        }
        return studenten;
    }

    // TODO what als programma afgesloten word in ontwerp
    // TODO toewijzingsaanvraag niet opnemen in database
    public static void setStudenten(HashMap<String, Student> studentenMap) throws DBException {
        Connection con = null;
        try {
            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql;
            ResultSet srs;

            for (Student student: studentenMap.values()) {
                Ouder ouder = student.getOuder();
                School school = student.getToegewezenSchool();

                if(ouder != null) {
                    sql = "UPDATE Student " +
                            "SET RijksregisternummerOuder = " + ouder.getRijksregisterNummer() +
                            " WHERE Rijksregisternummer = " + student.getRijksregisterNummer();
                    stmt.executeQuery(sql);
                } else {
                    sql = "UPDATE Student " +
                            "SET RijksregisternummerOuder = NULL" +
                            "WHERE Rijksregisternummer = " + student.getRijksregisterNummer();
                    stmt.executeQuery(sql);
                }

                if(school != null) {
                    int schoolnummer = DBSchool.getSchoolnummer(school);

                    sql = "UPDATE Student " +
                            "SET Schoolnummer = " + schoolnummer +
                            " WHERE Rijksregisternummer = " + student.getRijksregisterNummer();

                    stmt.executeQuery(sql);
                } else {
                    sql = "UPDATE Student " +
                            "SET Schoolnummer = NULL " +
                            "WHERE Rijksregisternummer = " + student.getRijksregisterNummer();

                    stmt.executeQuery(sql);
                }
            }
            DBConnector.closeConnection(con);
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
            DBConnector.closeConnection(con);
            throw new DBException(ex);
        }
    }
}
