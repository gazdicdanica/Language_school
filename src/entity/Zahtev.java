package entity;

import java.time.LocalDate;

public class Zahtev {
	public enum stanje{
		kreiran, uObradi, prihvacen, odbijen
	}
	
	private stanje s;
	private Ucenik ucenik;
	private Kurs kurs;
	private int id;
	private Sekretar sekretar;
	private LocalDate datum;
	
	public Zahtev(int id, Ucenik u, Kurs k) {
		this.id = id;
		this.ucenik = u;
		this.kurs = k;
		this.s = stanje.kreiran;
	}
	
	public Zahtev(int id, Ucenik u, Kurs k, stanje s) {
		this(id, u, k);
		this.s = s;
	}
	
	public Zahtev(int id, Ucenik u, Kurs k, stanje s, Sekretar sekretar, LocalDate datum) {
		this(id, u, k, s);
		this.sekretar = sekretar;
		this.datum = datum;
	}
	
	
	public int getId() {
		return this.id;
	}
	
	public Ucenik getUcenik() {
		return this.ucenik;
	}
	
	public Kurs getKurs() {
		return this.kurs;
	}
	
	public stanje getStanje() {
		return this.s;
	}
	
	public void obradiZahtev() {
		this.s = stanje.uObradi;
	}
	
	public void prihvatiZahtev() {
		this.s = stanje.prihvacen;
	}
	
	public void odbijZahtev() {
		this.s = stanje.odbijen;
	}
	
	public Sekretar getSekretar() {
		return this.sekretar;
	}
	
	public void setSekretar(Sekretar s) {
		this.sekretar = s;
	}
	
	public LocalDate getDatum() {
		return this.datum;
	}
	
	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}
	

	public String fileString() {
		String str;
		String datumStr;
		if(this.sekretar == null) {
			str="/";
			datumStr = "/";
		}else {
			str = String.valueOf(this.sekretar.getId());
			datumStr = datum.toString();
		}
		return String.valueOf(this.id) + ";" + String.valueOf(this.ucenik.getId())+";"+String.valueOf(this.kurs.getId())+";"+this.s + ";" + str+";"+datumStr;
	}
	
	public Object toCell(int col) {
		switch(col){
		case 0:return this.id;
		case 1: return this.ucenik.toString();
		case 2: return this.kurs.toString();
		default: return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + id;
		result = prime * result + ((kurs == null) ? 0 : kurs.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		result = prime * result + ((sekretar == null) ? 0 : sekretar.hashCode());
		result = prime * result + ((ucenik == null) ? 0 : ucenik.hashCode());
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
		Zahtev other = (Zahtev) obj;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (id != other.id)
			return false;
		if (kurs == null) {
			if (other.kurs != null)
				return false;
		} else if (!kurs.equals(other.kurs))
			return false;
		if (s != other.s)
			return false;
		if (sekretar == null) {
			if (other.sekretar != null)
				return false;
		} else if (!sekretar.equals(other.sekretar))
			return false;
		if (ucenik == null) {
			if (other.ucenik != null)
				return false;
		} else if (!ucenik.equals(other.ucenik))
			return false;
		return true;
	}
	
}
