package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Deze klasse heb ik aangemaakt omdat het openen en sluiten van een connectie
 * niet enkel voor de DBStudent klasse zou zijn, maar voor alle klasse in de
 * persistence layer.
 *
 * @author stevmert
 */
public class DBConnector {

    //TODO pas dit aan
    private static final String DB_NAME = "BINFG04";//vul hier uw databank naam in
    private static final String DB_PASS = "ZUV1ISnN";//vul hier uw databank paswoord in

    public static Connection getConnection() throws DBException {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String protocol = "jdbc";
            String subProtocol = "mysql";
            String subName = "//mysqlha2.ugent.be/" + DB_NAME + "?user=" + DB_NAME + "&password=" + DB_PASS;
            String URL = protocol + ":" + subProtocol + ":" + subName;
            String myDatabase = "//mysqlha2.ugent.be/" + DB_NAME;
            String URL2 = protocol + ":" + subProtocol + ":" + myDatabase;

            //con = DriverManager.getConnection(URL);
            con = DriverManager.getConnection(URL2, DB_NAME, DB_PASS);
            return con;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            closeConnection(con);
            throw new DBException(sqle);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            closeConnection(con);
            throw new DBException(cnfe);
        } catch (Exception ex) {
            ex.printStackTrace();
            closeConnection(con);
            throw new DBException(ex);
        }
    }

    public static void closeConnection(Connection con) {
        try {
            if(con != null)
                con.close();
        } catch (SQLException sqle) {
            //do nothing
        }
    }
}
