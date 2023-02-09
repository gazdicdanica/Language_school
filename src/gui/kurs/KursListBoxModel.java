package gui.kurs;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import entity.Kurs;
import entity.Test;

@SuppressWarnings("rawtypes")
public class KursListBoxModel extends AbstractListModel{

	private static final long serialVersionUID = -6566558773425690287L;
	private List<Kurs> kursevi= new ArrayList<Kurs> ();
	
	public KursListBoxModel(List<Kurs> kursevi) {
		this.kursevi = kursevi;
	}
	
	@Override
	public int getSize() {
		return kursevi.size();
	}

	@Override
	public Object getElementAt(int index) {
		return kursevi.get(index);
	}

}
