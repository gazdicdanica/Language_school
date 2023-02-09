package gui.kurs;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

import entity.Kurs;
import managers.KursManager;

public class KursModel extends AbstractTableModel{
	private List<Kurs> kursevi;
	private KursManager km;
	private JFrame parent;
	private String[] kolone = {"ID", "Naziv", "Jezik", "Cena kursa", "Cena pojedinačnog testa"};

	private static final long serialVersionUID = -8889198592357880666L;

	public KursModel(List<Kurs> kursevi, KursManager km, JFrame parent) {
		this.kursevi = kursevi;
		this.km = km;
		this.parent = parent;
	}
	
	@Override
	public int getRowCount() {
		return kursevi.size();
	}

	@Override
	public int getColumnCount() {
		return kolone.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			Kurs k = this.kursevi.get(rowIndex);
			switch(columnIndex) {
			case 0:return k.getId();
			case 1:return k.getNaziv();
			case 2:return k.getJezik();
			case 3:
				if(parent.getClass().getSimpleName().equals("PreporukeFrm")) {
					return km.getCenovnikManager().getCenovnik().getPopusti().get(k);
				}else if(parent.getClass().getSimpleName().equals("UcenikFrame")) {
					if(rowIndex>0) {
						return km.getCenovnikManager().getCenovnik().getPopusti().get(k);
					}else {
						return km.getCenovnikManager().getCenovnik().getCene().get(k);
					}
				}else {
					return km.getCenovnikManager().getCenovnik().getCene().get(k);
				}
			case 4:return km.getCenovnikManager().getCenovnik().getCeneTesta().get(k);
			}
			return null;
		}catch(IndexOutOfBoundsException e) {
			return "";
		}
		
	}
	
	@Override 
	public String getColumnName(int col) {
		if(col == 3 && parent.getClass().getSimpleName().equals("PreporukeFrm")) {
			return "Cena kursa sa popustom";
		}
		return this.kolone[col];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.getValueAt(0, columnIndex).getClass();
	}
	
	@Override
	public boolean isCellEditable(int row,int column) {
		if(parent.getClass().getSimpleName().equals("AdminFrame")) {
			if(column == 3 || column == 4)
				return true;
		}return false;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		Kurs k = kursevi.get(row);
		if(col == 3) {
			km.getCenovnikManager().getCenovnik().getCene().put(k, (Float)value);
		}else if(col == 4) {
			km.getCenovnikManager().getCenovnik().getCeneTesta().put(k, (Float)value);
		}fireTableCellUpdated(row, col);
	}

}
