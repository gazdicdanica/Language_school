package gui.kurs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import entity.Kurs;

@SuppressWarnings("rawtypes")
public class KursListRenderer extends JLabel implements ListCellRenderer{

	private static final long serialVersionUID = 683767215607605631L;

	public KursListRenderer() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Kurs k = (Kurs) value;
		if(k != null) {
			setText(k.getNaziv());
			setIcon(new ImageIcon(k.getJezik().getIcon().getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
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
