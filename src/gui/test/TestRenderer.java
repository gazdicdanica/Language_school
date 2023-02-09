package gui.test;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import entity.Test;

@SuppressWarnings("rawtypes")
public class TestRenderer extends JLabel implements ListCellRenderer{

	private static final long serialVersionUID = 1484046702918243499L;

	public TestRenderer() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Test t = (Test) value;
		if(t!=null) {
			setText(t.getDatum().toString());
			if ( isSelected ) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
		}
		return this;
	}

}
