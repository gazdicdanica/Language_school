package gui.ucenik;

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
import entity.Ucenik;
import gui.kurs.AddKursDlg;
import gui.kurs.KursListBoxModel;
import gui.kurs.KursListRenderer;
import gui.sekretar.SekretarFrame;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class UceniciAddEdit extends JDialog{

	private static final long serialVersionUID = -8713515541851536414L;
	
	private UserManager um;
	private Ucenik ucenik;
	private JFrame f;
	private JList<Kurs> list;
	private JTextField tfKIme = new JTextField(20);
	
	public UceniciAddEdit(UserManager um, JFrame f, Ucenik u) {
		super(f, true);
		this.um = um;
		this.f = f;
		if(u != null) {
			setTitle("Izmena");
			ImageIcon img = new ImageIcon("./img/edit.png");
			this.setIconImage(img.getImage());
		}else {
			setTitle("Dodavanje");
			ImageIcon img = new ImageIcon("./img/add.png");
			this.setIconImage(img.getImage());
		}
		this.ucenik = u;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		setLayout(new MigLayout("wrap 3", "[]10[][]", "[]10[]10[]10[]10[]10[]10[]10[]10[][]10[]"));
		
		if(u!=null) {
			JLabel lblKIme = new JLabel("Korisničko ime");
			this.add(lblKIme);
			add(tfKIme, "span 2");
		}
		
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
		
		List<Kurs> listaKurs = this.um.getKursManager().getKursevi();
		JLabel lblKursevi = new JLabel("Kursevi");
		add(lblKursevi);
		if(ucenik!=null) {
			listaKurs = ucenik.getKursevi();
			if(ucenik.getKorisnickoIme().equals("")) {
				tfKIme.setEditable(false);
			}
			tfKIme.setText(ucenik.getKorisnickoIme());
			tfIme.setText(ucenik.getIme());
			tfPrezime.setText(ucenik.getPrezime());
			switch(ucenik.getPol()) {
			case "M":
				btnM.setSelected(true);
				break;
			case "Ž":
				btnZ.setSelected(true);
				break;
			}
			tfDatum.setText(ucenik.getDatumRodjenja().toString());
			tfAdresa.setText(ucenik.getAdresa());
			tfTelefon.setText(ucenik.getBrTelefona());
		}
			
		JPanel pnl = new JPanel();
		list = new JList<Kurs>(new KursListBoxModel(listaKurs));
		list.setCellRenderer(new KursListRenderer());
		list.setVisibleRowCount(4);
		JScrollPane scr = new JScrollPane(list);
		list.setFixedCellHeight(20);
		list.setFixedCellWidth(150);
		list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pnl.add(scr);
		
		if(ucenik!=null) {		
			JToolBar tb = new JToolBar(null, JToolBar.VERTICAL);
			JButton btnDodaj = new JButton();
			ImageIcon imgDodaj = new ImageIcon("./img/add.png");
			btnDodaj.setIcon(new ImageIcon(imgDodaj.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
			tb.add(btnDodaj);
			
			btnDodaj.addActionListener(new ActionListener() {
					
				@Override
				public void actionPerformed(ActionEvent e) {
					JDialog addDlg = new AddKursDlg(um.getKursManager().getKursevi(), ucenik, um);
					um.saveData();
					list.updateUI();
					JOptionPane.showMessageDialog(null, "Zahtev za upis na kurs je uspešno poslat.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
				}
			});
				
			JButton btnIzbrisi = new JButton();
			ImageIcon imgIzbrisi = new ImageIcon("./img/remove.png");
			btnIzbrisi.setIcon(new ImageIcon(imgIzbrisi.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
			
			btnIzbrisi.addActionListener(new ActionListener() {
					
				@Override
				public void actionPerformed(ActionEvent e) {
					Kurs k = list.getSelectedValue();
					if (k!=null) {
						Object[] options = {"Da", "Ne"};
						int answer = JOptionPane.showOptionDialog(null, "Da li želite da ispišete učenika sa kursa?", "Da li ste sigurni?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
						switch(answer) {
						case JOptionPane.YES_OPTION:
							ucenik.getKursevi().remove(k);
							um.getZahtevManager().removeZahtev(ucenik, k);
							um.saveData();
							list.updateUI();
							break;
						case JOptionPane.NO_OPTION:
							break;
						}
						list.updateUI();
					}else {
						JOptionPane.showMessageDialog(null, "Niste izabrali nijedan kurs.", "Greška!", JOptionPane.WARNING_MESSAGE);
					}					
				}
			});
				
			tb.add(btnIzbrisi);
			tb.setFloatable(false);
				
			pnl.add(tb);
		}
		getContentPane().add(pnl, "span 3");
			

			
		
		JButton btnSacuvaj = new JButton("Sačuvaj");
		
		btnSacuvaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String kIme = tfKIme.getText().trim();
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
					Kurs k = list.getSelectedValue();
					if(ime.equals("")||prezime.equals("")||adresa.equals("")||telefon.equals("")||(!btnZ.isSelected()&&!btnM.isSelected())) {
						JOptionPane.showMessageDialog(null, "Niste uneli sve podatke!", "Greška!", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if(ucenik==null && k==null) {
						JOptionPane.showMessageDialog(null, "Niste uneli sve podatke!", "Greška!", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if(ucenik!=null) {
						UceniciAddEdit.this.um.getUcenikManager().editUcenik(ucenik.getId(), kIme, ime, prezime, pol, rodjendan, telefon, adresa);
					}else {
						int id;
						Random rand = new Random();
						boolean found = false;
						while(true) {
							id = rand.nextInt(100);
							for(Ucenik u:UceniciAddEdit.this.um.getUcenici()) {
								if(u.getId() == id) {
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
						UceniciAddEdit.this.um.getUcenikManager().addUcenik(id, ime, prezime, pol, rodjendan, telefon, adresa, k);
						JOptionPane.showMessageDialog(null, "Zahtev za upis na kurs je uspešno poslat!", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
					}
				}catch (DateTimeParseException ex){
					JOptionPane.showMessageDialog(null, "Unesite datum oblika gggg-mm-dd", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {
					((UceniciTblFrame) f).refresh();
				}catch(ClassCastException c) {
					((SekretarFrame) f).refresh();
				}
				
				UceniciAddEdit.this.dispose();
				
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
					UceniciAddEdit.this.dispose();
				case JOptionPane.NO_OPTION:
					break;
				}
			}
		});
		
		add(btnSacuvaj, "cell 1 9");
		add(btnOdustani, "cell 2 9");
		
		
		UceniciAddEdit.this.pack();
		setLocationRelativeTo(null);
	}

}
