package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import pages.home.HomeApplication;
import components.AppPanelManager;

/**
 * @author Victor Puska
 */

class MainFrame extends JFrame {

    private Rectangle winrect = new Rectangle();

    MainFrame() {
        System.out.println("Create main.MainFrame!");
        initComponents();
        getSizeSettings();
    }

    private void getSizeSettings() {
        winrect.x = Settings.getSetting("WINDOW.X", 20);
        winrect.y = Settings.getSetting("WINDOW.Y", 20);
        winrect.width = Settings.getSetting("WINDOW.WIDTH", 500);
        winrect.height = Settings.getSetting("WINDOW.HEIGHT", 300);
        this.setBounds(winrect);
        this.setExtendedState(Settings.getSetting("WINDOW.STATE", NORMAL));
    }

    private void saveSizeSettings() {
        Settings.putSetting("WINDOW.X", "", winrect.x);
        Settings.putSetting("WINDOW.Y", "", winrect.y);
        Settings.putSetting("WINDOW.WIDTH", "", winrect.width);
        Settings.putSetting("WINDOW.HEIGHT", "", winrect.height);
        Settings.putSetting("WINDOW.STATE", "", this.getExtendedState());
    }

    private void mouseHover(MouseEvent e) {
        JLabel label = (JLabel) e.getSource();
        label.setOpaque(true);
        label.repaint();
    }

    private void mouseLeave(MouseEvent e) {
        JLabel label = (JLabel) e.getSource();
        label.setOpaque(false);
        label.repaint();
    }

    private void thisWindowClosing(WindowEvent e) {
        saveSizeSettings();
        AppPanelManager.getManager().closePanels();
        Database.close();
        System.exit(0);
    }

    private void thisComponentMoved(ComponentEvent e) {
        if ( (this.getExtendedState() & MAXIMIZED_VERT) == 0)
            winrect.y = this.getY();
        if ( (this.getExtendedState() & MAXIMIZED_HORIZ) == 0)
            winrect.x = this.getX();
    }

    private void thisComponentResized(ComponentEvent e) {
        if ( (this.getExtendedState() & MAXIMIZED_VERT) == 0)
            winrect.height = this.getHeight();
        if ( (this.getExtendedState() & MAXIMIZED_HORIZ) == 0)
            winrect.width = this.getWidth();
        mainPanel.revalidate();
    }

    private void homeLabelMouseClicked(MouseEvent e) {
        AppPanelManager.getManager().gotoApp(new HomeApplication());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        mainPanel = new JPanel();
        headingPanel = new JPanel();
        headingLabel = new JLabel();
        hSpacer1 = new JPanel(null);
        homeLabel = new JLabel();
        settingsLabel = new JLabel();
        hSpacer2 = new JPanel(null);
        appManager = new AppPanelManager();

        //======== this ========
        setTitle("money.manager");
        setBackground(new Color(255, 204, 204));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                thisComponentMoved(e);
            }
            @Override
            public void componentResized(ComponentEvent e) {
                thisComponentResized(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== mainPanel ========
        {
            mainPanel.setBackground(Color.white);
            mainPanel.setBorder(null);
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

            //======== headingPanel ========
            {
                headingPanel.setBackground(Color.white);
                headingPanel.setMaximumSize(new Dimension(32767, 48));
                headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.X_AXIS));

                //---- headingLabel ----
                headingLabel.setIcon(new ImageIcon(getClass().getResource("/moneymanger.png")));
                headingPanel.add(headingLabel);

                //---- hSpacer1 ----
                hSpacer1.setOpaque(false);
                headingPanel.add(hSpacer1);

                //---- homeLabel ----
                homeLabel.setIcon(new ImageIcon(getClass().getResource("/Home 24x24.png")));
                homeLabel.setPreferredSize(new Dimension(32, 32));
                homeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                homeLabel.setMinimumSize(new Dimension(28, 28));
                homeLabel.setMaximumSize(new Dimension(28, 28));
                homeLabel.setBackground(new Color(234, 234, 247));
                homeLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        homeLabelMouseClicked(e);
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        mouseHover(e);
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        mouseLeave(e);
                    }
                });
                headingPanel.add(homeLabel);

                //---- settingsLabel ----
                settingsLabel.setIcon(new ImageIcon(getClass().getResource("/Gears-icon.png")));
                settingsLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                settingsLabel.setBackground(new Color(234, 234, 247));
                settingsLabel.setToolTipText("Settings");
                settingsLabel.setBorder(new EmptyBorder(2, 2, 2, 2));
                settingsLabel.setPreferredSize(new Dimension(32, 32));
                settingsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                settingsLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        mouseHover(e);
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        mouseLeave(e);
                    }
                });
                headingPanel.add(settingsLabel);

                //---- hSpacer2 ----
                hSpacer2.setMaximumSize(new Dimension(20, 12));
                hSpacer2.setMinimumSize(new Dimension(20, 12));
                hSpacer2.setPreferredSize(new Dimension(20, 12));
                hSpacer2.setOpaque(false);
                headingPanel.add(hSpacer2);
            }
            mainPanel.add(headingPanel);
            mainPanel.add(appManager);
        }
        contentPane.add(mainPanel, BorderLayout.CENTER);
        setSize(775, 600);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel mainPanel;
    private JPanel headingPanel;
    private JLabel headingLabel;
    private JPanel hSpacer1;
    private JLabel homeLabel;
    private JLabel settingsLabel;
    private JPanel hSpacer2;
    private AppPanelManager appManager;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
