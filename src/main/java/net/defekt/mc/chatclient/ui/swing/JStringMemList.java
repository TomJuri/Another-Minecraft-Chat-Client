package net.defekt.mc.chatclient.ui.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An extension of {@link JMemList} made for String storage
 * 
 * @author Defective4
 *
 */
public class JStringMemList extends JMemList<String> {
    private static final long serialVersionUID = 1L;

    /**
     * Add a string
     * 
     * @param value string
     */
    public void addString(final String value) {
        final List<String> list = new ArrayList<String>();
        Collections.addAll(list, getListData() == null ? new String[0] : getListData());
        list.add(value);
        setListData(list.toArray(new String[list.size()]));
    }

    /**
     * Remove a string at given index
     * 
     * @param index index of string
     */
    public void removeString(final int index) {
        if (index != -1) {
            final List<String> list = new ArrayList<String>();
            Collections.addAll(list, getListData() == null ? new String[0] : getListData());
            list.remove(index);
            setListData(list.toArray(new String[list.size()]));
            setSelectedIndex(index >= list.size() ? index - 1 : index);
        }
    }
}