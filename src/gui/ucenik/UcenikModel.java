package gui.ucenik;

import javax.swing.table.AbstractTableModel;

import entity.Ucenik;
import managers.UserManager;

public class UcenikModel extends AbstractTableModel{
	private UserManager um;
	private String[] kolone = {"ID", "Korisnicko ime", "Ime", "Prezime", "Pol", "Datum rodjenja", "Broj telefona", "Adresa"};

	private static final long serialVersionUID = -8363578500054298107L;
	
	public UcenikModel(UserManager um) {
		this.um = um;
		
	}

	@Override
	public int getRowCount() {
		return um.getUcenici().size();
	}

	@Override
	public int getColumnCount() {
		return kolone.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			Ucenik u = this.um.getUcenici().get(rowIndex);
			return u.toCell(columnIndex);
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
