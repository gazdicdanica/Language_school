package gui.kurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import entity.Jezik;
import entity.Kurs;
import gui.admin.AdminFrame;
import managers.KursManager;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class KursAddEdit extends JDialog{

	private static final long serialVersionUID = 1788591439624587535L;

	private KursManager km;
	private Kurs k;
	private JFrame f;
	private JComboBox<Jezik> cbJezici = new JComboBox<Jezik>();
	
	public KursAddEdit(UserManager um, JFrame f, Kurs k) {
		super(f, true);
		this.km = um.getKursManager();
		this.k = k;
		this.f = f;
		if(k != null) {
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
		
		setLayout(new MigLayout("wrap 3", "[]10[][]", "[]10[]10[]10[]10[]"));
		
		JLabel lblNaziv = new JLabel("Naziv");
		add(lblNaziv);
		JTextField tfNaziv = new JTextField(15);
		add(tfNaziv, "span 2");
		JLabel lblCena = new JLabel("Cena");
		add(lblCena);
		JTextField tfCena = new JTextField(15);
		add(tfCena, "span 2");
		JLabel lblCenaTesta = new JLabel("Cena testa");
		add(lblCenaTesta);
		JTextField tfCenaTesta = new JTextField(15);
		add(tfCenaTesta, "span 2");
		
		if(k!=null) {
			tfNaziv.setText(k.getNaziv());
			tfCena.setText(String.valueOf(km.getCenovnikManager().getCenovnik().getCene().get(k)));
			tfCenaTesta.setText(String.valueOf(km.getCenovnikManager().getCenovnik().getCeneTesta().get(k)));
		}else {
			JLabel lblJezik = new JLabel("Jezik");
			add(lblJezik);
			for(Jezik j:um.getJezikManager().getJezici()) {
				cbJezici.addItem(j);
			}
			add(cbJezici, "span 2");
		}
		
		JButton btnSacuvaj = new JButton("Sačuvaj");
		btnSacuvaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String naziv = tfNaziv.getText().trim();
					float cena = Float.parseFloat(tfCena.getText().trim());
					float cenaTesta = Float.parseFloat(tfCenaTesta.getText().trim());
					if(naziv.equals("")) {
						JOptionPane.showMessageDialog(null, "Niste uneli sve podatke!", "Greška!", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if(k!=null) {
						km.editKurs(k, naziv, cena, cenaTesta);
					}else {
						Jezik j = (Jezik) cbJezici.getSelectedItem();
						km.addKurs(naziv, cena, cenaTesta, j);
					}
					
				}catch(NumberFormatException nf) {
					JOptionPane.showMessageDialog(null, "Unesite podatke u adekvatnom obliku!", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}
				KursAddEdit.this.dispose();
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
					KursAddEdit.this.dispose();
				case JOptionPane.NO_OPTION:
					break;
				}
			}
		});
		
		add(btnSacuvaj, "cell 1 4");
		add(btnOdustani, "cell 2 4");
		
		pack();
		setLocationRelativeTo(null);
	}
}
