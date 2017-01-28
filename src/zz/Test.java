package zz;

import com.sun.rowset.CachedRowSetImpl;
import application.Database;

import javax.sql.rowset.CachedRowSet;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Created by VJP on 23/11/2016.
 */
public class Test {

    final static String dashes = "------------------------------------------------------------------------";

    public static void main(String[] args) throws Exception {
        System.out.println("Test");
        Database.connect();
        DbTable accounts = new DbTable(Database.conn, "ACCOUNTS");
        accounts.addColumn("_BALANCE", Types.DECIMAL);

        // List out the metadata
        System.out.format("%-20s %-10s %-10s %5s\n", "NAME", "PKEY", "BOUND", "TYPE");
        System.out.format("%-20s %-10s %-10s %5s\n",
                dashes.substring(0,20),
                dashes.substring(0,10),
                dashes.substring(0,10),
                dashes.substring(0,5));

        for(int j = 0; j < accounts.columnCount(); j++) {
            DbColumnInfo info = accounts.getColumnInfo(j);
            System.out.format("%-20s %-10s %-10s %5d\n",
                    accounts.getColumnName(j),
                    accounts.isPrimaryKeyColumn(accounts.getColumnName(j)),
                    info.isBoundColumn(),
                    info.getSqlType()
                    );
        }

        // List out the table
        accounts.orderBy("ACCOUNT_UID");
        accounts.deleteRow(55);
        ArrayList<DbTableRow> rows = accounts.selectAll();
        System.out.format("\n\n%5s %-40s %-10s %5s %10s\n", "ID", "NAME", "ACTIVE", "TYPE", "BALANCE");
        System.out.format("%5s %-40s %-10s %5s %10s\n",
                dashes.substring(0, 5),
                dashes.substring(0,40),
                dashes.substring(0,10),
                dashes.substring(0, 5),
                dashes.substring(0,10) );

        for(DbTableRow r: rows) {
            r.setColumn(4, new BigDecimal("123.45"));
            System.out.format("%5d %-40s %-10s %5d %10s\n",
                    r.getColumn(0),
                    r.getColumn(1),
                    r.getColumn(2),
                    r.getColumn(3),
                    ((BigDecimal) r.getColumn(4)).toString());
        }

//
//        accounts.orderBy("ACCOUNT_UID");
//        accounts.deleteRow(52);
//        ArrayList<DbTableRow> rows = accounts.selectAll();
//        for (DbTableRow r : rows) {
//            System.out.println(r);
//            if ((int)r.getColumn("ACCOUNT_UID") == 33)
//                accounts.deleteRow(r);
//        }
//        System.out.println("select & update test");
//        DbTableRow r = accounts.selectSingle(37);
//        System.out.println(r);
//        r.setColumn("ACCOUNT_NAME", "*** The Victor Account ***");
//        accounts.updateRow(r);
    }

    public static void dotest() throws Exception {
        CachedRowSet crs = new CachedRowSetImpl();
        int [] keys = { 1 };
        crs.setCommand("SELECT * FROM ACCOUNTS");
        crs.setTableName("ACCOUNTS");
        crs.setKeyColumns(keys);
        crs.execute(Database.conn);
        while(crs.next()) {
            System.out.println(crs.getInt(1));
            int uid = crs.getInt("ACCOUNT_UID");
            if(uid==11) {
                crs.updateString("ACCOUNT_NAME", "Test CachedRowSet  xxx yyy");
                crs.updateRow();
            }
            System.out.println(crs.getString("ACCOUNT_NAME"));
        }
        crs.acceptChanges();
    }

    public static void dotest2() throws Exception {
        CachedRowSet crs = new CachedRowSetImpl();
        int [] keys = { 1 };
        crs.setCommand("SELECT * FROM ACCOUNTS");
        crs.setTableName("ACCOUNTS");
        crs.setKeyColumns(keys);
        crs.execute(Database.conn);
        crs.moveToInsertRow();
        crs.updateString("ACCOUNT_NAME", "Test insert !!!");
        //crs.updateBoolean("ACCOUNT_ACTIVE", false);
        //crs.updateInt("ACCOUNT_TYPE", 0);
        crs.insertRow();
        crs.acceptChanges(Database.conn);
    }
}
