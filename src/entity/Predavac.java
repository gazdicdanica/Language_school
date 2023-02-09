package entity;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class Predavac extends Zaposleni{

	private int id;
	private List<Kurs> kursevi = new ArrayList<Kurs>();
	private List<Jezik> jezici = new ArrayList<Jezik>();
	
	
	public Predavac(int id, String ime, String prezime, String pol, LocalDate datumRodjenja, String brTelefona, String adresa,
			String korisnickoIme, String lozinka, NivoSS strucnaS, int staz, double bonus, LocalDate datumZaposlenja) {
		super(ime, prezime, pol, datumRodjenja, brTelefona, adresa, korisnickoIme, lozinka, strucnaS, staz, bonus, datumZaposlenja);
		this.id = id;
	}
	
	public Predavac(int id, String ime, String prezime, String pol, LocalDate datumRodjenja, String brTelefona, String adresa,
			String korisnickoIme, String lozinka, NivoSS strucnaS, int staz, double bonus, LocalDate datumZaposlenja, List<Kurs> kursevi, List<Jezik> jezici) {
		this(id, ime, prezime, pol, datumRodjenja, brTelefona, adresa, korisnickoIme, lozinka, strucnaS, staz, bonus, datumZaposlenja);
		this.kursevi = kursevi;
		this.jezici = jezici;
	}

	public Predavac(int id, String ime, String prezime, String pol, LocalDate datumRodjenja, String brTelefona, String adresa,
			String korisnickoIme, String lozinka, NivoSS strucnaS, int staz, double bonus, LocalDate datumZaposlenja, List<Jezik> jezici) {
		this(id, ime, prezime, pol, datumRodjenja, brTelefona, adresa, korisnickoIme, lozinka, strucnaS, staz, bonus, datumZaposlenja);
		this.jezici = jezici;
	}
	
	

	public List<Kurs> getKursevi() {
		return kursevi;
	}

	public void setKursevi(List<Kurs> kursevi) {
		this.kursevi = kursevi;
	}

	public List<Jezik> getJezici() {
		return jezici;
	}

	public void setJezici(Jezik j) {
		this.jezici.add(j);
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getKurseviId() {
		String ret = "";
		if (this.kursevi.size()>0) {
			for(Kurs k: kursevi) {
				ret+=String.valueOf(k.getId())+",";
			}
			ret = ret.substring(0,ret.length()-1);
		}else {
			ret = "/";
		}
		return ret;
	}
	
	public String getJeziciId() {
		String ret = "";
		if (this.jezici.size()>0) {
			for(Jezik j: jezici) {
				ret+=String.valueOf(j.getId())+",";
			}
			ret = ret.substring(0,ret.length()-1);
		}else {
			ret = "/";
		}
		return ret;
	}
	
	public String fileString() {
		return String.valueOf(id)+ ";predavac;" + this.getIme() + ";" + this.getPrezime() + ";" + this.getPol() + ";" + this.getDatumRodjenja() + ";" + this.getBrTelefona() + ";" + this.getAdresa() 
		+ ";" + this.getKorisnickoIme() + ";" + this.getLozinka() + ";" + this.getStrucnaS() + ";" + this.getStaz() + ";" + this.getBonus() + ";" 
		+ this.getKurseviId() + ";" + this.getJeziciId() + ";" + this.getDatumZaposlenja();
	}
		
	public void dodajKurs(Kurs k) {
		this.kursevi.add(k);
	}
	
	public Object toCell(int columIndex) {
		switch(columIndex) {
		case 0: return this.id;
		case 1: return this.getKorisnickoIme();
		case 2: return this.getIme();
		case 3: return this.getPrezime();
		case 4: return this.getPol();
		case 5: return this.getDatumRodjenja();
		case 6: return this.getBrTelefona();
		case 7: return this.getAdresa();
		case 8: return this.getPlata();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return this.getIme() + " " + this.getPrezime();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((jezici == null) ? 0 : jezici.hashCode());
		result = prime * result + ((kursevi == null) ? 0 : kursevi.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Predavac other = (Predavac) obj;
		if (id != other.id)
			return false;
		if (jezici == null) {
			if (other.jezici != null)
				return false;
		} else if (!jezici.equals(other.jezici))
			return false;
		if (kursevi == null) {
			if (other.kursevi != null)
				return false;
		} else if (!kursevi.equals(other.kursevi))
			return false;
		return true;
	}
	
	
}
