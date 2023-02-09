package entity;

import java.time.LocalDate;

public class Administrator extends Osoba{
	private int id;

	public Administrator(int id, String ime, String prezime, String pol, LocalDate datumRodjenja, String brTelefona,
			String adresa, String korisnickoIme, String lozinka) {
		super(ime, prezime, pol, datumRodjenja, brTelefona, adresa, korisnickoIme, lozinka);
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String fileString() {
		return String.valueOf(id)+ ";admin;" + this.getIme() + ";" + this.getPrezime() + ";" + this.getPol() + ";" + this.getDatumRodjenja() + ";" + this.getBrTelefona() + ";" + this.getAdresa() + ";" + this.getKorisnickoIme() + ";" + this.getLozinka() ;
	}

}
