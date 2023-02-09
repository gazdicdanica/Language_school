package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Test {
	private int id;
	private Kurs kurs;
	private List<Ucenik> ucenici;
	private LocalDate datum;
	private Predavac predavac;
	private boolean ocenjen;
	private List<RezultatTesta> rezultati;
	private double prosek;
	
	public Test(int id, Kurs k, LocalDate datum, Predavac p, boolean ocenjen) {
		this.id = id;
		this.kurs = k;
		this.datum = datum;
		this.predavac = p;
		this.ocenjen = ocenjen;
		this.ucenici = new ArrayList<Ucenik>();
		this.rezultati = new ArrayList<RezultatTesta>();
		
	}
	
	public Test(int id, Kurs k, LocalDate datum, Predavac p, boolean ocenjen, List<Ucenik> ucenici) {
		this(id, k, datum, p, ocenjen);
		this.ucenici = ucenici;
	}
	
	public Test(int id, Kurs k, LocalDate datum, Predavac p, boolean ocenjen, List<Ucenik> ucenici, List<RezultatTesta> rezultati) {
		this(id, k, datum, p, ocenjen, ucenici);
		this.rezultati = rezultati;
	}
	
	public void setPredavac(Predavac p) {
		this.predavac = null;
	}
	
	public boolean getOcenjen() {
		return this.ocenjen;
	}
	
	public void setOcenjen() {
		this.ocenjen = true;
	}
		
	public int getId() {
		return this.id;
	}

	public Kurs getKurs() {
		return kurs;
	}

	public List<Ucenik> getUcenici() {
		return ucenici;
	}
	
	public void setUcenik(Ucenik u) {
		this.ucenici.add(u);
	}

	public LocalDate getDatum() {
		return datum;
	}
	
	public void setDatum(LocalDate d) {
		this.datum = d;
	}

	public Predavac getPredavac() {
		return predavac;
	}
	
	public List<RezultatTesta> getRezultati(){
		return this.rezultati;
	}
	
	public void addRezultat(RezultatTesta t) {
		this.rezultati.add(t);
	}
	
	public double getProsek() {
		return this.prosek;
	}
	
	public void setProsek(double value) {
		this.prosek = value;
	}
	
	public String getUceniciId() {
		String ret="";
		if(this.ucenici.size()>0) {
			for(Ucenik u:ucenici) {
				ret+=String.valueOf(u.getId())+",";
			}ret = ret.substring(0,ret.length()-1);
		}else {
			ret="/";
		}return ret;
	}
	
	public String getRezultatiString() {
		String ret = "";
		if(this.rezultati.size()>0) {
			for(RezultatTesta r:rezultati) {
				ret+=r.getUcenik().getId() + "." + r.getBodovi() + ",";
			}ret = ret.substring(0, ret.length()-1);
		}else {
			ret = "/";
		}return ret;
	}
	
	public String fileString() {
		String p;
		try {
			p = "" + predavac.getId();
		}catch(NullPointerException e) {
			p = "/";
		}return String.valueOf(id)+";"+String.valueOf(kurs.getId())+";"+ this.datum +";" +p+ ";" +this.getUceniciId() + ";" +this.ocenjen + ";" + this.getRezultatiString();
	}
	
	public Object toCell(int colIndex) {
		switch(colIndex) {
		case 0: return this.id;
		case 1:return this.kurs.toString();
		case 2: return this.datum;
		case 3:return this.ucenici.size();
		case 4:return this.ocenjen;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + ((kurs == null) ? 0 : kurs.hashCode());
		result = prime * result + ((predavac == null) ? 0 : predavac.hashCode());
		result = prime * result + ((ucenici == null) ? 0 : ucenici.hashCode());
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
		Test other = (Test) obj;
		if(id != other.id)
			return false;
		return true;
	}
	
	
	
	
}
