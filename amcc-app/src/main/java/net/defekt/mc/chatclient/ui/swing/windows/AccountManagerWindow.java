package net.defekt.mc.chatclient.ui.swing.windows;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class AccountManagerWindow extends JDialog {

    private JPanel contentPane;

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

        JList<String> list = new JList<>(); // TOOD
        list.setBounds(5, 25, 424, 189);
        contentPane.add(list);

        JButton btnNewButton = new JButton("Close");
        btnNewButton.setBounds(335, 225, 89, 23);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Add account");
        btnNewButton_1.setBounds(195, 225, 130, 23);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Remove");
        btnNewButton_2.setBounds(96, 225, 89, 23);
        contentPane.add(btnNewButton_2);

        btnNewButton.addActionListener(e -> {
            dispose();
        });
    }
}
