package gui.predavac;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.Kurs;
import entity.Predavac;
import gui.izvestaj.AnalizaUcenika;
import gui.izvestaj.StatistikaRezultata;
import gui.kurs.KursModel;
import gui.main.LoginFrame;
import gui.test.TestModel;
import gui.test.TestTblFrame;
import managers.TestManager;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class PredavacFrame extends JFrame{
	private Predavac predavac;
	private UserManager um;
	private TestManager tm;
	private JFrame loginFrame;
	private JTextField tfTrazi = new JTextField(20);
	private JTable tbl;
	private TableRowSorter<AbstractTableModel> tblSort = new TableRowSorter<AbstractTableModel>();

	private static final long serialVersionUID = -8020221574871043315L;

	public PredavacFrame(Predavac predavac, UserManager um, TestManager tm, JFrame loginFrame) {
		this.predavac = predavac;
		this.um = um;
		this.tm = tm;
		this.loginFrame = loginFrame;
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				handleClosing();
			}
		});
		this.setTitle("Škola stranih jezika Lingua");
		ImageIcon img = new ImageIcon("./img/languages.png");
		this.setIconImage(img.getImage());
		
		setResizable(false);
		
		JMenuBar mainMenu = new JMenuBar();
		
		JMenu analizaMenu = new JMenu("Analiza učenika");
		JMenuItem analizaMI = new JMenuItem("Statistika po starosnim grupama");
		
		analizaMI.addActionListener(new ActionListener(){
    
			@Override
			public void actionPerformed(ActionEvent e) {
				AnalizaUcenika f = new AnalizaUcenika(um);
				f.setVisible(true);
			}
		});
		
		analizaMenu.add(analizaMI);
		mainMenu.add(analizaMenu);
		
		JMenu podaciMenu = new JMenu("Lični podaci");
		JMenuItem sifraMI = new JMenuItem("Promeni lozinku");
		sifraMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PredavacFrame.this.promeniLozinkuDlg();
			}
		});
		
		podaciMenu.add(sifraMI);
		mainMenu.add(podaciMenu);
		
		setJMenuBar(mainMenu);
		
		JButton btnLogout = new JButton("Odjavi se");
		btnLogout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] options = {"Da", "Ne"};
				int answer = JOptionPane.showOptionDialog(null, "Odjavite se sa profila?", "Odjava", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				switch(answer) {
				case JOptionPane.YES_OPTION:
					PredavacFrame.this.dispose();
					LoginFrame f = (LoginFrame) loginFrame;
					f.getTfUname().setText("");
					f.getPfPassword().setText("");
					f.setVisible(true);
					break;
				case JOptionPane.NO_OPTION:
					break;
				}
			}
		});
		
		JPanel btnPnl = new JPanel();
		btnPnl.setLayout(new BoxLayout(btnPnl, BoxLayout.LINE_AXIS));
		btnPnl.setPreferredSize(new Dimension(700,40));
		getContentPane().add(btnPnl, BorderLayout.PAGE_START);
		
		
		tbl = new JTable(new KursModel(predavac.getKursevi(), this.um.getKursManager(), PredavacFrame.this));
		
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
		
		JPanel prikaziPnl = new JPanel();
		JButton prikaziBtn = new JButton("Prikaži testove");
		prikaziBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row == -1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan kurs", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}
				int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
				Kurs k = um.getKursManager().nadjiKursPoId(id);
				TestTblFrame f = new TestTblFrame(k, predavac, PredavacFrame.this.um);				
			}
		});
		
		JButton statistikaBtn = new JButton("Statistika rezultata");
		statistikaBtn.addActionListener(new ActionListener(){
    
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row == -1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan kurs", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}
				int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
				Kurs k = um.getKursManager().nadjiKursPoId(id);
				try {
					StatistikaRezultata sr = new StatistikaRezultata(tm, k);
					sr.setVisible(true);
				}catch(IllegalArgumentException i) {
					JOptionPane.showMessageDialog(null, "Nije moguće napraviti grafikon jer nema dostupnih rezultata.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
			}
		});
		
		prikaziPnl.add(prikaziBtn);
		prikaziPnl.add(statistikaBtn);
		add(prikaziPnl, BorderLayout.PAGE_END);
		JToolBar tb = new JToolBar();
		JLabel lblPretraga = new JLabel("  Pretraga:  ");
		tb.add(lblPretraga);
		tb.add(tfTrazi);
		
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
		tb.setFloatable(false);
		
		btnPnl.add(tb);
		btnPnl.add(Box.createHorizontalGlue());
		btnPnl.add(btnLogout);
		
		JScrollPane scr = new JScrollPane(tbl);
		getContentPane().add(scr, BorderLayout.CENTER);
		
		this.setSize(700,500);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void handleClosing() {
		Object[] options = {"Da", "Ne"};
		int answer = JOptionPane.showOptionDialog(null, "Da li zelite da zatvorite aplikaciju?", "Da li ste sigurni?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		switch(answer) {
		case JOptionPane.YES_OPTION:
			System.exit(0);
		case JOptionPane.NO_OPTION:
			break;
		}
	}
	
	private void promeniLozinkuDlg() {
		JDialog dlg = new JDialog();
		dlg.setTitle("Izmena lozinke");
		dlg.setResizable(false);
		dlg.setModal(true);
		dlg.setLayout(new MigLayout("wrap 3", "[]10[]10[]", "[]10[]10[]10[]"));
		JLabel lblStara = new JLabel("Stara lozinka");
		JPasswordField tfStara = new JPasswordField(20);
		dlg.add(lblStara);
		dlg.add(tfStara, "span 2");
		JLabel lblNova = new JLabel("Nova lozinka");
		JPasswordField tfNova = new JPasswordField(20);
		dlg.add(lblNova);
		dlg.add(tfNova, "span 2");
		JLabel lblPonovo = new JLabel("Ponovljena nova lozinka");
		JPasswordField tfPonovo = new JPasswordField(20);
		dlg.add(lblPonovo);
		dlg.add(tfPonovo, "span 2");
		
		JButton btnSacuvaj = new JButton("Sačuvaj");
		JButton btnOdustani = new JButton("Odustani");
		
		btnSacuvaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String stara = new String(tfStara.getPassword());
				String nova = new String(tfNova.getPassword());
				String ponovo = new String(tfNova.getPassword());
				if(stara.equals("")||nova.equals("")||ponovo.equals("")) {
					JOptionPane.showMessageDialog(null, "Niste uneli sve podatke!", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}if(!(nova.equals(ponovo))) {
					JOptionPane.showMessageDialog(null, "Nova i ponovljena nova lozinka se ne poklapaju.", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}
				boolean bool = um.getPredavacManager().promeniLozinku(predavac, stara, nova, ponovo);
				if(!bool) {
					JOptionPane.showMessageDialog(null, "Pogrešna stara lozinka!", "Greška!", JOptionPane.WARNING_MESSAGE);
				}dlg.dispose();
			}
		});
		btnOdustani.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] options = {"Da", "Ne"};
				int answer = JOptionPane.showOptionDialog(null, "Svaka izmena neće biti sačuvana.\nDa li želite da nastavite?", "Da li ste sigurni?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				switch(answer) {
				case JOptionPane.YES_OPTION:
					dlg.dispose();
				case JOptionPane.NO_OPTION:
					break;
				}
			}
		});
		
		dlg.add(btnSacuvaj, "cell 1 3");
		dlg.add(btnOdustani, "cell 2 3");
		dlg.pack();
		dlg.setLocationRelativeTo(null);
		dlg.setVisible(true);
		
	}
	
	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer, Integer>() {{put(0,1);put(1,1);put(2,1);put(3,1);put(4,1);}};
	
	protected void sort(int index) {
		this.predavac.getKursevi().sort(new Comparator<Kurs>() {
			int ret = 0;
			@Override
			public int compare(Kurs o1, Kurs o2) {
				switch(index) {
				case 0:
					ret = ((Integer)o1.getId()).compareTo((Integer)o2.getId());
					break;
				case 1:
					ret = o1.getNaziv().compareTo(o2.getNaziv());
					break;
				case 2:
					ret = o1.getJezik().getJezik().compareTo(o2.getJezik().getJezik());
					break;
				case 3:
					ret = ((Float)um.getKursManager().getCenovnikManager().getCenovnik().getCene().get(o1)).compareTo((Float)um.getKursManager().getCenovnikManager().getCenovnik().getCene().get(o2));
					break;
				case 4:
					ret = ((Float)um.getKursManager().getCenovnikManager().getCenovnik().getCeneTesta().get(o1)).compareTo((Float)um.getKursManager().getCenovnikManager().getCenovnik().getCeneTesta().get(o2));
				default:
					break;	
				}return ret*sortOrder.get(index);
			}
		});
		sortOrder.put(index, sortOrder.get(index)*-1);
		refresh();
	}
	
	public void refresh() {
		KursModel km = (KursModel) this.tbl.getModel();
		km.fireTableDataChanged();
	}
	
}
