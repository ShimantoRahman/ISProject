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
/*
    public static void saveStudent(Student s) throws DBException {
        Connection con = null;
        try {
            con = DBConnector.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT Rijksregisternummer Student "
                    + "FROM  "
                    + "WHERE Rijkssregisternummer Student = "
                    + s.getRijksregisterNummer();
            ResultSet srs = stmt.executeQuery(sql);
            if (srs.next()) {

                sql = "UPDATE Student "
                        + "SET Telefoonnummer = '" + s.getPhoneNumber()+ "'"
                        + ", Rijksregisternummer Ouder = '" + s.getOuder() + "'"
                        + "WHERE number = " + s.getRijksregisterNummer();
                ;
                stmt.executeUpdate(sql);
            } else {

                sql = "INSERT into Student "
                        + "(Rijksregisternummer Student, Telefoonnummer, Rijksregisternummer Ouder) "
                        + "VALUES (" + s.getRijksregisterNummer()
                        + ", '" + s.getPhoneNumber() + "'"
                        + ", '" + s.getOuder() + "')";
                stmt.executeUpdate(sql);
            }


            stmt.executeUpdate(sql);

            DBConnector.closeConnection(con);
        } catch (Exception ex) {
            ex.printStackTrace();
            DBConnector.closeConnection(con);
            throw new DBException(ex);
        }
    }
    */
}
