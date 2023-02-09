package gui.zahtev;

import javax.swing.table.AbstractTableModel;

import entity.Zahtev;
import managers.ZahtevManager;

public class ZahtevModel extends AbstractTableModel{

	private ZahtevManager zm;
	private String[] kolone = {"ID", "Učenik", "Kurs"};
	
	private static final long serialVersionUID = 6486541096478400146L;

	public ZahtevModel(ZahtevManager zm) {
		this.zm = zm;
	}
	
	@Override
	public int getRowCount() {
		return zm.getNeobradjeniZahtevi().size();
	}

	@Override
	public int getColumnCount() {
		return kolone.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			Zahtev z = this.zm.getNeobradjeniZahtevi().get(rowIndex);
			return z.toCell(columnIndex);
		}catch(IndexOutOfBoundsException e) {
			return "";
		}
		
	}
	
	@Override
	public String getColumnName(int col) {
		return this.kolone[col];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex){
		return this.getValueAt(0, columnIndex).getClass();
	}
}
