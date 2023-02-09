package gui.sekretar;

import java.time.LocalDate;

import javax.swing.table.AbstractTableModel;

import entity.Sekretar;
import managers.UserManager;

public class SekretarModel extends AbstractTableModel{

	private static final long serialVersionUID = -416456187648143314L;

	private UserManager um;
	private String kolone [] = {"ID", "Korisnicko ime", "Ime", "Prezime", "Pol", "Datum rodjenja", "Broj telefona", "Adresa", "Plata"};
	
	public SekretarModel(UserManager um) {
		this.um = um;
	}
	
	@Override
	public int getRowCount() {
		return um.getSekretari().size();
	}

	@Override
	public int getColumnCount() {
		return kolone.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			Sekretar s = this.um.getSekretari().get(rowIndex);
			return s.toCell(columnIndex);
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
