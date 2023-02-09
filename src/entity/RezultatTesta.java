package entity;

public class RezultatTesta {
	private Ucenik u;
	private int bodovi;
	private int ocena;
	
	public RezultatTesta() {
		
	}
	
	public RezultatTesta(Ucenik u) {
		this.u = u;
	}
	
	public RezultatTesta(Ucenik u, int bodovi) {
		this(u);
		this.setBodovi(bodovi);
	}

	public Ucenik getUcenik() {
		return u;
	}

	public int getBodovi() {
		return bodovi;
	}

	public int getOcena() {
		return ocena;
	}
	
	public Object tabelaOcena() {
		if(ocena>5) {
			return ocena;
		}else if(ocena == 5)
			return "nije položio/la";
		else
			return "";
	}

	public void setBodovi(int bodovi) {
		this.bodovi = bodovi;
		if(bodovi >=91) {
			this.ocena = 10;
		}else if(bodovi>=81) {
			this.ocena = 9;
		}else if(bodovi >= 71) {
			this.ocena = 8;
		}else if(bodovi >= 61) {
			this.ocena = 7;
		}else if(bodovi >= 51) {
			this.ocena = 6;
		}else
			this.ocena = 5;
	}
	

}
