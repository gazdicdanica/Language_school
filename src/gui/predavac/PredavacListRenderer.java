package gui.predavac;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import entity.Predavac;

@SuppressWarnings("rawtypes")
public class PredavacListRenderer extends JLabel implements ListCellRenderer{

	private static final long serialVersionUID = 1568172876022699679L;

	public PredavacListRenderer() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Predavac p = (Predavac) value;
		if (p!=null) {
			setText(p.toString());
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
