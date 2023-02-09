package gui.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XChartPanel;

import entity.RezultatTesta;
import entity.Test;
import entity.Ucenik;
import gui.izvestaj.OceneChart;
import managers.UserManager;

public class OceniTblFrame extends JFrame{
	
	private Test t;
	private JTable tbl;
	private TableRowSorter<AbstractTableModel> tblSort = new TableRowSorter<AbstractTableModel>();
	
	private static final long serialVersionUID = 2172901238329846180L;

	public OceniTblFrame(Test t, UserManager um, boolean oceni) {
		this.t = t;
		
		ImageIcon img;
		
		setResizable(false);
		if(oceni) {
			setTitle("Ocenjivanje testa");
			img = new ImageIcon("./img/languages.png");
		}else {
			setTitle("Statistika rezultata");
			img = new ImageIcon("./img/report.png");
		}
		this.setIconImage(img.getImage());
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		if(oceni) {
			for(Ucenik u: t.getUcenici()) {
			RezultatTesta rezultat = new RezultatTesta(u);
			t.getRezultati().add(rezultat);
			}
		}
		
		int deset = 0;
		int devet = 0;
		int osam = 0;
		int sedam = 0;
		int sest = 0;
		int np = 0;
		for(RezultatTesta r:OceniTblFrame.this.t.getRezultati()) {
			if(r.getOcena() == 10)
				deset+=1;
			else if(r.getOcena() == 9)
				devet+=1;
			else if(r.getOcena() == 8)
				osam+=1;
			else if(r.getOcena() == 7)
				sedam+=1;
			else if(r.getOcena() == 6)
				sest+=1;
			else
				np+=1;
		}
		List<Integer> broj = Arrays.asList(deset, devet, osam, sedam, sest, np);
		OceneChart chart = new OceneChart(broj);
		JPanel charPnl = new XChartPanel<CategoryChart>(chart.getChart());		
		
		tbl = new JTable(new OceniModel(this.t, oceni));
		tbl.getTableHeader().setOpaque(false);
		tbl.getTableHeader().setReorderingAllowed(false);
		tbl.getTableHeader().setBackground(new Color(204, 166, 166));
		tbl.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tblSort.setModel((AbstractTableModel) tbl.getModel());
		tbl.setRowSorter(tblSort);
		
		tbl.getTableHeader().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int index = tbl.getTableHeader().columnAtPoint(arg0.getPoint());
				sort(index);
			}
			
		});
		
		JPanel btn = new JPanel();
		JButton btnSacuvaj = new JButton("Sačuvaj");
		btn.add(btnSacuvaj);
		
		btnSacuvaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<OceniTblFrame.this.t.getRezultati().size();i++) {
					if(tbl.getValueAt(i, 2).equals("")) {
						JOptionPane.showMessageDialog(null, "Niste ocenili sve učenike!", "Greška!", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
				}for(RezultatTesta r:t.getRezultati()) {
					if(r.getBodovi()>=51) {
						r.getUcenik().getPolozeno().add(t.getKurs());
					}
				}
				t.setOcenjen();
				um.getTestManager().saveData();
				um.saveData();
				OceniTblFrame.this.dispose();
				return;
			}
		});
		
		
		
		JScrollPane scr = new JScrollPane(tbl);
		add(scr, BorderLayout.CENTER);
		if(oceni) {
			add(scr,BorderLayout.CENTER);
			add(btn, BorderLayout.PAGE_END);
		}else
			add(charPnl, BorderLayout.CENTER);
		
				
		pack();
		setLocationRelativeTo(null);
	}
	
	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer, Integer>(){{put(0,1);put(1,1);put(2,1);}};
	
	private void sort(int i) {
		this.t.getRezultati().sort(new Comparator<RezultatTesta>() {
			int ret = 0;
			@Override
			public int compare(RezultatTesta o1, RezultatTesta o2) {
				switch(i) {
				case 0:
					ret = o1.getUcenik().toString().compareTo(o2.getUcenik().toString());
					break;
				case 1:
					ret = ((Integer)o1.getBodovi()).compareTo(o2.getBodovi());
					break;
				case 2:
					ret = ((Integer)o1.getOcena()).compareTo(o2.getOcena());
					break;
				default:break;
				}
				return ret*sortOrder.get(i);
			}
		});
		sortOrder.put(i, sortOrder.get(i)*-1);
		refresh();
	}
	
	private void refresh() {
		OceniModel m = (OceniModel) this.tbl.getModel();
		m.fireTableDataChanged();
	}
}
