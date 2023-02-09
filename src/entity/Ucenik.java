package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ucenik extends Osoba{
	private List<Kurs> kursevi;
	private List<Kurs> polozeno;
	private int id;
	
	public Ucenik(int id, String ime, String prezime, String pol, LocalDate datumRodjenja, String brTelefona, String adresa,
			String korisnickoIme, String lozinka) {
		super(ime, prezime, pol, datumRodjenja, brTelefona, adresa, korisnickoIme, lozinka);
		this.kursevi = new ArrayList<Kurs>();
		this.polozeno = new ArrayList<Kurs>();
		this.id = id;
	}
	
	

	public Ucenik(int id, String ime, String prezime, String pol, LocalDate datumRodjenja, String brTelefona, String adresa,
			String korisnickoIme, String lozinka, List<Kurs> kursevi) {
		this(id, ime, prezime, pol, datumRodjenja, brTelefona, adresa, korisnickoIme, lozinka);
		this.kursevi = kursevi;
	}
	
	public Ucenik(int id,String ime,String prezime,String pol,LocalDate datumRodjenja, String brTelefona,String adresa,String korisnickoIme,String lozinka,
			List<Kurs> kursevi,List<Kurs> polozeno) {
    this(id,ime, prezime, pol, datumRodjenja, brTelefona, adresa, korisnickoIme, lozinka,kursevi);
    this.polozeno = polozeno;
  }



  public List<Kurs> getKursevi() {
		return kursevi;
	}
  
  public List<Kurs> getPolozeno(){
	  return polozeno;
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
	
	public String getPolozenoId() {
		String ret = "";
		if (this.polozeno.size()>0) {
			for(Kurs k: polozeno) {
				ret+=String.valueOf(k.getId())+",";
			}
			ret = ret.substring(0,ret.length()-1);
		}else {
			ret = "/";
		}
		return ret;
	}

	public void setKursevi(List<Kurs> kursevi) {
		this.kursevi = kursevi;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Object toCell(int colIndex) {
		switch(colIndex) {
		case 0: return this.id;
		case 1: return this.getKorisnickoIme();
		case 2: return this.getIme();
		case 3: return this.getPrezime();
		case 4: return this.getPol();
		case 5:	return this.getDatumRodjenja();
		case 6: return this.getBrTelefona();
		case 7: return this.getAdresa();
		}
		return null;
		
	}

	
	public String fileString() {
		String username = this.getKorisnickoIme();
		String password = this.getLozinka();
		if (username.equals("")) {
			username = "/";
			password = "/";
		}
		return String.valueOf(id)+ ";ucenik;" + this.getIme() + ";" + this.getPrezime() + ";" + this.getPol() + ";" + this.getDatumRodjenja() + ";" + this.getBrTelefona() + ";" + this.getAdresa() + ";" + username + ";" + password + ";" + this.getKurseviId()+";"+this.getPolozenoId();
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
		Ucenik other = (Ucenik) obj;
		if (id != other.id)
			return false;
		if (kursevi == null) {
			if (other.kursevi != null)
				return false;
		} else if (!kursevi.equals(other.kursevi))
			return false;
		return true;
	}
	

	
}
