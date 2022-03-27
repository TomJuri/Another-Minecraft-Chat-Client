package net.defekt.mc.chatclient.ui.swing;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import net.defekt.mc.chatclient.protocol.MinecraftStat;
import net.defekt.mc.chatclient.protocol.data.ModInfo;
import net.defekt.mc.chatclient.protocol.data.QueryInfo;
import net.defekt.mc.chatclient.protocol.data.StatusInfo;
import net.defekt.mc.chatclient.ui.Main;
import net.defekt.mc.chatclient.ui.Messages;
import net.defekt.mc.chatclient.ui.ServerEntry;

/**
 * A new dialog displaying details about selected server
 * 
 * @author Defective4
 *
 */
public class ServerDetailsDialog extends JDialog {

	/**
	 * Create new details dialog
	 * 
	 * @param parent parent window
	 * @param server server
	 */
	public ServerDetailsDialog(Window parent, ServerEntry server) {
		super(parent);
		setModal(true);
		setPreferredSize(new Dimension((int) (SwingUtils.sSize.width / 3), (int) (SwingUtils.sSize.height / 2.5)));
		setTitle(Messages.getString("ServerDetailsDialog.detailsDialogTitle") + ": " + server.getName());
		StatusInfo info = server.getInfo();

		JTabbedPane tabsPane = new JTabbedPane();
		tabsPane.setBackground(new Color(35, 35, 35));

		JVBoxPanel modsPane = new JVBoxPanel();
		modsPane.setBackground(Color.white);
		if (info == null || info.getModType() == null) {
			modsPane.add(new JLabel(Messages.getString("ServerDetailsDialog.vanilla")));
		} else {
			for (ModInfo mod : info.getModList()) {
				Box modBox = Box.createHorizontalBox();
				JTextPane modLabel = new JTextPane();
				modLabel.setEditable(false);
				modLabel.setOpaque(false);
				modLabel.setText((mod.getModID() + " (" + mod.getVersion() + ")"));

				JButton cfLink = new JButton("CurseForge");
				cfLink.addMouseListener(new MouseAdapter() {

					private String oldString;
					private String newString;
					private JTextPane label = modLabel;

					@Override
					public void mouseExited(MouseEvent e) {
						JButton link = (JButton) e.getSource();
						link.setText(oldString);
						modLabel.setText(newString);
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						JButton link = (JButton) e.getSource();
						oldString = link.getText();
						newString = label.getText();
						link.setText(newString);
						modLabel.setText("");
					}
				});

				cfLink.addActionListener(new ActionListener() {

					private String modID = mod.getModID();

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							URL url = new URL("https://www.curseforge.com/minecraft/mc-mods/" + modID);
							Desktop.getDesktop().browse(url.toURI());

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});

				modBox.add(modLabel);
				modBox.add(cfLink);

				modsPane.add(modBox);
			}
		}

		modsPane.alignAll();

		JTextPane infoPane = initTextPane();
		SwingUtils.appendColoredText(Messages.getString("ServerDetailsDialog.address") + ":\u00a77 " + server.getHost(),
				infoPane);
		SwingUtils.appendColoredText(
				"\n" + Messages.getString("ServerDetailsDialog.port") + ":\u00a77 " + server.getPort(), infoPane);
		SwingUtils.appendColoredText("\nForge: \u00a77" + server.getForgeMode(), infoPane);

		JTextPane playersPane = initTextPane();
		if (info != null && info.getPlayersList().length > 0)
			for (String player : info.getPlayersList())
				SwingUtils.appendColoredText(player + "\n", playersPane);
		else {
			SwingUtils.appendColoredText("\u00a77" + Messages.getString("ServerDetailsDialog.none"), playersPane);
		}

		JTextPane queryPane = initTextPane();
		populateQueryPane(null, queryPane);
		JMinecraftButton refreshBtn = new JMinecraftButton(Messages.getString("Main.refreshOption"));
		refreshBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						refreshBtn.setEnabled(false);
						refreshBtn.setText(Messages.getString("ServerDetailsDialog.querying"));
						try {
							QueryInfo info = MinecraftStat.query(server.getHost(), server.getPort());
							populateQueryPane(info, queryPane);
							refreshBtn.setText(Messages.getString("Main.refreshOption"));
						} catch (Exception ex) {
							ex.printStackTrace();
							refreshBtn.setText("(" + Messages.getString("ServerDetailsDialog.error") + ")");
						}
						refreshBtn.setEnabled(true);
					}
				}).start();
			}
		});

		JVBoxPanel queryBox = new JVBoxPanel();
		queryBox.add(queryPane);
		queryBox.add(refreshBtn);
		queryBox.setBackground(new Color(35, 35, 35));
		queryBox.alignAll();

		tabsPane.addTab(Messages.getString("ServerDetailsDialog.infoTab"), infoPane);
		tabsPane.addTab(Messages.getString("ServerDetailsDialog.queryTab"), queryBox);
		tabsPane.addTab(Messages.getString("ServerDetailsDialog.modsTab"), new JScrollPane(modsPane));
		tabsPane.addTab(Messages.getString("ServerDetailsDialog.playersTab"), new JScrollPane(playersPane));
		if (info == null) {
			tabsPane.setEnabledAt(2, false);
			tabsPane.setEnabledAt(3, false);
		}
		setContentPane(tabsPane);
		pack();
		SwingUtils.centerWindow(this);
	}

	private void populateQueryPane(QueryInfo info, JTextPane pane) {
		String motd = "-";
		String gamemode = "-";
		String map = "-";
		String online = "-";
		String max = "-";
		if (info != null) {
			motd = info.getMotd();
			gamemode = info.getGamemode();
			map = info.getMap();
			online = info.getOnlinePlayer();
			max = info.getMaxPlayers();
		}

		pane.setText("");
		SwingUtils.appendColoredText(Messages.getString("ServerDetailsDialog.motd") + ":\u00a77 " + motd, pane);
		SwingUtils.appendColoredText("\n" + Messages.getString("ServerDetailsDialog.gamemode") + ":\u00a77 " + gamemode,
				pane);
		SwingUtils.appendColoredText("\n" + Messages.getString("ServerDetailsDialog.map") + ":\u00a77 " + map, pane);
		SwingUtils.appendColoredText("\n" + Messages.getString("ServerDetailsDialog.online") + ":\u00a77 " + online,
				pane);
		SwingUtils.appendColoredText("\n" + Messages.getString("ServerDetailsDialog.maxPlayers") + ":\u00a77 " + max,
				pane);
	}

	private static JTextPane initTextPane() {
		JTextPane pane = new JTextPane();
		pane.setBackground(new Color(35, 35, 35));
		pane.setForeground(Color.white);
		pane.setEditable(false);
		pane.setFont(Main.mcFont);
		return pane;
	}
}
