package gui.test;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.RezultatTesta;
import entity.Test;
import entity.Ucenik;
import managers.TestManager;

public class RezultatModel extends AbstractTableModel{
	
	private List<Test> testovi;
	private Ucenik u;
	private TestManager tm;
	private String[] kolone = {"Kurs", "Datum", "Bodovi", "Ocena"};

	private static final long serialVersionUID = 8339326048172398715L;
	
	public RezultatModel(List<Test> testovi, Ucenik u, TestManager tm) {
		this.testovi = testovi;
		this.u = u;
		this.tm = tm;
	}

	@Override
	public int getRowCount() {
		return testovi.size();
	}

	@Override
	public int getColumnCount() {
		return kolone.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			Test t = testovi.get(rowIndex);
			RezultatTesta r = tm.nadjiRezultat(t, u);
			switch(columnIndex) {
			case 0: return t.getKurs().toString();
			case 1: return t.getDatum();
			case 2: return r.getBodovi();
			case 3: return r.tabelaOcena();
			default: return null;
			}
		}catch(IndexOutOfBoundsException e) {
			return "" ;
		}
		
	}
	
	@Override
	public String getColumnName(int i) {
		return this.kolone[i];
	}
	
	@Override
	public Class<?> getColumnClass(int colIndex){
		return this.getValueAt(0,  colIndex).getClass();
	}

}
