package managers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Sekretar;
import entity.Zahtev;
import entity.Zaposleni;
import entity.Zaposleni.NivoSS;

public class SekretarManager {
	private UserManager um;
	
	public SekretarManager(UserManager um) {
		this.um = um;
	}
	
	public Sekretar parseSekretar(String[] tokens) {
		int id = Integer.parseInt(tokens[0].trim());
		String ime = tokens[2].trim();
		String prezime = tokens[3].trim();
		String pol = tokens[4].trim();
		String datum = tokens[5].trim();
		LocalDate rodjendan = LocalDate.parse(datum);
		String telefon = tokens[6].trim();
		String adresa = tokens[7].trim();
		String username = tokens[8].trim();
		String password = tokens[9].trim();
		NivoSS ss = Zaposleni.NivoSS.fromString(tokens[10].trim());
		int staz = Integer.parseInt(tokens[11].trim());
		double bonus = Double.parseDouble(tokens[12].trim());
		LocalDate datumZaposlenja = LocalDate.parse(tokens[13].trim());
		Sekretar ret =  new Sekretar(id, ime, prezime, pol, rodjendan, telefon, adresa, username, password, ss, staz, bonus, datumZaposlenja);
		return ret;
		
	}
	
	public Sekretar nadjiSekretaraPoId(int id) {
		List<Sekretar> s = um.getSekretari();
		Sekretar ret = null;
		for(int i=0;i<s.size();i++) {
			Sekretar sek = s.get(i);
			if(sek.getId() == id) {
				ret = sek;
				break;
			}
		}return ret;
	}
	
	public void addSekretar(int id, String korisnickoIme, String lozinka, String ime, String prezime, String pol, LocalDate datum, String telefon, String adresa, NivoSS nivoS, int staz) {
		Sekretar s = new Sekretar(id, ime, prezime, pol, datum, telefon, adresa, korisnickoIme, lozinka, nivoS, staz, 0, LocalDate.now());
		um.getKorisnici().add(s);
		um.getSekretari().add(s);
		um.saveData();
	}
	
	public void editSekretar(int id, String korisnickoIme, String ime, String prezime, String pol, LocalDate datum, String telefon, String adresa, NivoSS nivoS, int staz, double bonus) {
		Sekretar s = this.nadjiSekretaraPoId(id);
		s.setKorisnickoIme(korisnickoIme);
		s.setIme(ime);
		s.setPrezime(prezime);
		s.setPol(pol);
		s.setAdresa(adresa);
		s.setBrTelefona(telefon);
		s.setDatumRodjenja(datum);
		s.setBonus(bonus);
		s.setStaz(staz);
		s.setStrucnaS(nivoS);
		s.setPlata();
		um.saveData();
	}
	
	public void delSekretar(Sekretar s) {
		um.getSekretari().remove(s);
		um.getKorisnici().remove(s);
		um.saveData();
	}
	
	public boolean promeniLozinku(Sekretar s, String stara, String nova, String opet) {
		if(!(s.getLozinka().equals(stara))) {
			return false;
		}else {
			s.setLozinka(opet);
			um.saveData();
			return true;
		}
	}

}
