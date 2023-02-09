package gui.test;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import entity.Test;

@SuppressWarnings("rawtypes")
public class TestListBoxModel extends AbstractListModel{
	private List<Test> testovi = new ArrayList<Test> ();
	private static final long serialVersionUID = -6525231413531235307L;

	public TestListBoxModel(List<Test> testovi) {
		this.testovi = testovi;
	}
	
	@Override
	public int getSize() {
		return testovi.size();
	}

	@Override
	public Object getElementAt(int index) {
		return testovi.get(index);
	}

}
