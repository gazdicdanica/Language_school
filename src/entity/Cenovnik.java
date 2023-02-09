package entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Cenovnik {
	private int id;
	private LocalDate vaziOd;
	private LocalDate vaziDo;
	private Map<Kurs, Float> cene = new HashMap<Kurs,Float>();
	private Map<Kurs, Float> ceneTesta = new HashMap<Kurs,Float>();
	private Map<Kurs, Float> popusti = new HashMap<Kurs,Float>();
	
	public Cenovnik(int id, LocalDate vaziOd,LocalDate vaziDo) {
		this.id = id;
		this.vaziDo = vaziDo;
		this.vaziOd = vaziOd;
	}
	
	public Cenovnik(int id, LocalDate vaziOd, LocalDate vaziDo, Map<Kurs,Float> cene, Map<Kurs,Float> ceneTesta) {
		this(id,vaziOd,vaziDo);
		this.cene = cene;
		this.ceneTesta = ceneTesta;
		
		Iterator<Map.Entry<Kurs, Float>> itr = cene.entrySet().iterator();
		
		while(itr.hasNext()) {
			Map.Entry<Kurs, Float> pair = itr.next();
			popusti.put(pair.getKey(), pair.getValue()*0.9f);
		}
	}

	public int getId() {
		return id;
	}

	public LocalDate getVaziOd() {
		 return vaziOd;
	}

	public LocalDate getVaziDo() {
		return vaziDo;
	}

	public Map<Kurs,Float> getCene() {
		return cene;
	}

	public Map<Kurs,Float> getPopusti() {
		return popusti;
	}
	
	public Map<Kurs, Float> getCeneTesta(){
		return this.ceneTesta;
	}
	
	public void setVaziOd(LocalDate value) {
		this.vaziOd = value;
	}
	
	public void setVaziDo(LocalDate value) {
		this.vaziDo = value;
	}
	
	public String getCeneString() {
		String ret = "";
		Iterator<Map.Entry<Kurs, Float>> itr = cene.entrySet().iterator();
		
		while(itr.hasNext()) {
			Map.Entry<Kurs, Float> pair = itr.next();
			ret+=pair.getKey().getId()+"-"+pair.getValue()+",";
		}try{
			ret = ret.substring(0,ret.length()-1);
		}catch(StringIndexOutOfBoundsException e) {
			return null;
		}
		return ret;
	}
	
	public String getCeneTestaString() {
		String ret = "";
		Iterator<Map.Entry<Kurs, Float>> itr = ceneTesta.entrySet().iterator();
		
		while(itr.hasNext()) {
			Map.Entry<Kurs, Float> pair = itr.next();
			ret+=pair.getKey().getId()+"-"+pair.getValue()+",";
		}try{
			ret = ret.substring(0,ret.length()-1);
		}catch(StringIndexOutOfBoundsException e) {
			return null;
		}
		return ret;
	}
	
	public String fileString() {
		return this.id + ";" + this.vaziOd + ";" + this.vaziDo + ";" +getCeneString()+";"+getCeneTestaString();
	}
	
}
