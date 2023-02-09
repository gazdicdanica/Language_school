package gui.predavac;

import java.util.List;

import javax.swing.AbstractListModel;

import entity.Predavac;

@SuppressWarnings("rawtypes")
public class PredavacListBoxModel extends AbstractListModel{

	private List<Predavac> predavaci;
	private static final long serialVersionUID = 4703187158145735968L;

	public PredavacListBoxModel(List<Predavac> predavaci) {
		this.predavaci = predavaci;
	}
	
	@Override
	public int getSize() {
		return predavaci.size();
	}

	@Override
	public Object getElementAt(int index) {
		return predavaci.get(index);
	}

}
