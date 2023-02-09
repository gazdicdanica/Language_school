package gui.test;

import javax.swing.table.AbstractTableModel;

import entity.RezultatTesta;
import entity.Test;
import entity.Ucenik;

public class OceniModel extends AbstractTableModel{

	private Test t;
	private boolean oceni;
	private String kolone[] = {"Učenik", "Bodovi", "Ocena"};
	
	private static final long serialVersionUID = 228813000630273082L;

	public OceniModel(Test t, boolean oceni) {
		this.t = t;
		this.oceni = oceni;
	}
	
	@Override
	public int getRowCount() {
		return t.getRezultati().size();
	}

	@Override
	public int getColumnCount() {
		return kolone.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			RezultatTesta r = t.getRezultati().get(rowIndex);
			switch(columnIndex) {
			case 0: return r.getUcenik().toString();
			case 1: return r.getBodovi();
			case 2: return r.tabelaOcena();
			default:return null;
			}
		}catch(IndexOutOfBoundsException e) {
			return"";
		}
	}
	
	@Override
	public String getColumnName(int i ) {
		return this.kolone[i];
	}
	
	@Override
	public Class<?> getColumnClass(int colIndex){
		return this.getValueAt(0,  colIndex).getClass();
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if(column ==1 && oceni)
			return true;
		return false;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		RezultatTesta r = t.getRezultati().get(row);
		if(col == 1) {
			r.setBodovi((int)value);
		}fireTableCellUpdated(row, col);
	}

}
