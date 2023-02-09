package gui.sekretar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import entity.Jezik;
import entity.Sekretar;
import entity.Zaposleni.NivoSS;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class SekretarAddEdit extends JDialog{

	private static final long serialVersionUID = 606742552362976033L;

	private UserManager um;
	private Sekretar sekretar;
	private JFrame f;
	private JLabel lblBonus = new JLabel("Bonus");
	private JTextField tfBonus = new JTextField(20);
	
	public SekretarAddEdit(UserManager um, JFrame f, Sekretar s) {
		super(f, true);
		this.um = um;
		this.sekretar = s;
		this.f = f;
		
		if(s!=null) {
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
		
		setLayout(new MigLayout("wrap 3", "[]10[][]", "[]10[]10[]10[]10[]10[]10[]10[]10[]10[]10[]"));
		
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
		
		if(this.sekretar!=null) {
			add(lblBonus);
			add(tfBonus, "span 2");
			
			tfKIme.setText(sekretar.getKorisnickoIme());
			tfIme.setText(sekretar.getIme());
			tfPrezime.setText(sekretar.getPrezime());
			switch(sekretar.getPol()) {
				case "M":
					btnM.setSelected(true);
					break;
				case "Ž":
					btnZ.setSelected(true);
					break;
			}
			tfDatum.setText(sekretar.getDatumRodjenja().toString());
			tfAdresa.setText(sekretar.getAdresa());
			tfTelefon.setText(sekretar.getBrTelefona());
			tfStaz.setText(String.valueOf(sekretar.getStaz()));
			tfPlata.setText(String.valueOf(sekretar.getPlata()));
			tfBonus.setText(String.valueOf(sekretar.getBonus()));
			cbNivoSS.setSelectedItem(sekretar.getStrucnaS());
			
			if(this.f.getClass().getSimpleName().equals("SekretarFrame")) {
				cbNivoSS.setEditable(false);
				tfStaz.setEditable(false);
				tfPlata.setEditable(false);
				tfBonus.setEditable(false);
			}
		}
		
		JButton btnSacuvaj = new JButton("Sačuvaj");
		btnSacuvaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String kIme = tfKIme.getText().trim();
					String lozinka = SekretarAddEdit.this.um.sifra();
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
					
					if(kIme.equals("")||ime.equals("")||prezime.equals("")||adresa.equals("")||telefon.equals("")||(!btnZ.isSelected()&&!btnM.isSelected())) {
						JOptionPane.showMessageDialog(null, "Niste uneli sve podatke!", "Greška!", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if(sekretar == null) {
						Random rand = new Random();
						boolean found = false;
						int id;
						while(true) {
							id = rand.nextInt(100);
							for(Sekretar s:SekretarAddEdit.this.um.getSekretari()) {
								if(s.getId() == id) {
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
						
						SekretarAddEdit.this.um.getSekretarManager().addSekretar(id, kIme, lozinka, ime, prezime, pol, rodjendan, telefon, adresa, nivoS, staz);
					}else {
						int id = sekretar.getId();
						double bonus = Double.parseDouble(tfBonus.getText().trim());
						SekretarAddEdit.this.um.getSekretarManager().editSekretar(id, kIme, ime, prezime, pol, rodjendan, telefon, adresa, nivoS, staz, bonus);
					}
					
				}catch(DateTimeParseException i) {
					JOptionPane.showMessageDialog(null, "Unesite datum u obliku gggg-mm-dd !", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}catch(NumberFormatException nf) {
					JOptionPane.showMessageDialog(null, "Niste uneli podatke u validnom formatu!", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}
				((SekretarTblFrame) f).refresh();
				SekretarAddEdit.this.dispose();
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
					SekretarAddEdit.this.dispose();
				case JOptionPane.NO_OPTION:
					break;
				}
			}
		});
		
		add(btnSacuvaj, "cell 1 11");
		add(btnOdustani, "cell 2 11");
		pack();
		setLocationRelativeTo(null);
	}
}
