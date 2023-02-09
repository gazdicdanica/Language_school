package managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import entity.Administrator;
import entity.Jezik;
import entity.Kurs;
import entity.Predavac;
import entity.Sekretar;
import entity.Test;
import entity.Ucenik;
import entity.Zahtev;
import entity.Zahtev.stanje;

public class UserManager {
	private String file;
	private UcenikManager ucenikManager;
	private SekretarManager sekretarManager;
	private PredavacManager predavacManager;
	private KursManager kursManager;
	private ZahtevManager zahtevManager;
	private TestManager testManager;
	private JezikManager jezikManager;
	private Administrator admin;
	private List<Sekretar> sekretari;
	private List<Predavac> predavaci;
	private List<Ucenik> ucenici;
	private List<Object> korisnici;
	

	public UserManager(String file, String zahtevFile, String testFile, KursManager km, JezikManager jm) {
		this.file = file;
		this.ucenikManager = new UcenikManager(this, km);
		this.sekretarManager = new SekretarManager(this);
		this.predavacManager = new PredavacManager(this, km);
		this.kursManager = km;
		this.jezikManager = jm;
		this.korisnici = new ArrayList<Object>();
		this.sekretari = new ArrayList<Sekretar>();
		this.predavaci = new ArrayList<Predavac>();
		this.ucenici = new ArrayList<Ucenik>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while((line=in.readLine())!=null) {
				line=line.trim();
				String [] tokens = line.split(";");
				switch(tokens[1]) {
					case "admin":
						Administrator a = parseAdmin(tokens);
						this.admin = a;
						this.korisnici.add(a);
						break;
					case "predavac":
						Predavac p = predavacManager.parsePredavac(tokens);
						this.predavaci.add(p);
						this.korisnici.add(p);
						break;
					case "sekretar":
						Sekretar s = sekretarManager.parseSekretar(tokens);
						this.sekretari.add(s);
						this.korisnici.add(s);
						break;
					case "ucenik":
						Ucenik u = ucenikManager.parseUcenik(tokens);
						this.ucenici.add(u);
						this.korisnici.add(u);
						break;
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.zahtevManager = new ZahtevManager(zahtevFile ,this);
		this.testManager = new TestManager(testFile, this, this.kursManager);
	}
	
	public boolean saveData() {
		try {
			PrintWriter p = new PrintWriter(new FileWriter(this.file, false));
			p.println(admin.fileString());
			for (Sekretar s : sekretari) {
				p.println(s.fileString());
			}
			for (Predavac x: predavaci) {
				p.println(x.fileString());
			}
			for(Ucenik u:ucenici) {
				p.println(u.fileString());
			}
			p.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	private Administrator parseAdmin(String[] tokens) {
		int id = Integer.parseInt(tokens[0].trim());
		String ime = tokens[2].trim();
		String prezime = tokens[3].trim();
		String pol = tokens[4].trim();
		String datum = tokens[5].trim();
		LocalDate rodjendan = LocalDate.parse(datum);
		String telefon = tokens[6].trim();
		String adresa = tokens[7].trim();
		String username = tokens[8].trim();
		String password = tokens[9].trim();
		Administrator ret = new Administrator(id, ime, prezime, pol, rodjendan, telefon, adresa, username, password);
		
		return ret;
	}
	
	public String sifra() {
		String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                					+ "0123456789"
                					+ "abcdefghijklmnopqrstuvxyz";
		
		StringBuilder sb = new StringBuilder(10);
		
		for(int i=0;i<10;i++) {
			int index = (int)(s.length()*Math.random());
			sb.append(s.charAt(index));
		}
		return sb.toString();
	}
	
	public Map<Jezik, Integer> popularnostJezika(){
		Map<Jezik, Integer> ret = new HashMap<Jezik, Integer>();
		for(Jezik j:jezikManager.getJezici()) {
			ret.put(j, 0);
		}
		for(Ucenik u:this.ucenici) {
			List<Jezik> jezici = new ArrayList<Jezik>();
			for(Kurs k:u.getKursevi()) {
				Jezik j = k.getJezik();
				if(jezici.contains(j)) {
					continue;
				}else {
					jezici.add(j);
					int br = ret.get(k.getJezik())+1;
					ret.put(k.getJezik(), br);
				}
				
			}
		}return ret;
	}
	
	public Map<LocalDate,Float> prihodi(){
		Map<LocalDate,Float> ret = new TreeMap<LocalDate,Float>();
		for(Zahtev z:getZahtevManager().getZahtevi()) {
			if(z.getStanje().equals(Zahtev.stanje.prihvacen)) {
				try {
					Float d = ret.get(z.getDatum());
					ret.put(z.getDatum(), d+getKursManager().getCenovnikManager().getCenovnik().getCene().get(z.getKurs()));
				}catch(NullPointerException n) {
					ret.put(z.getDatum(), getKursManager().getCenovnikManager().getCenovnik().getCene().get(z.getKurs()));
				}
			}
		}for(Test t:getTestManager().getTestovi()) {
			if(t.getOcenjen()) {
				try {
					Float d = ret.get(t.getDatum());
					ret.put(t.getDatum(), d+getKursManager().getCenovnikManager().getCenovnik().getCeneTesta().get(t.getKurs()));
				}catch(NullPointerException e) {
					ret.put(t.getDatum(), getKursManager().getCenovnikManager().getCenovnik().getCene().get(t.getKurs()));
				}
			}
		}return ret;
	}
	
	public int izbrojUcenike(Jezik j, String kat) {
		int ret = 0;
		int donja;
		int gornja;
		switch(kat) {
		case "5-9": donja = 5; gornja = 9; break;
		case "10-15": donja = 10; gornja = 15;break;
		case "16-19": donja = 16; gornja = 19;break;
		case "20-24":donja = 20; gornja = 24;break;
		case "25-30":donja = 25;gornja = 30;break;
		case "31-39":donja = 31; gornja = 39;break;
		default: donja = 40; gornja = 100;break;
		}
		for(Ucenik u:ucenici) {
			Period period = Period.between(u.getDatumRodjenja(),LocalDate.now());
			int godine = period.getYears();
			if(donja <= godine && godine <= gornja) {
				for(Kurs k:u.getKursevi()) { 
					if(k.getJezik().equals(j)) {
						ret+=1;
						break;
					}
				}
			}
			
		}return ret;
	}
	
	public Map<String, Double> prihodiMap(){
		Map<String, Double> ret = new LinkedHashMap<String,Double>();
		String [] meseci = {"Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar"};
		for(int i = 0;i<meseci.length;i++) {
			ret.put(meseci[i], 0.0);
		}for(Zahtev z:getZahtevManager().getZahtevi()) {
			if(z.getStanje().equals(stanje.prihvacen) && z.getDatum().getYear() == LocalDate.now().getYear() && z.getStanje().equals(Zahtev.stanje.prihvacen)) {
				int mesec = z.getDatum().getMonthValue()-1;
				double br = ret.get(meseci[mesec])+ kursManager.getCenovnikManager().getCenovnik().getCene().get(z.getKurs());
				ret.put(meseci[mesec], br);
			}
		}for(Test t:getTestManager().getTestovi()) {
			for(Ucenik u:t.getUcenici()) {
				if(t.getDatum().getYear() == 2021 && getTestManager().ponovljenTest(u, t.getDatum(), t.getKurs())) {
					int mesec = t.getDatum().getMonthValue()-1;
					double br = ret.get(meseci[mesec])+ kursManager.getCenovnikManager().getCenovnik().getCeneTesta().get(t.getKurs());
					ret.put(meseci[mesec], br);
				}
			}
		}return ret;
		
	}
	
	public Map<String, Double> rashodiMap(){
		Map<String, Double> ret = new LinkedHashMap<String, Double>();
		String [] meseci = {"Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar"};
		LocalDate danas = LocalDate.now();
		for(int i = 0;i<meseci.length;i++) {
			LocalDate m = LocalDate.of(danas.getYear(),i+1,1);
			double ukupno = 0.0;
			if(danas.getMonthValue()<i+1) {
				ret.put(meseci[i], 0.0);
			}else {
				for(Predavac p:predavaci) {
					if(p.getDatumZaposlenja().isBefore(m)){
						ukupno+=p.getPlata();
					}
				}for(Sekretar s:sekretari) {
					if(s.getDatumZaposlenja().isBefore(m)) {
						ukupno+=s.getPlata();
					}
				}
				ret.put(meseci[i], ukupno);
			}
		}return ret;
	}
	
	public Map<LocalDate,Double> rashodi(){
		Map<LocalDate,Double> ret = new TreeMap<LocalDate,Double>();
		LocalDate danas = LocalDate.now();
		for(int i=1;i<13;i++) {
			LocalDate d = LocalDate.of(danas.getYear(), i, 1);
			double ukupno = 0.0;
			if(danas.getMonthValue()<i) {
				return ret;
			}else {
				for(Sekretar s:sekretari) {
					if(s.getDatumZaposlenja().isBefore(d)) {
						ukupno+=s.getPlata();
					}
				}for(Predavac p:predavaci) {
					if(p.getDatumZaposlenja().isBefore(d))
						ukupno+=p.getPlata();
				}
			}if(ukupno == 0) {
				continue;
			}
			ret.put(d, ukupno);
		}return ret;
		
	}
	
	public Object checkLogin(String username,String password) {
		for(Object o:korisnici) {
			switch(String.valueOf(o.getClass().getSimpleName())) {
			case "Administrator":
				Administrator a = (Administrator) o;
				if (a.getKorisnickoIme().equals(username) && a.getLozinka().equals(password))
					return a;
				break;
			case "Predavac":
				Predavac p = (Predavac) o;
				if (p.getKorisnickoIme().equals(username) && p.getLozinka().equals(password))
					return p;
				break;
			case "Sekretar":
				Sekretar s = (Sekretar) o;
				if (s.getKorisnickoIme().equals(username) && s.getLozinka().equals(password))
					return s;
				break;
			case "Ucenik":
				Ucenik u = (Ucenik) o;
				if (u.getKorisnickoIme().equals(username) && u.getLozinka().equals(password))
					return u;
				break;
			}
			
		}return null;
	}

	public List<Object> getKorisnici() {
		return korisnici;
	}

	public Administrator getAdmin() {
		return admin;
	}

	public List<Sekretar> getSekretari() {
		return sekretari;
	}

	public List<Predavac> getPredavaci() {
		return predavaci;
	}

	public List<Ucenik> getUcenici() {
		return ucenici;
	}	
	
	public UcenikManager getUcenikManager() {
		return this.ucenikManager;
	}

	public SekretarManager getSekretarManager() {
		return sekretarManager;
	}

	public PredavacManager getPredavacManager() {
		return predavacManager;
	}

	public KursManager getKursManager() {
		return kursManager;
	}
	
	public ZahtevManager getZahtevManager() {
		return zahtevManager;
	}
	
	public JezikManager getJezikManager() {
		return jezikManager;
	}
	
	public TestManager getTestManager() {
		return testManager;
	}

	public void setUcenikManager(UcenikManager ucenikManager) {
		this.ucenikManager = ucenikManager;
	}

	public void setSekretarManager(SekretarManager sekretarManager) {
		this.sekretarManager = sekretarManager;
	}

	public void setPredavacManager(PredavacManager predavacManager) {
		this.predavacManager = predavacManager;
	}

	public void setKursManager(KursManager kursManager) {
		this.kursManager = kursManager;
	}

	public void setZahtevManager(ZahtevManager zahtevManager) {
		this.zahtevManager = zahtevManager;
	}

	public void setTestManager(TestManager testManager) {
	  this.testManager = testManager;
	}

	public void setJezikManager(JezikManager jezikManager) {
		this.jezikManager = jezikManager;
	}

	public void setAdmin(Administrator admin) {
	  this.admin = admin;
	}

	public void setSekretari(Sekretar s) {
		this.sekretari  = new ArrayList<Sekretar>();
		sekretari.add(s);
	}

	public void setPredavaci(Predavac p) {
		this.predavaci = new ArrayList<Predavac>();
		predavaci.add(p);
	}

	public void setUcenici(Ucenik u) {
	  this.ucenici = new ArrayList<Ucenik> ();
	  ucenici.add(u);
	}

	public void setKorisnici(List<Object> korisnici) {
		this.korisnici = korisnici;
	}
	
	
	
}
