package gui.test;

import javax.swing.table.AbstractTableModel;

import entity.Kurs;
import entity.Test;
import managers.TestManager;

public class TestModel extends AbstractTableModel{

	private TestManager tm;
	private Kurs k;
	private String[] kolone = {"ID", "Kurs", "Datum", "Broj prijavljenih učenika", "Ocenjen"};
	
	private static final long serialVersionUID = 5982902282880476326L;

	public TestModel(TestManager tm, Kurs k) {
		this.tm = tm;
		this.k = k;
	}
	
	@Override
	public int getRowCount() {
		return tm.getTestoviZaKurs(k).size();
	}

	@Override
	public int getColumnCount() {
		return kolone.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			Test t = this.tm.getTestoviZaKurs(k).get(rowIndex);
			return t.toCell(columnIndex);
		}catch(IndexOutOfBoundsException e) {
			return "";
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
