package entity;

import java.time.LocalDate;


public class Sekretar extends Zaposleni{
	private int id;

	public Sekretar(int id, String ime, String prezime, String pol, LocalDate datumRodjenja, String brTelefona,
			String adresa, String korisnickoIme, String lozinka) {
		super(ime, prezime, pol, datumRodjenja, brTelefona, adresa, korisnickoIme, lozinka);
		this.id = id;
	}

	public Sekretar(int id, String ime, String prezime, String pol, LocalDate datumRodjenja, String brTelefona, String adresa,
			String korisnickoIme, String lozinka, NivoSS strucnaS, int staz, double bonus, LocalDate datumZaposlenja) {
		super(ime, prezime, pol, datumRodjenja, brTelefona, adresa, korisnickoIme, lozinka, strucnaS, staz, bonus, datumZaposlenja);
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
		
	public String fileString() {
		return String.valueOf(id)+ ";sekretar;" + this.getIme() + ";" + this.getPrezime() + ";" + this.getPol() + ";" + this.getDatumRodjenja()+ ";" + this.getBrTelefona() + ";" + this.getAdresa() + ";" + this.getKorisnickoIme() + ";" + this.getLozinka() + ";" + this.getStrucnaS() + ";" + this.getStaz() + ";" + this.getBonus()+ ";" +  this.getDatumZaposlenja();
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Sekretar other = (Sekretar) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.getIme() + " " + this.getPrezime();
	}
	
}
