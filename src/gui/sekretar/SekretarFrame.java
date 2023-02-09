package gui.sekretar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.Sekretar;
import entity.Ucenik;
import entity.Zahtev;
import gui.kurs.AddKursDlg;
import gui.main.LoginFrame;
import gui.ucenik.UceniciAddEdit;
import gui.zahtev.ZahtevModel;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class SekretarFrame extends JFrame{

	private static final long serialVersionUID = 3794923494542514277L;

	private UserManager um;
	private JFrame loginFrame;
	private Sekretar s;
	private JTable tbl;
	private JButton btnIzmeni = new JButton();
	private JButton btnDodaj = new JButton();
	private JButton btnIzbrisi = new JButton();
	private JTextField tfTrazi = new JTextField(20);
	private TableRowSorter<AbstractTableModel> tblSort = new TableRowSorter<AbstractTableModel>();
	
	public SekretarFrame(Sekretar s, UserManager um, JFrame loginFrame) {
		this.um = um;
		this.loginFrame = loginFrame;
		this.s = s;
		
		this.setSize(700, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				handleClosing();
			}
		});
		
		this.setTitle("Škola stranih jezika Lingua");
		ImageIcon img = new ImageIcon("./img/languages.png");
		this.setIconImage(img.getImage());
		
		JMenuBar mainMenu = new JMenuBar();
		JMenu izmeniMenu = new JMenu("Lični podaci");
		
		JMenuItem sifraMI = new JMenuItem("Promeni lozinku");
		sifraMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SekretarFrame.this.izmeniLozinkuDlg();
			}
		});
		
		JMenu predavaciMenu = new JMenu("Kursevi");
		JMenuItem sviMI = new JMenuItem("Dodaj predavača na kurs");
		predavaciMenu.add(sviMI);
		mainMenu.add(predavaciMenu);
		
		sviMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DodajPredavacaNaKurs dlg = new DodajPredavacaNaKurs(SekretarFrame.this.um);
				dlg.setVisible(true);
			}
		});
		
		izmeniMenu.add(sifraMI);
		mainMenu.add(izmeniMenu);
		
		setJMenuBar(mainMenu);
		
		
		tbl = new JTable(new ZahtevModel(um.getZahtevManager()));
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
		
		JPanel pnlLogout = new JPanel();
		pnlLogout.setLayout(new BoxLayout(pnlLogout, BoxLayout.LINE_AXIS));
		JButton btnLogout = new JButton("Odjavi se");
		
		btnLogout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] options = {"Da", "Ne"};
				int answer = JOptionPane.showOptionDialog(null, "Odjavite se sa profila?", "Odjava", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				switch(answer) {
				case JOptionPane.YES_OPTION:
					SekretarFrame.this.dispose();
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
		
		JScrollPane scr = new JScrollPane(tbl);
		add(scr, BorderLayout.CENTER);
		
		JPanel btnPanel = new JPanel(new FlowLayout());
		
		JButton btnOdobri = new JButton("Odobri zahtev");
		
		btnOdobri.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row == -1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan zahtev.", "Greška!", JOptionPane.WARNING_MESSAGE);
				}else {
					int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
					um.getZahtevManager().odobriZahtev(id, s);
					refresh();
				}
			}
		});
		
		JButton btnOdbij = new JButton("Odbij zahtev");
		
		btnOdbij.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row==-1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan zahtev.", "Greška!", JOptionPane.WARNING_MESSAGE);
				}else {
					int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
					um.getZahtevManager().odbijZahtev(id, s);
					refresh();
				}
			}
		});
		
		JButton btnDodaj = new JButton();
		ImageIcon imgDodaj = new ImageIcon("./img/add.png");
		btnDodaj.setIcon(new ImageIcon(imgDodaj.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		
		btnDodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object [] options= {"OK", "Odustani"};
				Object answer = JOptionPane.showInputDialog(null, "Ako učenik ima svoj profil, unesite njegov jedinstveni id.\nU suprotnom ostavite polje za unos prazno.", "ID", JOptionPane.PLAIN_MESSAGE, null, null, null);
				int id;
				if(answer == null) {
					return;
				}else if(answer.toString().equals("")) {
					UceniciAddEdit add = new UceniciAddEdit(SekretarFrame.this.um, SekretarFrame.this, null);
					add.setVisible(true);
				}else {
					try {
						id = Integer.parseInt((String) answer);
						Ucenik u = um.getUcenikManager().nadjiUcenikaPoId(id);
						AddKursDlg add = new AddKursDlg(um.getKursManager().getKursevi(), u, um);
						refresh();
					}catch(NumberFormatException n) {
						JOptionPane.showMessageDialog(null, "Morate uneti brojnu vrednost!", "Greška!", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
			}
		});
		
		btnPanel.add(btnOdobri);
		btnPanel.add(btnOdbij);
		
		JToolBar tb = new JToolBar();
		tb.setFloatable(false);
		tb.add(btnDodaj);
		
		pnlLogout.add(tb);
		pnlLogout.add(Box.createHorizontalGlue());
		pnlLogout.add(btnLogout);
		
		add(pnlLogout, BorderLayout.PAGE_START);
		
		add(btnPanel, BorderLayout.PAGE_END);
		
		this.setVisible(true);
	}
	
	private void izmeniLozinkuDlg() {
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
				boolean bool = um.getSekretarManager().promeniLozinku(s, stara, nova, ponovo);
				if(!bool) {
					JOptionPane.showMessageDialog(null, "Pogrešna stara lozinka!", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}else {
					dlg.dispose();
				}
				
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
	
	public void refresh() {
		ZahtevModel zm = (ZahtevModel) this.tbl.getModel();
		zm.fireTableDataChanged();
	}
	
	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer, Integer>() {{put(0, 1);put(1, 1);put(2, 1);}};
	
	protected void sort(int i) {
		this.um.getZahtevManager().getZahtevi().sort(new Comparator<Zahtev>() {
			int ret = 0;
			@Override
			public int compare(Zahtev o1, Zahtev o2) {
				switch(i) {
				case 0:
					ret = ((Integer)o1.getId()).compareTo((Integer)o2.getId());
					break;
				case 1:
					ret = o1.getUcenik().toString().compareTo(o2.getUcenik().toString());
					break;
				case 2:
					ret = o1.getKurs().toString().compareTo(o2.getKurs().toString());
					break;
				default: break;
				}
				return ret*sortOrder.get(i);
			}
			
		});
		sortOrder.put(i, sortOrder.get(i)*-1);
		refresh();
	}
}
