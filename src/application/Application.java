package application;

import components.AppPanelManager;
import pages.home.HomeApplication;
import javax.swing.*;

/**
 * Created by VJP on 7/01/2017.
 */
public class Application {

    private static void setLookAndFeel(String lafName) {
        try {
            for (UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
                if (lafName.equals(info.getName()))
                    UIManager.setLookAndFeel(info.getClassName());

            }
        } catch (Exception e) {
            // if Nimbus is not available, you can set the GUI to another L&F
        }
    }

    public static void main(String[] args) {
        System.out.print("Connecting to database...");
        Database.connect();
        System.out.println("done!");

        setLookAndFeel("Nimbus");

        //InitialFileDialog ifd = new InitialFileDialog();
        //ifd.setVisible(true);

        MainFrame frame = new MainFrame();
        AppPanelManager.getManager().gotoApp(new HomeApplication());
        frame.setVisible(true);
    }

    public static void fatalError(Exception e) {
        JOptionPane.showMessageDialog(
                new JFrame(),
                e,
                "Money.Manger Application Error",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        System.exit(-1);
    }

}
