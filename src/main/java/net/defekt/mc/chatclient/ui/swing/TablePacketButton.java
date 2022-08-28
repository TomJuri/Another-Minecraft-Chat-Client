package net.defekt.mc.chatclient.ui.swing;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import net.defekt.mc.chatclient.protocol.packets.Packet;

/**
 * A button to be used in packet analyzer
 * 
 * 
 * @author Defective4
 *
 */
public class TablePacketButton extends JButton implements TableCellEditor, TableCellRenderer, MouseListener {

    /**
     * Default constructor
     */
    public TablePacketButton() {
    }

    /**
     * Initialize this button with provided table
     * 
     * @param table table to initialize with
     */
    public void init(final JTable table) {
        setText("Info");
        table.getColumn(" ").setCellEditor(this);
        table.getColumn(" ").setCellRenderer(this);
        table.addMouseListener(this);
    }

    @SuppressWarnings("javadoc")
    JButton editor = new JButton("Info");

    private Packet packet = null;

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public boolean isCellEditable(final EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(final EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        return true;
    }

    @Override
    public void cancelCellEditing() {
    }

    @Override
    public void addCellEditorListener(final CellEditorListener l) {
    }

    @Override
    public void removeCellEditorListener(final CellEditorListener l) {
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mousePressed(final MouseEvent e) {
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        return editor;
    }

    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
            final int row, final int column) {
        if (value instanceof Packet) {
            this.packet = (Packet) value;
        }
        return this;
    }

    /**
     * Get last selected packet
     * 
     * @return the packet
     */
    public Packet getPacket() {
        return packet;
    }

}
