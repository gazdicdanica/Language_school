package gui.predavac;

import javax.swing.table.AbstractTableModel;

import entity.Predavac;
import managers.UserManager;

public class PredavacModel extends AbstractTableModel{

	private UserManager um;
	private String[] kolone = {"ID", "Korisnicko ime", "Ime", "Prezime", "Pol", "Datum rodjenja", "Broj telefona", "Adresa", "Plata"};
	
	private static final long serialVersionUID = 73609438372823943L;

	public PredavacModel(UserManager um) {
		this.um = um;
	}
	
	@Override
	public int getRowCount() {
		return um.getPredavaci().size();
	}

	@Override
	public int getColumnCount() {
		return kolone.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			Predavac p = this.um.getPredavaci().get(rowIndex);
			return p.toCell(columnIndex);
		}catch(IndexOutOfBoundsException e) {
			return "";
		}
		
	}
	
	@Override
	public String getColumnName(int col) {
		return this.kolone[col];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.getValueAt(0, columnIndex).getClass();
	}

}
