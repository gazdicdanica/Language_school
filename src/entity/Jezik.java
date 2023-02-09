package entity;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Jezik {
	private int id;
	private String jezik;
	private ImageIcon icon;
	
	public Jezik(int id, String naziv) {
		this.id = id;
		this.jezik = naziv;
		try {
			this.icon = new ImageIcon("./img/" + jezik + ".png");
	
		}catch (Exception e) {
			this.icon = null;
		}
	}

	public int getId() {
		return id;
	}

	public String getJezik() {
		return jezik;
	}
	
	public ImageIcon getIcon() {
		return icon;
	}
	
	@Override
	public String toString() {
		return this.getJezik();
	}
	
	public String fileString() {
		return String.valueOf(this.id) + ";" + this.jezik;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result + id;
		result = prime * result + ((jezik == null) ? 0 : jezik.hashCode());
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
		Jezik other = (Jezik) obj;
		if (id != other.id)
			return false;
		if (jezik == null) {
			if (other.jezik != null)
				return false;
		} else if (!jezik.equals(other.jezik))
			return false;
		return true;
	}	
	

}
