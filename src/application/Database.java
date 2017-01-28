/**
 * Created by VJP on 26/06/2016.
 *
 * Database handler.
 */

package application;

import javax.swing.*;
import java.sql.*;
import java.util.prefs.Preferences;

public class Database {

    static final private int        DEVMODE = 1;
    static final private String     DEVDB   = "C:/Users/VJP/Documents/Programming/JMoneyManager/DB/dev";
    static final private String     NODE    = "/vjp/money.manager";

    public static Connection conn = null;

    private static String getConnString() {
        Preferences prefs = Preferences.userRoot().node(NODE);
        String dbname;
        if (DEVMODE == 1)
            dbname = DEVDB;
        else
            dbname = prefs.get("DATABASE", "~/moneymanager");
        return "jdbc:h2:file:" + dbname;
    }

    public static void connect() {
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(getConnString());
        }
        catch (Exception e) {
            Application.fatalError(e);
        }
    }

    public static void close() {
        try {
            if(conn != null)
                conn.close();
        }
        catch (Exception e) {
            Application.fatalError(e);
        }
    }


}
