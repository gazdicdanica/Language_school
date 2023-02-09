package gui.test;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.Test;
import entity.Ucenik;
import managers.TestManager;

public class UcenikTestTblFrame extends JFrame{

	private Ucenik u;
	private TestManager tm;
	private JTable tbl;
	private JTextField tfTrazi = new JTextField(20);
	private TableRowSorter<AbstractTableModel> tblSort= new TableRowSorter<AbstractTableModel>();
	
	private static final long serialVersionUID = 973353175437519774L;
	
	public UcenikTestTblFrame(Ucenik u, TestManager tm) {
		this.u = u;
		this.tm = tm;
		
		setTitle("Istorija polaganja");
		ImageIcon img = new ImageIcon("./img/languages.png");
		this.setIconImage(img.getImage());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		
		List<Test> t = new ArrayList<Test>();
		for(Test test:tm.getTestovi()) {
			if(test.getUcenici().contains(u)&&test.getOcenjen()) {
				t.add(test);
			}
		}
		
		tbl = new JTable(new RezultatModel(t, u, tm));
		tbl.getTableHeader().setOpaque(false);
		tbl.getTableHeader().setBackground(new Color(204, 166, 166));
		tbl.getTableHeader().setReorderingAllowed(false);
		tbl.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tblSort.setModel((AbstractTableModel) tbl.getModel());
		tbl.setRowSorter(tblSort);
		tbl.getTableHeader().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int index = tbl.getTableHeader().columnAtPoint(arg0.getPoint());
				
				sort(index, t);
			}
			
		});
		
		JScrollPane scr = new JScrollPane(tbl);
		add(scr);
		setSize(700,500);
		setLocationRelativeTo(null);
	}
	
	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer,Integer>(){{put(0,1);put(1,1);put(2,1);put(3,1);}};
	
	private void sort(int i, List<Test> t) {
		t.sort(new Comparator<Test>() {
			int ret = 0;
			@Override
			public int compare(Test o1, Test o2) {
				switch(i) {
				case 0:
					ret = o1.getKurs().toString().compareTo(o2.getKurs().toString());
					break;
				case 1:
					ret = o1.getDatum().compareTo(o2.getDatum());
					break;
				case 2:
					ret = ((Integer)tm.nadjiRezultat(o1, u).getBodovi()).compareTo((Integer)tm.nadjiRezultat(o2, u).getBodovi());
					break;
				case 3:
					ret = ((Integer)tm.nadjiRezultat(o1, u).getOcena()).compareTo((Integer)tm.nadjiRezultat(o2, u).getOcena());
					break;
				default: break;
					
				}return ret*sortOrder.get(i);
			}
			
		});
		sortOrder.put(i,  sortOrder.get(i)*-1);
		refresh();
	}
	
	public void refresh() {
		RezultatModel m = (RezultatModel) tbl.getModel();
		m.fireTableDataChanged();
	}
}
