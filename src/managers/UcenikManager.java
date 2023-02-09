package managers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entity.Kurs;
import entity.RezultatTesta;
import entity.Test;
import entity.Ucenik;
import entity.Zahtev;

public class UcenikManager {
	private UserManager um;
	private KursManager km;
	
	public UcenikManager(UserManager um, KursManager km) {
		this.um = um;
		this.km = km;
	}
	
	public Ucenik nadjiUcenikaPoId(int id) {
		List<Ucenik> ucenici = um.getUcenici();
		Ucenik ret = null;
		for(int i =0;i<ucenici.size();i++) {
			Ucenik u = ucenici.get(i);
			if(u.getId() == id) {
				ret = u;
				break;
			}
		}
		return ret;
	}
	
	public Ucenik parseUcenik(String[] tokens) {
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
		if(username.equals("/")) {
			username = "";
			password = "";
		}
		String kursevi = tokens[10].trim();
		String polozeno = tokens[11].trim();
		Ucenik ret;
		if(kursevi.equals("/")) {
			ret = new Ucenik(id, ime, prezime, pol, rodjendan, telefon, adresa, username, password);	
		}else {
			List<Kurs> k = new ArrayList<Kurs>();
			String kursId[] = kursevi.split(",");
			for(int i=0;i<kursId.length;i++) {
				int kid = Integer.parseInt(kursId[i]);
				Kurs kurs = km.nadjiKursPoId(kid);
				k.add(kurs);
			}
			if(polozeno.equals("/")) {
				ret = new Ucenik(id, ime, prezime, pol, rodjendan, telefon, adresa, username, password, k);
			}else {
				List<Kurs> p = new ArrayList<Kurs>();
				String polID [] = polozeno.split(",");
				for(int i=0;i<polID.length;i++) {
					int pid = Integer.parseInt(polID[i]);
					Kurs kurs = km.nadjiKursPoId(pid);
					p.add(kurs);
				}
				ret = new Ucenik(id,ime,prezime,pol,rodjendan,telefon,adresa,username,password,k,p);
			}
		}
		return ret;
	}
	
	
	public void addUcenik(int id, String ime, String prezime, String pol, LocalDate datumRodjenja, String telefon, String adresa, Kurs k) {
		Ucenik u = new Ucenik(id, ime, prezime, pol, datumRodjenja, telefon, adresa, "", "");
		um.getZahtevManager().addZahtev(u, k);
		um.getKorisnici().add(u);
		um.getUcenici().add(u);
		um.saveData();
		
	}
	
	public void editUcenik(int id, String korisnickoIme, String ime, String prezime, String pol, LocalDate datumRodjenja, String telefon, String adresa) {
		Ucenik u = this.nadjiUcenikaPoId(id);
		u.setKorisnickoIme(korisnickoIme);
		u.setIme(ime);
		u.setPrezime(prezime);
		u.setPol(pol);
		u.setAdresa(adresa);
		u.setBrTelefona(telefon);
		u.setDatumRodjenja(datumRodjenja);
		um.saveData();
	}
	
	public void delUcenik(Ucenik u) {
		for(Test t:um.getTestManager().getTestovi()) {
			if(t.getUcenici().contains(u)) {
				t.getUcenici().remove(u);
				Iterator<RezultatTesta> itr = t.getRezultati().iterator();
				while(itr.hasNext()) {
					RezultatTesta r = itr.next();
					if(r.getUcenik().equals(u)) {
						itr.remove();
						break;
					}
				}
			}
		}
		um.getUcenici().remove(u);
		um.getKorisnici().remove(u);
		um.saveData();
		um.getTestManager().saveData();
	}
	
	public List<Kurs> nadjiMoguceKurseve(Ucenik u){
		List<Kurs> ret = new ArrayList<Kurs>();
		boolean found = false;
		for(Kurs a:km.getKursevi()) {
			for(Kurs b:u.getKursevi()) {
				if(a.equals(b)) {
					found = true;
					break;
				}
			}if(!found) {
				ret.add(a);
			}else {
				found = false;
				continue;
			}
		}return ret;
	}
	
	public List<Kurs> preporuke(Ucenik u){
		List<Kurs> ret = new ArrayList<Kurs>();
		for(Kurs k:u.getPolozeno()) {
			for(Kurs kurs:um.getKursManager().getKursevi()) {
				if(k.getJezik().equals(kurs.getJezik()) && !k.equals(kurs)) {
					ret.add(kurs);
				}
			}
		}return ret;
	}
	
	public boolean pao(Ucenik u, Kurs k) {
		for(Test t:um.getTestManager().getTestovi()) {
			if(t.getKurs().equals(k) && !u.getPolozeno().contains(k)) {
				for(RezultatTesta r:t.getRezultati()) {
					if(r.getUcenik().equals(u))
						return true;
				}
			}
		}return false;
	}
	
	
	public String napraviKIme(Ucenik u) {
		return u.getIme().toLowerCase()+u.getPrezime().toLowerCase()+String.valueOf(u.getId());
		
	}
	
	public boolean promeniLozinku(Ucenik ucenik, String stara, String nova) {
		if(!(ucenik.getLozinka().equals(stara))) {
			return false;
		}else {
			ucenik.setLozinka(nova);
			um.saveData();
			return true;
		}
	}
	
	public boolean addKurs(Kurs k,Ucenik u) {
		if(u.getKursevi().size()>0) {
			for(Kurs kurs:u.getKursevi()) {
				if(kurs.equals(k))
					return false;
				else
					continue;
			}
		}for(Zahtev z:um.getZahtevManager().getZahtevi()) {
			if(z.getUcenik().equals(u) && z.getKurs().equals(k)) {
				return false;
			}else
				continue;
		}
		return true;
	}

}
