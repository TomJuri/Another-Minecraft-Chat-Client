package net.defekt.mc.chatclient.integrations.discord;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.callbacks.JoinGameCallback;
import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.ui.Main;
import net.defekt.mc.chatclient.ui.Messages;

public class DiscordPresence {

    private final String id;
    private final Main main;
    private final JTabbedPane tabs;
    private final Map<JSplitPane, MinecraftClient> clients;

    public DiscordPresence(String id, Main main, JTabbedPane tabPane, Map<JSplitPane, MinecraftClient> clients) {
        this.main = main;
        this.id = id;
        this.tabs = tabPane;
        this.clients = clients;

        tabs.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                update();
            }
        });
    }

    public void start() {
        if (!Main.up.isDisableDiscordPresence()) {
            DiscordRPC.discordInitialize(id,
                    new DiscordEventHandlers.Builder().setJoinGameEventHandler(new JoinGameCallback() {

                        @Override
                        public void apply(String arg0) {
                            System.out.println(arg0);
                        }
                    }).build(), true);

            update();
        }
    }

    public void update() {
        if (Main.up.isDisableDiscordPresence()) {
            DiscordRPC.discordShutdown();
            return;
        }
        MinecraftClient[] clients = main.getClients();
        DiscordRichPresence presence;
        if (clients.length <= 0) {
            presence = new DiscordRichPresence.Builder(Messages.getString("Discord.menu"))
                    .setBigImage("logo", "AMCC v" + Main.VERSION).build();
        } else {
            if (clients.length == 1 && clients[0].isConnected()) {
                MinecraftClient client = clients[0];
                presence = new DiscordRichPresence.Builder(Messages.getString("Discord.chatting") + ": "
                        + (Main.up.isHideDiscordServer() ? Messages.getString("Discord.hidden")
                                : client.getHost() + (client.getPort() == 25565 ? "" : ":" + client.getPort())))
                                        .setBigImage("logo", "AMCC v" + Main.VERSION)
                                        .setDetails(Messages.getString("Discord.nick") + ": "
                                                + (Main.up.isHideDiscordNickname()
                                                        ? Messages.getString("Discord.hidden")
                                                        : client.getUsername()))
                                        .setStartTimestamps(client.getStartDate()).build();
            } else {
                MinecraftClient current = null;
                Component selected = tabs.getSelectedComponent();
                if (selected != null && selected instanceof JSplitPane) {
                    current = this.clients.get(selected);
                }
                if (current == null) {
                    List<String> nicknames = new ArrayList<String>();
                    List<String> hosts = new ArrayList<String>();
                    for (MinecraftClient client : clients) {
                        String nickname = client.getUsername();
                        String host = client.getHost();
                        if (!nicknames.contains(nickname)) nicknames.add(nickname);
                        if (!hosts.contains(host)) hosts.add(host);
                    }
                    presence = new DiscordRichPresence.Builder(
                            hosts.size() == 1
                                    ? Messages.getString("Discord.chatting") + ": "
                                            + (Main.up.isHideDiscordServer() ? Messages.getString("Discord.hidden")
                                                    : hosts.get(0))
                                    : Messages.getString("Discord.chatting") + " " + clients.length + " "
                                            + Messages.getString("Discord.servSuffix"))
                                                    .setBigImage("logo",
                                                            "AMCC v" + Main.VERSION)
                                                    .setDetails(
                                                            nicknames.size() == 1
                                                                    ? Messages.getString("Discord.nick") + ": "
                                                                            + (Main.up.isHideDiscordNickname()
                                                                                    ? Messages
                                                                                            .getString("Discord.hidden")
                                                                                    : nicknames.get(0))
                                                                    : Messages.getString("Discord.sesPrefix") + " "
                                                                            + nicknames.size() + " "
                                                                            + Messages.getString("Discord.sesSuffix"))
                                                    .build();
                } else {
                    presence = new DiscordRichPresence.Builder(Messages.getString("Discord.chatting") + ": "
                            + (Main.up.isHideDiscordServer() ? Messages.getString("Discord.hidden")
                                    : current.getHost() + (current.getPort() == 25565 ? "" : ":" + current.getPort())))
                                            .setBigImage("logo", "AMCC v" + Main.VERSION)
                                            .setDetails(Messages.getString("Discord.nick") + ": "
                                                    + (Main.up.isHideDiscordNickname()
                                                            ? Messages.getString("Discord.hidden")
                                                            : current.getUsername()))
                                            .setStartTimestamps(current.getStartDate()).build();
                }
            }
        }
        DiscordRPC.discordUpdatePresence(presence);

    }
}
