package application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by VJP on 18/09/2016.
 */
public class Settings {

    private static PreparedStatement getSettingIntStmt;
    private static PreparedStatement getSettingStrStmt;
    private static PreparedStatement putSettingStmt;

    static {
        try {
            getSettingIntStmt = Database.conn.prepareStatement("SELECT SETTING_INT from SETTINGS WHERE setting_key = ?");
            getSettingStrStmt = Database.conn.prepareStatement("SELECT SETTING_STRING from SETTINGS WHERE setting_key = ?");
            putSettingStmt = Database.conn.prepareStatement("MERGE INTO SETTINGS (SETTING_KEY, SETTING_STRING, SETTING_INT) VALUES (?,?,?)");
        } catch (SQLException e) {
            Application.fatalError(e);
        }
    }

    static public int getSetting(String key, int deflt) {
        int setting = deflt;
        try {
            getSettingIntStmt.setString(1, key);
            ResultSet rs = getSettingIntStmt.executeQuery();
            if (rs.next())
                setting = rs.getInt("SETTING_INT");
        } catch (SQLException e) {
            Application.fatalError(e);
        }
        return setting;
    }

    static public String getSetting(String key, String deflt) {
        String setting = deflt;
        try {
            getSettingStrStmt.setString(1, key);
            ResultSet rs = getSettingStrStmt.executeQuery();
            if (rs.next())
                setting = rs.getString("SETTING_STRING");
        } catch (SQLException e) {
            Application.fatalError(e);
        }
        return setting;
    }

    static public void putSetting(String key, String svalue, int ivalue) {
        ResultSet rs;
        try {
            putSettingStmt.setString(1,key);
            putSettingStmt.setString(2,svalue);
            putSettingStmt.setInt(3, ivalue);
            putSettingStmt.execute();
        } catch (SQLException e) {
            Application.fatalError(e);
        }
    }

}
