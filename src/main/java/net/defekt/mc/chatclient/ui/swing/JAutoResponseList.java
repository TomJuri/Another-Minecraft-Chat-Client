package net.defekt.mc.chatclient.ui.swing;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import net.defekt.mc.chatclient.ui.AutoResponseRule;

@SuppressWarnings("javadoc")
public class JAutoResponseList extends JMemList<AutoResponseRule> {
	private static final long serialVersionUID = 1L;

	private class ResponseRuleCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(final JList<? extends Object> list, final Object value,
				final int index, final boolean isSelected, final boolean cellHasFocus) {

			final JLabel ct = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			final AutoResponseRule rule = (AutoResponseRule) value;
			ct.setText(rule.getName());

			return ct;
		}
	}

	public JAutoResponseList() {
		setCellRenderer(new ResponseRuleCellRenderer());
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
		if (index <= -1 || getListData() == null)
			return;
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
		if (getListData() == null)
			return;
		final AutoResponseRule[] rs = getListData();
		rs[index] = rule;
		setListData(rs);
	}
}
