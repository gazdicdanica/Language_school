package gui.sekretar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import entity.Sekretar;
import entity.Zahtev;
import entity.Zahtev.stanje;
import managers.UserManager;

public class SekretarTblFrame extends JFrame{

	private static final long serialVersionUID = 5060363159694726558L;
	private UserManager um;
	private JTable tbl;
	private JButton btnIzmeni = new JButton();
	private JButton btnDodaj = new JButton();
	private JButton btnIzbrisi = new JButton();
	private JToolBar toolbar = new JToolBar();
	private JTextField tfTrazi = new JTextField(20);
	private TableRowSorter<AbstractTableModel> tblSort = new TableRowSorter<AbstractTableModel>();
	
	public SekretarTblFrame(UserManager um) {
		this.um = um;
		
		tbl = new JTable(new SekretarModel(um));
		tbl.getTableHeader().setOpaque(false);
		tbl.getTableHeader().setBackground(new Color(204, 166, 166));
		tbl.getTableHeader().setReorderingAllowed(false);
		tbl.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tblSort.setModel((AbstractTableModel) tbl.getModel());
		tblSort.setSortable(6, false);
		tbl.setRowSorter(tblSort);
		
		tbl.getTableHeader().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int index = tbl.getTableHeader().columnAtPoint(arg0.getPoint());
				sort(index);
			}
			
		});
		
		ImageIcon imgDodaj = new ImageIcon("./img/add.png");
		btnDodaj.setIcon(new ImageIcon(imgDodaj.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		toolbar.add(btnDodaj);
		ImageIcon imgIzmeni = new ImageIcon("./img/edit.png");
		btnIzmeni.setIcon(new ImageIcon(imgIzmeni.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		toolbar.add(btnIzmeni);
		ImageIcon imgIzbrisi = new ImageIcon("./img/remove.png");
		btnIzbrisi.setIcon(new ImageIcon(imgIzbrisi.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		toolbar.add(btnIzbrisi);
		toolbar.setFloatable(false);
		
		btnDodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SekretarAddEdit add = new SekretarAddEdit(um, SekretarTblFrame.this, null);
				add.setVisible(true);
			}
		});
		
		btnIzmeni.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row == -1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijednog sekretara!", "Greška!", JOptionPane.WARNING_MESSAGE);
				}else {
					int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
					Sekretar s = um.getSekretarManager().nadjiSekretaraPoId(id);
					if(s!=null) {
						SekretarAddEdit dlg = new SekretarAddEdit(um, SekretarTblFrame.this, s);
						dlg.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "Nismo uspeli da pronađemo sekretara.", "Greška!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		btnIzbrisi.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row != -1) {
					int id = Integer.parseInt(tbl.getValueAt(row,0).toString());
					Sekretar s = um.getSekretarManager().nadjiSekretaraPoId(id);
					if(s == null) {
						JOptionPane.showMessageDialog(null, "Nismo uspeli da pronađemo sekretara.", "Greška!", JOptionPane.ERROR_MESSAGE);
					}else {
						Object[] options = {"Da", "Ne"};
						int answer = JOptionPane.showOptionDialog(null, "Da li želite da obrišete sekretara " + s.getIme() + " " + s.getPrezime() 
						+ "?", "Da li ste sigurni?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
						switch(answer) {
						case JOptionPane.YES_OPTION:
							um.getSekretarManager().delSekretar(s);
							refresh();
						case JOptionPane.NO_OPTION:
							break;
						}
					}
					
				}else {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijednog sekretara.", "Greška!", JOptionPane.WARNING_MESSAGE);
				}				
			}
		});
		
		toolbar.add(new JLabel("Pretraga:  "));
		tfTrazi.setMaximumSize(tfTrazi.getPreferredSize());
		toolbar.add(tfTrazi);
		
		JScrollPane scr = new JScrollPane(tbl);
		add(toolbar, BorderLayout.NORTH);
		add(scr, BorderLayout.CENTER);
		setTitle("Sekretari");
		setSize(800, 450);
		setResizable(false);
		setLocationRelativeTo(null);
		ImageIcon img = new ImageIcon("./img/languages.png");
		this.setIconImage(img.getImage());
		
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
				if(tfTrazi.getText().trim().length()==0) {
					tblSort.setRowFilter(null);
				}else {
					tblSort.setRowFilter(RowFilter.regexFilter("(?i)" + tfTrazi.getText().trim()));
				}
			}
		});
		
		JPanel btnPnl = new JPanel();
		JButton izvestajBtn = new JButton("Broj obrađenih zahteva");
		
		izvestajBtn.addActionListener(new ActionListener(){
    
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row == -1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijednog sekretara", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
				Sekretar s = um.getSekretarManager().nadjiSekretaraPoId(id);
				JFrame f = new JFrame();
				ImageIcon img = new ImageIcon("./img/report.png");
				f.setIconImage(img.getImage());
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				f.setTitle(s.toString() + " - Izveštaj");
				
				int br = 0;
				for(Zahtev z:um.getZahtevManager().getZahtevi()) {
					if(!(z.getStanje().equals(stanje.uObradi)) && z.getSekretar().equals(s))
						br+=1;
				}
				f.add(new JLabel("Broj zahteva koje je obradio sekretar " + s.toString() + ": " +br), BorderLayout.CENTER);
				f.setSize(350,100);
				f.setLocationRelativeTo(null);
				f.setVisible(true);
			}
		});
		
		btnPnl.add(izvestajBtn);
		add(btnPnl, BorderLayout.PAGE_END);
	}
	
	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer, Integer>() {{put(0, 1);put(1, 1);put(2, 1);put(3, 1);put(4, 1);put(5, 1);put(6, 1);put(7, 1);put(8,1);}};

	
	protected void sort(int i) {
		this.um.getSekretari().sort(new Comparator<Sekretar>() {
			int ret = 0;
			@Override
			public int compare(Sekretar o1, Sekretar o2) {
				switch(i) {
					case 0:
						ret = ((Integer)o1.getId()).compareTo((Integer)o2.getId());
						break;
					case 1:
						ret = o1.getKorisnickoIme().compareTo(o2.getKorisnickoIme());
						break;
					case 2:
						ret = o1.getIme().compareTo(o2.getIme());
						break;
					case 3:
						ret = o1.getPrezime().compareTo(o2.getPrezime());
						break;
					case 4:
						ret = o1.getPol().compareTo(o2.getPol());
						break;
					case 5:
						ret = o1.getDatumRodjenja().compareTo(o2.getDatumRodjenja());
						break;
					case 7:
						ret = o1.getAdresa().compareTo(o2.getAdresa());
						break;
					case 8:
						ret = ((Double)o1.getPlata()).compareTo((Double)o2.getPlata());
					default:
						break;
						
				}return ret*sortOrder.get(i);
				
			}
			
		});
		
		sortOrder.put(i, sortOrder.get(i)*-1);
		refresh();
	}
	
	protected void refresh() {
		SekretarModel sekretarM = (SekretarModel) this.tbl.getModel();
		sekretarM.fireTableDataChanged();
	}
}
