package managers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entity.Jezik;
import entity.Kurs;
import entity.Predavac;
import entity.Test;
import entity.Zaposleni;
import entity.Zaposleni.NivoSS;

public class PredavacManager {
	private UserManager um;
	private KursManager km;
	
	public PredavacManager(UserManager um, KursManager km) {
		this.um = um;
		this.km = km;
	}
	
	public Predavac parsePredavac(String[]tokens) {
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
		String kursevi = tokens[13].trim();
		String jezici = tokens[14].trim();
		LocalDate datumZaposlenja = LocalDate.parse(tokens[15].trim());
		Predavac ret;
		List<Jezik> j = new ArrayList<Jezik>();
		if(!jezici.equals("/")) {
			String[] jeziciId = jezici.split(",");
			for(int i=0;i<jeziciId.length;i++) {
				int jid = Integer.parseInt(jeziciId[i]);
				Jezik jezik = km.getJezikManager().nadjiJezikPoId(jid);
				j.add(jezik);
			}
		}
		
		if(!kursevi.equals("/")) {
			List<Kurs> k = new ArrayList<Kurs>();
			String[] kurseviId = kursevi.split(",");
			for(int i=0;i<kurseviId.length;i++) {
				int kid = Integer.parseInt(kurseviId[i]);
				Kurs kurs = km.nadjiKursPoId(kid);
				k.add(kurs);
			}
			ret = new Predavac(id, ime, prezime, pol, rodjendan, telefon, adresa,username, password, ss, staz, bonus, datumZaposlenja, k, j);
		}else {
			ret = new Predavac(id, ime, prezime, pol, rodjendan, telefon, adresa, username, password, ss, staz, bonus, datumZaposlenja, j);
		}
		return ret;
	}

	
	public Predavac nadjiPredavacPoId(int id) {
		List<Predavac> p = um.getPredavaci();
		Predavac ret = null;
		for(int i = 0; i<p.size();i++) {
			Predavac pr = p.get(i);
			if(pr.getId() == id) {
				ret = pr;
				break;
			}
		}return ret;
	}
	
	public void addPredavac(int id, String korisnickoIme, String lozinka, String ime, String prezime, String pol, LocalDate datum, String telefon, String adresa, NivoSS nivoS, int staz, List<Jezik> jezici) {
		Predavac p = new Predavac(id, ime, prezime, pol, datum, telefon, adresa, korisnickoIme, lozinka, nivoS, staz, 0, LocalDate.now(), jezici);
		um.getKorisnici().add(p);
		um.getPredavaci().add(p);
		um.saveData();
	}
	
	public void editPredavac(int id, String korisnickoIme, String ime, String prezime, String pol, LocalDate datumRodjenja, String telefon, String adresa, NivoSS nivoS, int staz, double bonus) {
		Predavac p = this.nadjiPredavacPoId(id);
		p.setKorisnickoIme(korisnickoIme);
		p.setIme(ime);
		p.setPrezime(prezime);
		p.setPol(pol);
		p.setAdresa(adresa);
		p.setBrTelefona(telefon);
		p.setDatumRodjenja(datumRodjenja);
		p.setBonus(bonus);
		p.setStaz(staz);
		p.setStrucnaS(nivoS);
		p.setPlata();
		um.saveData();
	}

	
	public void delPredavac(Predavac p) {
		for(Test t:um.getTestManager().getTestovi()) {
			if(t.getPredavac().equals(p)&&t.getOcenjen()==true) {
				t.setPredavac(null);
			}
		}
		um.getTestManager().saveData();
		um.getPredavaci().remove(p);
		um.getKorisnici().remove(p);
		um.saveData();
	}
	
	public List<Kurs> getMoguciKursevi(Predavac p){
		List<Kurs> ret = new ArrayList<Kurs>();
		for(Kurs k:km.getKursevi()) {
			for(Jezik j:p.getJezici()) {
				if(k.getJezik().equals(j)) {
					ret.add(k);
				}
			}
		}return ret;
	}
	
	public void removeJezik(Jezik j) {
		for(Predavac p:um.getPredavaci()) {
			Iterator<Jezik> itr = p.getJezici().iterator();
			while(itr.hasNext()) {
				Jezik jezik = itr.next();
				if(jezik.getId() == j.getId()) {
					itr.remove();
					break;
				}
			}
		}um.saveData();
	}
	
	public List<Predavac> getPredavaciKurs(Kurs kurs){
		List<Predavac> ret = new ArrayList<Predavac>();
		for(Predavac p:um.getPredavaci()) {
			for(Kurs k:p.getKursevi()) {
				if(k.getId() == kurs.getId()) {
					ret.add(p);
				}
			}
		}return ret;
	}
	
	public List<Predavac> getMoguciPredavaci(Kurs k){
		List<Predavac> ret = new ArrayList<Predavac>();
		for(Predavac p:um.getPredavaci()) {
			for(Jezik j:p.getJezici()) {
				if(j.equals(k.getJezik()) && (!p.getKursevi().contains(k))) {
					ret.add(p);
				}
			}
		}return ret;
	}
	
	public void delKurs(Kurs k) {
		for(Predavac p:um.getPredavaci()) {
			if(p.getKursevi().contains(k)) {
				p.getKursevi().remove(k);
			}
		}um.saveData();
	}
	
	public boolean promeniLozinku(Predavac p, String stara, String nova, String ponovo) {
		if(!(p.getLozinka().equals(stara))) {
			return false;
		}else {
			p.setLozinka(ponovo);
			um.saveData();
			return true;
		}
	}
	
	public Kurs addKurs(Kurs k, Predavac p) {
		if(p.getKursevi().size()>0) {
			for(Kurs kurs:p.getKursevi()) {
				if(k.equals(kurs))
					return k;
				else 
					continue;
			}p.getKursevi().add(k);
		}else
			p.getKursevi().add(k);
		return null;
	}
	
	public Jezik addJezik(Jezik j, Predavac p) {
		if(p.getJezici().size()>0) {
			for(Jezik jezik:p.getJezici()) {
				if(j.equals(jezik)) {
					return j;
				}else
					continue;
			}
		}p.getJezici().add(j);
		return null;
	}
}
