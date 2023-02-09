package gui.jezik;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import entity.Jezik;

public class JezikListRenderer extends JLabel implements ListCellRenderer{

	private static final long serialVersionUID = 6348204038561759240L;

	public JezikListRenderer() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Jezik j = (Jezik) value;
		if(j != null) {
			setText(j.getJezik());
			setIcon(new ImageIcon(j.getIcon().getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH)));
			if(isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
		}
		return this;
	}

}
