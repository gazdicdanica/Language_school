package gui.predavac;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import entity.Jezik;
import entity.Kurs;
import entity.Predavac;
import entity.Zaposleni.NivoSS;
import gui.jezik.AddJezikDlg;
import gui.jezik.JezikListBoxModel;
import gui.jezik.JezikListRenderer;
import gui.kurs.AddKursDlg;
import gui.kurs.KursListBoxModel;
import gui.kurs.KursListRenderer;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class PredavacAddEdit extends JDialog{

	private static final long serialVersionUID = -243265219020037303L;
	
	private UserManager um;
	private Predavac predavac;
	private JFrame f;
	private JLabel lblBonus = new JLabel("Bonus");
	private JTextField tfBonus = new JTextField(20);
	
	public PredavacAddEdit(UserManager um, JFrame f, Predavac p) {
		super(f, true);
		this.um = um;
		this.predavac = p;
		this.f = f;
		
		if(p != null) {
			setTitle("Izmena");
			ImageIcon img = new ImageIcon("./img/edit.png");
			this.setIconImage(img.getImage());
		}else {
			setTitle("Dodavanje");
			ImageIcon img = new ImageIcon("./img/add.png");
			this.setIconImage(img.getImage());
		}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		setLayout(new MigLayout("wrap 3", "[]10[][]", "[]10[]10[]10[]10[]10[]10[]10[]10[]10[]10[]10[]10[]"));
		
		JLabel lblKIme = new JLabel("Korisničko ime");
		this.add(lblKIme);
		JTextField tfKIme = new JTextField(20);
		add(tfKIme, "span 2");
		JLabel lblIme = new JLabel("Ime");
		add(lblIme);
		JTextField tfIme = new JTextField(20);
		add(tfIme, "span 2");
		JLabel lblPrezime = new JLabel("Prezime");
		add(lblPrezime);
		JTextField tfPrezime = new JTextField(20);
		add(tfPrezime, "span 2");
		JLabel lblPol = new JLabel("Pol");
		add(lblPol);
		ButtonGroup bgPol = new ButtonGroup();
		JRadioButton btnM = new JRadioButton("M");
		JRadioButton btnZ = new JRadioButton("Ž");
		bgPol.add(btnZ);
		bgPol.add(btnM);
		add(btnM);
		add(btnZ);
		JLabel lblDatum = new JLabel("Datum rodjenja");
		add(lblDatum);
		JTextField tfDatum = new JTextField(20);
		add(tfDatum, "span 2");
		JLabel lblTelefon = new JLabel("Broj telefona");
		add(lblTelefon);
		JTextField tfTelefon = new JTextField(20);
		add(tfTelefon, "span 2");
		JLabel lblAdresa = new JLabel("Adresa");
		add(lblAdresa);
		JTextField tfAdresa = new JTextField(20);
		add(tfAdresa, "span 2");
		JLabel lblNivoSS = new JLabel("Nivo stručne spreme");
		add(lblNivoSS);
		JComboBox<NivoSS> cbNivoSS = new JComboBox<>(NivoSS.values());
		add(cbNivoSS, "span 2");
		JLabel lblStaz = new JLabel("Godine staža");
		add(lblStaz);
		JTextField tfStaz = new JTextField(20);
		add(tfStaz, "span 2");
		JLabel lblPlata = new JLabel("Plata");
		add(lblPlata);
		JTextField tfPlata = new JTextField(20);
		add(tfPlata, "span 2");
		tfPlata.setEditable(false);
		//bonus
		List<Jezik> listaJezika = this.um.getJezikManager().getJezici();
		if(this.predavac != null) {
			listaJezika = predavac.getJezici();
			add(lblBonus);
			add(tfBonus, "span 2");
		}
		JLabel lblJezici = new JLabel("Jezici");
		add(lblJezici);
		@SuppressWarnings("unchecked")
		JList<Jezik> listJezici = new JList<Jezik>(new JezikListBoxModel(listaJezika));
		listJezici.setCellRenderer(new JezikListRenderer());
		listJezici.setVisibleRowCount(4);
		JScrollPane scrJezik = new JScrollPane(listJezici);
		listJezici.setFixedCellHeight(20);
		listJezici.setFixedCellWidth(150);
		JPanel pnlJezici = new JPanel();
		pnlJezici.add(scrJezik);
		JToolBar tb = new JToolBar(null, JToolBar.VERTICAL);
		JButton btnDodaj = new JButton();
		ImageIcon imgDodaj = new ImageIcon("./img/add.png");
		btnDodaj.setIcon(new ImageIcon(imgDodaj.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		tb.add(btnDodaj);
		
		btnDodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog addJezik = new AddJezikDlg(um.getJezikManager().getJezici(), p, um);
				um.saveData();
				listJezici.updateUI();
			}
		});
		
		JButton btnIzbrisi = new JButton();
		ImageIcon imgIzbrisi = new ImageIcon("./img/remove.png");
		btnIzbrisi.setIcon(new ImageIcon(imgIzbrisi.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		
		btnIzbrisi.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jezik j = listJezici.getSelectedValue();
				if(j!=null) {
					Object[] options = {"Da", "Ne"};
					int answer = JOptionPane.showOptionDialog(null, "Da li želite da obrišete izabrani jezik?", "Da li ste sigurni?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
					switch(answer){
						case JOptionPane.YES_OPTION:
							predavac.getJezici().remove(j);
							um.saveData();
							listJezici.updateUI();
							break;
						case JOptionPane.NO_OPTION:
							break;
					}listJezici.updateUI();
				}else {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan jezik.", "Greška!", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		tb.add(btnIzbrisi);
		tb.setFloatable(false);
		add(pnlJezici, "span 2");
		if(predavac!=null) {
			pnlJezici.add(tb);
			listJezici.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			JLabel lblKursevi = new JLabel("Kursevi");
			add(lblKursevi);
			
			JPanel pnlKursevi = new JPanel();
			JList<Kurs> listKurs = new JList<Kurs>(new KursListBoxModel(predavac.getKursevi()));
			listKurs.setCellRenderer(new KursListRenderer());
			listKurs.setVisibleRowCount(4);
			JScrollPane scr1 = new JScrollPane(listKurs);
			listKurs.setFixedCellHeight(20);
			listKurs.setFixedCellWidth(150);
			listKurs.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			pnlKursevi.add(scr1);
			
			JToolBar tb1 = new JToolBar(null, JToolBar.VERTICAL);
			JButton btnDodaj1 = new JButton();
			btnDodaj1.setIcon(new ImageIcon(imgDodaj.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
			tb1.add(btnDodaj1);
			
			btnDodaj1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					List<Kurs> kk = um.getPredavacManager().getMoguciKursevi(predavac);
					if(kk.size()==0) {
						JOptionPane.showMessageDialog(null, "Trenutno ne postoje kursevi koje predavač može da predaje.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					AddKursDlg dlg = new AddKursDlg(kk, predavac, PredavacAddEdit.this.um);
					PredavacAddEdit.this.um.saveData();
					listKurs.updateUI();
				}
			});
			
			JButton btnIzbrisi1 = new JButton();
			btnIzbrisi1.setIcon(new ImageIcon(imgIzbrisi.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
			
			btnIzbrisi1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Kurs k = listKurs.getSelectedValue();
					if (k!=null) {
						Object[] options = {"Da", "Ne"};
						int answer = JOptionPane.showOptionDialog(null, "Da li želite da uklonite izabrani kurs?", "Da li ste sigurni?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
						switch(answer) {
						case JOptionPane.YES_OPTION:
							predavac.getKursevi().remove(k);
							PredavacAddEdit.this.um.saveData();
							listKurs.updateUI();
							break;
						case JOptionPane.NO_OPTION:
							break;
						}
					}else
						JOptionPane.showMessageDialog(null, "Niste izabrali nijedan kurs.", "Greška!", JOptionPane.WARNING_MESSAGE);
				}
			});
			
			tb1.add(btnIzbrisi1);
			tb1.setFloatable(false);
			
			pnlKursevi.add(scr1);
			pnlKursevi.add(tb1);
			add(pnlKursevi, "span 2");
			
			tfKIme.setText(predavac.getKorisnickoIme());
			tfIme.setText(predavac.getIme());
			tfPrezime.setText(predavac.getPrezime());
			switch(predavac.getPol()) {
				case "M":
					btnM.setSelected(true);
					break;
				case "Ž":
					btnZ.setSelected(true);
					break;
			}
			tfDatum.setText(predavac.getDatumRodjenja().toString());
			tfAdresa.setText(predavac.getAdresa());
			tfTelefon.setText(predavac.getBrTelefona());
			tfStaz.setText(String.valueOf(predavac.getStaz()));
			tfPlata.setText(String.valueOf(predavac.getPlata()));
			tfBonus.setText(String.valueOf(predavac.getBonus()));
			cbNivoSS.setSelectedItem(predavac.getStrucnaS());
			
			
		}else {
			listJezici.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		}
		
		
		JButton btnSacuvaj = new JButton("Sačuvaj");
		btnSacuvaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String kIme = tfKIme.getText().trim();
					String lozinka = PredavacAddEdit.this.um.sifra();
					String ime = tfIme.getText().trim();
					String prezime = tfPrezime.getText().trim();
					String pol;
					if(btnZ.isSelected())
						pol = "Ž";
					else
						pol = "M";
					String datum = tfDatum.getText().trim();
					LocalDate rodjendan = LocalDate.parse(datum);
					String adresa = tfAdresa.getText().trim();
					String telefon = tfTelefon.getText().trim();
					NivoSS nivoS = (NivoSS) cbNivoSS.getSelectedItem();
					int staz = Integer.parseInt(tfStaz.getText().trim());
					List<Jezik> j = listJezici.getSelectedValuesList();
					if(kIme.equals("")||ime.equals("")||prezime.equals("")||adresa.equals("")||telefon.equals("")||(!btnZ.isSelected()&&!btnM.isSelected())) {
						JOptionPane.showMessageDialog(null, "Niste uneli sve podatke!", "Greška!", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if(predavac== null && j.size()==0) {
						JOptionPane.showMessageDialog(null, "Niste uneli sve podatke!", "Greška!", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if(predavac==null) {
						int id;
						Random rand = new Random();
						boolean found = false;
						while(true) {
							id = rand.nextInt(100);
							for(Predavac p:um.getPredavaci()) {
								if(p.getId() == id) {
									found = true;
									break;
								}
							}if(found) {
								found = false;
								continue;
							}else {
								break;
							}
						}
						PredavacAddEdit.this.um.getPredavacManager().addPredavac(id, kIme, lozinka, ime, prezime, pol, rodjendan, telefon, adresa, nivoS, staz, j);
						
					}else {
						int id = predavac.getId();
						double bonus =Double.parseDouble(tfBonus.getText().trim());
						PredavacAddEdit.this.um.getPredavacManager().editPredavac(id, kIme, ime, prezime, pol, rodjendan, telefon, adresa, nivoS, staz, bonus);
					}
					
				}catch(DateTimeParseException i) {
					JOptionPane.showMessageDialog(null, "Unesite datum u obliku gggg-mm-dd!", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}catch(NumberFormatException nf) {
					JOptionPane.showMessageDialog(null, "Niste uneli podatke u validnom formatu!", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}
				((PredavaciTblFrame) f).refresh();
				PredavacAddEdit.this.dispose();
			}
		});
		
		JButton btnOdustani = new JButton("Odustani");
		btnOdustani.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] options = {"Da", "Ne"};
				int answer = JOptionPane.showOptionDialog(null, "Svaka izmena podataka neće biti sačuvana.\n Da li želite da nastavite?", "Da li ste sigurni?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				switch(answer) {
				case JOptionPane.YES_OPTION:
					PredavacAddEdit.this.dispose();
				case JOptionPane.NO_OPTION:
					break;
				}
			}
		});
		
		add(btnSacuvaj, "cell 1 13");
		add(btnOdustani, "cell 2 13");
		pack();
		setLocationRelativeTo(null);
	}
}
