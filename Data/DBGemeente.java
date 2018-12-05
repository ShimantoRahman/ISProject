package Data;

import SysteemKlasses.Gemeente;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class DBGemeente {
    private static HashMap<Integer,Gemeente> gemeenten = null;

    public static HashMap<Integer, Gemeente> getGemeenten() throws DBException {
        if (gemeenten == null) {
            HashMap<Integer,Gemeente> gemeenten = new HashMap<>();
            Connection con = null;
            try {
                con = DBConnector.getConnection();
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

                String sql = "SELECT * "
                        + "FROM Gemeente ";

                ResultSet srs = stmt.executeQuery(sql);

                String naam;
                int gemeentenummer,postcode;
                double breedtegraad,lengtegraad;

                while (srs.next()) {
                    gemeentenummer = srs.getInt("Gemeentenummer");
                    postcode = srs.getInt("Postcode");
                    naam = srs.getString("Gemeente");
                    breedtegraad = srs.getDouble("Breedtegraad");
                    lengtegraad = srs.getDouble("Lengtegraad");

                    Gemeente gemeente = new Gemeente(naam,postcode,breedtegraad,lengtegraad);
                    gemeenten.put(gemeentenummer, gemeente);
                }

                DBConnector.closeConnection(con);

            }

            catch (Exception ex) {
                ex.printStackTrace();
                DBConnector.closeConnection(con);
                throw new DBException(ex);
            }
        }
        return gemeenten;
    }
}
