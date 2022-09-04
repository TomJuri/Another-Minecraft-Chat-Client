package net.defekt.mc.chatclient.ui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import net.defekt.mc.chatclient.protocol.data.ChatColor;
import net.defekt.mc.chatclient.ui.Main;
import net.defekt.mc.chatclient.ui.Messages;
import net.defekt.mc.chatclient.ui.UserPreferences;
import net.defekt.mc.chatclient.ui.UserPreferences.Constants;

/**
 * Various UI utilities used internally
 * 
 * @see ChatColor
 * @author Defective4
 *
 */

public class SwingUtils {

    public static String[] getInstalledLookAndFeels() {
        List<String> installed = new ArrayList<String>();
        installed.add("System");
        installed.add("Flat Light");
        installed.add("Flat Dark");
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            installed.add(info.getName());
        return installed.toArray(new String[0]);
    }

    /**
     * Set system look and feel.<br>
     */
    public static void setNativeLook(UserPreferences up) {
        try {
            switch (up.getUiTheme()) {
                case "System": {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                }
                case "Flat Light": {
                    FlatLightLaf.setup();
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
                }
                case "Flat Dark": {
                    FlatDarkLaf.setup();
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    break;
                }
                default: {
                    String name = up.getUiTheme();
                    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                        if (info.getName().equalsIgnoreCase(name)) UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Aligns spinner's text field to the left side
     * 
     * @param spinner spinner to align
     */
    public static void alignSpinner(final JSpinner spinner) {
        final DefaultEditor editor = (DefaultEditor) spinner.getEditor();
        editor.getTextField().setHorizontalAlignment(SwingConstants.LEFT);
    }

    /**
     * User's screen size
     */
    public static final Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * Sets window's position to center
     * 
     * @param win window to center
     */
    public static void centerWindow(final Window win) {
        final int x = (sSize.width - win.getWidth()) / 2;
        final int y = (sSize.height - win.getHeight()) / 2;

        win.setLocation(x, y);
    }

    /**
     * Appends colored text to text pane.<br>
     * It follows Minecraft colors defined in {@link ChatColor}
     * 
     * @param text text to append.<br>
     *             It follows the same rules defined in Minecraft, for example,
     *             \u00a74Hello \u00a79World would be
     *             "<font style="color: aa0000;">Hello</font>
     *             <font style="color: 5555ff;">World</font>""
     * @param pane pane to append text to
     */
    public static void appendColoredText(final String text, final JTextPane pane) {
        final StyledDocument doc = pane.getStyledDocument();
        final StyleContext ctx = new StyleContext();
        final Style style = ctx.addStyle("style", null);

        final String[] split = text.split("\u00a7");
        boolean lineSupported = true;
        final int ctxIndex = doc.getLength();
        for (final String part : split) {
            try {
                if (text.startsWith(part)) {
                    doc.insertString(doc.getLength(), part, null);
                } else {
                    final String code = part.substring(0, 1);
                    Color c;
                    boolean isHex = false;
                    if (code.equals("#") && part.length() > 7) {
                        try {
                            final String hex = part.substring(1, 7);
                            final int rgb = Integer.parseInt(hex, 16);
                            c = new Color(rgb);
                            isHex = true;
                        } catch (final Exception e) {
                            c = ChatColor.translateColorCode(code);
                        }
                    } else {
                        c = ChatColor.translateColorCode(code);
                    }
                    StyleConstants.setForeground(style, c);

                    final String rest = part.substring(isHex ? 7 : 1);

                    if (!net.defekt.mc.chatclient.ui.swing.SwingConstants.checkMCSupported(rest)) {
                        lineSupported = false;
                    }

                    doc.insertString(doc.getLength(), rest, style);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        if ((!lineSupported
                && !Main.up.getUnicodeCharactersMode().equals(UserPreferences.Constants.UNICODECHARS_KEY_FORCE_CUSTOM))
                || Main.up.getUnicodeCharactersMode().equals(Constants.UNICODECHARS_KEY_FORCE_UNICODE)) {
            final SimpleAttributeSet set = new SimpleAttributeSet();
            StyleConstants.setFontFamily(set, "arial");
            StyleConstants.setFontSize(set, 16);
            StyleConstants.setBold(set, true);
            doc.setCharacterAttributes(ctxIndex, doc.getLength() - ctxIndex, set, false);
        }
    }

    /**
     * Shows error dialog, allowing to display exception details
     * 
     * @param parent  parent of this dialog
     * @param title   dialog title
     * @param ex      exception to display details of
     * @param message custom dialog message
     */
    public static void showErrorDialog(final Window parent, final String title, final Exception ex,
            final String message) {
        final JDialog errDial = new JDialog(parent);
        errDial.setModal(true);
        errDial.setTitle(title);

        final JOptionPane jop = new JOptionPane(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
                new Object[] { new JButton(Messages.getString("Main.ok")) {
                    {
                        addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(final ActionEvent ev) {
                                errDial.dispose();
                            }
                        });
                    }
                }, new JButton(Messages.getString("SwingUtils.errorDialogOptionDetails")) {
                    {
                        addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(final ActionEvent ev) {
                                showExceptionDetails(parent, ex);
                            }
                        });
                    }
                } });

        errDial.setContentPane(jop);
        errDial.pack();
        SwingUtils.centerWindow(errDial);
        errDial.setVisible(true);
    }

    /**
     * Get color as RGB in HEX
     * 
     * @param c color to convert
     * @return HEX string
     */
    public static String getHexRGB(final Color c) {
        return Integer.toHexString(c.getRGB()).substring(2);
    }

    /**
     * Change each of RGB values by index
     * 
     * @param c     color to modify
     * @param index color modifier
     * @return modified color
     */
    public static Color brighten(final Color c, final int index) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        for (int x = 0; x < Math.abs(index); x++)
            if (index < 0) {
                if (r > 1) {
                    r--;
                }
                if (g > 1) {
                    g--;
                }
                if (b > 1) {
                    b--;
                }
            } else {
                if (r < 255) {
                    r++;
                }
                if (g < 255) {
                    g++;
                }
                if (b < 255) {
                    b++;
                }
            }
        return new Color(r, g, b);
    }

    /**
     * Creates an version information dialog.<br>
     * Used when there is an update available
     * 
     * @param oldVersion  current version of application
     * @param newVersion  new version
     * @param difference  version difference
     * @param versionType version difference type.<br>
     *                    Options used:<br>
     *                    "major"<br>
     *                    "minor"<br>
     *                    "fix"
     * @param changesList list of changes
     */
    public static void showVersionDialog(final String oldVersion, final String newVersion, final int difference,
            final String versionType, final List<String> changesList) {
        final JFrame win = new JFrame();
        win.setTitle("New version available!");
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Box message = Box.createVerticalBox();
        message.add(new JLabel("<html><font style=\"font-weight:bold;\">A new update is available!</font><br><br>"
                + "An update is available to download!<br><br>" + "Current version: <font style=\"font-weight:bold;\">"
                + oldVersion + "</font><br>" + "New version: <font style=\"font-weight:bold;\">" + newVersion
                + "</font><br><br>" + "You are <font style=\"font-weight:bold;\">" + Integer.toString(difference)
                + "</font> " + versionType + " versions behind!</html>"));

        for (final Component ct : message.getComponents())
            if (ct instanceof JComponent) {
                ((JComponent) ct).setAlignmentX(Component.LEFT_ALIGNMENT);
            }

        final JButton ok = new JButton(Messages.getString("Main.ok"));
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                synchronized (win) {
                    win.notify();
                }
            }
        });

