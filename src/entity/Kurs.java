package entity;

public class Kurs {
	private int id;
	private String naziv;
	private Jezik jezik;
	
	public String getNaziv() {
		return naziv;
	}
	
	public Jezik getJezik() {
		return jezik;
	}
	
	public int getId() {
		return this.id;
	}
	
	
	public Kurs(int id, String naziv, Jezik jezik) {
		this.id = id;
		this.naziv = naziv;
		this.jezik = jezik;
	}
			
	public String fileString() {
		return String.valueOf(this.id)+";"+this.naziv+";"+String.valueOf(this.jezik.getId());
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	@Override
	public String toString() {
		return this.jezik.getJezik() + " - " + this.naziv;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kurs other = (Kurs) obj;
		if (id != other.id)
			return false;
		if (jezik == null) {
			if (other.jezik != null)
				return false;
		} else if (!jezik.equals(other.jezik))
			return false;
		if (naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!naziv.equals(other.naziv))
			return false;
		return true;
	}

	

}
