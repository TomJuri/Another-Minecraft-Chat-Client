package net.defekt.mc.chatclient.ui.swing;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.defekt.mc.chatclient.protocol.data.ChatColor;
import net.defekt.mc.chatclient.protocol.data.Messages;
import net.defekt.mc.chatclient.protocol.data.UserPreferences;
import net.defekt.mc.chatclient.protocol.data.UserPreferences.Constants;
import net.defekt.mc.chatclient.protocol.io.IOUtils;
import net.defekt.mc.chatclient.ui.FontAwesome;
import net.defekt.mc.chatclient.ui.Main;

import javax.swing.*;
import javax.swing.SwingConstants;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.*;
import java.awt.*;
import java.awt.Desktop.Action;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Various UI utilities used internally
 *
 * @author Defective4
 * @see ChatColor
 */

public class SwingUtils {

    /**
     * User's screen size
     */
    public static final Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void playAsterisk() {
        final Object obj = Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.asterisk");
        if (obj instanceof Runnable) ((Runnable) obj).run();
    }

    public static void playExclamation() {
        final Object obj = Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
        if (obj instanceof Runnable) ((Runnable) obj).run();
    }

    public static void showAboutDialog(final Window parent) {
        final JDialog dialog = new JDialog(parent);
        dialog.setTitle("About AMCC");
        dialog.setModal(true);

        final JVBoxPanel panel = new JVBoxPanel();
        panel.add(new JLabel(new ImageIcon(IOUtils.resizeImageProp(Main.logoImage, 64))));
        panel.add(new JLabel("Another Minecraft Chat Client") {
            {
                setFont(getFont().deriveFont(Font.BOLD).deriveFont(15f));
            }
        });
        panel.add(new JLabel("Version " + Main.VERSION));
        panel.add(new JLabel(" "));
        panel.add(new JLabel("Author: Defective4"));
        panel.add(new JLinkLabel("https://github.com/Defective4/Another-Minecraft-Chat-Client") {
            {
                setFont(getFont().deriveFont(13f));
                setText("<html><a href=#>GitHub</a></html>");
                setIcon(FontAwesome.createIcon(FontAwesome.GITHUB,
                                               this,
                                               getFont().getSize2D(),
                                               FontAwesome.BRANDS_FONT));
            }
        });

        final Box discordBox = Box.createHorizontalBox();

        discordBox.add(new JLabel("Defective#3858") {
            {
                setFont(getFont().deriveFont(13f));
                setIcon(FontAwesome.createIcon(FontAwesome.DISCORD,
                                               this,
                                               getFont().getSize2D(),
                                               FontAwesome.BRANDS_FONT));
            }
        });

        final JButton copy = new JButton(FontAwesome.COPY);
        copy.setFont(FontAwesome.FONT.deriveFont(11f));
        copy.setMargin(new Insets(5, 5, 5, 5));

        copy.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("Defective#3858"), null);
            playAsterisk();
            JOptionPane.showOptionDialog(dialog,
                                         Messages.getString("Main.copied"),
                                         "...",
                                         JOptionPane.CANCEL_OPTION,
                                         JOptionPane.INFORMATION_MESSAGE,
                                         null,
                                         new String[]{Messages.getString("Main.ok")},
                                         0);
        });

        discordBox.add(copy);

        panel.add(discordBox);

        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.alignAll();

        dialog.setContentPane(panel);
        dialog.pack();
        centerWindow(dialog);
        dialog.setVisible(true);
    }

    /**
     * Get installed looks and feels
     *
     * @return list of installed LaFs
     */
    public static String[] getInstalledLookAndFeels() {
        final List<String> installed = new ArrayList<String>();
        installed.add("System");
        installed.add("Flat Light");
        installed.add("Flat Dark");
        for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            installed.add(info.getName());
        }
        return installed.toArray(new String[0]);
    }

    /**
     * Set system look and feel.<br>
     *
     * @param up User Preferences
     */
    public static void setNativeLook(final UserPreferences up) {
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
                    final String name = up.getUiTheme();
                    for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                        if (info.getName().equalsIgnoreCase(name)) {
                            UIManager.setLookAndFeel(info.getClassName());
                        }
                    break;
                }
            }
        } catch (final Exception e) {
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
    public static void appendColoredText(String text, final JTextPane pane) {
        text = ChatColor.legacyToHex("\u00a7#ffffff" + text);
        final StyledDocument doc = pane.getStyledDocument();
        final StyleContext ctx = new StyleContext();
        final Style style = ctx.addStyle("style", null);

        boolean lineSupported = true;
        final int ctxIndex = doc.getLength();

        for (JsonElement el : ChatColor.parseColors(text)) {
            try {
                JsonObject obj = el.getAsJsonObject();
                String tx = obj.get("text").getAsString();
                Color c = Color.decode("#" + obj.get("color").getAsString());
                StyleConstants.setForeground(style, c);

                if (!net.defekt.mc.chatclient.ui.swing.SwingConstants.checkMCSupported(tx)) {
                    lineSupported = false;
                }

                doc.insertString(doc.getLength(), tx, style);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        if ((!lineSupported && !Main.up.getUnicodeCharactersMode()
                                       .equals(UserPreferences.Constants.UNICODECHARS_KEY_FORCE_CUSTOM)) || Main.up.getUnicodeCharactersMode()
                                                                                                                   .equals(Constants.UNICODECHARS_KEY_FORCE_UNICODE)) {
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
    public static void showErrorDialog(final Window parent, final String title, final Exception ex, final String message) {
        final JDialog errDial = new JDialog(parent);
        errDial.setModal(true);
        errDial.setTitle(title);

        final List<Component> btns = new ArrayList<>();
        btns.add(new JButton(Messages.getString("Main.ok")) {
            {
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent ev) {
                        errDial.dispose();
                    }
                });
            }
        });

        if (ex != null) {
            btns.add(new JButton(Messages.getString("SwingUtils.errorDialogOptionDetails")) {
                {
                    addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent ev) {
                            showExceptionDetails(parent, ex);
                        }
                    });
                }
            });

        }

        final JOptionPane jop = new JOptionPane(message,
                                                JOptionPane.ERROR_MESSAGE,
                                                JOptionPane.DEFAULT_OPTION,
                                                null,
                                                btns.toArray(new Component[0]));

        errDial.setContentPane(jop);
        errDial.pack();
        SwingUtils.centerWindow(errDial);

        playExclamation();
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
    public static void showVersionDialog(final String oldVersion, final String newVersion, final int difference, final String versionType, final List<String> changesList) {
        final JFrame win = new JFrame();
        win.setTitle("New version available!");
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Box message = Box.createVerticalBox();
        message.add(new JLabel("<html><font style=\"font-weight:bold;\">A new update is available!</font><br><br>" + "An update is available to download!<br><br>" + "Current version: <font style=\"font-weight:bold;\">" + oldVersion + "</font><br>" + "New version: <font style=\"font-weight:bold;\">" + newVersion + "</font><br><br>" + "You are <font style=\"font-weight:bold;\">" + Integer.toString(
                difference) + "</font> " + versionType + " versions behind!</html>"));

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
                SwingUtils.playAsterisk();
                JOptionPane.showOptionDialog(win,
                                             jsp,
                                             Messages.getString("SwingUtils.updateDialogChangesTitle") + newVersion,
                                             JOptionPane.DEFAULT_OPTION,
                                             JOptionPane.PLAIN_MESSAGE,
                                             null,
                                             new String[]{Messages.getString("Main.ok")},
                                             0);
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

        final JOptionPane pane = new JOptionPane(message,
                                                 JOptionPane.INFORMATION_MESSAGE,
                                                 JOptionPane.DEFAULT_OPTION,
                                                 null,
                                                 new Object[]{ok, changes, update, exit});

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

        JOptionPane.showOptionDialog(parent,
                                     box,
                                     Messages.getString("SwingUtils.exceptionDetailsDialogTitle"),
                                     JOptionPane.DEFAULT_OPTION,
                                     JOptionPane.PLAIN_MESSAGE,
                                     null,
                                     new Object[]{Messages.getString("Main.ok")},
                                     0);
    }
}
