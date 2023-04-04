package net.defekt.mc.chatclient.ui.swing.windows;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.WeakHashMap;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.defekt.mc.chatclient.protocol.auth.UserInfo;
import net.defekt.mc.chatclient.protocol.io.IOUtils;
import net.defekt.mc.chatclient.ui.Main;
import net.defekt.mc.chatclient.ui.swing.JLinkLabel;
import net.defekt.mc.chatclient.ui.swing.SwingUtils;
import net.defekt.minecraft.auth.microsoft.CodeResponse;
import net.defekt.minecraft.auth.microsoft.MicrosoftAuth;
import net.defekt.minecraft.auth.microsoft.MinecraftAuthResponse;
import net.defekt.minecraft.auth.microsoft.OnlineProfile;
import net.defekt.minecraft.auth.microsoft.TokenCallback;
import net.defekt.minecraft.auth.microsoft.TokenErrorResponse;
import net.defekt.minecraft.auth.microsoft.TokenResponse;
import net.defekt.minecraft.auth.microsoft.XSTSResponse;

public class AccountManagerWindow extends JDialog {

    private JPanel contentPane;

    private void update(JList<UserInfo> list) {
        list.setListData(Main.up.getMsUsers().toArray(new UserInfo[0]));
    }

    /**
     * Create the frame.
     */
    public AccountManagerWindow(Window parent) {
        super(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setModal(true);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblMicrosoftAccounts = new JLabel("Microsoft accounts");
        lblMicrosoftAccounts.setHorizontalAlignment(SwingConstants.CENTER);
        lblMicrosoftAccounts.setBounds(5, 0, 424, 22);
        lblMicrosoftAccounts.setMaximumSize(new Dimension(0, 20));
        contentPane.add(lblMicrosoftAccounts);

        JList<UserInfo> list = new JList<>(); // TOOD
        list.setBounds(5, 25, 424, 189);
        update(list);
        contentPane.add(list);

        list.setCellRenderer(new DefaultListCellRenderer() {

            WeakHashMap<String, BufferedImage> skins = new WeakHashMap<>();

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                Component cpt = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (!(cpt instanceof JLabel)) return cpt;
                JLabel label = (JLabel) cpt;
                UserInfo val = (UserInfo) value;

                if (!skins.containsKey(val.getUuid())) {
                    try {
                        BufferedImage img = ImageIO.read(new URL(val.getSkin()));
                        skins.put(val.getUuid(), IOUtils.trimSkinHead(img, true));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (skins.containsKey(val.getUuid()))
                    label.setIcon(new ImageIcon(IOUtils.resizeImageProp(skins.get(val.getUuid()), 32)));

                return label;
            }
        });

        JButton btnNewButton = new JButton("Close");
        btnNewButton.setBounds(335, 225, 89, 23);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Add account");
        btnNewButton_1.setBounds(195, 225, 130, 23);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Remove");
        btnNewButton_2.setBounds(96, 225, 89, 23);
        btnNewButton_2.setEnabled(false);
        contentPane.add(btnNewButton_2);

        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                btnNewButton_2.setEnabled(list.getSelectedValue() != null);
            }
        });

        btnNewButton_2.addActionListener(e -> {
            if (list.getSelectedValue() != null) {
                Main.up.getMsUsers().remove(list.getSelectedValue());
                update(list);
            }
        });

        btnNewButton_1.addActionListener(e -> {
            CodeResponse code;
            try {
                code = MicrosoftAuth.retrieveCode();
            } catch (Exception e2) {
                SwingUtils.showErrorDialog(AccountManagerWindow.this, "Error", e2, "An error occured...");
                return;
            }

            JDialog dialog = new JDialog(AccountManagerWindow.this);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setModal(true);

            JButton copyCode = new JButton("Copy Code");
            JButton close = new JButton("Cancel");

            copyCode.addActionListener(e2 -> {
                StringSelection sel = new StringSelection(code.getUser_code());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, sel);
            });

            close.addActionListener(e2 -> {
                dialog.dispose();
            });

            Box box = Box.createVerticalBox();

            JProgressBar prog = new JProgressBar();
            prog.setMaximum(4);
            prog.setIndeterminate(true);

            Box labels = Box.createHorizontalBox();
            labels.add(new JLabel("Open "));
            labels.add(new JLinkLabel(code.getVerification_uri()));
            labels.add(new JLabel(" in your browser and enter the code "));
            labels.add(new JTextField(code.getUser_code()) {
                {
                    setFont(getFont().deriveFont(Font.BOLD));
                    setEditable(false);
                }
            });
            labels.add(new JLabel(" to add a new account."));

            box.add(labels);
            box.add(prog);

            dialog.setContentPane(new JOptionPane(box, JOptionPane.INFORMATION_MESSAGE, JOptionPane.CANCEL_OPTION, null,
                    new Object[] { copyCode, close }, null));

            MicrosoftAuth.authenticateCode(code, new TokenCallback() {

                @Override
                public void exception(Exception ex) {
                    dialog.dispose();
                    SwingUtils.showErrorDialog(AccountManagerWindow.this, "Error", ex,
                            "An error occured while adding the account.");
                }

                @Override
                public void errored(TokenErrorResponse resp) {
                    JOptionPane.showOptionDialog(dialog, "Server responded with an error: " + resp.getResponse(),
                            "Error", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null,
                            new String[] { "Ok" }, 0);
                    dialog.dispose();
                }

                @Override
                public void authed(TokenResponse resp) {
                    try {
                        labels.removeAll();
                        JLabel lb = new JLabel("Authenticating with Xbox Live...");
                        labels.add(lb);
                        labels.revalidate();
                        prog.setIndeterminate(false);
                        prog.setValue(1);
                        String xblToken = MicrosoftAuth.authXBL(resp);
                        prog.setValue(2);
                        lb.setText("Retrieving XSTS token...");
                        XSTSResponse xstsToken = MicrosoftAuth.authXSTS(xblToken);
                        prog.setValue(3);
                        lb.setText("Authenticating with Minecraft servers...");
                        MinecraftAuthResponse mar = MicrosoftAuth.authMinecraft(xstsToken);
                        prog.setValue(4);
                        lb.setText("Checking game ownership...");
                        OnlineProfile prof = MicrosoftAuth.getProfile(mar.getAccess_token());
                        dialog.dispose();

                        UserInfo user = new UserInfo(prof.getName(), mar.getAccess_token(), prof.getId(),
                                resp.getRefresh_token(), prof.getSkinUrl());

                        Main.up.getMsUsers().add(user);
                        update(list);
                    } catch (Exception e2) {
                        dialog.dispose();
                        SwingUtils.showErrorDialog(parent, "Error", e2, "An error occured while adding an account");
                    }
                }
            });

            dialog.pack();
            SwingUtils.centerWindow(dialog);
            dialog.setVisible(true);
        });

        btnNewButton.addActionListener(e -> {
            dispose();
        });
    }
}
