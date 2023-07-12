package net.defekt.mc.chatclient.ui.swing;

import javax.swing.*;

/**
 * An extension of {@link JList} allowing to get list data that was set before
 *
 * @param <E> the type of the elements of this list
 * @author Defective4
 */
public class JMemList<E> extends JList<E> {
    private static final long serialVersionUID = 1L;

    private E[] listData = null;

    /**
     * Get stored list data
     *
     * @return list data contained in this object
     */
    public E[] getListData() {
        return listData;
    }

    @Override
    public void setListData(final E[] entries) {
        if (entries == null) return;
        super.setListData(entries);
        listData = entries;
    }
}
