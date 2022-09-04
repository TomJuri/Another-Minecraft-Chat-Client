package net.defekt.mc.chatclient.ui.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.defekt.mc.chatclient.ui.AutoResponseRule;

@SuppressWarnings("javadoc")
public class JAutoResponseList extends JMemList<AutoResponseRule> {
    private static final long serialVersionUID = 1L;

    public JAutoResponseList() {
    }

    public void addRule(final AutoResponseRule rule) {
        final List<AutoResponseRule> list = new ArrayList<AutoResponseRule>();
        if (getListData() != null) {
            Collections.addAll(list, getListData());
        }
        list.add(rule);
        setListData(list.toArray(new AutoResponseRule[list.size()]));
    }

    public void removeRule(final int index) {
        if (index <= -1 || getListData() == null) return;
        final List<AutoResponseRule> list = new ArrayList<AutoResponseRule>();
        final AutoResponseRule[] d = getListData();
        for (int x = 0; x < d.length; x++)
            if (x != index) {
                list.add(d[x]);
            }
        setListData(list.toArray(new AutoResponseRule[list.size()]));
        setSelectedIndex(index >= list.size() ? index - 1 : index);
    }

    public void setRule(final AutoResponseRule rule, final int index) {
        if (getListData() == null) return;
        final AutoResponseRule[] rs = getListData();
        rs[index] = rule;
        setListData(rs);
    }
}