        final JButton changes = new JButton(Messages.getString("SwingUtils.updateDialogOptionShowChanges"));
        changes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                final StringBuilder msg = new StringBuilder();
                for (final String line : changesList) {
                    if (line.startsWith("[")) {
                        msg.append("<font style=\"font-weight: bold;\">");
                    }
                    msg.append(line).append("<br>");
                    if (line.startsWith("[")) {
                        msg.append("</font>");
                    }
                }

                final JLabel message = new JLabel("<html>" + msg.append("</html>").toString());
                final JScrollPane jsp = new JScrollPane(message);
                jsp.setPreferredSize(win.getSize());
                JOptionPane.showOptionDialog(win, jsp,
                        Messages.getString("SwingUtils.updateDialogChangesTitle") + newVersion,
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                        new String[] { Messages.getString("Main.ok") }, 0);
            }
        });
        final JButton update = new JButton(Messages.getString("SwingUtils.updateDialogOptionUpdate"));
        update.setEnabled(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE));
        update.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Desktop.getDesktop()
                            .browse(new URI("https://github.com/Defective4/Another-Minecraft-Chat-Client/releases"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        final JButton exit = new JButton(Messages.getString("SwingUtils.updateDialogOptionExit"));
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                System.exit(0);
            }
        });

        final JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION,
                null, new Object[] { ok, changes, update, exit });

        win.setContentPane(pane);
        win.pack();
        win.setResizable(false);
        centerWindow(win);
        win.setVisible(true);
        win.toFront();

        synchronized (win) {
            try {
                win.wait();
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }

        }

        win.dispose();
    }

    private static void showExceptionDetails(final Window parent, final Exception ex) {

        final Box box = Box.createVerticalBox();
        box.add(new JLabel(Messages.getString("SwingUtils.exceptionDetailsDialogLabel")));
        box.add(new JScrollPane(new JTextArea() {
            {
                append(ex.toString() + "\r\n");
                for (final StackTraceElement ste : ex.getStackTrace()) {
                    append(ste.toString() + "\r\n");
                }
                setForeground(new Color(200, 0, 0));
                setFont(getFont().deriveFont(11f));
            }
        }) {
            {
                setPreferredSize(new Dimension((int) (sSize.getWidth() / 3), (int) (sSize.getHeight() / 2)));
            }
        });

        for (final Component ct : box.getComponents()) {
            if (ct instanceof JComponent) {
                ((JComponent) ct).setAlignmentX(Component.LEFT_ALIGNMENT);
            }
            if (ct instanceof JScrollPane) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        ((JScrollPane) ct).getVerticalScrollBar().setValue(0);
                    }
                });
            }
        }

        JOptionPane.showOptionDialog(parent, box, Messages.getString("SwingUtils.exceptionDetailsDialogTitle"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                new Object[] { Messages.getString("Main.ok") }, 0);
    }
}
