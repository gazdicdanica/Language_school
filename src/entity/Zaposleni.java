package entity;

import java.time.LocalDate;

public abstract class Zaposleni extends Osoba{
	
	public enum NivoSS{
		SO, SS, OAS, MAS, DR;
		
		private String[] s = {"Specijalističko obrazovanje", "Strukovne studije", "Osnovne akademske studije",
				"Master akademske studije", "Doktorske studije"};
		
		@Override
		public String toString() {
			return this.s[this.ordinal()];
		}
		
		
		public static NivoSS fromString(String text) {
		       for (NivoSS n : NivoSS.values()) {
		           if (n.toString().equalsIgnoreCase(text)) {
		               return n;
		           }
		       }
		       return null;
		}
	}
	
	private NivoSS strucnaS;
	private int staz;
	private double bonus;
	private double plata;
	private LocalDate datumZaposlenja;
	
	protected Zaposleni(String ime, String prezime, String pol, LocalDate datumRodjenja, String brTelefona,
			String adresa, String korisnickoIme, String lozinka) {
		super(ime, prezime, pol, datumRodjenja, brTelefona, adresa, korisnickoIme, lozinka);
	}

	public Zaposleni(String ime, String prezime, String pol, LocalDate datumRodjenja, String brTelefona, String adresa,
			String korisnickoIme, String lozinka, NivoSS strucnaS, int staz, double bonus, LocalDate datumZaposlenja) {
		super(ime, prezime, pol, datumRodjenja, brTelefona, adresa, korisnickoIme, lozinka);
		this.strucnaS = strucnaS;
		this.staz = staz;
		this.bonus = bonus;
		setPlata();
		this.datumZaposlenja = datumZaposlenja;
	}


	public void setPlata() {
		double osnova = 40000;
		switch(this.strucnaS) {
		case SO: 
			this.plata = osnova*1.2+staz*500+bonus;
			break;
		case SS:
			this.plata = osnova*1.4+staz*500+bonus;
			break;
		case OAS:
			this.plata = osnova*1.6+staz*500+bonus;
			break;
		case MAS:
			this.plata = osnova*1.8+staz*500+bonus;
			break;
		case DR:
			this.plata = osnova*2+staz*500+bonus;
		
		}
	}
	
	public LocalDate getDatumZaposlenja() {
		return this.datumZaposlenja;
	}

	public NivoSS getStrucnaS() {
		return strucnaS;
	}

	public void setStrucnaS(NivoSS strucnaS) {
		this.strucnaS = strucnaS;
	}

	public int getStaz() {
		return staz;
	}

	public void setStaz(int staz) {
		this.staz = staz;
	}

	public double getBonus() {
		return bonus;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}

	public double getPlata() {
		return plata;
	}

	public void setPlata(double plata) {
		this.plata = plata;
	}
	
	

}
