package net.defekt.mc.chatclient.ui.swing;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextPane;

/**
 * Simple {@link JPanel} extension with vertical {@link BoxLayout}
 * 
 * @author Defective4
 *
 */

public class JVBoxPanel extends JPanel {
	/**
	 * Default constructor
	 */
	public JVBoxPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	/**
	 * Aligns all contained components to the left
	 */
	public void alignAll() {
		for (final Component ct : getComponents())
			if (ct instanceof JComponent) {
				final JComponent jct = (JComponent) ct;
				jct.setAlignmentX(Component.LEFT_ALIGNMENT);
				jct.setOpaque(!(ct instanceof JTextPane));
			}
	}
}
