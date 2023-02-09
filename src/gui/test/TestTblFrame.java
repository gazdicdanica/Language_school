package gui.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.Kurs;
import entity.Predavac;
import entity.Test;
import gui.izvestaj.TestIzvestaj;
import managers.TestManager;
import managers.UserManager;

public class TestTblFrame extends JFrame{

	private static final long serialVersionUID = 6554550006007354433L;
	private Kurs kurs;
	private Predavac predavac;
	private TestManager tm;
	private JTextField tfTrazi = new JTextField(20);
	private JButton btnIzmeni = new JButton();
	private JButton btnDodaj = new JButton();
	private JButton btnIzbrisi = new JButton();
	private JTable tbl;
	private TableRowSorter<AbstractTableModel> tblSort = new TableRowSorter<AbstractTableModel>();
	
	public TestTblFrame(Kurs k, Predavac p, UserManager um) {
		kurs = k;
		this.predavac = p;
		this.tm = um.getTestManager();
		setTitle("Testovi za kurs: " + k.toString());
		ImageIcon img = new ImageIcon("./img/languages.png");
		this.setIconImage(img.getImage());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		tbl = new JTable(new TestModel(tm, k));
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
				
				sort(index);
			}
			
		});
		
		JScrollPane scr = new JScrollPane(tbl);
		
		
		JToolBar toolbar = new JToolBar();
		
		ImageIcon imgDodaj = new ImageIcon("./img/add.png");
		btnDodaj.setIcon(new ImageIcon(imgDodaj.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		
		btnDodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new TestAddEdit(tm, null, kurs, predavac);
				refresh();
			}
		});
		
		toolbar.add(btnDodaj);
		ImageIcon imgIzmeni = new ImageIcon("./img/edit.png");
		btnIzmeni.setIcon(new ImageIcon(imgIzmeni.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		
		btnIzmeni.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row == -1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan test", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}else {
					int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
					Test t = tm.nadjiTestPoId(id);
					if(t==null) {
						JOptionPane.showMessageDialog(null, "Nismo uspeli da nadjemo test.", "Error", JOptionPane.ERROR_MESSAGE);
					}else {
						if(t.getUcenici().size()>0) {
							JOptionPane.showMessageDialog(null, "Datum ne može više biti promenjen\njer postoje učenici koji su se već prijavili za polaganje.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
						}else {
							new TestAddEdit(tm, t, kurs, predavac);
							refresh();
						}
					}
				}
			}
		});
		
		toolbar.add(btnIzmeni);
		ImageIcon imgIzbrisi = new ImageIcon("./img/remove.png");
		btnIzbrisi.setIcon(new ImageIcon(imgIzbrisi.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		
		btnIzbrisi.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row==-1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan test.", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
				Test t = tm.nadjiTestPoId(id);
				if(t==null) {
					JOptionPane.showMessageDialog(null, "Nismo uspeli da nadjemo test.", "Error", JOptionPane.ERROR_MESSAGE);
				}else {
					boolean bool = tm.obrisiTest(t);
					if(!bool)
						JOptionPane.showMessageDialog(null, "Nije moguće obrisati test jer postoje učenici koji su se prijavili za polaganje.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
					else {
						refresh();
					}
				}
			}
		});
		
		toolbar.add(btnIzbrisi);
		toolbar.setFloatable(false);
		toolbar.add(new JLabel("  Pretraga:  "));
		toolbar.add(tfTrazi);
		tfTrazi.setMaximumSize(tfTrazi.getPreferredSize());
		tfTrazi.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (tfTrazi.getText().trim().length() == 0) {
				     tblSort.setRowFilter(null);
				  } else {
					  tblSort.setRowFilter(RowFilter.regexFilter("(?i)" + tfTrazi.getText().trim()));
				  }
			}
		});
		
		JPanel pnlOceni = new JPanel();
		JButton btnOceni = new JButton("Oceni test");
		
		btnOceni.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row==-1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan test.", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
				Test t = tm.nadjiTestPoId(id);
				if(t==null) {
					JOptionPane.showMessageDialog(null, "Nismo uspeli da nadjemo test.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(t.getDatum().compareTo(LocalDate.now())>0) {
					JOptionPane.showMessageDialog(null, "Ovaj test još nije održan.", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(t.getUcenici().size()==0) {
					JOptionPane.showMessageDialog(null, "Ovaj test nije polagao nijedan učenik.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
					return;
				}if(t.getOcenjen()) {
					JOptionPane.showMessageDialog(null,"Test je već ocenjen.", "Obaveštenje!", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				OceniTblFrame f = new OceniTblFrame(t, um, true);
				f.setVisible(true);
				refresh();

			}
		});
		
		pnlOceni.add(btnOceni);
		
		JButton btnPrikazi = new JButton("Grafički prikaz rezultata");
		
		btnPrikazi.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row == -1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan test.", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
				Test t = tm.nadjiTestPoId(id);
				if(!t.getOcenjen()) {
					JOptionPane.showMessageDialog(null,"Test nije ocenjen.", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}OceniTblFrame f = new OceniTblFrame(t, um, false);
				f.setVisible(true);
				
			}
		});
		
		JButton btnStampaj = new JButton("Odštampaj rezultate");
		
		btnStampaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row == -1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan test.", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
				Test t = tm.nadjiTestPoId(id);
				if(!t.getOcenjen()) {
					JOptionPane.showMessageDialog(null, "Ovaj test nije ocenjen.", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}new TestIzvestaj(t);
			}
		});
		
		pnlOceni.add(btnStampaj);
		pnlOceni.add(btnPrikazi);
		
		add(toolbar, BorderLayout.PAGE_START);
		add(scr, BorderLayout.CENTER);
		add(pnlOceni, BorderLayout.PAGE_END);
		
		setSize(700,500);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	@SuppressWarnings("serial")
	private Map<Integer,Integer> sortOrder =  new HashMap<Integer, Integer>(){{put(0,1);put(1,1);put(2,1);put(3,1);}};
	
	private void sort(int i) {
		this.tm.getTestoviZaKurs(kurs).sort(new Comparator<Test>() {
			int ret = 0;
			@Override
			public int compare(Test o1, Test o2) {
				switch(i) {
				case 0:
					ret = ((Integer)o1.getId()).compareTo((Integer)o2.getId());
					break;
				case 1:
					ret = o1.getKurs().toString().compareTo(o2.getKurs().toString());
					break;
				case 2:
					ret = o1.getDatum().compareTo(o2.getDatum());
				case 3:
					ret = ((Integer)o1.getUcenici().size()).compareTo((Integer)o2.getUcenici().size());
					break;
				default: break;
				}return ret*sortOrder.get(i);
			}
			
		});
		sortOrder.put(i, sortOrder.get(i)*-1);
		refresh();
	}
	
	public void refresh() {
		TestModel tModel =(TestModel) tbl.getModel();
		tModel.fireTableDataChanged();
	}
}
