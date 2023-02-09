package gui.jezik;

import java.util.List;

import javax.swing.AbstractListModel;

import entity.Jezik;

public class JezikListBoxModel extends AbstractListModel{

	private static final long serialVersionUID = 503790034090227913L;

	private List<Jezik> jezici;
	
	public JezikListBoxModel(List<Jezik> jezici) {
		this.jezici = jezici;
	}
	
	@Override
	public int getSize() {
		return jezici.size();
	}

	@Override
	public Object getElementAt(int index) {
		return jezici.get(index);
	}

}
