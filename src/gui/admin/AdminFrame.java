package gui.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.Administrator;
import entity.Cenovnik;
import entity.Jezik;
import entity.Kurs;
import gui.izvestaj.DatumDlg;
import gui.izvestaj.PopularnostJezika;
import gui.izvestaj.PrihodiChartFrame;
import gui.jezik.JezikListBoxModel;
import gui.jezik.JezikListRenderer;
import gui.kurs.KursAddEdit;
import gui.kurs.KursModel;
import gui.main.LoginFrame;
import gui.predavac.PredavaciTblFrame;
import gui.sekretar.SekretarTblFrame;
import gui.ucenik.UceniciTblFrame;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class AdminFrame extends JFrame{
	private UserManager um;
	private JFrame loginFrame;
	private ImageIcon img;
	private JTable tbl;
	JTextField tfTrazi = new JTextField(20);
	private TableRowSorter<AbstractTableModel> tblSort = new TableRowSorter<AbstractTableModel>();


	private static final long serialVersionUID = 3846408261359081571L;
	
	public AdminFrame(Administrator a, UserManager um, JFrame loginFrame) {
		this.um = um;
		this.loginFrame = loginFrame;
		
		this.setSize(700, 500);
		setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				handleClosing();
			}
		});
		
		this.setTitle("Škola stranih jezika Lingua");
		img = new ImageIcon("./img/languages.png");
		this.setIconImage(img.getImage());
				
		JMenuBar mainMenu= new JMenuBar();
		
		JMenu studentMenu = new JMenu("Učenici");
		JMenuItem studentShowMI = new JMenuItem("Svi učenici");
		studentMenu.add(studentShowMI);
		
		studentShowMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UceniciTblFrame tbl = new UceniciTblFrame(um);
				tbl.setVisible(true);
			}
			
		});
		mainMenu.add(studentMenu);
		
		JMenu employeeMenu = new JMenu("Zaposleni");
		JMenuItem employeeSMI = new JMenuItem("Sekretari");
		JMenuItem employeePMI = new JMenuItem("Predavači");
		employeeMenu.add(employeeSMI);
		employeeMenu.add(employeePMI);
		mainMenu.add(employeeMenu);
		
		employeePMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PredavaciTblFrame tblP = new PredavaciTblFrame(um);
				tblP.setVisible(true);
			}
		});
		
		employeeSMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SekretarTblFrame tblS = new SekretarTblFrame(um);
				tblS.setVisible(true);
			}
		});
		
		JMenu jezikMenu = new JMenu("Jezici");
		JMenuItem jezikMI = new JMenuItem("Svi jezici");
		jezikMenu.add(jezikMI);
		mainMenu.add(jezikMenu);
		jezikMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AdminFrame.this.showJezikDlg();
			}
		});
		
		JMenu prMenu = new JMenu("Prihodi/rashodi");
		JMenuItem pMI = new JMenuItem("Prikaži prihode i rashode");
		JMenuItem rMI = new JMenuItem("Grafički prikaz");
			
		pMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DatumDlg(um, false);
			}
		});
		
		rMI.addActionListener(new ActionListener(){
    
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new PrihodiChartFrame(um);
				}catch(IllegalArgumentException i) {
					JOptionPane.showMessageDialog(null, "Nije moguće napraviti grafik jer nema dostupnih prihoda i rashoda.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
			}
		});
		
		prMenu.add(pMI);
		prMenu.add(rMI);
		mainMenu.add(prMenu);
		
		JButton btnLogout = new JButton("Odjavi se");
		
		btnLogout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] options = {"Da", "Ne"};
				int answer = JOptionPane.showOptionDialog(null, "Odjavite se sa profila?", "Odjava", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				switch(answer) {
				case JOptionPane.YES_OPTION:
					AdminFrame.this.dispose();
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
		
		tbl = new JTable();
		tbl.setModel(new KursModel(um.getKursManager().getKursevi(), um.getKursManager(), AdminFrame.this));
		tbl.getTableHeader().setOpaque(false);
		tbl.getTableHeader().setBackground(new Color(204, 166, 166));
		tbl.getTableHeader().setReorderingAllowed(false);
		tbl.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbl.setRowSorter(tblSort);
		
		tblSort.setModel((AbstractTableModel) tbl.getModel());
		
		tbl.getModel().addTableModelListener(new TableModelListener(){
		    
			@Override
			public void tableChanged(TableModelEvent e) {
				um.getKursManager().getCenovnikManager().saveData(); 
			}
		});
		
		tbl.getTableHeader().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int index = tbl.getTableHeader().columnAtPoint(arg0.getPoint());
				
				sort(index);
			}
			
		});
		
		JToolBar toolbar = new JToolBar();
		JButton btnDodaj = new JButton();
		
		btnDodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				KursAddEdit add = new KursAddEdit(um, AdminFrame.this, null);
				add.setVisible(true);
				refresh();
			}
		});
		
		JButton btnIzmeni = new JButton();
		
		btnIzmeni.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row == -1) {
					JOptionPane.showMessageDialog(null, "Niste odabrali nijedan kurs!", "Greška", JOptionPane.WARNING_MESSAGE);
				}else {
					int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
					Kurs k = um.getKursManager().nadjiKursPoId(id);
					if(k!=null) {
						KursAddEdit dlg = new KursAddEdit(um, AdminFrame.this, k);
						dlg.setVisible(true);
					}
				}refresh();
			}
		});
		
		JButton btnIzbrisi = new JButton();
		
		btnIzbrisi.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tbl.getSelectedRow();
				if(row==-1) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan kurs.", "Greška", JOptionPane.WARNING_MESSAGE);
				}else {
					int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
					boolean bool = um.getKursManager().tryDelKurs(id, um);
					if(!bool) {
						JOptionPane.showMessageDialog(null, "Ovaj kurs nisu još položili svi upisani učenici.\nNije moguće obrisati ga.", "Greška.", JOptionPane.WARNING_MESSAGE);
					}else {
						Kurs k = um.getKursManager().nadjiKursPoId(id);
						Object[] options = {"Da", "Ne"};
						int answer = JOptionPane.showOptionDialog(null, "Da li želite da obrišete kurs " + k.getNaziv() + "?", "Da li ste sigurni?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
						switch(answer) {
						case JOptionPane.YES_OPTION:
							um.getKursManager().delKurs(k);
							um.getPredavacManager().delKurs(k);
							refresh();
							break;
						case JOptionPane.NO_OPTION:
							break;
						}
					}
				}
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
		toolbar.add(new JLabel("  Pretraga:  "));
		
		tfTrazi.setMaximumSize(tfTrazi.getPreferredSize());
		toolbar.add(tfTrazi);
		
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
		
		btnPnl.add(toolbar);
		btnPnl.add(Box.createHorizontalGlue());
		btnPnl.add(btnLogout);
	
		JScrollPane scr = new JScrollPane(tbl);
		scr.setPreferredSize(new Dimension(685,300));
		
		JPanel mainPnl = new JPanel();
		mainPnl.add(scr,BorderLayout.CENTER);
		
		JPanel pnlDatum = new JPanel();
		pnlDatum.setLayout(new MigLayout("wrap 6","[]30[][]20[][]30[]", "[]"));
		
		pnlDatum.add(new JLabel("Važenje cenovnika"));
		pnlDatum.add(new JLabel("Od:"));
		
		JTextField tfOd = new JTextField(15);
		tfOd.setText(um.getKursManager().getCenovnikManager().getCenovnik().getVaziOd().toString());
		
		pnlDatum.add(tfOd);
		
		pnlDatum.add(new JLabel("Do:"));
		
		JTextField tfDo = new JTextField(15);
		
		tfDo.setText(um.getKursManager().getCenovnikManager().getCenovnik().getVaziDo().toString());
		pnlDatum.add(tfDo);
		
		JButton btnS = new JButton("Izmeni");
		pnlDatum.add(btnS);
		
		btnS.addActionListener(new ActionListener(){
    
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					LocalDate od = LocalDate.parse(tfOd.getText());
					LocalDate doo = LocalDate.parse(tfDo.getText());
					
					Cenovnik c = um.getKursManager().getCenovnikManager().getCenovnik();
					c.setVaziOd(od);
					c.setVaziDo(doo);
					um.getKursManager().getCenovnikManager().saveData();
				}catch(DateTimeException d) {
					JOptionPane.showMessageDialog(null, "Unesite datum u obliku gggg-mm-dd!", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		});
		
		mainPnl.add(pnlDatum, BorderLayout.PAGE_END);
		
		mainPnl.setPreferredSize(new Dimension(700,500));
		
		getContentPane().add(mainPnl, BorderLayout.CENTER);
		
		JPanel izvestajPnl = new JPanel();
		JButton btnZahtev= new JButton("Statistika zahteva");
		
		btnZahtev.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				new DatumDlg(AdminFrame.this.um, true);
			}
    
		});
		
		JButton btnPopularnost = new JButton("Popularnost jezika");
		izvestajPnl.add(btnZahtev);
		izvestajPnl.add(btnPopularnost);
		
		btnPopularnost.addActionListener(new ActionListener(){
    
			@Override
			public void actionPerformed(ActionEvent e) {
				new PopularnostJezika(AdminFrame.this.um);
			}
		});
		
		add(izvestajPnl, BorderLayout.PAGE_END);
		
		setJMenuBar(mainMenu);
		this.setVisible(true);
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
	
	private void showJezikDlg() {
		JDialog dlg = new JDialog();
		dlg.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		dlg.setTitle("Jezici");
		dlg.setIconImage(img.getImage());
		@SuppressWarnings("unchecked")
		JList<Jezik> list = new JList<Jezik>(new JezikListBoxModel(um.getJezikManager().getJezici()));
		list.setCellRenderer(new JezikListRenderer());
		list.setVisibleRowCount(5);
		JScrollPane scr = new JScrollPane(list);
		scr.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		list.setFixedCellHeight(25);
		list.setFixedCellWidth(200);
		list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dlg.getContentPane().add(scr, BorderLayout.CENTER);
		
		JPanel pnlButton = new JPanel(new FlowLayout());
		JButton btnDodaj = new JButton("Dodaj");
		
		btnDodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addJezikDlg(dlg);
				list.updateUI();
			}
		});
		
		JButton btnIzbrisi = new JButton("Izbriši");
		
		btnIzbrisi.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jezik j = list.getSelectedValue();
				if(j==null) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan kurs.", "Greška", JOptionPane.WARNING_MESSAGE);
				}else {
					boolean bool = um.getJezikManager().tryDelJezik(j, um.getKursManager());
					if(!bool) {
						JOptionPane.showMessageDialog(null, "Ovaj jezik ima aktivne kurseve.\nNije moguće brisanje", "Greška", JOptionPane.WARNING_MESSAGE);
					}else {
						Object[] options = {"Da", "Ne"};
						int answer = JOptionPane.showOptionDialog(null, "Da li želite da obrišete " + j.getJezik() + "?", "Da li ste sigurni?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
						switch(answer) {
						case JOptionPane.YES_OPTION:
							um.getJezikManager().delJezik(j, um.getPredavacManager());
							list.updateUI();
							break;
						case JOptionPane.NO_OPTION:
							break;
						}
						
					}
				}
			}
		});
		
		pnlButton.add(btnDodaj);
		pnlButton.add(btnIzbrisi);
		dlg.getContentPane().add(pnlButton, BorderLayout.PAGE_END);
		dlg.pack();
		dlg.setLocationRelativeTo(null);
		dlg.setVisible(true);
	}
	
	
	public void addJezikDlg(JDialog parent) {
		JDialog dlg = new JDialog(parent, true);
		dlg.setTitle("Dodaj jezik");
		dlg.setResizable(false);
		dlg.getContentPane().setLayout(new MigLayout("wrap 3", "[]10[][]", "[]10[]"));
		JLabel lblIme = new JLabel("Ime: ");
		JTextField tfIme = new JTextField(20);
		JButton btnDodaj = new JButton("Dodaj");
		btnDodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String ime= tfIme.getText().trim();
				if(ime.equals("")) {
					JOptionPane.showMessageDialog(null, "Molimo Vas, unesite ime.", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}else if(!ime.endsWith("jezik")){
					ime = ime + " jezik";
				}
				um.getJezikManager().addJezik(ime);
				dlg.dispose();
			}
		});
		
		dlg.getContentPane().add(lblIme);
		dlg.getContentPane().add(tfIme, "span 2");
		dlg.getContentPane().add(btnDodaj, "cell 1 2");
		
		ImageIcon imgDodaj = new ImageIcon("./img/add.png");
		dlg.setIconImage(imgDodaj.getImage());
		dlg.pack();
		dlg.setLocationRelativeTo(null);
		dlg.setVisible(true);
	}
	
	protected void refresh() {
		KursModel kM = (KursModel) this.tbl.getModel();
		kM.fireTableDataChanged();
	}
	
	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer, Integer>(){{put(0, 1);put(1, 1);put(2, 1);put(3, 1);put(4, 1);}};
	
	protected void sort(int index) {
		this.um.getKursManager().getKursevi().sort(new Comparator<Kurs>() {
			int ret=0;
			@Override
			public int compare(Kurs o1, Kurs o2) {
				switch(index) {
				case 0:
					ret = o1.getNaziv().compareTo(o2.getNaziv());
					break;
				case 1:
					ret = o1.getJezik().getJezik().compareTo(o2.getJezik().getJezik());
					break;
				case 2:
					ret = ((Float)um.getKursManager().getCenovnikManager().getCenovnik().getCene().get(o1)).compareTo((Float)um.getKursManager().getCenovnikManager().getCenovnik().getCene().get(o2));
					break;
				case 3:
					ret = ((Float)um.getKursManager().getCenovnikManager().getCenovnik().getCeneTesta().get(o1)).compareTo((Float)um.getKursManager().getCenovnikManager().getCenovnik().getCeneTesta().get(o2));
				default:
					break;	
				}return ret*sortOrder.get(index);
			}
			
		});
		
		sortOrder.put(index, sortOrder.get(index)*-1);
		refresh();
		
	}
	

}
