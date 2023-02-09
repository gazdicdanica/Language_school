package gui.izvestaj;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import entity.Kurs;
import entity.Ucenik;
import managers.KursManager;
import net.miginfocom.swing.MigLayout;

public class UcenikIzvestaj extends JFrame{

	private Ucenik u;
	private KursManager km;
	
	private static final long serialVersionUID = 6835822210625022228L;
	
	public UcenikIzvestaj(Ucenik u, KursManager km) {
		this.u = u;
		this.km = km;
		
		setTitle(u.toString() + " - Izveštaj");
		ImageIcon img = new ImageIcon("./img/report.png");
		setIconImage(img.getImage());
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new MigLayout("wrap 2", "[]10[]", "[]10[]10[]10[]10[]10[]10[]"));
		
		add(new JLabel("Ime: "));
		add(new JLabel(u.getIme()));
		add(new JLabel("Prezime: "));
		add(new JLabel(u.getPrezime()));
		add(new JLabel("Pol: "));
		add(new JLabel(u.getPol()));
		add(new JLabel("Datum rođenja: "));
		add(new JLabel(u.getDatumRodjenja().toString()));
		add(new JLabel("Broj telefona: "));
		add(new JLabel(u.getBrTelefona()));
		add(new JLabel("Adresa: "));
		add(new JLabel(u.getAdresa()));
		add(new JLabel("Korisničko ime: "));
		add(new JLabel(u.getKorisnickoIme()));
		add(new JLabel("Broj kurseva: "));
		add(new JLabel(String.valueOf(u.getKursevi().size())));
		double cena = 0;
		for(Kurs k:u.getKursevi()) {
			cena+=km.getCenovnikManager().getCenovnik().getCene().get(k);
		}
		add(new JLabel("Cena kurseva ukupno: "));
		add(new JLabel(String.valueOf(cena) + " din."));
		
		pack();
		setLocationRelativeTo(null);
	}
}
