package net.defekt.mc.chatclient.ui.swing;

import net.defekt.mc.chatclient.protocol.packets.PacketFactory;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.util.Collection;

public class VersionFieldRenderer extends BasicComboBoxRenderer {

    private final Collection<String> userVers = PacketFactory.getUserVersions().values();

    @Override
    public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        final Component val = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof String && val instanceof JLabel) {
            if (userVers.contains(value)) {
                final JLabel label = (JLabel) val;
                label.setText(value + " (Plugin)");
            }
        }

        return val;
    }

}
